package com.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.factory.mysql;

import com.talexframe.frame.core.pojo.dao.interfaces.IProcessorConfig;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * <br /> {@link com.talexframe.frame.config Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/21 15:48 <br /> Project: TalexFrame <br />
 */
@Configuration
public class MysqlConfig implements IProcessorConfig {

    @Getter
    private static MysqlConfig instance;
    @Value( "${mysql.ip}" )
    private String ip;
    @Value( "${mysql.port}" )
    private int port;
    @Value( "${mysql.database}" )
    private String database;
    @Value( "${mysql.username}" )
    private String username;
    @Value( "${mysql.password}" )
    private String password;
    @Value( "${mysql.useSSL}" )
    private boolean useSSL;

    public MysqlConfig() {

        instance = this;

    }

    @Override
    public String getIpAddress() {

        return ip;
    }

    @Override
    public int getPort() {

        return port;
    }

    @Override
    public String getDatabaseName() {

        return database;
    }

    @Override
    public String getUsername() {

        return username;
    }

    @Override
    public String getPassword() {

        return password;
    }

    @Override
    public String getExtra() {

        return "autoReconnect=true&serverTimezone=Asia/Shanghai&useSSL=" + useSSL;
    }

}
