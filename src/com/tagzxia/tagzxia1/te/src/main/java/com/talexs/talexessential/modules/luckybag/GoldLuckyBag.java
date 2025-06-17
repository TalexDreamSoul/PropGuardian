package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.luckybag;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.luckybag.BaseLuckyBag;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.luckybag.DiamondLuckyBag;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.luckybag.IronLuckyBag;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.player.PlayerDataRunnable;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.utils.item.ItemBuilder;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Random;

public class GoldLuckyBag extends BaseLuckyBag {

    static Material[] materials = {
            Material.GOLD_NUGGET, Material.LAPIS_LAZULI, Material.IRON_INGOT,
            Material.GOLD_INGOT, Material.DIAMOND, Material.ANCIENT_DEBRIS,
            Material.NETHERITE_SCRAP
    };

    static Material[] materials_chores = {
            Material.QUARTZ, Material.HONEYCOMB, Material.GOLDEN_APPLE,
            Material.GHAST_TEAR, Material.GOLD_BLOCK
    };

    public static String id = "lucky_bag_gold";

    public GoldLuckyBag() {
        super(id, new ItemBuilder(new HeadDatabaseAPI().getItemHead("44193"))
                        .setName("&c金蚕龙态 &8&l· &a&l新春福袋 &8&l· &e金光晨曦")
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
            while ( Math.random() <= 0.3 && index + 1 < materials.length ) {
                index += 1;
            }

            int amo = (int)(Math.random() * materials.length - index) * 3;
            user
                    .playSound(Sound.BLOCK_NOTE_BLOCK_PLING)
                    .addItem(new ItemBuilder(materials[index]).setAmount(amo).toItemStack());

        }

        if ( per < 0.45 ) {
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
        if ( per < 0.65 ) {
            int exp = (int)((Math.random() * 800) * (random.nextDouble() < 0.25 ? -0.25 : 1));

            player.giveExp(Math.abs(exp), exp < 0);

            user.playSound(Sound.BLOCK_ANVIL_HIT);
            return;
        }

        if ( per < 0.7 ) {
            user.triggerDarkness();

            return;
        }

        if ( per < 0.75 ) {
            user.firework();

            return;
        }

        if ( per < 0.80 ) {
            new IronLuckyBag().drop2Loc(player.getLocation(), random.nextInt(5));

            user
                    .title("", "&f&l金福袋品质下降！", 0, 20, 15)
                    .playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
            return;
        }

        if ( per < 0.9 ) {
            new DiamondLuckyBag().drop2Loc(player.getLocation(), random.nextInt(3) + 1);

            user
                    .title("", "&b&l金福袋品质上升！", 0, 20, 15)
                    .playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
            return;
        }

        if ( per < 0.95 ) {
            int money = (int)((Math.random() * 600) * (random.nextDouble() < 0.5 ? -0.5 : 1));

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
