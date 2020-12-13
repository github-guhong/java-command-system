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
 * 测试命令工作
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class CommandTestJob implements CommandJob {

    /**
     * 获得命令的配置
     *
     * @return 返回命令的配置
     */
    @Override
    public CommandConfig getCommandConfig() {
        String commandKey = "test";
        Map<String, Boolean> paramConfig = CollectionUtil.newHashMap();
        paramConfig.put("-a", true);
        paramConfig.put("-f", false);
        CommandConfig commandConfig = new SystemCommandConfig(commandKey, paramConfig);
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
        PrintUtil.println("commandKey："+command.getKey());
        Map<String, String> params = command.getParams();
        PrintUtil.println("commandParam: ");
        for (String key : params.keySet()) {
            PrintUtil.println("key: "+key + " | value: " + params.get(key) );
        }
        PrintUtil.println("commandValue: "+ command.getValueList());
    }
}
