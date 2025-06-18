package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.soultech.classifies;

import com.talexs.soultech.internal.entity.classfies.BaseClassifiesCreator;
import com.talexs.soultech.internal.entity.unlock.RankCondition;
import com.talexs.talexessential.utils.item.ItemBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SoulClass extends BaseClassifiesCreator {
    @Override
    public String getId() {
        return "st_soul";
    }

    @Override
    public String getName() {
        return "灵魂学";
    }

    @Override
    public ItemStack getDisplayStack() {
        return new ItemBuilder(Material.GHAST_TEAR).setLore("", "&8| &f灵魂，生命深邃的神秘本质。", "&8| &7夜深思灵魂，修者探超凡奥秘。", "&8| &7晨曦映心灵，坚定寻内在星辰。", "")
                .setName(MiniMessage.miniMessage().deserialize("<gradient:#9F78BA:#C61FCD>灵魂学")).toItemStack();
    }

    public SoulClass() {

        addUnlockCondition(new RankCondition(6));

    }

    //        CategoryObject light = new CategoryObject(8, "st_light", new ItemBuilder(Material.GLOWSTONE_DUST).setLore("", "§8> §f光明是我们的信仰", "").setName(MiniMessage.miniMessage().deserialize("<gradient:#F2F2F2:#E6E6E6>光明学")).toItemStack());
    //        CategoryObject dark = new CategoryObject(9, "st_dark", new ItemBuilder(Material.COAL).setLore("", "§8> §f黑暗是我们的归宿", "").setName(MiniMessage.miniMessage().deserialize("<gradient:#1E1F22:#2B2D30>黑暗学")).toItemStack());
    //        CategoryObject life = new CategoryObject(10, "st_life", new ItemBuilder(Material.GRASS).setLore("", "§8> §f生命是我们的意义", "").setName(MiniMessage.miniMessage().deserialize("<gradient:#4E5052:#262626>生命学")).toItemStack());
    //        CategoryObject death = new CategoryObject(11, "st_death", new ItemBuilder(Material.BONE).setLore("", "§8> §f死亡是我们的终结", "").setName(MiniMessage.miniMessage().deserialize("<gradient:#6580BA:#2A3959>死亡学")).toItemStack());

    //        CategoryObject time = new CategoryObject(13, "st_time", new ItemBuilder(Material.CLOCK).setLore("", "§8> §f时间是我们的归宿", "").setName(MiniMessage.miniMessage().deserialize("<gradient:#C3D7F1:#FF99A4>时间学")).toItemStack());
    //
    //        CategoryObject witch = new CategoryObject(14, "st_witch", new ItemBuilder(Material.POTION).setLore("", "§8| §5巫术&7, 上古时代的语言", "").setName(MiniMessage.miniMessage().deserialize("<gradient:#50295D:#906C97>巫术学")).toItemStack());

    //        CategoryObject enchant = new CategoryObject(16, "st_enchant", new ItemBuilder(Material.ENCHANTING_TABLE).setLore("", "&8| &5*$(#&*(&%*(@#&%*(", "").setName(MiniMessage.miniMessage().deserialize("<gradient:#A5B4BA:#454547>附魔学")).toItemStack());
    //        CategoryObject curse = new CategoryObject(17, "st_curse", new ItemBuilder(Material.WITHER_SKELETON_SKULL).setLore("", "&8| &5诅咒&7, 来临之时", "").setName(MiniMessage.miniMessage().deserialize("<gradient:#F2F2F2:#E6E6E6>诅咒学")).toItemStack());
    //        CategoryObject summon = new CategoryObject(18, "st_summon", new ItemBuilder(Material.SKELETON_SKULL).setLore("", "§8| §a召唤&7, 传奇永恒不朽", "").setName(MiniMessage.miniMessage().deserialize("<gradient:#1E1F22:#2B2D30>召唤学")).toItemStack());
    //        CategoryObject element = new CategoryObject(18, "st_element", new ItemBuilder(Material.HEART_OF_THE_SEA).setLore("", "&8| &a元素&7, 时代之泪", "").setName(MiniMessage.miniMessage().deserialize("<gradient:#5E435D:#C3E88D>元素学")).toItemStack());


}
