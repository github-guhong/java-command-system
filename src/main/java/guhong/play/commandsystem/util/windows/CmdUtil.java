package guhong.play.commandsystem.util.windows;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import guhong.play.commandsystem.constant.Constant;
import guhong.play.commandsystem.util.ToolUtil;
import guhong.play.commandsystem.util.print.PrintUtil;
import lombok.Data;

import java.io.*;
import java.nio.charset.Charset;

/**
 * windows命令工具
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class CmdUtil {


    /**
     * 执行命令
     *
     * @param command 命令
     * @return 返回执行结果
     */
    public static Process exec(String command) {
        command = "cmd.exe /c " + command;
        Runtime run = Runtime.getRuntime();
        try {
            return run.exec(command);
        } catch (Exception e) {
            PrintUtil.errorPrint("windows命令执行错误: " + e.getMessage(), true);
        }
        return null;
    }

    /**
     * 执行多个命令
     *
     * @param commands 命令列表
     * @return 返回执行后的结果
     */
    public static Process exec(String... commands) {
        String command = ArrayUtil.join(commands, " & ");
        return exec(command);
    }

    /**
     * 是否成功
     *
     * @param process 执行结果
     * @return true为成功
     */
    public static Boolean isSuccess(Process process) {
        if (null == process) {
            return false;
        }
        // 如果还在执行，则使用waitFor进入等待
        if (process.isAlive()) {
            try {
                process.waitFor();
            } catch (Exception e) {
                return false;
            }
        }
        return process.exitValue() == 0;
    }


    /**
     * 打开文件或目录
     *
     * @param file 文件对象
     * @return 返回执行结果
     */
    public static Process openFile(File file) {
        if (!FileUtil.exist(file)) {
            PrintUtil.errorPrint("windows命令执行错误: 文件不存在");
            return null;
        }
        String[] commands = null;
        String drive = ToolUtil.getDrive(file.getPath());
        if (file.isDirectory()) {
            commands = new String[]{drive + ":", "explorer " + formatValue(file.getPath())};
        } else {
            commands = new String[]{drive + ":", "start " + formatValue(file.getPath())};
        }
        return exec(commands);
    }

    public static Process openFile(String filePath) {
        return openFile(new File(filePath));
    }

    /**
     * 打印执行结果
     *
     * @param process 执行结果
     */
    public static void printProcess(Process process) {
        if (null == process) {
            return;
        }

        InputStream inputStream = process.getInputStream();
        InputStream errorStream = process.getErrorStream();
        SequenceInputStream mergeInputStream = new SequenceInputStream(inputStream, errorStream);

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(mergeInputStream, "gbk"));
            String tmp = null;
            while ((tmp = bufferedReader.readLine()) != null) {
                PrintUtil.println(tmp);
            }
        } catch (Exception e) {
            //
        } finally {
            IoUtil.close(bufferedReader);
            IoUtil.close(inputStream);
        }
    }

    /**
     * 执行并打印
     *
     * @param command 执行的命令
     */
    public static void execAndPrint(String command) {
        Process process = exec(command);
        if (null != process) {
            printProcess(process);
        }
    }

    /**
     * 解析执行结果
     * 执行成功返回成功信息，失败则返回失败信息
     *
     * @param process 执行结果
     * @return 返回解析后执行结果信息
     */
    public static String parseProcess(Process process) {
        InputStream inputStream = null;
        if (isSuccess(process)) {
            inputStream = process.getInputStream();
        } else {
            inputStream = process.getErrorStream();
        }
        return IoUtil.read(inputStream, Charset.defaultCharset()).trim();
    }

    /**
     * 执行并解析执行结果
     *
     * @param command 执行的命令
     * @return 返回解析后的执行结果
     */
    public static String execAndParse(String command) {
        return parseProcess(exec(command));
    }


    /**
     * 获得当前所在路径
     *
     * @return 返回当前所在路径
     */
    public static String getCurrentPath() {
        String command = "echo %cd%";
        String path = execAndParse(command);
        // 转换正反斜杠
        path = path.replaceAll("\\\\", "/");
        return path;
    }


    /**
     * 将字符串格式化成cmd支持的字符
     *
     * @param value 要格式化的文字
     * @return 格式化后的文字
     */
    public static String formatValue(String value) {
        // 删除双引号
        value = value.replaceAll("\"", "");

        // 存在空格，使用双引号括起来
        if (value.contains(" ")) {
            if (FileUtil.isFile(value)) {
                return "\"\" " + addQuote(value);
            } else {
                return addQuote(value);
            }
        }
        for (String s : Constant.SPECIAL_CHAR) {
            if (value.contains(s)) {
                value = value.replace(s, "\""+s+"\"");
            }
        }
        return value;
    }

    /**
     * 添加双引号
     *
     * @param value 值
     * @return 返回加好后的值
     */
    public static String addQuote(String value) {
        return "\"" + value + "\"";
    }

    public static void killTask(String... pids) {
        for (String pid : pids) {
            CmdUtil.execAndPrint("taskkill /PID " + pid);
        }
    }
}
