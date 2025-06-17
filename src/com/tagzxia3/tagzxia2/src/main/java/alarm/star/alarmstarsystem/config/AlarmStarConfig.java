package com.tagzxia3.tagzxia2.src.main.java.alarm.star.alarmstarsystem.config;

import alarm.star.alarmstarsystem.entity.AlarmStar;
import alarm.star.alarmstarsystem.entity.ListConfiguration;
import alarm.star.alarmstarsystem.entity.Monster;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlarmStarConfig extends ListConfiguration<AlarmStar> {
    public AlarmStarConfig() {
        super("stars", "Stars");
    }

    @Override
    public AlarmStar load(YamlConfiguration yaml, String path, String key) {
        World w = Bukkit.getWorld(yaml.getString(path + ".respawn.w"));
        Location loc = new Location(w,
                yaml.getInt(path + ".respawn.x"),
                yaml.getInt(path + ".respawn.y"),
                yaml.getInt(path + ".respawn.z")
        );

        return new AlarmStar(
                yaml.getBoolean(path + ".clear"),
                yaml.getBoolean(path +".prison"),
                loc,
                yaml.getDouble(path + ".health"),
                yaml.getDouble(path + ".pay")
        );
    }
}
