package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.soultech.items.alchemy.ingots;

import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.items.alchemy.BaseAlchemyItem;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

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
public class FireEnderEye extends BaseAlchemyItem {

    public FireEnderEye() {

        super("fire_ender_eye", new ItemBuilder(Material.ENDER_EYE)
                .addEnchant(Enchantment.DURABILITY, 1)
                .addFlag(ItemFlag.HIDE_ENCHANTS)
                .setName("§4灼热之眼").setLore("", "§8| §c微微的灼烧感..", "").toItemStack());
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(10));
    }

}
