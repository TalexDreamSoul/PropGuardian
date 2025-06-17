package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.data.player;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.IPlayerUser;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author TalexDreamSoul
 */
@Accessors( chain = true )
@Getter
@Setter
public class OnlinePlayerData extends PlayerData {

    private final Player player;

    private MenuBasic lastGuider;

    private IPlayerUser user;

    @SneakyThrows
    public OnlinePlayerData(@NotNull Player player) {
        super(player.getName());

        this.player = player;
        this.user = new PlayerUser(player);

        /*for ( Player player : Bukkit.getOnlinePlayers() ) {

            player.sendActionBar("§8§l▸ §e" + OnlinePlayerData.this.player.getName() + " §a加入了游戏!");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_FLUTE, 1.1F, 1.1F);

        }*/

//        map.put(super.getName(), this);

    }

    @Override
    public int hashCode() {

        return this.player.hashCode();

    }

    @Override
    public boolean equals(Object obj) {

        if ( !( obj instanceof OnlinePlayerData) ) {
            return false;
        }

        OnlinePlayerData onlinePlayerData = (OnlinePlayerData) obj;

        return onlinePlayerData.hashCode() == this.hashCode();

    }

}
