package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.soultech.internal.block;

import com.talexs.soultech.addon.BaseAddon;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.utils.NBTsUtil;
import com.talexs.talexessential.utils.block.TalexBlock;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.ItemUtil;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import com.talexs.talexessential.TalexEssential;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author TalexDreamSoul
 */
public class BlockAddon extends BaseAddon implements Listener {

    private final HashMap<Location, TalexBlock> placeBlocks = new HashMap<>(1024);

    private final File savedFile;

    public BlockAddon() {
        savedFile = new File(plugin.getDataFolder() + "/caches/block_caches.yml");
    }

    public TalexBlock getBlock(Location loc) {

        return placeBlocks.get(loc.getBlock().getLocation());

    }

    public TalexBlock getBlock(Block block) {

        return placeBlocks.get(block.getLocation());

    }

    public TalexBlock check(BlockBreakEvent event) {

        Block block = event.getBlock();

        if ( placeBlocks.containsKey(block.getLocation()) ) {

            return placeBlocks.get(block.getLocation());

        }

        return null;

    }

    public BlockAddon delBlock(Location location) {

        placeBlocks.remove(location.getBlock().getLocation());

        return this;

    }

    public BlockAddon delBlock(Block block) {

        placeBlocks.remove(block.getLocation());

        return this;

    }

    @SneakyThrows
    public void saveAllIntoFile(File file) {

        YamlConfiguration yaml = new YamlConfiguration();

        if ( !file.exists() ) {

            file.getParentFile().mkdirs();

            if ( !file.createNewFile() ) {

                throw new Exception("创建文件失败，无法保存数据");

            }

        }

        int whole = 0;

        for ( Map.Entry<Location, TalexBlock> entry : placeBlocks.entrySet() ) {

            String str = NBTsUtil.Location2String(entry.getKey());
            TalexBlock stack = entry.getValue();

            if ( stack == null ) {
                continue;
            }

            Location loc = NBTsUtil.String2Location(str);

            if ( loc == null ) {
                TalexEssential.getInstance().getLogger().info("非法物品位置: " + str);
                continue;
            }

            String randomStr = NBTsUtil.getRandomStr(16);
            while ( yaml.contains("Blocks." + randomStr) ) {

                randomStr = NBTsUtil.getRandomStr(16);

            }

            String path = "Blocks." + randomStr;

            yaml.set(path + ".loc", str);
            yaml.set(path + ".data", ItemUtil.item2Str(stack.getStack()));
            whole++;

        }

        try {
            yaml.save(file);
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        TalexEssential.getInstance().getServer().getConsoleSender().sendMessage("[TPT] §e保存数据方块: §a" + whole + "§7/§c" + placeBlocks.keySet().size());

    }

    public void loadAllFromFile(String filePath) {

        if ( !new File(filePath).exists() ) {
            return;
        }

        YamlConfiguration yaml = new YamlConfiguration();

        try {
            yaml.load(filePath);
        } catch ( IOException | InvalidConfigurationException e ) {
            e.printStackTrace();
        }

        if ( !yaml.contains("Blocks") ) {
            return;
        }

        Set<String> keys = yaml.getConfigurationSection("Blocks").getKeys(false);
        int whole = 0;
        for ( String key : keys ) {

            String path = "Blocks." + key;
            String location = yaml.getString(path + ".loc");

            Location loc = NBTsUtil.String2Location(location);

            if ( loc == null ) {
                continue;
            }

            String data = yaml.getString(path + ".data");
            ItemStack stack = NBTsUtil.GetItemStack(data);

            if ( stack == null ) {
                continue;
            }

            new TalexBlock(loc, stack);

            whole++;

        }

        TalexEssential.getInstance().getServer().getConsoleSender().sendMessage("[TPT] §a加载数据方块: §b" + whole + "§7/§c" + keys.size());

    }

    public BlockAddon addBlock(Location loc, TalexBlock block) {

        if ( loc == null ) {
            return this;
        }

        placeBlocks.put(loc.getBlock().getLocation(), block);

        return this;

    }

    @Override
    public void onEnable() {
        loadAllFromFile(savedFile.getAbsolutePath());
    }

    @Override
    public void onDisable() {
        saveAllIntoFile(savedFile);
    }

    @EventHandler( ignoreCancelled = true, priority = EventPriority.LOWEST )
    public void onBlockBreak(BlockBreakEvent event) {

        ItemStack stack = event.getPlayer().getItemInHand();

        PlayerData playerData = PlayerData.g(event.getPlayer());

//        if ( !getProtectorAddon().couldBreak(event.getPlayer(), event.getBlock().getLocation()) ) {
//
//            return;
//
//        }

        SoulTechItem sti = SoulTechItem.getItem(stack);
        if ( sti != null ) {

            if ( sti.useItemBreakBlock(playerData, event) ) {

                delBlock(event.getBlock().getLocation());
                return;

            }

        }

        TalexBlock tb = check(event);

        if ( tb != null ) {

            tb.onBlockBreak(playerData, event);

        }

    }

    @EventHandler( ignoreCancelled = true, priority = EventPriority.MONITOR )
    public void onBlockPlaced(BlockPlaceEvent event) {

        ItemStack stack = event.getItemInHand().clone();

        Material material = stack.getType();
        String materialName = material.name();

        if ( material == Material.AIR || material == Material.DRAGON_EGG || material == Material.SAND || material == Material.GRAVEL || materialName.contains("SHULKER_BOX") || materialName.contains("AXE") || materialName.contains("HOE") || materialName.contains("SPADE") || materialName.contains("SWORD") ) {
            return;
        }

        ItemStack target = new ItemBuilder(stack).setAmount(1).toItemStack();
        SoulTechItem sti = SoulTechItem.getItem(target);
        if ( sti == null ) return;

        PlayerData pd = PlayerData.g(event.getPlayer());
        if ( !sti.onPlaceItem(pd, event) ) {

            TalexBlock tb = new TalexBlock(event.getBlock().getLocation(), target);

            tb.setItem(sti);

        }

//        for(Map.Entry<String, SoulTechItem> entry : SoulTechItem.getItems().entrySet()) {
//
//
//
////            if(sti.verify(stack, new HashSet<>(Arrays.asList(TalexItem.VerifyIgnoreTypes.IgnoreAmount, TalexItem.VerifyIgnoreTypes.IgnoreDurability)))) {
////
////                if(!sti.onPlaceItem(playerData, event)) {
////
////                    TalexBlock tb = new TalexBlock(event.getBlock().getLocation(), target);
////
////                    tb.setItem(entry.getValue());
////
////                }
////
////                return;
////
////            }
//
//        }


    }

}
