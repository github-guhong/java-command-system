package guhong.play.commandsystem.gui.key.system;

import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.gui.command.CommandContent;
import guhong.play.commandsystem.gui.key.KeyListenerHandler;
import guhong.play.commandsystem.gui.terminal.Terminal;
import lombok.Data;

import java.awt.event.KeyEvent;

/**
 * 退格键监听
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class BackspaceHandler implements KeyListenerHandler {

    /**
     * 是否监听
     *
     * @param e 事件对象
     * @return 监听返回true
     */
    @Override
    public boolean isListener(KeyEvent e) {
        return e.getKeyCode() == KeyEvent.VK_BACK_SPACE;
    }

    /**
     * 是否结束监听
     *
     * @param e        事件对象
     * @param terminal 终端对象
     * @return 不监听返回true
     */
    @Override
    public boolean isExit(KeyEvent e, Terminal terminal) {
        String commandStr = terminal.getCommandContent().getCommandStr();
        return StrUtil.isBlank(commandStr);
    }

    /**
     * 执行
     *
     * @param terminal 终端对象
     */
    @Override
    public void execute(Terminal terminal) {
        CommandContent commandContent = terminal.getCommandContent();
         if (StrUtil.isNotBlank(commandContent.getCommandStr())) {
            commandContent.delete();
        }
    }

}
