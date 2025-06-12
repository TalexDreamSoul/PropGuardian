package dao;

import cn.hutool.db.Entity;
import core.PropCore;
import dao.upper.IDataStorage;
import db.MySql;
import lombok.SneakyThrows;

import java.util.List;

public abstract class BaseEntity implements IDataStorage {

    private String databaseName;

    public BaseEntity(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public String getDatabaseName() {
        return this.databaseName;
    }

    public MySql getMySql() {
        return PropCore.INS.getMySql();
    }

    public Entity getEntity() {
        return Entity.create(this.getDatabaseName());
    }

    @SneakyThrows
    public int insertOrUpdate(Entity entity) {
        return this.getMySql().use().insertOrUpdate(entity);
    }

    @SneakyThrows
    @Override
    public List<Entity> loadAll() {
        return this.getMySql().use().findAll(getEntity());
    }

    @SneakyThrows
    public boolean delete(String key, String value) {
        return this.getMySql().use().del(getDatabaseName(), key, value) > 0;
    }

    @SneakyThrows
    public List<Entity> loadAll(Entity entity) {
        return this.getMySql().use().findAll(entity);
    }

    public Entity getQueryEntity(String key, String value, Entity entity) {
        return Entity.create(this.getDatabaseName()).set(key, value);
    }

    public Entity getQueryEntity(String key, String value) {
        return getQueryEntity(key, value, getEntity());
    }
}
