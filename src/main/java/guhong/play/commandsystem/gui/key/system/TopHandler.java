package guhong.play.commandsystem.gui.key.system;

import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.gui.command.CommandContent;
import guhong.play.commandsystem.gui.history.CommandHistoryManage;
import guhong.play.commandsystem.gui.key.KeyListenerHandler;
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
        CommandHistoryManage historyCommandManage = terminal.getHistoryCommandManage();
        String historyCommand = historyCommandManage.last();
        CommandContent commandContent = terminal.getCommandContent();
        String commandStr = commandContent.getCommandStr();
        if (StrUtil.isNotBlank(commandStr)) {
            // 清除终端上当前的命令
            String text = terminal.getText();
            terminal.setText(text.substring(0, text.length() - commandStr.length()));
            commandContent.clear();
        }
        // 打印历史命令
        terminal.print(historyCommand);
        terminal.getCommandContent().append(historyCommand);

    }
}
