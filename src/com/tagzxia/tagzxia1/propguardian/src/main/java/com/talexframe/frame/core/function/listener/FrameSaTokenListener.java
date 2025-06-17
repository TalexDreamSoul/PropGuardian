package com.tagzxia.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.function.listener;

import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.SaLoginModel;
import com.talexframe.frame.core.modules.event.events.satoken.*;
import com.talexframe.frame.core.talex.TFrame;
import org.springframework.stereotype.Component;

/**
 * {@link com.talexframe.frame.core.function.listener Package }
 *
 * @author TalexDreamSoul 22/06/05 下午 11:52 Project: TalexFrame
 */
@Component
public class FrameSaTokenListener implements SaTokenListener {

    @Override
    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel) {

        TFrame.tframe.callEvent(new SaPostLoginEvent(loginType, loginId, tokenValue, loginModel));

    }

    /** 每次注销时触发 */
    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {

        TFrame.tframe.callEvent(new SaPostLogoutEvent(loginType, loginId, tokenValue));

    }

    /** 每次被踢下线时触发 */
    @Override
    public void doKickout(String loginType, Object loginId, String tokenValue) {

        TFrame.tframe.callEvent(new SaKickLogoutEvent(loginType, loginId, tokenValue));

    }

    /** 每次被顶下线时触发 */
    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue) {

        TFrame.tframe.callEvent(new SaPostReplacedOutEvent(loginType, loginId, tokenValue));

    }

    /** 每次被封禁时触发 */
    @Override
    public void doDisable(String loginType, Object loginId, long disableTime) {

        TFrame.tframe.callEvent(new SaUserDisabledEvent(loginType, loginId, disableTime));

    }

    /** 每次被解封时触发 */
    @Override
    public void doUntieDisable(String loginType, Object loginId) {

        TFrame.tframe.callEvent(new SaUserEnabledEvent(loginType, loginId));

    }

    /** 每次创建Session时触发 */
    @Override
    public void doCreateSession(String id) {

        TFrame.tframe.callEvent(new SaSessionCreateEvent(id));

    }

    /** 每次注销Session时触发 */
    @Override
    public void doLogoutSession(String id) {

        TFrame.tframe.callEvent(new SaSessionLogoutEvent(id));

    }

}
