package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.resources.de.soultech.inventory.guider;

import lombok.Getter;
import org.bukkit.entity.Player;
import com.talexs.talexessential.TalexEssential;
import pubsher.talexsoultech.entity.PlayerData;
import pubsher.talexsoultech.inventory.MenuBasic;
import pubsher.talexsoultech.talex.BaseTalex;

public abstract class BaseGuider extends MenuBasic {

    @Getter
    private PlayerData activePlayerData;

    public BaseGuider(PlayerData activePlayerData, String title, Integer rows) {

        super(activePlayerData.getPlayer(), TalexEssential.getInstance().getPrefix() + " §8> " + title, rows);

        this.activePlayerData = activePlayerData;

    }

    @Override
    public void Setup() {

        SetupForPlayer(player, getActivePlayerData(player));

    }

    @Override
    public void SetupForPlayer(Player player) {

        SetupForPlayer(player, getActivePlayerData(player));

    }

    @Override
    public void onlyOpen() {

        onlyOpenForPlayer(activePlayerData, false);

    }

    @Override
    public void onlyOpenForPlayer(Player player) {

        onlyOpenForPlayer(getActivePlayerData(player), true);

    }

    public abstract void SetupForPlayer(Player player, PlayerData playerData);

    public void onlyOpenForPlayer(PlayerData playerData, boolean bypass) {

        this.onlyOpenForPlayer(playerData.getPlayer(), bypass);

    }

    public PlayerData getActivePlayerData(Player player) {

        this.activePlayerData = BaseTalex.getInstance().getPlayerManager().get(player.getName());

        return this.activePlayerData;

    }

}
