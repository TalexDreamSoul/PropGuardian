package com.tagzxia3.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.interfaces;

import com.talexframe.frame.core.pojo.dao.DataProcessorStatus;

/**
 * 接口：数据处理器 <br /> {@link com.talexframe.frame.core.pojo.dao.interfaces Package }
 *
 * @author TalexDreamSoul
 * @date 22/03/05 下午 01:03 <br /> Project: TalexFrame <br />
 */
public interface IDataProcessor {

    /**
     * 连接数据库
     */
    boolean connect();

    /**
     * 断开数据库连接
     */
    boolean disconnect();

    /**
     * 获得连接状态
     */
    DataProcessorStatus getStatus();

    /**
     * 刷新连接状态
     */
    boolean checkStatus();

    /**
     * 重新连接
     */
    void reConnect();

    /**
     * 如果连接失败是否停止框架运行
     **/
    boolean doCrash();

}
