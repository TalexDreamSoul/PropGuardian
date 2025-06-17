package com.tagzxia.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.factory.mysql.builder.insert;

import lombok.Getter;

/**
 * <br /> {@link com.talexframe.frame.core.pojo.dao.factory.mysql.builder.insert Package }
 *
 * @author TalexDreamSoul
 * @date 22/03/05 下午 03:42 <br /> Project: TalexFrame <br />
 */
public enum InsertType {

    /**
     * 忽略 ( 如果有数据则不添加 )
     **/
    IGNORE(0),

    /**
     * 替换 ( 如果有数据则替换 )
     **/
    REPLACE(1);

    @Getter
    private final int code;

    InsertType(int code) {

        this.code = code;

    }

}
