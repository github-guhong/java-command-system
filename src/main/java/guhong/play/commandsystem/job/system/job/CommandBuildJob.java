package guhong.play.commandsystem.job.system.job;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.constant.Constant;
import guhong.play.commandsystem.dto.entity.Command;
import guhong.play.commandsystem.dto.entity.CommandConfig;
import guhong.play.commandsystem.job.CommandJob;
import guhong.play.commandsystem.job.system.SystemCommandConfig;
import guhong.play.commandsystem.util.FileOperationUtil;
import guhong.play.commandsystem.util.ToolUtil;
import guhong.play.commandsystem.util.XmlOperationUtil;
import guhong.play.commandsystem.util.windows.CmdUtil;
import lombok.Data;

import java.io.File;
import java.util.Map;

/**
 * 构建工作，重新构建项目
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class CommandBuildJob implements CommandJob {

    /**
     * 获得命令的配置
     *
     * @return 返回命令的配置
     */
    @Override
    public CommandConfig getCommandConfig() {
        SystemCommandConfig systemCommandConfig = new SystemCommandConfig("build", "用于重新构建项目");
        Map<String, Boolean> paramConfig = CollectionUtil.newHashMap();
        paramConfig.put("-p", true);
        systemCommandConfig.setParamConfig(paramConfig);
        systemCommandConfig.setFileIntroduce("build.txt");
        return systemCommandConfig;
    }

    /**
     * 开始执行任务
     *
     * @param command 命令对象
     */
    @Override
    public void run(Command command) {
        String buildProjectPath = command.getParamValue("-p");
        if (StrUtil.isBlank(buildProjectPath)) {
            buildProjectPath = Constant.PROJECT_PATH;
        }
        String version = XmlOperationUtil.getMavenVersion(buildProjectPath);

        // 文件检查
        String projectName = buildProjectPath.substring(buildProjectPath.lastIndexOf(File.separator)+1);
        File startDir = FileOperationUtil.createDir(buildProjectPath + "/start");
        File startLibDir = FileOperationUtil.createDir(buildProjectPath + "/start/lib");
        FileOperationUtil.createFile(buildProjectPath + "/start/start.bat", getStartBatModel(projectName, version));


        // 开始打包
        System.out.println("开始打包。。。");
        String commandModel = buildCommand(buildProjectPath);
        Process process = CmdUtil.exec(commandModel);
        CmdUtil.printProcess(process);

        if (CmdUtil.isSuccess(process)) {
            // 复制jar文件
            String jarFilePath = buildProjectPath + "/target/"+projectName + "-" + version + "-jar-with-dependencies.jar";
            FileUtil.copy(jarFilePath, startLibDir.getPath() , true);

            // 复制config
            String configPath = buildProjectPath + "/config";
            if (FileUtil.exist(configPath)) {
                FileUtil.copy(configPath, startDir.getPath() , true);
            }

            // 复制document
            String documentPath = buildProjectPath + "/document";
            if (FileUtil.exist(documentPath)) {
                FileUtil.copy(documentPath, startDir.getPath() , true);
            }
            System.out.println("打包完成！");
        }

    }


    /**
     * 获得start.bat脚本的模板
     * @return 返回模板
     */
    private String getStartBatModel(String projectName,String version) {
        return "java -jar ./lib/"+projectName+"-"+version+"-jar-with-dependencies.jar\n" +
                "\n" +
                "exit\n";
    }

    /**
     * 构建命令
     * @param buildProjectPath 打包的项目地址
     * @return 返回构件号的命令
     */
    private String buildCommand(String buildProjectPath) {
        // 进到指定目录
        // 执行clean assembly的打包命令
        String drive = ToolUtil.getDrive(buildProjectPath);
        return drive + ": &" +
                "cd " + buildProjectPath + " & call mvn clean assembly:assembly -Dmaven.test.skip=true"  ;
    }
}
