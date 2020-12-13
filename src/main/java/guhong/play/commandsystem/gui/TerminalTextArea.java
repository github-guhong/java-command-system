package guhong.play.commandsystem.gui;

import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.CommandManager;
import guhong.play.commandsystem.gui.interfaces.Terminal;
import guhong.play.commandsystem.util.ToolUtil;
import guhong.play.commandsystem.util.print.PrintUtil;
import lombok.Data;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * 终端对象
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public class TerminalTextArea extends JTextArea implements KeyListener,
        CaretListener, Terminal {

    /**
     * 当前输入的键盘标识
     */
    private int currentKeyCode = 0;
    /**
     * 用来存储当前输入的文本
     */
    private StringBuffer textBuffer = new StringBuffer();


    public TerminalTextArea() {
        super();
    }

    @Override
    public void keyTyped(KeyEvent e) {

        if (currentKeyCode == KeyEvent.VK_ENTER) {
            // 监听回车
            String commandStr = textBuffer.toString();
            if (StrUtil.isNotBlank(commandStr)) {
                try {
                    // 执行命令
                    CommandManager.execute(commandStr);
                } catch (Exception exception) {
                    PrintUtil.errorPrint(exception);
                } finally {
                    // 清除buffer
                    textBuffer.delete(0, textBuffer.length());
                }

            }
            this.append(ToolUtil.getHeadInfo());
        } else if (currentKeyCode == KeyEvent.VK_BACK_SPACE) {
            // 监听退格键，删除一个字符
            if (StrUtil.isNotBlank(textBuffer)) {
                textBuffer.delete(textBuffer.length() - 1, textBuffer.length());
            } else {
                e.consume();
            }
        } else if (currentKeyCode == KeyEvent.VK_ESCAPE) {
            // 监听esc键，结束系统
            ToolUtil.shutdown();
        } else if (currentKeyCode == KeyEvent.VK_KP_UP || currentKeyCode == KeyEvent.VK_UP || currentKeyCode == KeyEvent.VK_PAGE_UP) {
            System.out.println("kjdfla");
        } else {
            // 追加字符串
            textBuffer.append(e.getKeyChar());
        }
    }


    @Override
    public void append(String message) {
        super.append(message);
        caretPosition();
    }

    public void appendLn(String message) {
        super.append(message + "\n");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        currentKeyCode = e.getKeyCode();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * Called when the caret position is updated.
     *
     * @param e the caret event
     */
    @Override
    public void caretUpdate(CaretEvent e) {
//        caretPosition();
    }


    /**
     * 终端上打印
     *
     * @param message 打印的信息
     */
    @Override
    public void print(String message) {
        append(message);
    }

    /**
     * 终端上换行打印
     *
     * @param message 打印的消息
     */
    @Override
    public void println(String message) {
        appendLn(message);
    }

    /**
     * 清空打印
     */
    @Override
    public void clear() {
        setText("");
    }

    /**
     * 定位光标位置在文本末
     */
    public void caretPosition() {
        setCaretPosition(this.getText().length());
    }
}