package dao.upper;

import cn.hutool.db.Entity;
import db.MySql;

import java.util.List;

public interface IDataStorage {

    String getDatabaseName();

    boolean storage();

    List<Entity> loadAll();

}
