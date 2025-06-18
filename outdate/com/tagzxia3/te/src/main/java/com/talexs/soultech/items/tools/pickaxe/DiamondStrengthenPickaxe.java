package com.tagzxia3.te.src.main.java.com.talexs.soultech.items.tools.pickaxe;

import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.internal.entity.unlock.RankCondition;
import com.talexs.soultech.items.tools.BaseToolItem;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Set;

public class DiamondStrengthenPickaxe extends BaseToolItem {
    public DiamondStrengthenPickaxe() {
        super("st_diamond_strengthen_pickaxe", new ItemBuilder(Material.DIAMOND_PICKAXE).setName("&f强化钻石镐")
                .setLore(
                        "",
                        "&8| &7蕴藏强大材料的钻石镐",
                        "&8| &7能够容纳多种强大附魔",
                        "",
                        "&8| &e蹲下右键 &7以切换附魔.",
                        ""
                )
                .addEnchant(Enchantment.DIG_SPEED, 6)
                .addEnchant(Enchantment.SILK_TOUCH, 1)
                .addEnchant(Enchantment.DURABILITY, 8)
                .addEnchant(Enchantment.MENDING, 1)
                .toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe("stc_diamond_strengthen_pickaxe", this)
                .addRequired("st_diamond_hard_pickaxe")
                .addRequired("st_harden_core")
                .addRequired("st_diamond_hard_pickaxe")
                .addRequiredNull()
                .addRequired("st_strengthen_emerald_stick")
                .addRequiredNull()
                .addRequiredNull()
                .addRequired("st_strengthen_emerald_stick")
                .addRequiredNull()
                ;
    }

    @Override
    public void onInteract(PlayerData playerData, PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if ( !player.isSneaking() ) return;

        PlayerInventory inventory = player.getInventory();

        ItemStack stack = inventory.getItemInMainHand();

        ItemMeta meta = stack.getItemMeta();

        if ( meta.hasEnchant(Enchantment.SILK_TOUCH) ) {
            meta.removeEnchant(Enchantment.SILK_TOUCH);
            meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 3, true);
        } else {
            meta.removeEnchant(Enchantment.LOOT_BONUS_BLOCKS);
            meta.addEnchant(Enchantment.SILK_TOUCH, 5, true);
        }

        if ( !meta.hasEnchant(Enchantment.DURABILITY) )
            meta.addEnchant(Enchantment.DURABILITY, 8, true);

        meta.setUnbreakable(false);

        stack.setItemMeta(meta);

        player.damageItemStack(stack, 10);

        inventory.setItemInMainHand(stack);

        event.setCancelled(true);

        new PlayerUser(event.getPlayer()).infoActionBar("模式已切换!").playSound(Sound.BLOCK_ANVIL_HIT, 1, 1);

    }

    @Override
    public void onItemHeld(PlayerData playerData, PlayerItemHeldEvent event) {
        new PlayerUser(event.getPlayer()).infoActionBar("蹲下右键以切换模式!");
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(15));
    }
}
