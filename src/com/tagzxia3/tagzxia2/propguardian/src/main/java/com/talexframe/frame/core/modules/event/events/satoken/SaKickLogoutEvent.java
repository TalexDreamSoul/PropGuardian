package com.tagzxia3.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.modules.event.events.satoken;

import com.talexframe.frame.core.modules.event.service.TalexEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 当用户被踢出时触发
 */
@EqualsAndHashCode( callSuper = true )
@Data
public class SaKickLogoutEvent extends TalexEvent {

    private final String loginType;
    private final Object loginId;
    private final String tokenValue;

    public SaKickLogoutEvent(String loginType, Object loginId, String tokenValue) {

        this.loginType = loginType;
        this.loginId = loginId;
        this.tokenValue = tokenValue;
    }


}
