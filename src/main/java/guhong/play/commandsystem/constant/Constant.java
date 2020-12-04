package guhong.play.commandsystem.constant;

import guhong.play.commandsystem.util.ToolUtil;
import lombok.Data;

/**
 * 常量
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class Constant {

    /**
     * 项目路径
     */
    public static final String PROJECT_PATH = System.getProperty("user.dir");

    /**
     * lib包目录
     */
    public static final String LIB = PROJECT_PATH + "/lib";

    /**
     * 文档地址
     */
    public static final String DOCUMENT_PATH = PROJECT_PATH + "/document";

    /**
     * 配置文件目录
     */
    public static final String CONFIG_PATH = PROJECT_PATH + "/config";

    /**
     * 系统配置文件地址
     */
    public static final String SYSTEM_CONFIG_FILE = CONFIG_PATH + "/config.json";

    /**
     * 命令文件地址
     */
    public static final String COMMAND_DATA_PATH =  CONFIG_PATH + "/command-data.json";


}
