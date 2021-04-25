package guhong.play.commandsystem.job.system.job;

import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.CommandManager;
import guhong.play.commandsystem.constant.CommandMode;
import guhong.play.commandsystem.dto.entity.Command;
import guhong.play.commandsystem.dto.entity.CommandConfig;
import guhong.play.commandsystem.dto.entity.SystemConfig;
import guhong.play.commandsystem.job.CommandJob;
import guhong.play.commandsystem.job.system.SystemCommandConfig;
import guhong.play.commandsystem.util.print.PrintUtil;
import lombok.Data;

/**
 * 命令模式切换
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class ModeSwitchJob implements CommandJob {

    /**
     * 获得命令的配置
     *
     * @return 返回命令的配置
     */
    @Override
    public CommandConfig getCommandConfig() {
        SystemCommandConfig commandConfig = new SystemCommandConfig("mode", "切换命令模式");
        commandConfig.setIntroduce("直接输入该命令可以查询当前的命令模式，使用-l参数查询当前可用的命令模式，使用[mode 命令模式]切换当前的命令模式");
        commandConfig.putParamConfig("-l", false);
        return commandConfig;
    }

    /**
     * 开始执行任务
     *
     * @param command 命令对象
     */
    @Override
    public void run(Command command) {
        String l = command.getParamValue("-l");
        if (null != l) {
            for (CommandMode mode : CommandMode.values()) {
                PrintUtil.println(mode.getName() + ": " + mode.getDescription());
            }
            return;
        }
        String value = command.getFirstValue();
        if (StrUtil.isBlank(value)) {
            CommandMode commandMode = CommandManager.getSystemConfig().getMode();
            PrintUtil.println("当前命令模式: " + commandMode.getName() );
        } else {
            CommandMode commandMode = CommandMode.getByName(value);
            if (null == commandMode) {
                PrintUtil.errorPrint(value + "模式不存在！你可以通过[mode -l]命令查看支持的模式。");
            }
            SystemConfig systemConfig = CommandManager.getSystemConfig();
            systemConfig.setMode(commandMode);
            systemConfig.sync();
            PrintUtil.println("切换完成。当前命令模式：" + value);
        }

    }
}
