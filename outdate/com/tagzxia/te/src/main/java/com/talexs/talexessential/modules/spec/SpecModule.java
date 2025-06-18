package com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.spec;//package com.talexs.talexessential.modules.spec;
//
//import com.bekvon.bukkit.residence.api.ResidenceApi;
//import com.bekvon.bukkit.residence.protection.ClaimedResidence;
//import com.talexs.talexessential.TalexEssential;
//import com.talexs.talexessential.data.PlayerData;
//import com.talexs.talexessential.data.player.PlayerUser;
//import com.talexs.talexessential.modules.BaseModule;
//import net.milkbowl.vault.economy.EconomyResponse;
//import org.bukkit.GameMode;
//import org.bukkit.Location;
//import org.bukkit.Sound;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.player.PlayerMoveEvent;
//import org.bukkit.scheduler.BukkitRunnable;
//
//import java.util.*;
//
//public class SpecModule extends BaseModule {
//
//    public static Map<String, ResSpec> specs = new HashMap<>();
//
//    public static SpecModule instance;
//
//    @Override
//    public void onEnable() {
//
//    }
//
//    @Override
//    protected boolean shouldOutput() {
//        return false;
//    }
//
//    public SpecModule() {
//        super("spec");
//
//        instance = this;
//
//        loadEachRes();
//
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//
//                PlayerData.map.values().forEach(pd -> checkSpec(pd));
//
//                new BukkitRunnable() {
//                    @Override
//                    public void run() {
//
//                        saveEachRes();
//
//                    }
//                }.runTaskAsynchronously(TalexEssential.getInstance());
//
//            }
//        }.runTaskTimer(TalexEssential.getInstance(), 0 ,20);
//    }
//
//    private void saveEachRes() {
//        specs.values().forEach(resSpec -> {
//
//            yaml.set("res." + resSpec.getResName() + ".owner", resSpec.getOwner());
//            yaml.set("res." + resSpec.getResName() + ".ownerUniqueId", resSpec.getOwnerUniqueId().toString());
//            yaml.set("res." + resSpec.getResName() + ".money", resSpec.getMoney().name());
//            yaml.set("res." + resSpec.getResName() + ".gameMode", resSpec.getGameMode().name());
//            yaml.set("res." + resSpec.getResName() + ".earnMoney", resSpec.getEarnMoney());
//            yaml.set("res." + resSpec.getResName() + ".logs", resSpec.getLogs());
//
//        });
//    }
//
//    private void loadEachRes() {
//        if ( yaml.contains("res") )
//            Objects.requireNonNull(yaml.getConfigurationSection("res")).getKeys(false).forEach(resName -> {
//
//                String owner = yaml.getString("res." + resName + ".owner");
//                String ownerUniqueId = yaml.getString("res." + resName + ".ownerUniqueId");
//                String money = yaml.getString("res." + resName + ".money");
//                String gameMode = yaml.getString("res." + resName + ".gameMode");
//                double earnMoney = yaml.getDouble("res." + resName + ".earnMoney");
//                List<String> logs = yaml.getStringList("res." + resName + ".logs");
//
//                assert ownerUniqueId != null;
//                ResSpec resSpec = new ResSpec(ResidenceApi.getResidenceManager().getByName(resName), resName, owner, UUID.fromString(ownerUniqueId), ResSpecMoney.valueOf(money));
//
//                resSpec.setGameMode(GameMode.valueOf(gameMode));
//                resSpec.setEarnMoney(earnMoney);
//                resSpec.setLogs(logs);
//
//                specs.put(resName, resSpec);
//
//            });
//    }
//
//    private void checkSpec(PlayerData pd) {
//        PlayerResSpec resSpec = pd.getResSpec();
//        if ( resSpec == null ) return;
//        ResSpec spec = resSpec.getSpec();
//        if ( spec == null ) return;
//
//        Player player = pd.getOfflinePlayer().getPlayer();
//        assert player != null;
//
//        ClaimedResidence res = spec.getRes();
//        if ( !res.containsLoc(player.getLocation()) ) {
//            resSpec.setSpec(null);
//            resSpec.setStartTime(0);
//            return;
//        }
//
//        long now = System.currentTimeMillis();
//        long lastTime = resSpec.getLastTime();
//
//        long diff = now - lastTime;
//        if ( lastTime != -1 && diff < 60 * 1000 ) {
//            return;
//        }
//
//        long diffStart = now - resSpec.getStartTime();
//        if ( lastTime != -1 && diffStart < 1000 * 60 * 15 ) {
//            return;
//        }
//
//        // take money
//        double price = lastTime == -1 ? spec.getMoney().getStartPrice() : spec.getMoney().getPricePerMinute();
//
//        EconomyResponse economyResponse = TalexEssential.getInstance().getEcon().withdrawPlayer(player, price);
//        if ( !economyResponse.transactionSuccess() ) {
//
//            resSpec.leave("金币不足:" + economyResponse.errorMessage);
//            return;
//
//        }
//
//        resSpec.setLastTime(now);
//
//    }
//
//    public boolean doResSpec(ClaimedResidence res) {
//        return specs.containsKey(res.getName());
//    }
//
//    public ResSpec getResSpec(ClaimedResidence res) {
//        return specs.get(res.getName());
//    }
//
//    public void addResSpec(ClaimedResidence res, ResSpec spec) {
//        specs.put(res.getName(), spec);
//    }
//
//    @EventHandler
//    public void onMove(PlayerMoveEvent event) {
//        Location to = event.getTo();
//
//        ClaimedResidence res = ResidenceApi.getResidenceManager().getByLoc(to);
//        if ( res == null ) return;
//
//        if ( !doResSpec(res) ) return;
//
//        Player player = event.getPlayer();
//        if ( player.hasPermission("talex.spec.join.bypass") ) return;
//
//        if ( res.isTrusted(player) ) return;
//
////        PlayerData pd = PlayerData.g(event.getPlayer());
////        ResSpec spec = getResSpec(res);
//
//        new PlayerUser(player).errorActionBar("你无法直接进入 领衔位标 !").playSound(Sound.BLOCK_ANVIL_FALL, 1, 1);
//
//        event.setCancelled(false);
//
////        pd.getResSpec().start(event.getPlayer().getGameMode(), to, spec);
//    }
//}
