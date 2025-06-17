package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.resource;

import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.utils.NBTsUtil;
import com.talexs.talexessential.utils.item.ItemUtil;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
public class PlayerRes {

    private final PlayerData pd;

    private Map<Integer, ResSlot> slotMapper = new HashMap<>();

    private ResCustomSlot customSlot = new ResCustomSlot(26);

    public static String PATH_PREFIX = "Resource";

    public static List<Material> PASS_MATERIALS = Arrays.asList(
            Material.COAL,
            Material.CHARCOAL,
            Material.RAW_IRON,
            Material.RAW_COPPER,
            Material.RAW_GOLD,
            Material.EMERALD,
            Material.LAPIS_LAZULI,
            Material.DIAMOND,
            Material.AMETHYST_SHARD,
            Material.IRON_NUGGET,
            Material.GOLD_NUGGET,

            Material.IRON_INGOT,
            Material.COPPER_INGOT,
            Material.GOLD_INGOT,
            Material.FLINT,
            Material.SNOWBALL,
            Material.INK_SAC,
            Material.GLOW_INK_SAC,
            Material.CLAY_BALL,
            Material.REDSTONE,

            Material.ANCIENT_DEBRIS
    );

    public Location saveLoc;

    private int amo = 0, accFailed = 0;

    public void addAmo() {
        this.amo += 1;
    }

    public void addFailed() {
        this.accFailed += 1;
    }

    public PlayerRes(PlayerData pd) {
        this.pd = pd;

        slots.forEach(slot -> {
            String path = PATH_PREFIX + ".Storage.S" + slot;
            ResSlot resSlot = new ResSlot();

            resSlot.slot = slot;

            if ( pd.getInfo().contains(path) ) {
                ItemStack stack = ItemUtil.str2Item(pd.getInfo().getString(path));

                resSlot.stack = stack;
            }

            slotMapper.put(slot, resSlot);
        });

        this.amo = pd.getInfo().getInt(PATH_PREFIX + ".Amo");
        this.accFailed = pd.getInfo().getInt(PATH_PREFIX + ".AccFailed");

        if ( pd.getInfo().contains(PATH_PREFIX + ".SaveLoc") ) {
            this.saveLoc = NBTsUtil.getLocation(Objects.requireNonNull(pd.getInfo().getString(PATH_PREFIX + ".SaveLoc")));
        }

    }

    public void save() {
        YamlConfiguration yaml = pd.getInfo();

        yaml.set(PATH_PREFIX + ".Amo", amo);
        yaml.set(PATH_PREFIX + ".AccFailed", accFailed);

        if ( saveLoc != null ) {
            yaml.set(PATH_PREFIX + ".SaveLoc", NBTsUtil.Location2String(saveLoc));
        } else {
            yaml.set(PATH_PREFIX + ".SaveLoc", null);
        }

        slotMapper.values().forEach(resSlot -> {
            String path = PATH_PREFIX + ".Storage.S" + resSlot.slot;
            if ( resSlot.stack == null ) {
                yaml.set(path, null);
            } else {
                yaml.set(path, ItemUtil.item2Str(resSlot.stack));
            }
        });
    }

    public void clear() {
        slotMapper.values().forEach(resSlot -> {
            resSlot.stack = null;
        });
    }

    public ItemStack mergeItem(ItemStack stack) throws RuntimeException {
        ItemStack s = stack.clone();

        for ( ResSlot resSlot : slotMapper.values() ) {

            int r = resSlot.couldEmerge(s);
            if ( r == 1 ) {
                ItemStack item = resSlot.merge(s);

                if ( item == null ) return null;
            } else if ( r == -1 ) throw new RuntimeException("Could not merge item: " + s.getType().name());

        }

        return s;

    }

    public static List<Integer> slots = Arrays.asList(11, 12, 13, 14, 15, 20, 21, 22, 23, 24, 29, 30, 31, 32, 33);

    @Getter
    public static class ResSlot {

        private ItemStack stack;

        private int slot;

        public int couldEmerge(ItemStack stack) {
            // check 2 itemstack similarity
            if ( !PASS_MATERIALS.contains(stack.getType()) && !stack.getType().name().contains("ORE") ) return -1;
            if ( this.stack == null ) return 1;
            if ( this.stack.getType() != stack.getType() ) return 0;
            if ( this.stack.getAmount() >= 64 ) return 0;

            return 1;
        }

        public ItemStack merge(ItemStack stack) {
            if ( this.stack == null ) {
                this.stack = stack;
                return null;
            }

            int amount = stack.getAmount();
            int max = 64;

            int left = max - this.stack.getAmount();

            if ( left >= amount ) {
                this.stack.setAmount(this.stack.getAmount() + amount);
                return null;
            } else {
                this.stack.setAmount(max);
                stack.setAmount(amount - left);
                return stack;
            }
        }

    }

    public static class ResCustomSlot extends ResSlot {

        private int amount = 0, max = 1000;

        public ResCustomSlot(int slot) {
            super.slot = slot;
        }

    }

}
