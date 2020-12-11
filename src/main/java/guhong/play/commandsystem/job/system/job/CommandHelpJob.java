package guhong.play.commandsystem.job.system.job;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import guhong.play.commandsystem.CommandManager;
import guhong.play.commandsystem.dto.entity.Command;
import guhong.play.commandsystem.dto.entity.CommandConfig;
import guhong.play.commandsystem.exception.ExecuteException;
import guhong.play.commandsystem.exception.NotCommandException;
import guhong.play.commandsystem.job.CommandJob;
import guhong.play.commandsystem.job.system.SystemCommandConfig;
import guhong.play.commandsystem.util.FileOperationUtil;
import guhong.play.commandsystem.util.print.PrintUtil;

import java.util.List;

/**
 * 帮助命令工作对象
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public class CommandHelpJob implements CommandJob {



    /**
     * 获得命令的配置
     *
     * @return 返回命令的配置
     */
    @Override
    public CommandConfig getCommandConfig() {
        CommandConfig commandConfig = new SystemCommandConfig("help");
        commandConfig.setDescription("查看命令描述");
        commandConfig.setIntroduce("-f "+ System.getProperty("user.dir") +"/src/main/resource/document/Help.txt");
        return commandConfig;
    }

    /**
     * 开始执行任务
     *
     * @param command 命令对象
     */
    @Override
    public void run(Command command) {
        List<String> valueList = command.getValueList();
        String commandKey = command.getKey();
        if (CollectionUtil.isEmpty(valueList)) {
            throw new ExecuteException("必须指定一个要查看的命令。比如：help list", commandKey);
        }
        String value = valueList.get(0);
        CommandJob commandJob = CommandManager.getCommandDto().getCommandJob(value);
        if (null == commandJob) {
            throw new NotCommandException(value);
        }
        CommandConfig commandConfig = commandJob.getCommandConfig();
        System.out.println("=========="+value + "命令帮助==========");
        System.out.println("描述：");
        PrintUtil.printChapter(commandConfig.getDescription());
        System.out.println("介绍：");
        String introduce = commandConfig.getIntroduce();
        if (introduce.startsWith("file:")) {
            String documentPath = introduce.substring(introduce.indexOf(":")+1);
            if (!FileUtil.exist(documentPath)) {
                introduce = "介绍文件未找到，所以没有介绍！";
            } else {
                introduce = FileOperationUtil.read(documentPath, false);
            }
        }
        PrintUtil.printChapter(introduce);
        System.out.println("==============================");
    }
}
