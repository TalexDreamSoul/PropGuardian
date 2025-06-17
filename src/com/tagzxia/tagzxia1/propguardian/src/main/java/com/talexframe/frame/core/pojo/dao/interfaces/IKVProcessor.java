package com.tagzxia.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.interfaces;

import com.talexframe.frame.core.pojo.wrapper.WrappedData;

/**
 * Key-Value 数据库 <br /> {@link com.talexframe.frame.core.pojo.dao.interfaces Package }
 *
 * @author TalexDreamSoul
 * @date 22/03/05 下午 01:08 <br /> Project: TalexFrame <br />
 */
public interface IKVProcessor {

    /**
     * 加入数据点
     *
     * @param key   键
     * @param value 值
     *
     * @return 是否成功
     */
    boolean joinPoint(String key, String value);

    /**
     * 获得数据点
     *
     * @param key 键
     *
     * @return 数据点
     */
    Object getPoint(String key);

    /**
     * 获得数据点
     *
     * @param key 键
     * @param clz 数据点类型
     *
     * @return 数据点
     */
    WrappedData<?> getPointTyped(String key, Class<?> clz);

    /**
     * 获得数据点
     *
     * @param key          键
     * @param defaultValue 默认值
     *
     * @return 数据点
     */
    WrappedData<?> getPoint(String key, WrappedData<?> defaultValue);

    /**
     * 删除数据点
     *
     * @param key 键
     *
     * @return 是否成功
     */
    boolean deletePoint(String key);

    /**
     * 更新数据点
     *
     * @param key   键
     * @param value 值
     *
     * @return 是否成功
     */
    boolean updatePoint(String key, String value);

}
