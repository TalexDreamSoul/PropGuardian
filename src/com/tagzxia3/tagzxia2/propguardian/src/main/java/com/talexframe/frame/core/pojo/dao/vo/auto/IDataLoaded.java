package com.tagzxia3.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.pojo.dao.vo.auto;

/**
 * 实现本接口已实现当数据初始化完成时调用. <br /> {@link com.talexframe.frame.function.auto.data Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 16:16 <br /> Project: TalexFrame <br />
 */
public interface IDataLoaded {

    /**
     * 当数据建立并且初始化完成后 (不同于 new 是刚开始建立）
     */
    void onDataLoaded();

}
