package guhong.play.commandsystem.job.tool.calculator;

import guhong.play.commandsystem.dto.entity.Command;
import guhong.play.commandsystem.dto.entity.CommandConfig;
import guhong.play.commandsystem.job.CommandJob;
import guhong.play.commandsystem.util.print.PrintUtil;
import lombok.Data;

import java.util.List;

/**
 * 时间计算器
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class TimeCalculatorJob implements CommandJob {


    /**
     * 获得命令的配置
     *
     * @return 返回命令的配置
     */
    @Override
    public CommandConfig getCommandConfig() {
        CommandConfig commandConfig = new CommandConfig("tc");
        commandConfig.setDescription("计算两个时间之间的值");
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
        if (valueList.size() < 2) {
            PrintUtil.println("请传入两个时间！");
            return;
        }
        String timeStr1 = valueList.get(0);
        String timeStr2 = valueList.get(1);

    }
}
