package guhong.play.commandsystem.exception.util;

import lombok.Data;

/**
 * cmd命令错误
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class CmdCommandException extends RuntimeException{

    @Override
    public String getMessage() {
        return "windows命令执行错误: "+super.getMessage();
    }
}
