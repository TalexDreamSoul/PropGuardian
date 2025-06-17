package com.tagzxia3.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.modules.event.events.satoken;

import com.talexframe.frame.core.modules.event.service.TalexEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 当 Session注销 时触发
 */
@EqualsAndHashCode( callSuper = true )
@Data
public class SaSessionLogoutEvent extends TalexEvent {

    private final String id;

    public SaSessionLogoutEvent(String id) {

        this.id = id;

    }


}
