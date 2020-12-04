package guhong.play.commandsystem.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import guhong.play.commandsystem.constant.Constant;
import guhong.play.commandsystem.dto.entity.Command;
import guhong.play.commandsystem.dto.entity.CommandConfig;
import guhong.play.commandsystem.dto.entity.SystemConfig;
import guhong.play.commandsystem.exception.SystemException;
import guhong.play.commandsystem.job.CommandJob;
import guhong.play.commandsystem.util.print.PrintUtil;
import lombok.Data;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import static guhong.play.commandsystem.constant.Constant.COMMAND_DATA_PATH;

/**
 * 读取工具
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class FileOperationUtil {



    /**
     * 读取指定文件
     * @param path 文件地址
     * @param isCreate 文件不存在是否创建
     * @return 返回配置文件内容，如果为空则说明文件不存在或没有任何配置
     */
    public static String read(String path, boolean isCreate) {
        File configFile = new File(path);
        if (!FileUtil.exist(configFile)) {
            if (isCreate) {
                try {
                    // 创建父目录
                    FileUtil.mkParentDirs(configFile);
                    if (!configFile.createNewFile()){
                        throw new IOException("创建 ["+path+"] 文件失败！");
                    }
                } catch (IOException e) {
                    throw new SystemException(e.getMessage());
                }
            }
            return null;
        }
        BufferedInputStream inputStream = FileUtil.getInputStream(configFile);
        try {
            return IoUtil.read(inputStream , Charset.defaultCharset());
        }catch (Exception e) {
            throw new SystemException(e.getMessage());
        } finally {
            IoUtil.close(inputStream);
        }
    }


    /**
     * 覆盖追加配置
     * @param path 配置文件地址
     * @param config 追加的内容
     */
    public static void coverAppendConfig(String path, String config) {
        JSONObject jsonObject = readConfigAndParse(path);
        if (null == jsonObject) {
            jsonObject = new JSONObject();
        }
        JSONObject appendConfig = JSONObject.parseObject(config);
        jsonObject.putAll(appendConfig);
        IoUtil.write(FileUtil.getOutputStream(path), true, jsonObject.toJSONString().getBytes());
    }

    /**
     * 读取并解析配置文件
     * @param path 配置文件路径
     * @return 返回解析后的json对象
     */
    public static JSONObject readConfigAndParse(String path) {
        String configContent = read(path, true);
        if (StrUtil.isBlank(configContent)) {
            return null;
        }
        return JSONObject.parseObject(configContent);
    }


    /**
     * 读取系统配置
     * @return 返回系统配置对象
     */
    public static SystemConfig readSystemConfig() {
        JSONObject jsonObject = readConfigAndParse(Constant.SYSTEM_CONFIG_FILE);
        if (null == jsonObject) {
            return null;
        }
        SystemConfig systemConfig = JSONObject.parseObject(jsonObject.toJSONString(), SystemConfig.class);
        if (null == systemConfig || systemConfig.isEmpty()) {
            return null;
        }
        return systemConfig;
    }

    /**
     * 读取命令数据
     * @return 返回命令数据
     */
    public static Map<String, Map<String, CommandJob>> readCommandData() {
        JSONObject jsonObject = readConfigAndParse(Constant.COMMAND_DATA_PATH);
        if (null == jsonObject) {
            return null;
        }
        // 手动解析一遍
        Map<String, Map<String, CommandJob>> result = CollectionUtil.newHashMap();

        for (String group : jsonObject.keySet()) {
            Map<String, CommandJob> commandJobMap = CollectionUtil.newHashMap();

            JSONObject commandJobMapJson = jsonObject.getJSONObject(group);
            for (String commandKey : commandJobMapJson.keySet()) {
                // 获得工作对象的类型创建具体的对象
                String commandJobString = commandJobMapJson.getString(commandKey);
                CommandJob commandJob = null;
                try {
                    commandJob = ReflectUtil.newInstance(commandJobString);
                } catch (Exception e) {
                    PrintUtil.errorPrint("实例化["+commandKey+"]命令时出现错误，这将导致你无法使用该命令。如果该命令已删除，请使用[reload]命令重新加载命令");
                }
                commandJobMap.put(commandKey, commandJob);

            }

            result.put(group, commandJobMap);
        }
        return result;
    }


}
