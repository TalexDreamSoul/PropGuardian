package com.tagzxia2.te.src.main.resources.de.talex.electricity.achieve.addon;

import com.tagzxia.te.src.main.resources.de.talex.electricity.achieve.addon.TalexLocation;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import com.talexs.talexessential.TalexEssential;
import pubsher.talexsoultech.talex.electricity.achieve.Capacity;
import pubsher.talexsoultech.talex.electricity.achieve.IPather;
import pubsher.talexsoultech.talex.electricity.achieve.IPower;
import pubsher.talexsoultech.talex.electricity.achieve.IReceiver;
import pubsher.talexsoultech.talex.electricity.achieve.transfer.PathAlgorithm;
import pubsher.talexsoultech.talex.electricity.function.ElectricityAchiever;
import pubsher.talexsoultech.talex.electricity.function.wire.IWire;
import pubsher.talexsoultech.talex.items.machine.GeneratorMachine;
import pubsher.talexsoultech.talex.managers.ElectricityManager;
import pubsher.talexsoultech.utils.ParticleUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

/**
 * @author TalexDreamSoul
 */
public class GlobalRunner extends BukkitRunnable {

    private final ElectricityAchiever electricityAchiever;

    public GlobalRunner(ElectricityAchiever electricityAchiever) {

        this.electricityAchiever = electricityAchiever;

    }

    /**
     * When an object implementing interface <code>Runnable</code> is used to create a thread, starting the thread
     * causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        algorithm();

        for ( Map.Entry<String, PathAlgorithm> entry : new HashSet<>(electricityAchiever.getElectricityMap().entrySet()) ) {

            PathAlgorithm path = entry.getValue();
            path.initial();

        }

    }

    private void algorithm() {

        electricityAchiever.getElectricityMap().clear();

        ElectricityManager em = ElectricityManager.INSTANCE;

        for ( Map.Entry<Location, GeneratorMachine> entry : new HashSet<>(em.getGeneratorHashMap().entrySet()) ) {

            Location loc = entry.getKey();
            GeneratorMachine gm = entry.getValue();

            PathAlgorithm pathAlgorithm = new PathAlgorithm(loc.toString(), gm, new ArrayList<>()) {

                @Override
                public void onAlgorithmic(PathAlgorithm pa) {

                    for ( IPather ipather : pa.getPaths() ) {

                        TalexLocation tl = (TalexLocation) ipather;
                        TalexLocation last = null;
                        double loss = 0;

                        for ( TalexLocation loc = tl; loc != null; loc = loc.getFather() ) {

                            if ( tl.getFather() == null ) {
                                continue;
                            }

                            last = loc;

                            ParticleUtil.StraightLine(tl.getLoc().clone().add(0.5, 0.5, 0.5), tl.getFather().getLoc().clone().add(0.5, 0.5, 0.5), Particle.FLAME);

                            IPower power = getLocationBlockInstance(loc.getLoc());

                            if ( power instanceof IWire ) {

                                loss += ( (IWire) power ).getSingleTransmissionLoss();

                            }

                        }

                        assert tl != null;
                        assert last != null;
                        GeneratorMachine gm = ElectricityManager.INSTANCE.getGeneratorHashMap().get(last.getLoc());

                        IReceiver receiver = ElectricityManager.INSTANCE.getReceiverHashMap().get(tl.getLoc());

                        if ( receiver == null ) {
                            return;
                        }

                        Capacity capacity = new Capacity(gm.getSingleSupplyCapacity(), receiver.getCapacityVoltage());

                        if ( receiver.canStorageCapacity(capacity) && gm.canProvideCapacity(capacity) ) {

                            double finalLoss = loss;
                            new BukkitRunnable() {

                                @Override
                                public void run() {

                                    receiver.saveStorageCapacity(gm.provideCapacity(capacity.addStorageCapacity(new Capacity(-finalLoss, capacity.getVoltage()))));

                                }
                            }.runTask(TalexEssential.getInstance());

                        }

                    }

                }

                @Override
                public IPower getLocationBlockInstance(Location loc) {

                    return ElectricityManager.INSTANCE.getPowerInstance(loc);
                }

                @Override
                public boolean checkIsPowerAndReceiver(Location loc) {

                    return ElectricityManager.INSTANCE.checkPowerAndReceiver(loc);
                }
            };

            electricityAchiever.registerAlgorithm(pathAlgorithm);

        }

    }

}
