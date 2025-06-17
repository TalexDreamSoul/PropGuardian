package com.tagzxia3.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.param.valid;

import com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddon;
import com.talexframe.frame.core.modules.network.connection.app.subapp.MethodAppReceiver;
import com.talexframe.frame.core.pojo.wrapper.ResultData;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;

/**
 * {@link com.talexframe.frame.core.modules.network.connection.app.addon.login Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 04:47 Project: TalexFrame
 */
@SuppressWarnings( "UnstableApiUsage" )
@Slf4j
public abstract class ReceiverValidateAddon<T extends Annotation> extends ReceiverAddon {

    protected final Class<? extends Annotation> templateData;

    public ReceiverValidateAddon(String receiverName) {

        super(receiverName, new ReceiverAddonType[] { ReceiverAddonType.PARAM_APP, ReceiverAddonType.URL_PARAM_APP });

        this.templateData = (Class<T>) ( (ParameterizedType) this.getClass().getGenericSuperclass() ).getActualTypeArguments()[0];

    }

    @Override
    public boolean onPreAddParam(MethodAppReceiver methodAppReceiver, Parameter parameter, WrappedResponse wr, Object addedParam) {

        T t = (T) parameter.getAnnotation(this.templateData);

        if( t == null ) return true;

        return _validate( wr, t, addedParam, parameter.getName() );
    }
    @Override
    public boolean onPreAddUrlParam(MethodAppReceiver methodAppReceiver, Parameter parameter, WrappedResponse wr, String originUrl, String comingUrl, String identifier, String value) {

        T t = (T) parameter.getAnnotation(this.templateData);

        if( t == null ) return true;

        return _validate( wr, t, value, identifier );

    }

    private boolean _validate( WrappedResponse wr, T t, Object addedParam, String paramName ) {

        if( validate( wr, t, addedParam ) ) return true;
        else {

            log.info("[ParamValidator] For error | Param name: {} | ValidateType: {} | In param: {} | At class: {}",
                    paramName, t.getClass(), addedParam, this.getClass().getSimpleName());

            wr.returnDataByFailed( ResultData.ResultEnum.INFORMATION_ERROR, getErrorMessage(wr, t, addedParam) );

            return false;

        }

    }

    public String getErrorMessage(WrappedResponse wr, T t, Object addedParam) {

        return "参数不匹配!";

    }

    /** 返回 false 将禁止 **/
    public abstract boolean validate( WrappedResponse wr, T t, Object addedParam);

}
