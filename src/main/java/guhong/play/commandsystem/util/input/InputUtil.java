package guhong.play.commandsystem.util.input;

import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.util.print.PrintUtil;
import lombok.Data;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 键盘输入工具
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class InputUtil {


    private static BufferedReader stream = new BufferedReader(new InputStreamReader(System.in));


    /**
     * 控制台输入一个字符串
     *
     * @param defaultValue 默认值
     */
    public static String input(Object defaultValue) {
        try {
            String s = stream.readLine();
            if (StrUtil.isBlank(s)) {
                if (null == defaultValue) {
                    while (true) {
                        if (!StrUtil.isBlank(s)) {
                            break;
                        } else {
                            s = stream.readLine();
                        }
                    }
                } else {
                    return defaultValue.toString();
                }
            }
            return s;
        } catch (Exception e) {
            PrintUtil.errorPrint(e);
            return "";
        }
    }

    /**
     * 控制台输入一个int
     * @param defaultValue 默认值
     */
    public static Integer inputInt(Integer defaultValue) {
        try {
            return Integer.parseInt(input(defaultValue));
        } catch (Exception e) {
            if (null != defaultValue) {
                PrintUtil.errorPrint("你输的不是数字，默认返回:" + defaultValue);
                return defaultValue;
            }
            PrintUtil.errorPrint("能不能好好输个数字！");
            return null;
        }
    }

    /**
     * 是否 y 或 yes
     * @param choice 选择的值
     * @return 是y返回true
     */
    public static boolean isY(String choice) {
        if (choice.toLowerCase().equals("y") || choice.toLowerCase().equals("yes")) {
            return true;
        }
        return false;
    }
}
