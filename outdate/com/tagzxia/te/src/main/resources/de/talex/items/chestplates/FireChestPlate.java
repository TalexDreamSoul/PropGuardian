package com.tagzxia.te.src.main.resources.de.talex.items.chestplates;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import pubsher.talexsoultech.entity.PlayerData;
import pubsher.talexsoultech.talex.guider.category.RecipeObject;
import pubsher.talexsoultech.talex.machine.advanced_workbench.WorkBenchRecipe;
import pubsher.talexsoultech.utils.NBTsUtil;
import pubsher.talexsoultech.utils.block.TalexBlock;
import pubsher.talexsoultech.utils.item.ItemBuilder;
import pubsher.talexsoultech.utils.item.SoulTechItem;

/**
 * <p>
 * {@link # pubsher.talexsoultech.talex.items.chestplates }
 *
 * @author TalexDreamSoul
 * @date 2021/8/14 22:27
 * <p>
 * Project: TalexSoulTech
 * <p>
 */
public class FireChestPlate extends SoulTechItem {

    public FireChestPlate() {

        super("fire_chestplate", new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§c烈焰甲").setLore("", "§8> §c抵御一切自然火焰伤害", "§a抵挡部分自然伤害.")

                .setLeatherArmorColor(Color.RED)
                .toItemStack());

    }

    @Override
    public RecipeObject getRecipe() {

        return new WorkBenchRecipe("fire_chestplate", this)

                .addRequired("fire_ingot")
                .addRequiredNull()
                .addRequired("fire_ingot")
                .addRequired("fire_ingot")
                .addRequired("fire_ingot_block")
                .addRequired("fire_ingot")
                .addRequired("fire_ingot")
                .addRequired("fire_ingot")
                .addRequired("fire_ingot")

                ;
    }

    @Override
    public void onDamaged(PlayerData playerData, EntityDamageEvent event) {

        ItemStack stack = playerData.getPlayer().getInventory().getChestplate();

        String id = NBTsUtil.getTag(stack, "soul_tech_item_id");

        if ( id != null && id.equalsIgnoreCase(this.getID()) ) {

            if ( event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK ) {

                playerData.playSound(Sound.ENTITY_IRONGOLEM_ATTACK, 0.3f, 1.3f);

                event.setCancelled(true);

            }

            event.setDamage(EntityDamageEvent.DamageModifier.ARMOR, 5);

        }

    }

    @Override
    public void onInteract(PlayerData playerData, PlayerInteractEvent event) {

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
