package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.internal.entity.unlock;

import com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import org.bukkit.entity.Player;

public class ExpCondition implements IUnlockCondition {

    private int exp;

    public ExpCondition(int exp) {
        this.exp = exp;
    }

    @Override
    public boolean onCheck(Player player, PlayerData pd, PlayerUser user) {
        return player.getTotalExperience() >= exp;
    }

    @Override
    public String getDisplay(Player player, PlayerData pd) {
        return "经验需求 &l" + exp;
    }

    @Override
    public void consume(Player player, PlayerData pd) {
        player.setTotalExperience(player.getTotalExperience() - exp);
    }
}
