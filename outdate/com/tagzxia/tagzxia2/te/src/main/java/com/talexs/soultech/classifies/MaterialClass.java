package com.tagzxia2.te.src.main.java.com.talexs.soultech.classifies;

import com.talexs.soultech.internal.entity.classfies.BaseClassifiesCreator;
import com.talexs.soultech.internal.entity.unlock.RankCondition;
import com.talexs.talexessential.utils.item.ItemBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaterialClass extends BaseClassifiesCreator {
    @Override
    public String getId() {
        return "st_material";
    }

    @Override
    public String getName() {
        return "材料学";
    }

    @Override
    public ItemStack getDisplayStack() {
        return new ItemBuilder(Material.IRON_INGOT).setLore("", "&8| &f材料学是一切学科的根本基础", "&8| &7使用合适的配比打造绝对强悍的材料", "").setName(MiniMessage.miniMessage().deserialize("<gradient:#4E5052:#262626>材料学")).toItemStack();
    }

    public MaterialClass() {

        addUnlockCondition(new RankCondition(5));

    }

}
