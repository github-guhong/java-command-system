package guhong.play.commandsystem.dto.entity;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.util.ToolUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * 命令对象
 *
 * @date : 2019-11-20 22:32
 **/
@Data
@Accessors(chain = true)
public class Command {

    /**
     * 命令的名字
     */
    private String key;

    /**
     * 命令参数
     * 参数名： 参数值
     */
    private Map<String, String> params;

    /**
     * 命令的值
     */
    private List<String> valueList;

    /**
     * 命令源字符串
     */
    private String source;


    @Override
    public String toString() {
        return "Command{" +
                "key='" + key + '\'' +
                ", params=" + params +
                ", value='" + valueList + '\'' +
                '}';
    }


    /**
     * 获得指定参数的值
     *
     * @param paramName 参数名
     * @return 返回参数值，如果没有则返回null
     */
    public String getParamValue(String paramName) {
        if (CollectionUtil.isNotEmpty(params)) {
            String value = params.get(paramName);
            if (StrUtil.isNotBlank(value)) {
                if (ToolUtil.isBlankParam(value)) {
                    return "";
                }
            }
            return value;
        }
        return null;
    }

    /**
     * 获得第一个值
     *
     * @return 返回第一个值
     */
    public String getFirstValue() {
        return getValue(0);
    }

    /**
     * 获得第二个值
     *
     * @return 返回第二个值
     */
    public String getSecondValue() {
        return getValue(1);
    }


    /**
     * 获得第三个值
     *
     * @return 返回第撒个值
     */
    public String getThirdValue() {
        return getValue(2);
    }

    /**
     * 获得指定索引下的值
     *
     * @param index 索引
     * @return 返回指定索引下的值
     */
    public String getValue(int index) {
        if (CollectionUtil.isNotEmpty(valueList)) {
            if (valueList.size() <= index) {
                return null;
            }
            String value = valueList.get(index);
            if (ToolUtil.isBlankParam(value)) {
                return "";
            }
            return value;
        }
        return null;
    }

    /**
     * 获得最后一个值
     * @return 返回最后一个值
     */
    public String getLastValue() {
        return valueList.get(valueList.size() - 1);
    }
}
