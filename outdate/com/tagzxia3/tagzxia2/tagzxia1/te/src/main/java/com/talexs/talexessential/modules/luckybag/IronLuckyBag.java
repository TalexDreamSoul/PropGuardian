package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.luckybag;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.luckybag.BaseLuckyBag;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.player.PlayerDataRunnable;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.utils.item.ItemBuilder;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Random;

public class IronLuckyBag extends BaseLuckyBag {

    static Material[] materials = {
            Material.IRON_NUGGET, Material.IRON_INGOT, Material.GOLD_INGOT, Material.DIAMOND,
            Material.GOLDEN_CARROT, Material.ANCIENT_DEBRIS
    };

    static Material[] materials_chores = {
            Material.GUNPOWDER, Material.PRISMARINE_CRYSTALS, Material.RABBIT_HIDE,
            Material.AMETHYST_SHARD
    };

    public static String id = "lucky_bag_iron";

    public IronLuckyBag() {
        super(id, new ItemBuilder(new HeadDatabaseAPI().getItemHead("28584"))
                        .setName("&c儒学龙钟 &8&l· &a&l新春福袋 &8&l· &e晨曦光辉")
                        .setLore("", "&8| &e2024 龙年新辞福利福袋", "&8| &7右键打开福袋 ...", "")
                        .toItemStack());
    }

    public void open(Player player) {
        PlayerUser user = new PlayerUser(player);

        user.reducePlayerHandItem(1);

        double per = Math.random();

        if ( per < 0.25 ) {
            // 随机获取一个材料，数量是长度-index，概率是每增加index，下降10%
            int index = 0;
            while ( Math.random() <= 0.5 && index + 1 < materials.length ) {
                index += 1;
            }

            int amo = (int)(Math.random() * materials.length - index);
            user
                    .playSound(Sound.BLOCK_NOTE_BLOCK_PLING)
                    .addItem(new ItemBuilder(materials[index]).setAmount(amo).toItemStack());

            return;
        }

        if ( per < 0.65 ) {
            // 随机获取一个材料，数量是长度-index，概率是每增加index，下降10%
            int index = 0;
            while ( Math.random() <= 0.3 && index + 1 < materials_chores.length ) {
                index += 1;
            }

            int amo = (int)(Math.random() * materials_chores.length - index);
            user
                    .playSound(Sound.BLOCK_NOTE_BLOCK_FLUTE)
                    .addItem(new ItemBuilder(materials_chores[index]).setAmount(amo).toItemStack());

            return;
        }

        Random random = new Random();

        // exp
        if ( per < 0.8 ) {
            int exp = (int)((Math.random() * 500) * (random.nextDouble() < 0.25 ? -0.25 : 1));

            player.giveExp(Math.abs(exp), exp < 0);

            user.playSound(Sound.BLOCK_ANVIL_HIT);
            return;
        }

        if ( per < 0.85 ) {
            user.triggerDarkness();

            return;
        }

        if ( per < 0.9 ) {
            user.firework();

            return;
        }

        if ( per < 0.95 ) {
            int money = (int)((Math.random() * 400) * (random.nextDouble() < 0.25 ? -0.25 : 1));

            if ( money > 0 )
                TalexEssential.getInstance().getEcon().depositPlayer(player, money);
            else TalexEssential.getInstance().getEcon().withdrawPlayer(player, -money);

            user
                    .infoActionBar("金币变动 " + money)
                    .playSound(Sound.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON);
            return;
        }

        user.delayRun(new PlayerDataRunnable() {
            @Override
            public void run() {
                user.throwHandItem();
            }
        }, 10);
    }

}
