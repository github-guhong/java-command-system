package guhong.play.commandsystem.job.file;

import guhong.play.commandsystem.dto.entity.CommandConfig;

import java.util.Map;

/**
 * 文件命令对象规则
 * @author 李双凯
 * @date : 2019-11-20 22:32
 **/
public class FileCommandConfig extends CommandConfig {



    public FileCommandConfig(String commandKey) {
        super(commandKey);
    }


    public FileCommandConfig(String commandKey, String description) {
        super(commandKey,description);
    }


    public FileCommandConfig(String commandKey, Map<String, Boolean> params) {
        super(commandKey,params);
    }



    @Override
    public String getGroup() {
        return "file";
    }
}
