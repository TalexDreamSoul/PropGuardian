package com.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.plugins.adapt.config.yaml;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.HashMultimap;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.plugins.adapt.config.yaml.YamlConfig;
import com.talexframe.frame.core.modules.plugins.adapt.PluginCompAdapter;
import com.talexframe.frame.core.modules.plugins.addon.PluginScanner;
import com.talexframe.frame.core.modules.plugins.core.WebPlugin;
import com.talexframe.frame.core.pojo.annotations.TValueConfig;
import com.talexframe.frame.core.talex.FrameCreator;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * {@link com.talexframe.frame.core.modules.plugins.adapt.config Package }
 *
 * @author TalexDreamSoul 22/04/17 上午 07:58 Project: TalexFrame
 */
public class YamlConfigAdapter extends PluginCompAdapter<YamlConfig> {

    private static final HashMultimap<Class<?>, YamlConfig> map = HashMultimap.create();

    @SneakyThrows
    @Override
    protected boolean injectWithInstance(PluginScanner scanner, WebPlugin webPlugin, YamlConfig instance) {

        map.get(instance.getClass()).forEach(yamlConfig -> {

            if( yamlConfig.getPath().equalsIgnoreCase(instance.getPath()) ){

                tframe.crash(new RuntimeException(yamlConfig.getPath() + " is already exist"));

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

        YamlConfiguration yaml = new YamlConfiguration();

        yaml.load( file );

        Class<?> clz = instance.getClass();

        TValueConfig classAnno = clz.getAnnotation(TValueConfig.class);

        if( classAnno != null ) {

            for( Field field : clz.getDeclaredFields() ) {

                String name = StrUtil.isBlankIfStr(classAnno.name()) ? field.getName() : classAnno.name();

                field.setAccessible( true );

                if( yaml.contains(name) ) {

                    Type type = field.getType();

                    if( type == String.class ) {

                        field.set( instance, yaml.getString( name ) );

                    } else field.set( instance, yaml.get( name ) );

                } else if( classAnno.ignore() ) {

                    tframe.getFrameSender().warnConsoleMessage("[JSONConfigAdapter] " + instance.getPath() + " not found - The plugin allow ignore it.");

                } else {

                    tframe.crash(new RuntimeException("[JSONConfigAdapter] " + instance.getPath() + "/@Yaml " + name + " - The plugin will not work without it! For app: " + clz.getName()));

                    return false;

                }

            }

        } else {

            for( Field field : clz.getDeclaredFields() ) {

                TValueConfig fieldAnno = field.getAnnotation(TValueConfig.class);
                if( fieldAnno == null ) continue;

                String name = StrUtil.isBlankIfStr(fieldAnno.name()) ? field.getName() : fieldAnno.name();

                field.setAccessible(true);

                if( yaml.contains(name) ) {

                    Type type = field.getType();

                    if( type == String.class ) {

                        field.set( instance, yaml.getString( name ) );

                    } else field.set( instance, yaml.get( name ) );

                } else if( fieldAnno.ignore() ) {

                    tframe.getFrameSender().warnConsoleMessage("[JSONConfigAdapter] " + instance.getPath() + "/@Yaml " + name + " not found - The plugin allow ignore it.");

                } else {

                    tframe.crash(new RuntimeException("[JSONConfigAdapter] " + instance.getPath() + "/@Yaml " + name + " - The plugin will not work without it! For app: " + clz.getName()));

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

        Set<YamlConfig> configs = map.get(clazz);

        for( YamlConfig config : configs ) {

            YamlConfiguration yaml = new YamlConfiguration();
            TValueConfig classAnno = clazz.getAnnotation(TValueConfig.class);

            if( classAnno != null ) {

                for( Field field : clazz.getDeclaredFields() ) {

                    String name = StrUtil.isBlankIfStr(classAnno.name()) ? field.getName() : classAnno.name();

                    field.setAccessible( true );

                    Object value = field.get( config );

                    if( value != null ) {

                        yaml.set( name, value );

                    } else if( classAnno.ignore() ) {

                        tframe.getFrameSender().warnConsoleMessage("[JSONConfigAdapter] " + config.getPath() + "/@Yaml " + name + " not found - The plugin allow ignore it.");

                    } else {

                        tframe.crash(new RuntimeException("[JSONConfigAdapter] " + config.getPath() + "/@Yaml " + name + " - The plugin will not work without it! For app: " + clazz.getName()));

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

                        yaml.set( name, value );

                    } else if( fieldAnno.ignore() ) {

                        tframe.getFrameSender().warnConsoleMessage("[JSONConfigAdapter] " + config.getPath() + "/@Yaml " + name + " not found - The plugin allow ignore it.");

                    } else {

                        tframe.crash(new RuntimeException("[JSONConfigAdapter] " + config.getPath() + "/@Yaml " + name + " - The plugin will not work without it! For app: " + clazz.getName()));

                        return false;

                    }

                }

            }

            yaml.save( new File(webPlugin.getPluginDataFolder(), config.getPath()) );

        }

        return true;
    }

}
