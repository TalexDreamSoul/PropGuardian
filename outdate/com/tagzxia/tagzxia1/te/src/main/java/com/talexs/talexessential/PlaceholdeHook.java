package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.talexessential;


import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.modules.arena.ArenaModule;
import com.talexs.talexessential.modules.rank.RankLevel;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import java.text.SimpleDateFormat;

public class PlaceholdeHook extends PlaceholderExpansion {
    public PlaceholdeHook() {

    }

    @Override
    public String onRequest(OfflinePlayer p, String params) {
        PlayerData pd = PlayerData.g(p.getName());
        if ( pd == null ) return "NONE PLAYER EXIST";

        if ( params.equalsIgnoreCase("rank_title") )
            return pd.getInfo().getString("Rank.Title", RankLevel.英.name());

        if ( params.equalsIgnoreCase("rank_level") )
            return String.valueOf(pd.getInfo().getInt("Rank.Level", 0));

        if ( params.equalsIgnoreCase("union_name") ) {
            String name = pd.getInfo().getString("Union.name", "");
            if (name.isEmpty()) return "";
            return "§8[§c" + (name.length() > 4 ? name.substring(0, 4) : name) + "§8]";
        }

        if ( params.equalsIgnoreCase("gender_name") ) {
            return pd.getInfo().getString("Manifest.Gender.Name", "未知");
        }

        if ( params.equalsIgnoreCase("gender_color") ) {
            return pd.getInfo().getString("Manifest.Gender.Color", "7");
        }

        if ( params.equalsIgnoreCase("arena_monster_amo") ) {
            return String.valueOf(pd.getInfo().getInt("Arena.Monster.amo", 0));
        }

        if ( params.equalsIgnoreCase("arena_monster_time") ) {
            return String.valueOf(pd.getInfo().getLong("Arena.Monster.time", 0L));
        }

        if ( params.equalsIgnoreCase("arena_monster_time_formatted") ) {
            long time = pd.getInfo().getLong("Arena.Monster.time", 0L);
            return new SimpleDateFormat("mm:ss:SSS").format(time);
        }

        if ( params.equalsIgnoreCase("arena_monster_now_player") ) {
            return ArenaModule.activePlayer == null ? "无" : ArenaModule.activePlayer.getName();
        }

        if ( params.equalsIgnoreCase("arena_monster_now_time") ) {
            if ( !ArenaModule.begin ) return "未开始";

            long diff = System.currentTimeMillis() - ArenaModule.startTime;

            return new SimpleDateFormat("mm:ss:SSS").format(diff);
        }

        return "NO THIS VAR";
    }

    @Override
    public String getIdentifier() {
        return "te";
    }

    @Override
    public String getAuthor() {
        return "TalexDreamSoul";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }
}
