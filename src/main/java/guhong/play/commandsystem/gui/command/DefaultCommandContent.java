package guhong.play.commandsystem.gui.command;

import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.util.UnicodeConvertUtil;
import lombok.Data;

/**
 * 默认的命令上下文
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class DefaultCommandContent implements CommandContent {

    /**
     * 用来存储当前输入的命
     */
    private StringBuilder commandStrContent = new StringBuilder();


    /**
     * 追加
     *
     * @param str 命令字符串
     */
    @Override
    public void append(String str) {
        String uniCodeValue = UnicodeConvertUtil.str2Unicode(str);
        // \\u0016 表示一个未定的字符，打印出来就是一个方框
        if ("\\u0016".equals(uniCodeValue)) {
            return;
        }
        commandStrContent.append(str);
    }

    /**
     * 删除一个字符串
     */
    @Override
    public void delete() {
        if (StrUtil.isNotBlank(commandStrContent)) {
            commandStrContent.delete(commandStrContent.length() - 1, commandStrContent.length());
        }
    }

    /**
     * 清空命令
     */
    @Override
    public void clear() {
        if (StrUtil.isNotBlank(commandStrContent)) {
            commandStrContent.delete(0, commandStrContent.length());
        }
    }

    /**
     * 获得命令字符串
     *
     * @return 返回命令字符串
     */
    @Override
    public String getCommandStr() {
        return commandStrContent.toString();
    }
}
