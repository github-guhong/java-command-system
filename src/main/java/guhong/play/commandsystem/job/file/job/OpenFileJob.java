package guhong.play.commandsystem.job.file.job;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.dto.entity.Command;
import guhong.play.commandsystem.dto.entity.CommandConfig;
import guhong.play.commandsystem.job.CommandJob;
import guhong.play.commandsystem.job.file.FileCommandConfig;
import guhong.play.commandsystem.job.file.FileIndexManage;
import guhong.play.commandsystem.util.print.PrintUtil;
import guhong.play.commandsystem.util.windows.CmdUtil;
import lombok.Data;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 打开文件/目录命令
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class OpenFileJob implements CommandJob {


    private FileIndexManage fileIndexManage = new FileIndexManage();


    /**
     * 获得命令的配置
     *
     * @return 返回命令的配置
     */
    @Override
    public CommandConfig getCommandConfig() {
        FileCommandConfig commandConfig = new FileCommandConfig("of","帮你快速打开文件");
        Map<String, Boolean> paramConfig = CollectionUtil.newHashMap();
        paramConfig.put("reload", true);
        paramConfig.put("-i", true);
        paramConfig.put("-s", true);
        commandConfig.setFileIntroduce("of.txt");
        commandConfig.setParamConfig(paramConfig);
        return commandConfig;
    }

    /**
     * 开始执行任务
     *
     * @param command 命令对象
     */
    @Override
    public void run(Command command) {

        String ignoreValue = command.getParamValue("-i");
        if (null != ignoreValue) {
            fileIndexManage.ignore(ignoreValue);
        }
        String directoryValue = command.getParamValue("-s");
        if (null != directoryValue) {
            fileIndexManage.addDirectory(directoryValue);
        }
        String reloadValue = command.getParamValue("reload");
        if (null != reloadValue) {
            fileIndexManage.reload(reloadValue);
        }


        String fileName = command.getFirstValue();
        if (null == fileName) {
            return;
        }

        List<FileIndexManage.FileIndex> fileIndexList = fileIndexManage.get(fileName);
        if (CollectionUtil.isEmpty(fileIndexList)) {
            PrintUtil.println("没有找到["+fileName+"]。如果是新文件请使用[of reload]重新加载。");
            return;
        }

        String openIndexStr = command.getSecondValue();
        int openIndex = 0;
        if (null != openIndexStr) {
            openIndex = Integer.parseInt(openIndexStr) - 1;
        }
        // 如果找到不止一个文件，则打印出来进行选择
        int fileCount = fileIndexList.size();
        if (fileCount > 1) {
            PrintUtil.println("共找到"+fileCount+"个文件");
            for (int i = 0; i < fileIndexList.size(); i++) {
                FileIndexManage.FileIndex fileIndex = fileIndexList.get(i);
                PrintUtil.println((i + 1) + "、" + fileIndex.getFilePath());
            }
            // 窗口模式下会堵塞
//            PrintUtil.print("序号 / 重新输入文件名：");
//            String choice = InputUtil.inputNotEmpty();
//            if (ToolUtil.isInteger(choice)) {
//                openIndex = Integer.parseInt(choice);
//            } else {
//                this.run(new Command().setValueList(CollectionUtil.newArrayList(choice)));
//                return;
//            }
        }
        if (fileCount == 1 || null != openIndexStr && openIndex >= 0) {
            CmdUtil.openFile(fileIndexList.get(openIndex).toFile());
        }

    }
}
