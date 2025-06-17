package com.tagzxia.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.factory.mysql.builder.liker;

import com.talexframe.frame.core.pojo.dao.factory.mysql.BuilderMap;
import com.talexframe.frame.core.pojo.dao.factory.mysql.builder.SqlBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors( chain = true )
public class SqlDelBuilder extends SqlBuilder {

    @Getter
    @Setter
    private BuilderMap<SqlDelBuilder> map = new BuilderMap<>(this);

    public SqlDelBuilder(String tableName) {

        super("SqlDelBuilder", tableName);

    }

    @Override
    public String toString() {

        return "DELETE * FROM `" + this.tableName + "` WHERE " + map.toSqlWhereColumn();

    }

    public String toStringWithLimit(int limit) {

        return toString() + " LIMIT " + limit;

    }

}
