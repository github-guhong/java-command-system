package guhong.play.commandsystem.job.system.job;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import guhong.play.commandsystem.CommandManager;
import guhong.play.commandsystem.dto.entity.Command;
import guhong.play.commandsystem.dto.entity.CommandConfig;
import guhong.play.commandsystem.job.CommandJob;
import guhong.play.commandsystem.job.system.entity.SystemCommandConfig;
import lombok.Data;

import java.util.List;

/**
 * 重新加载命令工作
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class CommandReloadJob implements CommandJob {

    /**
     * 获得命令的配置
     *
     * @return 返回命令的配置
     */
    @Override
    public CommandConfig getCommandConfig() {
        return new SystemCommandConfig("reload", "重新加载命令");
    }

    /**
     * 开始执行任务
     *
     * @param command 命令对象
     */
    @Override
    public void run(Command command) {
        List<String> valueList = command.getValueList();
        String[] value = null;
        if (CollectionUtil.isNotEmpty(valueList)) {
            value = ArrayUtil.toArray(valueList, String.class);
        }
        CommandManager.getCommandDto().reload(value);
        System.out.println("重新加载成功。");
    }
}
