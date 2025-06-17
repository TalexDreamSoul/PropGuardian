package com.tagzxia2.te.src.main.java.com.talexs.soultech.items.material.food;

import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.items.material.BaseMaterialItem;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.soultech.internal.RecipeObject;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.CropState;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Crops;

import java.util.Random;
import java.util.Set;

/**
 * <p>
 * {@link # pubsher.talexsoultech.talex.items.food }
 *
 * @author TalexDreamSoul
 * @date 2021/8/6 15:52
 * <p>
 * Project: TalexSoulTech
 * <p>
 */
public class SuperBone extends BaseMaterialItem {

    public SuperBone() {

        super("super_bone", new ItemBuilder(Material.BONE_MEAL)
                .setName("§a超级骨粉")
                .setLore("",
                        "§8| §a对普通作物使用使其直接成熟.", "§8| §7右键甘蔗可使其直接掉落",
                        "§8| §7右键地狱疣可使其直接掉落",
                        "§8| §7右键泥土让其直接变成草方块", "§e魔法作物对其具有抵抗性.", "")
                .toItemStack());
    }

    @Override
    public RecipeObject getRecipe() {

        return new WorkBenchRecipe("super_bone", this)
                .setAmount(4)
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.GOLD_NUGGET))
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.IRON_NUGGET))
                .addRequired(new MineCraftItem(Material.BONE_MEAL))
                .addRequired(new MineCraftItem(Material.IRON_NUGGET))
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.IRON_NUGGET));

    }

    @Override
    public void onInteract(PlayerData playerData, PlayerInteractEvent event) {

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();

        if (block == null) {
            return;
        }

        if ( block.getType() == Material.SUGAR_CANE ) {

            int amo = new Random().nextInt(5) + 1;

            block.getWorld().dropItem(block.getLocation(), new ItemBuilder(Material.SUGAR_CANE).setAmount(amo).toItemStack());

            new PlayerUser(event.getPlayer())
                    .reducePlayerHandItem(1);

            block.getWorld().playEffect(block.getLocation(), Effect.VILLAGER_PLANT_GROW, 30);

            return;
        }

        if ( block.getType() == Material.NETHER_WART ) {

            int amo = new Random().nextInt(3) + 1;

            block.getWorld().dropItem(block.getLocation(), new ItemBuilder(Material.NETHER_WART).setAmount(amo).toItemStack());

            new PlayerUser(event.getPlayer())
                    .reducePlayerHandItem(1);

            block.getWorld().playEffect(block.getLocation(), Effect.VILLAGER_PLANT_GROW, 30);

            return;
        }

        if ( block.getType() == Material.DIRT ) {

            block.setType(Material.GRASS_BLOCK);

            block.getWorld().playEffect(block.getLocation(), Effect.VILLAGER_PLANT_GROW, 30);

            return;
        }

        BlockState state = block.getState();

        if (state.getData() instanceof Crops) {

            event.setCancelled(true);

            new PlayerUser(event.getPlayer()).reducePlayerHandItem(1);

            Crops crops = (Crops) state.getData();

            crops.setState(CropState.RIPE);

            state.setData(crops);

            state.update(true);

            block.getWorld().playEffect(block.getLocation(), Effect.VILLAGER_PLANT_GROW, 30);

        }

    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(1));
    }

}
