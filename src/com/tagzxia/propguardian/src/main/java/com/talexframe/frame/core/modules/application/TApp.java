package com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.application;

import com.talexframe.frame.core.modules.repository.TRepo;
import com.talexframe.frame.core.modules.repository.TRepoManager;
import com.talexframe.frame.core.talex.FrameCreator;
import com.talexframe.frame.core.talex.TFrame;
import lombok.Getter;

/**
 * <br /> {@link com.talexframe.frame.core.modules.application Package }
 *
 * @author TalexDreamSoul
 * 2022/1/20 16:54 <br /> Project: TalexFrame <br />
 */
@Getter
public abstract class TApp extends FrameCreator {

    protected final TFrame tframe = TFrame.tframe;

    protected TAppManager controllerManager = tframe.getAppManager();
    protected TRepoManager repositoryManager = tframe.getRepoManager();
    protected TRepo repo;

    public TApp(String provider) {

        super("TAPP", provider);

        this.repo = this.getRepo();

    }

    public abstract TRepo getRepo();

    /**
     *
     * 获取这个类所有请求接口的起始路径
     * For example, if you set 'api/v1/' as the base path, then in order to enter, the url path must initiative added on each request path;
     *
     * @return basic path
     */
    public String getDefaultPath() {

        return "";

    }

    /**
     * Use getRepo().getProvider() instead, because of single responsibility
     */
    @Deprecated
    public String getTableName() {

        return this.repo.getProvider();

    }

}
