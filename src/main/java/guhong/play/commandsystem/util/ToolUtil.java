package guhong.play.commandsystem.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.dto.entity.Command;
import guhong.play.commandsystem.job.CommandJob;
import guhong.play.commandsystem.util.windows.CmdUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 工具类
 *
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
     *
     * @param command    命令
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
     *
     * @param paramValue 参数值
     * @return 是空字符串则为true
     */
    public static Boolean isBlankParam(String paramValue) {
        if (paramValue.matches("\"\\s*\"")) {
            return true;
        }
        if (paramValue.matches("'\\s*'")) {
            return true;
        }
        if ("\"\"".equals(paramValue)) {
            return true;
        }
        if ("''".equals(paramValue)) {
            return true;
        }
        return false;
    }


    /**
     * 获得命令名
     *
     * @param commandStr 命令字符串
     * @return 返回命令名
     */
    public static String getCommandKey(String commandStr) {
        if (StrUtil.isBlank(commandStr)) {
            return null;
        }
        commandStr = commandStr.trim();
        String[] split = commandStr.split("\\s+");
        String commandKey = split[0];
        if (StrUtil.isBlank(commandKey)) {
            return null;
        }
        return commandKey;
    }

    /**
     * 获得环境变量的值
     *
     * @param key key
     * @return 返回环境变量的值
     */
    public static String getEnv(String key) {
        Map<String, String> envMap = System.getenv();
        return envMap.get(key);
    }

    /**
     * 获得盘符
     *
     * @param path 路径
     * @return 返回路径所在的盘符
     */
    public static String getDrive(String path) {
        return path.substring(0, path.indexOf(":"));
    }


    /**
     * 是否是int
     *
     * @param str 字符串
     * @return 是返回true
     */
    public static boolean isInteger(String str) {
        if (StrUtil.isBlank(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static String getHeadInfo() {
        String userName = System.getProperty("user.name");
        String currentDir = CmdUtil.getCurrentPath();
        currentDir = currentDir.substring(currentDir.lastIndexOf("/") + 1);
        String now = DateUtil.format(new Date(), "yyyy-MM-dd HH-mm");
        return "\n" + userName + "#" + now + " " + currentDir + "/ : ";
    }

    /**
     * 关机
     */
    public static void shutdown() {
        System.exit(0);
    }

    /**
     * 对象组中是否全是 Empty Object
     *
     * @param os
     * @return
     */
    public static boolean isAllEmpty(Object... os) {
        for (Object o : os) {
            if (isNotEmpty(o)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 对象是否不为空(新增)
     *
     * @param o String,List,Map,Object[],int[],long[]
     * @return
     */
    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    /**
     * 对象是否为空
     *
     * @param o String,List,Map,Object[],int[],long[]
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof String) {
            if (o.toString().trim().equals("")) {
                return true;
            }
        } else if (o instanceof List) {
            if (((List) o).size() == 0) {
                return true;
            }
        } else if (o instanceof Map) {
            if (((Map) o).size() == 0) {
                return true;
            }
        } else if (o instanceof Set) {
            if (((Set) o).size() == 0) {
                return true;
            }
        } else if (o instanceof Object[]) {
            if (((Object[]) o).length == 0) {
                return true;
            }
        } else if (o instanceof int[]) {
            if (((int[]) o).length == 0) {
                return true;
            }
        } else if (o instanceof long[]) {
            if (((long[]) o).length == 0) {
                return true;
            }
        } else if (o instanceof BigDecimal) {
            BigDecimal bigDecimal = (BigDecimal) o;
            return bigDecimal.equals(BigDecimal.ZERO);
        }
        return false;
    }

    /**
     * 对象组中是否存在 Empty Object
     *
     * @param os 对象组
     * @return
     */
    public static boolean isOneEmpty(Object... os) {
        for (Object o : os) {
            if (isEmpty(o)) {
                return true;
            }
        }
        return false;
    }

}
