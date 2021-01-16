package guhong.play.commandsystem.parse;


import guhong.play.commandsystem.dto.entity.Command;
import guhong.play.commandsystem.dto.entity.CommandConfig;
import guhong.play.commandsystem.exception.ParseException;
import lombok.NonNull;

/**
 * 命令解析器
 * 可以自行扩展解析的方式
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public interface CommandParseHandler {

    /**
     * 解析命令
     *
     * @param commandStr    要执行的命令
     * @param commandConfig 命令的配置对象
     * @return 返回一个命令对象
     * @throws ParseException 解释错误会出现解析错误
     */
    public Command parse(@NonNull String commandStr, @NonNull CommandConfig commandConfig) throws ParseException;
}
