package com.tagzxia.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.interfaces;

/**
 * 数据库的配置信息 <br /> {@link com.talexframe.frame.core.pojo.dao.interfaces Package }
 *
 * @author TalexDreamSoul
 * @date 22/03/05 下午 01:27 <br /> Project: TalexFrame <br />
 */
public interface IProcessorConfig {

    /**
     * 获取数据库的IP地址
     */
    String getIpAddress();

    /**
     * 获取数据库的端口
     */
    int getPort();

    /**
     * 获取数据库的数据库名
     */
    String getDatabaseName();

    /**
     * 获取数据库的用户名
     */
    String getUsername();

    /**
     * 获取数据库的密码
     */
    String getPassword();

    /**
     * 获取数据库的 url 额外信息
     */
    String getExtra();

}
