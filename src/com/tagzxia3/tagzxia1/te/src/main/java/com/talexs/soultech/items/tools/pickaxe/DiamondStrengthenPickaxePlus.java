package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.soultech.items.tools.pickaxe;

import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.items.tools.BaseToolItem;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Set;

public class DiamondStrengthenPickaxePlus extends BaseToolItem {
    public DiamondStrengthenPickaxePlus() {
        super("st_diamond_strengthen_pickaxe_plus", new ItemBuilder(Material.DIAMOND_PICKAXE).setName("&f强化钻石镐 &b&l+")
                .setLore(
                        "",
                        "&8| &7蕴藏强大材料的钻石镐",
                        "&8| &7能够容纳多种强大附魔",
                        "",
                        "&8| &7主动技 &e空易",
                        "&8|   &e蹲下右键 &7以切换附魔",
                        "&8|",
                        "&8| &7主动技 &e地岩狱锁",
                        "&8|   &7在地狱挖掘地狱岩时引发连锁",
                        ""
                )
                .addEnchant(Enchantment.DIG_SPEED, 7)
                .addEnchant(Enchantment.SILK_TOUCH, 5)
                .addEnchant(Enchantment.DURABILITY, 10)
                .addEnchant(Enchantment.MENDING, 1)
                .toItemStack());
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe("stc_diamond_strengthen_pickaxe", this)
                .addRequired("st_diamond_strengthen_pickaxe")
                .addRequired("st_harden_core")
                .addRequired("st_diamond_strengthen_pickaxe")
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

        if ( !player.isSneaking() || event.getAction() != Action.RIGHT_CLICK_AIR ) return;

        PlayerInventory inventory = player.getInventory();

        ItemStack stack = inventory.getItemInMainHand();

        ItemMeta meta = stack.getItemMeta();
        if ( meta == null ) return;

        if ( meta.hasEnchant(Enchantment.SILK_TOUCH) ) {
            meta.removeEnchant(Enchantment.SILK_TOUCH);
            meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 5, true);
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
    public boolean useItemBreakBlock(PlayerData playerData, BlockBreakEvent event) {
        Block block = event.getBlock();

        World world = block.getWorld();

        if ( !(world.getName().contains("nether")) ) return super.useItemBreakBlock(playerData, event);

        if ( block.getType() != Material.NETHERRACK ) return super.useItemBreakBlock(playerData, event);

        for ( int x = -1; x <= 1; x++ ) {
            for ( int y = -1; y <= 1; y++ ) {
                for ( int z = -1; z <= 1; z++ ) {
                    Block b = block.getRelative(x, y, z);
                    if ( b.getType() == Material.NETHERRACK ) {

                        BlockBreakEvent _event = new BlockBreakEvent(b, event.getPlayer());

                        if ( !_event.isCancelled() )
                            b.breakNaturally(event.getPlayer().getItemInHand());
                    }
                }
            }
        }

        return super.useItemBreakBlock(playerData, event);

    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(15));
    }
}
