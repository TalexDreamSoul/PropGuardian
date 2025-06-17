package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.rank;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public enum RankLevel {

    英(0, Collections.emptyList(), Collections.emptyList()),
    初(10, Arrays.asList("iron_ingot:10", "gold_ingot:10"), Arrays.asList("铁锭", "金锭")),
    逐(50, Collections.singletonList("diamond:3"), Collections.singletonList("钻石")),
    坚(100, Collections.singletonList("obsidian:10"), Collections.singletonList("黑曜石")),
    进(200, Arrays.asList("ancient_debris:1", "blaze_rod:4"), Arrays.asList("远古残骸", "烈焰棒")),
    锐(300, Arrays.asList("end_crystal:1", "crying_obsidian:1"), Arrays.asList("末地水晶", "哭泣的黑曜石")),
    力(500, Arrays.asList("netherite_scrap:2", "amethyst_block:1"), Arrays.asList("下界合金碎片", "紫水晶块")),
    强(1000, Arrays.asList("wither_skeleton_skull:2", "glow_ink_sac:8"), Arrays.asList("凋灵骷髅头颅", "荧光墨囊")),
    雄(3000, Arrays.asList("conduit:1", "amethyst_shard:16"), Arrays.asList("潮涌核心", "紫水晶碎片")),
    霸(5000, Arrays.asList("enchanted_golden_apple:1", "RECOVERY_COMPASS:1"), Arrays.asList("附魔金苹果", "追溯指针")),
    谛(10000, Arrays.asList("END_STONE:256", "SCULK:256", "DEEPSLATE:256"), Arrays.asList("末地石", "幽匿块", "深板岩")),
    阗(30000, Arrays.asList("CALIBRATED_SCULK_SENSOR:32", "COBWEB:256", "PHANTOM_MEMBRANE:256"), Arrays.asList("校频幽匿感测体", "蜘蛛网", "幻翼膜")),
    扆(50000, Arrays.asList("GOAT_HORN:4", "SUSPICIOUS_STEW:8", "DIAMOND_HORSE_ARMOR:4"), Arrays.asList("任意山羊角", "迷之炖菜", "钻石马铠")),
    诡(80000, Arrays.asList("nether_star:8", "dragon_breath:4"), Arrays.asList("下界之星", "龙息")),
    寰(100000, Arrays.asList("RESPAWN_ANCHOR:8", "TNT:256", "ANCIENT_DEBRIS:64"), Arrays.asList("重生锚", "TNT", "远古残骸")),
    埒(300000, Arrays.asList("DIAMOND_ORE:32", "EMERALD_ORE:16"), Arrays.asList("钻石矿", "绿宝石矿")),
    琫(500000, Arrays.asList("SCULK_CATALYST:32", "CHORUS_FLOWER:512"), Arrays.asList("幽匿催发体", "紫颂花")),
    勐(800000,
            Arrays.asList("MUSIC_DISC_13:1", "MUSIC_DISC_CAT:1", "MUSIC_DISC_BLOCKS:1", "MUSIC_DISC_CHIRP:1", "MUSIC_DISC_FAR:1", "MUSIC_DISC_MALL:1", "MUSIC_DISC_MELLOHI:1", "MUSIC_DISC_STAL:1", "MUSIC_DISC_STRAD:1", "MUSIC_DISC_WARD:1", "MUSIC_DISC_11:1", "MUSIC_DISC_WAIT:1", "MUSIC_DISC_OTHERSIDE:1", "MUSIC_DISC_5:1"),
            Arrays.asList("音乐唱片 13", "音乐唱片 cat", "音乐唱片 blocks", "音乐唱片 chirp", "音乐唱片 far", "音乐唱片 mall", "音乐唱片 mellohi", "音乐唱片 stal", "音乐唱片 strad", "音乐唱片 ward", "音乐唱片 11", "音乐唱片 wait", "音乐唱片 otherside", "音乐唱片 5")),
    旃(1000000, Arrays.asList("DRAGON_EGG:1", "dragon_breath:320"), Arrays.asList("龙蛋", "龙息")),
    ;

    private final int money;

    private final List<String> costs, translations;

}
