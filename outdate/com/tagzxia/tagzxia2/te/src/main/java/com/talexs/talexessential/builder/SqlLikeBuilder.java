package com.tagzxia2.te.src.main.java.com.talexs.talexessential.builder;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.builder.SqlBuilder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class SqlLikeBuilder extends SqlBuilder {

    @Getter
    @Setter
    private List<String> map = new ArrayList<>();

    private String like;
    @Getter
    private String tableName = "default_table_" + System.currentTimeMillis();

    public SqlLikeBuilder setLike(String like) {

        this.like = like;
        return this;

    }

    public SqlLikeBuilder addTableParam(String likeTableParam) {

        this.map.add(likeTableParam);
        return this;

    }

    public SqlLikeBuilder setTableName(String name) {

        this.tableName = name;
        return this;

    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("SELECT * FROM `" + this.tableName + "` WHERE  CONCAT(");
        int i = 0;

        for ( String key : getMap() ) {

            ++i;
            StringBuilder tsb = new StringBuilder("`" + key + "`");

            if ( map.size() > i ) {

                tsb.append(",");

            }

            sb.append(tsb);

        }

        return sb.append(") LIKE '%").append(like).append("%'").toString();

    }

}
