package com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.parkour;

import com.talexs.soultech.internal.protector.IProtector;
import com.talexs.talexessential.data.PlayerData;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

public class ParkourProtector implements IProtector {

    @Getter
    private final World world;

    protected ParkourProtector(World world) {
        this.world = world;
    }

    @Override
    public boolean shouldLoad() {
        return true;
    }

    @Override
    public boolean checkProtect(PlayerData playerData, PlayerEvent event) {
        Player player = event.getPlayer();

        return player.getWorld() != this.world;
    }

    @Override
    public boolean checkBuild(Player player, Location loc) {

        return player.getWorld() != this.world;
    }

    @Override
    public boolean checkBreak(Player player, Location loc) {

        return player.getWorld() != this.world;
    }
}
