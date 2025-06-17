package com.tagzxia3.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.factory.mysql.builder;

import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.factory.mysql.builder.BuilderParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * BuilderMap Plus # 提供详细的逻辑sql生成 <br /> {@link com.talexframe.frame.core.pojo.dao.factory.mysql.builder Package
 * }
 *
 * @author TalexDreamSoul
 * @date 22/03/05 下午 04:02 <br /> Project: TalexFrame <br />
 */
@Setter
@Getter
@Accessors( chain = true )
@NoArgsConstructor
public class BuilderWhereParam extends BuilderParam {

    public BuilderWhereParam(String key, Object value) {

        super(key, value);

    }

    /**
     * 决定在这个字段后是加入 or 或者是 and 为真则为 or 否则为 and
     */
    private boolean afterAnd = false;

    /**
     * 当为真时 使用类似 user like ? 的形式 当为假时 使用类似 user = ? 的形式
     */
    private boolean useLike = false;

}
