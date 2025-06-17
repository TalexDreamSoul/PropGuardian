package com.tagzxia3.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.limit;

import com.google.common.util.concurrent.RateLimiter;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.limit.TReqLimit;
import com.talexframe.frame.core.modules.network.connection.app.ClassAppReceiver;
import com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddon;
import com.talexframe.frame.core.modules.network.connection.app.subapp.MethodAppReceiver;
import com.talexframe.frame.core.pojo.wrapper.ResultData;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * {@link com.talexframe.frame.core.modules.network.connection.app.addon.login Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 04:47 Project: TalexFrame
 */
@SuppressWarnings( "UnstableApiUsage" )
public class ReceiverLimitAddon extends ReceiverAddon {

    private final Map<Class<?>, RateLimiter> classRateLimiterMap = new HashMap<>();
    private final Map<String, RateLimiter> methodRateLimiterMap = new HashMap<>();

    public ReceiverLimitAddon() {

        super("ReceiverLimit", new ReceiverAddonType[] { ReceiverAddonType.CLASS_APP, ReceiverAddonType.METHOD_APP });

        super.priority = ReceiverAddonPriority.MOST_HIGHEST;

    }

    @Override
    public boolean onPreCheckAppReceiver(ClassAppReceiver classAppReceiver, WrappedResponse wr) {

        TReqLimit reqLimit = classAppReceiver.getOwnClass().getAnnotation(TReqLimit.class);

        if( reqLimit == null ) return true;

        RateLimiter rateLimiter = Optional.ofNullable(classRateLimiterMap.get(classAppReceiver.getOwnClass())).orElseGet(() -> {

            RateLimiter rl = RateLimiter.create(reqLimit.QPS(), reqLimit.timeout(), reqLimit.timeUnit());

            classRateLimiterMap.put(classAppReceiver.getOwnClass(), rl);

            return rl;

        });

        return got(rateLimiter, wr);

    }

    @Override
    public boolean onPreInvokeMethod(MethodAppReceiver methodAppReceiver, WrappedResponse wr) {

        TReqLimit reqLimit = methodAppReceiver.getMethod().getAnnotation(TReqLimit.class);

        if( reqLimit == null ) return true;

        RateLimiter rateLimiter = Optional.ofNullable(methodRateLimiterMap.get(methodAppReceiver.getMethod().getName())).orElseGet(() -> {

            RateLimiter rl = RateLimiter.create(reqLimit.QPS(), reqLimit.timeout(), reqLimit.timeUnit());

            methodRateLimiterMap.put(methodAppReceiver.getMethod().getName(), rl);

            return rl;

        });

        return got(rateLimiter, wr);

    }

    private boolean got(RateLimiter rateLimiter, WrappedResponse wr) {

        if( !rateLimiter.tryAcquire() ) {

            wr.returnDataByFailed(ResultData.ResultEnum.SERVICE_LIMITED, "服务器繁忙!");

            return false;

        }

        rateLimiter.acquire();

        return true;

    }

}
