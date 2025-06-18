package com.tagzxia.tagzxia1.te.src.main.resources.de.talex.electricity.achieve.transfer;

import pubsher.talexsoultech.talex.electricity.achieve.Capacity;
import pubsher.talexsoultech.talex.electricity.achieve.IPower;

/**
 * 转移类 - 实现此接口即可分发电量
 *
 * @author TalexDreamSoul
 */
public interface ITransfer extends IPower {

    /**
     * 申请从系统提取电量
     *
     * @param willCapacity 要提取的电量
     *
     * @return 成功返回真 失败返回所能调取的电量 如系统只有3 调取5 则返回3 返回null代表调取失败 可能被其他程序阻止 建议先 canProvideCapacity 并且调取的电量受字段
     * singleSupplyCapacity 影响 调取的电量默认是 Voltage 电压
     */
    Capacity provideCapacity(Capacity willCapacity);


    /**
     * 检测是否可以调取指定的电量
     *
     * @param willCapacity 要调取的电量
     *
     * @return 可以调取返回真
     */
    boolean canProvideCapacity(Capacity willCapacity);

}
