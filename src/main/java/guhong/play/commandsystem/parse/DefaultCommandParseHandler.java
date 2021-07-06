package guhong.play.commandsystem.parse;

import cn.hutool.core.collection.CollectionUtil;
import guhong.play.commandsystem.dto.entity.Command;
import guhong.play.commandsystem.dto.entity.CommandConfig;
import guhong.play.commandsystem.exception.ParseException;
import lombok.Data;
import lombok.NonNull;

import java.util.List;
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
        Map<String, String> params = CollectionUtil.newHashMap();
        List<String> commandValue = CollectionUtil.newArrayList();

        Map<String, Boolean> paramConfig = commandConfig.getParamConfig();
        // 跳过第一个，第一个是命令的名字
        for (index = index + 1; index <= maxIndex; index++) {
            String paramName = commandSection[index];

            // 判断是参数还是值
            Boolean existValue = paramConfig.get(paramName);
            // 参数
            if (null != existValue) {
                // 判断该参数是否需要一个值
                String paramValue = "";
                if (existValue && (index + 1) <= maxIndex) {
                    ++index;
                    paramValue = commandSection[index];

                    // 如果这个值是一个参数，则抛出异常
                    if (null != paramConfig.get(paramValue)) {
                        throw new ParseException("[" + paramName + "] 参数必须包含一个值！");
                    }
                }
                params.put(paramName, paramValue);

            } else {
                // 值
                commandValue.add(paramName);
            }

        }

        Command command = new Command();
        command.setKey(commandKey);
        command.setParams(params);
        command.setValueList(commandValue);
        command.setSource(commandStr);
        return command;
    }
}
