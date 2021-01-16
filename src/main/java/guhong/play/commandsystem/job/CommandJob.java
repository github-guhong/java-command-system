package guhong.play.commandsystem.job;

import cn.hutool.core.lang.Singleton;
import guhong.play.commandsystem.dto.entity.Command;
import guhong.play.commandsystem.dto.entity.CommandConfig;
import guhong.play.commandsystem.executor.DefaultCommandExecutor;
import guhong.play.commandsystem.executor.CommandExecutor;
import guhong.play.commandsystem.parse.DefaultCommandParseHandler;
import guhong.play.commandsystem.parse.CommandParseHandler;

/**
 * 命令执行的工作
 * 每个命令都需要实现一个job
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public interface CommandJob {


    /**
     * 获得命令的配置
     *
     * @return 返回命令的配置
     */
    public CommandConfig getCommandConfig();

    /**
     * 开始执行任务
     *
     * @param command 命令对象
     */
    public void run(Command command);


    /**
     * 获得命令执行器
     *
     * @return 返回命令执行器
     */
    public default CommandExecutor getExecutor() {
        return Singleton.get(DefaultCommandExecutor.class);
    }

    /**
     * 获得命令解析器
     *
     * @return 返回命令解析器
     */
    public default CommandParseHandler getParseHandler() {
        return Singleton.get(DefaultCommandParseHandler.class);
    }

    /**
     * 执行任务
     *
     * @param commandStr 命令字符串
     */
    public default void doStart(String commandStr) {
        Command command = getParseHandler().parse(commandStr, this.getCommandConfig());
        getExecutor().execute(command, this);
    }
}
