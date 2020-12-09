package guhong.play.commandsystem.util.windows;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.constant.Constant;
import guhong.play.commandsystem.util.FileOperationUtil;
import guhong.play.commandsystem.util.ToolUtil;
import guhong.play.commandsystem.util.print.PrintUtil;
import lombok.Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
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
     * 执行多个命令
     * @param commands 命令列表
     * @return 返回执行后的结果
     */
    public static Process exec(String... commands) {
        String command = ArrayUtil.join(commands, "&");
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
        if (null == process) {
            return ;
        }
        InputStream inputStream = process.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String tmp = null;
        try {
            while ((tmp = br.readLine()) != null) {
                System.out.println(tmp);
            }
        } catch (Exception e) {
            //
        } finally {
            IoUtil.close(br);
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
     * 执行临时脚本
     *
     * @param batCommandModel 命令模板
     */
    public static boolean executeTempBat(String batCommandModel) {
        batCommandModel += "\n exit";

        FileOperationUtil.createDir(Constant.TEMP_PATH);
        String tempBatPath = Constant.TEMP_PATH + "/tempBat" + IdUtil.simpleUUID() + ".bat";
        File file = new File(tempBatPath);
        if (!FileUtil.exist(file)) {
            try {
                if (!file.createNewFile()) {
                    throw new Exception();
                }
            } catch (Exception e) {
                PrintUtil.errorPrint("创建临时脚本失败：" + e.getMessage());
            }
        }
        // 将脚本写入临时文件
        IoUtil.write(FileUtil.getOutputStream(file), true, batCommandModel.getBytes());
        // 执行命令
        Process process = exec(file.getPath());
        CmdUtil.printProcess(process);

        Boolean result = isSuccess(process);
        if (result) {
            FileUtil.del(file);
        }
        return result;
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
            return addQuote(value);

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
}
