package guhong.play.commandsystem;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.util.print.PrintUtil;
import guhong.play.commandsystem.util.windows.CmdUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

/**
 *
 * 主程序
 * 初始化：
 * 加载配置——》读取命令——》构建命令管理器
 * 使用：
 * 输入命令——》解析——》验证——》命令执行器——》执行工作
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public class MainProgram {

    private static final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception{
        // 初始化
        System.out.println("欢迎使用Java命令系统");
        System.out.println("正在加载命令（第一次执行可能需要点时间）...");
        CommandManager.init();

        // 定位到C盘
//        CmdUtil.exec("C:");
        System.out.println("加载完成。");

        // 打印说明信息
        System.out.println("\n帮助：");
        System.out.println("1、可以通过 [list] 命令查看当前系统的所有命令。");
        System.out.println("2、可以通过 [help 命令] 命令查看指定命令的帮助信息");
        System.out.println("3、你也可以在这里直接执行windows命令\n");
        while (true) {
            print();
            String commandStr = input.readLine();
            if (StrUtil.isBlank(commandStr)) {
                continue;
            }
            if ("exit".equals(commandStr)) {
                break;
            }
            try {
                CommandManager.execute(commandStr);
            } catch (Exception e) {
                PrintUtil.errorPrint(e);
            }
        }
        System.out.println("告辞！");
    }


    private static void print() {
        String userName = System.getProperty("user.name");
        String currentDir = CmdUtil.getCurrentPath();
        currentDir = currentDir.substring(currentDir.lastIndexOf("/")+1);
        String now = DateUtil.format(new Date(), "yyyy-MM-dd HH-mm");
        System.out.print(userName + "#" + now + " " + currentDir + "/ : ");
    }
}


