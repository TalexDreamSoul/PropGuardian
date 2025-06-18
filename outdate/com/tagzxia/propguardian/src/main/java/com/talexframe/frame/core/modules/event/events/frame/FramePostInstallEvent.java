package com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.event.events.frame;

import com.talexframe.frame.core.modules.event.service.Cancellable;
import com.talexframe.frame.core.modules.event.service.TalexEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 这里指 SpringBoot 准备好 框架已启动
 */
@EqualsAndHashCode( callSuper = true )
@Data
public class FramePostInstallEvent extends TalexEvent implements Cancellable {

    /**
     * 框架启动时间 (nanoTime)
     */
    @Getter
    private long installedTimeStamp;
    private boolean cancelled = false;


    public FramePostInstallEvent(long installedTimeStamp) {

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
