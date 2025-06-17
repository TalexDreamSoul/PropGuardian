package com.tagzxia3.tagzxia2.tagzxia1.src.main.java.alarm.star.alarmstarsystem.entity;

import alarm.star.alarmstarsystem.enums.AreaType;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class Area {

    private AreaType areaType;

    private World world;

    private Location pos1, pos2;

    private AreaData areaData;

    public static class AreaData {

    }

    public boolean isInArea(Location loc) {
        double x1 = pos1.getX(), x2 = pos2.getX();
        double y1 = pos1.getY(), y2 = pos2.getY();
        double z1 = pos1.getZ(), z2 = pos2.getZ();

        double minx = Math.min(x1, x2) - 0.5, maxx = Math.max(x1, x2) + 0.5;
        double miny = Math.min(y1, y2) - 0.5, maxy = Math.max(y1, y2) + 0.5;
        double minz = Math.min(z1, z2) - 0.5, maxz = Math.max(z1, z2) + 0.5;

        double x = loc.getX(), y = loc.getY(), z = loc.getZ();

        return x >= minx && x <= maxx && y >= miny && y <= maxy && z >= minz && z <= maxz;
    }

    public Location getMiddlePoint() {
        return new Location(world, (pos1.getX() + pos2.getX()) / 2, (pos1.getY() + pos2.getY()) / 2, (pos1.getZ() + pos2.getZ()) / 2);
    }

    public static class SafeData extends AreaData {

        @Getter
        private final Map<Integer, Integer> timeMap = new HashMap<>();

    }

    @Data
    @Accessors(chain = true)
    public static class PrisonData extends AreaData {

        private final int bailTime, bailSelfCost, bailOtherCost;

    }

}
