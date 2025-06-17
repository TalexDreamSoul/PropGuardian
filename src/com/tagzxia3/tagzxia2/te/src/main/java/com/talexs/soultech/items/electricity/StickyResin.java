package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.soultech.items.electricity;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.electricity.BaseElectricityItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Set;

public class StickyResin extends BaseElectricityItem {

    public StickyResin() {

        super("sticky_resin", new ItemBuilder(Material.SLIME_BALL)

                .setName("§f粘性树脂")
                .setLore("", "§8| §a黏糊糊的,就像是§l鼻涕§a...", "§e试试全身泡在水里丢出它..", "")
                .addEnchant(Enchantment.DURABILITY, 1)
                .addFlag(ItemFlag.HIDE_ENCHANTS)

                .toItemStack());

    }

    @Override
    public void onInteract(PlayerData playerData, PlayerInteractEvent event) {

        new PlayerUser(event.getPlayer()).actionBar("§a请不要用这一坨像是鼻涕的东西乱摸!").playSound(Sound.ENTITY_VILLAGER_NO, 1.2F, 1.2F);

    }

    @Override
    public void throwItem(PlayerData playerData, PlayerDropItemEvent event) {

        Item dropItem = event.getItemDrop();

        new PlayerUser(event.getPlayer()).actionBar("§a§l你扔出了这一坨黏糊糊的东西!真恶心!").playSound(Sound.ENTITY_VILLAGER_NO, 1.1F, 1.1F);

        Location loc = dropItem.getLocation();
        if ( !loc.getBlock().getType().name().contains("WATER") ) {
            return;
        }

        Collection<Entity> entities = dropItem.getWorld().getNearbyEntities(dropItem.getLocation(), 2, 2, 2);

        for ( Entity entity : entities ) {

            if ( !( entity instanceof Item ) ) {
                continue;
            }

            Item item = (Item) entity;

            ItemStack stack = item.getItemStack();

            if ( !this.checkID(stack) ) {
                continue;
            }

            item.remove();
            dropItem.remove();

            item.getWorld().dropItem(loc, SoulTechItem.get("resin"));
            item.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, item.getLocation(), 5, 0, 0.1, 0, 0.001);

        }

    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(1));
    }

}
