package com.tagzxia3.tagzxia2.tagzxia1.src.main.java.alarm.star.alarmstarsystem.config;

import alarm.star.alarmstarsystem.entity.ListConfiguration;
import alarm.star.alarmstarsystem.entity.Area;
import alarm.star.alarmstarsystem.enums.AreaType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;


public class AreaConfig extends ListConfiguration<Area> {

    // TODO make it realise
    public static int REFRESH_TIME = 900;
    public static String REFRESH_MSG;

    public AreaConfig() {
        super("areas", "Areas");
    }

    @Override
    public void onPreLoadEntries() {

        REFRESH_TIME = getYaml().getInt("Settings.refresh.time");
        REFRESH_MSG = getYaml().getString("Settings.refresh.msg");

    }

    @Override
    public Area load(YamlConfiguration yaml, String path, String key) {
        Area area = new Area()
                .setAreaType(AreaType.valueOf(yaml.getString(path + ".type")))
                .setWorld(Bukkit.getWorld(yaml.getString(path + ".world.name")))
                ;

        Location pos1 = new Location(area.getWorld(), yaml.getInt(path + ".world.pos1.x"), yaml.getInt(path + ".world.pos1.y"), yaml.getInt(path + ".world.pos1.z"));
        Location pos2 = new Location(area.getWorld(), yaml.getInt(path + ".world.pos2.x"), yaml.getInt(path + ".world.pos2.y"), yaml.getInt(path + ".world.pos2.z"));

        area.setPos1(pos1).setPos2(pos2);

        if ( area.getAreaType() == AreaType.SAFE ) {

            Area.SafeData areaData = new Area.SafeData();

            yaml.getConfigurationSection(path + ".stars").getKeys(false).forEach(_key -> {

                areaData.getTimeMap().put(Integer.parseInt(_key), yaml.getInt(path + ".stars." + _key));

            });

            area.setAreaData(areaData);

        } else {

            Area.PrisonData areaData = new Area.PrisonData(
                    yaml.getInt(path + ".bail.time"),
                    yaml.getInt(path + ".bail.self"),
                    yaml.getInt(path + ".bail.other")
            );

            area.setAreaData(areaData);

        }

        return area;
    }

}
