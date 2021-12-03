package guhong.play.commandsystem.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * 命令模式。在默认情况下会以什么样的方式去执行命令。
 * 简单来说就是在命令找不到时，使用什么方式去处理输入的命令。
 *
 * 后期修改，取消枚举，在配置文件中直接指定commandKey
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public enum CommandMode {

    /**
     * 使用windows执行命令
     */
    CMD("cmd","使用windows执行命令", "cmd"),
    /**
     * 使用 of 命令执行命令
     */
    OF("of","使用 of 命令执行命令", "of");

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String commandKey;

    CommandMode(String name) {
        this.name = name;
    }

    CommandMode(String name, String description) {
        this.name = name;
        this.description = description;
    }

    CommandMode(String name, String description, String commandKey) {
        this.name = name;
        this.description = description;
        this.commandKey = commandKey;
    }

    public static CommandMode getByName(String name) {
        for (CommandMode commandMode : values()) {
            if (commandMode.getName().equals(name)) {
                return commandMode;
            }
        }
        return null;
    }

}
