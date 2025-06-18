package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.method;

import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.method.TReqBody;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.method.TReqSupportMethod;
import com.talexframe.frame.core.modules.network.connection.app.ClassAppReceiver;
import com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddon;
import com.talexframe.frame.core.modules.network.connection.app.subapp.MethodAppReceiver;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * {@link com.talexframe.frame.core.modules.network.connection.app.addon.login Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 04:47 Project: TalexFrame
 */
@SuppressWarnings( "UnstableApiUsage" )
@Slf4j
public class ReceiverMethodAddon extends ReceiverAddon {

    public ReceiverMethodAddon() {

        super("ReceiverMethod", new ReceiverAddonType[] { ReceiverAddonType.CLASS_APP, ReceiverAddonType.METHOD_APP });

        super.priority = ReceiverAddonPriority.SUPER_HIGHEST;

    }

    @Override
    public boolean onPreCheckAppReceiver(ClassAppReceiver classAppReceiver, WrappedResponse wr) {

        TReqSupportMethod supportMethod = classAppReceiver.getOwnClass().getAnnotation(TReqSupportMethod.class);

        if( supportMethod == null ) return true;

        return got(supportMethod, wr);

    }

    @Override
    public boolean onPreInvokeMethod(MethodAppReceiver methodAppReceiver, WrappedResponse wr) {

        TReqBody tReqBody = methodAppReceiver.getMethod().getAnnotation(TReqBody.class);
        TReqSupportMethod supportMethod = methodAppReceiver.getMethod().getAnnotation(TReqSupportMethod.class);

        if( tReqBody != null ) {

            // 如果有注解，然后用 get 方式
            if( supportMethod != null && Arrays.stream(supportMethod.value()).anyMatch(method -> method == HttpMethod.GET) ) {

                throw new RuntimeException("[解析层] MethodNotSupport # GET: 当为 GET 时无法解析 Body Data ！");

            }

            log.debug("For method: {} | TReqBody: {}", methodAppReceiver.getMethod().getName(), tReqBody);
            log.debug("Annotations: {}", methodAppReceiver.getMethod().getAnnotations());

            methodAppReceiver.setParseData(true, "body not null");

        }

        return supportMethod == null || got(supportMethod, wr);

    }

    private boolean got(TReqSupportMethod supportMethod, WrappedResponse wr) {

        HttpServletRequest request = wr.getRequest();

        for( HttpMethod method : supportMethod.value() ) {

            if( method.name().equals(request.getMethod()) ) return true;

        }

        return false;

    }

}
