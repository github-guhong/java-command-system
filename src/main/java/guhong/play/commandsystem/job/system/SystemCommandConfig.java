package guhong.play.commandsystem.job.system;

import guhong.play.commandsystem.dto.entity.CommandConfig;

import java.util.Map;

/**
 * 系统命令对象规则
 * @author 李双凯
 * @date : 2019-11-20 22:32
 **/
public class SystemCommandConfig extends CommandConfig {



    public SystemCommandConfig(String commandKey) {
        super(commandKey);
    }


    public SystemCommandConfig(String commandKey, String description) {
        super(commandKey,description);
    }


    public SystemCommandConfig(String commandKey, Map<String, Boolean> params) {
        super(commandKey,params);
    }



    @Override
    public String getGroup() {
        return "system";
    }
}
