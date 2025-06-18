package com.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.factory.mysql.builder.liker;

import com.talexframe.frame.core.pojo.dao.factory.mysql.BuilderMap;
import com.talexframe.frame.core.pojo.dao.factory.mysql.builder.SqlBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors( chain = true )
public class SqlLikeBuilder extends SqlBuilder {

    @Getter
    @Setter
    private BuilderMap<SqlLikeBuilder> map = new BuilderMap<>(this);

    public SqlLikeBuilder(String tableName) {

        super("SqlLikeBuilder", tableName);

    }

    @Override
    public String toString() {

        return "SELECT * FROM `" + this.tableName + "` WHERE " + map.toSqlWhereColumn();

    }

}
