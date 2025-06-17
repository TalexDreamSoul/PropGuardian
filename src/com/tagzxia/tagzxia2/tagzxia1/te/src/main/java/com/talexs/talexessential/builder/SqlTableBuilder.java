package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.builder;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.builder.SqlBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class SqlTableBuilder extends SqlBuilder {

    @Getter
    @Setter
    private List<TableParam> map = new ArrayList<>();
    private List<String> tableKeys = new ArrayList<>();
    @Getter
    private String tableName = "default_table_" + System.currentTimeMillis();

    public SqlTableBuilder addTableParamIgnore(TableParam tp) {

        if ( tableKeys.contains(tp.getSubParamName()) ) {

            return this;

        }

        this.map.add(tp);
        tableKeys.add(tp.getSubParamName());

        return this;

    }

    @SneakyThrows
    public SqlTableBuilder addTableParam(TableParam tp) {

        if ( tableKeys.contains(tp.getSubParamName()) ) {

            throw new Exception("此 Key 早已存在: " + tp.getSubParamName());

        }

        this.map.add(tp);
        tableKeys.add(tp.getSubParamName());

        return this;

    }

    public SqlTableBuilder setTableName(String name) {

        this.tableName = name;
        return this;

    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS `" + this.tableName + "` (");
        String key = "PRIMARY KEY (`%stp%`)";

        for ( TableParam tp : map ) {

            if ( tp.line != null ) {
                sb.append(tp.line).append(",");
                continue;
            }

            StringBuilder tsb = new StringBuilder("`" + tp.getSubParamName() + "` " + tp.type).append(" ");

            if ( tp.isMain() ) {

                key = key.replace("%stp%", tp.subParamName);

            }

            if ( tp.defaultNull == null ) {

                tsb.append("DEFAULT NULL,");

            } else if ( tp.defaultNull.equalsIgnoreCase("null") ) {

                tsb.append("NOT NULL,");

            } else {

                tsb.append("DEFAULT ").append(tp.defaultNull).append(",");

            }

            sb.append(tsb);

        }

        return sb.append(key).append(")").toString();

    }

    public static class TableParam {

        @Getter
        private String line;

        @Getter
        private String subParamName;
        @Getter
        private String type = "TEXT(1024)";
        @Getter
        private String defaultNull;
        @Getter
        private boolean main = false;

        public TableParam setSubParamName(String name) {

            this.subParamName = name;
            return this;

        }

        public TableParam setType(String type) {

            this.type = type;
            return this;

        }

        public TableParam setLine(String line) {

            this.line = line;
            return this;

        }

        public TableParam setDefaultNull(String defaultNull) {

            this.defaultNull = defaultNull;
            return this;

        }

        public TableParam setMain(boolean main) {

            this.main = main;
            return this;

        }

    }

}
