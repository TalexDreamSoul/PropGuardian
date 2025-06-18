package com.tagzxia3.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.application;

import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.application.TApp;
import com.talexframe.frame.core.modules.event.events.app.AppPostRegisterEvent;
import com.talexframe.frame.core.modules.event.events.app.AppPreRegisterEvent;
import com.talexframe.frame.core.modules.event.events.app.AppUnRegisteredEvent;
import com.talexframe.frame.core.modules.network.interfaces.IUnRegisterHandler;
import com.talexframe.frame.core.modules.plugins.core.WebPlugin;
import com.talexframe.frame.core.modules.repository.TRepo;
import com.talexframe.frame.core.modules.repository.TRepoManager;
import com.talexframe.frame.core.pojo.annotations.TRepoInject;
import com.talexframe.frame.core.talex.TFrame;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <br /> {@link com.talexframe.frame.core.modules.application Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 18:49 <br /> Project: TalexFrame <br />
 */
@Getter
public class TAppManager {

    private static TAppManager manager;
    private final ConcurrentMap<Class<?>, TApp> controllers = new ConcurrentHashMap<>(32);
    private final ConcurrentMap<TApp, String> controllerPluginMap = new ConcurrentHashMap<>(32);

    private TAppManager() {


    }

    public static TAppManager init() {

        if ( manager == null ) {
            manager = new TAppManager();
        }

        return manager;

    }

    /**
     * 注册一个管理器
     *
     * @param plugin     管理器插件
     * @param controller 管理器
     *
     * @return 注册是否成功
     */
    @SneakyThrows
    public boolean registerController(WebPlugin plugin, TApp controller) {

        if ( this.controllers.containsKey(controller.getClass()) ) {

            return false;

        }

        AppPreRegisterEvent event = new AppPreRegisterEvent(plugin, controller);

        TFrame.tframe.callEvent(event);

        if( event.isCancelled() ) return false;

        this.controllers.put(controller.getClass(), controller);
        this.controllerPluginMap.put(controller, plugin.getName());

        TRepoManager repoManager = TFrame.tframe.getRepoManager();

        /*

          扫描类中所有字段 带有 TRepInject 的字段，自动从 TRepoManager 中根据字段类型注入

         */
        for ( Field field : controller.getClass().getDeclaredFields() ) {

            TRepoInject repoInject = field.getAnnotation(TRepoInject.class);

            if ( repoInject != null ) {

                Class<?> repClz = field.getType();

                field.setAccessible(true);

                TRepo tRep = repoManager.getASRepoByClass(repClz);

                if ( tRep == null ) {

                    throw new NullPointerException("Inject repo with null - " + repClz.getName());

                }

                field.set(controller, tRep);

            }

        }

        TFrame.tframe.callEvent(new AppPostRegisterEvent(plugin, controller));

        return true;

    }

    /**
     * 注销一个储存库
     *
     * @param plugin     储存库插件
     * @param controller 管理器
     *
     * @return 注销是否成功
     */
    public boolean unRegisterController(WebPlugin plugin, TApp controller) {

        if ( !this.controllers.containsKey(controller.getClass()) ) {

            return false;

        }

        if ( controller instanceof IUnRegisterHandler ) {

            ( (IUnRegisterHandler) controller ).onUnRegister();

        }

        this.controllers.remove(controller.getClass(), controller);
        this.controllerPluginMap.remove(controller, plugin.getName());

        TFrame.tframe.callEvent(new AppUnRegisteredEvent(plugin, controller));

        return true;

    }

    public static class TAppAnnoGetter<T extends Annotation> {

        public TAppAnnoGetter() {



        }

        public List<T> getAnnos(Class<T> templateCls) {

            List<T> list = new ArrayList<>();

            manager.controllers.keySet().forEach((cls) -> {

                for( Method method : cls.getMethods() ) {

                    Annotation[] annotationsByType = method.getAnnotationsByType(templateCls);

                    for ( Annotation anno : annotationsByType ) {

                        list.add((T) anno);

                    }

                }

            });

            return list;

        }

    }

}
