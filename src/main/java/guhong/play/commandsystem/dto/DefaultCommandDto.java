package guhong.play.commandsystem.dto;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import guhong.play.commandsystem.constant.Constant;
import guhong.play.commandsystem.util.file.FileOperationUtil;
import guhong.play.commandsystem.dto.entity.CommandConfig;
import guhong.play.commandsystem.job.CommandJob;
import guhong.play.commandsystem.util.print.PrintUtil;
import lombok.Data;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.util.*;

/**
 * 默认传输对象
 * 在内存中维护命令数据，使用配置文件持久化命令
 * 通过反射读取项目所有的命令工作
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class DefaultCommandDto implements CommandDto {

    /**
     * 命令列表
     * key: 组名
     * value :
     *  key: 命令名字
     *  value： 执行的工作
     */
    private static Map<String, Map<String, CommandJob>> commandMap = CollectionUtil.newHashMap();


    /**
     * 加载所有工作
     *
     * @param packagePath 指定路径，可以多个，如果为空则全盘扫描
     * @return 返回工作对象列表
     */
    @Override
    public Map<String, Map<String, CommandJob>> load(String... packagePath) {
        Map<String, Map<String, CommandJob>> result = FileOperationUtil.readCommandData();
        if (CollectionUtil.isEmpty(result)) {
            // 如果配置文件没有内容则扫描所有包，重新写入配置，这个过程可能会比较久
            result = CollectionUtil.newHashMap();
            Reflections reflections = new Reflections();
            if (ArrayUtil.isNotEmpty(packagePath)) {
                reflections = new Reflections(new ConfigurationBuilder().forPackages(packagePath));
            }

            Set<Class<? extends CommandJob>> subClassList = reflections.getSubTypesOf(CommandJob.class);
            for (Class<? extends CommandJob> subClass : subClassList) {
                CommandJob commandJob = Singleton.get(subClass);
                CommandConfig commandConfig = commandJob.getCommandConfig();
                if (null == commandConfig) {
                    PrintUtil.println("warn:" + commandJob.getClass() + "没有配置命令，该命令将无法使用！");
                } else {
                    String commandKey = commandConfig.getCommandKey();
                    String group = commandConfig.getGroup();
                    Map<String, CommandJob> commandJobMap = result.get(group);
                    if (CollectionUtil.isEmpty(commandJobMap)) {
                        // 如果为空则创建并加入到最终的map中
                        commandJobMap = CollectionUtil.newHashMap();
                        result.put(group, commandJobMap);
                    }
                    commandJobMap.put(commandKey, commandJob);
                }
            }
            // 转换为配置所需的格式，然后重新写入文件
            Map<String, Map<String, String>> configMap = toConfigMap(result);
            FileOperationUtil.coverAppendConfig(Constant.COMMAND_DATA_PATH, JSONObject.toJSONString(configMap));
        }
        commandMap = result;
        return result;
    }

    /**
     * 重新加载工作
     *
     * @param packagePath 指定路径，可以多个，如果为空则全盘扫描
     * @return 返回工作数据
     */
    @Override
    public Map<String, Map<String, CommandJob>> reload(String... packagePath) {
        IoUtil.write(FileUtil.getOutputStream(Constant.COMMAND_DATA_PATH), true, "{}".getBytes());
        return load(packagePath);
    }

    /**
     * 添加命令，可以多个
     *
     * @param commandJobList 命令列表
     * @return 成功返回命令列表
     */
    @Override
    public List<CommandJob> add(List<CommandJob> commandJobList) {
        if (CollectionUtil.isEmpty(commandJobList)) {
            return commandJobList;
        }
        // 加入到内存中
        for (CommandJob commandJob : commandJobList) {
            CommandConfig commandConfig = commandJob.getCommandConfig();
            String group = commandConfig.getGroup();
            String commandKey = commandConfig.getCommandKey();
            Map<String, CommandJob> commandJobMap = commandMap.get(group);
            if (CollectionUtil.isEmpty(commandJobMap)) {
                commandJobMap = CollectionUtil.newHashMap();
                commandMap.put(group, commandJobMap);
            }
            commandJobMap.put(commandKey, commandJob);
        }
        // 持久化
        Map<String, Map<String, String>> configMap = toConfigMap(commandMap);
        FileOperationUtil.coverAppendConfig(Constant.COMMAND_DATA_PATH, JSONObject.toJSONString(configMap));
        return commandJobList;
    }

    /**
     * 获得工作列表
     *
     * @param group 组名
     * @return 返回工作列表
     */
    @Override
    public Collection<CommandJob> getCommandJobList(String group) {
        Collection<CommandJob> result = new HashSet<>();
        if (StrUtil.isNotBlank(group)) {
            Map<String, CommandJob> commandJobMap = commandMap.get(group);
            if (null != commandJobMap) {
                result.addAll(commandJobMap.values());
            }
        } else {
            for (String key : commandMap.keySet()) {
                Map<String, CommandJob> commandJobMap = commandMap.get(key);
                result.addAll(commandJobMap.values());
            }
        }
        return result;
    }

    /**
     * 获得指定的工作对象
     *
     * @param commandKey 命令名
     * @return 返回工作对象
     */
    @Override
    public CommandJob getCommandJob(String commandKey) {
        for (String group : commandMap.keySet()) {
            Map<String, CommandJob> commandJobMap = commandMap.get(group);
            CommandJob commandJob = commandJobMap.get(commandKey);
            if (null != commandJob) {
                return commandJob;
            }
        }
        return null;
    }


    /**
     * 将命令数据转换配置所需的格式
     * 配置文件中只存储CommandJob对象的类型，不存储序列化后的json对象，因为那样会有问题。
     * 所以配置只存类型，然后读取的时候再根据类型创建具体的对象
     *
     * @param commandData 命令数据
     * @return 返回配置所需的格式
     */
    private Map<String, Map<String, String>> toConfigMap(Map<String, Map<String, CommandJob>> commandData) {
        Map<String, Map<String, String>> result = CollectionUtil.newHashMap();
        for (String group : commandData.keySet()) {

            Map<String, String> commandJobMapString = CollectionUtil.newHashMap();

            Map<String, CommandJob> commandJobMap = commandData.get(group);
            for (String commandKey : commandJobMap.keySet()) {
                String className = commandJobMap.get(commandKey).getClass().getName();
                commandJobMapString.put(commandKey, className);
            }

            result.put(group, commandJobMapString);
        }
        return result;
    }
}
