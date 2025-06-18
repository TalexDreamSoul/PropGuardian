package com.tagzxia3.tagzxia2.te.src.main.resources.de.talex.machine.griddle;

import com.tagzxia.te.src.main.resources.de.talex.machine.griddle.GriddleObject;
import com.tagzxia.te.src.main.resources.de.talex.machine.griddle.GriddleRecipe;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import pubsher.talexsoultech.entity.PlayerData;
import pubsher.talexsoultech.inventory.guider.GuiderBook;
import pubsher.talexsoultech.inventory.machine_info.InfoWorldConstruct;
import pubsher.talexsoultech.talex.guider.category.RecipeObject;
import pubsher.talexsoultech.talex.machine.BaseMachine;
import pubsher.talexsoultech.talex.machine.MachineChecker;
import pubsher.talexsoultech.utils.NBTsUtil;
import pubsher.talexsoultech.utils.inventory.InventoryUI;
import pubsher.talexsoultech.utils.item.ItemBuilder;
import pubsher.talexsoultech.utils.item.TalexItem;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * {@link # pubsher.talexsoultech.talex.machine.furnace_cauldron }
 *
 * @author TalexDreamSoul
 * @date 2021/8/14 3:52
 * <p>
 * Project: TalexSoulTech
 * <p>
 */
public class GriddleMachine extends BaseMachine {

    public static final HashMap<String, GriddleObject> map = new HashMap<>(64);
    public static HashMap<Material, Integer> MAP = new HashMap<>();

    public GriddleMachine() {

        super("griddles", new ItemBuilder(Material.TRAP_DOOR).setName("§c过滤筛子").setLore("", "§8> §e无尽的磨练即千锤百炼!", "").toItemStack(), new MachineChecker() {

            @Override
            public boolean onCheck(PlayerEvent event) {

                if ( event instanceof PlayerInteractEvent ) {

                    PlayerInteractEvent event1 = (PlayerInteractEvent) event;

                    Block block = event1.getClickedBlock();

                    if ( block != null && block.getType().name().replace("_", "").contains("TRAPDOOR") ) {

                        Block fire = block.getLocation().add(0, -1, 0).getBlock();

                        return fire.getType() == Material.HOPPER;

                    }

                }

                return false;
            }
        });

        MAP.put(Material.SAND, 5);
        MAP.put(Material.COBBLESTONE, 7);
        MAP.put(Material.DIRT, 5);
        MAP.put(Material.GRAVEL, 5);
        MAP.put(Material.WOOL, 12);

    }

    @Override
    public void onOpenMachineInfoViewer(PlayerData playerData) {

        new InfoWorldConstruct(playerData, new TalexItem(new ItemBuilder(Material.TRAP_DOOR)

                .setName("§c筛子")

                .setLore("", "§8> §e将任意筛网放置在漏斗上形成筛子.", "")

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

        if ( event.getAction() != Action.RIGHT_CLICK_BLOCK || playerData.getPlayer().isSneaking() ) {
            return;
        }

        event.setCancelled(true);

        Block block = event.getClickedBlock();

        String str = NBTsUtil.Location2String(block.getLocation().add(0, -1, 0));

        GriddleObject obj = map.get(str);

        if ( obj == null ) {

            playerData.title("§c§l✖", "§e筛网未正确放置!!", 5, 15, 5)
                    .playSound(Sound.BLOCK_ANVIL_LAND, 1.1f, 1.2f);

            return;

        }

        obj.onUpdate(playerData);

        if ( obj.getUsedDurability() > obj.getMaxDurability() ) {

            block.setType(Material.AIR);

            playerData.playSound(Sound.BLOCK_ANVIL_BREAK, 1.1f, 1.2f);

            map.remove(str);

        }

    }

    @Override
    public boolean onOpenRecipeView(GuiderBook guiderBook) {

        RecipeObject recipeObject = guiderBook.getCategory().getRecipeObject();

        if ( !( recipeObject instanceof GriddleRecipe) ) {

            return false;

        }

        GriddleRecipe gr = (GriddleRecipe) recipeObject;

        guiderBook.inventoryUI.setItem(22, new InventoryUI.AbstractSuperClickableItem() {

            @Override
            public ItemStack getItemStack() {

                return gr.getNeed();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {


                return false;
            }
        });

        ItemBuilder ib = new ItemBuilder(gr.getExport().clone()).setAmount(gr.getAmount())
                .addLoreLine("")
                .addLoreLine("§a概率 §e" + new DecimalFormat("##.##").format(gr.getRandom() * 100) + "% ")
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

        YamlConfiguration yamlConfiguration = new YamlConfiguration();

        for ( Map.Entry<String, GriddleObject> entry : map.entrySet() ) {

            yamlConfiguration.set("Griddles." + NBTsUtil.Base64_Encode(entry.getKey()), NBTsUtil.Base64_Encode(entry.getValue().serialize()));

        }

        return yamlConfiguration.saveToString();
    }

    @SneakyThrows
    @Override
    public void onLoad(String str) {

        YamlConfiguration yaml = new YamlConfiguration();

        yaml.loadFromString(str);

        if ( !yaml.contains("Griddles") ) {
            return;
        }

        for ( String key : yaml.getConfigurationSection("Griddles").getKeys(false) ) {

            String finalKey = NBTsUtil.Base64_Decode(key);

            map.put(finalKey, GriddleObject.deserialize(NBTsUtil.Base64_Decode(yaml.getString("Griddles." + key))));

        }

    }

}
