package com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.spec;

import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ResSpecGUI extends MenuBasic {

    private ResSpec resSpec;

    public ResSpecGUI(Player player, ResSpec resSpec) {
        super(player, "&e领衔位标 &8> &e设置", 6);

        this.resSpec = resSpec;
        player.playSound(player, Sound.ENTITY_BAT_TAKEOFF, 1, 1);
    }

    @Override
    public void SetupForPlayer(Player player) {
        new InventoryPainter(this).drawFull().drawBorder();
    }
}
