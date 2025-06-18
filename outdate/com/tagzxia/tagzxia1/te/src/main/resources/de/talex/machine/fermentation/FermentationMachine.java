package com.tagzxia.tagzxia1.te.src.main.resources.de.talex.machine.fermentation;

import com.tagzxia.te.src.main.resources.de.talex.machine.fermentation.FermentationGUI;
import com.tagzxia.te.src.main.resources.de.talex.machine.fermentation.FermentationObject;
import com.tagzxia.te.src.main.resources.de.talex.machine.fermentation.FermentationRecipe;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import pubsher.talexsoultech.entity.PlayerData;
import pubsher.talexsoultech.inventory.guider.GuiderBook;
import pubsher.talexsoultech.inventory.machine_info.InfoWorldConstruct;
import pubsher.talexsoultech.talex.BaseTalex;
import pubsher.talexsoultech.talex.guider.category.RecipeObject;
import pubsher.talexsoultech.talex.machine.BaseMachine;
import pubsher.talexsoultech.talex.machine.MachineChecker;
import pubsher.talexsoultech.utils.NBTsUtil;
import pubsher.talexsoultech.utils.inventory.InventoryUI;
import pubsher.talexsoultech.utils.item.ItemBuilder;
import pubsher.talexsoultech.utils.item.TalexItem;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <br /> {@link pubsher.talexsoultech.talex.machine.furnace_cauldron } <br />
 *
 * @author TalexDreamSoul
 * @date 2021/8/14 3:52 <br /> Project: TalexSoulTech <br />
 */
public class FermentationMachine extends BaseMachine {


    private final HashMap<String, com.tagzxia.te.src.main.resources.de.talex.machine.fermentation.FermentationObject> map = new HashMap<>(64);

    public FermentationMachine() {

        super("furnace_cauldron", new ItemBuilder(Material.CAULDRON_ITEM).setName("§c冶炼锅炉").setLore("", "§8> §c冶炼的打造 无尽的锤炼!", "").toItemStack(), new MachineChecker() {

            @Override
            public boolean onCheck(PlayerEvent event) {

                if ( event instanceof PlayerInteractEvent ) {

                    PlayerInteractEvent event1 = (PlayerInteractEvent) event;

                    Block block = event1.getClickedBlock();

                    if ( block != null && block.getType() == Material.CAULDRON ) {

                        Block fire = block.getLocation().add(0, -1, 0).getBlock();

                        return fire.getType() == Material.FIRE;

                    }

                }

                return false;
            }
        });

        runner();

    }

    private void runner() {

        new BukkitRunnable() {

            @Override
            public void run() {

                if ( !BaseTalex.getInstance().getPlugin().isEnabled() ) {

                    cancel();
                    return;

                }

                subRun();

            }

        }.runTaskTimerAsynchronously(BaseTalex.getInstance().getPlugin(), 20, 20);

    }

    private void subRun() {

        new BukkitRunnable() {

            @Override
            public void run() {

                for ( Map.Entry<String, com.tagzxia.te.src.main.resources.de.talex.machine.fermentation.FermentationObject> entry : new HashSet<>(map.entrySet()) ) {

                    com.tagzxia.te.src.main.resources.de.talex.machine.fermentation.FermentationObject obj = entry.getValue();

                    Block block = obj.getBlock();

                    if ( block.getType() != Material.CAULDRON || block.getLocation().add(0, -1, 0).getBlock().getType() != Material.FIRE ) {

                        if ( obj.hologram != null ) {

                            obj.hologram.destroy();

                        }

                        if ( obj.getProcessingItem() != null && obj.getProcessingItem().getType() != Material.AIR ) {

                            block.getWorld().dropItem(block.getLocation(), obj.getProcessingItem());

                        }

                        for ( TalexItem item : obj.getSaveItems() ) {

                            block.getWorld().dropItem(block.getLocation(), item.getItemBuilder().toItemStack());

                        }

                        map.remove(entry.getKey());

                        block.getWorld().createExplosion(block.getLocation(), (float) ( Math.abs(( obj.getTotalTime() / 50000 ) * 1.5f) - 0.5f ));

                        continue;

                    }

                    entry.getValue().onUpdate();

                }

            }

        }.runTask(BaseTalex.getInstance().getPlugin());

    }

    @Override
    public void onOpenMachineInfoViewer(PlayerData playerData) {

        new InfoWorldConstruct(playerData, new TalexItem(new ItemBuilder(Material.CAULDRON_ITEM)

                .setName("§c冶炼熔炉")

                .setLore("", "§8> §e在锅炉下点火, 然后右键锅炉!", "")

        )).open();

    }

    @Override
    public void onOpenMachine(PlayerData playerData, PlayerEvent event) {

        if ( !( event instanceof PlayerInteractEvent ) ) {

            return;

        }

        process(playerData, (PlayerInteractEvent) event);

    }

    private void process(PlayerData playerData, PlayerInteractEvent event) {

        if ( event.getAction() != Action.RIGHT_CLICK_BLOCK ) {
            return;
        }

        Block block = event.getClickedBlock();

        String str = NBTsUtil.Location2String(block.getLocation());

        com.tagzxia.te.src.main.resources.de.talex.machine.fermentation.FermentationObject obj = map.get(str);

        if ( obj == null ) {

            obj = new com.tagzxia.te.src.main.resources.de.talex.machine.fermentation.FermentationObject();

            obj.setBlock(block);

            map.put(str, obj);

        }

        event.setCancelled(true);

        new FermentationGUI(playerData, obj).open();

    }

    @Override
    public boolean onOpenRecipeView(GuiderBook guiderBook) {

        RecipeObject recipeObject = guiderBook.getCategory().getRecipeObject();

        if ( !( recipeObject instanceof com.tagzxia.te.src.main.resources.de.talex.machine.fermentation.FermentationRecipe) ) {

            return false;

        }

        com.tagzxia.te.src.main.resources.de.talex.machine.fermentation.FermentationRecipe fcr = (FermentationRecipe) recipeObject;

        guiderBook.inventoryUI.setItem(22, new InventoryUI.AbstractSuperClickableItem() {

            @Override
            public ItemStack getItemStack() {

                return fcr.getNeed();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {


                return false;
            }
        });

        ItemBuilder ib = new ItemBuilder(fcr.getExport().clone())
                .addLoreLine("")
                .addLoreLine("§a配方冶炼时间: §e" + new DecimalFormat("##.###").format(fcr.getNeedTime() / 1000f) + "秒 ")
                .addLoreLine("");

        guiderBook.inventoryUI.setItem(25, new InventoryUI.AbstractSuperClickableItem() {

            @Override
            public ItemStack getItemStack() {

                return ib.toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {


                return false;
            }
        });

        return true;

    }

    @Override
    public String onSave() {

        YamlConfiguration yaml = new YamlConfiguration();

        for ( Map.Entry<String, com.tagzxia.te.src.main.resources.de.talex.machine.fermentation.FermentationObject> entry : map.entrySet() ) {

            com.tagzxia.te.src.main.resources.de.talex.machine.fermentation.FermentationObject obj = entry.getValue();

            if ( obj.hologram != null ) {
                obj.hologram.destroy();
            }

            yaml.set("Objects." + entry.getKey(), NBTsUtil.Base64_Encode(obj.toString()));

        }

        return yaml.saveToString();

    }

    @SneakyThrows
    @Override
    public void onLoad(String str) {

        YamlConfiguration yamlConfiguration = new YamlConfiguration();

        yamlConfiguration.loadFromString(str);

        if ( yamlConfiguration.contains("Objects") ) {

            Set<String> keys = yamlConfiguration.getConfigurationSection("Objects").getKeys(false);

            for ( String key : keys ) {

                map.put(key, FermentationObject.deserialize(key, yamlConfiguration.getString("Objects." + key)));

            }

        }

    }

}
