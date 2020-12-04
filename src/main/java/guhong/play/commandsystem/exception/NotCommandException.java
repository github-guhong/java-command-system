package guhong.play.commandsystem.exception;

import lombok.Data;

/**
 * 命令找不到异常
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class NotCommandException extends RuntimeException {

    private String commandKey;


    public NotCommandException(String commandKey) {
        this.commandKey = commandKey;
    }

    @Override
    public String getMessage() {
        return "[" + commandKey + "] 命令找不到: "+super.getMessage();
    }
}
