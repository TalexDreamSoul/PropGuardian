package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.resources.de.talex.machine.griddle;

import com.tagzxia.te.src.main.resources.de.talex.machine.griddle.GriddleMachine;
import com.tagzxia.te.src.main.resources.de.talex.machine.griddle.GriddleRecipe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import pubsher.talexsoultech.entity.PlayerData;
import pubsher.talexsoultech.talex.BaseTalex;
import pubsher.talexsoultech.talex.guider.category.CategoryObject;
import pubsher.talexsoultech.talex.guider.category.RecipeObject;
import pubsher.talexsoultech.utils.NBTsUtil;
import pubsher.talexsoultech.utils.StringUtil;

import java.io.Serializable;
import java.text.DecimalFormat;

import static org.bukkit.Sound.ENTITY_VILLAGER_NO;

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
public class GriddleObject implements Serializable {

    private ItemStack processingItem;

    private short hasGriddedAmo;

    private Block block;

    private short maxDurability;

    private short usedDurability;

    private boolean isIron;

    @SneakyThrows
    public static GriddleObject deserialize(String str) {

        YamlConfiguration yamlConfiguration = new YamlConfiguration();

        yamlConfiguration.loadFromString(str);

        return new GriddleObject()
                .setHasGriddedAmo((short) yamlConfiguration.getInt("hasGriddedAmo"))
                .setBlock(NBTsUtil.String2Location(yamlConfiguration.getString("block")).getBlock())
                .setIron(yamlConfiguration.getBoolean("iron"))
                .setMaxDurability((short) yamlConfiguration.getInt("mesh"))
                .setProcessingItem(NBTsUtil.GetItemStack(yamlConfiguration.getString("process_item")));

    }

    public String serialize() {

        YamlConfiguration yamlConfiguration = new YamlConfiguration();

        yamlConfiguration.set("process_item", NBTsUtil.ItemData(processingItem));
        yamlConfiguration.set("hasGriddedAmo", hasGriddedAmo);
        yamlConfiguration.set("block", NBTsUtil.Location2String(block.getLocation()));
        yamlConfiguration.set("mesh", maxDurability);
        yamlConfiguration.set("iron", isIron);

        return yamlConfiguration.saveToString();

    }

    public double getDurabilityPercent() {

        return usedDurability / ( maxDurability + 0f );

    }

    public double getProgressPercent() {

        return hasGriddedAmo / ( GriddleMachine.MAP.get(processingItem.getType()) + 0f );

    }

    public void onUpdate(PlayerData playerData) {

//        if(processingItem != null && !GriddleMachine.MAP.containsKey(processingItem.getType())) {
//
//            reset();
//
//
//
//            return;
//
//        }

        if ( processingItem == null || processingItem.getType() == Material.AIR ) {

            if ( !GriddleMachine.MAP.containsKey(playerData.getPlayer().getInventory().getItemInHand().getType()) ) {

                playerData.playSound(ENTITY_VILLAGER_NO, 1f, 1.2f);

                return;

            }

            processingItem = playerData.reducePlayerHandItem(1);

        }

        usedDurability++;
        hasGriddedAmo++;

        double percent = getProgressPercent();

        block.getWorld().spawnParticle(Particle.BLOCK_DUST, block.getLocation().add(0.5, 0.45, 0.5), 100, 0, 0, 0, 0.01);

        String str = StringUtil.generateProgressString(percent, 9, "§b■", "§7■");

        String str2 = StringUtil.generateProgressString(getDurabilityPercent(), 20, "§b|", "§7|");

        playerData.playSound(Sound.BLOCK_SAND_HIT, 1.1f, 1.2f)
                .title("", "§8[ §r" + str + " §8] §7(§e" + ( new DecimalFormat("##.##").format(percent * 100) ) + "%§7) " + hasGriddedAmo + "/" + GriddleMachine.MAP.get(processingItem.getType()), 5, 5, 5)
                .actionBar("§a筛子耐久: §8[ §r" + str2 + " §8] §7(§e" + ( new DecimalFormat("##.##").format(getDurabilityPercent() * 100) ) + "%§7) " + usedDurability + "/" + maxDurability);

        if ( percent >= 1 ) {

            spawn();

            block.getWorld().spawnParticle(Particle.CLOUD, block.getLocation().add(0.5, 0.3, 0.5), 10, 0, 0, 0, 0.01);
            block.getWorld().playSound(block.getLocation(), Sound.BLOCK_SAND_STEP, 1, 1.1f);

        }

    }

    public void reset() {

        this.hasGriddedAmo = 0;
        this.processingItem = null;

    }

    private void spawn() {

        reset();

        this.hasGriddedAmo = 0;

        for ( CategoryObject category : BaseTalex.getInstance().getCategoryManager().getCategories() ) {

            RecipeObject recipeObject = category.getRecipeObject();

            if ( !( recipeObject instanceof GriddleRecipe) ) {
                continue;
            }

            GriddleRecipe recipe = (GriddleRecipe) recipeObject;

            if ( recipe.isIronRequired() && !isIron ) {
                continue;
            }

            if ( !NBTsUtil.isSimilar(recipe.getNeed(), processingItem) ) {
                continue;
            }

            if ( Math.random() <= recipe.getRandom() ) {

                block.getWorld().dropItem(block.getLocation().add(0.5, 1.25, 0.5), recipe.getExport().getItemBuilder().toItemStack());

                if ( !recipe.isAllowedRepeat() ) {
                    return;
                }

            }

        }

    }

}
