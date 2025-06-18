package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.trade.sub;

import org.bukkit.inventory.ItemStack;

public interface ITradeItem {

    ItemStack getDisplay();

    void tradeSuccess();

    void tradeFailed();

}
