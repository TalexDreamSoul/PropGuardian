package com.tagzxia2.tagzxia1.te.src.main.resources.de.talex.electricity.function.generator;

import com.tagzxia.te.src.main.resources.de.talex.electricity.function.generator.IElectricityGenerator;
import lombok.Getter;
import org.bukkit.Location;
import pubsher.talexsoultech.talex.electricity.achieve.Capacity;
import pubsher.talexsoultech.talex.electricity.achieve.IPower;
import pubsher.talexsoultech.talex.electricity.achieve.IReceiver;
import pubsher.talexsoultech.talex.electricity.achieve.transfer.ITransfer;
import pubsher.talexsoultech.talex.electricity.achieve.transfer.RouterPath;
import pubsher.talexsoultech.utils.NBTsUtil;
import pubsher.talexsoultech.utils.item.ItemBuilder;
import pubsher.talexsoultech.utils.item.MachineBlockItem;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class BaseGeneratorObject extends MachineBlockItem implements IElectricityGenerator, IPower, ITransfer {


    // 不属于 电力基本范畴 属于应用范畴
//    /** 最多供电距离 **/
//    private int maxProvidePowerDistance = 0;

//    /** 存储电量 **/
//    private double storageCapacity = 0;

    // 使用 Capacity 类代替

//    /** 电压大小 **/
//    private double voltage = 0;

    /**
     * 最多存储电量
     **/
    private final double storageCapacity;
    /**
     * 接收能量设备到路径额映射
     **/
    private final Map<IReceiver, RouterPath> receiverPathMap = new HashMap<>(16);
    /**
     * 单次供电大小
     **/
    private double singleSupplyCapacity = 0;
    private Capacity capacity;

    public BaseGeneratorObject(String ID, ItemBuilder ib, double storageCapacity, double singleSupplyCapacity, double voltage) {

        super(ID, ib.addLoreLine("").addLoreLine("§f最大存储电量: §e" + storageCapacity + " §lSE ⚡")
                .addLoreLine("§f机器输出电压: §e" + voltage + " §lSV ⚡").addLoreLine("§f单次输出电量: §e" + singleSupplyCapacity + " §lSP ⚡")
                .addLoreLine("").toItemStack());

        this.singleSupplyCapacity = singleSupplyCapacity;
        this.capacity = new Capacity(0, voltage);
        this.storageCapacity = storageCapacity;

    }

    protected static Location String2Location(String loc) {

        return NBTsUtil.String2Location(loc);

    }

    protected static String Location2String(Location loc) {

        return NBTsUtil.Location2String(loc);

    }

    public BaseGeneratorObject delReceiverPath(IReceiver receiver, RouterPath routerPath) {

        this.receiverPathMap.remove(receiver, routerPath);

        return this;

    }

    public BaseGeneratorObject addReceiverPath(IReceiver receiver, RouterPath routerPath) {

        this.receiverPathMap.put(receiver, routerPath);

        return this;

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
    public void clearStorageCapacity() {

        capacity = null;

    }

}
