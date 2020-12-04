package guhong.play.commandsystem.util.windows;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.util.print.PrintUtil;
import lombok.Data;

import java.io.File;
import java.io.InputStream;
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
        command = "cmd.exe /c" + command;
        Runtime run = Runtime.getRuntime();
        try {
            return run.exec(command);
        } catch (Exception e) {
            PrintUtil.errorPrint("windows命令执行错误: " + e.getMessage(), true);
        }
        return null;
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
            }catch (Exception e) {
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
            PrintUtil.errorPrint("windows命令执行错误: 文件不存在", true);
        }
        String command = null;
        if (file.isDirectory()) {
            command = "explorer " + file.getPath();
        } else {
            command = "start " + file.getPath();
        }
        return exec(command);
    }


    /**
     * 打印执行结果
     *
     * @param process 执行结果
     */
    public static void printProcess(Process process) {
        String str = parseProcess(process);
        if (StrUtil.isNotBlank(str)) {
            PrintUtil.println(str);
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


    public static String getCurrentPath() {
        String command = "echo %cd%";
        String path = execAndParse(command);
        // 转换正反斜杠
        path = path.replaceAll("\\\\", "/");
        return path;
    }
}
