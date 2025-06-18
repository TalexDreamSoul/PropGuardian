package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.parkour;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.parkour.ParkourProtector;
import com.talexs.soultech.addon.AddonHolder;
import com.talexs.soultech.internal.protector.ProtectorAddon;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.BaseModule;
import com.talexs.talexessential.modules.resource.PlayerRes;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class ParkourModule extends BaseModule {

    public ParkourModule() {
        super("parkour");
    }

    private Location startPoint;

    private Map<String, Location> savePoints = new HashMap<>();

    public boolean doWorldEnable(World world) {
        return yaml.getString("Settings.enableWorld", "").equalsIgnoreCase(world.getName());
    }

    public Location getPlayerPoint(Player player) {
        return this.savePoints.getOrDefault(player.getUniqueId().toString(), startPoint);
    }

    @EventHandler
    public void onWorldChange(PlayerTeleportEvent event) {
        Location eventTo = event.getTo();
        if ( event.getFrom().getWorld() == world || eventTo.getWorld() != world ) {
            return;
        }

        Location savePoint = getPlayerPoint(event.getPlayer());

        event.setTo(savePoint);

        new PlayerUser(event.getPlayer())
                .title("&f&lParkour", "&a欢迎回来，已定位到上次的存档点！", 0, 60, 20)
                .actionBar("&eSunrise&Lv &8&L| &a活泼生命 动力健康 主题跑酷")
                .playSound(Sound.ENTITY_ENDER_DRAGON_GROWL);
    }

    private static World world;

    @Override
    public void onEnable() {
        String _world = yaml.getString("Settings.enableWorld", "");
        world = Bukkit.getWorld(_world);

        String loc = yaml.getString("Settings.startPoint");

        String[] locs = loc.split(",");
        // -71,-50,233

        this.startPoint = new Location(world, Integer.parseInt(locs[0]), Integer.parseInt(locs[1]), Integer.parseInt(locs[2]));
    }

    @Override
    public void onAllModulesEnabled() {
        AddonHolder.getINSTANCE().get(ProtectorAddon.class).regProtector(new ParkourProtector(world));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if ( player.getWorld() != world ) return;

        toSavePoint(player);
    }

    @EventHandler
    public void onCmd(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if ( player.getWorld() != world ) return;

        if ( player.hasPermission("talex.res.bypass") ) return;

        if ( !event.getMessage().contains("tpa") && !event.getMessage().contains("tpaccept") ) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent  e) {
        Player player = e.getPlayer();
        World world = player.getWorld();

        if ( !doWorldEnable(world) ) return;

        if ( e.getAction().isRightClick() ) {

            ItemStack item = e.getItem();

            if ( item != null && item.getType() == Material.FIREWORK_ROCKET ) {
                e.setCancelled(true);
            }

        }
    }

    private void toSavePoint(Player player) {
        Location loc = getPlayerPoint(player);

        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

        new PlayerUser(player).teleport(loc).playSound(Sound.BLOCK_NOTE_BLOCK_PLING).title("", "&e已返回记录点！", 0, 25, 5);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        World world = player.getWorld();

        if ( !doWorldEnable(world) ) return;

        Block block = player.getLocation().add(0, -1, 0).getBlock();
        Material type = block.getType();

//        PlayerData pd = PlayerData.g(player);
        PlayerUser user = new PlayerUser(player);

        if ( !player.hasPermission("te.vip.admin") && !player.hasPermission("te.parkour.flight.bypass") ) {
            if ( player.isFlying() || player.getAllowFlight() ) {
                user.errorActionBar("跑酷时不允许飞行！").playSound(Sound.BLOCK_ANVIL_LAND);

                toSavePoint(player);

                return;
            }
        }

        if ( type == Material.GOLD_BLOCK) {
            if ( getPlayerPoint(player).distance(player.getLocation()) < 3 ) return;

            user.infoActionBar("你的记录点已成功设置！").playSound(Sound.ENTITY_PLAYER_LEVELUP);

            savePoints.put(player.getUniqueId().toString(), player.getLocation().add(0, 1, 0));

        } else if ( type == Material.WATER || type == Material.REDSTONE_BLOCK ) {
            toSavePoint(player);
        } else if ( type == Material.EMERALD_BLOCK ) {

            user.triggerEffect(new PotionEffect(PotionEffectType.JUMP, 10, 1));
        } else if ( type == Material.NETHERITE_BLOCK ) {

            user.triggerDarkness();
        } else if ( type == Material.IRON_BLOCK ) {

            user.triggerEffect(new PotionEffect(PotionEffectType.LEVITATION, 100, 1));
        } else if ( type == Material.DIAMOND_BLOCK ) {

            user.triggerEffect(new PotionEffect(PotionEffectType.SPEED, 10, 1));
        }
    }
}
