package guhong.play.commandsystem;

import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSONObject;
import guhong.play.commandsystem.constant.Constant;
import guhong.play.commandsystem.exception.SystemException;
import guhong.play.commandsystem.util.FileOperationUtil;
import guhong.play.commandsystem.dto.entity.SystemConfig;
import guhong.play.commandsystem.job.CommandJob;
import guhong.play.commandsystem.dto.CommandDto;
import guhong.play.commandsystem.exception.NotCommandException;
import guhong.play.commandsystem.util.ToolUtil;
import guhong.play.commandsystem.util.print.PrintUtil;
import guhong.play.commandsystem.util.windows.CmdUtil;
import lombok.Getter;

/**
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


    private CommandManager() {}


    public static void execute(String commandStr) {
        String commandKey = ToolUtil.getCommandKey(commandStr);
        if (null == commandKey) {
            throw new NotCommandException(commandStr);
        } else {
            CommandJob commandJob = commandDto.getCommandJob(commandKey);
            if (null == commandJob) {
                // 如果没有找到则直接执行windows命令，如果windows命令也执行失败，那就抛出异常
                Process process = CmdUtil.exec(commandStr);
                if (null == process) {
                    throw new NotCommandException(commandKey);
                } else {
                    CmdUtil.printProcess(process);
                }
            } else {
                commandJob.doStart(commandStr);
            }
        }
    }



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
        } catch (Exception e) {
            PrintUtil.errorPrint("命令管理器初始化失败："+e.getMessage());
        }

    }



}
