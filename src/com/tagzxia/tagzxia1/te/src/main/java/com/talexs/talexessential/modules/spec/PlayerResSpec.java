package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.spec;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.spec.ResSpec;
import com.talexs.talexessential.data.PlayerData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
@Setter
public class PlayerResSpec {

    private final PlayerData pd;

    private com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.spec.ResSpec spec;

    private long startTime, lastTime = -1;

    private Location startLoc;

    private GameMode gm;

    public PlayerResSpec(PlayerData pd) {

        this.pd = pd;

    }

    public void save() {

        leave("离开游戏");

    }

    public void start(GameMode gm, Location loc, ResSpec spec) {

        this.spec = spec;
        this.startTime = System.currentTimeMillis();

        this.startLoc = loc;
        this.gm = gm;

    }

    public void leave(String message) {
        if ( startLoc == null ) return;

        if ( pd.getOfflinePlayer().isOnline() ) {
            Player player = pd.getOfflinePlayer().getPlayer();

            assert player != null;
            player.teleport(startLoc);
            player.setGameMode(gm);

            player.sendMessage("§a你已经离开了§e" + spec.getResName() + "§a领衔位标 (" + message + ")");
        }

        spec = null;
        startTime = -1;
        lastTime = -1;
        startLoc = null;
        gm = null;

    }

}
