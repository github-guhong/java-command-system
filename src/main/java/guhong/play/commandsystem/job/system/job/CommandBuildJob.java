package guhong.play.commandsystem.job.system.job;

import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.constant.Constant;
import guhong.play.commandsystem.dto.entity.Command;
import guhong.play.commandsystem.dto.entity.CommandConfig;
import guhong.play.commandsystem.job.CommandJob;
import guhong.play.commandsystem.job.system.entity.SystemCommandConfig;
import lombok.Data;

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
        SystemCommandConfig systemCommandConfig = new SystemCommandConfig("build", "用于重新构建项目，暂不可用");
        systemCommandConfig.setIntroduce("file:"+Constant.DOCUMENT_PATH+ "/system/Build.txt");
        return systemCommandConfig;
    }

    /**
     * 开始执行任务
     *
     * @param command 命令对象
     */
    @Override
    public void run(Command command) {
        String buildProjectPath = command.getFirstValue();
        if (StrUtil.isBlank(buildProjectPath)) {
            buildProjectPath = Constant.PROJECT_PATH;
        }

        // 进到指定目录
        // 先clean
        // 执行assembly的打包命令，如果没有则报错
        // 把打包好的jar复制到start目录，如果目录不存在则创建
        // 检查是否存在启动脚本，如果不存在则创建


    }
}
