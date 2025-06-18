package com.tagzxia3.tagzxia1.te.src.main.resources.de.talex.magic;

import com.tagzxia.te.src.main.resources.de.talex.magic.BaseHandle;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.util.Vector;
import pubsher.talexsoultech.addon.PlayerBackCoordinate;
import pubsher.talexsoultech.entity.PlayerData;
import pubsher.talexsoultech.entity.PlayerDataRunnable;
import pubsher.talexsoultech.talex.guider.category.RecipeObject;
import pubsher.talexsoultech.utils.block.TalexBlock;
import pubsher.talexsoultech.utils.item.ItemBuilder;

import java.util.Collection;

/**
 * @author TalexDreamSoul
 * @date 2021/8/3 17:25
 */
public class MagicMysteryHandle extends BaseHandle {

    public MagicMysteryHandle() {

        super("normal", new ItemBuilder(Material.BLAZE_ROD)
                .setName("§5◈ §c恐怖手杖 §eI").setLore("", "§c聚千物之灵魂, 养万年之精魄", "", "§e> §a剩余魔能: §e∞").addEnchant(Enchantment.DURABILITY, 1).addFlag(ItemFlag.HIDE_ENCHANTS).toItemStack());

        addIgnoreType(VerifyIgnoreTypes.IgnoreLores);

    }

    @Override
    public RecipeObject getRecipe() {

        return new RecipeObject("mystery_handle", this);

    }

    @Override
    public void onInteract(PlayerData playerData, PlayerInteractEvent event) {

        Player player = playerData.getPlayer();

        Location loc = player.getLocation();

        double yAddon = loc.getPitch() / ( Math.PI * 7 );

        loc = player.getLocation().add(0, 1.7 - ( yAddon ), 0);

        PlayerBackCoordinate playerBackCoordinate = new PlayerBackCoordinate(loc, 2.5);

        if ( player.isSneaking() ) {

            Collection<Entity> entites = loc.getWorld().getNearbyEntities(playerBackCoordinate.newLocation(0, 0), 5, 5, 5);

            for ( Entity entity : entites ) {

                if ( entity.equals(player) ) {
                    continue;
                }

                Vector vector = entity.getVelocity();

                entity.setVelocity(vector.multiply(-2.5).setY(5));

                for ( int angle = 0; angle < 90; angle++ ) {

                    double radians = Math.toRadians(angle * 4);
                    double x = Math.cos(radians) * 3.5;
                    double y = Math.sin(radians) * 3.5;

                    Location loc2 = playerBackCoordinate.newLocation(x, y);
                    loc.getWorld().spawnParticle(Particle.CRIT_MAGIC, loc2, 1, 0, 0, 0, 0);

                }

                if ( entity instanceof LivingEntity ) {

                    LivingEntity livingEntity = (LivingEntity) entity;

                    livingEntity.setGlowing(true);
                    livingEntity.setHealth(1);

                    if ( entity instanceof Player ) {

                        Player target = (Player) entity;

                        target.setAllowFlight(true);
                        target.setFoodLevel(0);
                        target.setFlySpeed(0);

                    }

                }

                entity.playEffect(EntityEffect.HURT);

            }

            Location finalLoc = loc;
            playerData.delayRun(new PlayerDataRunnable() {

                @Override
                public void run() {

                    for ( Entity entity : entites ) {

                        if ( entity.equals(player) ) {
                            continue;
                        }

                        Vector vector = entity.getVelocity();

                        entity.setVelocity(vector.multiply(-5.5).setY(5.5));

                        for ( int angle = 0; angle < 90; angle++ ) {

                            double radians = Math.toRadians(angle * 4);
                            double x = Math.cos(radians) * 2.5;
                            double y = Math.sin(radians) * 2.5;

                            Location loc2 = entity.getLocation().clone().add(x, 1, y);
                            finalLoc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc2, 1, 0, 0, 0, 0);

                        }

                        if ( entity instanceof LivingEntity ) {

                            LivingEntity livingEntity = (LivingEntity) entity;

                            livingEntity.setGlowing(false);

                            if ( entity instanceof Player ) {

                                Player target = (Player) entity;

                                target.setAllowFlight(false);
                                target.setFlySpeed(0.1F);

                            }

                        }

                        entity.playEffect(EntityEffect.LOVE_HEARTS);

                    }

                }

            }, 50);

            playerData.delayRun(new PlayerDataRunnable() {

                @Override
                public void run() {

                    for ( Entity entity : entites ) {

                        if ( entity.hasPermission("talex.soultech.admin") ) {
                            continue;
                        }

                        if ( entity.equals(player) ) {
                            continue;
                        }

                        Vector vector = entity.getVelocity();

                        entity.setVelocity(vector.multiply(-3.5).setY(1.5));

                        for ( int angle = 0; angle < 90; angle++ ) {

                            double radians = Math.toRadians(angle * 4);
                            double x = Math.cos(radians) * 5.5;
                            double y = Math.sin(radians) * 5.5;

                            Location loc2 = entity.getLocation().clone().add(x, 0.5, y);
                            finalLoc.getWorld().spawnParticle(Particle.SLIME, loc2, 3, 0, 0, 0, 0);

                        }

                        entity.playEffect(EntityEffect.RABBIT_JUMP);

                        if ( entity instanceof LivingEntity ) {

                            LivingEntity livingEntity = (LivingEntity) entity;

                            livingEntity.getWorld().strikeLightning(livingEntity.getLocation());

                            livingEntity.setHealth(0);

                        }

                    }

                }

            }, 100);

            playerData.actionBar("§a你使用了 §5剥夺灵魄").playSound(Sound.ENTITY_ENDERDRAGON_SHOOT, 1.1F, 1.1F);

            return;

        }

        playerData.actionBar("§a按下 §eSHIFT §a进行施法...").playSound(Sound.BLOCK_NOTE_BELL, 1.1F, 1.1F);

        for ( int angle = 0; angle < 90; angle++ ) {

            double radians = Math.toRadians(angle * 4);
            double x = Math.cos(radians) * 5.5;
            double y = Math.sin(radians) * 5.5;

            Location loc2 = playerBackCoordinate.newLocation(x, y);
            loc.getWorld().spawnParticle(Particle.CRIT_MAGIC, loc2, 1, 0, 0, 0, 0);

        }

    }

    @Override
    public boolean useItemBreakBlock(PlayerData playerData, BlockBreakEvent event) {

        return false;
    }

    @Override
    public void throwItem(PlayerData playerData, PlayerDropItemEvent event) {

    }

    @Override
    public boolean onPlaceItem(PlayerData playerData, BlockPlaceEvent event) {

        return false;
    }

    @Override
    public void onCrafted(PlayerData playerData) {

    }

    @Override
    public void onItemHeld(PlayerData playerData, PlayerItemHeldEvent event) {

        if ( playerData.getPlayer().getGameMode() != GameMode.CREATIVE ) {

            playerData.getPlayer().setAllowFlight(!playerData.getPlayer().getAllowFlight());

        }

    }

    @Override
    public boolean onItemBlockBreak(PlayerData playerData, TalexBlock block, BlockBreakEvent event) {

        return false;
    }

}
