package guhong.play.commandsystem.job.system.job;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.CommandManager;
import guhong.play.commandsystem.dto.entity.Command;
import guhong.play.commandsystem.dto.entity.CommandConfig;
import guhong.play.commandsystem.dto.entity.SystemConfig;
import guhong.play.commandsystem.gui.history.CommandHistoryManage;
import guhong.play.commandsystem.job.CommandJob;
import guhong.play.commandsystem.job.system.SystemCommandConfig;
import guhong.play.commandsystem.util.ToolUtil;
import guhong.play.commandsystem.util.print.PrintUtil;
import lombok.Data;

import java.util.List;

/**
 * 命令历史命令
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class CommandHistoryJob implements CommandJob {

    @Override
    public CommandConfig getCommandConfig() {
        SystemCommandConfig systemCommandConfig = new SystemCommandConfig("history");
        systemCommandConfig.putParamConfig("-l", false);
        systemCommandConfig.putParamConfig("-c", false);
        systemCommandConfig.putParamConfig("-max", true);
        systemCommandConfig.setDescription("命令历史");
        systemCommandConfig.setFileIntroduce("history.txt");
        return systemCommandConfig;
    }

    @Override
    public void run(Command command) {
        CommandHistoryManage historyCommandManage = CommandManager.getTerminal().getHistoryCommandManage();
        if (command.isExistParam("-l")) {
            List<String> historyList = historyCommandManage.getHistoryList();
            PrintUtil.println("命令历史最大容量：" + CommandManager.getSystemConfig().getMaxHistory());
            PrintUtil.println("当前历史记录：");
            if (CollectionUtil.isNotEmpty(historyList)) {
                PrintUtil.printLnWithNumber(historyList);
            }
        } else if (command.isExistParam("-c")) {
            historyCommandManage.getHistoryList().clear();
            PrintUtil.println("清除成功！");
        }
        String maxValueStr = command.getParamValue("-max");
        if (StrUtil.isNotBlank(maxValueStr)) {
            if (ToolUtil.isInteger(maxValueStr)) {
                int maxValue = Integer.parseInt(maxValueStr);
                SystemConfig systemConfig = CommandManager.getSystemConfig();
                systemConfig.setMaxHistory(maxValue);
                systemConfig.sync();
                PrintUtil.println("设置成功！");
            }
        }
    }

    @Override
    public void windowClosing() {
        // 保存本次记录的命令历史
        CommandManager.getTerminal().getHistoryCommandManage().save();
    }
}
