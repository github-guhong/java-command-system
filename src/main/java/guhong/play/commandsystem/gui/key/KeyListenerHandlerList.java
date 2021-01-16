package guhong.play.commandsystem.gui.key;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ReflectUtil;
import guhong.play.commandsystem.gui.key.system.BackspaceHandler;
import guhong.play.commandsystem.gui.key.system.EnterHandler;
import guhong.play.commandsystem.gui.key.system.EscHandler;
import guhong.play.commandsystem.util.print.PrintUtil;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * 这是一个键盘监听处理器列表对象
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public class KeyListenerHandlerList extends HashSet<KeyListenerHandler> {

    public KeyListenerHandlerList() {
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages("guhong.play.commandsystem.gui.key"));
        Set<Class<? extends KeyListenerHandler>> subTypesOf = reflections.getSubTypesOf(KeyListenerHandler.class);
        if (CollectionUtil.isNotEmpty(subTypesOf)) {
            for (Class<? extends KeyListenerHandler> aClass : subTypesOf) {
                try {
                    KeyListenerHandler keyListenerHandler = ReflectUtil.newInstance(aClass);
                    super.add(keyListenerHandler);
                } catch (Exception e) {
                    PrintUtil.warnPrint(aClass.getName() + "实例化失败，这会导致你无法监听指定的按键。");
                }
            }
        }
    }
}
