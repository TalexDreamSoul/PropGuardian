package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.actions;

import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.modules.BaseModule;
import com.talexs.talexessential.modules.attract.AttractGUI;
import com.talexs.talexessential.modules.guider.PlayerProfile;
import com.talexs.talexessential.modules.resource.ResGUI;
import com.talexs.talexessential.modules.resource.ResModule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class HeadCmd extends BaseModule {

    public HeadCmd() {
        super("headcmd");
    }

    @Override
    public void onEnable() {

    }

    @Override
    protected boolean configurable() {
        return false;
    }

    @EventHandler
    public void onShift(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if ( !player.isSneaking() ) return;

        PlayerData pd = PlayerData.g(player);

        long now = System.currentTimeMillis();
        if ( now - pd.getLastPlace() < 1200 ) {
            return;
        }

        // check if player is looking at sky
        Location loc = player.getLocation();
        if ( loc.getPitch() < -60 ) {
            if( loc.getWorld() == ResModule.INS.getWorld() ) new ResGUI(player).open();
           if ( player.getItemInHand().getType() == Material.ENDER_PEARL ) new AttractGUI(player).open();
            else new PlayerProfile(player).open();
        }
    }

}
