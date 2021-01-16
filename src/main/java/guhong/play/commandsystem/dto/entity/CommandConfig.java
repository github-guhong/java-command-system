package guhong.play.commandsystem.dto.entity;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.constant.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * 命令对象规则
 * 1、一个命令分为三个部分：
 * 命令名 、 命令参数 、 命令值
 * 2、命令名是必须的，其他的可以为空
 * 3、命令的参数不能是必须的
 * 4、命令的值可以多个
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
@AllArgsConstructor
public class CommandConfig {

    /**
     * 是否异步执行
     */
    private Boolean isAsynch = false;

    /**
     * 命令名字
     */
    private String commandKey;

    /**
     * 命令所属组
     */
    private String group = "default";

    /**
     * 命令的参数的配置
     * key：参数名
     * value：参数是否需要一个值，改值不会为null，除非没有输入该参数
     */
    private Map<String, Boolean> paramConfig = CollectionUtil.newHashMap();


    /**
     * 命令介绍
     */
    private String introduce = "没有任何介绍。。。";

    /**
     * 命令描述
     * 字数限制15个字
     */
    private String description = "没有任何描述";


    public CommandConfig(String commandKey) {
        this.commandKey = commandKey;
    }

    public CommandConfig(String commandKey, String description) {
        this.commandKey = commandKey;
        this.setDescription(description);
    }


    public CommandConfig(String commandKey, Map<String, Boolean> paramConfig) {
        this.commandKey = commandKey;
        this.paramConfig = paramConfig;
    }

    public void setDescription(String description) {
        if (description.length() > 15) {
            description = StrUtil.sub(description, 0, 16);
        }
        this.description = description;
    }


    public void setFileIntroduce(String fileName) {
        this.setIntroduce("file:" + Constant.DOCUMENT_PATH + "/" + this.getGroup() + "/" + fileName);
    }

}
