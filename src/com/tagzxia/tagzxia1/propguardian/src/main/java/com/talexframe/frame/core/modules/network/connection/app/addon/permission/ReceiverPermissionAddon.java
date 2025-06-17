package com.tagzxia.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.permission;

import cn.dev33.satoken.stp.StpUtil;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.permission.TReqPermissionChecker;
import com.talexframe.frame.core.modules.network.connection.app.ClassAppReceiver;
import com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddon;
import com.talexframe.frame.core.modules.network.connection.app.subapp.MethodAppReceiver;
import com.talexframe.frame.core.pojo.wrapper.ResultData;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;

/**
 * {@link com.talexframe.frame.core.modules.network.connection.app.addon.login Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 04:47 Project: TalexFrame
 */
public class ReceiverPermissionAddon extends ReceiverAddon {

    public ReceiverPermissionAddon() {

        super("ReceiverPermission", new ReceiverAddonType[] { ReceiverAddonType.CLASS_APP, ReceiverAddonType.METHOD_APP });

        super.priority = ReceiverAddonPriority.HIGH;

    }

    @Override
    public boolean onPreCheckAppReceiver(ClassAppReceiver classAppReceiver, WrappedResponse wr) {

        return got(classAppReceiver.getOwnClass().getAnnotation(com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.permission.TReqPermissionChecker.class), wr);

    }

    @Override
    public boolean onPreInvokeMethod(MethodAppReceiver methodAppReceiver, WrappedResponse wr) {

        return got(methodAppReceiver.getMethod().getAnnotation(com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.permission.TReqPermissionChecker.class), wr);

    }

    private boolean got(TReqPermissionChecker reqLoginChecker, WrappedResponse wr) {

        // 不存在直接跳过
        if( reqLoginChecker == null ) {

            return true;

        }

        // 存在则检查

        if( StpUtil.hasPermissionAnd( reqLoginChecker.value() ) ) {

            return true;

        }

        wr.returnDataByFailed( ResultData.ResultEnum.ACCESS_DENIED, "权限不足! ");

        return false;

    }

}
