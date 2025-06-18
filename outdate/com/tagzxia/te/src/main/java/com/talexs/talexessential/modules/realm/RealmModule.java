package com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.realm;

import com.google.common.collect.Maps;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.builder.SqlTableBuilder;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.OnlinePlayerData;
import com.talexs.talexessential.data.player.PlayerDataRunnable;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.BaseModule;
import com.talexs.talexessential.modules.chat.ChatFunction;
import com.talexs.talexessential.modules.realm.addon.RealmAddons;
import com.talexs.talexessential.modules.realm.cmd.RealmCmdCompleter;
import com.talexs.talexessential.modules.realm.cmd.RealmCommands;
import com.talexs.talexessential.modules.realm.entity.RealmFlag;
import com.talexs.talexessential.modules.realm.entity.RealmIcon;
import com.talexs.talexessential.utils.LocationUtil;
import com.talexs.talexessential.utils.LogUtil;
import io.papermc.paper.event.entity.EntityMoveEvent;
import lombok.Getter;
import lombok.SneakyThrows;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import top.zoyn.particlelib.pobject.EffectGroup;
import top.zoyn.particlelib.pobject.Line;

import javax.annotation.Nullable;
import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RealmModule extends BaseModule {

    @Getter
    private static String serverName;

    private static List<String> disableWorlds = new ArrayList<>();

    public RealmModule() {
        super("realm");
    }

    private File flagFile, realmIcons;

    public static YamlConfiguration realmIconYaml;

    @SneakyThrows
    private void initRealmIcons() {
        this.realmIcons = outputs("icons.yml");

        realmIconYaml = new YamlConfiguration();

        realmIconYaml.load(this.realmIcons);
    }

    @SneakyThrows
    private void initFlags() {
        this.flagFile = outputs("flags.yml");

        YamlConfiguration yaml = new YamlConfiguration();

        yaml.load(this.flagFile);

        for ( String key : yaml.getConfigurationSection("Flags").getKeys(false) ) {
            RealmFlag realmFlag = RealmAddons.INS.getAddon(key);
            RealmFlag.quickAdapt(realmFlag, yaml);
//            RealmAddons.INS.regAddon(realmFlag);
        }
    }

    public static List<PlayerRealm> realms = new ArrayList<>();

    public static void injectTable() {
        // auto gen table
        SqlTableBuilder stb = new SqlTableBuilder()
                .setTableName("realm_list")
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull("null").setMain(true).setSubParamName("te_uuid").setType("VARCHAR(64)"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull(null).setMain(false).setSubParamName("te_name").setType("VARCHAR(64)"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull(null).setMain(false).setSubParamName("te_owner").setType("VARCHAR(64)"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull(null).setMain(false).setSubParamName("te_owner_uuid").setType("VARCHAR(64)"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull(null).setMain(false).setSubParamName("te_server").setType("VARCHAR(64)"))
                // timestamp (created and updated)
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setSubParamName("u").setLine("`te_updated` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setSubParamName("c").setLine("`te_created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull(null).setMain(false).setSubParamName("te_info").setType("MEDIUMTEXT"))
                ;

        TalexEssential.getInstance().getMySQLSource().table(stb);
    }

    public static @Nullable PlayerRealm getRealmByLoc(Location loc) {
        for ( PlayerRealm realm : realms ) {
            if ( realm.getRealmArea().isInArea(loc) ) {
                return realm;
            }
        }
        return null;
    }

    public static @Nullable PlayerRealm getRealmByName(String name) {
        for ( PlayerRealm realm : realms ) {
            if ( realm.getName().equalsIgnoreCase(name) ) {
                return realm;
            }
        }
        return null;
    }

    public static List<PlayerRealm> getRealmByOwner(String owner) {
        List<PlayerRealm> realms = new ArrayList<>();
        for ( PlayerRealm realm : realms ) {
            if ( realm.getOwner().equalsIgnoreCase(owner) ) {
                realms.add(realm);
            }
        }
        return realms;
    }

    @SneakyThrows
    private void loadAllRealms() {
        // load all realms from database
        ResultSet rs = TalexEssential.getInstance().getMySQLSource().r("realm_list", "te_server", serverName);

        while ( rs != null && rs.next() ) {

            realms.add(new PlayerRealm().de(rs));

        }

        LogUtil.log("[Realm] " + realms.size() + " realms loaded!");

    }

    @EventHandler
    public void onMove(EntityMoveEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player) return;

        Location from = event.getFrom().toBlockLocation();
        Location to = event.getTo().toBlockLocation();

        PlayerRealm fromRealm = getRealmByLoc(from);
        PlayerRealm toRealm = getRealmByLoc(to);

        if ( fromRealm == null && toRealm != null ) {
            RealmAddons.INS.getAddon("MOB").onEntityEnter(toRealm, event);
        }

        if ( fromRealm != null && toRealm == null ) {
            RealmAddons.INS.getAddon("MOB").onEntityLeave(toRealm, event);
        }

    }

    public static void drawBarrierMarker(Location loc) {
        World world = loc.getWorld();

        world.spawnParticle(Particle.BLOCK_MARKER, loc.clone().add(0, .25, 0), 1, 0, 0, 0, 0, Material.BARRIER.createBlockData());
        world.spawnParticle(Particle.BLOCK_MARKER, loc.clone().add(0, 1.25, 0), 1, 0, 0, 0, 0, Material.BARRIER.createBlockData());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Location from = event.getFrom().toBlockLocation();
        Location to = event.getTo().toBlockLocation();

        Player player = event.getPlayer();
        PlayerUser user = new PlayerUser(player);

        PlayerRealm fromRealm = getRealmByLoc(from);
        PlayerRealm toRealm = getRealmByLoc(to);

        if ( toRealm != null ) {

            if ( !toRealm.doVisible(player) ) {
                if ( toRealm.getRealmArea().isInArea(to) ) {
                    drawBarrierMarker(to);
                    event.setCancelled(true);
                    new PlayerUser(player)
                            .playSound(Sound.BLOCK_ANVIL_HIT, 1, 1)
                            .errorActionBar("你不可以进入这个领域！");
                    return;
                }
            }

            if ( fromRealm == null ) {
                user.playSound(Sound.BLOCK_NOTE_BLOCK_CHIME, 1, .25f)
                        .actionBar("&a+ &7已进入领域 &b" + toRealm.getName() + " &8| &7主人 &e" + toRealm.getOwner() + " &a+");
            }

            return;
        }

        if ( fromRealm != null ) {
            user.playSound(Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1, .25f)
                    .actionBar("&c- &7已离开领域 &b" + fromRealm.getName() + " &c-");
        }

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();
        if ( clickedBlock == null ) return;

        Location to = clickedBlock.getLocation();
        PlayerRealm realm = getRealmByLoc(to);
        if ( realm == null ) {
            goCreate(event);
            return;
        }

        Player player = event.getPlayer();
        OnlinePlayerData opd = PlayerData.g(player);

        BlockState state = clickedBlock.getState();

        if ( (state instanceof Sign) ) {

            if ( clickedBlock.getType().name().contains("SIGN") ) {

                Sign sign = (Sign) state;

                Side interactableSideFor = sign.getInteractableSideFor(event.getPlayer());

                RealmAddons.INS.getAddon("SIGN").onOpenSign(opd, realm, sign.getSide(interactableSideFor), event);

                return;
            }

        } else if ( state instanceof Container ) {

            RealmAddons.INS.getAddons().forEach(addon -> addon.onOpenContainer(opd, realm, (Container) state, event));

        } else {

            RealmAddons.INS.getAddons().forEach(addon -> addon.onInteract(opd, realm, event));
//                .onBlockExploded(pr, event, block);

        }

    }

    static Map<String, Location> realmCreateMap = Maps.newHashMap();

    private void goCreate(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if ( item == null ) return;

        if ( item.getType() != Material.WOODEN_HOE ) return;

        Player player = event.getPlayer();
        PlayerUser user = new PlayerUser(player);

        Location interactionPoint = event.getInteractionPoint();
        if ( interactionPoint == null ) return;

        if ( !player.hasPermission("te.realm.admin") && disableWorlds.stream().anyMatch((worldName) -> worldName.equalsIgnoreCase(interactionPoint.getWorld().getName())) ) {
            return;
        }

        List<PlayerRealm> playerRealms = getRealmByOwner(player.getName());

        int amo = 8;

        if ( player.hasPermission("te.admin") ) amo = -1;
        else if ( player.hasPermission("te.vip.pro") ) amo = 20;
        else if ( player.hasPermission("te.vip.plus") ) amo = 10;

        if ( amo != -1 && playerRealms.size() >= amo ) {
            user.errorActionBar("你最多只能拥有 " + amo + " 个领域！").playSound(Sound.BLOCK_ANVIL_HIT, 1, 1);
            return;
        }

        if ( !player.isSneaking() ) {
            user.errorActionBar("请蹲下以完成领域选点！").playSound(Sound.BLOCK_ANVIL_HIT, 1, 1);
            return;
        }

        event.setCancelled(true);
        Location location = realmCreateMap.get(player.getName());

        assert event.getClickedBlock() != null;
        Location clicked = event.getClickedBlock().getLocation();
        if ( location == null ) {
            realmCreateMap.put(player.getName(), clicked);
            user.errorActionBar("请再次选择一个点！").playSound(Sound.BLOCK_ANVIL_HIT, 1, 1);
            return;
        }

        Location loc1 = location;
        Location loc2 = clicked;
        if ( loc1.getWorld() != loc2.getWorld() ) {
            realmCreateMap.remove(player.getName());
            user.errorActionBar("请在同一个世界内选择！").playSound(Sound.BLOCK_ANVIL_HIT, 1, 1);
            return;
        }

        double size = RealmArea.calcLocSize(loc1, loc2);

        if ( size < 50 ) {
            realmCreateMap.remove(player.getName());
            user.errorActionBar("领域大小太小！").playSound(Sound.BLOCK_ANVIL_HIT, 1, 1);
            return;
        }

        RealmArea.BoundingSphere boundingSphere = RealmArea.BoundingSphere.getBoundingSphere(loc1, loc2);

        for (PlayerRealm realm : realms) {
            if ( !RealmArea.BoundingSphere.doSphereIntersect(boundingSphere, realm.getRealmArea().getSphere()) ) {
                realmCreateMap.remove(player.getName());
                user
                        .title("", "&e您与领域 &b" + realm.getName() + " &e间隔太小！", 0, 60, 40)
                        .errorActionBar("领域与领域间隔过小！").playSound(Sound.BLOCK_ANVIL_HIT, 1, 1);
                return;
            }
        }

        double price = size * 0.03;
        double tax = price * 0.05;

        if ( size > 100000 ) {
            price = price * 1.75;
            tax *= .99;
        }

        if ( size > 500000 ) {
            price = price * 1.25;
            tax *= .95;
        }

        if ( size > 1000000 ) {
            price = price * .99;
            tax *= 1.25;
        }

        if ( size > 3000000 ) {
            price = price * .95;
            tax *= 1.5;
        }

        if ( size > 5000000 ) {
            price = price * .90;
            tax *= 2;
        }

        if ( size > 10000000 ) {
            price = price * .88;
            tax *= 3;
        }

        if ( size > 50000000 ) {
            price = price * .85;
            tax *= 5;
        }

        if ( size > 100000000 ) {
            price = price * .80;
            tax *= 10;
        }

        String priceStr = String.format("%.2f", price);
        String taxStr = String.format("%.2f", tax);

        Location[] locs = LocationUtil.fixShafts(loc1, loc2);
        Line[] lines = LocationUtil.locs2Lines(locs);

        EffectGroup eg = new EffectGroup();

        for (Line line : lines) {
            eg.addEffect(line);
        }

        user.delayRunTimer(new PlayerDataRunnable() {
            @Override
            public void run() {
                if ( !realmCreateMap.containsKey(player.getName()) ) {
                    cancel();
                    return;
                }

                eg.show();

            }
        }, 0, 10);

        double finalPrice = price;
        double finalTax = tax;

        user.chatFunc(new ChatFunction() {

            @Override
            public void onBefore() {
                player.sendMessage("");
                player.sendMessage("");
                player.sendMessage("  §8[§bRealm §6领域§8]");
                player.sendMessage("  §7你正在创建一个私有领域, 这里是领域的基本信息.");
                player.sendMessage("  §7私有领域每增大 §c10W, 50W, 100W §7价格会减少.");
                player.sendMessage("  ");
                player.sendMessage("  §7领域大小: §b" + size + " §7方块");
                player.sendMessage("  §7预估价格: §b" + priceStr + " §7金币");
                player.sendMessage("  §7交纳税收: §b" + taxStr + " §7金币");
                player.sendMessage("");
                player.sendMessage("  §7你有 §e§l15秒 §7的时间来输入.");
                player.sendMessage("  §7输入 §c取消 §7以取消创建.");
                player.sendMessage("  §7请确认你的信息, 输入 §b你想要的领地名字 §7以确认创建.");
                player.sendMessage("");
                player.sendMessage("");
            }

            @Override
            public void execute(String value) {

                if ( value.equalsIgnoreCase("取消") ) {
                    user
                            .errorActionBar("你取消了领域创建.")
                            .playSound(Sound.ENTITY_VILLAGER_NO, 1, 1)
                    ;
                    return;
                }

                if ( value.contains(" ") ) {
                    user.errorActionBar("领域名字不能包含空格.")
                            .playSound(Sound.ENTITY_VILLAGER_NO, 1, 1)
                    ;
                    return;
                }

                PlayerRealm realm = getRealmByName(value);
                if ( realm != null ) {
                    user.errorActionBar("领域名字已被占用.")
                            .playSound(Sound.ENTITY_VILLAGER_NO, 1, 1)
                    ;
                    return;
                }

                EconomyResponse economyResponse = TalexEssential.getInstance().getEcon().withdrawPlayer(player, finalPrice + finalTax);
                if ( !economyResponse.transactionSuccess() ) {
                    user.errorActionBar("你没有足够的金币来创建领域(" + economyResponse.errorMessage + ").")
                            .playSound(Sound.ENTITY_VILLAGER_NO, 1, 1)
                    ;
                    realmCreateMap.remove(player.getName());
                    return;
                }

                PlayerRealm pr = new PlayerRealm();

                pr.setName(value).setRealmIcon(new RealmIcon(pr)).setOwner(player.getName()).setOwnerUUID(player.getUniqueId())
                        .setUuid(UUID.randomUUID())
                        .setUpdated(System.currentTimeMillis()).setServerName(serverName)
                        .setRealmArea(new RealmArea(pr, location, loc2));

                pr.save();

                realms.add(pr);

                user.infoActionBar("你成功创建了领域.").firework()
                        .sendMessage("§7[§bRealm§7] §7你创建了一个领域，名字: §e" + pr.getName())
                        .playSound(Sound.ENTITY_VILLAGER_YES, 1, 1);

            }

            @Override
            public void rejected() {

                user
                        .sendMessage("§7[§bRealm§7] §e领域创建已取消.")
                        .errorActionBar("领域创建已取消.")
                        .playSound(Sound.ENTITY_VILLAGER_NO, 1, 1)
                ;

            }

            @Override
            public void onBeforeCall() {
                realmCreateMap.remove(player.getName());
            }
        });

    }

    @EventHandler
    public void onIgnite(BlockBurnEvent event) {
        Block clickedBlock = event.getBlock();

        Location to = clickedBlock.getLocation();

        PlayerRealm pr = getRealmByLoc(to);
        if ( pr == null ) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onExploded(BlockExplodeEvent event) {
        new ArrayList<>(event.blockList()).forEach(block -> {
            Location to = block.getLocation();

            PlayerRealm pr = getRealmByLoc(to);
            if ( pr == null ) return;

            RealmAddons.INS.getAddon("EXPLODE")
                    .onBlockExploded(pr, event, block);
        });
    }

    @EventHandler
    public void onEntityExploded(EntityExplodeEvent event) {
//        Entity entity = event.getEntity();

//        if ( entity.getType() != EntityType.CREEPER ) return;

        new ArrayList<>(event.blockList()).forEach(block -> {
            Location to = block.getLocation();

            PlayerRealm pr = getRealmByLoc(to);
            if ( pr == null ) return;

            RealmAddons.INS.getAddon("EXPLODE")
                    .onEntityExploded(pr, event, block);
//            event.blockList().remove(block);
        });
    }

    @EventHandler
    public void onPVP(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if ( !(entity instanceof Player) ) return;

        Location location = entity.getLocation();
        PlayerRealm pr = getRealmByLoc(location);
        if ( pr == null ) return;

        RealmAddons.INS.getAddon("PVP").onPVP(PlayerData.g((Player) entity), pr, event);
    }

    @EventHandler
    public void onPVE(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();

        Location location = entity.getLocation();
        PlayerRealm pr = getRealmByLoc(location);
        if ( pr == null ) return;

        if ( damager instanceof Player && pr.use((Player) damager) ) {
            return;
        }

        if ( damager instanceof Projectile ) {
            Projectile projectile = (Projectile) damager;
            ProjectileSource shooter = projectile.getShooter();
            if ( shooter instanceof Player ) {
                Player player = (Player) shooter;
                if ( !pr.getFlag(RealmSets.ANIMAL) ) {
                    event.setCancelled(true);
                    new PlayerUser(player).errorActionBar("在这个领域你不可以这么做!").playSound(Sound.ENTITY_VILLAGER_NO, 1, 1);
                }
            }
        } else

        if ( !pr.getFlag(RealmSets.ANIMAL) ) {
            event.setCancelled(true);
            if ( damager instanceof Player )
                new PlayerUser((Player) damager).errorActionBar("在这个领域你不可以这么做!").playSound(Sound.ENTITY_VILLAGER_NO, 1, 1);
        }
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        Location location = event.getLocation();
        PlayerRealm pr = getRealmByLoc(location);
        if ( pr == null ) return;

        Entity entity = event.getEntity();
        if ( !(entity instanceof Monster) ) return;

//        if ( PlayerRealm.animals.contains(entity.getType()) ) return;

        RealmAddons.INS.getAddon("PVP").onMobSpawn(pr, event);
//        if ( entity instanceof ExperienceOrb
//                || entity instanceof Item || entity instanceof Projectile || entity instanceof ArmorStand
//                || entity instanceof TNTPrimed || entity instanceof Painting || entity instanceof ItemFrame
//        ) return;
//
//        if ( !pr.onEntitySpawn(entity.getType()) ) {
//            event.setCancelled(true);
//        }

    }

    @EventHandler
    public void onLiquidForm(BlockFromToEvent event) {
        Location toLoc = event.getToBlock().getLocation();
        Location fromLoc = event.getBlock().getLocation();

        PlayerRealm toPr = getRealmByLoc(toLoc);
        PlayerRealm fromPr = getRealmByLoc(fromLoc);

        if ( fromPr == null && toPr != null )
            RealmAddons.INS.getAddon("FLOW").onLiquidFlow(toPr, event);

    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Location toLoc = event.getTo();
        PlayerRealm toPr = getRealmByLoc(toLoc);

        if ( toPr == null ) return;

        OnlinePlayerData opd = PlayerData.g(event.getPlayer());

        boolean b = RealmAddons.INS.getAddons().stream().anyMatch(addon -> !addon.onTeleport(toPr, opd));

        if ( !b ) return;

        opd.getUser().errorActionBar("你不可以传送到这个领域！").playSound(Sound.BLOCK_ANVIL_HIT, 1, 1);

        event.setCancelled(true);

    }

    @Override
    public void onEnable() {
        serverName = this.yaml.getString("Settings.serverName");

        disableWorlds.addAll(this.yaml.getStringList("Settings.disableWorlds"));

        PluginCommand realmCmd = Bukkit.getPluginCommand("realm");
        assert realmCmd != null;
        realmCmd.setExecutor(new RealmCommands());
        realmCmd.setTabCompleter(new RealmCmdCompleter());

        outputs("flags.yml");

        LogUtil.log("[Realm] Server name loaded: " + serverName);

        loadAllRealms();

        this.initFlags();
        this.initRealmIcons();
    }

    @Override
    public void onDisable() {

        RealmModule.realms.forEach(PlayerRealm::save);

    }
}
