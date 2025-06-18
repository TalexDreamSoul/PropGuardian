package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.machine.furnace_cauldron;

import cn.hutool.core.codec.Base64;
import com.tagzxia.te.src.main.java.com.talexs.soultech.machine.furnace_cauldron.FurnaceCauldronRecipe;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.items.TalexItem;
import com.talexs.talexessential.utils.NBTsUtil;
import com.talexs.talexessential.utils.StringUtil;
import com.talexs.talexessential.utils.item.ItemUtil;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * {@link # pubsher.talexsoultech.talex.machine.furnace_cauldron }
 *
 * @author TalexDreamSoul
 * @date 2021/8/14 3:58
 * <p>
 * Project: TalexSoulTech
 * <p>
 */
@Data
@Accessors( chain = true )
@AllArgsConstructor
@NoArgsConstructor
public class FurnaceCauldronObject implements Serializable {

    public Hologram hologram;
    private ItemStack processingItem;
    private double totalTime = -1;
    private long startTime = -1;
    private Block block;

    private FurnaceCauldronRecipe ownRecipe;

    private boolean run;

    private List<TalexItem> saveItems = new ArrayList<>();

    @SneakyThrows
    public static FurnaceCauldronObject deserialize(String key, String str) {
        YamlConfiguration yamlConfiguration = new YamlConfiguration();

        yamlConfiguration.loadFromString(Base64.decodeStr(str));

        return deserialize(key, yamlConfiguration);

    }

    public static FurnaceCauldronObject deserialize(String key, YamlConfiguration yaml) {

        FurnaceCauldronObject object = new FurnaceCauldronObject();

        if ( yaml.contains("type") ) {

            String type = yaml.getString("type");

            if ( "SoulTechItem".equalsIgnoreCase(type) ) {

                object.processingItem = SoulTechItem.getWithoutAddon(yaml.getString("processingItem"));

            }

        } else {

            object.processingItem = NBTsUtil.GetItemStack(yaml.getString("processingItem"));

        }

        object.startTime = yaml.getLong("startTime");
        object.totalTime = yaml.getDouble("totalTime");

        Location loc = NBTsUtil.String2Location(key);

        object.block = loc.getBlock();

        return object;

    }

    public void setOwnRecipe(FurnaceCauldronRecipe ownRecipe) {

        this.ownRecipe = ownRecipe;

        this.totalTime = ownRecipe.getNeedTime();
        this.startTime = System.currentTimeMillis();

        run = true;

    }

    public double getProgressPercent() {

        if ( !run ) {
            return 0;
        }

        long period = System.currentTimeMillis() - startTime;

        return period / totalTime;


    }

    public void speed() {

        this.startTime -= 1000;

    }

    public void onUpdate() {

        if ( !run ) {

            if ( hologram != null ) {
                hologram.destroy();
            }

        } else {

            if ( saveItems.size() >= 9 ) {

                startTime += 500;

                block.getWorld().spawnParticle(Particle.SOUL, block.getLocation().add(0.5, 0.75, 0.5), 1, 0, 0, 0, 0.00001);
                block.getWorld().playSound(block.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1.3f);

                return;

            }

            double percent = getProgressPercent();

            if ( percent >= 1 ) {

                processingItem.setAmount(processingItem.getAmount() - 1);

                if ( processingItem.getAmount() < 1 ) {

                    processingItem = null;

                }

                spawn();

                block.getWorld().spawnParticle(Particle.CLOUD, block.getLocation().add(0.5, 2.05, 0.5), (int) ( 4 * ( totalTime / 2000 ) + 5 ), 0, 0, 0, 0.001);
                block.getWorld().playSound(block.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1.1f);

            }

            if ( hologram != null ) {

                hologram.destroy();

            }

            percent = percent > 1 ? 1 : percent;

            String str = StringUtil.generateProgressString(percent, 5, "§b■", "§7■");

            String item = processingItem == null ? "" : "#ICON: " + processingItem.getType().name() + ":" + processingItem.getAmount();

            List<String> list = Arrays.asList(
                    item,
                    "§8[ §r" + str + " §8] §7(§e" + ( new DecimalFormat("##.##").format(percent * 100) ) + "%§7)",
                    "§e" + new DecimalFormat("##.###").format(Math.abs(( totalTime - System.currentTimeMillis() + startTime ) / 1000f)) + "秒 "
            );

            hologram = DHAPI.createHologram(NBTsUtil.getRandomStr(12), block.getLocation().add(0.5, 2.25, 0.5), list);

            block.getWorld().spawnParticle(Particle.FLAME, block.getLocation().add(0.5, 1.85, 0.5), (int) ( 4 * ( totalTime / 2000 ) + 3 ), 0, 0, 0, 0.01);
            block.getWorld().playSound(block.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1.1f);

        }

    }

    public void reset() {

        this.startTime = System.currentTimeMillis();

    }

    private void spawn() {

        reset();

        saveItems.add(ownRecipe.getExport());

        if ( processingItem == null || processingItem.getType() == Material.AIR ) {

            run = false;

        }

    }

    @Override
    public String toString() {

        YamlConfiguration yamlConfiguration = new YamlConfiguration();

        if ( processingItem instanceof SoulTechItem ) {

            yamlConfiguration.set("type", "SoulTechItem");
            yamlConfiguration.set("processingItem", ( (SoulTechItem) processingItem ).getID());

        } else {

            yamlConfiguration.set("processingItem", ItemUtil.item2Str(processingItem));

        }

        yamlConfiguration.set("totalTime", totalTime);
        yamlConfiguration.set("startTime", startTime);

        return yamlConfiguration.saveToString();

    }

}
