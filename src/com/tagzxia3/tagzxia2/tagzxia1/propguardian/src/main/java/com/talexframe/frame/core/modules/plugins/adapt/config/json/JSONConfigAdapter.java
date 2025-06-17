package com.tagzxia3.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.plugins.adapt.config.json;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.HashMultimap;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.plugins.adapt.config.json.JSONConfig;
import com.talexframe.frame.core.modules.plugins.adapt.PluginCompAdapter;
import com.talexframe.frame.core.modules.plugins.addon.PluginScanner;
import com.talexframe.frame.core.modules.plugins.core.WebPlugin;
import com.talexframe.frame.core.pojo.annotations.TValueConfig;
import com.talexframe.frame.core.talex.FrameCreator;
import lombok.SneakyThrows;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * {@link com.talexframe.frame.core.modules.plugins.adapt.config Package }
 *
 * @author TalexDreamSoul 22/04/17 上午 08:59 Project: TalexFrame
 */
public class JSONConfigAdapter extends PluginCompAdapter<JSONConfig> {

    private static final HashMultimap<Class<?>, JSONConfig> map = HashMultimap.create();

    @SneakyThrows
    @Override
    protected boolean injectWithInstance(PluginScanner scanner, WebPlugin webPlugin, JSONConfig instance) {

        map.get(instance.getClass()).forEach(jsonConfig -> {

            if( jsonConfig.getPath().equalsIgnoreCase(instance.getPath()) ){

                tframe.crash(new RuntimeException(jsonConfig.getPath() + " is already exist"));

            }

        });

        File file = new File(webPlugin.getPluginDataFolder(), instance.getPath());

        if( !file.exists() ) {

            if( instance.isRequired() ) {

                tframe.getFrameSender().warnConsoleMessage("[JSONConfigAdapter] " + instance.getPath() + " not found - The plugin will not work without it!");

                tframe.crash(new RuntimeException(instance.getPath() + " not found - The plugin will not work without it!"));

            } else {

                tframe.getFrameSender().sendConsoleMessage("[JSONConfigAdapter] " + instance.getPath() + " not found!");

            }

            return false;

        }

        JSONObject jsonObject = JSONUtil.readJSONObject(file, StandardCharsets.UTF_8);

        Class<?> clz = instance.getClass();

        TValueConfig classAnno = clz.getAnnotation(TValueConfig.class);

        if( classAnno != null ) {

            for( Field field : clz.getDeclaredFields() ) {

                String name = StrUtil.isBlankIfStr(classAnno.name()) ? field.getName() : classAnno.name();

                field.setAccessible( true );

                if( jsonObject.containsKey(name) ) {

                    field.set( instance, jsonObject.get( name, field.getType() ) );

                } else if( classAnno.ignore() ) {

                    tframe.getFrameSender().warnConsoleMessage("[JSONConfigAdapter] " + instance.getPath() + " not found - The plugin allow ignore it.");

                } else {

                    tframe.crash(new RuntimeException("[JSONConfigAdapter] " + instance.getPath() + "/@JSON " + name + " - The plugin will not work without it! For app: " + clz.getName()));

                    return false;

                }

            }

        } else {

            for( Field field : clz.getDeclaredFields() ) {

                TValueConfig fieldAnno = field.getAnnotation(TValueConfig.class);
                if( fieldAnno == null ) continue;

                String name = StrUtil.isBlankIfStr(fieldAnno.name()) ? field.getName() : fieldAnno.name();

                field.setAccessible(true);

                if( jsonObject.containsKey(name) ) {

                    field.set( instance, jsonObject.get( name, field.getType() ) );

                } else if( fieldAnno.ignore() ) {

                    tframe.getFrameSender().warnConsoleMessage("[JSONConfigAdapter] " + instance.getPath() + "/@JSON " + name + " not found - The plugin allow ignore it.");

                } else {

                    tframe.crash(new RuntimeException("[JSONConfigAdapter] " + instance.getPath() + "/@JSON " + name + " - The plugin will not work without it! For app: " + clz.getName()));

                    return false;

                }

            }

        }

        if( classAnno == null || !classAnno.disable() )
            map.put( clz, instance );

        return true;
    }

    @SneakyThrows
    @Override
    public boolean logoutInstance(PluginScanner scanner, WebPlugin webPlugin, Class<? extends FrameCreator> clazz) {

        Set<JSONConfig> configs = map.get(clazz);

        for( JSONConfig config : configs ) {

            JSONObject json = new JSONObject();
            TValueConfig classAnno = clazz.getAnnotation(TValueConfig.class);

            if( classAnno != null ) {

                for( Field field : clazz.getDeclaredFields() ) {

                    String name = StrUtil.isBlankIfStr(classAnno.name()) ? field.getName() : classAnno.name();

                    field.setAccessible( true );

                    Object value = field.get( config );

                    if( value != null ) {

                        json.set( name, value );

                    } else if( classAnno.ignore() ) {

                        tframe.getFrameSender().warnConsoleMessage("[JSONConfigAdapter] " + config.getPath() + "/@json " + name + " not found - The plugin allow ignore it.");

                    } else {

                        tframe.crash(new RuntimeException("[JSONConfigAdapter] " + config.getPath() + "/@json " + name + " - The plugin will not work without it! For app: " + clazz.getName()));

                        return false;

                    }

                }

            } else {

                for( Field field : clazz.getDeclaredFields() ) {

                    TValueConfig fieldAnno = field.getAnnotation(TValueConfig.class);
                    if( fieldAnno == null ) continue;
                    if( fieldAnno.disable() ) continue;

                    String name = StrUtil.isBlankIfStr(fieldAnno.name()) ? field.getName() : fieldAnno.name();

                    Object value = field.get( config );
                    field.setAccessible( true );

                    if( value != null ) {

                        json.set( name, value );

                    } else if( fieldAnno.ignore() ) {

                        tframe.getFrameSender().warnConsoleMessage("[JSONConfigAdapter] " + config.getPath() + "/@json " + name + " not found - The plugin allow ignore it.");

                    } else {

                        tframe.crash(new RuntimeException("[JSONConfigAdapter] " + config.getPath() + "/@json " + name + " - The plugin will not work without it! For app: " + clazz.getName()));

                        return false;

                    }

                }

            }

            File file = new File(webPlugin.getPluginDataFolder(), config.getPath());

            FileUtil.writeString(json.toString(), file, StandardCharsets.UTF_8);

        }

        return true;

    }

}
