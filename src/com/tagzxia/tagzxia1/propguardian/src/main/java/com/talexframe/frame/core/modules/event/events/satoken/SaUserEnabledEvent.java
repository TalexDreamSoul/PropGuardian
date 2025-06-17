package com.tagzxia.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.event.events.satoken;

import com.talexframe.frame.core.modules.event.service.TalexEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 当用户被允许登录时触发
 */
@EqualsAndHashCode( callSuper = true )
@Data
public class SaUserEnabledEvent extends TalexEvent {

    private final String loginType;
    private final Object loginId;

    public SaUserEnabledEvent(String loginType, Object loginId) {

        this.loginType = loginType;
        this.loginId = loginId;

    }


}
