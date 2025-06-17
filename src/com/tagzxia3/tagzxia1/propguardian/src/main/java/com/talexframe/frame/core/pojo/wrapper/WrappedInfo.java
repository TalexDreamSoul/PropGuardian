package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.pojo.wrapper;

import com.talexframe.frame.core.modules.plugins.core.PluginInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.jar.JarFile;

/**
 * <br /> {@link com.talexframe.frame.wrapper Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/25 16:03 <br /> Project: TalexFrame <br />
 */
@Getter
@AllArgsConstructor
public class WrappedInfo {

    private PluginInfo info;
    private JarFile jarFile;

}
