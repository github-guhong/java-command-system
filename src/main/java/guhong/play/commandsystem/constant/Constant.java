package guhong.play.commandsystem.constant;

import lombok.Data;

/**
 * 常量
 *
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
     * 数据文件地址
     */
    public static final String DATA_PATH = PROJECT_PATH + "/data";


    /**
     * 缓存文件地址
     */
    public static final String CACHE_PATH = DATA_PATH + "/cache";

    /**
     * 配置文件目录
     */
    public static final String CONFIG_PATH = PROJECT_PATH + "/config";

    /**
     * 系统配置文件地址
     */
    public static final String SYSTEM_CONFIG_FILE = CONFIG_PATH + "/sys-config.json";

    /**
     * 命令文件地址
     */
    public static final String COMMAND_DATA_PATH = CONFIG_PATH + "/command-data.json";


    /**
     * 特殊字符
     */
    public static final String[] SPECIAL_CHAR = new String[]{"!","@","#","$","%","^","&","*","(",")","-","=","~","·"};


}
