package com.tagzxia3.te.src.main.java.com.talexs.talexessential.modules.environment;

import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.BaseModule;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class VillagerModule extends BaseModule {

    private static NamespacedKey villagerTrades = new NamespacedKey("talexs", "villager_trades");

    public VillagerModule() {
        super("villagers");
    }

    @Override
    public void onEnable() {

    }

    @Override
    protected boolean configurable() {
        return false;
    }

    //    @EventHandler
//    public void onVillagerTrade(VillagerAcquireTradeEvent event) {
//        AbstractVillager entity = event.getEntity();
//
//        PersistentDataContainer persistentDataContainer = entity.getPersistentDataContainer();
//
//        if ( !persistentDataContainer.has(villagerTrades, PersistentDataType.INTEGER) ) {
//            persistentDataContainer.set(villagerTrades, PersistentDataType.INTEGER, 0);
//            return;
//        }
//
//        int amo = persistentDataContainer.get(villagerTrades, PersistentDataType.INTEGER);
//
//        if ( amo >= 5 ) {
//            infectVillager(event.getRecipe()., entity);
//        }
//
//    }

    @EventHandler
    public void onVillagerRescued(PlayerInteractEntityEvent event) {
        Entity rightClicked = event.getRightClicked();
        if ( rightClicked.getType() != EntityType.VILLAGER ) return;

        Player player = event.getPlayer();
        ItemStack stack = player.getInventory().getItem(event.getHand());
        if (stack.getType() != Material.GOLDEN_APPLE) return;

        PersistentDataContainer pdc = rightClicked.getPersistentDataContainer();

        if ( !pdc.has(villagerTrades, PersistentDataType.INTEGER) ) {
            pdc.set(villagerTrades, PersistentDataType.INTEGER, 0);
        }

        int amo = pdc.get(villagerTrades, PersistentDataType.INTEGER);

        if ( amo >= 2 ) {

            if ( Math.random() < 0.4 ) {
                rightClicked.remove();

                new PlayerUser(player).errorActionBar("一个村民不堪重负自杀了！")
                        .playSound(Sound.ENTITY_VILLAGER_DEATH, 1, 1)
                        .playSound(Sound.ENTITY_VILLAGER_AMBIENT, 1, 1);
                return;
            }

            if ( Math.random() < 0.65 ) {
                Villager villager = (Villager) rightClicked;

                villager.shakeHead();

                villager.zombify();

                new PlayerUser(player).errorActionBar("一个村民不堪重负被感染了！")
                        .playSound(Sound.ENTITY_VILLAGER_DEATH, 1, 1)
                        .playSound(Sound.ENTITY_VILLAGER_AMBIENT, 1, 1);
            }

        }
    }

//    @EventHandler
//    public void onVillagerCareerChanged(VillagerCareerChangeEvent event) {
//        if ( Math.random() < 0.8 ) return;
//
//        Villager entity = event.getEntity();
//
//        entity.shakeHead();
//
//        entity.zombify();
//
//        Location location = entity.getLocation();
//
//        location.getWorld().getNearbyPlayers(location, 10).forEach(player -> {
//            new PlayerUser(player).errorActionBar("一个村民不堪重负被感染了！")
//                    .playSound(Sound.ENTITY_VILLAGER_DEATH, 1, 1)
//                    .playSound(Sound.ENTITY_VILLAGER_AMBIENT, 1, 1);
//        });
//
//    }

    @EventHandler
    public void onEntityBreed(EntityBreedEvent event) {
        if ( Math.random() < 0.85 ) return;

        event.setCancelled(true);

    }

    @EventHandler
    public void onItemMend(PlayerItemMendEvent event) {
        Player player = event.getPlayer();
        PlayerData pd = PlayerData.g(player);

        int rankLevel = pd.getInfo().getInt("Rank.Level", 0);

        // 根据 rankLevel 百分比减少经验 | 最低0 减少90% 最高15 减少20%
        int reduce = (int) (Math.random() * (rankLevel * 0.2) + 0.05 * rankLevel);

        event.setRepairAmount((int) ((event.getRepairAmount() - reduce) * 0.95));

    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();

        int onlines = Bukkit.getOnlinePlayers().size();

        // 根据人数来确定概率，不同生物基准不一样 | 人数越多，概率越低
        float probability = (float) ((float) 1 - (onlines * 0.025)) + .1f;

        // iron golem
        if ( entity.getType() == EntityType.IRON_GOLEM ) {
            if ( Math.random() > (.45f + probability) )
                event.setCancelled(true);
        }

        if ( entity.getType() == EntityType.VILLAGER ) {
            if ( Math.random() > (.125f + probability) )
                event.setCancelled(true);
        }

        if ( entity.getType() == EntityType.ENDERMAN ) {
            if ( Math.random() > (.2f + probability) )
                event.setCancelled(true);
        }

    }

}
