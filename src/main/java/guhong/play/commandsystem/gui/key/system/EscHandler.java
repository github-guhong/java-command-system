package guhong.play.commandsystem.gui.key.system;

import guhong.play.commandsystem.gui.key.KeyListenerHandler;
import guhong.play.commandsystem.gui.terminal.Terminal;
import guhong.play.commandsystem.util.ToolUtil;
import lombok.Data;

import java.awt.event.KeyEvent;

/**
 * ESC键处理
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class EscHandler implements KeyListenerHandler {

    /**
     * 是否监听
     *
     * @param e 事件对象
     * @return 监听返回true
     */
    @Override
    public boolean isListener(KeyEvent e) {
        return e.getKeyCode() == KeyEvent.VK_ESCAPE;
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
        return false;
    }

    /**
     * 执行
     *
     * @param terminal 终端对象
     */
    @Override
    public void execute(Terminal terminal) {
        ToolUtil.shutdown();
    }


}
