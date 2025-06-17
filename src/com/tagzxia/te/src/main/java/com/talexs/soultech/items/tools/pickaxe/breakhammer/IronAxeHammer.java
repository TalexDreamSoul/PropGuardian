package com.tagzxia.te.src.main.java.com.talexs.soultech.items.tools.pickaxe.breakhammer;

import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class IronAxeHammer extends BaseBreakHammer {

    private final StoneHammer stoneHammer;

    public IronAxeHammer(StoneHammer stoneHammer) {

        super("axe_gold", new ItemBuilder(Material.GOLDEN_AXE).setName("§f◆ 破碎锤斧(金)").setLore("", "§8| §e斧刃!", "").toItemStack());

        this.stoneHammer = stoneHammer;

    }

    @Override
    public ItemStack produce(PlayerData playerData, Block block) {

        Material material = block.getType();

        if ( material == Material.COBBLESTONE ) {

            Random random = new Random();

            int a = random.nextInt(1000);

            if ( a < 20 ) {

                return new ItemBuilder(Material.RED_SAND).setAmount(random.nextInt(8)).toItemStack();

            }

            if ( a <= 50 ) {

                if ( a <= 45 ) {
                    return new ItemBuilder(Material.COAL).setAmount(random.nextInt(11)).toItemStack();
                } else {
                    return new ItemBuilder(Material.IRON_ORE).setAmount(random.nextInt(5)).toItemStack();
                }

            }

            if ( a >= 70 && a <= 100 ) {
                return new ItemBuilder(Material.REDSTONE).setAmount(random.nextInt(9)).toItemStack();
            }

        }

        ItemStack last = stoneHammer.produce(playerData, block);

        if ( last != null ) {

            return last;

        }

        if ( material == Material.NETHERRACK ) {

            return new ItemStack(Material.SOUL_SAND);

        }

        return null;

    }

    @Override
    public void throwItem(PlayerData playerData, PlayerDropItemEvent event) {

    }

    @Override
    public void onItemHeld(PlayerData playerData, PlayerItemHeldEvent event) {

    }

}
