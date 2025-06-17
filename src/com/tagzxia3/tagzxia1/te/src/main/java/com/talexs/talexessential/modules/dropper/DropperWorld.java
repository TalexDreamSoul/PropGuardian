package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.dropper;

import com.talexs.talexessential.utils.LogUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.MemoryConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Define a world that will drop inventory contents when palyer death.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/21 上午 10:56
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
@Getter
public class DropperWorld {

    private World world;

    private int dayAmo;

    private Map<Integer, Integer> costMapper = new HashMap<>();

    public DropperWorld(MemoryConfiguration config, String worldName) {
        world = Bukkit.getWorld(worldName);
        if ( world == null )
            throw new NullPointerException("Cannot load world \"" + worldName + "\" , dropper ignored.");

        this.dayAmo = config.getInt("Worlds." + worldName + ".dayAmo");

        Set<String> keys = Objects.requireNonNull(config.getConfigurationSection("Worlds." + worldName + ".costs")).getKeys(false);

        for (String key : keys) {

            costMapper.put( Integer.parseInt(key), config.getInt("Worlds." + worldName + ".costs." + key) );

        }
    }

    public double getDeathCost(int amo, int maxAmo) {
        if ( amo < maxAmo ) return 0d;

        double cost = -1;
        Integer thisAmo = amo - maxAmo;

        while ( cost == -1 && thisAmo > 0 ) {

            if ( costMapper.containsKey(thisAmo) ) {
                cost = costMapper.get(thisAmo);
            } else {
                thisAmo--;
            }

        }

        return cost + getDeathCost(amo - 1, maxAmo);
    }

}
