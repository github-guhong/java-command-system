package guhong.play.commandsystem.util.print;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import guhong.play.commandsystem.CommandManager;
import guhong.play.commandsystem.constant.Name;
import guhong.play.commandsystem.gui.terminal.Terminal;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

/**
 * 打印工具
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class PrintUtil {

    /**
     * 延时打印
     *
     * @param second  延时的时间，秒
     * @param message 打印的消息
     * @param isLn    是否换行
     */
    public static void delayPrint(int second, String message, boolean isLn) {
        try {
            Thread.sleep(second * 1000);
        } catch (Exception e) {
            println("延时打印出错: " + e.getMessage());
        }
        print(message, isLn);
    }


    /**
     * 延时打印 换行
     *
     * @param second  延时的时间，秒
     * @param message 打印的消息
     */
    public static void delayPrint(int second, String message) {
        delayPrint(second, message, true);
    }

    /**
     * 打印警告
     *
     * @param message 打印的信息
     * @param isLn    是否换行
     */
    public static void warnPrint(String message, boolean isLn) {
        message = "warn: " + message;
        print(message, isLn);
    }

    /**
     * 打印警告 换行打印
     *
     * @param message 打印的信息
     */
    public static void warnPrint(String message) {
        warnPrint(message, true);
    }

    /**
     * 打印错误
     *
     * @param message 打印的信息
     * @param isLn    是否换行
     */
    public static void errorPrint(String message, boolean isLn) {
        message = "error: " + message;
        print(message, isLn);
    }

    /**
     * 打印错误 换行打印
     *
     * @param message 打印的信息
     */
    public static void errorPrint(String message) {
        errorPrint(message, true);
    }

    /**
     * 打印错误 换行打印
     *
     * @param e 打印的异常
     */
    public static void errorPrint(Exception e) {
        errorPrint(e.getMessage(), true);
    }


    /**
     * 打印
     *
     * @param message 打印的信息
     * @param isLn    是否换行
     */
    public static void print(String message, boolean isLn) {
        Terminal terminal = CommandManager.getTerminal();
        if (isLn) {
            terminal.println(message);
        } else {
            terminal.print(message);
        }
    }

    /**
     * 换行打印
     *
     * @param message 打印信息
     */
    public static void println(String message) {
        print(message, true);
    }

    public static void println() {
        print("", true);
    }

    /**
     * 普通打印
     *
     * @param message 打印的消息
     */
    public static void print(String message) {
        print(message, false);
    }

    public static void print() {
        print("", false);
    }


    /**
     * 换行打印
     */
    public static void newLine() {
        print("", true);
    }

    /**
     * 打印表格
     *
     * @param columnList 列名
     * @param values     值
     * @param maxLength  表格宽度，单位/字符数，默认50
     */
    public static void printTable(@NonNull List<String> columnList, List<List<Object>> values, Integer maxLength) {
        if (null == maxLength) {
            maxLength = 50;
        }

        // 开始打印

        // 打印头
        for (int i = 0; i < maxLength; i++) {
            print("-", false);
        }
        newLine();

        // 打印列名
        print("| ", false);
        for (String columnName : columnList) {
            String message = columnName + " | ";
            print(message, false);
        }
        newLine();
        for (int i = 0; i < maxLength; i++) {
            print("-", false);
        }
        newLine();


        // 打印值
        for (List<Object> objectList : values) {
            print("| ", false);
            for (Object o : objectList) {
                String value = o.toString();
                String message = value + " | ";
                print(message, false);
            }
            newLine();
        }


        // 打印尾
        for (int i = 0; i < maxLength; i++) {
            print("-", false);
        }
        newLine();
    }


    /**
     * 打印段落
     *
     * @param message 打印的消息
     */
    public static void printChapter(String message) {
        printChapter(2, message);
    }

    /**
     * 打印一个端落开头
     *
     * @param space   空几个格
     * @param message 打印的消息
     */
    public static void printChapter(int space, String message) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < space; i++) {
            stringBuffer.append(" ");
        }
        stringBuffer.append(message);
        println(stringBuffer.toString());
    }

    /**
     * 打印数组
     *
     * @param array 数组
     */
    public static void printArray(String... array) {
        String joinStr = ArrayUtil.join(array, ", ");
        println(joinStr);
    }

    /**
     * 打印一个带序号的列表
     * @param list 列表
     */
    public static void printWithNumberByName(List<? extends Name> list) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            print((i+1) + "." + list.get(i).getName() + " ");
        }
        println("");
    }

    /**
     * 打印一个带序号的列表
     * @param list 列表
     */
    public static void printLnWithNumberByName(List<? extends Name> list) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            println((i+1) + "." + list.get(i).getName() + " ");
        }
        println("");
    }


    /**
     * 打印一个带序号的列表
     * @param list 列表
     */
    public static void printWithNumber(List<?> list) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            print((i+1) + "." + list.get(i) + " ");
        }
        println("");
    }

    /**
     * 打印一个带序号的列表
     * @param list 列表
     */
    public static void printLnWithNumber(List<?> list) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            println((i+1) + "." + list.get(i) + " ");
        }
        println("");
    }




}
