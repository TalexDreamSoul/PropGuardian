package com.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.login;

import cn.dev33.satoken.stp.StpUtil;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.login.TReqLoginChecker;
import com.talexframe.frame.core.modules.network.connection.app.ClassAppReceiver;
import com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddon;
import com.talexframe.frame.core.modules.network.connection.app.subapp.MethodAppReceiver;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;

/**
 * {@link com.talexframe.frame.core.modules.network.connection.app.addon.login Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 04:47 Project: TalexFrame
 */
public class ReceiverLoginAddon extends ReceiverAddon {

    public ReceiverLoginAddon() {

        super("ReceiverLogin", new ReceiverAddonType[] { ReceiverAddonType.CLASS_APP, ReceiverAddonType.METHOD_APP });

        super.priority = ReceiverAddonPriority.HIGHEST;

    }

    @Override
    public boolean onPreCheckAppReceiver(ClassAppReceiver classAppReceiver, WrappedResponse wr) {

        return got(classAppReceiver.getOwnClass().getAnnotation(TReqLoginChecker.class), wr);

    }

    @Override
    public boolean onPreInvokeMethod(MethodAppReceiver methodAppReceiver, WrappedResponse wr) {

        return got(methodAppReceiver.getMethod().getAnnotation(TReqLoginChecker.class), wr);

    }

    private boolean got(TReqLoginChecker reqLoginChecker, WrappedResponse wr) {

        // 不存在直接跳过
        if( reqLoginChecker == null ) {

            return true;

        }

        // 存在则检查

        // 需要登录
        if( reqLoginChecker.value() ) {

            if( StpUtil.isLogin() ) return true;
            else wr.returnDataByFailed("您还未登录!");

        } else {

            if( StpUtil.isLogin() ) wr.returnDataByFailed("只有未登录才能访问!");
            else return true;

        }

        return false;

    }

}
