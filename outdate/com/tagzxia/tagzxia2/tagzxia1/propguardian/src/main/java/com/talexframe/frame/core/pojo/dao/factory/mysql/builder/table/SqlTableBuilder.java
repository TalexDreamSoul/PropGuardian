package com.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.factory.mysql.builder.table;

import cn.hutool.core.util.StrUtil;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.factory.mysql.builder.table.TableParam;
import com.talexframe.frame.core.pojo.dao.factory.mysql.builder.SqlBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors( chain = true )
@Deprecated
public class SqlTableBuilder extends SqlBuilder {

    private final List<String> tableKeys = new ArrayList<>();
    @Getter
    @Setter
    private List<TableParam> map = new ArrayList<>();
    /**
     * 若不设置则不会追加代码 追加代码结尾必须为 ,
     */
    @Getter
    @Setter
    private String addonSql;

    public SqlTableBuilder(String tableName) {

        super("SqlTableBuilder", tableName);

    }

    public void addTableParamIgnore(TableParam tp) {

        if ( tableKeys.contains(tp.getSubParamName()) ) {

            return;

        }

        this.map.add(tp);
        tableKeys.add(tp.getSubParamName());

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

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS `" + this.tableName + "` (");
        String key = "PRIMARY KEY (`%stp%`)";
        StringBuilder uniqueKey = new StringBuilder("UNIQUE KEY `only` (");

        for ( TableParam tp : map ) {

            StringBuilder tsb = new StringBuilder("`" + tp.getSubParamName() + "` " + tp.getType()).append(" ");

            if ( tp.isMain() ) {

                key = key.replace("%stp%", tp.getSubParamName());

            }

            if ( !StrUtil.isBlankIfStr(tp.getColumnContent()) ) {

                String content = tp.getColumnContent();

                if ( !content.endsWith(",") ) {

                    content = content + ",";

                }

                sb.append(content);

            } else {

                if ( tp.getDefaultNull() == null ) {

                    tsb.append("DEFAULT NULL,");

                } else if ( tp.getDefaultNull().equalsIgnoreCase("null") ) {

                    tsb.append("NOT NULL,");

                } else {

                    tsb.append("DEFAULT \"").append(tp.getDefaultNull()).append("\",");

                }

                if ( tp.isUniqueOnly() ) {

                    uniqueKey.append("`").append(tp.getSubParamName()).append("`").append(",");

                }

                sb.append(tsb);

            }

        }

        String unique = uniqueKey.toString();

        if ( unique.endsWith(",") ) {

            unique = unique.substring(0, unique.length() - 1) + "),";

            sb.append(unique);

        }

        if ( this.getAddonSql() != null ) {

            sb.append(this.getAddonSql());

        }

        return sb.append(key).append(")").toString();

    }

}
