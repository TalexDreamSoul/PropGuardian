package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.machine.bsae;

import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface IMachineBuilder {

    boolean checkMaterials(Player player, PlayerData pd, PlayerUser user);

    void build(Player player, PlayerData pd, PlayerUser user, Location startLoc);

    void consumeMaterials(Player player, PlayerData pd, PlayerUser user);

}
