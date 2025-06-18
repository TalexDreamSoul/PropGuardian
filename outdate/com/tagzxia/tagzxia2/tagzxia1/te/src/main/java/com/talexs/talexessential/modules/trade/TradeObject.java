package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.trade;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.trade.TradeType;
import com.talexs.talexessential.modules.trade.sub.ITradeItem;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class TradeObject {

    private Player sender, target;

    private TradeType type;

    private List<ITradeItem> tradesSelf = new ArrayList<>(), tradesTarget = new ArrayList<>();

}
