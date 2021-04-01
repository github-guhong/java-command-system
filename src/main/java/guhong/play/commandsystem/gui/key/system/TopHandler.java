package guhong.play.commandsystem.gui.key.system;

import guhong.play.commandsystem.gui.key.KeyListenerHandler;
import guhong.play.commandsystem.gui.key.type.KeyType;
import guhong.play.commandsystem.gui.terminal.Terminal;
import lombok.Data;

import java.awt.event.KeyEvent;

/**
 *  ↑ 方向键监听
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class TopHandler implements KeyListenerHandler {

    /**
     * 是否监听
     *
     * @param e 事件对象
     * @return 监听返回true
     */
    @Override
    public boolean isListener(KeyEvent e) {
        return e.getKeyCode() == KeyEvent.VK_UP;
    }


    /**
     * 执行
     *
     * @param terminal 终端对象
     */
    @Override
    public void execute(Terminal terminal) {
        Integer index = terminal.getHistoryIndex().increase();
        if (index <= 0) {
            return;
        }
        String command = terminal.getHistoryCommand().get(index - 1);
        terminal.print(command);
        terminal.getCommandContent().append(command);
    }
}
