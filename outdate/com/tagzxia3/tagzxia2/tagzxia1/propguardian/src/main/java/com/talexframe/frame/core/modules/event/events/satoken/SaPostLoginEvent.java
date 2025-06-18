package com.tagzxia3.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.event.events.satoken;

import cn.dev33.satoken.stp.SaLoginModel;
import com.talexframe.frame.core.modules.event.service.TalexEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 当用户登录时触发
 */
@EqualsAndHashCode( callSuper = true )
@Data
public class SaPostLoginEvent extends TalexEvent {

    private final String loginType;
    private final Object loginId;
    private final String tokenValue;
    private final SaLoginModel loginModel;

    public SaPostLoginEvent(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel) {

        this.loginType = loginType;
        this.loginId = loginId;
        this.tokenValue = tokenValue;
        this.loginModel = loginModel;

    }


}
