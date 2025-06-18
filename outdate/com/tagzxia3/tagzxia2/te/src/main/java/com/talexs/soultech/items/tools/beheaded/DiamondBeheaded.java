package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.soultech.items.tools.beheaded;

import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.internal.entity.unlock.RankCondition;
import com.talexs.soultech.items.tools.BaseToolItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.Set;

public class DiamondBeheaded extends BaseToolItem {
    public DiamondBeheaded() {
        super("st_diamond_beheaded", new ItemBuilder(Material.DIAMOND_SWORD).setName("&e斩首剑")
                .setLore(
                        "",
                        "&8| &e纯净&7，而不含一丝杂质！",
                        "&8| &e斩首&7，而不留一丝余地！",
                        "&8| &e锋芒&7，而不含一丝瑕疵！",
                        "&8| &e裂魂&7，而不留一丝残魄！",
                        "",
                        "&8| &7强化的材料能更好保存头颅",
                        ""
                )
                .addEnchant(Enchantment.LOOT_BONUS_MOBS, 6)
                .addEnchant(Enchantment.FIRE_ASPECT, 2)
                .addEnchant(Enchantment.KNOCKBACK, 2)
                .addEnchant(Enchantment.DAMAGE_ALL, 5)
                .addEnchant(Enchantment.SWEEPING_EDGE, 3)
                .addEnchant(Enchantment.MENDING, 1)
                .addEnchant(Enchantment.DURABILITY, 3)
                .toItemStack());

        addIgnoreType(VerifyIgnoreTypes.IgnoreDurability);
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe("stc_diamond_beheaded", this)
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.DIAMOND_BLOCK))
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.DIAMOND))
                .addRequired("st_strengthen_diamond_stick")
                .addRequired(new MineCraftItem(Material.DIAMOND))
                .addRequired("st_strengthen_gold_stick")
                .addRequired("st_glow_dust")
                .addRequired("st_strengthen_gold_stick")
                ;
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(50));
    }
}
