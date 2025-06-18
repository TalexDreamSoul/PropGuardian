package com.tagzxia.src.main.java.alarm.star.alarmstarsystem.config;

import alarm.star.alarmstarsystem.AlarmStarSystem;
import alarm.star.alarmstarsystem.entity.TalexShop;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TalexShopConfig {
    public static Map<String, TalexShop> shops = new HashMap<>();

    public static TalexShop getShop(String shopOwner) {
        if ( shops.containsKey(shopOwner.toLowerCase()) ) return shops.get(shopOwner.toLowerCase());

        return newShop(shopOwner);
    }

    @SneakyThrows
    public static void saveShop(TalexShop shop) {
        File f = new File(getFile(), shop.getShopOwner() + ".yml");
        if ( !f.exists() ) f.createNewFile();

        YamlConfiguration yaml = shop.serialize();

        yaml.save(f);

//        shops.remove(shop.getShopOwner());
    }

    @SneakyThrows
    private static TalexShop newShop(String owner) {
        File f = new File(getFile(), owner + ".yml");

        if ( !f.exists() ) return TalexShop.create(owner, 0);

        YamlConfiguration yaml = new YamlConfiguration();

        yaml.load(f);

        return TalexShop.deserialize(yaml);
    }

    @Getter
    private static File file;

    public static void __init() {
        file = new File(AlarmStarSystem.getInstance().getDataFolder(), "shops");

        if ( !file.exists() ) {
            file.mkdirs();
        } else {
            File[] files = file.listFiles();

            if ( files == null ) return;

            for ( File f : files ) {
                if ( !f.getName().endsWith(".yml") ) continue;

                String name = f.getName().replace(".yml", "");

                TalexShop shop = newShop(name);

                shops.put(name.toLowerCase(), shop);
            }
        }
    }

}
