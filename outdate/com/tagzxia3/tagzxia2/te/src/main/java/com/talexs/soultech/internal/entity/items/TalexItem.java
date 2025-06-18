package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.soultech.internal.entity.items;

import com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.StNameSpace;
import com.talexs.soultech.internal.entity.classfies.Classifies;
import com.talexs.talexessential.utils.item.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TalexItem extends ItemStack {

    public enum TalexItemType {
        SOULTECH_ITEM, SOULTECH_BLOCK
    }

    /**
     * 物品 TC KEY
     * 含有此KEY的物品为自定义物品
     */
    public static NamespacedKey TC_KEY = new NamespacedKey(StNameSpace.PLUGIN_NAME, "tc_key");

    /**
     * 在进行检查时需要忽略哪些数据
     */
    private final Set<VerifyIgnoreTypes> ignoreTypesSet = new HashSet<>();

    protected ItemBuilder itemBuilder;

    /**
     * 自定义物品种类 默认是 SoulTechItem
     */
    @Getter
    protected String stType = TalexItemType.SOULTECH_ITEM.name();

    @Setter
    @Getter
    private Classifies ownCategoryObject;

    public TalexItem(ItemStack stack) {
        super(stack);

        this.itemBuilder = new ItemBuilder(stack);
    }

    public TalexItem(Material material) {
        this(new ItemStack(material));
    }

    public TalexItem(String soulTechItemID, SoulTechItem defaultValue) {
        super(new ItemStack(SoulTechItem.getOrDefault(soulTechItemID, defaultValue)));

        this.itemBuilder = new ItemBuilder(SoulTechItem.getOrDefault(soulTechItemID, defaultValue).getItemBuilder().toItemStack());
    }

    public TalexItem(ItemBuilder ib) {
        super(ib.toItemStack());

        this.itemBuilder = ib;
    }

    public static boolean checkItem(@NotNull ItemStack stack) {
        if ( stack.getItemMeta() == null ) return false;

        return stack.getItemMeta().getPersistentDataContainer().has(SoulTechItem.TC_KEY);

    }

    public static @Nullable String getItemCustomType(@NotNull ItemStack stack) {
        if ( !checkItem(stack) ) return null;

        return stack.getItemMeta().getPersistentDataContainer().get(SoulTechItem.TC_KEY, PersistentDataType.STRING);
    }

    public ItemBuilder getItemBuilder() {

        return this.itemBuilder.clone().addPRCData(TC_KEY, this.stType);

    }

    public TalexItem setType(String stType) {

        this.stType = stType;

        return this;

    }

    public TalexItem setType(TalexItemType type) {
        return this.setType(type.name());
    }

    public TalexItem addToPlayer(Player player) {
        return addToPlayer(player, false);
    }

    public TalexItem addToPlayer(Player player, boolean playerSymbol) {

        ItemStack stack = playerSymbol ? getItemBuilder()
                .addPRCData(StNameSpace.OWNER, player.getUniqueId().toString())
                .addLoreLine("")
                .addLoreLine("&8#" + player.getUniqueId())
                .toItemStack() : getItemBuilder().toItemStack();

        player.getInventory().addItem(stack);

        return this;

    }

    @Deprecated
    public TalexItem addIgnoreType(VerifyIgnoreTypes type) {

        this.ignoreTypesSet.add(type);

        return this;

    }

    public TalexItem addTag(NamespacedKey key, String value) {

        this.itemBuilder.addPRCData(key, value);

        return this;

    }

    @Deprecated
    public TalexItem delIgnoreType(VerifyIgnoreTypes type) {

        this.ignoreTypesSet.remove(type);

        return this;

    }

    @Deprecated
    public boolean verify(ItemStack stack, Set<VerifyIgnoreTypes> customTypes) {

        if ( stack == null || stack.getType() == Material.AIR ) {
            return false;
        }

        if ( stack.isSimilar(this.itemBuilder.toItemStack()) ) {
            return true;
        }

        ItemStack target = stack.clone();
        ItemStack tStack = this.itemBuilder.toItemStack().clone();

        if ( customTypes.contains(VerifyIgnoreTypes.SUFFIX_MATCHER) ) {
            String suffix = target.getType().name();
            String[] splits = suffix.split("_");
            suffix = splits[splits.length - 1];

            if ( !tStack.getType().name().contains(suffix) ) return false;

        } else if ( target.getType() != tStack.getType() ) return false;

        if ( customTypes.contains(VerifyIgnoreTypes.MINECRAFT_CHECKER) ) {

            return target.getType() == tStack.getType();

        }

        if ( customTypes.contains(VerifyIgnoreTypes.IgnoreAmount) ) {

            target.setAmount(1);
            tStack.setAmount(1);

        }

        if ( !customTypes.contains(VerifyIgnoreTypes.IgnoreDurability) && target.getDurability() != tStack.getDurability() ) return false;

        if ( !customTypes.contains(VerifyIgnoreTypes.IgnoreEnchants) && target.getEnchantments().size() != tStack.getEnchantments().size() ) {

//            System.out.println("验证 - Enchants大小不一致");

            return false;

        }

        if ( !customTypes.contains(VerifyIgnoreTypes.IgnoreEnchants) ) {

            for ( Map.Entry<Enchantment, Integer> entry : new HashSet<>(target.getEnchantments().entrySet()) ) {

                if ( tStack.getEnchantments().get(entry.getKey()) == null ) {

//                    System.out.println("Enchants 无: " + entry.getKey().getName());

                    return false;

                }

                int targetValue = tStack.getEnchantments().get(entry.getKey());

                if ( targetValue != entry.getValue() ) {

//                    System.out.println("Enchants等级错误: " + entry.getKey().getName() + "@" + entry.getValue() + " | " + tStack.getEnchantments().get(entry.getKey()));

                    return false;

                }

            }

        }

        if ( !customTypes.contains(VerifyIgnoreTypes.IgnoreItemMeta) && target.hasItemMeta() && tStack.hasItemMeta() ) {

            ItemMeta sMeta = target.getItemMeta();
            ItemMeta tMeta = tStack.getItemMeta();

            if ( !customTypes.contains(VerifyIgnoreTypes.IgnoreDisplayName) && sMeta.hasDisplayName() && tMeta.hasDisplayName() ) {

                if ( !sMeta.getDisplayName().equalsIgnoreCase(tMeta.getDisplayName()) ) {

//                    System.out.println("验证 - 名字不一致");

                    return false;

                }

            }

            if ( !customTypes.contains(VerifyIgnoreTypes.IgnoreItemFlags) ) {

                if ( target.getItemMeta().getItemFlags().size() != tStack.getItemMeta().getItemFlags().size() ) {

//                    System.out.println("验证 - ItemFlags大小不一致");

                    return false;

                }

                for ( ItemFlag flag : new HashSet<>(target.getItemMeta().getItemFlags()) ) {

                    if ( !tStack.getItemMeta().getItemFlags().contains(flag) ) {

//                        System.out.println("验证 - FlagError: " + flag.name() + " Don't have");

                        return false;

                    }

                }

            }
            ;

            if ( !customTypes.contains(VerifyIgnoreTypes.IgnoreLores) && sMeta.hasLore() && tMeta.hasLore() && sMeta.hasLore() ) {

                if ( tMeta.hasLore() && sMeta.getLore().size() != tMeta.getLore().size() ) {

//                    System.out.println("验证 - Lores大小不一致");

                    return false;

                }

                for ( int i = 0; i < sMeta.getLore().size(); ++i ) {

                    if ( !sMeta.getLore().get(i).equalsIgnoreCase(tMeta.getLore().get(i)) ) {

//                        System.out.println("lore内容不一致");
//
//                        System.out.println("This: " + sMeta.getLore().get(i));
//                        System.out.println("Target: " + tMeta.getLore().get(i));

                        return false;

                    }

                }

            }

            if ( customTypes.contains(VerifyIgnoreTypes.IgnoreUnbreakable) ) {
                return true;
            } else {
                return tMeta.isUnbreakable() == sMeta.isUnbreakable();
            }

        }

        return true;

    }

    @Deprecated
    public boolean verify(ItemStack stack) {

        return verify(stack, ignoreTypesSet);

//        if(stack == null || stack.getType() == Material.AIR) return false;
//
//        if(stack.isSimilar(this.itemBuilder.toItemStack())) return true;
//
//        ItemStack target = stack.clone();
//        ItemStack tStack = this.itemBuilder.toItemStack().clone();
//
//        if(ignoreTypesSet.contains(VerifyIgnoreTypes.MINECRAFT_CHECKER)) {
//
//            return target.getType() == tStack.getType();
//
//        }
//
//        if(ignoreTypesSet.contains(VerifyIgnoreTypes.IgnoreAmount)) {
//
//            target.setAmount(1);
//            tStack.setAmount(1);
//
//        }
//
//        if(target.getType() != tStack.getType()) {
//
//            System.out.println("验证 - Type不一致");
//
//            return false;
//
//        }
//
//        if(!ignoreTypesSet.contains(VerifyIgnoreTypes.IgnoreDurability) && target.getDurability() != tStack.getDurability()) {
//
//            System.out.println("验证 - Durability不一致");
//
//            return false;
//
//        }
//
//        if(!ignoreTypesSet.contains(VerifyIgnoreTypes.IgnoreEnchants) && target.getEnchantments().size() != tStack.getEnchantments().size()) {
//
//            System.out.println("验证 - Enchants大小不一致");
//
//            return false;
//
//        }
//
//        if(!ignoreTypesSet.contains(VerifyIgnoreTypes.IgnoreEnchants)) {
//
//            for(Map.Entry<Enchantment, Integer> entry : new HashSet<>(target.getEnchantments().entrySet())) {
//
//                if(tStack.getEnchantments().get(entry.getKey()) == null) {
//
//                    System.out.println("Enchants 无: " + entry.getKey().getName());
//
//                    return false;
//
//                }
//
//                int targetValue = tStack.getEnchantments().get(entry.getKey());
//
//                if(targetValue != entry.getValue()) {
//
//                    System.out.println("Enchants等级错误: " + entry.getKey().getName() + "@" + entry.getValue() + " | " + tStack.getEnchantments().get(entry.getKey()));
//
//                    return false;
//
//                }
//
//            }
//
//        }
//
//        if(!ignoreTypesSet.contains(VerifyIgnoreTypes.IgnoreItemMeta) && target.hasItemMeta() && tStack.hasItemMeta()) {
//
//            ItemMeta sMeta = target.getItemMeta();
//            ItemMeta tMeta = tStack.getItemMeta();
//
//            if(!ignoreTypesSet.contains(VerifyIgnoreTypes.IgnoreDisplayName) && sMeta.hasDisplayName() && tMeta.hasDisplayName()) {
//
//                if(!sMeta.getDisplayName().equalsIgnoreCase(tMeta.getDisplayName())) return false;
//
//            }
//
//            if(!ignoreTypesSet.contains(VerifyIgnoreTypes.IgnoreItemFlags)) {
//
////            System.out.println("验证 - ItemFlags不一致");
//
//                if(target.getItemMeta().getItemFlags().size() != tStack.getItemMeta().getItemFlags().size()) {
//
//                    System.out.println("验证 - ItemFlags大小不一致");
//
//                    return false;
//
//                }
//
//                for(ItemFlag flag : new HashSet<>(target.getItemMeta().getItemFlags())) {
//
//                    if(!tStack.getItemMeta().getItemFlags().contains(flag)) {
//
//                        System.out.println("验证 - FlagError: " + flag.name() + " Don't have");
//
//                        return false;
//
//                    }
//
//                }
//
//            };
//
//            if(!ignoreTypesSet.contains(VerifyIgnoreTypes.IgnoreLores) && this.getItemBuilder().toItemStack().getType() != Material.AIR) {
//
//                if(sMeta.getLore().size() != tMeta.getLore().size()) {
//
////                    System.out.println("验证 - Lores大小不一致");
//
//                    return false;
//
//                }
//
//                for(int i = 0;i < sMeta.getLore().size();++i) {
//
//                    if(!sMeta.getLore().get(i).equalsIgnoreCase(tMeta.getLore().get(i))) {
//
////                        System.out.println("lore内容不一致");
////
////                        System.out.println("This: " + sMeta.getLore().get(i));
////                        System.out.println("Target: " + tMeta.getLore().get(i));
//
//                        return false;
//
//                    }
//
//                }
//
//            }
//
//            if(ignoreTypesSet.contains(VerifyIgnoreTypes.IgnoreUnbreakable))
//                return true;
//            else return tMeta.isUnbreakable() && sMeta.isUnbreakable();
//
//        }
//
//        return true;

    }

    @Deprecated
    public enum VerifyIgnoreTypes {

        IgnoreDurability(),
        IgnoreAmount(),
        IgnoreItemFlags(),
        IgnoreEnchants(),
        IgnoreLores(),
        IgnoreItemMeta(),
        IgnoreDisplayName(),
        IgnoreUnbreakable(),
        SUFFIX_MATCHER(),
        MINECRAFT_CHECKER()

    }

}
