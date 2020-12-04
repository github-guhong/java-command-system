package guhong.play.commandsystem.job.system.job;

import guhong.play.commandsystem.dto.entity.Command;
import guhong.play.commandsystem.dto.entity.CommandConfig;
import guhong.play.commandsystem.job.CommandJob;
import guhong.play.commandsystem.util.windows.CmdUtil;
import lombok.Data;

/**
 * 显示当前cmd所在路径命令
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class ShowCmdPathJob implements CommandJob {

    /**
     * 获得命令的配置
     *
     * @return 返回命令的配置
     */
    @Override
    public CommandConfig getCommandConfig() {
        return new CommandConfig("pwd","获得当前所在目录");
    }

    /**
     * 开始执行任务
     *
     * @param command 命令对象
     */
    @Override
    public void run(Command command) {
        String currentPath = CmdUtil.getCurrentPath();
        System.out.println(currentPath+"\n");
    }
}
