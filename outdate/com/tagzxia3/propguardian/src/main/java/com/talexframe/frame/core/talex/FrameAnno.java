package com.tagzxia3.propguardian.src.main.java.com.talexframe.frame.core.talex;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link com.talexframe.frame.core.talex Package }
 *
 * @author TalexDreamSoul 22/05/02 下午 01:12 Project: TalexFrame
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.TYPE_PARAMETER, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.PACKAGE, ElementType.PARAMETER, ElementType.TYPE_USE } )
public @interface FrameAnno {
}
