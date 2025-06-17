package com.tagzxia3.te.src.main.java.com.talexs.soultech.internal.entity.items;

import com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.items.IPersistItem;
import com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.items.TalexItem;
import com.talexs.soultech.addon.AddonHolder;
import com.talexs.soultech.internal.entity.tech_object.TechObjectAddon;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.soultech.internal.StNameSpace;
import com.talexs.soultech.internal.RecipeObject;
import com.talexs.talexessential.utils.NBTsUtil;
import com.talexs.talexessential.utils.block.TalexBlock;
import com.talexs.talexessential.utils.item.ItemBuilder;
import lombok.Getter;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author TalexDreamSoul
 */
public abstract class SoulTechItem extends com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.items.TalexItem {

    /**
     * 自定义灵魂科技物品
     * 此项用于判断物品的ID
     */
    public static NamespacedKey TC_ITEM_ID = new NamespacedKey(StNameSpace.PLUGIN_NAME, "item_id");

    public static HeadDatabaseAPI api() {
        return TalexEssential.getInstance().getApi();
    }

    @Getter
    private static HashMap<String, SoulTechItem> items = new HashMap<>(256);

    @Getter
    private final String ID;

    @Getter
    private RecipeObject recipe;

    public SoulTechItem(@NotNull String ID, @NotNull ItemStack stack) {

        super(stack);

        super.addTag(SoulTechItem.TC_KEY, ID);

        this.ID = ID;

        items.put("sti_" + ID, this);

        _internalInit();
    }

    private void _internalInit() {
        // if this class implements IPersistItem
        if (this instanceof IPersistItem ipi) {
            ipi.onItemBlocksLoad(AddonHolder.getINSTANCE().get(TechObjectAddon.class).getPersistItemSavedData(ipi));
        }
    }

    @Deprecated
    public static boolean isSoulTechItem(@NotNull ItemStack stack) {
        String type = com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.items.TalexItem.getItemCustomType(stack);
        if ( type != null ) {
            return Arrays.stream(TalexItemType.values()).anyMatch(t -> t.name().equals(type));
        }

        return false;
    }

    public static boolean hasSoulTechID(@NotNull ItemStack stack) {
        if ( stack.getItemMeta() == null ) return false;

        return stack.getItemMeta().getPersistentDataContainer().has(TC_ITEM_ID, PersistentDataType.STRING);
    }

    public static @Nullable SoulTechItem getItem(@NotNull ItemStack stack) {

        if ( hasSoulTechID(stack) ) {

            return get(stack.getItemMeta().getPersistentDataContainer().get(TC_ITEM_ID, PersistentDataType.STRING));

        }

        return null;

    }

    public static SoulTechItem getWithoutAddon(String ID) {

        return items.get(ID);

    }

    public static SoulTechItem get(String ID) {

        return items.get("sti_" + ID);

    }
    
    public static @Nullable String getOwnerUUID(ItemStack stack) {
        return new ItemBuilder(stack).getPRCData(StNameSpace.OWNER);
    }

    public static SoulTechItem getOrDefault(String ID, SoulTechItem sti) {

        SoulTechItem item = get(ID);

        return item == null ? sti : item;

    }

    public ItemStack build() {
        return getItemBuilder().toItemStack();
    }

    public boolean checkID(@Nullable ItemStack stack) {

        if ( stack == null || stack.getItemMeta() == null ) {
            return false;
        }

        // 先判断是不是 TalexItem
        if ( !TalexItem.checkItem(stack) ) {
            return false;
        }

        PersistentDataContainer pdc = stack.getItemMeta().getPersistentDataContainer();

        return pdc.has(TC_ITEM_ID, PersistentDataType.STRING) && Objects.equals(pdc.get(TC_ITEM_ID, PersistentDataType.STRING), this.ID);

    }

    public SoulTechItem setRecipe(RecipeObject recipe) {

        this.recipe = recipe;

        return this;

    }

    public void onDamaged(PlayerData playerData, EntityDamageEvent event) {}

    public void onEntityDamage(PlayerData playerData, EntityDamageByEntityEvent event) {}

    public void onBucketFull(PlayerData playerData, PlayerBucketFillEvent event) {}

    public void onSneak(PlayerData playerData, PlayerToggleSneakEvent event) {}

    public void onInteract(PlayerData playerData, PlayerInteractEvent event) {}

    public void onInteractEntity(PlayerData playerData, PlayerInteractEntityEvent event) {}

    /**
     * 设置EventCancel 代表方块不破坏 - 如果返回真将会把这个物品从 {@link com.talexs.soultech.manager BlockAddon}
     * 中移除!
     *
     * @param playerData: 玩家数据
     * @param event:      事件传递
     *
     * @return 是否从BlockManager中移除
     */
    public boolean useItemBreakBlock(PlayerData playerData, BlockBreakEvent event) {return true;}

    public void throwItem(PlayerData playerData, PlayerDropItemEvent event) {}

    public boolean onPlaceItem(PlayerData playerData, BlockPlaceEvent event) {return false;}

    public void onCrafted(Player player) {}

    public void onItemHeld(PlayerData playerData, PlayerItemHeldEvent event) {}

    @Override
    public ItemBuilder getItemBuilder() {
        return super.getItemBuilder().addPRCData(TC_ITEM_ID, this.ID);
    }

    /**
     * 当放置的方块被破坏时
     *
     * @param playerData 玩家数据
     * @param block      当前的方块实例
     * @param event      事件传递
     *
     * @return 返回真则掉落物品 - 可返回假自定义
     */
    public boolean onItemBlockBreak(PlayerData playerData, TalexBlock block, BlockBreakEvent event) {return true;}

    /**
     * 是否可以当成原版物品使用 # 即是否允许放到工作台内.
     *
     * @return 是否允许
     */
    public boolean canUseAsOrigin() {return false;}

    public Enchantment matchEnchantment(String str) {

        return NBTsUtil.matchEnchantment(str);

    }

    public boolean isMinecraftOriginSimilar(ItemStack obj) {

        return NBTsUtil.isMinecraftOriginSimilar(this.itemBuilder.toItemStack(), obj);

    }

    public boolean isSimilarity(ItemStack obj) {

        return NBTsUtil.isSimilar(this.itemBuilder.toItemStack(), obj);

    }

//    public boolean isType(String type, String compare) {
//
//        return NBTsUtil.stackIsType(this.itemBuilder.toItemStack(), type, compare);
//
//    }

    public Material matchMaterial(String material) {

        return NBTsUtil.matchMaterial(material);

    }

    /**
     * 获取随机字符串，由数字、大小写字母组成
     *
     * @param bytes：生成的字符串的位数
     *
     * @return String: 返回生成的字符串
     *
     * @author TalexDreamSoul
     */
    public String getRandomStr(int bytes) {

        return NBTsUtil.getRandomStr(bytes);

    }

    public Location getLocation(String loc) {

        return NBTsUtil.getLocation(loc);

    }

    public String getLocationString(Location loc) {
        return NBTsUtil.Location2String(loc);
    }

}
