package com.tagzxia3.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.factory.mysql.builder.table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <br /> {@link com.talexframe.frame.core.pojo.dao.factory.mysql.builder.table Package }
 *
 * @author TalexDreamSoul
 * @date 22/03/05 下午 03:53 <br /> Project: TalexFrame <br />
 */
@Getter
@Setter
@Accessors( chain = true )
@NoArgsConstructor
@Deprecated
public class TableParam {

    private String subParamName;
    private String type = "TEXT(1024)";
    private String defaultNull;
    private boolean main = false;
    private boolean uniqueOnly = false;
    private String columnContent = null;

    public TableParam(String subParamName) {

        this.subParamName = subParamName;

    }

    public TableParam(String subParamName, String type) {

        this.subParamName = subParamName;
        this.type = type;

    }

    public TableParam(String subParamName, String type, String defaultNull) {

        this.subParamName = subParamName;
        this.type = type;
        this.defaultNull = defaultNull;

    }

    public TableParam(String subParamName, String type, String defaultNull, boolean main) {

        this.subParamName = subParamName;
        this.type = type;
        this.defaultNull = defaultNull;
        this.main = main;

    }

    public TableParam(String subParamName, String type, String defaultNull, boolean main, boolean uniqueOnly) {

        this.subParamName = subParamName;
        this.type = type;
        this.defaultNull = defaultNull;
        this.main = main;
        this.uniqueOnly = uniqueOnly;

    }

    public TableParam(String subParamName, String type, String defaultNull, boolean main, boolean uniqueOnly, String columnContent) {

        this.subParamName = subParamName;
        this.type = type;
        this.defaultNull = defaultNull;
        this.main = main;
        this.uniqueOnly = uniqueOnly;
        this.columnContent = columnContent;

    }

    public String getColumnContent() {

        return columnContent == null ? subParamName : columnContent;

    }

}
