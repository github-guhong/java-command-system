package guhong.play.commandsystem.gui;

import com.sun.java.swing.plaf.motif.MotifScrollPaneUI;
import guhong.play.commandsystem.CommandManager;
import guhong.play.commandsystem.gui.terminal.TextAreaTerminal;
import guhong.play.commandsystem.util.ToolUtil;
import guhong.play.commandsystem.util.print.PrintUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * 主窗口
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public class MainWindow extends JFrame {


    public MainWindow() throws HeadlessException {
        // 设置主窗体
        addWindowListener(new MainWindowListener());
        setTitle("Java命令系统");
        setWindowSize();
        setVisible(true);
        setLocationRelativeTo(null);

        // 设置终端文本域
        TextAreaTerminal terminal = new TextAreaTerminal();
        add(terminal, BorderLayout.CENTER);
        terminal.addKeyListener(terminal);
        terminal.addCaretListener(terminal);
        terminal.setFont(new Font("宋体", Font.PLAIN, 20));
        terminal.requestFocus();
        terminal.setLineWrap(true);
        CommandManager.setTerminal(terminal);

        // 设置滚动窗口
        JScrollPane scroll = new JScrollPane(terminal);
        scroll.setSize(this.getSize());
        scroll.setUI(new MotifScrollPaneUI());
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scroll);

        // 加载系统
        initSystem();
        terminal.keepCaretInTextEnd();
        // 重新设置宽度，响应滚动条
        this.setSize(this.getWidth() + 1, this.getHeight());
    }


    private void setWindowSize() {
        // 得到显示器屏幕的宽、高
        int systemWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int systemHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        // 得到窗体的宽、高
        int windowsWidth = this.getWidth();
        int windowsHeight = this.getHeight();
        // 130是为了更好的显示list命令
        int finishWidth = (systemWidth - windowsWidth) / 2 + 130;
        int finishHeight = (systemHeight - windowsHeight) / 2 + 130;
        setSize(finishWidth, finishHeight);
    }

    /**
     * 初始化命令系统
     */
    private void initSystem() {
        // 初始化
        PrintUtil.println("欢迎使用Java命令系统");
        PrintUtil.println("正在加载命令（第一次执行可能需要点时间）...");
        CommandManager.init();

        // 定位到C盘
//        CmdUtil.exec("C:");
        PrintUtil.println("加载完成。");

        // 打印说明信息
        PrintUtil.println("\n帮助：");
        PrintUtil.println("1、可以通过 [list] 命令查看当前系统的所有命令。");
        PrintUtil.println("2、可以通过 [help 命令] 命令查看指定命令的帮助信息。如：[help list]。");
        PrintUtil.println("3、目前，无法支持动态的打印，所以在执行build、of -s 时可能会“卡住”，这属于正常现象，不是程序卡死。");
//        PrintUtil.println("3、你也可以在这里直接执行windows命令\n");

        // 打印头部信息
        PrintUtil.print(ToolUtil.getHeadInfo());


    }

}

/**
 * 主窗口的监听
 */
class MainWindowListener implements WindowListener {


    @Override
    public void windowOpened(WindowEvent e) {

    }


    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }


    @Override
    public void windowClosed(WindowEvent e) {

    }


    @Override
    public void windowIconified(WindowEvent e) {

    }


    @Override
    public void windowDeiconified(WindowEvent e) {

    }


    @Override
    public void windowActivated(WindowEvent e) {

    }


    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}

