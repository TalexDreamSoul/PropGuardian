package com.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.spec;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@Getter
@AllArgsConstructor
public enum ResSpecMoney {

    FREE("免费", 0, 0, 0, Material.GOLD_NUGGET),
    CHEAP("便宜", 1.2f, 10, 0.01, Material.GOLD_INGOT),
    NORMAL("正常", 6.8f, 100, 0.05, Material.GOLD_ORE),
    EXPENSIVE("稍贵", 14.5f, 300, 0.1, Material.DEEPSLATE_GOLD_ORE),
    VERY_EXPENSIVE("小贵", 36.0f, 500, 0.2, Material.RAW_GOLD_BLOCK),
    EXTREMELY_EXPENSIVE("大贵", 108.8f, 1000, 0.3, Material.GOLD_BLOCK),
    IMPOSSIBLE("贵重", 1888.88f, 1500, 0.5, Material.GOLD_BLOCK);

    private final String displayName;

    private final double pricePerMinute, startPrice, taxPer;

    private final Material material;

}
