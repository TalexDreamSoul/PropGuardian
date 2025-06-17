package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.soultech.internal.entity.unlock;

import com.tagzxia.te.src.main.java.com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.rank.RankLevel;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Arrays;

@Getter
public class RankCondition implements IUnlockCondition {

    private int level;

    public RankCondition(int level) {
        this.level = level;
    }

    @Override
    public boolean onCheck(Player player, PlayerData pd, PlayerUser user) {
        int rankLevel = pd.getInfo().getInt("Rank.Level", 0);

        return rankLevel >= level;
    }

    @Override
    public String getDisplay(Player player, PlayerData pd) {
        Object[] array = Arrays.stream(RankLevel.values()).toArray();

        RankLevel rank = (RankLevel) array[level];

        return "通行排位达到 &a" + rank.name();
    }

    @Override
    public void consume(Player player, PlayerData pd) {
    }

}
