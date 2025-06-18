package com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.event.events.request;

import com.talexframe.frame.core.modules.event.service.Cancellable;
import com.talexframe.frame.core.modules.event.service.TalexEvent;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;
import lombok.Getter;

/**
 * 框架尝试同意一个 OPTIONS 请求时 <br /> {@link com.talexframe.frame.function.event.events.request Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/22 17:19 <br /> Project: TalexFrame <br />
 */
public class RequestCorsTryEvent extends TalexEvent implements Cancellable {

    @Getter
    private final WrappedResponse response;
    private boolean cancel;

    public RequestCorsTryEvent(WrappedResponse response) {

        this.response = response;

    }

    @Override
    public boolean isCancelled() {

        return cancel;
    }

    @Override
    public void setCancelled(boolean var) {

        cancel = var;

    }

}
