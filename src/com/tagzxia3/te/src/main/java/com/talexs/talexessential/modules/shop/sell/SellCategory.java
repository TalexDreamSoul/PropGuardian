package com.tagzxia3.te.src.main.java.com.talexs.talexessential.modules.shop.sell;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SellCategory {
    FLOWER("鲜花"),
    ORE("矿物"),
    FOOD("食物"),
    BUILD("方块"),
    MATERIAL("材料"),
    ALL("所有");

    private final String name;
}
