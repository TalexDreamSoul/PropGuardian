package com.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.modules.event.events.satoken;

import com.talexframe.frame.core.modules.event.service.TalexEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 当 Session创建 时触发
 */
@EqualsAndHashCode( callSuper = true )
@Data
public class SaSessionCreateEvent extends TalexEvent {

    private final String id;

    public SaSessionCreateEvent(String id) {

        this.id = id;

    }


}
