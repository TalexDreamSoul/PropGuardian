package com.tagzxia3.te.src.main.java.com.talexs.soultech.machine.compressor;

import com.tagzxia.te.src.main.java.com.talexs.soultech.machine.compressor.CompressorGUI;
import com.talexs.talexessential.utils.NBTsUtil;
import com.talexs.talexessential.utils.item.ItemUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import top.zoyn.particlelib.pobject.Cube;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class CompressorObject {

    private com.tagzxia.te.src.main.java.com.talexs.soultech.machine.compressor.CompressorGUI gui;

    private Location loc;

    public void update() {
        drawEmptyCube();
    }

    private int lastTick = 0, processedTick = 0;

    private List<ItemStack> recipeItems = new ArrayList<>(20), loadItems = new ArrayList<>(4);

    public void addRecipeItem(ItemStack item) {
        recipeItems.add(item);
    }

    public void addLoadItem(ItemStack item) {
        loadItems.add(item);
    }

    private void drawEmptyCube() {
        lastTick += 1;
        if ( lastTick < 4 ) return;
        lastTick = 0;

        Cube cube = new Cube(loc.clone().add(0.2, 0.2, 0.2), loc.clone().add(.8, .8, .8));
        cube.setPeriod(20L)
                .setColor(Color.WHITE)
                .show();
    }

    public void save(YamlConfiguration yaml) {
        String path = "Compressor." + NBTsUtil.getRandomStr(12);

        gui.save();

        yaml.set(path + ".loc", NBTsUtil.Location2String(loc));

        List<String> recipeItemStrs = new ArrayList<>();

        for ( ItemStack item : recipeItems ) {
            if ( item == null || item.getType() == Material.AIR )
                recipeItemStrs.add("");
            else recipeItemStrs.add(ItemUtil.item2Str(item));
        }

        yaml.set(path + ".recipeItems", recipeItemStrs);

        List<String> loadItemStrs = new ArrayList<>();

        for ( ItemStack item : loadItems ) {
            if ( item == null || item.getType() == Material.AIR )
                loadItemStrs.add("");
            else loadItemStrs.add(ItemUtil.item2Str(item));
        }

        yaml.set(path + ".loadItems", loadItemStrs);
        yaml.set(path + ".processedTick", processedTick);
        yaml.set(path + ".lastTick", lastTick);

    }

    public static CompressorObject deserialize(YamlConfiguration yaml, String path) {
        CompressorObject co = new CompressorObject();

        co.setLoc(NBTsUtil.String2Location(yaml.getString(path + ".loc")));

        co.getLoadItems().clear();
        co.getRecipeItems().clear();

        List<String> recipeItemStrs = yaml.getStringList(path + ".recipeItems");

        for ( String itemStr : recipeItemStrs ) {
            if ( itemStr == null || itemStr.isEmpty() ) co.addRecipeItem(null);
            else co.addRecipeItem(ItemUtil.str2Item(itemStr));
        }

        List<String> loadItemStrs = yaml.getStringList(path + ".loadItems");

        for ( String itemStr : loadItemStrs ) {
            if ( itemStr == null || itemStr.isEmpty() ) co.addLoadItem(null);
            else co.addLoadItem(ItemUtil.str2Item(itemStr));
        }

        co.setProcessedTick(yaml.getInt(path + ".processedTick"));
        co.setLastTick(yaml.getInt(path + ".lastTick"));

        co.gui = new CompressorGUI(co);

        co.gui.load();

        return co;
    }

}
