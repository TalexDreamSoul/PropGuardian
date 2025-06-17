package com.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.modules.event;

import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.event.FrameListener;
import com.talexframe.frame.core.modules.event.service.THandler;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TalexDreamSoul
 */
public class MethodManager {

    @Getter
    private final com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.event.FrameListener owner;
    @Getter
    private final Map<String, Long> listenedEvents = new HashMap<>();

    private Method method;
    @Setter
    @Getter
    private THandler tHandler;

    public MethodManager(FrameListener listener, Method method, THandler tHandler) {

        this.owner = listener;
        this.method = method;
        this.tHandler = tHandler;

    }

    public void listened(String key) {

        if ( listenedEvents.containsKey(key) ) {

            throw new RuntimeException("在监听时出现错误 - key的重复发放 " + key);

        }

        listenedEvents.put(key, System.currentTimeMillis());

    }

    public Method getMethod() {return method;}

    public void setMethod(Method method) {this.method = method;}

    @Override
    public int hashCode() {

        return owner.getClass().getName().hashCode() + method.getName().hashCode();

    }

    @Override
    public boolean equals(Object obj) {

        if ( obj instanceof MethodManager ) {

            MethodManager target = (MethodManager) obj;

            if ( target.getClass().getName().equals(owner.getClass().getName()) ) {

                return method.getName().equals(target.getMethod().getName());

            }

        }

        return false;

    }

}


