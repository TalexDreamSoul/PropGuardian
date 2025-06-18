package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.pattern;

import cn.hutool.core.util.StrUtil;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.pattern.TPatternValid;
import com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.ReceiverValidateAddon;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;

/**
 * {@link com.talexframe.frame.core.modules.network.connection.app.addon.login Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 04:47 Project: TalexFrame
 */
@SuppressWarnings( "UnstableApiUsage" )
public class ReceiverPatternValidateAddon extends ReceiverValidateAddon<TPatternValid> {

    public ReceiverPatternValidateAddon() {

        super("ReceiverPatternValidate");

    }

    @Override
    public boolean validate(WrappedResponse wr, TPatternValid tPatternValid, Object addedParam) {

        return String.valueOf(addedParam).matches(tPatternValid.value());

    }

    @Override
    public String getErrorMessage(WrappedResponse wr, TPatternValid tPatternValid, Object addedParam) {

        return StrUtil.isBlankIfStr(tPatternValid.msg()) ? super.getErrorMessage(wr, tPatternValid, addedParam) : tPatternValid.msg();
    }

}
