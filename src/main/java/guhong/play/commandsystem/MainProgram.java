package guhong.play.commandsystem;

import guhong.play.commandsystem.gui.MainWindow;
import guhong.play.commandsystem.util.ToolUtil;
import guhong.play.commandsystem.util.windows.CmdUtil;

/**
 * 主程序
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public class MainProgram {


    public static void main(String[] args) {
        String result = CmdUtil.execAndParse("jps");
        String[] javaProcess = result.split("\n");
        // 在开发环境中启动时不算做一个独立的进程，但实际用bat脚本执行却会把自己也算作一个独立的进程
        // 所以max是2，而不是1
        int max = 2;
        int count = 0;
        for (String process : javaProcess) {
            if (process.contains("java-command-system")) {
                count++;
                String pid = process.split(" ")[0];
            }
        }
        if (count >= max) {
            // 弹出当前在运行的java-command-system
            ToolUtil.shutdown();
        }
        new MainWindow();
    }

}


