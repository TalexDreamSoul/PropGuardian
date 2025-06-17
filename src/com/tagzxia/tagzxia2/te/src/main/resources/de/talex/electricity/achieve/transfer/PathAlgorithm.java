package com.tagzxia2.te.src.main.resources.de.talex.electricity.achieve.transfer;

import com.tagzxia.te.src.main.resources.de.talex.electricity.achieve.transfer.MCTransfer;
import com.tagzxia.te.src.main.resources.de.talex.electricity.achieve.transfer.RouterPath;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Location;
import pubsher.talexsoultech.talex.electricity.achieve.IPather;
import pubsher.talexsoultech.talex.electricity.achieve.IPower;
import pubsher.talexsoultech.talex.electricity.achieve.addon.TalexLocation;
import pubsher.talexsoultech.utils.NBTsUtil;

import java.util.*;

/**
 * 路径算法实例类 - 调用此类进行计算
 */
@Data
public abstract class PathAlgorithm {

    private final com.tagzxia.te.src.main.resources.de.talex.electricity.achieve.transfer.MCTransfer mcTransfer;

    @Getter
    private final String ID;
    private long lastCalculationTime;
    private List<? extends IPather> paths;
    private List<Location> visit = new ArrayList<>();
    private HashMap<String, TalexLocation> tlmap = new HashMap<>(128);

    @Deprecated
    public PathAlgorithm(String ID, com.tagzxia.te.src.main.resources.de.talex.electricity.achieve.transfer.MCTransfer mcTransfer, List<? extends IPather> providePath) {

        this.ID = ID;
        this.mcTransfer = mcTransfer;
        this.paths = providePath;

    }

    public PathAlgorithm(String ID, MCTransfer mcTransfer) {

        this.ID = ID;
        this.mcTransfer = mcTransfer;

    }

    /**
     * 计算
     */
    public void initial() {

        Location loc = mcTransfer.getMainTransferLocation();

        long a = System.currentTimeMillis();

        this.paths = getLocationsByDFS(getTLByLoc(null, loc), mcTransfer.getProvideMaxDistance());

        this.lastCalculationTime = System.currentTimeMillis() - a;

        onAlgorithmic(this);

    }

    @Deprecated
    public void initial_Old() {

        Location loc = mcTransfer.getMainTransferLocation();

        long a = System.currentTimeMillis();

        this.paths = getRouterPathsByDFS(getTLByLoc(null, loc), mcTransfer.getProvideMaxDistance());

        this.lastCalculationTime = System.currentTimeMillis() - a;

        onAlgorithmic(this);

    }

    public abstract void onAlgorithmic(PathAlgorithm pathAlgorithm);

    protected Location String2Location(String str) {

        return NBTsUtil.String2Location(str);

    }

    protected String Location2String(Location loc) {

        return "[Location:" + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + "@" + loc.getWorld().getName() + "]";

    }

    private List<TalexLocation> getLocationsByDFS(TalexLocation mainLoc, double maxDistance) {

        visit = new ArrayList<>();

        return dfs(mainLoc, maxDistance);

    }

    @Deprecated
    private List<com.tagzxia.te.src.main.resources.de.talex.electricity.achieve.transfer.RouterPath> getRouterPathsByDFS(TalexLocation mainLoc, double maxDistance) {

        List<com.tagzxia.te.src.main.resources.de.talex.electricity.achieve.transfer.RouterPath> list = new ArrayList<>();

        visit = new ArrayList<>();

        List<TalexLocation> tls = dfs(mainLoc, maxDistance);

        for ( TalexLocation tl : tls ) {

            list.add(new RouterPath(tl.getFather(), getLocationBlockInstance(tl.getFather().getLoc()), tl, getLocationBlockInstance(tl.getLoc())));

//            for(TalexLocation loc = tl.getFather();loc != null && loc.getFather() != null;loc = loc.getFather()) {
//
//                System.out.println("Loc: " + loc.getLoc().toVector() + " 's father: " + loc.getFather().getLoc().toVector());
//
//            }

//            System.out.println("-?> 搜索成功 <?- # 从 " + Location2String(tl.getFather().getLoc()) + " 到 " + Location2String(tl.getLoc()) + " 是一条通路!");

        }
//
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//
//                for(TalexLocation tl : tls) {
//
//                    for(TalexLocation loc = tl;loc != null;loc = loc.getFather()) {
//
//                        ParticleUtil.StraightLine(tl.getLoc(), tl.getFather().getLoc(), Particle.FLAME);
//
//                    }
//
//                }
//
//            }
//        }.runTaskTimer(TalexEssential.getInstance(), 20, 20);

        return list;

    }

    @SneakyThrows
    private List<TalexLocation> dfs(TalexLocation startLoc, double maxDistance) {

        List<TalexLocation> list = new ArrayList<>();

        Set<TalexLocation> sets = getTalexLocationNearBy(startLoc, startLoc.getLoc());

        for ( TalexLocation loc : sets ) {

            if ( visit.contains(loc.getLoc()) ) {
                continue;
            }

            visit.add(loc.getLoc());

            if ( loc.getLoc().distance(startLoc.getLoc()) > maxDistance ) {
                continue;
            }

            if ( getLocationBlockInstance(loc.getLoc()) == null ) {
                continue;
            }

            list.addAll(dfs(loc, maxDistance));

            if ( checkIsPowerAndReceiver(loc.getLoc()) ) {

                list.add(getTLByLoc(loc, loc.getLoc()));

            }

        }

        return list;

    }

    protected TalexLocation getTLByLoc(TalexLocation nullFather, Location loc) {

        ;

        String str = Location2String(loc);

        if ( tlmap.containsKey(str) ) {
            return tlmap.get(str);
        }

        Location location = loc.clone().set(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

        tlmap.put(str, new TalexLocation(nullFather, location));

        return getTLByLoc(loc);

    }

    protected TalexLocation getTLByLoc(Location loc) {

        Location location = loc.clone().set(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

        String str = Location2String(location);

        if ( tlmap.containsKey(str) ) {
            return tlmap.get(str);
        }

        tlmap.put(str, new TalexLocation(getTLByLoc(loc), loc.getBlock().getLocation().clone()));

        return getTLByLoc(loc);

    }

    protected Set<Location> getLocationNearBy(Location mainLoc) {

        Location loc1 = mainLoc.clone().add(0, 1, 0);
        Location loc2 = mainLoc.clone().add(0, -1, 0);
        Location loc3 = mainLoc.clone().add(1, 0, 0);
        Location loc4 = mainLoc.clone().add(-1, 0, 0);
        Location loc5 = mainLoc.clone().add(0, 0, 1);
        Location loc6 = mainLoc.clone().add(0, 0, -1);

        return new HashSet<>(Arrays.asList(loc1, loc2, loc3, loc4, loc5, loc6));

    }

    protected Set<TalexLocation> getTalexLocationNearBy(TalexLocation nullFather, Location mainLoc) {

        Set<TalexLocation> locs = new HashSet<>();

        for ( Location loc : getLocationNearBy(mainLoc) ) {

            locs.add(getTLByLoc(nullFather, loc));

        }

        return new HashSet<>(locs);

    }

    protected Set<TalexLocation> getTalexLocationNearBy(Location mainLoc) {

        Set<TalexLocation> locs = new HashSet<>();

        for ( Location loc : getLocationNearBy(mainLoc) ) {

            locs.add(getTLByLoc(loc));

        }

        return new HashSet<>(locs);

    }

    /**
     * 获取目标位置的电力系统情况 - 即若返回 null 代表目标位置非电力系统!
     *
     * @param loc 提供的位置
     *
     * @return 目标位置的电力系统情况
     */
    public abstract IPower getLocationBlockInstance(Location loc);

    /**
     * 判断目标位置是否是 电力系统的接收类
     *
     * @param loc 提供的位置
     *
     * @return 标位置是否是 电力系统的接收类
     */
    public abstract boolean checkIsPowerAndReceiver(Location loc);

}
