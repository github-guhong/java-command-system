package guhong.play.commandsystem.gui.key.system;

import guhong.play.commandsystem.gui.key.KeyListenerHandler;
import guhong.play.commandsystem.gui.terminal.Terminal;
import guhong.play.commandsystem.util.ToolUtil;
import lombok.Data;

import java.awt.event.KeyEvent;

/**
 * Ctrl + Shift + L 清理终端
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class CtrlLHandler implements KeyListenerHandler {

    /**
     * 是否监听
     *
     * @param e 事件对象
     * @return 监听返回true
     */
    @Override
    public boolean isListener(KeyEvent e) {
        // 单纯使用 Ctrl + L 触发不了，加上Shift就可以，不知道为什么
        return e.isControlDown() && e.getKeyCode() == KeyEvent.VK_L;
    }



    /**
     * 执行
     *
     * @param terminal 终端对象
     */
    @Override
    public void execute(Terminal terminal) {
        terminal.clear();
        terminal.print(ToolUtil.getHeadInfo());
    }
}
