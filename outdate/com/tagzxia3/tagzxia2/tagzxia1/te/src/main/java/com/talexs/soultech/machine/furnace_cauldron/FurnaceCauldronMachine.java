package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.machine.furnace_cauldron;

import cn.hutool.core.codec.Base64Encoder;
import com.tagzxia.te.src.main.java.com.talexs.soultech.machine.furnace_cauldron.CauldronGUI;
import com.tagzxia.te.src.main.java.com.talexs.soultech.machine.furnace_cauldron.FurnaceCauldronObject;
import com.tagzxia.te.src.main.java.com.talexs.soultech.machine.furnace_cauldron.FurnaceCauldronRecipe;
import com.talexs.soultech.internal.book.IndicateBook;
import com.talexs.soultech.internal.entity.classfies.Classifies;
import com.talexs.soultech.internal.entity.tech_object.TechObject;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerDataRunnable;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.soultech.internal.StNameSpace;
import com.talexs.soultech.internal.entity.items.TalexItem;
import com.talexs.soultech.machine.bsae.BaseMachine;
import com.talexs.soultech.machine.bsae.IMachineBuilder;
import com.talexs.soultech.internal.RecipeObject;
import com.talexs.talexessential.utils.NBTsUtil;
import com.talexs.talexessential.utils.block.TalexBlock;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.player.InventoryUtils;
import lombok.SneakyThrows;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import top.zoyn.particlelib.pobject.Cube;

import java.text.DecimalFormat;
import java.util.*;

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
public class FurnaceCauldronMachine extends BaseMachine implements IMachineBuilder {

    private final HashMap<String, FurnaceCauldronObject> map = new HashMap<>(64);

    public FurnaceCauldronMachine() {

        super("FurnaceCauldron", new ItemBuilder(Material.CAULDRON).setName("§c冶炼锅炉").setLore("", "§8> §c冶炼的打造 无尽的锤炼!", "").toItemStack(), event -> {

            if ( event instanceof PlayerInteractEvent ) {

                PlayerInteractEvent event1 = (PlayerInteractEvent) event;

                Block block = event1.getClickedBlock();

                if ( block != null && block.getType() == Material.CAULDRON ) {

                    Block fire = block.getLocation().add(0, -1, 0).getBlock();

                    return fire.getType() == Material.FIRE || ( fire.getType().name().contains("LAVA") && fire.getState().getData().getData() == 0 );

                }

            }

            return false;
        });

        runner();

    }

    private void runner() {

        new BukkitRunnable() {

            @Override
            public void run() {

                if ( !TalexEssential.getInstance().isEnabled() ) {

                    cancel();
                    return;

                }

                subRun();

            }

        }.runTaskTimerAsynchronously(TalexEssential.getInstance(), 20, 20);

    }

    private void subRun() {

        new BukkitRunnable() {

            @Override
            public void run() {

                for ( Map.Entry<String, FurnaceCauldronObject> entry : new HashSet<>(map.entrySet()) ) {

                    FurnaceCauldronObject obj = entry.getValue();

                    Block block = obj.getBlock();

                    Block fire = block.getLocation().add(0, -1, 0).getBlock();

                    if ( block.getType() != Material.CAULDRON || ( fire.getType() != Material.FIRE && !( fire.getType().name().contains("LAVA") && fire.getState().getData().getData() == 0 ) ) ) {

                        if ( obj.hologram != null ) {

                            obj.hologram.destroy();

                        }

                        if ( obj.getProcessingItem() != null && obj.getProcessingItem().getType() != Material.AIR ) {

                            block.getWorld().dropItem(block.getLocation(), obj.getProcessingItem());

                        }

                        for ( TalexItem item : obj.getSaveItems() ) {

                            block.getWorld().dropItem(block.getLocation(), item.getItemBuilder().toItemStack());

                        }

                        map.remove(entry.getKey());

                        block.getWorld().createExplosion(block.getLocation(), (float) ( Math.abs(( obj.getTotalTime() / 50000 ) * 1.5f) - 0.5f ));

                        continue;

                    }

                    entry.getValue().onUpdate();

                    if ( fire.getType().name().contains("LAVA") ) {

                        // TODO
                        block.getWorld().spawnParticle(Particle.LAVA, block.getLocation().add(0.5, 0.5, 0.5), 1, 0, 0, 0, 0);
//                        ParticleUtil.drawBlockParticleLine(block, Particle.DRIP_LAVA, 1.05);

                        entry.getValue().speed();

                    }

                }

            }

        }.runTask(TalexEssential.getInstance());

    }

    @Override
    public void onOpenMachine(PlayerData playerData, PlayerEvent event) {

        if ( !( event instanceof PlayerInteractEvent ) ) {

            return;

        }

        process(event.getPlayer(), (PlayerInteractEvent) event);

    }

    private void process(Player player, PlayerInteractEvent event) {

        if ( event.getAction() != Action.RIGHT_CLICK_BLOCK ) {
            return;
        }

        Block block = event.getClickedBlock();

        String str = NBTsUtil.Location2String(block.getLocation());

        FurnaceCauldronObject obj = map.get(str);

        if ( obj == null ) {

            obj = new FurnaceCauldronObject();

            obj.setBlock(block);

            map.put(str, obj);

        }

        event.setCancelled(true);

        new PlayerUser(player)
                .playSound(Sound.BLOCK_NOTE_BLOCK_GUITAR, 1.2F, 1.2F)
                .infoActionBar("你打开了 冶炼熔炉 !");

        new CauldronGUI(obj).openForPlayer(player);

    }

    @Override
    public boolean onOpenRecipeView(IndicateBook guiderBook) {
        Classifies classifies = guiderBook.getClassifies();

        if ( !(classifies instanceof TechObject techObject) ) return false;

        RecipeObject recipeObject = techObject.getRecipe();

        if ( !(recipeObject instanceof FurnaceCauldronRecipe fcr) ) {

            return false;

        }

        guiderBook.inventoryUI.setItem(22, new InventoryUI.AbstractSuperClickableItem() {

            @Override
            public ItemStack getItemStack() {

                return fcr.getNeed();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {

                deepRecipeView(guiderBook.setClassifies(fcr.getNeed().getOwnCategoryObject()));

                return false;
            }
        });

        ItemBuilder ib = new ItemBuilder(fcr.getExport().clone())
                .addLoreLine("")
                .addLoreLine("§a配方冶炼时间: §e" + new DecimalFormat("##.###").format(fcr.getNeedTime() / 1000f) + "秒 ")
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
    public boolean onBreakMachine(TalexBlock tblock) {
        return false;
    }

    @Override
    public String onSave() {

        YamlConfiguration yaml = new YamlConfiguration();

        for ( Map.Entry<String, FurnaceCauldronObject> entry : map.entrySet() ) {

            FurnaceCauldronObject obj = entry.getValue();

            if ( obj.hologram != null ) {
                obj.hologram.destroy();
            }

            String path = "Objects." + NBTsUtil.getRandomStr(12);

            yaml.set(path + ".key", entry.getKey());
            yaml.set(path + ".data", Base64Encoder.encode(obj.toString()));

        }

        return yaml.saveToString();

    }

    @SneakyThrows
    @Override
    public void onLoad(String str) {
        if ( str == null || str.isEmpty() ) return;

        YamlConfiguration yamlConfiguration = new YamlConfiguration();

        yamlConfiguration.loadFromString(str);

        if ( yamlConfiguration.contains("Objects") ) {

            Set<String> keys = Objects.requireNonNull(yamlConfiguration.getConfigurationSection("Objects")).getKeys(false);

            for ( String key : keys ) {

                String data = yamlConfiguration.getString("Objects." + key + ".data");
                String keyStr = yamlConfiguration.getString("Objects." + key + ".key");

                map.put(keyStr, FurnaceCauldronObject.deserialize(keyStr, data));

            }

        }

    }

    @Override
    public boolean checkMaterials(Player player, PlayerData pd, PlayerUser user) {
        PlayerInventory inventory = player.getInventory();
        if ( inventory.first(Material.CAULDRON) < 1 ) {
            user.playSound(Sound.ENTITY_AXOLOTL_HURT, 1, 1).errorActionBar("你需要 炼药锅 才能完成自动建造!");
            return false;
        }
        if ( inventory.first(Material.LAVA_BUCKET) < 1 ) {
            user.playSound(Sound.ENTITY_AXOLOTL_HURT, 1, 1).errorActionBar("你需要 岩浆桶 才能完成自动建造!");
            return false;
        }

        return true;
    }

    @Override
    public void build(Player player, PlayerData pd, PlayerUser user, Location startLoc) {
        Location loc = startLoc.clone().add(0, 1, 0);
        Location up = loc.clone().add(0, 1, 0);

        if ( loc.getBlock().getType() != Material.AIR || up.getBlock().getType() != Material.AIR ) {
            user.errorActionBar("§c§l无法在这里建造!").playSound(Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
            return;
        }

        if ( !couldPlace(player, loc) || !couldPlace(player, up) ) {
            user.errorActionBar("§c§l无法在这里建造!").playSound(Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
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
                placeBlock(up, Material.CAULDRON).placeBlock(loc, Material.LAVA);
            }
        }, 10);

    }

    @Override
    public void consumeMaterials(Player player, PlayerData pd, PlayerUser user) {
        InventoryUtils.deletePlayerItem(player, Material.CAULDRON, 1);
        InventoryUtils.deletePlayerItem(player, Material.LAVA_BUCKET, 1);

        user.addItem(new ItemStack(Material.BUCKET));
    }

    @Override
    public void onOpenMachineInfoViewer(Player player) {

    }
}
