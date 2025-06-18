package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.resources.de.talex.magic;

import com.tagzxia.te.src.main.resources.de.talex.magic.BaseHandle;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
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
import pubsher.talexsoultech.talex.guider.category.RecipeObject;
import pubsher.talexsoultech.talex.machine.advanced_workbench.WorkBenchRecipe;
import pubsher.talexsoultech.utils.block.TalexBlock;
import pubsher.talexsoultech.utils.item.ItemBuilder;
import pubsher.talexsoultech.utils.item.MineCraftItem;

import java.util.Collection;

/**
 * @author TalexDreamSoul
 * @date 2021/8/3 17:25
 */
public class MagicNormalHandle extends BaseHandle {

    public MagicNormalHandle() {

        super("normal", new ItemBuilder(Material.STICK)
                .setName("§5◈ §b魔力手杖 §eI").setLore("", "§5蕴含奇妙能量的手杖..", "", "§e> §a剩余魔能: §e0.0/100").addEnchant(Enchantment.DURABILITY, 1).addFlag(ItemFlag.HIDE_ENCHANTS).toItemStack());

        addIgnoreType(VerifyIgnoreTypes.IgnoreLores);

    }

    @Override
    public RecipeObject getRecipe() {

        return new WorkBenchRecipe("normal_magic_handle", this)

                .addRequiredNull()
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.IRON_INGOT))
                .addRequiredNull()
                .addRequired("compress_stick2")
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.IRON_INGOT))
                .addRequiredNull()
                .addRequiredNull()

                ;
    }

    @Override
    public void onInteract(PlayerData playerData, PlayerInteractEvent event) {

        Player player = playerData.getPlayer();

        Location loc = player.getLocation();

        double yAddon = loc.getPitch() / ( Math.PI * 7 );

        loc = player.getLocation().add(0, 1.7 - ( yAddon ), 0);

        PlayerBackCoordinate playerBackCoordinate = new PlayerBackCoordinate(loc, 1.5);

        if ( player.isSneaking() ) {

            Collection<Entity> entites = loc.getWorld().getNearbyEntities(playerBackCoordinate.newLocation(0, 0), 1, 1, 1);

            for ( Entity entity : entites ) {

                Vector vector = entity.getVelocity();

                entity.setVelocity(vector.multiply(-3.5).setY(1.5));

                for ( int angle = 0; angle < 90; angle++ ) {

                    double radians = Math.toRadians(angle * 4);
                    double x = Math.cos(radians) * 1.5;
                    double y = Math.sin(radians) * 1.5;

                    Location loc2 = playerBackCoordinate.newLocation(x, y);
                    loc.getWorld().spawnParticle(Particle.CRIT_MAGIC, loc2, 1, 0, 0, 0, 0);

                }

                entity.playEffect(EntityEffect.HURT);

            }

            playerData.actionBar("§a你使用了 §f引力波动").playSound(Sound.ENTITY_ENDERDRAGON_SHOOT, 1.1F, 1.1F);

            return;

        }

        playerData.actionBar("§a按下 §eSHIFT §a进行施法...").playSound(Sound.BLOCK_NOTE_BELL, 1.1F, 1.1F);

        for ( int angle = 0; angle < 90; angle++ ) {

            double radians = Math.toRadians(angle * 4);
            double x = Math.cos(radians) * 0.5;
            double y = Math.sin(radians) * 0.5;

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

    }

    @Override
    public boolean onItemBlockBreak(PlayerData playerData, TalexBlock block, BlockBreakEvent event) {

        return false;
    }

}
