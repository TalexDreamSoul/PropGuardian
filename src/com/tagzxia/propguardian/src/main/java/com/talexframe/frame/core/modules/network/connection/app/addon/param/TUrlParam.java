package com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.param;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 注入URL字段 <br /> {@link com.talexframe.frame.pojo.annotations Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/22 17:14 <br /> Project: TalexFrame <br />
 */
@Retention( RetentionPolicy.RUNTIME )
public @interface TUrlParam {

    /**
     * 当前从url中取得的参数名 比如{hello}就写hello
     */
    String value();

}
