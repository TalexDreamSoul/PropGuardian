package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.soultech;

import com.talexs.soultech.addon.AddonHolder;
import com.talexs.soultech.internal.block.BlockAddon;
import com.talexs.soultech.internal.entity.classfies.ClassifiesAddon;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.internal.machine.MachineAddon;
import com.talexs.soultech.internal.protector.ProtectorAddon;
import com.talexs.soultech.internal.entity.tech_object.TechObjectAddon;
import com.talexs.soultech.internal.registry.RegistryAddon;
import com.talexs.soultech.items.soul.Soul;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.BaseModule;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MachineItem;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Map;

/**
 * Define a soultech sub plugin.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/28 上午 04:19
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
@Getter
public class TalexSoulTech extends BaseModule {

    public static NamespacedKey SOULTECH_KEY = NamespacedKey.fromString("talex_soul_tech");

    public TalexSoulTech() {
        super("soultech");
        _internalInit();
    }

    public void onDisable() {
        AddonHolder.getINSTANCE().onDisable();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
//        EntityType entityType = e.getEntityType();

        if ( Math.random() < 0.125 ) {
            LivingEntity entity = e.getEntity();

            Location location = entity.getEyeLocation();

            Item spawn = location.getWorld().spawn(location, Item.class);

            spawn.setItemStack(new Soul());
            spawn.setCanMobPickup(false);
            spawn.setCanPlayerPickup(true);
            spawn.setGlowing(true);
            spawn.setGravity(false);
            spawn.setVelocity(spawn.getVelocity().setY(1.05));
        }

    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        Inventory inventory = event.getView().getTopInventory();

        if ( inventory.getType() == InventoryType.WORKBENCH || inventory.getType() == InventoryType.ANVIL || inventory.getType() == InventoryType.SMITHING ) {

            ItemStack stack = event.getCurrentItem();

            if (stack != null && (SoulTechItem.hasSoulTechID(stack))) {

                SoulTechItem item = SoulTechItem.getItem(stack);

                if (item != null && item.canUseAsOrigin()) {
                    return;
                }

                event.setCancelled(true);

                Player player = (Player) event.getWhoClicked();

                new PlayerUser(player).playSound(Sound.ENTITY_VILLAGER_NO, 1.1F, 1.1F).actionBar("§c§l物品的诡异魔力让你无法操纵!!").closeInventory();

            }

        }

    }

    @EventHandler
    public void onItemHold(PlayerItemHeldEvent event) {

        Player player = event.getPlayer();
        PlayerData playerData = PlayerData.g(player);

        ItemStack stack = player.getInventory().getItem(event.getNewSlot());
        if ( stack == null ) return;

        SoulTechItem sti = SoulTechItem.getItem(stack);
        if ( sti == null ) return;

        sti.onItemHeld(playerData, event);

    }

    @EventHandler
    public void onToggleSneak(PlayerToggleSneakEvent event) {

        PlayerData playerData = PlayerData.g(event.getPlayer());

        for ( Map.Entry<String, SoulTechItem> entry : SoulTechItem.getItems().entrySet() ) {

            entry.getValue().onSneak(playerData, event);

        }

    }

    @EventHandler
    public void onBucketFull(PlayerBucketFillEvent event) {

        PlayerData playerData = PlayerData.g(event.getPlayer());
        triggerEvent(playerData, event, event.getItemStack(), new SuccessTrigger() {
            @Override
            public void success(SoulTechItem sti) {

                if ( sti != null ) {

                    sti.onBucketFull(playerData, event);

                }
            }
        });

    }

    @EventHandler
    public void onDamaged(EntityDamageByEntityEvent event) {

        Entity entity = event.getEntity();

        if ( !( entity instanceof Player ) ) {
            return;
        }

        PlayerData playerData = PlayerData.g((Player) entity);

        for ( Map.Entry<String, SoulTechItem> entry : SoulTechItem.getItems().entrySet() ) {

            entry.getValue().onEntityDamage(playerData, event);

        }


    }

    @EventHandler
    public void onDamaged(EntityDamageEvent event) {

        Entity entity = event.getEntity();

        if ( !( entity instanceof Player ) ) {
            return;
        }

        Player player = (Player) entity;
        PlayerData playerData = PlayerData.g(player);

        if ( event.getCause() == EntityDamageEvent.DamageCause.FALL ) {
            int rankLevel = playerData.getInfo().getInt("Rank.Level", 0);
            double maxHealth = player.getMaxHealth();

            double damage = event.getDamage();

            damage = damage * rankLevel / 6 + damage * maxHealth * .005;

            if ( Math.random() < .75f )
                event.setDamage(EntityDamageEvent.DamageModifier.BASE, damage / 8);
            else event.setDamage(EntityDamageEvent.DamageModifier.BASE, damage / 2);
        }

        for ( Map.Entry<String, SoulTechItem> entry : SoulTechItem.getItems().entrySet() ) {

            entry.getValue().onDamaged(playerData, event);

        }


    }

    @EventHandler( priority = EventPriority.LOWEST )
    public void onInteract(PlayerInteractEvent event) {

        PlayerData playerData = PlayerData.g(event);
        if ( playerData == null ) return;

        AddonHolder.getINSTANCE().get(MachineAddon.class).onEvent(event);

        for ( Map.Entry<String, SoulTechItem> entry : new HashSet<>(SoulTechItem.getItems().entrySet()) ) {

            if ( entry.getValue() instanceof MachineItem) {

                ( (MachineItem) entry.getValue() ).onClickedMachineItemBlock(playerData, event);

            }

        }

        ItemStack stack = event.getItem();
        if ( stack == null ) return;

        SoulTechItem sti = SoulTechItem.getItem(stack);
        if ( sti == null ) return;

        triggerEvent(playerData, event, stack, new SuccessTrigger() {
            @Override
            public void success(SoulTechItem sti) {
                sti.onInteract(playerData, event);
            }
        });

    }

    @EventHandler( priority = EventPriority.LOWEST )
    public void onInteractEntity(PlayerInteractEntityEvent event) {

        PlayerData playerData = PlayerData.g(event);
        if ( playerData == null ) return;

        ItemStack stack = event.getPlayer().getInventory().getItem(event.getHand());

        SoulTechItem sti = SoulTechItem.getItem(stack);
        if ( sti == null ) return;

        triggerEvent(playerData, event, stack, new SuccessTrigger() {
            @Override
            public void success(SoulTechItem sti) {
                sti.onInteractEntity(playerData, event);
            }
        });

    }

    @EventHandler( priority = EventPriority.HIGHEST )
    public void onDrop(PlayerDropItemEvent event) {

        PlayerData playerData = PlayerData.g(event.getPlayer());
        ItemStack stack = event.getItemDrop().getItemStack().clone();

        triggerEvent(playerData, event, stack, new SuccessTrigger() {
            @Override
            public void success(SoulTechItem sti) {

                if ( sti != null ) {

                    sti.throwItem(playerData, event);

                }
            }
        });

    }

    private abstract static class SuccessTrigger {

        public abstract void success(SoulTechItem sti);

    }

    private void triggerEvent(PlayerData pd, PlayerEvent event, ItemStack stack, SuccessTrigger success) {
        triggerEvent(pd, event, stack, success, () -> {});
    }

    private void triggerEvent(PlayerData pd, PlayerEvent event, ItemStack stack, SuccessTrigger success, Runnable failed) {
        if ( stack != null ) {

            if ( event instanceof PlayerDropItemEvent || AddonHolder.getINSTANCE().get(ProtectorAddon.class).checkProtect(pd, event) ) {

                // check if the item is a SoulTech item
                SoulTechItem sti = SoulTechItem.getItem(stack);
                if ( sti != null ) {
                    success.success(sti);
                    return;
                }

            }

        }


        failed.run();
    }

    @Override
    public void onEnable() {
        AddonHolder instance = AddonHolder.getINSTANCE();

        instance.register(new ClassifiesAddon());
        instance.register(new TechObjectAddon());
        instance.register(new ProtectorAddon());
        instance.register(new MachineAddon());
        instance.register(new BlockAddon());
        instance.register(new RegistryAddon());

        instance.onEnable();
    }
}
