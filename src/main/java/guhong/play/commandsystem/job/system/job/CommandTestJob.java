package guhong.play.commandsystem.job.system.job;

import cn.hutool.core.collection.CollectionUtil;
import guhong.play.commandsystem.dto.entity.Command;
import guhong.play.commandsystem.dto.entity.CommandConfig;
import guhong.play.commandsystem.job.CommandJob;
import guhong.play.commandsystem.job.system.SystemCommandConfig;
import guhong.play.commandsystem.util.print.PrintUtil;
import lombok.Data;

import java.util.Map;

/**
 * 一个用于测试命令的工作
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class CommandTestJob implements CommandJob {

    /**
     * 这个方法配置命令的一些命令
     *
     * @return 返回命令的配置
     */
    @Override
    public CommandConfig getCommandConfig() {
        // 设置命令的名字
        String commandKey = "test";
        // 设置命令的参数
        Map<String, Boolean> paramConfig = CollectionUtil.newHashMap();
        // 表示 -a 参数必须有一个值
        paramConfig.put("-a", true);
        // 表示 -f 参数的值可以为空
        paramConfig.put("-f", false);
        CommandConfig commandConfig = new SystemCommandConfig(commandKey, paramConfig);
        // 设置命令的介绍
        commandConfig.setDescription("这是一个测试命令");
        return commandConfig;

    }

    /**
     * 开始执行任务
     *
     * @param command 命令对象
     */
    @Override
    public void run(Command command) {
        // 这里写命令要执行的工作，也就是要做什么事情

        // 这里，我们打印出命令的名字、参数和值
        PrintUtil.println("commandKey：" + command.getKey());
        Map<String, String> params = command.getParams();
        PrintUtil.println("commandParam: ");
        for (String key : params.keySet()) {
            PrintUtil.println("key: " + key + " | value: " + params.get(key));
        }
        PrintUtil.println("commandValue: " + command.getValueList());
    }
}
