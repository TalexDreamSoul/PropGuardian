package com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.event.events.frame;

import com.talexframe.frame.core.modules.event.service.Cancellable;
import com.talexframe.frame.core.modules.event.service.TalexEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 这里指框架已完全启动
 */
@EqualsAndHashCode( callSuper = true )
@Data
public class FrameStartedEvent extends TalexEvent implements Cancellable {

    /**
     * 框架启动时间 (nanoTime)
     */
    @Getter
    private long installedTimeStamp;
    private boolean cancelled = false;


    public FrameStartedEvent(long installedTimeStamp) {

        this.installedTimeStamp = installedTimeStamp;

    }

    @Override
    public boolean isCancelled() {

        return this.cancelled;

    }

    @Override
    public void setCancelled(boolean var) {

        this.cancelled = var;

    }

}
