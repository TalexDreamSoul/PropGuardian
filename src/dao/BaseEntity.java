package dao;

import cn.hutool.db.Entity;
import core.PropCore;
import dao.entity.UserInfo;
import dao.upper.IDataStorage;
import db.MySql;
import lombok.SneakyThrows;
import utils.MentionUtil;

import javax.swing.*;
import java.util.List;

/**
 * 基础实体类，实现数据库存储功能
 * 提供通用的数据库操作方法实现
 * @author 
 * @version 1.0
 */
public abstract class BaseEntity implements IDataStorage {
    /**
     * 数据库名称
     */
    private String databaseName;

    /**
     * 构造函数
     *
     * @param databaseName 数据库名称
     */
    public BaseEntity(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * 获取数据库名称
     *
     * @return 数据库名称
     */
    @Override
    public String getDatabaseName() {
        return this.databaseName;
    }

    /**
     * 获取MySQL操作对象
     *
     * @return MySQL操作实例
     */
    public MySql getMySql() {
        return PropCore.INS.getMySql();
    }

    /**
     * 创建数据库实体对象
     *
     * @return 新的实体对象
     */
    public Entity getEntity() {
        return Entity.create(this.getDatabaseName());
    }

    /**
     * 插入或更新数据库记录
     *
     * @param entity 实体对象
     * @return 影响的行数
     */
    @SneakyThrows
    public int insertOrUpdate(Entity entity) {
        return this.getMySql().use().insertOrUpdate(entity);
    }

    /**
     * 加载所有记录
     *
     * @return 所有记录列表
     */
    @SneakyThrows
    @Override
    public List<Entity> loadAll() {
        return this.getMySql().use().findAll(getEntity());
    }

    /**
     * 加载所有记录
     *
     * @return 所有记录列表 自动映射为对应的类
     */
    @SneakyThrows
    public <T> List<T> loadAllByEntity(Class<T> beanClass) {
        return this.getMySql().use().findAll(getEntity(), beanClass);
    }

    /**
     * 根据键值删除记录
     *
     * @param key   键名
     * @param value 键值
     * @return 删除成功返回true
     */
    @SneakyThrows
    public boolean delete(String key, String value) {
        return this.getMySql().use().del(getDatabaseName(), key, value) > 0;
    }

    /**
     * 加载符合查询条件的所有记录
     *
     * @param entity 查询条件实体
     * @return 符合条件的记录列表
     */
    @SneakyThrows
    public List<Entity> loadAll(Entity entity) {
        return this.getMySql().use().findAll(entity);
    }

    /**
     * 加载符合查询条件的所有记录
     *
     * @param entity 查询条件实体
     * @return 符合条件的记录列表 自动映射为对应的类
     */
    @SneakyThrows
    public <T> List<T> loadAllByEntity(Entity entity, Class<T> beanClass) {
        return this.getMySql().use().findAll(entity, beanClass);
    }

    /**
     * 创建查询实体
     *
     * @param key    键名
     * @param value  键值
     * @param entity 基础实体
     * @return 查询实体
     */
    public Entity getQueryEntity(String key, String value, Entity entity) {
        return Entity.create(this.getDatabaseName()).set(key, value);
    }

    /**
     * 创建查询实体
     *
     * @param key   键名
     * @param value 键值
     * @return 查询实体
     */
    public Entity getQueryEntity(String key, String value) {
        return getQueryEntity(key, value, getEntity());
    }

    /**
     * 固定删除方法 自动处理键值删除
     */
    @SneakyThrows
    public void deleteFixed(String key, Object value, Runnable runnable, JFrame jFrame) {
        try {
            int val = Integer.parseInt(String.valueOf(value));
            if (val == -1) {
                throw new Exception("请选择要删除的行！");
            }
            boolean success = delete(key, String.valueOf(val));
            MentionUtil.mentionForDelete(success, jFrame, runnable);

        } catch (Exception e) {
            if ( e instanceof NumberFormatException ) {
                JOptionPane.showMessageDialog(jFrame, "输入格式错误！");
            } else {
                JOptionPane.showMessageDialog(jFrame, e.getMessage());
            }

            e.printStackTrace();
        }
    }

    /**
     * 固定删除方法 自动处理键值删除
     */
    public void deleteFixedEntity(Entity entity, Runnable runnable, JFrame jFrame) {
        try {
            boolean success = getMySql().use().del(entity) > 0;
            MentionUtil.mentionForDelete(success, jFrame, runnable);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(jFrame, e.getMessage());

            e.printStackTrace();
        }
    }

    /**
     * 固定新增方法 自动处理键值新增
     */
    @SneakyThrows
    public <T extends BaseEntity> void insertFixed(T value, Runnable runnable, JFrame jFrame) {
        MentionUtil.mentionForAdd(value.storage(), jFrame, runnable);
    }

    /**
     * 固定新增方法 自动处理键值新增
     */
    @SneakyThrows
    public <T extends BaseEntity> void insertFixedSelf(Runnable runnable, JFrame jFrame) {
        MentionUtil.mentionForAdd(this.storage(), jFrame, runnable);
    }

    /**
     * 固定修改方法 自动处理键值修改
     */
    @SneakyThrows
    public <T extends BaseEntity> void updateFixed(T value, Runnable runnable, JFrame jFrame) {
        MentionUtil.mentionForUpdate(value.storage(), jFrame, runnable);
    }

    /**
     * 固定修改方法 自动处理键值修改
     */
    @SneakyThrows
    public <T extends BaseEntity> void updateFixedSelf(Runnable runnable, JFrame jFrame) {
        MentionUtil.mentionForUpdate(this.storage(), jFrame, runnable);
    }
}