package com.tagzxia.te.src.main.resources.de.talex.items.machine.rooter;

import lombok.Getter;
import lombok.Setter;
import pubsher.talexsoultech.talex.electricity.achieve.Capacity;
import pubsher.talexsoultech.talex.electricity.achieve.IReceiver;
import pubsher.talexsoultech.talex.electricity.function.wire.IWire;

/**
 * @author TalexDreamSoul
 */
@Getter
public abstract class BaseWire implements IWire, IReceiver {

    private final double maxStorage;
    /**
     * 单次供电大小
     **/
    private final double singleSupplyCapacity;
    protected Capacity capacity;
    @Setter
    private String symbol;

    public BaseWire(double singleSupplyCapacity, double voltage, double maxStorage) {

        this.capacity = new Capacity(0, voltage);
        this.singleSupplyCapacity = singleSupplyCapacity;
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
