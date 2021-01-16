package guhong.play.commandsystem.exception;

import lombok.Data;

/**
 * 命令执行错误
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public class ExecuteException extends RuntimeException {

    private String commandKey;

    public ExecuteException(String commandKey) {
        this.commandKey = commandKey;
    }

    public ExecuteException(String message, String commandKey) {
        super(message);
        this.commandKey = commandKey;
    }

    @Override
    public String getMessage() {
        return "[" + commandKey + "] 执行失败: " + super.getMessage();
    }
}
