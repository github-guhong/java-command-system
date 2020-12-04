package test;

import cn.hutool.core.io.IoUtil;
import guhong.play.commandsystem.constant.Constant;
import guhong.play.commandsystem.job.CommandJob;
import guhong.play.commandsystem.util.FileOperationUtil;
import guhong.play.commandsystem.util.ToolUtil;
import guhong.play.commandsystem.util.windows.CmdUtil;
import lombok.Data;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.nio.charset.Charset;
import java.util.Set;

/**
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class TestContent {


    @Test
    public void test1() {
        Boolean blankParam = ToolUtil.isBlankParam("\"\"");
        System.out.println(blankParam);
    }

    @Test
    public void test2() {
        System.out.println(System.getProperties());
    }


    @Test
    public void test3() {
        System.out.println(Constant.COMMAND_DATA_PATH);
        System.out.println(Constant.PROJECT_PATH);
    }

    @Test
    public void test4() {
        Process process = CmdUtil.exec("dfa 1");
        String error = IoUtil.read(process.getErrorStream(), Charset.defaultCharset());
        String input = IoUtil.read(process.getInputStream(), Charset.defaultCharset());
        System.out.println("error"+error);
        System.out.println("input:"+input);
        System.out.println("value:"+process.exitValue());
        System.out.println("isAlive:"+process.isAlive());
        System.out.println("isSuccess:"+CmdUtil.isSuccess(process));
    }

    @Test
    public void test5() {
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages("guhong.play.commandsytemsys"));
        Set<Class<? extends CommandJob>> subClassList = reflections.getSubTypesOf(CommandJob.class);
        System.out.println(subClassList);
    }
}
