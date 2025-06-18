package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.data;

import cn.hutool.db.Db;
import cn.hutool.db.Session;
import cn.hutool.db.ds.simple.SimpleDataSource;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.data.SQLCommand;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.builder.SqlLikeBuilder;
import com.talexs.talexessential.builder.SqlTableBuilder;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
public class MySQLSource {

    private DataSource ds;

    private String url, name, pass;

    public MySQLSource(String url, String name, String pass) {
        this.url = url;
        this.name = name;
        this.pass = pass;

    }

    public void connect() {
        this.ds = new SimpleDataSource(url, name, pass);
    }

    public Session session() {
        return Session.create(this.ds);
    }

    public Db db() {
        return Db.use(TalexEssential.getInstance().getMySQLSource().getDs());
    }

    public MySQLSource table(SqlTableBuilder stb) {

        String cmd = stb.toString();

        try( PreparedStatement ps = this.ds.getConnection().prepareStatement(cmd) ) {

            ps.execute();

            TalexEssential.getInstance().log("[MySQL Source] [Table] &e" + stb.getTableName() + " &rcreated!");

        } catch ( SQLException throwables ) {

            TalexEssential.getInstance().log("[MySQL Source] [Table] &cE: " + throwables.getMessage() + " # " + cmd);
            throwables.printStackTrace();

        }

        return this;

    }

    public ResultSet r(String table, String selectType, String value) {

        String s = "NONE";

        try {

            PreparedStatement ps = this.ds.getConnection().prepareStatement(SQLCommand.SELECT_DATA.commandToString()
                    .replaceFirst("%select_type%", selectType)
                    .replaceFirst("%table_name%", table)
                    .replaceFirst("%username%", value), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ps.setFetchSize(Integer.MIN_VALUE);

            return ps.executeQuery();

        } catch ( SQLException e ) {

            TalexEssential.getInstance().log(e.getMessage() + " # Execute: " + s);
            return null;

        }

    }

    public ResultSet likeData(SqlLikeBuilder slb) {

        PreparedStatement ps;

        try {

            ps = this.ds.getConnection().prepareStatement(slb.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            return ps.executeQuery();

        } catch ( SQLException throwables ) {

            TalexEssential.getInstance().log("[MySQL Source] [LikeData] &cE: " + throwables.getMessage() + " # " + slb);
            return null;

        }

    }

    @SneakyThrows
    public PreparedStatement ps(String cmd) {
        return this.ds.getConnection().prepareStatement(cmd);
    }

}
