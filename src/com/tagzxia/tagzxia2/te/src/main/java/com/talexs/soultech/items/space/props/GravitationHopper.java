package com.tagzxia2.te.src.main.java.com.talexs.soultech.items.space.props;

import com.talexs.soultech.internal.entity.items.IPersistItem;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.soultech.internal.entity.unlock.RankCondition;
import com.talexs.soultech.items.space.BaseSpaceItem;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.block.TalexBlock;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GravitationHopper extends BaseSpaceItem implements IPersistItem {

    private static List<String> list = new ArrayList<>();

    public GravitationHopper() {
        super("gravitation_hopper", new ItemBuilder(Material.HOPPER)
                .setName("§b引力漏斗")
                .setLore("", "&8| &7自动吸收附近 &b15x9x15 &7区域的物品", "")
                .toItemStack()
        );
    }

    public WorkBenchRecipe getRecipe() {
        return new WorkBenchRecipe(getID(), this)
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.HOPPER))
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.HOPPER))
                .addRequired("space_fire_dust")
                .addRequired(new MineCraftItem(Material.HOPPER))
                .addRequiredNull()
                .addRequired(new MineCraftItem(Material.HOPPER))
                .addRequiredNull()
                ;
    }

    @Override
    public boolean onPlaceItem(PlayerData playerData, BlockPlaceEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();

        String locStr = getLocationString(location);
        if ( !list.contains(locStr) ) {
            list.add(locStr);
        }

        return false;
    }

    @Override
    public boolean onItemBlockBreak(PlayerData playerData, TalexBlock block, BlockBreakEvent event) {
        Block block1 = event.getBlock();

        Hopper hopper = (Hopper) block1.getState();

        for (ItemStack content : hopper.getInventory().getContents()) {
            if ( content == null ) continue;
            block1.getWorld().dropItemNaturally(block1.getLocation(), content);
        }

        Location location = block.getLoc();
        String locStr = getLocationString(location);

        list.remove(locStr);

        return true;
    }

    @Override
    public YamlConfiguration onItemBlocksSave() {
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("GravitationHopper.List", list);
        return yaml;
    }

    @Override
    public void onItemBlocksLoad(YamlConfiguration yaml) {
        list = yaml.getStringList("GravitationHopper.List");

        new BukkitRunnable() {
            @Override
            public void run() {

                new ArrayList<>(list).forEach(locStr -> {
                    Location loc = getLocation(locStr);
                    if ( loc == null ) return;

                    if ( loc.getBlock().getType() != Material.HOPPER ) {
                        list.remove(locStr);
                        return;
                    }

                    process(loc);
                });

            }
        }.runTaskTimer(TalexEssential.getInstance(), 0, 20);
    }

    private void process(@NotNull Location loc) {
        loc.getWorld().getNearbyEntities(loc, 15, 9, 15).forEach(entity -> {
            if ( entity instanceof org.bukkit.entity.Item ) {
                org.bukkit.entity.Item item = (org.bukkit.entity.Item) entity;
                ItemStack stack = item.getItemStack();
                if ( stack.getType() == Material.AIR ) {
                    return;
                }

                item.teleport(loc.clone().add(.5, 1.25, .5));
            }
        });

        loc.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 1, 1);
    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(15));
    }
}
