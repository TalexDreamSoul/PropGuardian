package com.tagzxia3.te.src.main.java.com.talexs.talexessential.modules.luckybag;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.luckybag.BaseLuckyBag;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.luckybag.CopperLuckyBag;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.player.PlayerDataRunnable;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.utils.item.ItemBuilder;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.Random;

public class DiamondLuckyBag extends BaseLuckyBag {

    static Material[] materials = {
            Material.GLOWSTONE, Material.IRON_INGOT, Material.GOLD_INGOT, Material.GOLD_BLOCK,
            Material.NAUTILUS_SHELL, Material.NETHERITE_INGOT, Material.ENCHANTED_GOLDEN_APPLE
    };

    static Enchantment[] enchants = {
            Enchantment.DIG_SPEED, Enchantment.KNOCKBACK, Enchantment.THORNS,
            Enchantment.ARROW_DAMAGE, Enchantment.LOOT_BONUS_BLOCKS
    };

    public static String id = "lucky_bag_diamond";

    public DiamondLuckyBag() {
        super(id, new ItemBuilder(new HeadDatabaseAPI().getItemHead("11625"))
                        .setName("&c钻湘龙奔 &8&l· &a&l新春福袋 &8&l· &e花蓝晨曦")
                        .setLore("", "&8| &e2024 龙年新辞福利福袋", "&8| &7右键打开福袋 ...", "")
                        .toItemStack());
    }

    public void open(Player player) {
        PlayerUser user = new PlayerUser(player);

        if ( !TalexEssential.getInstance().getEcon().has(player, 10000) ) {
            user
                    .playSound(Sound.BLOCK_ANVIL_HIT)
                    .infoActionBar("你需要保证有10000元的福利保证金才能打开这个福袋！");

            return;
        }

        if ( player.getLevel() < 50 ) {
            user
                    .playSound(Sound.BLOCK_ANVIL_HIT)
                    .infoActionBar("你需要保证有50级的福利保证级才能打开这个福袋！");

            return;
        }

        user.reducePlayerHandItem(1);

        double per = Math.random();

        if ( per < 0.4 ) {
            // 随机获取一个材料，数量是长度-index，概率是每增加index，下降10%
            int index = 0;
            while ( Math.random() <= 0.5 && index + 1 < materials.length ) {
                index += 1;
            }

            int amo = (int)(Math.random() * materials.length - index) * 4;
            user
                    .playSound(Sound.BLOCK_NOTE_BLOCK_PLING)
                    .addItem(new ItemBuilder(materials[index]).setAmount(amo).toItemStack());

        }

        Random random = new Random();

        // exp
        if ( per < 0.55 ) {
            int level = (int)((Math.random() * 120) * (random.nextDouble() < 0.65 ? -.5 : 1));

            if ( player.getLevel() > 300 ) {
                level = (int)((Math.random() * 300) * (random.nextDouble() < 0.75 ? -1 : .75));
            }

            player.setLevel(player.getLevel() + level);

            user.playSound(Sound.BLOCK_ANVIL_HIT);
            return;
        }

        if ( per < 0.6 ) {
            // 随机获取一个材料，数量是长度-index，概率是每增加index，下降10%
            int index = 0;
            while ( Math.random() <= 0.3 && index + 1 < enchants.length ) {
                index += 1;
            }

            int amo = (int)(Math.random() * enchants.length - index);
            user
                    .playSound(Sound.BLOCK_NOTE_BLOCK_FLUTE)
                    .addItem(new ItemBuilder(Material.ENCHANTED_BOOK)
                            .addEnchant(enchants[index], amo + 4)
                            .toItemStack());

            return;
        }

        if ( per < 0.65 ) {
            user.lighting(player.getLocation());

            return;
        }

        if ( per < 0.7 ) {
            player.getWorld().createExplosion(player, 0.5f, true, false);

            return;
        }

        if ( per < 0.8 ) {
            user.firework();

            return;
        }

        if ( per < 0.9 ) {
            new CopperLuckyBag().drop2Loc(player.getLocation(), random.nextInt(10) + 1);

            user
                    .title("", "&a&l钻石福袋品质下降！", 0, 20, 15)
                    .playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
            return;
        }

        if ( per < 0.98 ) {
            int money = (int)((Math.random() * 10000) * (random.nextDouble() < 0.65 ? -.75 : 1));

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
