package guhong.play.commandsystem.gui.key;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ReflectUtil;
import guhong.play.commandsystem.gui.key.system.BackspaceHandler;
import guhong.play.commandsystem.gui.key.system.EnterHandler;
import guhong.play.commandsystem.gui.key.system.EscHandler;
import guhong.play.commandsystem.gui.key.type.KeyType;
import guhong.play.commandsystem.util.print.PrintUtil;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.util.*;

/**
 * 这是一个键盘监听处理器管理器对象
 *
 * @author : 李双凯
 * @date : 2019-11-20 22:32
 **/
public class KeyListenerHandlerManage extends HashMap<KeyType, List<KeyListenerHandler>> {

    public KeyListenerHandlerManage() {
        // 初始化容器
        for (KeyType keyType : KeyType.values()) {
            super.put(keyType, CollectionUtil.newArrayList());
        }

        // 扫描监听，并实例化，转载到容器中
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages("guhong.play.commandsystem.gui.key"));
        Set<Class<? extends KeyListenerHandler>> subTypesOf = reflections.getSubTypesOf(KeyListenerHandler.class);
        if (CollectionUtil.isNotEmpty(subTypesOf)) {
            for (Class<? extends KeyListenerHandler> aClass : subTypesOf) {
                try {
                    KeyListenerHandler keyListenerHandler = ReflectUtil.newInstance(aClass);
                    for (KeyType keyType : KeyType.values()) {
                        if (keyListenerHandler.type().equals(keyType)) {
                            super.get(keyType).add(keyListenerHandler);
                        }
                    }
                } catch (Exception e) {
                    PrintUtil.warnPrint(aClass.getName() + "实例化失败，这会导致你无法监听指定的按键。");
                }
            }
        }
    }

}
