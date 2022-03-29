package guhong.play.commandsystem;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import guhong.play.commandsystem.constant.CommandMode;
import guhong.play.commandsystem.constant.Constant;
import guhong.play.commandsystem.dto.CommandDto;
import guhong.play.commandsystem.dto.entity.SystemConfig;
import guhong.play.commandsystem.exception.SystemException;
import guhong.play.commandsystem.gui.history.CommandHistoryManage;
import guhong.play.commandsystem.gui.terminal.Terminal;
import guhong.play.commandsystem.job.CommandJob;
import guhong.play.commandsystem.util.file.FileOperationUtil;
import guhong.play.commandsystem.util.print.PrintUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 命令管理器
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public class CommandManager {


    /**
     * 系统配置对象
     */
    @Getter
    private static SystemConfig systemConfig = null;

    /**
     * 命令传输对象
     */
    @Getter
    private static CommandDto commandDto = null;

    /**
     * 终端对象
     */
    @Getter
    @Setter
    private static Terminal terminal;


    private CommandManager() {
    }


    public static void execute(String commandStr) {
        String commandKey = getCommandKey(commandStr);
        if (null == commandKey) {
            return;
        }
        CommandJob commandJob = commandDto.getCommandJob(commandKey);
        if (null == commandJob) {
            CommandMode mode = systemConfig.getMode();
            commandKey = mode.getCommandKey();
            commandStr = commandKey + " " + commandStr;
            commandJob = commandDto.getCommandJob(commandKey);
            if (null == commandJob) {
                PrintUtil.errorPrint("命令模式【"+mode+"】设置了一个不存在的命令Key【"+commandKey+"】请检查！");
                return;
            }
        }
        try {
            commandJob.doStart(commandStr);
        } catch (Exception e) {
            e.printStackTrace();
            PrintUtil.errorPrint("命令执行失败：" + ExceptionUtil.getRootCause(e).getMessage());
        } finally {
            // 记录历史,刷新索引
            CommandHistoryManage historyCommandManage = terminal.getHistoryCommandManage();
            historyCommandManage.add(commandStr);
            historyCommandManage.flush();

        }
    }


    /**
     * 获得命令名
     *
     * @param commandStr 命令字符串
     * @return 返回命令名
     */
    public static String getCommandKey(String commandStr) {
        if (StrUtil.isBlank(commandStr)) {
            return null;
        }
        commandStr = commandStr.trim();
        String[] split = commandStr.split("\\s+");
        String commandKey = split[0];
        if (StrUtil.isBlank(commandKey)) {
            return null;
        }
        return commandKey;
    }


    /**
     * 初始化
     */
    public static void init() {
        try {
            // 读取配置文件
            systemConfig = FileOperationUtil.readSystemConfig();
            if (null == systemConfig) {
                // 使用默认配置
                systemConfig = SystemConfig.buildDefaultConfig();
                // 将默认配置再存入配置文件
                FileOperationUtil.coverAppendConfig(Constant.SYSTEM_CONFIG_FILE, JSONObject.toJSONString(systemConfig));
                PrintUtil.warnPrint("读取配置文件失败，已使用默认配置！");
            }

            // 使用命令传输对象加载工作
            commandDto = ReflectUtil.newInstance(systemConfig.getDto());
            if (null == commandDto) {
                throw new SystemException("命令传输对象无法初始化，请检查配置！");
            }
            commandDto.load(null);

            // 执行命令的初始化
            for (CommandJob commandJob : commandDto.getCommandJobList(null)) {
                commandJob.windowOpened();
            }
        } catch (Exception e) {
            PrintUtil.errorPrint("命令管理器初始化失败：" + e.getMessage());
        }
    }

    /**
     * 结束
     */
    public static void end() {
        // 执行命令的结束回调
        for (CommandJob commandJob : commandDto.getCommandJobList(null)) {
            commandJob.windowClosing();
        }
        System.exit(0);
    }


}
