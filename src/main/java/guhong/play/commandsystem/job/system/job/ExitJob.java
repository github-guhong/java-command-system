package guhong.play.commandsystem.job.system.job;

import guhong.play.commandsystem.dto.entity.Command;
import guhong.play.commandsystem.dto.entity.CommandConfig;
import guhong.play.commandsystem.job.CommandJob;
import guhong.play.commandsystem.job.system.SystemCommandConfig;
import guhong.play.commandsystem.util.ToolUtil;
import lombok.Data;

/**
 * 结束终端工作
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class ExitJob implements CommandJob {

    /**
     * 获得命令的配置
     *
     * @return 返回命令的配置
     */
    @Override
    public CommandConfig getCommandConfig() {
        return new SystemCommandConfig("exit", "结束程序");
    }

    /**
     * 开始执行任务
     *
     * @param command 命令对象
     */
    @Override
    public void run(Command command) {
        ToolUtil.shutdown();
    }
}
