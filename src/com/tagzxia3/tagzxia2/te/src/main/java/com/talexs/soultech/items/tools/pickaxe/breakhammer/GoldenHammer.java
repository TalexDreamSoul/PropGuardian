package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.soultech.items.tools.pickaxe.breakhammer;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.tools.pickaxe.breakhammer.BaseBreakHammer;
import com.tagzxia.te.src.main.java.com.talexs.soultech.items.tools.pickaxe.breakhammer.IronHammer;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class GoldenHammer extends BaseBreakHammer {

    private final IronHammer ironHammer;

    public GoldenHammer(IronHammer ironHammer) {

        super("golden", new ItemBuilder(Material.GOLDEN_PICKAXE).setName("§f◆ 破碎锤(金)").setLore("", "§8| §e破坏总比创造易!", "").toItemStack());

        this.ironHammer = ironHammer;

    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe("stc_break_hammer_golden", this)
                .addRequiredNull()
                .addRequired("st_break_hammer_gold")
                .addRequiredNull()
                .addRequired("st_rough_mesh")
                .addRequiredNull()
                .addRequired("st_rough_mesh")
                .addRequiredNull()
                .addRequiredNull()
                .addRequiredNull();
    }

    @Override
    public ItemStack produce(PlayerData playerData, Block block) {

        Material material = block.getType();

        if (material == Material.END_STONE) {

            if ( Math.random() <= 0.025 ) {

                return SoulTechItem.get("space_dust");

            } else return new ItemStack(Material.COBBLESTONE);

        }

        if (material == Material.OBSIDIAN) {

            if ( Math.random() <= 0.05 ) {

                return SoulTechItem.get("space_dust");

            } else return new ItemStack(Material.END_STONE);

        }

        return ironHammer.produce(playerData, block);
    }

}
