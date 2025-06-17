package com.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.shop.buy;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BuyOrder {

    MONEY_UP("价格升序"),
    MONEY_DOWN("价格降序"),
    AFFORDABLE("可购买"),
    //        SELL_AMO_UP("出售数量升序"),
//        SELL_AMO_DOWN("出售数量降序"),
    DEFAULT("默认排序");

    private final String name;

}
