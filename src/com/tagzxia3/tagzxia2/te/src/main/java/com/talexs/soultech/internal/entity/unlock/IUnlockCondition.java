package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.soultech.internal.entity.unlock;

import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import org.bukkit.entity.Player;

public interface IUnlockCondition {

    // if false -> forbidden
    boolean onCheck(Player player, PlayerData pd, PlayerUser user);

    String getDisplay(Player player, PlayerData pd);

    void consume(Player player, PlayerData pd);

}
