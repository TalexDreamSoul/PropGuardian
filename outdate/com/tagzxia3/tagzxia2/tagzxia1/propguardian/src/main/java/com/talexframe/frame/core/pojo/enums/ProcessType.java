package com.tagzxia3.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.pojo.enums;

public enum ProcessType {

    /**
     * 使用 TalexsFrame 框架网络层进行处理
     */
    FRAME,

    /**
     * 使用 TalexsFrame 框架基础层进行处理，即采用 SpringBootMapping
     */
    BASE,

    /**
     * 与Base相同，但在处理前会检测 Authorization 此项是否合格
     */
    BASE_LOGIN,

    /**
     * 与Frame相同，但在处理前会检测 Authorization 此项是否合格
     */
    FRAME_LOGIN,

    /**
     * 使用 TalexsFrame 框架接口层进行处理，成功后进入 框架基础层处理
     */
    API_BASE,

    /**
     * 使用 TalexsFrame 框架接口层进行处理, 成功后进入 框架网络层处理
     */
    API_LOGIN

}
