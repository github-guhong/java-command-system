package guhong.play.commandsystem.util;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.dto.entity.Command;
import guhong.play.commandsystem.job.CommandJob;
import lombok.Data;

import java.util.concurrent.*;

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
     * @param paramValue
     * @return
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
        String[] split = commandStr.split("\\s");
        String commandKey = split[0];
        if (StrUtil.isBlank(commandKey)) {
            return null;
        }
        return commandKey;
    }

}
