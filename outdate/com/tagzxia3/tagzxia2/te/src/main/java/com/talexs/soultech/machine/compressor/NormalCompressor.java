package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.soultech.machine.compressor;

import com.tagzxia.te.src.main.java.com.talexs.soultech.machine.compressor.CompressorChecker;
import com.tagzxia.te.src.main.java.com.talexs.soultech.machine.compressor.CompressorGUI;
import com.tagzxia.te.src.main.java.com.talexs.soultech.machine.compressor.CompressorObject;
import com.tagzxia.te.src.main.java.com.talexs.soultech.machine.compressor.CompressorRecipe;
import com.tagzxia.te.src.main.java.com.talexs.soultech.machine.compressor.CompressorRecipeView;
import com.talexs.soultech.internal.RecipeObject;
import com.talexs.soultech.internal.book.IndicateBook;
import com.talexs.soultech.internal.entity.classfies.Classifies;
import com.talexs.soultech.internal.entity.tech_object.TechObject;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.soultech.internal.entity.items.TalexItem;
import com.talexs.soultech.machine.bsae.BaseMachine;
import com.talexs.soultech.machine.machine_info.InfoWorldConstruct;
import com.talexs.talexessential.utils.block.TalexBlock;
import com.talexs.talexessential.utils.item.ItemBuilder;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NormalCompressor extends BaseMachine {

    static CompressorChecker checker = new CompressorChecker();

    static Map<String, CompressorObject> compressorObjectMap = new HashMap<>();

    public NormalCompressor() {
        super("NormalCompressor", new ItemBuilder(Material.STICKY_PISTON)
                .setName("&b高温压缩机")
                .setLore(
                        "",
                        "&8| &e压缩压力压强",
                        "&8| &7无尽必不可少",
                        "&8| &7层层挤压能力",
                        "&8| &7现在就大不同",
                        ""
                ).toItemStack(), checker);

        new BukkitRunnable() {
            @Override
            public void run() {

                new ArrayList<>(compressorObjectMap.values()).forEach(co -> {
                    Location loc = co.getLoc();
                    if ( loc == null ) return;
                    Block block = loc.getBlock();

                    if ( !checker.onCheck(new PlayerInteractEvent(null, null, null, block, null)) ) {
                        compressorObjectMap.remove(loc2Str(loc));
                    } else co.update();
                });

            }
        }.runTaskTimer(TalexEssential.getInstance(), 0, 10);
    }

    @Override
    public void onOpenMachineInfoViewer(Player player) {
        new InfoWorldConstruct(player, new TalexItem(
                new ItemBuilder(Material.CRAFTING_TABLE).setName("&b高温压缩机")
                        .setLore(
                                "",
                                "&8| &7中间由上至下分别构成：",
                                "&8| &f磁石、烧制钢化玻璃、炼药锅（岩浆）",
                                "&8| &7两侧构造均一致，为：",
                                "&8| &f深板岩瓦台阶、切石机，粘性活塞",
                                ""
                        ).toItemStack()
        )).open();
    }

    @Override
    public void onOpenMachine(PlayerData playerData, PlayerEvent event) {
        if ( !(event instanceof PlayerInteractEvent) ) return;

        Block clickedBlock = ((PlayerInteractEvent) event).getClickedBlock();
        if ( clickedBlock == null ) return;

        String strLoc = super.loc2Str(clickedBlock.getLocation());

        CompressorObject co = compressorObjectMap.get(strLoc);
        if ( !compressorObjectMap.containsKey(strLoc) ) {
            co = new CompressorObject().setLoc(clickedBlock.getLocation());

            co.setGui(new CompressorGUI(co));
        }

        co.getGui().openForPlayer(event.getPlayer());

        compressorObjectMap.put(strLoc, co);
    }

    @Override
    public boolean onOpenRecipeView(IndicateBook guiderBook) {
        Classifies classifies = guiderBook.getClassifies();

        if ( !(classifies instanceof TechObject techObject) ) return false;

        RecipeObject recipeObject = techObject.getRecipe();

        if ( !(recipeObject instanceof CompressorRecipe cr) ) {

            return false;

        }

        new CompressorRecipeView(guiderBook.player, cr, guiderBook)
                .reOpen(1);

        return true;
    }

    @Override
    public String onSave() {
        YamlConfiguration yaml = new YamlConfiguration();

        compressorObjectMap.values().forEach(co -> co.save(yaml));

        return yaml.saveToString();
    }

    @SneakyThrows
    @Override
    public void onLoad(String str) {
        YamlConfiguration yaml = new YamlConfiguration();

        yaml.loadFromString(str);

        if ( !yaml.contains("Compressor") ) return;

        Objects.requireNonNull(yaml.getConfigurationSection("Compressor")).getKeys(false).forEach(key -> {
            CompressorObject deserialize = CompressorObject.deserialize(yaml, "Compressor." + key);
            compressorObjectMap.put(loc2Str(deserialize.getLoc()), deserialize);
        });

    }

    @Override
    public boolean onBreakMachine(TalexBlock tblock) {
        String strLoc = super.loc2Str(tblock.getLoc());
        CompressorObject co = compressorObjectMap.get(strLoc);

        if ( co != null ) {
            for (HumanEntity viewer : co.getGui().inventoryUI.getCurrentUI().getCachedInventory().getViewers()) {
                viewer.closeInventory();
            }
            compressorObjectMap.remove(strLoc);
            return true;
        }

        return false;
    }
}
