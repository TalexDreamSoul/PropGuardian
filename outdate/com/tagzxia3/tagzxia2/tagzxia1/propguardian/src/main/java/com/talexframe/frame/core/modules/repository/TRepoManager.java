package com.tagzxia3.tagzxia2.tagzxia1.propguardian.src.main.java.com.talexframe.frame.core.modules.repository;

import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.repository.TRepo;
import com.tagzxia.propguardian.src.main.java.com.talexframe.frame.core.modules.repository.TRepoPlus;
import com.talexframe.frame.core.modules.network.interfaces.IUnRegisterHandler;
import com.talexframe.frame.core.modules.plugins.addon.PluginScanner;
import com.talexframe.frame.core.modules.plugins.core.WebPlugin;
import com.talexframe.frame.core.pojo.annotations.TRepoInject;
import com.talexframe.frame.core.talex.TFrame;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <br /> {@link com.talexframe.frame.core.modules.repository Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 17:01 <br /> Project: TalexFrame <br />
 */
@Getter
@Slf4j
public class TRepoManager {

    private static TRepoManager manager;
    private final ConcurrentMap<Class<? extends TRepo>, TRepo> repositories = new ConcurrentHashMap<>();
    /**
     * TableName -> RepoClass 映射
     */
    private final ConcurrentMap<String, Class<? extends TRepo>> tableNameClzMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<TRepo, String> RepoPluginMap = new ConcurrentHashMap<>();

    private TRepoManager() {


    }

    public static TRepoManager init() {

        if ( manager == null ) {
            manager = new TRepoManager();
        }

        return manager;

    }

    public TRepo getRepoByTableName(String tableName) {

        return getRepoByClass(tableNameClzMap.get(tableName));

    }

    public TRepo getRepoByClass(Class<?> clz) {

        return repositories.get(clz);

    }

    public TRepoPlus<?> getASRepoByTableName(String tableName) {

        return (TRepoPlus<?>) getRepoByClass(tableNameClzMap.get(tableName));

    }

    public TRepoPlus<?> getASRepoByClass(Class<?> clz) {

        return (TRepoPlus<?>) repositories.get(clz);

    }

    /**
     * 注册一个储存库
     *
     * @param plugin     储存库插件
     * @param Repo 储存库
     *
     * @return 注册是否成功
     */
    @SneakyThrows
    public boolean registerRepo(WebPlugin plugin, TRepo Repo) {

        if ( this.repositories.containsKey(Repo.getClass()) ) {

            return false;

        }

        this.tableNameClzMap.put(Repo.getProvider(), Repo.getClass());
        this.repositories.put(Repo.getClass(), Repo);
        this.RepoPluginMap.put(Repo, plugin.getName());

        PluginScanner scanner = TFrame.tframe.getPluginManager().getPluginScannerMap().get( plugin.getName() );

        scanner.pushService(() -> {

            Class<?> clz = Repo.getClass();

            /* 扫描类中所有字段 带有 TRepInject 的字段，自动从 TRepoManager 中根据字段类型注入 **/
            for ( Field field : clz.getDeclaredFields() ) {

                TRepoInject repoInject = field.getAnnotation(TRepoInject.class);

                if ( repoInject != null ) {

                    Class<?> repClz = field.getType();

                    field.setAccessible(true);

                    TRepo tRep = getASRepoByClass(repClz);

                    if ( tRep == null ) {

                        throw new NullPointerException("Inject Repo with null - " + repClz.getName() + " - " + clz.getName());

                    }

                    try {

                        field.set(Repo, tRep);
                        log.debug("Inject Repo - " + repClz.getName() + " - " + clz.getName());

                    } catch ( IllegalAccessException e ) {
                        e.printStackTrace();
                    }

                }

            }

            scanner.pushService(() -> {

                if ( Repo instanceof TRepoPlus) {

                    log.debug("Init table Repo - " + Repo.getClass().getName());

                    TRepoPlus<?> plusRepo = ( (TRepoPlus<?>) Repo );
                    plusRepo.initTable();

                    scanner.pushService(() -> {

                        log.debug("Init Repo - " + Repo.getClass().getName());

                        plusRepo.onInstall();

                        scanner.pushService(plusRepo::onAllRepoDone);

                    });

                }

            });

        });

        return true;

    }

    /**
     * 注销一个储存库
     *
     * @param plugin     储存库插件
     * @param Repo 储存库
     *
     * @return 注销是否成功
     */
    public boolean unRegisterRepo(WebPlugin plugin, TRepo Repo) {

        if ( !this.repositories.containsKey(Repo.getClass()) ) {

            return false;

        }

        if ( Repo instanceof TRepoPlus) {

            ( (TRepoPlus<?>) Repo ).saveAllDataToMysql();

        }

        if ( Repo instanceof IUnRegisterHandler ) {

            ( (IUnRegisterHandler) Repo ).onUnRegister();

        }

        this.tableNameClzMap.remove(Repo.getProvider(), Repo.getClass());
        this.repositories.remove(Repo.getClass(), Repo);
        this.RepoPluginMap.remove(Repo, plugin.getName());

        return true;

    }

}
