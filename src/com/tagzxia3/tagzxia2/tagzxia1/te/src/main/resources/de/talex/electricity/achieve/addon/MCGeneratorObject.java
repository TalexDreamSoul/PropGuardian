package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.resources.de.talex.electricity.achieve.addon;

import org.bukkit.Location;
import pubsher.talexsoultech.talex.electricity.achieve.Capacity;
import pubsher.talexsoultech.talex.electricity.achieve.transfer.MCTransfer;
import pubsher.talexsoultech.talex.items.machine.GeneratorMachine;

public abstract class MCGeneratorObject extends GeneratorMachine implements MCTransfer {

    public MCGeneratorObject(Location loc, Capacity capacity, double storageCapacity, double singleSupplyCapacity, double voltage) {

        super(loc, capacity, storageCapacity, singleSupplyCapacity, voltage);
    }

    public MCGeneratorObject(Location loc, double storageCapacity, double singleSupplyCapacity, double voltage) {

        super(loc, storageCapacity, singleSupplyCapacity, voltage);
    }

}
