package com.tagzxia.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.event;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.event.FrameListener;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.event.MethodManager;
import com.talexframe.frame.core.modules.event.service.IContinue;
import com.talexframe.frame.core.modules.event.service.IEventBus;
import com.talexframe.frame.core.modules.event.service.THandler;
import com.talexframe.frame.core.modules.event.service.TalexEvent;
import com.talexframe.frame.core.pojo.enums.ThreadMode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author TalexDreamSoul
 */
@Slf4j
public class TalexEventBus implements IEventBus {

    private static TalexEventBus INSTANCE;

    @Getter
    private final Multimap<com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.event.FrameListener, com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.event.MethodManager> listenerManager = ArrayListMultimap.create();

    @Getter
    private final Map<String, TalexEvent> continueEvents = new HashMap<>(16);

    private TalexEventBus() {

    }

    public static TalexEventBus getDefault() {

        if ( INSTANCE == null ) {

            INSTANCE = new TalexEventBus();

            return INSTANCE;

        }

        return null;

    }

    @Override
    public TalexEventBus registerListener(com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.event.FrameListener listener) {

        if( listenerManager.containsKey(listener) ) {

            return this;

        }

        List<com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.event.MethodManager> methodsList = new ArrayList<>(listenerManager.get(listener));

        Class<?> clazz = listener.getClass();

        Arrays.asList( clazz.getMethods() ).forEach((method) -> {

            THandler annotation = method.getAnnotation(THandler.class);

            if ( annotation == null ) return;

            Type type = method.getGenericReturnType();

            if ( !"void".equals(type.toString()) ) {

                throw new RuntimeException(method.getName() + " 返回值类型必须为void类型");

            }

            Class<?>[] parameterTypes = method.getParameterTypes();

            if ( parameterTypes.length > 2 ) {

                throw new RuntimeException(method.getName() + " 参数个数必须 <= 2");

            }

            com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.event.MethodManager methodManager = new com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.event.MethodManager(listener, method, annotation);

            methodsList.add(methodManager);

            for ( TalexEvent continueEvent : continueEvents.values() ) {

                this._callEvent(continueEvent, methodManager);

            }

        });

        listenerManager.putAll(listener, methodsList);

        return this;

    }

    private void _callEvent(TalexEvent event, com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.event.MethodManager methodManager) {

        // 判断 event 参数是否和 continue event 相同
        if( !methodManager.getMethod().getParameterTypes()[0].isAssignableFrom(event.getClass()) ) return;

        log.debug("[Event] For _event: {} | {}", event, JSONUtil.toJsonStr(methodManager));

        // execute
        if ( methodManager.getTHandler().threadMode() == ThreadMode.ASYNC ) {

            ThreadUtil.execAsync(() -> event(event, methodManager));

        } else {

            event(event, methodManager);

        }

    }

    @Override
    public TalexEventBus callEvent(TalexEvent event) {

        // 如果是 ContinuouslyEvent 放到 map 中
        if ( event instanceof IContinue ) {

            String matchKey = ( (IContinue) event ).getMatchKey();
            continueEvents.put(matchKey, event);

            log.debug("[Event] For continuously event: {} | {}", event, JSONUtil.toJsonStr(continueEvents));

        }

        // 筛选出匹配的 methodManager
        Map<Integer, com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.event.MethodManager> preList = new HashMap<>();

        listenerManager.entries().forEach((entry) -> {

            if( entry.getValue().getMethod().getParameterTypes()[0].isAssignableFrom(event.getClass()) ) {

                preList.put(entry.getKey().hashCode(), entry.getValue());

            }

        });

        log.debug("[Event] For event preList: {}", preList);

        List<com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.event.MethodManager> pl = new ArrayList<>(preList.values());

        pl.sort((o1, o2) -> {

            THandler ts1 = o1.getTHandler();
            THandler ts2 = o2.getTHandler();

            return Integer.compare(ts1.priority().getLevel(), ts2.priority().getLevel());

        });

        for ( com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.event.MethodManager methodManager : pl ) {

            if ( methodManager.getTHandler().threadMode() == ThreadMode.ASYNC ) {

                log.debug("[Event] Execute async event: {}", methodManager.getMethod().getName());

                ThreadUtil.execAsync(() -> event(event, methodManager));

            } else {

                log.debug("[Event] Execute event: {}", methodManager.getMethod().getName());

                event(event, methodManager);

            }

        }

        return this;

    }

    private void event(TalexEvent event, com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.event.MethodManager methodManager) {

        Method method = methodManager.getMethod();

        log.debug("[Event] --> Invoke: " + event.getClass().getName() + " | " + method.getName() + " <> " + methodManager.getOwner().getClass().getName());

        try {

            List<Object> objs = new ArrayList<>();

            objs.add(event);

            for ( Parameter parameter : method.getParameters() ) {

                if ( parameter.getType().isInstance(MethodManager.class) ) {

                    objs.add(methodManager);

                }

            }

            method.invoke(methodManager.getOwner(), objs.toArray(new Object[0]));

            if ( event instanceof IContinue ) {

                String matchKey = ( (IContinue) event ).getMatchKey();
                continueEvents.put(matchKey, event);

                methodManager.listened(matchKey);

            }

            if ( methodManager.getTHandler().once() ) {

                com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.event.FrameListener listener = methodManager.getOwner();

                listenerManager.remove(listener, methodManager);

            }

        } catch ( Exception e ) {

            if ( e.getCause() == null ) {

                log.error("[事件] 在调用 " + method.getName() + " 时, 目标抛出异常" + "   @" + method.getDeclaringClass().getName() + "." + method.getName());

            } else {
                log.error("[事件] 在调用 " + method.getName() + " 时, 目标抛出异常: " + e.getCause().getMessage() + "   @" + method.getDeclaringClass().getName() + "." + method.getName());
            }

            e.printStackTrace();

        }

    }

    @Override
    public TalexEventBus unRegisterListener(FrameListener listener) {

        listenerManager.removeAll(listener);

        return this;

    }

}


