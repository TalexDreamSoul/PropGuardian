package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.realm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum RealmSets {

    VISIBLE("可视", Material.ENDER_PEARL, Arrays.asList("", "&8| &7领域可视权限", "&8| &7此权限将领域公开至菜单", "&8| &7且允许玩家 &e传送 &7及 &e移动", "")),
    PVP("战斗", Material.DIAMOND_SWORD, Arrays.asList("", "&8| &7领域 PVP 权限", "&8| &7此权限将允许领域内玩家互相攻击", "")),
    MOB("怪物", Material.SPAWNER, Arrays.asList("", "&8| &7领域怪物生成权限", "&8| &7此权限将允许领域内怪物生成", "")),
    ANIMAL("动物", Material.COW_SPAWN_EGG, Arrays.asList("", "&8| &7领域动物移动权限", "&8| &7此权限将允许领域内动物跑出", "")),
    USE("使用", Material.CHEST, Arrays.asList("", "&8| &7领域使用权限", "&8| &7此权限将允许领域内玩家使用任意方块", "")),
    ;

//    private final String flagName;

    private final String name;

    private final Material material;

    private final List<String> lore;

}
