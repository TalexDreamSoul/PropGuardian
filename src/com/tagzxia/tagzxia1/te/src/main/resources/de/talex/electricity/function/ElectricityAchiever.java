package com.tagzxia.tagzxia1.te.src.main.resources.de.talex.electricity.function;

import lombok.Getter;
import com.talexs.talexessential.TalexEssential;
import pubsher.talexsoultech.talex.electricity.achieve.addon.GlobalRunner;
import pubsher.talexsoultech.talex.electricity.achieve.transfer.PathAlgorithm;

import java.util.HashMap;

/**
 * 电力装置管理类 - 提供操作方法
 *
 * @author TalexDreamSoul
 */

public class ElectricityAchiever {

    public static ElectricityAchiever INSTANCE;
    /**
     * 电力科技# 总Map 存储位置与电力设备的映射关系
     */
    @Getter
    private final HashMap<String, PathAlgorithm> electricityMap = new HashMap<>(64);
    @Getter
    private final GlobalRunner globalRunner;

    private ElectricityAchiever() {

        this.globalRunner = new GlobalRunner(this);

        this.globalRunner.runTaskTimerAsynchronously(TalexEssential.getInstance(), 40, 5);

    }

    public static ElectricityAchiever getInstance() {

        if ( INSTANCE == null ) {
            INSTANCE = new ElectricityAchiever();
        }

        return INSTANCE;

    }

    public ElectricityAchiever registerAlgorithm(PathAlgorithm pathAlgorithm) {

        this.electricityMap.put(pathAlgorithm.getID(), pathAlgorithm);

        return this;

    }

    public ElectricityAchiever unRegisterAlgorithm(PathAlgorithm pathAlgorithm) {

        this.electricityMap.remove(pathAlgorithm.getID(), pathAlgorithm);

        return this;

    }

}
