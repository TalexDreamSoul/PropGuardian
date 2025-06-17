package com.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.modules.network.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <br /> {@link com.talexframe.frame.interceptor Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/16 10:58 <br /> Project: TalexFrame <br />
 */
@Slf4j
@Component
@Order( Integer.MAX_VALUE - 10000 )
public class TimeConsumingInterceptor implements HandlerInterceptor {

    private final NamedThreadLocal<Long> threadLocal = new NamedThreadLocal<>("StopWatch_StartTime");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        long startTime = System.nanoTime();

        threadLocal.set(startTime);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        long consumeTime = System.nanoTime() - threadLocal.get();

        if ( consumeTime >= 500000000 ) {

            log.error("[TimeConsuming] 处理耗时超时 / [{}] #{}nanosecond", request.getRequestURI(), consumeTime);

        } else if ( consumeTime >= 300000000 ) {

            log.warn("[TimeConsuming] 处理耗时长 / [{}] #{}nanosecond", request.getRequestURI(), consumeTime);

        } else if ( consumeTime >= 100000000 ) {

            log.info("[TimeConsuming] 处理耗时较长 [{}] #{}nanosecond", request.getRequestURI(), consumeTime);

        }

        request.setAttribute("TalexTimeConsuming", consumeTime);

        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

}
