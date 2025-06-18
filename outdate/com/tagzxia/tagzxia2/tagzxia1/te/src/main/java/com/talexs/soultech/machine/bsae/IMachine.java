package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.machine.bsae;

import com.talexs.soultech.internal.book.IndicateBook;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.utils.block.TalexBlock;
import org.bukkit.event.player.PlayerEvent;

public interface IMachine {

    /**
     * 当打开机器时
     *
     * @param playerData 玩家信息
     * @param event      事件
     */
    void onOpenMachine(PlayerData playerData, PlayerEvent event);

    /**
     * 当查阅配方时
     *
     * @param guiderBook IndicateBook
     *
     * @return 是否允许打开
     */
    boolean onOpenRecipeView(IndicateBook guiderBook);

    /**
     * 当机器保存时
     */
    String onSave();

    /**
     * 当机器加载时
     */
    void onLoad(String str);

    /**
     * 当机器被破坏时（需自行检测并移除）
     */
    boolean onBreakMachine(TalexBlock tblock);

}
