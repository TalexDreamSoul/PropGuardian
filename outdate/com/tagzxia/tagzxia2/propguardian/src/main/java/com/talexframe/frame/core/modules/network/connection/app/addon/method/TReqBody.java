package com.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.modules.network.connection.app.addon.method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 解析HttpBody为JSON <br /> {@link com.talexframe.frame.core.modules.network.connection.app.addon.param Package }
 * \ 按理来说这个类应该放到 param 下面，但是因为在 method addon 中检测，所以放在这里。 /
 * @author TalexDreamSoul
 * 2022/06/06 23:19:23 <br /> Project: TalexFrame <br />
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.METHOD } )
public @interface TReqBody {

}
