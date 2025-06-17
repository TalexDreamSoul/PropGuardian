package com.tagzxia.te.src.main.resources.de.talex.items.machine;

import lombok.Getter;
import org.bukkit.Location;
import pubsher.talexsoultech.talex.electricity.achieve.Capacity;
import pubsher.talexsoultech.talex.electricity.achieve.IPower;
import pubsher.talexsoultech.talex.electricity.achieve.IReceiver;
import pubsher.talexsoultech.talex.electricity.achieve.transfer.MCTransfer;
import pubsher.talexsoultech.talex.electricity.achieve.transfer.RouterPath;
import pubsher.talexsoultech.talex.electricity.function.generator.IElectricityGenerator;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class GeneratorMachine extends ElectricityMachine implements IElectricityGenerator, IPower, MCTransfer {

    /**
     * 最多存储电量
     **/
    private final double storageCapacity;
    /**
     * 接收能量设备到路径额映射
     **/
    private final Map<IReceiver, RouterPath> receiverPathMap = new HashMap<>(16);
    protected Capacity capacity;
    /**
     * 单次供电大小
     **/
    private double singleSupplyCapacity = 0;

    public GeneratorMachine(Location loc, Capacity capacity, double storageCapacity, double singleSupplyCapacity, double voltage) {

        super(loc.clone().add(0.5, 1.45, 0.5));

        this.singleSupplyCapacity = singleSupplyCapacity;
        this.capacity = capacity;
        this.storageCapacity = storageCapacity;

    }

    public GeneratorMachine(Location loc, double storageCapacity, double singleSupplyCapacity, double voltage) {

        super(loc.clone().add(0.5, 1.45, 0.5));

        this.singleSupplyCapacity = singleSupplyCapacity;
        this.capacity = new Capacity(0, voltage);
        this.storageCapacity = storageCapacity;

    }

    public GeneratorMachine delReceiverPath(IReceiver receiver, RouterPath routerPath) {

        receiverPathMap.remove(receiver, routerPath);

        return this;

    }

    public GeneratorMachine addReceiverPath(IReceiver receiver, RouterPath routerPath) {

        receiverPathMap.put(receiver, routerPath);

        return this;

    }

    @Override
    public Capacity provideCapacity(Capacity willCapacity) {

        updateHologram();

        return capacity.provideStorageCapacity(willCapacity);

    }

    @Override
    public boolean canProvideCapacity(Capacity willCapacity) {

        return capacity.canProvideCapacity(willCapacity);

    }

    @Override
    public void clearStorageCapacity() {

        updateHologram();

        capacity = null;

    }

    @Override
    public void updateHologram() {

        if ( hologram == null || hologram.isDeleted() ) {
            return;
        }

        updateMachineHologram(this);

    }

    /**
     * 重写方法 - 给予本类对象 # 便于调用变量
     *
     * @param generatorMachine 本类实例
     */
    public abstract void updateMachineHologram(GeneratorMachine generatorMachine);

}
