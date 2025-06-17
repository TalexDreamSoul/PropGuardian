package com.tagzxia2.te.src.main.java.com.talexs.soultech.items.tools.pickaxe.breakhammer;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.tools.pickaxe.breakhammer.BaseBreakHammer;
import com.tagzxia.te.src.main.java.com.talexs.soultech.items.tools.pickaxe.breakhammer.StoneHammer;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class IronHammer extends BaseBreakHammer {

    private final StoneHammer stoneHammer;

    public IronHammer(StoneHammer stoneHammer) {

        super("iron", new ItemBuilder(Material.IRON_PICKAXE).setName("§f◆ 破碎锤(铁)").setLore("", "§8| §e破坏总比创造易!", "").toItemStack());

        this.stoneHammer = stoneHammer;

    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe("stc_break_hammer_iron", this)
                .addRequiredNull()
                .addRequired("st_break_hammer_stone")
                .addRequiredNull()
                .addRequired("st_rough_mesh")
                .addRequired("st_strengthen_iron_stick")
                .addRequired("st_rough_mesh")
                .addRequiredNull()
                .addRequired("st_strengthen_iron_stick")
                .addRequiredNull();
    }

    @Override
    public ItemStack produce(PlayerData playerData, Block block) {

        Material material = block.getType();

        if (material == Material.COBBLESTONE) {

            Random random = new Random();

            int a = random.nextInt(900);

            if (a < 10) {

                return new ItemBuilder(Material.RED_SAND).setAmount(random.nextInt(8)).toItemStack();

            }

            if (a <= 30) {

                if (a <= 20) {
                    return new ItemBuilder(Material.COAL).setAmount(random.nextInt(11)).toItemStack();
                } else {
                    return new ItemBuilder(Material.IRON_ORE).setAmount(random.nextInt(5)).toItemStack();
                }

            }

            if (a >= 80 && a <= 90) {
                return new ItemBuilder(Material.REDSTONE).setAmount(random.nextInt(9)).toItemStack();
            }

            return new ItemStack(Material.COBBLESTONE);

        } else if (material == Material.GRAVEL) {

            Random random = new Random();

            int a = random.nextInt(120);

            if (a >= 20 && a <= 25) {
                return new ItemBuilder(Material.GOLD_ORE).setAmount(random.nextInt(2)).toItemStack();
            }

            return new ItemStack(Material.GRAVEL);

        }

        ItemStack last = stoneHammer.produce(playerData, block);

        if (last != null) {

            return last;

        }

        if (material == Material.NETHERRACK) {

            return new ItemStack(Material.SOUL_SAND);

        }

        return null;
    }

}
