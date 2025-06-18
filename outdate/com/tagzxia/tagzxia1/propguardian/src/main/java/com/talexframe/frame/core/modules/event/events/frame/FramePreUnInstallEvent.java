package com.tagzxia.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.event.events.frame;

import com.talexframe.frame.core.modules.event.service.TalexEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode( callSuper = true )
@Data
public class FramePreUnInstallEvent extends TalexEvent {

    @Getter
    private long installTimeStamp;

    public FramePreUnInstallEvent(long installTimeStamp) {

        this.installTimeStamp = installTimeStamp;

    }

}
