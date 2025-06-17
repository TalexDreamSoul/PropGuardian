package com.tagzxia2.te.src.main.java.com.talexs.soultech.items.electricity;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.electricity.BaseElectricityItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;
import java.util.Set;

public class ResinExtractor extends BaseElectricityItem {

    public ResinExtractor() {

        super("resin_extractor", new ItemBuilder(Material.STONE_HOE)

                .setName("§f树脂提取器")
                .setLore("", "§8| §f破坏树叶即可提取..", "")

                .toItemStack());

        addIgnoreType(VerifyIgnoreTypes.IgnoreEnchants).addIgnoreType(VerifyIgnoreTypes.IgnoreUnbreakable)
                .addIgnoreType(VerifyIgnoreTypes.IgnoreItemFlags);

    }

    @Override
    public WorkBenchRecipe getRecipe() {

        return new WorkBenchRecipe("resin_extractor", this)

                .addRequiredNull()
                .addRequired("compress_wood_1")
                .addRequiredNull()
                .addRequiredNull()
                .addRequired("st_strengthen_iron_stick")
                .addRequiredNull()
                .addRequiredNull()
                .addRequired("st_strengthen_iron_stick")
                .addRequiredNull()

                ;

    }

    @Override
    public boolean useItemBreakBlock(PlayerData playerData, BlockBreakEvent event) {

        Block block = event.getBlock();

        if ( !block.getType().name().contains("LEAVES") ) {
            return false;
        }

        if ( Math.random() <= 0.3 ) {

            ItemStack stack = new ItemBuilder(SoulTechItem.get("sticky_resin").getItemBuilder().toItemStack()).setAmount(new Random().nextInt(4)).toItemStack();

            if ( stack != null && stack.getType() != Material.AIR ) {
                block.getWorld().dropItem(block.getLocation().add(0.5, 0.5, 0.5), stack);
            }

        }

        return false;

    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(1));
    }

}
