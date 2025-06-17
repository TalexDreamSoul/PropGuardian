package com.tagzxia.te.src.main.java.com.talexs.soultech.items.space.tank;

import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.soultech.internal.StNameSpace;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.soultech.internal.RecipeObject;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.zoyn.particlelib.pobject.Cube;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * {@link # pubsher.talexsoultech.talex.items.tank }
 *
 * @author TalexDreamSoul
 * @date 2021/8/17 0:32
 * <p>
 * Project: TalexSoulTech
 * <p>
 */
public class DiamondTankPlus extends BaseTank {

    public DiamondTankPlus() {

        super("diamond_tank_plus", 32000);

    }

    @Override
    public RecipeObject getRecipe() {

        return new WorkBenchRecipe("diamond_tank_plus", this)

                .addRequiredNull()
                .addRequiredNull()
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.NETHERITE_INGOT))
                .addRequired("tank_diamond_tank")
                .addRequired(new MineCraftItem(Material.DIAMOND_BLOCK))
                .addRequired("space_fire_dust")
                .addRequired(new MineCraftItem(Material.NETHERITE_INGOT))
                .addRequired("space_fire_dust")
                ;
    }

    @Override
    public void onInteract(PlayerData playerData, PlayerInteractEvent event) {

        event.setCancelled(true);

        Player player = event.getPlayer();
        ItemStack stack = player.getInventory().getItemInMainHand();
        if ( stack.getAmount() != 1 ) {
            new PlayerUser(player).title("", "§7一次性只能装填一个储罐!", 5, 12, 5)
                    .playSound(Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1.1f);
            return;
        }

        ItemBuilder ib = new ItemBuilder(stack);

        double storaged = Double.parseDouble(Objects.requireNonNull(ib.getPRCData(StNameSpace.LIQUID_TANK_STORAGED)));
        TankMode mode = TankMode.valueOf(ib.getPRCData(StNameSpace.LIQUID_TANK_MODE));

        if ( event.getAction() == Action.LEFT_CLICK_AIR ) {

            if ( mode == TankMode.PROVIDE ) {

                mode = TankMode.STORAGE;

            } else {

                mode = TankMode.PROVIDE;

            }

            stack = ib.addPRCData(StNameSpace.LIQUID_TANK_MODE, mode.name()).toItemStack();

            new PlayerUser(player).title("", "§7模式已切换!", 5, 12, 5)
                    .playSound(Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1.1f);

        } else {

            Block block = event.getClickedBlock();

            if ( block == null ) {
                return;
            }

            BlockFace face = event.getBlockFace();

            Block block2 = block.getLocation().add(face.getModX(), face.getModY(), face.getModZ()).getBlock();

            Material material = block2.getType();

            String materialType = getMaterialLiquidType(block2);

            String liquidType = "";

            if ( ib.hasPRCData(StNameSpace.LIQUID_TANK_TYPE) ) {

                liquidType = ib.getPRCData(StNameSpace.LIQUID_TANK_TYPE);

            }

            if ( mode == TankMode.PROVIDE ) {

                event.setCancelled(false);

            } else {

                if ( storaged >= Double.parseDouble(Objects.requireNonNull(ib.getPRCData(StNameSpace.LIQUID_TANK_MAX_STORAGE))) ) {

                    new PlayerUser(player).title("", "§7储罐已满!", 5, 12, 5)
                            .playSound(Sound.ENTITY_VILLAGER_NO, 1, 1.1f);

                    event.setCancelled(true);

                    Cube cube = new Cube(block2.getLocation().clone().add(0.1, 0.1, 0.1), block2.getLocation().clone().add(.9, .9, .9));
                    cube.setPeriod(20L)
                            .setColor(Color.GRAY)
                            .show();

                    return;

                }

                Cube cube = new Cube(block2.getLocation().clone().add(0.1, 0.1, 0.1), block2.getLocation().clone().add(.9, .9, .9));
                cube.setPeriod(20L)
                        .setColor(Color.GRAY)
                        .show();
//                ParticleUtil.drawBlockParticleLine(block2.getLocation(), Particle.SLIME, 0.95);

                if ( liquidType != null && !liquidType.equals("") && !liquidType.equalsIgnoreCase(materialType) ) {

//                    playerData.title("", "§7两种液体不可以混合..", 5, 12, 5)
//                            .actionBar(liquidType + " # " + materialType)
//                            .playSound(Sound.ENTITY_VILLAGER_NO, 1, 1.1f);

                    return;

                } else {

                    if ( materialType == null ) {

                        new PlayerUser(player).actionBar("&c不是支持的类型!");

                    } else {

                        liquidType = materialType;

                        block2.setType(Material.AIR);

                        storaged += 1000;

                        new PlayerUser(player)
                                .playSound(material.name().contains("LAVA") ? Sound.ITEM_BUCKET_FILL_LAVA : Sound.ITEM_BUCKET_FILL, 1, 1.1f);

                    }

                }



            }

            stack = ib.addPRCData(StNameSpace.LIQUID_TANK_STORAGED, String.valueOf(storaged))
                    .addPRCData(StNameSpace.LIQUID_TANK_TYPE, liquidType).toItemStack();

        }

        player.getInventory().setItemInMainHand(refreshStack(mode, storaged, stack));

    }

    @Override
    public void onBucketFull(PlayerData playerData, PlayerBucketFillEvent event) {

    }

    private String getMaterialLiquidType(Block block) {

        if ( !block.isLiquid() ) {
            return null;
        }

        short data = block.getState().getData().getData();

        if ( data != 0 ) {
            return null;
        }

        String name = block.getType().name();

        if ( name.contains("WATER") ) {

            return "WATER";

        } else if ( name.contains("LAVA") ) {

            return "LAVA";

        }

        return null;

    }

    public ItemStack refreshStack(TankMode mode, double storaged, ItemStack stack) {

        ItemBuilder ib = new ItemBuilder(stack);
        String liquid_tank_max_storage = ib.getPRCData(StNameSpace.LIQUID_TANK_MAX_STORAGE);
        String liquidType = ib.getPRCData(StNameSpace.LIQUID_TANK_TYPE);

        if ( mode == TankMode.PROVIDE ) {

            stack.setType(storaged >= 0 ? (liquidType.equalsIgnoreCase("LAVA") ? Material.LAVA_BUCKET : Material.WATER_BUCKET) : Material.BUCKET);

        } else {

            stack.setType(storaged >= Double.parseDouble(liquid_tank_max_storage) ? Material.POTION : Material.BUCKET);

        }

        ItemMeta im = stack.getItemMeta();

        List<String> lore = im.getLore();

        int i = 0;

        for ( String str : lore ) {

            if ( str.contains("§f容量") ) {

                lore.set(i, "§f容量: §e" + storaged + "/" + liquid_tank_max_storage + " ml");

            } else if ( str.contains("§f模式") ) {

                lore.set(i, "§f模式: " + ( mode == TankMode.STORAGE ? "§e存储" : "§a提供" ));

            } else if ( str.contains("液体") ) {

                lore.set(i, "§f液体: " + ( liquidType.equalsIgnoreCase("") ? "§e未知" : liquidType.equalsIgnoreCase("LAVA") ? "§c岩浆" : ( liquidType.equalsIgnoreCase("WATER") ? "§b水" : liquidType ) ));

            }

            ++i;

        }

        im.setLore(lore);

        if ( mode == TankMode.PROVIDE ) {

            im.addEnchant(Enchantment.DURABILITY, 1, true);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        } else {

            im.removeEnchant(Enchantment.DURABILITY);
            im.removeItemFlags(ItemFlag.HIDE_ENCHANTS);

        }

        stack.setItemMeta(im);

        return stack;

    }

}
