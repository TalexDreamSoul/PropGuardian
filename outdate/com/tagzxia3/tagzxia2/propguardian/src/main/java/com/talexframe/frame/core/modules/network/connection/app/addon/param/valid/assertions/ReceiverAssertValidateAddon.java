package com.tagzxia3.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.assertions;

import com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.ReceiverValidateAddon;
import com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.TAssertValid;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;
import org.jetbrains.annotations.NotNull;

/**
 * {@link com.talexframe.frame.core.modules.network.connection.app.addon.login Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 04:47 Project: TalexFrame
 */
@SuppressWarnings( "UnstableApiUsage" )
public class ReceiverAssertValidateAddon extends ReceiverValidateAddon<TAssertValid> {

    public ReceiverAssertValidateAddon() {

        super("ReceiverAssertValidate");

    }

    @Override
    public boolean validate(WrappedResponse wr, TAssertValid tNoNullValid, Object addedParam) {

        if( !(addedParam instanceof Boolean)) return false;

        return (Boolean) addedParam == tNoNullValid.value();

    }

    @Override
    public String getErrorMessage(WrappedResponse wr, @NotNull TAssertValid tNoNullValid, Object addedParam) {

        return tNoNullValid.msg();

    }

}
