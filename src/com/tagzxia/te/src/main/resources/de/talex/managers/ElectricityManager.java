package com.tagzxia.te.src.main.resources.de.talex.managers;

import lombok.Getter;
import org.bukkit.Location;
import pubsher.talexsoultech.talex.electricity.achieve.IPower;
import pubsher.talexsoultech.talex.electricity.achieve.IReceiver;
import pubsher.talexsoultech.talex.electricity.function.wire.IWire;
import pubsher.talexsoultech.talex.items.machine.GeneratorMachine;

import java.util.HashMap;

/**
 * 电力管理类
 *
 * @author TalexDreamSoul
 */
public class ElectricityManager {

    public static final ElectricityManager INSTANCE = new ElectricityManager();

    @Getter
    private final HashMap<Location, GeneratorMachine> generatorHashMap = new HashMap<>(32);

    @Getter
    private final HashMap<Location, IReceiver> receiverHashMap = new HashMap<>(64);

    @Getter
    private final HashMap<Location, IWire> wireHashMap = new HashMap<>(128);

    public IPower getPowerInstance(Location loc) {

        GeneratorMachine gm = generatorHashMap.getOrDefault(loc, null);
        IReceiver ir = receiverHashMap.getOrDefault(loc, null);

        return gm == null ? ( ir == null ? wireHashMap.getOrDefault(loc, null) : ir ) : gm;

    }

    public boolean checkPowerAndReceiver(Location loc) {

        IPower power = getPowerInstance(loc);

        return power instanceof IReceiver;

    }

    public void registerElectricity(Location loc, GeneratorMachine generator) {

        this.generatorHashMap.put(loc.clone().getBlock().getLocation(), generator);

    }

    public void registerElectricity(Location loc, IReceiver receiver) {

        this.receiverHashMap.put(loc.clone().getBlock().getLocation(), receiver);

    }

    public void registerElectricity(Location loc, IWire wire) {

        this.wireHashMap.put(loc.clone().getBlock().getLocation(), wire);

    }

    public void unRegisterElectricity(Location loc, GeneratorMachine generator) {

        this.generatorHashMap.remove(loc.clone().getBlock().getLocation(), generator);

    }

    public void unRegisterElectricity(Location loc, IReceiver receiver) {

        this.receiverHashMap.remove(loc.clone().getBlock().getLocation(), receiver);

    }

    public void unRegisterElectricity(Location loc, IWire wire) {

        this.wireHashMap.remove(loc.clone().getBlock().getLocation(), wire);

    }

    public void unRegisterGenerator(Location loc) {

        this.generatorHashMap.remove(loc.clone().getBlock().getLocation());

    }

    public void unRegisterReceiver(Location loc) {

        this.receiverHashMap.remove(loc.clone().getBlock().getLocation());

    }

    public void unRegisterWire(Location loc) {

        this.wireHashMap.remove(loc.clone().getBlock().getLocation());

    }

}
