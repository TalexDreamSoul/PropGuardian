package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.resources.de.talex.items.machine.rooter;

import org.bukkit.Location;
import pubsher.talexsoultech.talex.electricity.achieve.Capacity;
import pubsher.talexsoultech.talex.electricity.achieve.IReceiver;
import pubsher.talexsoultech.talex.items.machine.GeneratorMachine;

/**
 * @author TalexDreamSoul
 */
public abstract class BaseStorager extends GeneratorMachine implements IReceiver {

    public BaseStorager(Location loc, Capacity capacity, double storageCapacity, double singleSupplyCapacity, double voltage) {

        super(loc, capacity, storageCapacity, singleSupplyCapacity, voltage);
    }

    public BaseStorager(Location loc, double storageCapacity, double singleSupplyCapacity, double voltage) {

        super(loc, storageCapacity, singleSupplyCapacity, voltage);
    }

    @Override
    public Capacity saveStorageCapacity(Capacity addCapacity) {

        if ( !canStorageCapacity(addCapacity) ) {

            capacity.setStorageCapacity(getStorageCapacity());

            return null;
        }

        updateHologram();

        Capacity capacity = super.capacity.addStorageCapacity(addCapacity);

        if ( capacity.getStorageCapacity() > getStorageCapacity() ) {

            capacity.setStorageCapacity(getStorageCapacity());

        }

        return capacity;

    }

    @Override
    public double getCapacityVoltage() {

        return getCapacity().getVoltage();
    }

    @Override
    public boolean canStorageCapacity(Capacity capacity) {

        return getStorageCapacity() > super.capacity.getStorageCapacity();

    }

}
