package guhong.play.commandsystem;

import guhong.play.commandsystem.gui.MainWindow;
import guhong.play.commandsystem.util.windows.CmdUtil;

/**
 * 主程序
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public class MainProgram {


    public static void main(String[] args) {
        closeOther();
        new MainWindow();
    }

    private static void closeOther() {
        String result = CmdUtil.execAndParse("jps");
        String[] javaProcess = result.split("\n");
        for (String process : javaProcess) {
            String[] segment = process.split("\\s+");
            if (segment.length < 2) {
                continue;
            }
            String pid = segment[0];
            String name = segment[1];
            if (name.contains("java-command-system")) {
                CmdUtil.killTask(pid);
            }
        }
    }

}


