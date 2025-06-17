package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.soultech.internal.entity.unlock;

import com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import org.bukkit.entity.Player;

public class LevelCondition implements IUnlockCondition {

    private int level;

    public LevelCondition(int level) {
        this.level = level;
    }

    @Override
    public boolean onCheck(Player player, PlayerData pd, PlayerUser user) {
        return player.getLevel() >= level;
    }

    @Override
    public String getDisplay(Player player, PlayerData pd) {
        return "经验等级达到 &l" + level;
    }

    @Override
    public void consume(Player player, PlayerData pd) {
        player.setLevel(player.getLevel() - level);
    }
}
