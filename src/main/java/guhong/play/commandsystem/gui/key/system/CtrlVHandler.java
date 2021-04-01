package guhong.play.commandsystem.gui.key.system;

import cn.hutool.core.swing.clipboard.ClipboardUtil;
import guhong.play.commandsystem.gui.key.KeyListenerHandler;
import guhong.play.commandsystem.gui.terminal.Terminal;
import lombok.Data;

import java.awt.event.KeyEvent;

/**
 * Ctrl + V 粘贴处理器
 * 用于获取ctrl+v后的内容
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class CtrlVHandler implements KeyListenerHandler {


    /**
     * 是否监听
     *
     * @param e 事件对象
     * @return 监听返回true
     */
    @Override
    public boolean isListener(KeyEvent e) {
        // isControlDown : ctrl是否按下
        return e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V;
    }



    /**
     * 执行
     *
     * @param terminal 终端对象
     */
    @Override
    public void execute(Terminal terminal) {
        String clipboardValue = ClipboardUtil.getStr();
        terminal.getCommandContent().append(clipboardValue);
    }
}
