package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.soultech.machine.synthesizer;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.codec.Base64Encoder;
import com.tagzxia.te.src.main.java.com.talexs.soultech.machine.synthesizer.SynthesizerChecker;
import com.talexs.soultech.addon.AddonHolder;
import com.talexs.soultech.internal.RecipeObject;
import com.talexs.soultech.internal.StNameSpace;
import com.talexs.soultech.internal.book.IndicateBook;
import com.talexs.soultech.internal.entity.classfies.Classifies;
import com.talexs.soultech.internal.entity.classfies.ClassifiesAddon;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.items.TalexItem;
import com.talexs.soultech.internal.entity.tech_object.TechObject;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.soultech.machine.bsae.BaseMachine;
import com.talexs.soultech.machine.bsae.IMachineBuilder;
import com.talexs.soultech.machine.compressor.CompressorGUI;
import com.talexs.soultech.machine.compressor.CompressorObject;
import com.talexs.soultech.machine.compressor.CompressorRecipe;
import com.talexs.soultech.machine.compressor.CompressorRecipeView;
import com.talexs.soultech.machine.furnace_cauldron.CauldronGUI;
import com.talexs.soultech.machine.furnace_cauldron.FurnaceCauldronObject;
import com.talexs.soultech.machine.furnace_cauldron.FurnaceCauldronRecipe;
import com.talexs.soultech.machine.machine_info.InfoWorldConstruct;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerDataRunnable;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.utils.NBTsUtil;
import com.talexs.talexessential.utils.block.TalexBlock;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.ItemUtil;
import com.talexs.talexessential.utils.player.InventoryUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Dropper;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;
import top.zoyn.particlelib.pobject.Cube;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.*;

/**
 * <p>
 * {@link # pubsher.talexsoultech.talex.machine.furnace_cauldron }
 *
 * @author TalexDreamSoul
 * @date 2024/2/7 0:51:51
 * <p>
 * Project: TalexEssential
 * <p>
 */
public class SynthesizerMachine extends BaseMachine {

    @Getter
    @Setter
    @Accessors( chain = true )
    public static class SynthesizerObject implements Serializable {

        private double per = 0;

        private double step = 0.25;

        private Block centerBlock;

        @SneakyThrows
        public static SynthesizerObject deserialize(String key, String str) {
            YamlConfiguration yamlConfiguration = new YamlConfiguration();

            yamlConfiguration.loadFromString(Base64.decodeStr(str));

            return deserialize(key, yamlConfiguration);

        }

        public static SynthesizerObject deserialize(String key, YamlConfiguration yaml) {

            SynthesizerObject object = new SynthesizerObject();

            object.setPer(yaml.getDouble("per", 0));

            Location loc = NBTsUtil.String2Location(key);

            object.setCenterBlock(loc.getBlock());

            return object;

        }

        @Override
        public String toString() {

            YamlConfiguration yamlConfiguration = new YamlConfiguration();

            yamlConfiguration.set("per", per);

            return yamlConfiguration.saveToString();

        }

        private final int[] empties = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };

        public void update() {
            if ( per < 1 ) {
                per += step;
                return;
            }
            per = 0;

            // get up chest
            Block relative = centerBlock.getRelative(0, 1, 0);
            BlockState state1 = relative.getState();

            Chest chest = (Chest) state1;
            Inventory blockInventory = chest.getInventory();

            BlockState state = centerBlock.getState();

            Dropper dropper = (Dropper) state;

            Inventory inventory = dropper.getInventory();

            ItemStack recipe = findRecipe(inventory);

            if ( recipe == null ) return;

            if ( !blockInventory.containsAtLeast(recipe, 1) ) {

                if ( blockInventory.firstEmpty() < 0 ) {

                    dropper.update();
                    chest.update();

                    return;
                }

            }

            blockInventory.addItem(recipe);

            new ArrayList<>(blockInventory.getViewers()).forEach(viewer -> {
                if ( viewer instanceof Player p ) {
                    p.openInventory(blockInventory);
                } else viewer.closeInventory();
            });

            centerBlock.getWorld().playEffect(centerBlock.getLocation().add(0.5, 1.25, 0.5), Effect.ELECTRIC_SPARK, 1);

        }

        public @Nullable ItemStack findRecipe(Inventory inv) {

            List<Classifies> classifiesArray = new ArrayList<>(
                    List.of(AddonHolder.getINSTANCE().get(ClassifiesAddon.class).getRoot())
            );

            while ( !classifiesArray.isEmpty() ) {
                Classifies cls = classifiesArray.remove(0);

                if ( !cls.getChildren().isEmpty() ) {
                    classifiesArray.addAll(cls.getChildren());
                }

                if ( !(cls instanceof TechObject techObject) ) {
                    continue;
                }

                if ( !(techObject.getRecipe() instanceof WorkBenchRecipe wbr) ) {
                    continue;
                }

                ItemStack itemStack = validateRecipe(inv, wbr);

                if ( itemStack != null ) return itemStack;

            }

            return null;

        }

        public ItemStack validateRecipe(Inventory inv, WorkBenchRecipe wbr) {
            int accessAmount = 0;
            int least = 1;

            for ( int i = 0; i < empties.length; ++i ) {

                ItemStack item = inv.getItem(empties[i]);

                TalexItem ti = wbr.getRecipeAsID(i + 1);

                if ( ti == null || ti.getItemBuilder().toItemStack().getType() == Material.AIR ) {

                    if ( item == null || item.getType() == Material.AIR ) {
                        accessAmount++;
                    } else {
                        SoulTechItem sti = SoulTechItem.getItem(item);

                        if ( sti != null && sti.getID().equalsIgnoreCase("st_empty_mark") ) {
                            accessAmount++;
                        }
                    }

                    continue;

                }

                ti = new TalexItem(ti.getItemBuilder()).addIgnoreType(TalexItem.VerifyIgnoreTypes.IgnoreAmount);

                if ( ti.verify(item) ) {

                    accessAmount++;

                }

            }

            if ( wbr.getExport() == null || wbr.getExport().getItemBuilder().toItemStack().getType() == Material.AIR ) {

                return null;

            }

            if ( accessAmount >= 9 ) {

                for ( int i = 0; i < empties.length; ++i ) {

                    ItemStack stack = inv.getItem(empties[i]);

//                    if ( stack != null ) {
//                        SoulTechItem sti = SoulTechItem.getItem(stack);
//
//                        if ( sti != null && sti.getID().equalsIgnoreCase("st_empty_mark") ) {
//                            accessAmount++;
//                            continue;
//                        }
//                    }

                    TalexItem ti = wbr.getRecipeAsID(i + 1);

                    if ( ti == null || ti.getItemBuilder().toItemStack().getType() == Material.AIR ) {
                        continue;
                    }

                    if (stack != null && stack.getAmount() >= 1) {
                        if ( stack.getAmount() == least )
                            inv.setItem(empties[i], null);
                        else
                            inv.setItem(empties[i], new ItemBuilder(stack).setAmount(stack.getAmount() - least).toItemStack());
                    }

                }

                TalexItem item = wbr.getExport();

//                if ( item instanceof SoulTechItem) {
//
//                    ( (SoulTechItem) item ).onCrafted(player);
//
//                }

                return item.getItemBuilder().setAmount(wbr.getAmount()).toItemStack();

            }

            return null;

        }


    }

    private final HashMap<String, SynthesizerObject> map = new HashMap<>(64);

    static SynthesizerChecker checker = new SynthesizerChecker();

    public SynthesizerMachine() {

        super("Synthesizer", new ItemBuilder(Material.DROPPER).setName("§c自动合成器").setLore("", "§8| §e是时候解放你的双手了!", "").toItemStack(), checker);

        new BukkitRunnable() {
            @Override
            public void run() {

                new ArrayList<>(map.values()).forEach(co -> {
                    Location loc = co.getCenterBlock().getLocation();
                    Block block = loc.getBlock();

                    if ( !checker.onCheck(new PlayerInteractEvent(null, null, null, block, null)) ) {
                        map.remove(loc2Str(loc));
                    } else co.update();
                });

            }
        }.runTaskTimer(TalexEssential.getInstance(), 0, 10);

    }

    @Override
    public void onOpenMachineInfoViewer(Player player) {
        new InfoWorldConstruct(player, new TalexItem(
                new ItemBuilder(Material.DROPPER)
                        .setName(MiniMessage.miniMessage().deserialize("<gradient:#1E1F22:#2B2D30>自动合成器"))
                        .setLore("", "&8| &e解放双手时刻", "&8| &7插拔插槽升级", "&8| &7试试自动合成", "",
                                "&8| &7中间由上至下分别构成：",
                                "&8| &f箱子、投掷器、红石块",
                                "&8| &7两侧构造均一致，为：",
                                "&8| &f铁活板门、活塞，哭泣的黑曜石",
                                "&8| &e蹲下右键 &7以打开控制菜单",
                                "")
                        .toItemStack()
        )).open();
    }

    @Override
    public void onOpenMachine(PlayerData playerData, PlayerEvent event) {
        if ( !(event instanceof PlayerInteractEvent) ) return;

        Block clickedBlock = ((PlayerInteractEvent) event).getClickedBlock();
        if ( clickedBlock == null ) return;

        String strLoc = super.loc2Str(clickedBlock.getLocation());

        SynthesizerObject co = map.get(strLoc);
        if ( !map.containsKey(strLoc) ) {
            co = new SynthesizerObject().setCenterBlock(clickedBlock);
        }

        map.put(strLoc, co);
    }

    @Override
    public boolean onOpenRecipeView(IndicateBook guiderBook) {
        return false;
    }

    @Override
    public String onSave() {
        YamlConfiguration yaml = new YamlConfiguration();

        map.forEach((key, val) -> yaml.set(key, val.toString()));

        return yaml.saveToString();
    }

    @SneakyThrows
    @Override
    public void onLoad(String str) {
        YamlConfiguration yaml = new YamlConfiguration();

        yaml.loadFromString(str);

        if ( !yaml.contains("Synthesizer") ) return;

        Objects.requireNonNull(yaml.getConfigurationSection("Synthesizer")).getKeys(false).forEach(key -> {
            SynthesizerObject deserialize = SynthesizerObject.deserialize("Synthesizer." + key, yaml);
            map.put(loc2Str(deserialize.getCenterBlock().getLocation()), deserialize);
        });

    }

    @Override
    public boolean onBreakMachine(TalexBlock tblock) {
        String strLoc = super.loc2Str(tblock.getLoc());

        map.remove(strLoc);

        return false;
    }
}
