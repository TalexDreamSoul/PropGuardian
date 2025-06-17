package com.tagzxia3.tagzxia2.tagzxia1.src.main.java.alarm.star.alarmstarsystem.utils.item;

import com.comphenix.protocol.utility.StreamSerializer;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import java.beans.ConstructorProperties;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public final class ItemUtil {

    private ItemUtil() {

        throw new RuntimeException("Cannot instantiate a utility class.");
    }

    public static String formatMaterial(Material material) {

        String name = material.toString();
        name = name.replace('_', ' ');
        String result = "" + name.charAt(0);

        for ( int i = 1; i < name.length(); ++i ) {
            if ( name.charAt(i - 1) == ' ' ) {
                result = result + name.charAt(i);
            } else {
                result = result + Character.toLowerCase(name.charAt(i));
            }
        }

        return result;
    }

    public static ItemStack enchantItem(ItemStack itemStack, ItemEnchant... enchantments) {

        Arrays.asList(enchantments).forEach((enchantment) -> itemStack.addUnsafeEnchantment(enchantment.enchantment, enchantment.level));
        return itemStack;
    }

    public static ItemStack addPotionEffect(ItemStack itemStack, PotionEffect... potionEffect) {

        ItemStack result = itemStack;
        for ( PotionEffect pe : potionEffect ) {
            PotionMeta meta = (PotionMeta) result.getItemMeta();
            meta.addCustomEffect(pe, true);
            result.setItemMeta(meta);
        }
        return result;
    }

    public static ItemStack createItem(Material material, String name) {

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material material, String name, int amount) {

        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material material, String name, int amount, short damage) {

        ItemStack item = new ItemStack(material, amount, damage);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack hideEnchants(ItemStack item) {

        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE });
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack setUnbreakable(ItemStack item) {

        ItemMeta meta = item.getItemMeta();
        meta.setUnbreakable(true);
        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_UNBREAKABLE });
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack renameItem(ItemStack item, String name) {

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack reloreItem(ItemStack item, String... lores) {

        return reloreItem(ReloreType.OVERWRITE, item, lores);
    }

    public static ItemStack reloreItem(ReloreType type, ItemStack item, String... lores) {

        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if ( lore == null ) {
            lore = new LinkedList();
        }

        switch ( type ) {
            case APPEND:
                ( (List) lore ).addAll(Arrays.asList(lores));
                meta.setLore((List) lore);
                break;
            case PREPEND:
                List<String> nLore = new LinkedList(Arrays.asList(lores));
                nLore.addAll((Collection) lore);
                meta.setLore(nLore);
                break;
            case OVERWRITE:
                meta.setLore(Arrays.asList(lores));
        }

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack addItemFlag(ItemStack item, ItemFlag flag) {

        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(new ItemFlag[] { flag });
        item.setItemMeta(meta);
        return item;
    }

    public static enum ReloreType {
        OVERWRITE,
        PREPEND,
        APPEND;
    }

    public static class ItemEnchant {

        private final Enchantment enchantment;
        private final int level;

        @ConstructorProperties( { "enchantment", "level" } )
        public ItemEnchant(Enchantment enchantment, int level) {

            this.enchantment = enchantment;
            this.level = level;
        }

    }


    @SneakyThrows
    public static String item2Str(ItemStack stack) {
        return new StreamSerializer().serializeItemStack(stack);
    }

    @SneakyThrows
    public static ItemStack str2Item(String str) {
        return new StreamSerializer().deserializeItemStack(str);
    }


}
