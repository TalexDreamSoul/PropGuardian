package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.interfaces;

import cn.hutool.db.Db;
import com.talexframe.frame.core.pojo.dao.factory.mysql.BuilderMap;
import com.talexframe.frame.core.pojo.dao.factory.mysql.builder.SqlBuilder;
import com.talexframe.frame.core.pojo.dao.factory.mysql.builder.insert.SqlInsertBuilder;
import com.talexframe.frame.core.pojo.dao.factory.mysql.builder.liker.SqlDelBuilder;
import com.talexframe.frame.core.pojo.dao.factory.mysql.builder.liker.SqlLikeBuilder;
import com.talexframe.frame.core.pojo.dao.factory.mysql.builder.table.SqlTableBuilder;

import java.sql.ResultSet;

/**
 * 关系型数据库 <br /> {@link com.talexframe.frame.core.pojo.dao.interfaces Package }
 *
 * @author TalexDreamSoul
 * @date 22/03/05 下午 01:08 <br /> Project: TalexFrame <br />
 */
public interface IConnectorProcessor {

    /**
     * 获取 DbUtil 根据 DataSource
     * @return DbUtil
     */
    Db forDb();

    /**
     * 向数据库中插入数据
     *
     * @param sab 数据
     *
     * @return 影响行数
     */
    int insertData(SqlInsertBuilder sab);

    /**
     * 从数据库中删除数据
     *
     * @return 影响行数
     */
    int delData(SqlDelBuilder sdb);

    /**
     * 从数据库中查找相似数据
     *
     * @return 结果
     */
    ResultSet likeData(SqlLikeBuilder slb);

    /**
     * 自动执行命令 （继承于 SqlBuilder 即可）
     *
     * @return 影响行数
     */
    int autoAccess(SqlBuilder sb);

    /**
     * 创建表
     */
    void createTable(SqlTableBuilder stb);

    /**
     * 读取一个表中的所有数据
     */
    ResultSet readAllData(String table);

    /**
     * 读取一个表中的匹配数据
     *
     * @param limit 限制条数
     */
    ResultSet searchData(String table, String selectType, String value, int limit);

    /**
     * 读取一个表中的匹配数据
     */
    ResultSet searchData(String table, String selectType, String value);

    /**
     * 是否有匹配指定参数的数据
     */
    boolean hasData(String table, BuilderMap map);

    /**
     * 预备执行指令
     */
    int prepareStatement(String sql);

    /**
     * 执行指令
     *
     * @return 返回回调
     */
    ResultSet executeWithCallBack(String sql);

}
