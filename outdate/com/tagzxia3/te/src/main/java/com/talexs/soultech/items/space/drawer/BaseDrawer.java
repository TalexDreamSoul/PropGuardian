package com.tagzxia3.te.src.main.java.com.talexs.soultech.items.space.drawer;

import com.tagzxia.te.src.main.java.com.talexs.soultech.items.space.drawer.BaseDrawerItem;
import com.talexs.soultech.internal.entity.items.IPersistItem;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.soultech.internal.StNameSpace;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.items.TalexItem;
import com.talexs.soultech.internal.RecipeObject;
import com.talexs.talexessential.utils.NBTsUtil;
import com.talexs.talexessential.utils.block.TalexBlock;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.ItemUtil;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class BaseDrawer extends BaseDrawerItem implements IPersistItem {

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class DrawerObject {
        private ItemStack stack;
        
        private int amount, maxAmount, tick = 0, range = 1;

        private Hologram hd;

        public boolean similar(ItemStack item) {
          if ( amount == 0 || stack == null || stack.getType() == Material.AIR ) return true;

          TalexItem ti = new TalexItem(stack).addIgnoreType(TalexItem.VerifyIgnoreTypes.IgnoreAmount);

          return ti.verify(item);
        }

        public ItemStack merge(ItemStack item) {
            if ( !similar(item) ) return item;

            int left = maxAmount - amount;
            if ( left < 1 ) return item;

            if ( stack == null || stack.getType() == Material.AIR ) {
                stack = item.clone();
                amount = item.getAmount();
                return null;
            }

            int amo = item.getAmount();
            if ( amo > left ) {
                item.setAmount(amo - left);
                amount = maxAmount;
                return item;
            } else {
                amount += amo;
                return null;
            }
        }
    }

    private final int maxAmount;

    protected int range;

    public static Map<String, DrawerObject> drawers = new HashMap<>();

    public BaseDrawer(@NotNull String id, int maxAmo) {
        super("drawer_" + id, new ItemBuilder(Material.END_PORTAL_FRAME)
                .setName("&b虚尽存储方块")
                .setLore(
                        "",
                        "&8| &7虚无终端的奥秘，你至今也无法理解",
                        "&8| &7但仍能使用它来存储物品",
                        "&8| &7存储的物品将会永久保存在虚空中",
                        "&8| &7它会自动向周围吸引相同物品",
                        "",
                        "&8| &7物品: &r无",
                        "&8| &7容量: &e0/" + maxAmo,
                        "",
                        "&8| &e左键取拿 | 右键存放",
                        "&8| &eSHIFT + 右键 拆卸",
                        ""
                ).addPRCData(StNameSpace.VANITY_AMO_MAX, String.valueOf(maxAmo))
                .toItemStack());

        this.maxAmount = maxAmo;

        new BukkitRunnable() {
            @Override
            public void run() {

                new HashMap<>(drawers).forEach((key, value) -> {
                    Location loc = NBTsUtil.String2Location(key);
                    if ( loc != null ) {
                        Block block = loc.getBlock();
                        if ( block.getType() == Material.END_PORTAL_FRAME ) {
                            processDrawer(loc, value);
                            return;
                        }
                    }

                    if (value.hd != null) {
                        value.hd.destroy();
                    }
                    drawers.remove(key);
                });

            }
        }.runTaskTimer(TalexEssential.getInstance(), 0, 10);
    }

    @Override
    public void onCrafted(Player player) {
        PlayerData pd = PlayerData.g(player);

        if ( !pd.getPlayerSoul().doMachineUnlock("VanityDrawer") ) {
            pd.getPlayerSoul().unlockMachine("VanityDrawer");
        }
    }

    private static void processDrawer(Location loc, DrawerObject drawer) {
        if ( drawer.hd != null ) {
            drawer.hd.destroy();
        }

        String itemName = drawer.stack == null ? "" : "#ICON: " + drawer.stack.getType().name() + ":" + drawer.getAmount();
        String name = ItemUtil.getName(drawer.stack, false);

        List<String> list = Arrays.asList(
                name,
                "§7" + drawer.getAmount() + "/" + drawer.getMaxAmount(),
                itemName
        );

        drawer.hd = DHAPI.createHologram(NBTsUtil.getRandomStr(12), loc.add(0.5, 1.75, 0.5), list);

//        ItemStack stack = drawer.getStack();
        int amount = drawer.getAmount();
        if ( amount < 1 ) drawer.setStack(null);

        int maxAmount = drawer.getMaxAmount();

        if (amount >= maxAmount) return;

        Random random = new Random();
        loc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, loc.clone().add(-.5 + random.nextDouble(), -.75, -.5 + random.nextDouble()), 5, 0, 0, 0, 0);

        int tick = drawer.tick;
        if ( tick < 6 ) {
            drawer.tick++;
            return;
        }

        drawer.tick = 0;

        loc.getNearbyEntities(drawer.range, drawer.range, drawer.range).forEach(entity -> {
            if (!(entity instanceof Item)) return;

            Item item = (Item) entity;

            ItemStack itemStack = item.getItemStack();

            if ( drawer.similar(itemStack) ) {
                ItemStack left = drawer.merge(itemStack);

                if ( left == null ) {
                    item.remove();
                } else {
                    item.setItemStack(left);
                }
            }
        });
    }

    public abstract RecipeObject getRecipe();

    public static boolean isDrawer(Block block) {
        return getDrawer(block) != null;
    }

    public static @Nullable DrawerObject getDrawer(Block block) {
        if ( block == null ) return null;

        Location location = block.getLocation();

        String locStr = NBTsUtil.Location2String(location);

        return drawers.get(locStr);
    }

    @Override
    public boolean onPlaceItem(PlayerData playerData, BlockPlaceEvent event) {
        Block blockPlaced = event.getBlockPlaced();
        Location location = blockPlaced.getLocation();

        String locStr = super.getLocationString(location);

        ItemStack itemInHand = event.getItemInHand();

        ItemBuilder ib = new ItemBuilder(itemInHand);

        DrawerObject drawerObject = new DrawerObject()
                .setRange(range)
                .setMaxAmount(maxAmount);

        if ( ib.hasPRCData(StNameSpace.VANITY_AMO) ) {
            int amo = Integer.parseInt(Objects.requireNonNull(ib.getPRCData(StNameSpace.VANITY_AMO)));
            drawerObject.setAmount(amo);
        }

        if ( ib.hasPRCData(StNameSpace.VANITY_ITEM) ) {
            String itemData = ib.getPRCData(StNameSpace.VANITY_ITEM);
            if ( itemData != null && !itemData.isEmpty() ) {
                drawerObject.setStack(ItemUtil.str2Item(itemData));
            }
        }

        drawers.put(locStr, drawerObject);

        return super.onPlaceItem(playerData, event);
    }

    @Override
    public boolean onItemBlockBreak(PlayerData playerData, TalexBlock block, BlockBreakEvent event) {
        Location location = block.getLoc();

        String locStr = super.getLocationString(location);

        drawers.remove(locStr);

        return super.onItemBlockBreak(playerData, block, event);
    }

    public YamlConfiguration onItemBlocksSave() {
        YamlConfiguration yaml = new YamlConfiguration();

        drawers.forEach((key, value) -> {
            if ( value.hd != null ) value.hd.destroy();

            String path = "Drawers." + getRandomStr(16);
            yaml.set(path + ".item", value.getStack() != null ? ItemUtil.item2Str(value.getStack()) : "");
            yaml.set(path + ".amo", value.getAmount());
            yaml.set(path + ".maxAmo", value.getAmount());
            yaml.set(path + ".key", key);
        });

        return yaml;
    }

    public void onItemBlocksLoad(YamlConfiguration yaml) {
        if ( !yaml.contains("Drawers") ) return;

        yaml.getConfigurationSection("Drawers").getKeys(false).forEach(key -> {
            String path = "Drawers." + key;
            String itemStr = yaml.getString(path + ".item");
            ItemStack stack = !itemStr.isEmpty() ? ItemUtil.str2Item(itemStr) : null;
            int amo = yaml.getInt(path + ".amo");
            int maxAmo = yaml.getInt(path + ".maxAmo");

            drawers.put(yaml.getString(path + ".key"), new DrawerObject().setStack(stack).setAmount(amo).setMaxAmount(maxAmo));
        });
    }
}
