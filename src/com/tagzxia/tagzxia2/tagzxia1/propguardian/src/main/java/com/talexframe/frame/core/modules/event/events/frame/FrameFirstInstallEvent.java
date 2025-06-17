package com.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.event.events.frame;

import com.talexframe.frame.core.modules.event.service.Cancellable;
import com.talexframe.frame.core.modules.event.service.IContinue;
import com.talexframe.frame.core.modules.event.service.TalexEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode( callSuper = true )
@Data
public class FrameFirstInstallEvent extends TalexEvent implements Cancellable, IContinue {

    @Getter
    private long installedTimeStamp;
    /**
     * 在本事件中如果设定为真则不会输出默认的 config.yml 文件
     */
    private boolean cancelled = false;

    public FrameFirstInstallEvent(long installedTimeStamp) {

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

    @Override
    public String getMatchKey() {

        return System.nanoTime() + " # FrameFirstInstallEvent";
    }

}
