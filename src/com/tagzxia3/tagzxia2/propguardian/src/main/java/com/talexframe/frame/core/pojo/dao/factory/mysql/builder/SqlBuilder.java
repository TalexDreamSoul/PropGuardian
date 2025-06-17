package com.tagzxia3.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.factory.mysql.builder;

import com.talexframe.frame.core.pojo.FrameBuilder;
import com.talexframe.frame.core.pojo.dao.factory.DAOManager;
import com.talexframe.frame.core.pojo.dao.factory.mysql.Mysql;
import lombok.Getter;

public abstract class SqlBuilder extends FrameBuilder {

    protected static Mysql mysql = new DAOManager.ProcessorGetter<Mysql>(Mysql.class).getProcessor();

    @Getter
    protected String tableName;

    public SqlBuilder(String provider, String tableName) {

        super(provider);

        this.tableName = tableName;

    }

    @Override
    public abstract String toString();

}
