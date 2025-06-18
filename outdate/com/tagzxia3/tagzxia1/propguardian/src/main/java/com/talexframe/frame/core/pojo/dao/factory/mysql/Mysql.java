package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.factory.mysql;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Session;
import com.alibaba.druid.pool.DruidDataSource;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.factory.mysql.BuilderMap;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.factory.mysql.MysqlConfig;
import com.talexframe.frame.core.modules.event.events.dao.DAOProcessorConnectFailedEvent;
import com.talexframe.frame.core.modules.event.events.dao.DAOProcessorConnectedEvent;
import com.talexframe.frame.core.modules.event.events.dao.DAOProcessorPreShutdownEvent;
import com.talexframe.frame.core.pojo.dao.DataProcessorStatus;
import com.talexframe.frame.core.pojo.dao.factory.mysql.builder.SqlBuilder;
import com.talexframe.frame.core.pojo.dao.factory.mysql.builder.insert.SqlInsertBuilder;
import com.talexframe.frame.core.pojo.dao.factory.mysql.builder.liker.SqlDelBuilder;
import com.talexframe.frame.core.pojo.dao.factory.mysql.builder.liker.SqlLikeBuilder;
import com.talexframe.frame.core.pojo.dao.factory.mysql.builder.table.SqlTableBuilder;
import com.talexframe.frame.core.pojo.dao.interfaces.IConnectorProcessor;
import com.talexframe.frame.core.pojo.dao.interfaces.IDataProcessor;
import com.talexframe.frame.core.pojo.dao.interfaces.IProcessorConfig;
import com.talexframe.frame.core.talex.TFrame;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <br /> {@link com.talexframe.frame.core.pojo.dao.factory.mysql Package }
 *
 * @author TalexDreamSoul
 * @date 22/03/05 下午 01:06 <br /> Project: TalexFrame <br />
 */
@Slf4j
public class Mysql implements IDataProcessor, IConnectorProcessor {

    @Getter
    private final DruidDataSource dataSource = new DruidDataSource();

    @Getter
    private Session session;

    private DataProcessorStatus status = DataProcessorStatus.DISCONNECTED;

    // private final AtomicInteger counter = new AtomicInteger();

    @Override
    public int insertData(SqlInsertBuilder sab) {

        return prepareStatement(sab.toString());
    }

    @Override
    public int delData(SqlDelBuilder sdb) {

        return delData(sdb, 1);
    }

    public int delData(SqlDelBuilder sdb, int limit) {

        if ( limit > 0 ) {

            return prepareStatement(sdb.toString());

        } else {

            return prepareStatement(sdb.toStringWithLimit(limit));

        }

    }

    @Override
    public ResultSet likeData(SqlLikeBuilder slb) {

        log.debug("likeData: {}", slb.toString());

        return executeWithCallBack(slb.toString());
    }

    @Override
    public int autoAccess(SqlBuilder sb) {

        return prepareStatement(sb.toString());
    }

    @Override
    public void createTable(SqlTableBuilder stb) {

        prepareStatement(stb.toString());

    }

    public Db forDb() {

        return Db.use(dataSource);

    }

    @Override
    public ResultSet readAllData(String table) {

        return executeWithCallBack("SELECT * FROM `" + table + "`");

    }

    /**
     * 本着这里是提供容易方法的原则，用 and 而不是 like 因为有 SqlLikeBuilder 可以完成复杂的查询
     */
    @Override
    public ResultSet searchData(String table, String selectType, String value, int limit) {

        return executeWithCallBack("SELECT * FROM `" + table + "` WHERE `" + selectType + "` = '" + value + "' "
                + ( limit > 0 ? " LIMIT " + limit : "" ));

    }

    @Override
    public ResultSet searchData(String table, String selectType, String value) {

        return searchData(table, selectType, value, -1);

    }

    @SneakyThrows
    @Override
    public boolean hasData(String table, BuilderMap map) {

        ResultSet rs = executeWithCallBack(new SqlLikeBuilder(table).setMap(map).toString());

        return rs != null && rs.next();

    }

    @Override
    public int prepareStatement(String sql) {

        try {

            return this.session.execute(sql);

        } catch ( SQLException e ) {

            log.error("[数据库] [预备] 在执行指令的时候发生了错误: " + sql, e);

            return -1;

        }

    }

    @Override
    public ResultSet executeWithCallBack(String sql) {

        try {

            log.debug("[数据库] [执行] 执行指令: " + sql);

            return this.session.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery(sql);

        } catch ( SQLException e ) {

            log.error("[数据库] [预备] 在执行指令的时候发生了错误: " + sql);

            e.printStackTrace();

            return null;

        }

    }

    @Override
    public boolean connect() {

        IProcessorConfig config = MysqlConfig.getInstance();

        try {

            dataSource.setUrl("jdbc:mysql://" + config.getIpAddress() + ":" + config.getPort()
                    + "/" + config.getDatabaseName() + ( config.getExtra() != null ? "?" + config.getExtra() : "" ));
            dataSource.setUsername(config.getUsername());
            dataSource.setPassword(config.getPassword());

            // dataSource.setTimeBetweenEvictionRunsMillis(15000);
            // dataSource.setValidationQuery("SELECT 1");

            // dataSource.setTestWhileIdle(true);

            session = Session.create(dataSource);

            if ( !session.getConnection().isValid(1200) ) {

                throw new SQLException();

            }

            status = DataProcessorStatus.CONNECTED;

            task = CronUtil.schedule("0/10 * * * * ?", (Runnable) () -> {

                if( status != DataProcessorStatus.CONNECTED ) {

                     return;

                }

                if( !checker() ) return;

                if( !getStatus(3000) ) {

                    log.info("[数据库] [连接] 数据库连接已经断开，正在尝试重新连接...");

                    reConnect();

                } else {

                    try {

                        getSession().execute("SELECT 1");

                        log.info("[数据库] [连接] 测试数据库连接成功!");

                    } catch ( SQLException e ) {

                        throw new RuntimeException(e);

                    }

                }

            });

        } catch ( SQLException e ) {

            DAOProcessorConnectFailedEvent<Mysql> daoConnectFailedEvent = new DAOProcessorConnectFailedEvent<>(this, e);

            TFrame.tframe.callEvent(daoConnectFailedEvent);

            if ( !daoConnectFailedEvent.isCancelled() ) {

                log.error(dataSource.getUrl());

                e.printStackTrace();

            }

            status = DataProcessorStatus.FAILED_CONNECT;

            return false;

        }

        TFrame.tframe.callEvent(new DAOProcessorConnectedEvent<>(this));

        return true;
    }

    private String task;

    private boolean checker() {

        if( getStatus() != DataProcessorStatus.CONNECTED ) {

            log.warn("[数据库] [连接] 数据库连接已经断开，已移除检测线程 ...");

            CronUtil.remove(task);

            return false;

        }

        log.info("[数据库] [连接] 数据库连接正常, 正在测试通道是否正常 ...");

        return true;

    }

    @SneakyThrows
    @Override
    public boolean disconnect() {

        log.debug("[数据库] [断开] 断开数据库连接 ...");

        if ( this.session != null ) {

            DAOProcessorPreShutdownEvent<Mysql> daoProcessorPreShutdownEvent = new DAOProcessorPreShutdownEvent<>(this);

            TFrame.tframe.callEvent(daoProcessorPreShutdownEvent);

            if ( daoProcessorPreShutdownEvent.isCancelled() ) {

                return false;

            }

            status = DataProcessorStatus.DISCONNECTED;

            this.session.close();
            this.dataSource.close();

            log.warn("[数据库] MySQL数据库已停止服务!");

        }

        log.debug("[数据库] [断开] 已断开数据库连接 !");

        return false;
    }

    @Override
    public DataProcessorStatus getStatus() {

        return status;
    }

    @Override
    public boolean checkStatus() {

        return getStatus(3000);
    }

    @SneakyThrows
    public boolean getStatus(int timeout) {

        return this.session.getConnection().isValid(timeout);
    }

    public void reConnect(int delay) {

        this.disconnect();

        ThreadUtil.execAsync(() -> {

            ThreadUtil.sleep(delay);

            connect();

        });

    }

    @Override
    public void reConnect() {

        reConnect(100);

    }

    @Override
    public boolean doCrash() {

        return true;
    }

}
