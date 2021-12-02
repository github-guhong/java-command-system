package guhong.play.commandsystem.job.other.md.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import guhong.play.commandsystem.util.ToolUtil;
import guhong.play.commandsystem.util.print.PrintUtil;
import lombok.Data;
import org.reflections.Reflections;

import java.util.List;
import java.util.Set;

/**
 * 美化服务容器
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
@Data
public class BeautifyServiceContainer {

    private final List<BeautifyService> beautifyServiceList;

    public BeautifyServiceContainer() {
        beautifyServiceList = CollectionUtil.newArrayList();
        try {
            Reflections reflections = new Reflections("guhong.play.commandsystem.job.other.md.service.impl");
            Set<Class<? extends BeautifyService>> subTypesOf = reflections.getSubTypesOf(BeautifyService.class);
            if (CollectionUtil.isNotEmpty(subTypesOf)) {
                for (Class<? extends BeautifyService> aClass : subTypesOf) {
                    beautifyServiceList.add(ReflectUtil.newInstance(aClass));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            PrintUtil.warnPrint(ExceptionUtil.getRootCauseMessage(e));
        }
    }

    public List<BeautifyService> getBeautifyServiceList() {
        if (CollectionUtil.isEmpty(beautifyServiceList)) {
            PrintUtil.errorPrint("没有找到任何美化服务，程序无法运行！");
        }
        return beautifyServiceList;
    }


    public List<BeautifyService> choiceBeautifyService(String choice) {
        List<BeautifyService> beautifyServiceList = getBeautifyServiceList();
        if (StrUtil.isBlank(choice)) {
            PrintUtil.println("你没有选择任何服务，默认执行全部");
            PrintUtil.printWithNumberByName(beautifyServiceList);
            return beautifyServiceList;
        }

        String[] split = choice.split(",");
        List<BeautifyService> result = CollectionUtil.newArrayList();
        for (String index : split) {
            if (!ToolUtil.isInteger(index)) {
                continue;
            }
            int i = Integer.parseInt(index);
            if (i < 0 || i > beautifyServiceList.size()) {
                continue;
            }
            result.add(beautifyServiceList.get(i - 1));
        }

        if (CollectionUtil.isEmpty(result)) {
            PrintUtil.println("你没有选择任何服务！");
        } else {
            PrintUtil.println("以下是你选择的有效的服务：");
            PrintUtil.printWithNumberByName(result);
        }
        return result;
    }

}
