package com.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.event.events.request;

import com.talexframe.frame.core.modules.event.service.Cancellable;
import com.talexframe.frame.core.modules.event.service.TalexEvent;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;
import lombok.Getter;

import javax.servlet.http.HttpServletResponse;

/**
 * 在建立请求之前 (interceptor pre handle request) <br /> {@link com.talexframe.frame.function.event.events.request
 * Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 19:19 <br /> Project: TalexFrame <br />
 */
public class PreHandleRequest extends TalexEvent implements Cancellable {

    @Getter
    private final WrappedResponse request;

    @Getter
    private final HttpServletResponse response;

    @Getter
    private final Object handler;
    private boolean cancel;

    public PreHandleRequest(WrappedResponse request, HttpServletResponse response, Object handler) {

        this.request = request;
        this.response = response;
        this.handler = handler;

    }

    @Override
    public boolean isCancelled() {

        return cancel;
    }

    @Override
    public void setCancelled(boolean var) {

        this.cancel = var;

    }

}
