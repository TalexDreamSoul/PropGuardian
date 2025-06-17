package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.max.length;

import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.max.length.TMaxLengthValid;
import com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.ReceiverValidateAddon;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;

/**
 * {@link com.talexframe.frame.core.modules.network.connection.app.addon.login Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 04:47 Project: TalexFrame
 */
@SuppressWarnings( "UnstableApiUsage" )
public class ReceiverMaxLengthValidateAddon extends ReceiverValidateAddon<TMaxLengthValid> {

    public ReceiverMaxLengthValidateAddon() {

        super("ReceiverMaxLengthValidate");

    }

    @Override
    public boolean validate(WrappedResponse wr, TMaxLengthValid tMaxLengthValid, Object addedParam) {

        return addedParam == null || ((String) addedParam).length() <= tMaxLengthValid.value();

    }

    @Override
    public String getErrorMessage(WrappedResponse wr, TMaxLengthValid tMaxLengthValid, Object addedParam) {

        return tMaxLengthValid.msg();
    }

}
