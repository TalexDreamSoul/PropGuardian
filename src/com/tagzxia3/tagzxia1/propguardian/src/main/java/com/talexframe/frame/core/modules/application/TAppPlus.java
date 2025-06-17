package com.tagzxia3.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.application;

import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.application.TApp;
import com.talexframe.frame.core.modules.plugins.core.WebPlugin;
import com.talexframe.frame.core.modules.repository.TRepoPlus;
import com.talexframe.frame.core.modules.repository.TRepo;
import com.talexframe.frame.core.pojo.mapper.frame.FramePluginSender;
import com.talexframe.frame.core.talex.TFrame;
import lombok.Getter;

import java.lang.reflect.ParameterizedType;

/**
 * <br /> {@link com.talexframe.frame.core.modules.application Package }
 *
 * @author TalexDreamSoul
 * 2022/1/20 18:51 <br /> Project: TalexFrame <br />
 */
@Getter
public class TAppPlus<T extends TRepoPlus<?>> extends TApp {

    private final WebPlugin ownPlugin;
    /**
     * 如果使用的是 TAppPlus 那么应该调用这个 repo
     */
    protected T asRepository;
    protected FramePluginSender logger;

    private Class<T> templateData;

    /**
     * Provider 已采用全新架构方式 无需再输入本类名
     *
     * @param webPlugin 所属插件
     */
    public TAppPlus(String provider, WebPlugin webPlugin) {

        super(provider);

        this.ownPlugin = webPlugin;
        this.logger = webPlugin.getConsoleSender();

    }

    @SuppressWarnings( "unchecked" )
    @Override
    public TRepo getRepo() {

        this.templateData = (Class<T>) ( (ParameterizedType) this.getClass().getGenericSuperclass() ).getActualTypeArguments()[0];

        super.repo = asRepository = (T) TFrame.tframe.getRepoManager().getRepositories().get(templateData);

        return asRepository;
    }

}
