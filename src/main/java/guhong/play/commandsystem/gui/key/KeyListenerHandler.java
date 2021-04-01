package guhong.play.commandsystem.gui.key;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import guhong.play.commandsystem.gui.key.type.KeyType;
import guhong.play.commandsystem.gui.terminal.Terminal;

import java.awt.event.KeyEvent;

/**
 * 键盘监听处理器
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public interface KeyListenerHandler {

    /**
     * 是否监听
     *
     * @param e 事件对象
     * @return 监听返回true
     */
    public boolean isListener(KeyEvent e);

    /**
     * 监听类型
     */
    public default KeyType type() {
        return KeyType.NOT_PRINT;
    }

    /**
     * 执行
     *
     * @param terminal 终端对象
     */
    public void execute(Terminal terminal);

}
