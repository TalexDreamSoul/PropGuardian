package com.tagzxia2.te.src.main.java.com.talexs.talexessential.builder;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.builder.SqlBuilder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class SqlUpdBuilder extends SqlBuilder {

    @Getter
    @Setter
    private List<UpdParam> map = new ArrayList<>();
    @Getter
    private String tableName = "default_table_" + System.currentTimeMillis();
    private UpdParam whereParam;

    public SqlUpdBuilder addTableParam(UpdParam tp) {

        this.map.add(tp);
        return this;

    }

    public SqlUpdBuilder setTableName(String name) {

        this.tableName = name;
        return this;

    }

    public SqlUpdBuilder setWhereParam(UpdParam addParam) {

        this.whereParam = addParam;
        return this;

    }

    @Override
    public String toString() {

//        UPDATE `friend_list` SET `list` = \"" + StringUtil.Base64_Encode(String.join(" ",list)) + "\" WHERE name = \"" + current + "\"

        StringBuilder sb = new StringBuilder("UPDATE `" + this.tableName + "` SET ");
        int i = 0;

        for ( UpdParam tp : map ) {

            ++i;
            StringBuilder tsb = new StringBuilder("`" + tp.getSubParamName() + "` = \"" + tp.getSubParamValue() + "\"");

            if ( map.size() > i ) {

                tsb.append(", ");

            }

            sb.append(tsb);

        }

        if ( whereParam == null ) {

            return sb.toString();

        }

        return sb.append(" WHERE ").append(whereParam.getSubParamName()).append(" = \"").append(whereParam.getSubParamValue()).append("\"").toString();

    }

    public static class UpdParam {

        @Getter
        private String subParamName;
        @Getter
        private String subParamValue;

        public UpdParam setSubParamName(String name) {

            this.subParamName = name;
            return this;

        }

        public UpdParam setSubParamValue(String value) {

            this.subParamValue = value;
            return this;

        }

    }

}
