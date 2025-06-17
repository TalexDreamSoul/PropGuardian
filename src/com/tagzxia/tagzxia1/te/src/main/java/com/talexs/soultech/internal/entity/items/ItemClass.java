package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.soultech.internal.entity.items;

import com.talexs.soultech.internal.entity.classfies.BaseClassifiesCreator;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ItemClass {

    Class<? extends BaseClassifiesCreator> value();

}
