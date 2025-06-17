package com.tagzxia3.tagzxia1.src.main.java.alarm.star.alarmstarsystem.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bukkit.Location;

@Data
@Accessors(chain = true)
public class AlarmStar {

    private final boolean clear, prison;

    private final Location respawnLoc;

    private final double reserveHealth, pay;

}
