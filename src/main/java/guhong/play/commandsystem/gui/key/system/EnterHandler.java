package guhong.play.commandsystem.gui.key.system;

import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.CommandManager;
import guhong.play.commandsystem.gui.command.CommandContent;
import guhong.play.commandsystem.gui.key.KeyListenerHandler;
import guhong.play.commandsystem.gui.terminal.Terminal;
import guhong.play.commandsystem.util.ToolUtil;
import guhong.play.commandsystem.util.print.PrintUtil;
import lombok.Data;

import java.awt.event.KeyEvent;

/**
 * 回车键监听
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class EnterHandler implements KeyListenerHandler {

    /**
     * 是否监听
     *
     * @param e 事件对象
     * @return 监听返回true
     */
    @Override
    public boolean isListener(KeyEvent e) {
        return e.getKeyCode() == KeyEvent.VK_ENTER && '\n' == e.getKeyChar();
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
        CommandContent commandContent = terminal.getCommandContent();
        String commandStr = commandContent.getCommandStr();
        if (StrUtil.isNotBlank(commandStr)) {
            try {
                // 执行命令
                CommandManager.execute(commandStr);
            } catch (Exception exception) {
                PrintUtil.errorPrint(exception);
            } finally {
                commandContent.clear();
            }
        }
        terminal.print(ToolUtil.getHeadInfo());
    }


}