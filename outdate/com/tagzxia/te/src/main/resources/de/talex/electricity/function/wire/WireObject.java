package com.tagzxia.te.src.main.resources.de.talex.electricity.function.wire;

import lombok.Getter;
import pubsher.talexsoultech.talex.electricity.achieve.Capacity;
import pubsher.talexsoultech.talex.electricity.achieve.IPower;
import pubsher.talexsoultech.talex.electricity.achieve.IReceiver;

/**
 * @author TalexDreamSoul
 */
public class WireObject implements IWire, IPower, IReceiver {

    private final Capacity capacity;

    @Getter
    private final double singleTransmissionLoss, maxStorage;

    public WireObject(Capacity capacity, double singleTransmissionLoss, double maxStorage) {

        this.capacity = capacity;
        this.singleTransmissionLoss = singleTransmissionLoss;
        this.maxStorage = maxStorage;

    }

    /**
     * 获取单次传递损耗 - 即经过本导线需要扣除的能量
     *
     * @return 单次传递损耗
     */
    @Override
    public double getSingleTransmissionLoss() {

        return singleTransmissionLoss;

    }

    @Override
    public Capacity saveStorageCapacity(Capacity addCapacity) {

        return capacity.addStorageCapacity(addCapacity);
    }

    @Override
    public double getCapacityVoltage() {

        return capacity.getVoltage();
    }

    @Override
    public boolean canStorageCapacity(Capacity capacity) {

        return this.capacity.getStorageCapacity() < getMaxStorage();

    }

    @Override
    public Capacity provideCapacity(Capacity willCapacity) {

        return capacity.provideStorageCapacity(willCapacity);
    }

    @Override
    public boolean canProvideCapacity(Capacity willCapacity) {

        return capacity.canProvideCapacity(willCapacity);
    }

}
