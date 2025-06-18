package com.tagzxia.te.src.main.resources.de.talex.electricity.function.wire;

import pubsher.talexsoultech.talex.electricity.achieve.transfer.ITransfer;

/**
 * 导线基本类
 *
 * @author TalexDreamSoul
 */
public interface IWire extends ITransfer {

    /**
     * 获取单次传递损耗 - 即经过本导线需要扣除的能量
     *
     * @return 单次传递损耗
     */
    double getSingleTransmissionLoss();

}
