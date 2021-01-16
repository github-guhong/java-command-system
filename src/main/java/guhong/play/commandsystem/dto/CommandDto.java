package guhong.play.commandsystem.dto;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import guhong.play.commandsystem.job.CommandJob;
import lombok.Data;
import org.reflections.Reflections;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 命令传输对象
 * 读取所有的命令工作
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public interface CommandDto {


    /**
     * 加载所有工作
     *
     * @param packagePath 指定路径，可以多个，如果为空则全盘扫描
     * @return 返回工作数据
     */
    public Map<String, Map<String, CommandJob>> load(String... packagePath);

    /**
     * 重新加载工作
     *
     * @param packagePath 指定路径，可以多个，如果为空则全盘扫描
     * @return 返回工作数据
     */
    public Map<String, Map<String, CommandJob>> reload(String... packagePath);

    /**
     * 添加命令，可以多个
     *
     * @param commandJobList 命令列表
     * @return 成功返回命令列表
     */
    public List<CommandJob> add(List<CommandJob> commandJobList);

    /**
     * 获得工作列表
     *
     * @param group 组名
     * @return 返回工作列表
     */
    public Collection<CommandJob> getCommandJobList(String group);


    /**
     * 获得指定的工作对象
     *
     * @param commandKey 命令名
     * @return 返回工作对象
     */
    public CommandJob getCommandJob(String commandKey);
}
