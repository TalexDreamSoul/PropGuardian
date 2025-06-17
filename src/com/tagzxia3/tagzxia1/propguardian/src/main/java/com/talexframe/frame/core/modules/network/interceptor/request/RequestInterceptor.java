package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.network.interceptor.request;

import com.talexframe.frame.core.modules.event.events.request.PostHandleRequestEvent;
import com.talexframe.frame.core.modules.event.events.request.PreHandleRequest;
import com.talexframe.frame.core.modules.event.events.request.RequestAfterCompletion;
import com.talexframe.frame.core.modules.network.connection.RequestConnector;
import com.talexframe.frame.core.pojo.wrapper.BodyCopyHttpServletRequestWrapper;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;
import com.talexframe.frame.core.talex.TFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求过滤器 <br /> {@link com.talexframe.frame.interceptor.request Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 16:47 <br /> Project: TalexFrame <br />
 */
@Component
// @Order
@Slf4j
public final class RequestInterceptor implements HandlerInterceptor {

    static TFrame tframe = TFrame.tframe;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        BodyCopyHttpServletRequestWrapper copiedRequest = new BodyCopyHttpServletRequestWrapper(request);

        WrappedResponse wr = new WrappedResponse(copiedRequest, response);

        PreHandleRequest event = new PreHandleRequest(wr, response, handler);

        tframe.callEvent(event);

        if ( event.isCancelled() ) {
            return false;
        }

        new RequestConnector(wr);

        return false;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

        BodyCopyHttpServletRequestWrapper copiedRequest = new BodyCopyHttpServletRequestWrapper(request);

        PostHandleRequestEvent event = new PostHandleRequestEvent(copiedRequest, response, handler, modelAndView);

        tframe.callEvent(event);

        // if( event.isCancelled() ) return;

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        tframe.callEvent(new RequestAfterCompletion(request, response, handler, ex));

    }

}
