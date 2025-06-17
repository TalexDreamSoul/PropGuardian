package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.event.events.satoken;

import com.talexframe.frame.core.modules.event.service.TalexEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 当用户被禁止登录时触发
 */
@EqualsAndHashCode( callSuper = true )
@Data
public class SaUserDisabledEvent extends TalexEvent {

    private final String loginType;
    private final Object loginId;
    private final long disableTime;

    public SaUserDisabledEvent(String loginType, Object loginId, long disableTime) {

        this.loginType = loginType;
        this.loginId = loginId;
        this.disableTime = disableTime;

    }


}
