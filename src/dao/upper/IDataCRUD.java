package dao.upper;

import cn.hutool.db.Entity;
import dao.BaseEntity;
import db.MySql;

import javax.swing.*;
import java.util.List;

public interface IDataCRUD {

    /**
     * 获取数据库名称
     *
     * @return 数据库名称
     */
    public String getDatabaseName();

    /**
     * 获取MySQL操作对象
     *
     * @return MySQL操作实例
     */
    MySql getMySql();

    /**
     * 创建数据库实体对象
     *
     * @return 新的实体对象
     */
    Entity getEntity();

    /**
     * 插入或更新数据库记录
     *
     * @param entity 实体对象
     * @return 影响的行数
     */
    int insertOrUpdate(Entity entity);

    /**
     * 加载所有记录
     *
     * @return 所有记录列表
     */
    List<Entity> loadAll();

    /**
     * 加载所有记录
     *
     * @return 所有记录列表 自动映射为对应的类
     */
    <T> List<T> loadAllByEntity(Class<T> beanClass);

    /**
     * 根据键值删除记录
     *
     * @param key   键名
     * @param value 键值
     * @return 删除成功返回true
     */
    boolean delete(String key, String value);

    /**
     * 加载符合查询条件的所有记录
     *
     * @param entity 查询条件实体
     * @return 符合条件的记录列表
     */
    List<Entity> loadAll(Entity entity);

    /**
     * 加载符合查询条件的所有记录
     *
     * @param entity 查询条件实体
     * @return 符合条件的记录列表 自动映射为对应的类
     */
    <T> List<T> loadAllByEntity(Entity entity, Class<T> beanClass);

    /**
     * 创建查询实体
     *
     * @param key    键名
     * @param value  键值
     * @param entity 基础实体
     * @return 查询实体
     */
    Entity getQueryEntity(String key, String value, Entity entity);

    /**
     * 创建查询实体
     *
     * @param key   键名
     * @param value 键值
     * @return 查询实体
     */
    Entity getQueryEntity(String key, String value);

    /**
     * 固定删除方法 自动处理键值删除
     */
    void deleteFixed(String key, Object value, Runnable runnable, JFrame jFrame);

    /**
     * 固定删除方法 自动处理键值删除
     */
    void deleteFixedEntity(Entity entity, Runnable runnable, JFrame jFrame);

    /**
     * 固定新增方法 自动处理键值新增
     */
    <T extends BaseEntity> void insertFixed(T value, Runnable runnable, JFrame jFrame);

    /**
     * 固定新增方法 自动处理键值新增
     */
    <T extends BaseEntity> void insertFixedSelf(Runnable runnable, JFrame jFrame);

    /**
     * 固定修改方法 自动处理键值修改
     */
    <T extends BaseEntity> void updateFixed(T value, Runnable runnable, JFrame jFrame);

    /**
     * 固定修改方法 自动处理键值修改
     */
    <T extends BaseEntity> void updateFixedSelf(Runnable runnable, JFrame jFrame);

}
