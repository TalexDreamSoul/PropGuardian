package com.tagzxia3.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddon;
import com.talexframe.frame.core.modules.network.connection.app.addon.cache.redis.ReceiverCacheRedisAddon;
import com.talexframe.frame.core.modules.network.connection.app.addon.limit.ReceiverLimitAddon;
import com.talexframe.frame.core.modules.network.connection.app.addon.login.ReceiverLoginAddon;
import com.talexframe.frame.core.modules.network.connection.app.addon.method.ReceiverMethodAddon;
import com.talexframe.frame.core.modules.network.connection.app.addon.param.ReceiverParamAddon;
import com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.assertions.ReceiverAssertValidateAddon;
import com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.max.length.ReceiverMaxLengthValidateAddon;
import com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.max.value.ReceiverMaxValueValidateAddon;
import com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.min.length.ReceiverMinLengthValidateAddon;
import com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.min.value.ReceiverMinValueValidateAddon;
import com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.pattern.ReceiverPatternValidateAddon;
import com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.sql.ReceiverParamSqlValidateAddon;
import com.talexframe.frame.core.modules.network.connection.app.addon.permission.ReceiverPermissionAddon;
import com.talexframe.frame.core.modules.plugins.adapt.PluginCompAdapter;
import com.talexframe.frame.core.modules.plugins.addon.PluginScanner;
import com.talexframe.frame.core.modules.plugins.core.WebPlugin;
import com.talexframe.frame.core.talex.FrameCreator;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@link com.talexframe.frame.core.modules.application Package }
 *
 * @author TalexDreamSoul 22/04/02 下午 08:44 Project: TalexFrame
 */
public class ReceiverAddonAdapter extends PluginCompAdapter<com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddon> {

    private static final Map<Class<?>, com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddon> receiverAddonMap = new HashMap<>();

    private static final Multimap<com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddon.ReceiverAddonType, com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddon> receiverAddonTypeMap = ArrayListMultimap.create();

    public static LinkedList<com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddon> getReceiverAddons(com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddon.ReceiverAddonType type) {

        return receiverAddonTypeMap.get(type).stream().sorted((o1, o2) -> o2.getPriority().getPriority() - o1.getPriority().getPriority()).collect(Collectors.toCollection(LinkedList::new));

    }

    public ReceiverAddonAdapter() {

        // 初始化内部的插件

        register(new ReceiverLoginAddon());
        register(new ReceiverPermissionAddon());
        register(new ReceiverLimitAddon());
        register(new ReceiverMethodAddon());
        register(new ReceiverParamAddon());

        register(new ReceiverCacheRedisAddon());

        register(new ReceiverPatternValidateAddon());
        register(new ReceiverAssertValidateAddon());
        register(new ReceiverMaxLengthValidateAddon());
        register(new ReceiverMaxValueValidateAddon());
        register(new ReceiverMinLengthValidateAddon());
        register(new ReceiverMinValueValidateAddon());
        register(new ReceiverParamSqlValidateAddon());

    }

    private void register(com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddon instance) {

        for( com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddon.ReceiverAddonType type : instance.getAddonType() ) {

            receiverAddonTypeMap.put(type, instance);

        }

        receiverAddonMap.put(instance.getClass(), instance);

    }

    @SneakyThrows
    @Override
    public boolean injectWithInstance(PluginScanner scanner, WebPlugin webPlugin, com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddon instance) {

        if( receiverAddonMap.containsKey(instance.getClass()) ) {

            return false;

        }

        register(instance);

        return true;

    }

    @Override
    public boolean logoutInstance(PluginScanner scanner, WebPlugin webPlugin, Class<? extends FrameCreator> clazz) {

        if( !receiverAddonMap.containsKey(clazz) ) {

            return false;

        }

        com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddon receiverAddon = receiverAddonMap.get(clazz);

        for( ReceiverAddon.ReceiverAddonType type : receiverAddon.getAddonType() ) {

            receiverAddonTypeMap.remove(type, receiverAddon);

        }

        receiverAddonMap.remove(clazz);

        return false;

    }

}
