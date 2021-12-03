package guhong.play.commandsystem.job.system.job;

import cn.hutool.core.collection.CollectionUtil;
import guhong.play.commandsystem.dto.entity.Command;
import guhong.play.commandsystem.dto.entity.CommandConfig;
import guhong.play.commandsystem.job.CommandJob;
import guhong.play.commandsystem.job.system.SystemCommandConfig;
import guhong.play.commandsystem.util.windows.CmdUtil;
import lombok.Data;

/**
 * 用cmd执行命令
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class CmdJob implements CommandJob {

    @Override
    public CommandConfig getCommandConfig() {
        SystemCommandConfig systemCommandConfig = new SystemCommandConfig("cmd");
        systemCommandConfig.setDescription("用cmd执行输入的命令");
        return systemCommandConfig;
    }

    @Override
    public void run(Command command) {
        CmdUtil.execAndPrint(CollectionUtil.join(command.getValueList(), " "));
    }
}
