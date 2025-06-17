package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.items.alchemy.ingots;

import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.items.alchemy.BaseAlchemyItem;
import com.talexs.soultech.machine.furnace_cauldron.FurnaceCauldronRecipe;
import com.talexs.soultech.internal.RecipeObject;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * <p>
 * {@link # pubsher.talexsoultech.talex.items.material.ingots }
 *
 * @author TalexDreamSoul
 * @date 2021/8/14 22:06
 * <p>
 * Project: TalexSoulTech
 * <p>
 */
public class FireIngotBlock extends BaseAlchemyItem {

    public FireIngotBlock() {

        super("fire_ingot_block", new ItemBuilder(Material.NETHER_BRICKS).setName("§4火焰块").setLore("", "§8| §c强烈的灼烧感..", "").toItemStack());

        setRecipe(new FurnaceCauldronRecipe("fire_ingot_block", this, 60000).setExport(this).setNeed(SoulTechItem.get("fire_ingot")));
    }

    @Override
    public void onCrafted(Player player) {

        player.setFireTicks(player.getFireTicks() + 100);

    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(10));
    }

}
