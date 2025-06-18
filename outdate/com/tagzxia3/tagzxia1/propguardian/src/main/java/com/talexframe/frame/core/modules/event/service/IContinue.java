package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.event.service;

/**
 * 持续性集成事件 # 即只要事件发布 不论多久监听器注册都会收到这个事件 <br /> {@link com.talexframe.frame.function.event Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/21 11:32 <br /> Project: TalexFrame <br />
 */
public interface IContinue {

    /**
     * 指定的 key # 注意，这是用来区分的，请确保事件 key 的唯一性。
     */
    String getMatchKey();

}
