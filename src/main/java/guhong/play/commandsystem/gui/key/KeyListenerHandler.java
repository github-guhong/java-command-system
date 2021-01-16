package guhong.play.commandsystem.gui.key;

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
     * 是否结束监听
     *
     * @param e        事件对象
     * @param terminal 终端对象
     * @return 不监听返回true
     */
    public boolean isExit(KeyEvent e, Terminal terminal);

    /**
     * 执行
     *
     * @param terminal 终端对象
     */
    public void execute(Terminal terminal);

}
