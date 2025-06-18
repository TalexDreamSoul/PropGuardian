package com.tagzxia.te.src.main.java.com.talexs.soultech.machine.advanced_workbench;

import com.talexs.soultech.internal.RecipeObject;
import com.talexs.soultech.internal.book.IndicateBook;
import com.talexs.soultech.internal.entity.classfies.Classifies;
import com.talexs.soultech.internal.entity.tech_object.TechObject;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerDataRunnable;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.soultech.internal.StNameSpace;
import com.talexs.soultech.internal.entity.items.TalexItem;
import com.talexs.soultech.machine.bsae.BaseMachine;
import com.talexs.soultech.machine.bsae.IMachineBuilder;
import com.talexs.soultech.machine.machine_info.InfoWorldConstruct;
import com.talexs.talexessential.utils.NBTsUtil;
import com.talexs.talexessential.utils.block.TalexBlock;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.ItemUtil;
import com.talexs.talexessential.utils.player.InventoryUtils;
import lombok.SneakyThrows;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import top.zoyn.particlelib.pobject.Cube;

import java.util.*;

public class AdvancedWorkBench extends BaseMachine implements IMachineBuilder {

    private static Map<String, AdvancedWorkBenchGUI> guis = new HashMap<>();

    public AdvancedWorkBench() {

        super("AdvancedWorkBench", new ItemBuilder(Material.CRAFTING_TABLE)

                .setName("§b高级工作台")
                .setLore("", "§8| §a通过 §b高级工作台 §a合成!", "")

                .addFlag(ItemFlag.HIDE_ENCHANTS)
                .addEnchant(Enchantment.DURABILITY, 1)
                .toItemStack(), new AdvanceWorkBenchChecker());

    }

    @Override
    public void onOpenMachine(PlayerData playerData, PlayerEvent event) {

        if ( !( event instanceof PlayerInteractEvent ) ) {
            return;
        }

        Location loc = Objects.requireNonNull(((PlayerInteractEvent) event).getClickedBlock()).getLocation();

        String key = NBTsUtil.Location2String(loc);

        Player player = event.getPlayer();

        new PlayerUser(player)
                .playSound(Sound.BLOCK_NOTE_BLOCK_GUITAR, 1.2F, 1.2F)
                .infoActionBar("你打开了 高级工作台 !");

        AdvancedWorkBenchGUI advancedWorkBenchGUI = guis.containsKey(key) ? guis.get(key) : new AdvancedWorkBenchGUI();

        guis.put(key, advancedWorkBenchGUI);

        advancedWorkBenchGUI.openForPlayer(player, true);

    }

    @Override
    public boolean onOpenRecipeView(IndicateBook guiderBook) {
        Classifies classifies = guiderBook.getClassifies();

        if ( !(classifies instanceof TechObject techObject) ) return false;

        RecipeObject recipeObject = techObject.getRecipe();
        if ( !(recipeObject instanceof WorkBenchRecipe wbr) ) {

            return false;

        }

        int startSlot = 12;

        for ( TalexItem talexItem : wbr.getRequiredList() ) {

            guiderBook.inventoryUI.setItem(startSlot, new InventoryUI.AbstractSuperClickableItem() {

                        @Override
                        public ItemStack getItemStack() {

                            return talexItem == null ? null : new ItemBuilder(talexItem.getItemBuilder().toItemStack().clone()).setAmount(1).toItemStack();

                        }

                        @Override
                        public boolean onClick(InventoryClickEvent e) {
                            if ( talexItem == null ) {
                                return true;
                            }

                            Classifies co = talexItem.getOwnCategoryObject();

                            if ( co == null ) {

                                // 原版物品

                                new PlayerUser(guiderBook.player).actionBar("§c§l无法从 §e§l灵魂科技配方表§c§l 找到这个物品的配方!").playSound(Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);

                            } else {

                                deepRecipeView(guiderBook.setClassifies(co));

                            }

                            return true;

                        }
                    }
            );

            startSlot++;

            if ( ( startSlot + 3 ) % 9 == 0 ) {

                startSlot += 6;

            }

        }

        guiderBook.inventoryUI.setItem(25, new InventoryUI.EmptyClickableItem(new ItemBuilder(guiderBook.getClassifies().getDisplayStack()).setAmount(wbr.getAmount()).toItemStack()));

        new PlayerUser(guiderBook.player).actionBar("§7§l你打开了 §b" + wbr.getDisplayItem().getItemBuilder().getDisplayNameOrDefaultName() + " §7§l的配方.");

        return true;

    }

    @Override
    public String onSave() {
        YamlConfiguration yaml = new YamlConfiguration();

        for ( Map.Entry<String, AdvancedWorkBenchGUI> entry : guis.entrySet() ) {
            String key = NBTsUtil.getRandomStr(16);

            List<String> list = new ArrayList<>();

            for (int i = 0; i < entry.getValue().getStacks().length; i++) {

                if ( entry.getValue().getStacks()[i] == null ) list.add("");
                else
                    list.add(ItemUtil.item2Str(entry.getValue().getStacks()[i]));

            }

            yaml.set("AdvancedWorkbench." + key + ".loc", entry.getKey());
            yaml.set("AdvancedWorkbench." + key + ".stacks", list);

        }


        return yaml.saveToString();
    }

    @SneakyThrows
    @Override
    public void onLoad(String str) {
        if ( str.isEmpty() ) return;

        YamlConfiguration yaml = new YamlConfiguration();

        yaml.loadFromString(str);

        Set<String> keys = Objects.requireNonNull(yaml.getConfigurationSection("AdvancedWorkbench")).getKeys(false);

        for ( String key : keys ) {
            String path = "AdvancedWorkbench." + key;

            String locKey = yaml.getString(path + ".loc");

            List<String> list = yaml.getStringList(path + ".stacks");
            ItemStack[] stacks = new ItemStack[9];

            for (int i = 0; i < list.size(); i++) {

                String thisStr = list.get(i);
                if ( thisStr.isEmpty() ) stacks[i] = null;
                else stacks[i] = ItemUtil.str2Item(thisStr);

            }

            AdvancedWorkBenchGUI advancedWorkBenchGUI = new AdvancedWorkBenchGUI();

            advancedWorkBenchGUI.setStacks(stacks);

            guis.put(locKey, advancedWorkBenchGUI);
        }

    }

    @Override
    public boolean onBreakMachine(TalexBlock tblock) {
        Location loc = tblock.getLoc();
        String key = NBTsUtil.Location2String(loc);

        if ( guis.containsKey(key) ) {
            AdvancedWorkBenchGUI advancedWorkBenchGUI = guis.get(key);

            advancedWorkBenchGUI.dropContents();

            guis.remove(key);

            return true;
        }

        return false;
    }

    @Override
    public boolean checkMaterials(Player player, PlayerData pd, PlayerUser user) {
        PlayerInventory inventory = player.getInventory();
        if ( inventory.first(Material.CRAFTING_TABLE) < 1 ) {
            user.playSound(Sound.ENTITY_AXOLOTL_HURT, 1, 1).errorActionBar("你需要 工作台 才能完成自动建造!");
            return false;
        }
        if ( inventory.first(Material.GLASS) < 1 ) {
            user.playSound(Sound.ENTITY_AXOLOTL_HURT, 1, 1).errorActionBar("你需要 玻璃 才能完成自动建造!");
            return false;
        }

        return true;
    }

    @Override
    public void build(Player player, PlayerData pd, PlayerUser user, Location startLoc) {
        Location loc = startLoc.clone().add(0, 1, 0);
        Location up = loc.clone().add(0, 1, 0);

        if ( loc.getBlock().getType() != Material.AIR || up.getBlock().getType() != Material.AIR ) {
            user.errorActionBar("无法在这里建造!").playSound(Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
            return;
        }

        if ( !couldPlace(player, loc) || !couldPlace(player, up) ) {
            user.errorActionBar("无法在这里建造!").playSound(Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
            return;
        }

        user.delMetaOnHandItem(StNameSpace.GUIDE_AUTO_BUILD);

        Cube cube = new Cube(loc.clone().add(0.1, 0.1, 0.1), up.clone().add(.9, .9, .9));
        cube.setPeriod(20L)
                .setColor(Color.GRAY)
                .show();

        user.delayRun(new PlayerDataRunnable() {
            @Override
            public void run() {
                placeBlock(up, Material.GLASS).placeBlock(loc, Material.CRAFTING_TABLE);
            }
        }, 10);

    }

    @Override
    public void consumeMaterials(Player player, PlayerData pd, PlayerUser user) {
        InventoryUtils.deletePlayerItem(player, Material.CRAFTING_TABLE, 1);
        InventoryUtils.deletePlayerItem(player, Material.GLASS, 1);
    }

    @Override
    public void onOpenMachineInfoViewer(Player player) {
        new InfoWorldConstruct(player, new TalexItem(
                new ItemBuilder(Material.CRAFTING_TABLE).setName("&e高级工作台")
                        .setLore(
                                "",
                                "&8| &7蕴藏着奇怪魔力的高级工作台",
                                "&8| &f在工作台上放置一个玻璃即可",
                                ""
                        ).toItemStack()
        )).open();
    }
}
