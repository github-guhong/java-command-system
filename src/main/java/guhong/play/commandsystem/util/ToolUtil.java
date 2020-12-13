package guhong.play.commandsystem.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.dto.entity.Command;
import guhong.play.commandsystem.job.CommandJob;
import guhong.play.commandsystem.util.print.PrintUtil;
import guhong.play.commandsystem.util.windows.CmdUtil;
import lombok.Data;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.*;
import java.util.regex.Pattern;

/**
 * 工具类
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class ToolUtil {



    private static ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 100,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), new ThreadFactoryBuilder().build(), new ThreadPoolExecutor.AbortPolicy());


    /**
     * 异步运行一个工作
     * @param command 命令
     * @param commandJob 工作
     */
    public static void asynchStart(Command command, CommandJob commandJob) {
        singleThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                commandJob.run(command);
            }
        });
    }

    /**
     * 参数值是否为空
     * 用于判断命令参数的值是否为空字符串
     * @param paramValue 参数值
     * @return 是空字符串则为true
     */
    public static Boolean isBlankParam(String paramValue){
        if (paramValue.matches("\"\\s*\"")) {
            return true;
        }
        return false;
    }


    /**
     * 获得命令名
     * @param commandStr 命令字符串
     * @return 返回命令名
     */
    public static String getCommandKey(String commandStr) {
        if (StrUtil.isBlank(commandStr)) {
            return null;
        }
        commandStr = commandStr.trim();
        String[] split = commandStr.split("\\s");
        String commandKey = split[0];
        if (StrUtil.isBlank(commandKey)) {
            return null;
        }
        return commandKey;
    }

    /**
     * 获得环境变量的值
     * @param key key
     * @return 返回环境变量的值
     */
    public static String getEnv(String key) {
        Map<String, String> envMap = System.getenv();
        return envMap.get(key);
    }

    /**
     * 获得盘符
     * @param path 路径
     * @return 返回路径所在的盘符
     */
    public static String getDrive(String path) {
        return path.substring(0, path.indexOf(":"));
    }


    /**
     * 是否是int
     * @param str 字符串
     * @return 是返回true
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static String getHeadInfo() {
        String userName = System.getProperty("user.name");
        String currentDir = CmdUtil.getCurrentPath();
        currentDir = currentDir.substring(currentDir.lastIndexOf("/")+1);
        String now = DateUtil.format(new Date(), "yyyy-MM-dd HH-mm");
        return userName + "#" + now + " " + currentDir + "/ : ";
    }

    /**
     * 关机
     */
    public static void shutdown() {
        PrintUtil.print("告辞！");
        System.exit(0);
    }
}
