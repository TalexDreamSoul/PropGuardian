package com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.min.value;

import com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.ReceiverValidateAddon;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;

/**
 * {@link com.talexframe.frame.core.modules.network.connection.app.addon.login Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 04:47 Project: TalexFrame
 */
@SuppressWarnings( "UnstableApiUsage" )
public class ReceiverMinValueValidateAddon extends ReceiverValidateAddon<TMinValueValid> {

    public ReceiverMinValueValidateAddon() {

        super("ReceiverMinValueValidate");

    }

    @Override
    public boolean validate(WrappedResponse wr, TMinValueValid tMinValueValid, Object addedParam) {

        if( !(addedParam instanceof Number) ) return false;

        return ((Number)addedParam).intValue() >= tMinValueValid.value();

    }

    @Override
    public String getErrorMessage(WrappedResponse wr, TMinValueValid tMinValueValid, Object addedParam) {

        return tMinValueValid.msg();
    }

}
