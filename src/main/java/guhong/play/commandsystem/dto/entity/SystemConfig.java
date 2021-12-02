package guhong.play.commandsystem.dto.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import guhong.play.commandsystem.constant.CommandMode;
import guhong.play.commandsystem.constant.Constant;
import guhong.play.commandsystem.util.file.FileOperationUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 系统配置
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
@Accessors(chain = true)
public class SystemConfig {

    /**
     * 指定dto类
     */
    private String dto;

    /**
     * 命令模式。在默认情况下会以什么样的方式去执行命令
     */
    private CommandMode mode;


    /**
     * 第三方配置
     */
    private JSONObject thirdParty;

    /**
     * 构建默认系统配置
     *
     * @return 返回默认系统配置
     */
    public static SystemConfig buildDefaultConfig() {
        SystemConfig config = new SystemConfig();
        config.setDto("guhong.play.commandsystem.dto.DefaultCommandDto");
        config.setMode(CommandMode.CMD);
        return config;
    }

    /**
     * 是否是空
     *
     * @return 空返回true
     */
    @JSONField(serialize = false)
    public boolean isEmpty() {
        if (null == dto) {
            return true;
        }
        return false;
    }

    /**
     * 同步配置
     */
    @JSONField(serialize = false)
    public void sync() {
        FileOperationUtil.createFile(Constant.SYSTEM_CONFIG_FILE);
        FileOperationUtil.coverAppendConfig(Constant.SYSTEM_CONFIG_FILE, JSON.toJSONString(this));
    }
}
