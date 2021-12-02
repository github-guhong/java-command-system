package guhong.play.commandsystem.parse;

import com.alibaba.fastjson.JSONObject;
import guhong.play.commandsystem.dto.entity.Command;
import guhong.play.commandsystem.dto.entity.CommandConfig;
import guhong.play.commandsystem.exception.ParseException;
import guhong.play.commandsystem.util.ToolUtil;
import lombok.Data;
import lombok.NonNull;

import java.util.Map;

/**
 * 默认解析器
 * 按空格来解析
 * <p>
 * 第一段：命令名
 * 第二段：参数
 * 第三段：命令值，多个
 * <p>
 * 参数和命令值的顺序可以随意
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class DefaultCommandParseHandler implements CommandParseHandler {

    /**
     * 解析命令
     *
     * @param commandStr    要执行的命令
     * @param commandConfig 命令配置对象
     * @return 返回一个命令对象
     * @throws ParseException 解释错误会出现解析错误
     */
    @Override
    public Command parse(@NonNull String commandStr, @NonNull CommandConfig commandConfig) throws ParseException {
        // 空格拆分
        String[] commandSection = commandStr.split("\\s+");
        int index = 0;
        int maxIndex = commandSection.length - 1;

        // 获得命令名
        String commandKey = commandSection[index];
        Command command = new Command();
        command.setKey(commandKey);
        command.setSource(commandStr);

        Map<String, Boolean> paramConfig = commandConfig.getParamConfig();
        // 跳过第一个，第一个是命令的名字
        for (index = index + 1; index <= maxIndex; index++) {
            String paramNameOrValue = commandSection[index];

            // 判断是参数还是值
            Boolean existValue = paramConfig.get(paramNameOrValue);
            if (null != existValue) {
                // 参数
                String paramValue = "";
                if (existValue && (index + 1) <= maxIndex) {
                    ++index;
                    paramValue = commandSection[index];
                    JSONObject result = doSpace(paramValue, commandSection, index);
                    paramValue = result.getString("str");
                    index = result.getInteger("index");

                    if (null != paramConfig.get(paramValue)) {
                        throw new ParseException("[" + paramNameOrValue + "] 参数必须包含一个值！");
                    }
                }
                command.putParamValue(paramNameOrValue, paramValue);

            } else {
                // 值
                JSONObject result = doSpace(paramNameOrValue, commandSection, index);
                paramNameOrValue = result.getString("str");
                index = result.getInteger("index");
                command.addValue(paramNameOrValue);
            }

        }


        return command;
    }

    public boolean isStringHead(String str) {
        String head = str.substring(0, 1);
        return "\"".equals(head) || "'".equals(head);
    }

    public boolean isStringTail(String str) {
        String tail = str.substring(str.length() - 1);
        return "\"".equals(tail) || "'".equals(tail);
    }

    public JSONObject doSpace(String str, String[] commandSection, int currentIndex) {
        JSONObject result = new JSONObject();
        result.put("str", str);
        result.put("index", currentIndex);

        if (ToolUtil.isBlankParam(str)) {
            result.put("str", "");
            return result;
        }

        // 处理带空格的值问题
        if (isStringHead(str)) {
            String next = null;
            StringBuilder strBuilder = new StringBuilder(str);
            do {
                currentIndex++;
                if (currentIndex >= commandSection.length) {
                    throw new ParseException("命令格式不正确，请完善\"或'符号");
                }
                next = commandSection[currentIndex];
                strBuilder.append(" ").append(next);
            } while (!isStringTail(next));
            str = strBuilder.toString();
            str = str.substring(1, str.length() - 1);
            result.put("str", str);
            result.put("index", currentIndex);
        }
        return result;
    }
}
