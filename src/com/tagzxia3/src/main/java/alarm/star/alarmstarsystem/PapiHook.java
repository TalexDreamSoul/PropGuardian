package com.tagzxia3.src.main.java.alarm.star.alarmstarsystem;

import alarm.star.alarmstarsystem.config.PlayerDataConfig;
import alarm.star.alarmstarsystem.entity.PlayerData;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PapiHook extends PlaceholderExpansion {

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        PlayerData pd = PlayerDataConfig.getInstance().getPd(player);

        if ( params.equalsIgnoreCase("star_level") ) {
            return String.valueOf(pd.getStarCount());
        }

        return "null";

    }

    @Override
    public @NotNull String getIdentifier() {

        return "alarmstarsystem";
    }

    @Override
    public @NotNull String getAuthor() {

        return "TalexDreamSoul";
    }

    @Override
    public @NotNull String getVersion() {

        return "1.0.0-PLUS";
    }

}
