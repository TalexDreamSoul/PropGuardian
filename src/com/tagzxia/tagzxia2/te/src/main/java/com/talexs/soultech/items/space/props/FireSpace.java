package com.tagzxia2.te.src.main.java.com.talexs.soultech.items.space.props;

import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.internal.entity.unlock.RankCondition;
import com.talexs.soultech.items.space.BaseSpaceItem;
import com.talexs.soultech.machine.furnace_cauldron.FurnaceCauldronRecipe;
import com.talexs.soultech.internal.RecipeObject;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.util.Set;

/**
 * <br /> {@link pubsher.talexsoultech.talex.items.space } <br />
 *
 * @author TalexDreamSoul
 * @date 2021/8/17 3:24 <br /> Project: TalexSoulTech <br />
 */
public class FireSpace extends BaseSpaceItem {

    public FireSpace() {

        super("space_fire_dust", new ItemBuilder(Material.GLOWSTONE_DUST)
                .setName("§e空间碎片 (火)").setLore("", "§8| §a虚空的烈焰能量..", "").addEnchant(Enchantment.DURABILITY, 1).addFlag(ItemFlag.HIDE_ENCHANTS).toItemStack());
    }

    @Override
    public RecipeObject getRecipe() {

        return new FurnaceCauldronRecipe("space_fire_dust", this, 300000).setExport(this).setAmount(1).setNeed(SoulTechItem.get("space_dust"));

    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(60));
    }

}
