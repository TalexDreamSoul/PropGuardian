package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.utils.item;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.map.MapView;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Easily create itemstacks, without messing your hands.
 * <i>Note that if you do use this in one of your projects, leave this notice.</i>
 * <i>Please do credit me if you do use this in one of your projects.</i>
 *
 * @author NonameSL
 */
@SuppressWarnings( "ALL" )
public class ItemBuilder {

    private ItemStack is;

    /**
     * Create a new ItemBuilder from scratch.
     *
     * @param m The material to create the ItemBuilder with.
     */
    public ItemBuilder(Material m) {

        this(m, 1);
    }

    /**
     * Create a new ItemBuilder over an existing itemstack.
     *
     * @param is The itemstack to create the ItemBuilder over.
     */
    public ItemBuilder(ItemStack is) {

        if ( is == null ) {

            throw new NullPointerException();

        }

        this.is = is;

    }

    /**
     * Create a new ItemBuilder from scratch.
     *
     * @param m      The material of the item.
     * @param amount The amount of the item.
     */
    public ItemBuilder(Material m, int amount) {

        is = new ItemStack(m, amount);
    }

    /**
     * Create a new ItemBuilder from scratch.
     *
     * @param m          The material of the item.
     * @param amount     The amount of the item.
     * @param durability The durability of the item.
     */
    public ItemBuilder(Material m, int amount, byte durability) {

        is = new ItemStack(m, amount, durability);
    }

    /**
     * Clone the ItemBuilder into a new one.
     *
     * @return The cloned instance.
     */
    public ItemBuilder clone() {

        return new ItemBuilder(is.clone());
    }

    /**
     * Change the durability of the item.
     *
     * @param dur The durability to set it to.
     */
    public ItemBuilder setDurability(short dur) {

        is.setDurability(dur);
        return this;
    }

    /**
     * Set the displayname of the item.
     *
     * @param name The name to change it to.
     */
    public ItemBuilder setName(String name) {

        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setName(Component component) {

        ItemMeta im = is.getItemMeta();
        im.displayName(component);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Set the amount of the item.
     *
     * @param amount The amount of the new item
     */
    public ItemBuilder setAmount(int amount) {

        this.is.setAmount(amount);

        return this;

    }

    /**
     * Add Item Flag.
     *
     * @param flag The name to change it to.
     */
    public ItemBuilder addFlag(ItemFlag... flag) {

        ItemMeta im = is.getItemMeta();
        im.addItemFlags(flag);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Add an unsafe enchantment.
     *
     * @param ench  The enchantment to add.
     * @param level The level to put the enchant on.
     */
    public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level) {

        is.addUnsafeEnchantment(ench, level);
        return this;
    }

    /**
     *
     */
    public ItemBuilder clearLores() {

        ItemMeta im = is.getItemMeta();
        im.setLore(new ArrayList<String>());
        is.setItemMeta(im);
        return this;
    }

    /**
     * Remove a certain enchant from the item.
     *
     * @param ench The enchantment to remove
     */
    public ItemBuilder removeEnchantment(Enchantment ench) {

        is.removeEnchantment(ench);
        return this;
    }

    /**
     * Remove all enchants from the item.
     */
    public ItemBuilder removeEnchantments() {

        ItemMeta im = is.getItemMeta();
        im.getEnchants().forEach((enchantment, integer) -> im.removeEnchant(enchantment));
        return this;
    }

    /**
     * Set the skull owner for the item. Works on skulls only.
     *
     * @param owner The name of the skull's owner.
     */
    public ItemBuilder setSkullOwner(String owner) {

        try {
            SkullMeta im = (SkullMeta) is.getItemMeta();
            im.setOwner(owner);
            is.setItemMeta(im);
        } catch ( ClassCastException expected ) {
        }
        return this;
    }

    /**
     * Add an enchant to the item.
     *
     * @param ench  The enchant to add
     * @param level The level
     */
    public ItemBuilder addEnchant(Enchantment ench, int level) {

        ItemMeta im = is.getItemMeta();
        im.addEnchant(ench, level, true);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Add multiple enchants at once.
     *
     * @param enchantments The enchants to add.
     */
    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {

        is.addEnchantments(enchantments);
        return this;
    }

    /**
     * Sets infinity durability on the item by setting the durability to Short.MAX_VALUE.
     */
    public ItemBuilder setInfinityDurability() {

        is.setDurability(Short.MAX_VALUE);
        return this;
    }

    public ItemBuilder setUnbreakable() {

        ItemMeta im = is.getItemMeta();
        im.setUnbreakable(true);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setType(Material material) {

        is.setType(material);
        return this;
    }

    /**
     * Re-sets the lore.
     *
     * @param lore The lore to set it to.
     */
    public ItemBuilder setLore(String... lore) {

        ItemMeta im = is.getItemMeta();

        List<String> list = new ArrayList<>();

        Arrays.asList(lore).forEach(str -> list.add(ChatColor.translateAlternateColorCodes('&', str)));

        im.setLore(list);

        is.setItemMeta(im);

        return this;

    }

    /**
     * Re-sets the lore.
     *
     * @param lore The lore to set it to.
     */
    public ItemBuilder setLore(Component... lore) {

        ItemMeta im = is.getItemMeta();

        im.lore(Arrays.asList(lore));

        is.setItemMeta(im);

        return this;

    }

    /**
     * Re-sets the lore.
     *
     * @param lore The lore to set it to.
     */
    public ItemBuilder setLore(List<String> lore) {

        int i = 0;

        for ( String str : lore ) {

            lore.set(i, ChatColor.translateAlternateColorCodes('&', str));
            i++;

        }

        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
        return this;

    }

    /**
     * Remove a lore line.
     */
    public ItemBuilder removeLoreLine(String line) {

        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if ( !lore.contains(line) ) {
            return this;
        }
        lore.remove(line);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Remove a lore line.
     *
     * @param index The index of the lore line to remove.
     */
    public ItemBuilder removeLoreLine(int index) {

        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if ( index < 0 || index > lore.size() ) {
            return this;
        }
        lore.remove(index);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Add a lore line.
     *
     * @param line The lore line to add.
     */
    public ItemBuilder addLoreLine(String line) {

        ItemMeta im = is.getItemMeta();

//        if(im == null) {
//
//            this.is = TalexItem.reSerialize(is);
//
//            ItemMeta meta = is.getItemMeta();
//
//        }

        List<String> lore = new ArrayList<>();

        if ( im.hasLore() ) {
            lore = new ArrayList<>(im.getLore());
        }

        lore.add(ChatColor.translateAlternateColorCodes('&', line));
        im.setLore(lore);
        is.setItemMeta(im);
        return this;

    }

    public ItemBuilder setLoreLine(String line, int pos) {
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        lore.set(pos, ChatColor.translateAlternateColorCodes('&', line));
        im.setLore(lore);
        is.setItemMeta(im);
        return this;

    }

    public ItemBuilder setLoreLine(Component line, int pos) {
        ItemMeta im = is.getItemMeta();
        List<Component> lore = new ArrayList<>(im.lore());
        lore.set(pos, line);
        im.lore(lore);
        is.setItemMeta(im);
        return this;
    }


    /**
     * Add a lore line.
     *
     * @param line The lore line to add.
     * @param pos  The index of where to put it.
     */
    public ItemBuilder addLoreLine(String line, int pos) {

        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        lore.add(pos, ChatColor.translateAlternateColorCodes('&', line));
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Sets the dye color on an item.
     * <b>* Notice that this doesn't check for item type, sets the literal data of the dyecolor as durability.</b>
     *
     * @param color The color to put.
     */
    public ItemBuilder setDyeColor(DyeColor color) {

        this.is.setDurability(color.getDyeData());
        return this;
    }

    /**
     * Sets the dye color of a wool item. Works only on wool.
     *
     * @param color The DyeColor to set the wool item to.
     *
     * @see ItemBuilder @setDyeColor(DyeColor)
     * @deprecated As of version 1.2 changed to setDyeColor.
     */
    @Deprecated
    public ItemBuilder setWoolColor(DyeColor color) {

        if ( !is.getType().name().contains("WOOL") ) {
            return this;
        }
        this.is.setDurability(color.getWoolData());
        return this;
    }

    /**
     * Sets the armor color of a leather armor piece. Works only on leather armor pieces.
     *
     * @param color The color to set it to.
     */
    public ItemBuilder setLeatherArmorColor(Color color) {

        try {
            LeatherArmorMeta im = (LeatherArmorMeta) is.getItemMeta();
            im.setColor(color);
            is.setItemMeta(im);
        } catch ( ClassCastException expected ) {
        }
        return this;
    }

    /**
     * 设置ID
     *
     * @param mapView 要设置的id
     */
    public ItemBuilder setMapView(MapView mapView) {

        setDurability((short) mapView.getId());
        return this;
    }
//    /**
//     *
//     *
//     *
//     */
//    public ItemBuilder clearCustomEffect(){
//        PotionMeta pm = (PotionMeta)is.getItemMeta();
//        pm.clearCustomEffects();
//        pm.setMainEffect(null);
//        is.setItemMeta(pm);
//        return this;
//    }

    /**
     * Retrieves the itemstack from the ItemBuilder.
     *
     * @return The itemstack created/modified by the ItemBuilder instance.
     */
    public ItemStack toItemStack() {

        return is;
    }

    public ItemBuilder isTrueAccessEnchantAndHide(boolean condition, Enchantment enchantment, int level) {

        if ( condition ) {

            addEnchant(enchantment, level);

        }

        return this.addFlag(ItemFlag.HIDE_ENCHANTS);

    }

    public ItemBuilder isTrueAccessEnchant(boolean condition, Enchantment enchantment, int level) {

        if ( condition ) {

            addEnchant(enchantment, level);

        }

        return this;

    }


    public ItemBuilder isTrueSetDurability(boolean condition, short dur) {

        if ( condition ) {
            is.setDurability(dur);
        }

        return this;

    }

    public String getDisplayNameOrDefaultName() {

        if ( is.hasItemMeta() && is.getItemMeta().hasDisplayName() ) {
            return is.getItemMeta().getDisplayName();
        }

        return is.getType().name();

    }

    public @Nullable String getPRCData(NamespacedKey key) {

        ItemMeta im = is.getItemMeta();
        if ( im == null ) return null;

        return im.getPersistentDataContainer().get(key, PersistentDataType.STRING);

    }

    public ItemBuilder addPRCData(NamespacedKey key, String value) {

        ItemMeta im = is.getItemMeta();

        im.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);

        is.setItemMeta(im);

        return this;

    }

    public boolean hasPRCData(NamespacedKey key) {
        ItemMeta im = is.getItemMeta();
        if ( im == null ) return false;

        return im.getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }

    public ItemBuilder delPRCData(NamespacedKey key) {

        ItemMeta im = is.getItemMeta();

        im.getPersistentDataContainer().remove(key);

        is.setItemMeta(im);

        return this;

    }

    public ItemBuilder addPRCData(NamespacedKey key, int value) {

        ItemMeta im = is.getItemMeta();

        im.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);

        is.setItemMeta(im);

        return this;

    }

    public ItemBuilder addPRCData(NamespacedKey key, double value) {

        ItemMeta im = is.getItemMeta();

        im.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);

        is.setItemMeta(im);

        return this;

    }

    public ItemBuilder addPRCData(NamespacedKey key, float value) {

        ItemMeta im = is.getItemMeta();

        im.getPersistentDataContainer().set(key, PersistentDataType.FLOAT, value);

        is.setItemMeta(im);

        return this;

    }

    public ItemBuilder addPRCData(NamespacedKey key, long value) {

        ItemMeta im = is.getItemMeta();

        im.getPersistentDataContainer().set(key, PersistentDataType.LONG, value);

        is.setItemMeta(im);

        return this;

    }

    public ItemBuilder addPRCData(NamespacedKey key, byte value) {

        ItemMeta im = is.getItemMeta();

        im.getPersistentDataContainer().set(key, PersistentDataType.BYTE, value);

        is.setItemMeta(im);

        return this;

    }

    public ItemBuilder addPRCData(NamespacedKey key, short value) {

        ItemMeta im = is.getItemMeta();

        im.getPersistentDataContainer().set(key, PersistentDataType.SHORT, value);

        is.setItemMeta(im);

        return this;

    }

    public ItemBuilder addPRCData(NamespacedKey key, boolean value) {

        ItemMeta im = is.getItemMeta();

        im.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) ( value ? 1 : 0 ));

        is.setItemMeta(im);

        return this;

    }

    public ItemBuilder addPRCData(NamespacedKey key, byte[] value) {

        ItemMeta im = is.getItemMeta();

        im.getPersistentDataContainer().set(key, PersistentDataType.BYTE_ARRAY, value);

        is.setItemMeta(im);

        return this;

    }

    public ItemBuilder addPRCData(NamespacedKey key, int[] value) {

        ItemMeta im = is.getItemMeta();

        im.getPersistentDataContainer().set(key, PersistentDataType.INTEGER_ARRAY, value);

        is.setItemMeta(im);

        return this;

    }

    public ItemBuilder addPRCData(NamespacedKey key, long[] value) {

        ItemMeta im = is.getItemMeta();

        im.getPersistentDataContainer().set(key, PersistentDataType.LONG_ARRAY, value);

        is.setItemMeta(im);

        return this;

    }

    public ItemBuilder addAttribute(Attribute attribute, AttributeModifier modifier) {
        ItemMeta im = is.getItemMeta();
        im.addAttributeModifier(attribute, modifier);
        is.setItemMeta(im);
        return this;
    }

    public ItemBuilder attributeSpeedScale(double scale, EquipmentSlot slot) {
        ItemMeta im = is.getItemMeta();
        im.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(), "generic.movementSpeed", scale, AttributeModifier.Operation.MULTIPLY_SCALAR_1, slot));
        is.setItemMeta(im);
        return this;
    }

}
