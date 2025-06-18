package com.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.event.events.request;

import com.talexframe.frame.core.modules.event.service.Cancellable;
import com.talexframe.frame.core.modules.event.service.TalexEvent;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;
import lombok.Getter;

/**
 * 当一个请求无法获得令牌而被取消时 <br /> {@link com.talexframe.frame.function.event.events.request Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/23 23:59 <br /> Project: TalexFrame <br />
 */
@Getter
public class RequestCannotGetTokenEvent extends TalexEvent implements Cancellable {

    private final WrappedResponse wr;
    private final LimiterType limiterType;
    private boolean cancel;

    public RequestCannotGetTokenEvent(WrappedResponse wr, LimiterType limiterType) {

        this.wr = wr;
        this.limiterType = limiterType;

    }

    @Override
    public boolean isCancelled() {

        return cancel;
    }

    @Override
    public void setCancelled(boolean var) {

        cancel = var;

    }

    public enum LimiterType {

        GLOBAL(), CLASS(), METHOD()

    }

}
