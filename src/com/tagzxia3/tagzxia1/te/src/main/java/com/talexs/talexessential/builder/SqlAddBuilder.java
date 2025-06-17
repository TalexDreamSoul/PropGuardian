package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.talexessential.builder;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.builder.SqlBuilder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class SqlAddBuilder extends SqlBuilder {

    @Getter
    @Setter
    private List<AddParam> map = new ArrayList<>();
    @Getter
    private String tableName = "default_table_" + System.currentTimeMillis();
    @Getter
    private AddType addType = AddType.IGNORE;

    public SqlAddBuilder addTableParam(AddParam tp) {

        this.map.add(tp);
        return this;

    }

    public SqlAddBuilder setTableName(String name) {

        this.tableName = name;
        return this;

    }

    public SqlAddBuilder setType(AddType type) {

        this.addType = type;
        return this;

    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder(( addType == AddType.IGNORE ? "INSERT " + addType.getDisplayName() : "REPLACE" ) + " INTO `" + this.tableName + "` (");
        StringBuilder key = new StringBuilder("VALUES (");
        int i = 0;

        for ( AddParam tp : map ) {

            ++i;
            StringBuilder tsb = new StringBuilder("`" + tp.getSubParamName() + "`");

            if ( map.size() <= i ) {

                key.append("\"").append(tp.getSubParamValue()).append("\"");

            } else {

                key.append("\"").append(tp.getSubParamValue()).append("\", ");
                tsb.append(", ");

            }

            sb.append(tsb);

        }

        return sb.append(") ").append(key).append(")").toString();

    }

    public enum AddType {

        IGNORE("IGNORE"),
        REPLACE("REPLACE");

        @Getter
        String displayName;

        AddType(String displayName) {

            this.displayName = displayName;

        }

    }

    public static class AddParam {

        @Getter
        private String subParamName;
        @Getter
        private String subParamValue;

        public AddParam setSubParamName(String name) {

            this.subParamName = name;
            return this;

        }

        public AddParam setSubParamValue(String value) {

            this.subParamValue = value;
            return this;

        }

    }

}
