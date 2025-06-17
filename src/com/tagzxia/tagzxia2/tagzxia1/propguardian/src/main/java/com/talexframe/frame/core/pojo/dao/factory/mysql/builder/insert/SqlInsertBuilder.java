package com.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.factory.mysql.builder.insert;

import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.factory.mysql.builder.insert.InsertType;
import com.talexframe.frame.core.pojo.dao.factory.mysql.BuilderMap;
import com.talexframe.frame.core.pojo.dao.factory.mysql.builder.SqlBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

@Accessors( chain = true )
public class SqlInsertBuilder extends SqlBuilder {

    @Getter
    @Setter
    private BuilderMap<SqlInsertBuilder> builderMap = new BuilderMap<>(this);
    @Getter
    @Setter
    private InsertType insertType;

    public SqlInsertBuilder(String tableName, InsertType insertType) {

        super("SqlInsertBuilder", tableName);

        this.insertType = insertType;

    }

    public SqlInsertBuilder(String tableName) {

        this(tableName, InsertType.IGNORE);

    }

    @Override
    public String toString() {

        return this.insertType == InsertType.IGNORE ? this.toStringWithIgnore() : this.toStringWithReplace();

    }

    private String toStringWithIgnore() {

        StringBuilder sb = new StringBuilder("INSERT INTO `" + this.tableName + "` (");
        StringBuilder keys = new StringBuilder("VALUES (");
        int i = 0;

        Map<String, String> map = builderMap.toMap(true);

        for ( Map.Entry<String, String> entry : map.entrySet() ) {

            ++i;
            StringBuilder tsb = new StringBuilder("`" + entry.getKey() + "`");

            if ( map.size() <= i ) {

                keys.append("\"").append(entry.getValue()).append("\"");

            } else {

                keys.append("\"").append(entry.getValue()).append("\", ");
                tsb.append(", ");

            }

            sb.append(tsb);

        }

        return sb.append(") ").append(keys).append(")").toString();

    }

    private String toStringWithReplace() {

        StringBuilder sb = new StringBuilder("REPLACE INTO `" + this.tableName + "` VALUES(");
        int i = 0;

        Map<String, String> map = builderMap.toMap(true);

        for ( Map.Entry<String, String> entry : map.entrySet() ) {

            ++i;
            StringBuilder tsb = new StringBuilder("`" + entry.getKey() + "`");

            if ( map.size() > i ) {

                tsb.append(", ");

            }

            sb.append(tsb);

        }

        return sb.append(") ").toString();

    }

}
