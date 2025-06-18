package com.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.event.events.request;

import com.talexframe.frame.core.modules.event.service.TalexEvent;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 在建立请求之前 (interceptor pre handle request) <br /> {@link com.talexframe.frame.function.event.events.request
 * Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 19:19 <br /> Project: TalexFrame <br />
 */
public class RequestAfterCompletion extends TalexEvent {

    @Getter
    private final HttpServletRequest request;

    @Getter
    private final HttpServletResponse response;

    @Getter
    private final Object handler;

    @Getter
    private final Exception ex;

    public RequestAfterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        this.request = request;
        this.response = response;
        this.handler = handler;
        this.ex = ex;

    }

}
