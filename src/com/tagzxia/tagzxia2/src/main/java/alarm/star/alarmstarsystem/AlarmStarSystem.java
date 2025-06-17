package com.tagzxia2.src.main.java.alarm.star.alarmstarsystem;

import alarm.star.alarmstarsystem.cmd.PrisonCmd;
import alarm.star.alarmstarsystem.config.*;
import alarm.star.alarmstarsystem.entity.ListConfiguration;
import alarm.star.alarmstarsystem.entity.PlayerData;
import alarm.star.alarmstarsystem.entity.TalexShop;
import alarm.star.alarmstarsystem.utils.inventory.UIListener;
import com.tagzxia.src.main.java.alarm.star.alarmstarsystem.Commands;
import com.tagzxia.src.main.java.alarm.star.alarmstarsystem.Listeners;
import com.tagzxia.src.main.java.alarm.star.alarmstarsystem.PapiHook;
import lombok.Getter;
import lombok.SneakyThrows;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public final class AlarmStarSystem extends JavaPlugin {

    @Getter
    private static AlarmStarSystem instance;

//    private final TraitInfo traitInfo = net.citizensnpcs.api.trait.TraitInfo.create(ShopAssistant.class);

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        new BukkitRunnable() {
            @Override
            public void run() {

                pluginHooker();

            }
        }.runTask(this);

        saveDefaultConfig();
        monsterConfig = new MonsterConfig().autoExport().load();
        areaConfig = new AreaConfig().autoExport().load();
        alarmStarConfig = new AlarmStarConfig().autoExport().load();
        TalexShopConfig.__init();

//        new PrisonCmd().Register();

        getServer().getPluginManager().registerEvents(new UIListener(), this);
        getServer().getPluginManager().registerEvents(new com.tagzxia.src.main.java.alarm.star.alarmstarsystem.Listeners(), this);
        getServer().getPluginManager().registerEvents(new PlayerDataConfig.ConfigListener(), this);
        getServer().getPluginCommand("alarmstarsystem").setExecutor(new Commands());

        PlaceholderAPI.registerExpansion(papi = new com.tagzxia.src.main.java.alarm.star.alarmstarsystem.PapiHook());
    }

    private PapiHook papi;

    private ListConfiguration monsterConfig;
    private ListConfiguration areaConfig;
    private ListConfiguration alarmStarConfig;

    @Override
    public void onDisable() {
        PlaceholderAPI.unregisterExpansion(papi);
        // Plugin shutdown logic
//        CitizensAPI.getTraitFactory().deregisterTrait(traitInfo);

        for ( TalexShop shop : TalexShopConfig.shops.values() ) {
            if ( shop.getType() == 999 ) continue;
            TalexShopConfig.saveShop(shop);
        }

        PlayerDataConfig.getInstance().saveAll();
    }

    public boolean reloadAllConfig() {
        // save config
        onDisable();

        monsterConfig.load();
        areaConfig.load();
        alarmStarConfig.load();

        reloadConfig();

        return true;
    }

    private void pluginHooker() {

        if ( getServer().getPluginManager().getPlugin("MythicMobs") == null ) {
            getLogger().log(Level.SEVERE, "MythicMobs not found or not enabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if(getServer().getPluginManager().getPlugin("Citizens") == null || !getServer().getPluginManager().getPlugin("Citizens").isEnabled()) {
            getLogger().log(Level.SEVERE, "Citizens 2.0 not found or not enabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        //Register your trait with Citizens.
//        net.citizensnpcs.api.CitizensAPI.getTraitFactory().registerTrait(traitInfo);

        if (!setupEconomy() ) {
            getLogger().log(Level.SEVERE, "Vault not found or not enabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

//        final long[] lastUpdate = {-1};
        AtomicInteger i = new AtomicInteger(0);
        new BukkitRunnable() {
            @Override
            public void run() {

                if ( i.incrementAndGet() > 15 * 60 ) {
                    i.set(0);

                    com.tagzxia.src.main.java.alarm.star.alarmstarsystem.Listeners.playerSafeList.clear();

                    Listeners.electSafeArea();
                }

//                long diff = System.currentTimeMillis() - lastUpdate[0];

//                if ( diff < 1000 * 60 * 60 * 24 ) return;

//                lastUpdate[0] = System.currentTimeMillis();

                // 判断现在是不是 24:00:xx
                if ( System.currentTimeMillis() % (1000 * 60 * 60 * 24) != 0 ) return;

                for ( Player player : getServer().getOnlinePlayers() ) {
//                    PlayerData pd = PlayerDataConfig.getInstance().getPd(player);

                    TalexShop shop = TalexShopConfig.getShop(player.getName());

                    EconomyResponse er = econ.depositPlayer(player, shop.getShopBalance());

                    if ( er.transactionSuccess() ) {
                        shop.setShopBalance(0);
                    } else {
                        getServer().getLogger().warning("EconomyResponse transaction failed: " + er.errorMessage);
                        throw new RuntimeException("EconomyResponse transaction failed: " + er.errorMessage);
                    }

//                    final int[] i = {0};
//                    new ArrayList<>(pd.getMoneyStreams()).forEach(stream -> {
//                        EconomyResponse er = econ.depositPlayer(player, stream.getMoney());
//                        if ( er.transactionSuccess() ) {
//                            pd.getMoneyStreams().remove(i[0]);
//                        } else {
//
//                            getServer().getLogger().warning("EconomyResponse transaction failed: " + er.errorMessage);
//
//                            throw new RuntimeException("EconomyResponse transaction failed: " + er.errorMessage);
//                        }
//
//                        i[0]++;
//                    });

                }

            }
        }.runTaskTimerAsynchronously(this, 0, 20);
    }

    @Getter
    private static Economy econ;

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            throw new RuntimeException("Vault not found.");
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            throw new RuntimeException("Economy not found.");
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    @SneakyThrows
    public void writeByIS(String name) {

        InputStream is = super.getResource(name);

        File file = new File(getDataFolder(), name);

        FileOutputStream fos = new FileOutputStream(file);

        byte[] bytes = new byte[1024];

        int len;

        while( true ) {
            assert is != null;
            if ( ( len = is.read(bytes) ) == -1 ) break;

            fos.write(bytes, 0, len);

        }

        fos.close();

        is.close();

    }
}
