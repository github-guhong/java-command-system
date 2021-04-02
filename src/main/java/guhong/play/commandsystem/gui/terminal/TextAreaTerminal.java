package guhong.play.commandsystem.gui.terminal;

import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.gui.key.KeyListenerHandler;
import guhong.play.commandsystem.gui.key.type.KeyType;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

/**
 * 文本域终端对象
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public class TextAreaTerminal extends JTextArea implements KeyListener,
        CaretListener, Terminal {


    /**
     * 当前输入的键盘标识，因为keyTyped中拿不到code
     */
    private volatile int currentKeyCode = 0;


    public TextAreaTerminal() {
        super();
    }

    /**
     * 键盘输入一个值的时候
     *
     * @param e 事件对象
     */
    @Override
    public synchronized void keyTyped(KeyEvent e) {
        e.setKeyCode(currentKeyCode);

        String commandStr = this.getCommandContent().getCommandStr();
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && StrUtil.isBlank(commandStr)) {
            e.consume();
            return;
        }

        // 监听打印字符的按键。比如：A B C D
        List<KeyListenerHandler> keyListenerHandlerList = this.getKeyListenerHandlerManage().get(KeyType.PRINT);
        for (KeyListenerHandler keyListenerHandler : keyListenerHandlerList) {
            if (keyListenerHandler.isListener(e)) {
                keyListenerHandler.execute(this);
                return;
            }
        }
        this.getCommandContent().append(new String(new char[]{e.getKeyChar()}));
    }

    /**
     * 键盘按下的时候，只是按下，还没有在终端中输入值
     *
     * @param e 事件对象
     */
    @Override
    public synchronized void keyPressed(KeyEvent e) {
        currentKeyCode = e.getKeyCode();

        // 防止删除系统提示
        String commandStr = this.getCommandContent().getCommandStr();
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && StrUtil.isBlank(commandStr)) {
            e.consume();
            return;
        }

        // 监听不打印字符的按键。比如: 方向键 ↑ ↓ ← →
        List<KeyListenerHandler> keyListenerHandlerList = this.getKeyListenerHandlerManage().get(KeyType.NOT_PRINT);
        for (KeyListenerHandler keyListenerHandler : keyListenerHandlerList) {
            if (keyListenerHandler.isListener(e)) {
                keyListenerHandler.execute(this);
                return;
            }
        }
    }

    /**
     * 键盘弹回去的时候，只要按键弹回去，不管有没有输入值，都会触发
     * 比如中文输入时，输入后并不会直接触发keyPressed方法，因为中文输入是需要确认的，所以系统判定此时还没有按下去
     * 但是键盘肯定会被释放（弹回来），所以这种情况就会先进这个方法。
     * @param e 事件对象
     */
    @Override
    public synchronized void keyReleased(KeyEvent e) {
        currentKeyCode = e.getKeyCode();
    }


    @Override
    public void append(String message) {
        super.append(message);
    }

    public void appendLn(String message) {
        super.append(message + "\n");
    }

    /**
     * 光标更新时
     * 更新时重新定位到文本末端
     *
     * @param e the caret event
     */
    @Override
    public void caretUpdate(CaretEvent e) {
//        System.out.println(e);
        int length = this.getText().length();
        // 光标和文本域长度不一致时才定位光标
        // 不是最优解，这样会定死光标。不能通过方向键移动
        if (length != e.getDot()) {
            keepCaretInTextEnd();
        }
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
    public void keepCaretInTextEnd() {
        super.setCaretPosition(this.getText().length());
    }
}