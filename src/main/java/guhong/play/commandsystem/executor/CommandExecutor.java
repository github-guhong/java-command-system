package guhong.play.commandsystem.executor;

import guhong.play.commandsystem.dto.entity.Command;
import guhong.play.commandsystem.exception.ExecuteException;
import guhong.play.commandsystem.job.CommandJob;

/**
 *  执行器
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public interface CommandExecutor {


    /**
     * 执行命令
     * @param command 执行的命令
     * @param commandJob 执行的任务
     * @exception ExecuteException 命令执行失败异常
     */
    public void execute(Command command, CommandJob commandJob) throws ExecuteException;
}
