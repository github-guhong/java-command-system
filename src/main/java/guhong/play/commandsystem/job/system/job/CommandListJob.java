package guhong.play.commandsystem.job.system.job;


import cn.hutool.core.collection.CollectionUtil;
import guhong.play.commandsystem.CommandManager;
import guhong.play.commandsystem.dto.entity.Command;
import guhong.play.commandsystem.dto.entity.CommandConfig;
import guhong.play.commandsystem.job.CommandJob;
import guhong.play.commandsystem.job.system.SystemCommandConfig;
import guhong.play.commandsystem.util.print.PrintUtil;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 查看命令列表工作
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class CommandListJob implements CommandJob {

    /**
     * 获得命令名字
     *
     * @return 返回命令名字
     */
    @Override
    public CommandConfig getCommandConfig() {
        CommandConfig commandConfig = new SystemCommandConfig("list");
        Map<String, Boolean> paramConfig = CollectionUtil.newHashMap();
        paramConfig.put("-g", true);
        commandConfig.setParamConfig(paramConfig);
        commandConfig.setDescription("查询系统当前所有命令");
        commandConfig.setIntroduce("查询系统当前所有命令参数解析 \n \t -g 组名 查询指定组中的命令");
        return commandConfig;
    }

    /**
     * 开始执行任务
     *
     * @param command 命令对象
     */
    @Override
    public void run(Command command) {
        String value = command.getParams().get("-g");
        Collection<CommandJob> commandJobList = CommandManager.getCommandDto().getCommandJobList(value);
        List<String> columnList = CollectionUtil.newArrayList("命令名", "所属组", "命令描述", "执行命令的类");
        List<List<Object>> valueList = CollectionUtil.newArrayList();
        commandJobList.forEach(commandJob -> {
            List<Object> list = CollectionUtil.newArrayList();
            CommandConfig commandConfig = commandJob.getCommandConfig();
            list.add(commandConfig.getCommandKey());
            list.add(commandConfig.getGroup());
            list.add(commandConfig.getDescription());
            list.add(commandJob.getClass());
            valueList.add(list);
        });
        PrintUtil.printTable(columnList, valueList, 100);
    }
}
