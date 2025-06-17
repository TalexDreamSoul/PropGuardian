package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.luckybag;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.luckybag.BaseLuckyBag;
import com.talexs.talexessential.data.player.PlayerDataRunnable;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.utils.item.ItemBuilder;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Random;

public class CoalLuckyBag extends BaseLuckyBag {

    static Material[] materials = {
            Material.STRING, Material.COAL, Material.GOLD_NUGGET, Material.GOLDEN_CARROT, Material.DIAMOND
    };

    public static String id = "lucky_bag_coal";

    public CoalLuckyBag() {
        super(id, new ItemBuilder(new HeadDatabaseAPI().getItemHead("33848"))
                        .setName("&c龙年新辞 &8&l· &a&l新春福袋 &8&l· &e晨曦祝福")
                        .setLore("", "&8| &e2024 龙年新辞福利福袋", "&8| &7右键打开福袋 ...", "")
                        .toItemStack());
    }

    public void open(Player player) {
        PlayerUser user = new PlayerUser(player);

        user.reducePlayerHandItem(1);

        double per = Math.random();

        if ( per < 0.3 ) {
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

        Random random = new Random();

        // exp
        if ( per < 0.4 ) {
            int exp = (int)((Math.random() * 100) * (random.nextDouble() < 0.5 ? -0.5 : 1));

            player.giveExp(Math.abs(exp), exp < 0);

            user.playSound(Sound.BLOCK_ANVIL_HIT);
            return;
        }

        if ( per < 0.65 ) {
            user.firework();

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
