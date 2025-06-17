package com.tagzxia3.te.src.main.java.com.talexs.talexessential.modules.attract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum AttractAddon {
    EXPANSION(Material.ENDER_PEARL, Material.PURPLE_STAINED_GLASS_PANE, "&e扩张", Arrays.asList("", "&8| &7全方位的范围引力, 结界范围", "&8| &7内的所有物品都会被吸引到你的身边", "", "&7等级: &e%level%", "&7范围: &e%value%", "")),
    COOLDOWN(Material.CLOCK, Material.YELLOW_STAINED_GLASS_PANE, "&b奇点", Arrays.asList("", "&8| &7缩短每次使用后需要等待一段时间", "", "&7等级: &e%level%", "&7冷却: &e%value%", "")),
    PARTICLES(Material.NETHER_STAR, Material.BLUE_STAINED_GLASS_PANE, "&a粒芒", Arrays.asList("", "&8| &7使用时会产生不同的粒子效果", "", "&7等级: &e%level%", "&7粒子: &e%value%", "")),
    SOUND(Material.NOTE_BLOCK, Material.PINK_STAINED_GLASS_PANE, "&d音波", Arrays.asList("", "&8| &7使用时会产生不同的音效", "", "&7等级: &e%level%", "&7音效: &e%value%", ""));

    private final Material material, unlockMaterial;

    private final String name;

    private final List<String> lore;
}
