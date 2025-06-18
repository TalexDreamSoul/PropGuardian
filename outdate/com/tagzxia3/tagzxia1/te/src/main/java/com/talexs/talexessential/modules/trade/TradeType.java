package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.trade;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum TradeType {

    ENTITY("生物", Arrays.asList("", "&8| &7与玩家交易生物", ""), Material.SPAWNER),
    RESIDENCE("领地", Arrays.asList("", "&8| &7与玩家交易领地", ""), Material.WOODEN_HOE),
    MONEY("金钱", Arrays.asList("", "&8| &7与玩家交易金钱", ""), Material.GOLD_INGOT),
    LOCATION("位置", Arrays.asList("", "&8| &7与玩家交易位置", ""), Material.COMPASS),
    EXP("经验", Arrays.asList("", "&8| &7与玩家交易经验", ""), Material.EXPERIENCE_BOTTLE);

    private final String name;

    private final List<String> lore;

    private final Material material;

}
