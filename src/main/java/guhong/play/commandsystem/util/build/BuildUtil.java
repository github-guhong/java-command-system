package guhong.play.commandsystem.util.build;

import cn.hutool.core.io.FileUtil;
import guhong.play.commandsystem.util.windows.CmdUtil;
import lombok.Data;

import java.io.File;

/**
 * 打包工具
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class BuildUtil {


    /**
     * 将jar安装到本地仓库
     * @param jarPath jar的路径
     * @param groupId 组织名
     * @return 成功返回true
     */
    public static boolean installToLocalhost(String jarPath, String groupId) {
        if (!FileUtil.exist(jarPath)) {
            throw new RuntimeException(jarPath + "不存在！");
        }
        File jar = new File(jarPath);
        String name = jar.getName();

        String projectName = name.substring(0, name.lastIndexOf("-"));
        String versionNumber = name.substring(name.lastIndexOf("-")+1, name.lastIndexOf("."));

        String command = "mvn install:install-file -Dfile="+jarPath+" -DgroupId="+groupId+" -DartifactId="+projectName+" -Dversion="+versionNumber+" -Dpackaging=jar";
        System.out.println(command);
        Process process = CmdUtil.exec(command);
        CmdUtil.printProcess(process);
        return CmdUtil.isSuccess(process);
    }




}
