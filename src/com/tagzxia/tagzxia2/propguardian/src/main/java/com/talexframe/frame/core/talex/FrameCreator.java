package com.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.talex;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 框架缔造者 - 意指任何会对框架进行操作的对象 <br /> {@link com.talexframe.frame.function.talex Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/18 21:44 <br /> Project: TalexFrame <br />
 */
@AllArgsConstructor
public class FrameCreator {

    @Getter
    private String type, provider;

}
