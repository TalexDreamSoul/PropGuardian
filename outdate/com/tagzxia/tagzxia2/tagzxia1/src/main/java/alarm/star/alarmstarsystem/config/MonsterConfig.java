package com.tagzxia2.tagzxia1.src.main.java.alarm.star.alarmstarsystem.config;

import alarm.star.alarmstarsystem.entity.ListConfiguration;
import alarm.star.alarmstarsystem.entity.Monster;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonsterConfig extends ListConfiguration<Monster> {

    // TODO make it realise
    public static final int SPAWN_RADIUS = 15;

    public static final Map<Integer, List<Monster>> starMap = new HashMap<>();

    public MonsterConfig() {
        super("monsters", "Monsters");
    }

    @Override
    public Monster load(YamlConfiguration yaml, String path, String key) {
        Monster monster = new Monster(
                yaml.getString(path + ".mob"),
                yaml.getInt(path + ".star"),
                yaml.getInt(path + ".amo"),
                yaml.getInt(path + ".cd")
        );

        List<Monster> arr = starMap.getOrDefault(monster.getStar(), new ArrayList<>());

        arr.add(monster);
        Bukkit.getConsoleSender().sendMessage("§eLoad star " + monster.getStar() + " as monster " + monster.getMob());

        starMap.put(monster.getStar(), arr);

        return monster;
    }

    @Override
    public void save(YamlConfiguration yaml, String path, String key, Monster monster) {

        yaml.set(path + ".mob", monster.getMob());
        yaml.set(path + ".star", monster.getStar());
        yaml.set(path + ".amo", monster.getAmo());
        yaml.set(path + ".cd", monster.getCd());

    }
}
