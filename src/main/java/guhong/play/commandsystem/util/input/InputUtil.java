package guhong.play.commandsystem.util.input;

import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.util.print.PrintUtil;
import lombok.Data;
import lombok.NonNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * 键盘输入工具
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class InputUtil {


    private static BufferedReader stream = null;

    static {
        try {
            stream = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        } catch (Exception e) {
            stream = new BufferedReader(new InputStreamReader(System.in));
        }
    }

    /**
     * 输入一个字符串
     *
     * @return 返回输入的字符串
     */
    public static String input() {
        try {
            return stream.readLine();
        } catch (Exception e) {
            PrintUtil.errorPrint(e);
            return null;
        }
    }

    /**
     * 输入一个字符串，不能为空，如果是空则循环输入
     *
     * @return 返回输入的字符串
     */
    public static String inputNotEmpty() {
        String str = input();
        while (StrUtil.isBlank(str)) {
            str = input();
        }
        return str;
    }

    /**
     * 输入一个字符串,可以指定默认值，如果为空则返回默认值
     *
     * @return 返回输入的字符串
     */
    public static String inputDefault(@NonNull String defaultValue) {
        String str = input();
        if (StrUtil.isBlank(str)) {
            str = defaultValue;
        }
        return str;
    }


    /**
     * 控制台输入一个int
     *
     * @return 返回输入的int
     */
    public static Integer inputInt() {
        try {
            String str = input();
            if (StrUtil.isNotBlank(str)) {
                return Integer.parseInt(str);
            } else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException("能不能好好输个数字！");
        }
    }

    /**
     * 控制台输入不为空的int
     *
     * @return 返回输入的int
     */
    public static Integer inputIntNotEmpty() {
        try {
            return Integer.parseInt(inputNotEmpty());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("能不能好好输个数字！");
        }
    }

    /**
     * 是否 y 或 yes
     *
     * @param choice 选择的值
     * @return 是y返回true
     */
    public static boolean isY(String choice) {
        return isChoice(choice, "yes", "y");
    }

    /**
     * 是否是指定的值
     *
     * @param choice 选择的值
     * @param values 判断的值
     * @return 是返回true
     */
    public static boolean isChoice(String choice, String... values) {
        for (String value : values) {
            if (choice.toLowerCase().equals(value.toLowerCase())) {
                return true;
            }
        }
        return false;
    }


}
