package com.tagzxia2.te.src.main.resources.de.talex.electricity.function.repeater;

import com.tagzxia.te.src.main.resources.de.talex.electricity.function.repeater.IRepeater;
import lombok.Getter;
import pubsher.talexsoultech.talex.electricity.achieve.Capacity;
import pubsher.talexsoultech.talex.electricity.achieve.IPower;
import pubsher.talexsoultech.talex.electricity.achieve.IReceiver;
import pubsher.talexsoultech.talex.electricity.achieve.transfer.ITransfer;

/**
 * @author TalexDreamSoul
 */
public class RepeaterObject implements IRepeater, IPower, ITransfer, IReceiver {

    private final Capacity capacity;

    @Getter
    private final double maxStorage;

    public RepeaterObject(Capacity capacity, double maxStorage) {

        this.capacity = capacity;
        this.maxStorage = maxStorage;

    }

    @Override
    public Capacity provideCapacity(Capacity willCapacity) {

        return capacity.provideStorageCapacity(willCapacity);
    }

    @Override
    public boolean canProvideCapacity(Capacity willCapacity) {

        return capacity.canProvideCapacity(willCapacity);
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

}
