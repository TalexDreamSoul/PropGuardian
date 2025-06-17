package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.soultech.items.material.food;

import com.talexs.soultech.internal.RecipeObject;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.items.material.BaseMaterialItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerDataRunnable;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.CropState;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
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
public class MagicBone extends BaseMaterialItem {

    public MagicBone() {

        super("magic_bone", new ItemBuilder(Material.BONE_MEAL)
                .setName("§5魔法骨粉")
                .addEnchant(Enchantment.ARROW_DAMAGE, 1)
                .addFlag(ItemFlag.HIDE_ENCHANTS)
                .setLore("",
                        "&8| &7对村民使用有 &e1.0% &7的几率催生",
                        "&8| &7对石头使用有 &e5.0% &7的几率变质",
                        "&8| &7对女巫使用有 &e10.0% &7的几率还原",
                        "&8| &7对熔炉加速 &e3200.0% &7在短时间内", "")
                .toItemStack());
    }

    @Override
    public RecipeObject getRecipe() {

        return new WorkBenchRecipe(getID(), this)
                .setAmount(1)
                .addRequired(new MineCraftItem(Material.DIAMOND))
                .addRequired(new MineCraftItem(Material.GOLD_INGOT))
                .addRequired(new MineCraftItem(Material.DIAMOND))
                .addRequired(new MineCraftItem(Material.IRON_INGOT))
                .addRequired("super_bone_plus")
                .addRequired(new MineCraftItem(Material.IRON_INGOT))
                .addRequired(new MineCraftItem(Material.DIAMOND))
                .addRequired(new MineCraftItem(Material.IRON_INGOT))
                .addRequired(new MineCraftItem(Material.DIAMOND));

    }

    @Override
    public void onInteractEntity(PlayerData playerData, PlayerInteractEntityEvent event) {

        ItemStack offStack = event.getPlayer().getInventory().getItemInOffHand();
        PlayerUser user = new PlayerUser(event.getPlayer());

        if ( offStack.getType() != Material.AIR ) {

            user.errorActionBar("你的左手具有驱魔效果，无法使用魔法物品！").playSound(Sound.ENTITY_VILLAGER_NO);

            return;

        }

        user.playSound(Sound.BLOCK_NOTE_BLOCK_PLING).reducePlayerHandItem(1);

        event.setCancelled(true);

        Entity rightClicked = event.getRightClicked();

        if ( rightClicked.getType() == EntityType.VILLAGER ) {

            if ( Math.random() <= 0.01 ) {

                rightClicked.getWorld().playEffect(rightClicked.getLocation(), Effect.VILLAGER_PLANT_GROW, 0);

                rightClicked.getWorld().spawn(rightClicked.getLocation(), Villager.class);

            }

        } else if ( rightClicked.getType() == EntityType.WITCH ) {

            if ( Math.random() <= 0.1 ) {

//                rightClicked.getWorld().playEffect(rightClicked.getLocation(), Effect.VILLAGER_PLANT_GROW, 0);
                rightClicked.getWorld().playSound(rightClicked.getLocation(), Sound.ENTITY_WITCH_DRINK, 1, 1);

                rightClicked.getWorld().spawn(rightClicked.getLocation(), Villager.class);
                rightClicked.remove();

            }

        }

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

        ItemStack offStack = event.getPlayer().getInventory().getItemInOffHand();
        PlayerUser user = new PlayerUser(event.getPlayer());

        if ( offStack.getType() != Material.AIR ) {

            user.errorActionBar("你的左手具有驱魔效果，无法使用魔法物品！").playSound(Sound.ENTITY_VILLAGER_NO);

            return;

        }

        event.setCancelled(true);

        user.playSound(Sound.BLOCK_NOTE_BLOCK_PLING).reducePlayerHandItem(1);

        Material type = block.getType();

        if ( type == Material.STONE ) {

            double r = Math.random();

            if ( r <= 0.05 ) {

                if ( r <= 0.005 ) {
                    block.setType(Material.EMERALD_ORE);
                } else if ( r <= 0.01 ) {
                    block.setType(Material.DIAMOND_ORE);
                } else if ( r <= 0.025 ) {
                    block.setType(Material.GOLD_ORE);
                } else {
                    block.setType(Material.IRON_ORE);
                }

            }

            return;

        }

        if ( type == Material.FURNACE ) {

            long c = System.currentTimeMillis();

            BlockState state = block.getState();

            Furnace furnace = (Furnace) state;

            furnace.setCookSpeedMultiplier(32);
            furnace.update();

            user.delayRunTimer(new PlayerDataRunnable() {
                @Override
                public void run() {
                    long diff = System.currentTimeMillis() - c;
                    if ( diff >= 15000 ) {

                        Furnace state1 = (Furnace) block.getState();
                        state1.setCookSpeedMultiplier(1);
                        state1.update();

//                        furnace.update(false);

                        cancel();
                        return;
                    }

                    block.getWorld().playEffect(block.getLocation(), Effect.MOBSPAWNER_FLAMES, 5, 1);


                }

            }, 0, 40);

        }

    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(1));
    }

}
