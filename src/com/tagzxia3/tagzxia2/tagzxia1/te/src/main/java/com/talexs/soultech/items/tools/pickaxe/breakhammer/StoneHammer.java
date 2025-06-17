package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.items.tools.pickaxe.breakhammer;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.tools.pickaxe.breakhammer.BaseBreakHammer;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class StoneHammer extends BaseBreakHammer {

    public StoneHammer() {

        super("stone", new ItemBuilder(Material.STONE_PICKAXE).setName("§8◆ 破碎锤(石)").setLore("", "§8| §e破坏总比创造易!", "").toItemStack());

    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe("stc_break_hammer_stone", this)
                .addRequiredNull()
                .addRequired("st_rough_mesh")
                .addRequiredNull()
                .addRequired("st_rough_mesh")
                .addRequired("st_stone_hard_pickaxe")
                .addRequired("st_rough_mesh")
                .addRequiredNull()
                .addRequired("st_rough_mesh")
                .addRequiredNull()
                ;
    }

    @Override
    public ItemStack produce(PlayerData playerData, Block block) {

        Material material = block.getType();

        if ( material.name().contains("PLANKS") ) {
            return new ItemStack(Material.COBBLESTONE);
        }
        if ( material == Material.COBBLESTONE ) {

            Random random = new Random();

            int a = random.nextInt(800);

            if ( a <= 10 ) {

                if ( a <= 7 ) {
                    return new ItemBuilder(Material.COAL).setAmount(random.nextInt(6)).toItemStack();
                } else {
                    return new ItemBuilder(Material.IRON_ORE).setAmount(random.nextInt(3)).toItemStack();
                }

            }

            if ( a >= 45 && a <= 50 ) {
                return new ItemBuilder(Material.REDSTONE).setAmount(random.nextInt(5)).toItemStack();
            }

            return new ItemStack(Material.GRAVEL);

        }
        if ( material == Material.GRAVEL ) {
            return new ItemStack(Material.SAND);
        }
        if ( material == Material.SAND ) {

            return new ItemBuilder(Material.SAND).setDurability((short) 1).toItemStack();

        }

        return null;

    }

}
