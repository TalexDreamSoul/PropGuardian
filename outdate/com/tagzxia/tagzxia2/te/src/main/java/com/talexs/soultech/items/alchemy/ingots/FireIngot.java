package com.tagzxia2.te.src.main.java.com.talexs.soultech.items.alchemy.ingots;

import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.items.alchemy.BaseAlchemyItem;
import com.talexs.soultech.machine.furnace_cauldron.FurnaceCauldronRecipe;
import com.talexs.soultech.internal.RecipeObject;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
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
public class FireIngot extends BaseAlchemyItem {

    public FireIngot() {

        super("fire_ingot", new ItemBuilder(Material.NETHER_BRICK).setName("§4火焰锭").setLore("", "§8| §c微微的灼烧感..", "").toItemStack());
    }

    @Override
    public RecipeObject getRecipe() {

        return new FurnaceCauldronRecipe("fire_ingot", this, 12000).setExport(this).setNeed(new MineCraftItem(Material.IRON_INGOT));

    }

    @Override
    public void onCrafted(Player player) {

        player.setFireTicks(player.getFireTicks() + 40);

    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(5));
    }
}
