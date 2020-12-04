package guhong.play.commandsystem.dto.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 系统配置
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
     * 构建默认系统配置
     * @return 返回默认系统配置
     */
    public static SystemConfig buildDefaultConfig() {
        SystemConfig config = new SystemConfig();
        config.setDto("guhong.play.commandsystem.dto.DefaultCommandDto");
        return config;
    }

    /**
     * 是否是空
     * @return 空返回true
     */
    public boolean isEmpty() {
        if (null == dto){
            return true;
        }
        return false;
    }

}
