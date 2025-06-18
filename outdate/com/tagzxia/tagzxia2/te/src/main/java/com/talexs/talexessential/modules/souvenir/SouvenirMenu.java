package com.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.souvenir;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.souvenir.Souvenir;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.souvenir.SouvenirModule;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.base.PlayerPicker;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.utils.item.ItemBuilder;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;

public class SouvenirMenu extends MenuBasic {

    static HeadDatabaseAPI api = new HeadDatabaseAPI();

    public SouvenirMenu(Player player, int start) {
        super(player, "&e纪念品", 6);
    }

    @Override
    public void SetupForPlayer(Player player) {
        new InventoryPainter(this).drawFull().drawBorder();

        PlayerData pd = PlayerData.g(player);

        int slot = 10;
        for (Souvenir souvenir : SouvenirModule.souvenirs) {

            boolean doLock = SouvenirModule.ins.doPlayerUnlock(player, souvenir.getKey()); //pd.getInfo().getBoolean("Souvenirs." + souvenir.getKey() + ".doLock", false);
            inventoryUI.setItem(slot, new InventoryUI.AbstractSuperClickableItem() {
                @Override
                public ItemStack getItemStack() {
                    ItemBuilder ib = new ItemBuilder(api.getItemHead(souvenir.getHeadId())).setName(souvenir.getTitle())
                            .setLore(
                                    "",
                                    "&8| &b个人藏品&7, 是一个纪念系统",
                                    "&8| &7你可以在这里完成成就兑换物品",
                                    "&8| &7每种成就对应物品仅能&c获取一次",
                                    "",
                                    "&8| &7" + souvenir.getDesc().get(0),
                                    "",
                                    "&8| &7藏品类型: &e" + souvenir.getType().name(),
                                    "&8| &7共计 &c" + SouvenirModule.ins.getSouvenirAmount(souvenir.getKey()) + " &7人获得"
                            )
                            ;

                    long time = SouvenirModule.ins.getPlayerUnlockTime(player, souvenir.getKey());

                    if ( time != -1 ) {
                        ib.addLoreLine("&8| &7获得时间: &e" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time));
                    }

                    ib.addLoreLine("");

                    ib.addLoreLine(
                            doLock ? "&8| &a你已解锁这个藏品." : "&8| &e你还未解锁这个藏品."
                    );

                    if ( souvenir.getKey().equals("VETERAN") ) {
                        ib.addLoreLine("");
                        long created = pd.getCreated();

                        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(created);
                        String requireDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(1690819200000L);

                        ib.addLoreLine("&8| &7账户创建时间: &e" + date + " *" + created);
                        ib.addLoreLine("&8| &7要求创建时间: &e" + requireDate + " *1690819200000L");

                        // 小于 2023年8月2日
                        if ( created > 1690819200000L && !doLock ) {
                            ib.addLoreLine("&8| &c你无法领取这个藏品.");
                        } else {
                            ib.addLoreLine("&8| &a点击直接领取.");
                        }
                        ib.addLoreLine("");
                    }

                    if ( player.hasPermission("talexessential.souvenir.bypass") ) {
                        ib.addLoreLine("");
                        ib.addLoreLine("&a左键给予 &8| &e右键获得");
                        ib.addLoreLine("");
                    }

                    return ib.toItemStack();
                }

                @Override
                public boolean onClick(InventoryClickEvent e) {
                    PlayerUser user = new PlayerUser(player);
                    if ( souvenir.getKey().equals("VETERAN") && !doLock ) {
                        long created = pd.getCreated();
                        // 小于 2023年8月2日
                        if ( created > 1690819200000L ) return false;

                        SouvenirModule.ins.addSouvenirPlayer(souvenir.getKey(), player);

                        player.sendMessage(Component.text("§8[§b!§8] §7你解锁了藏品 §r" + ChatColor.translateAlternateColorCodes('&', souvenir.getTitle()) + " §7点击这里查看.")
                                .clickEvent(ClickEvent.runCommand("/te souvenir")));

                        user
                                .infoActionBar("你已解锁了一个藏品！").playSound(Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);

                        open(true);
                        return false;
                    }
                    if ( player.hasPermission("talexessential.souvenir.bypass") ) {

                        if ( e.isLeftClick() ) {
                            new PlayerPicker(player, 0, (target) -> {
                                PlayerData tpd = PlayerData.g(target);

                                boolean tLock = tpd.getInfo().getBoolean("Souvenirs." + souvenir.getKey() + ".doLock", false);
                                if ( tLock ) {
                                    user.closeInventory().errorActionBar("对方已经解锁了这个藏品！");
                                    return;
                                }

                                SouvenirModule.ins.addSouvenirPlayer(souvenir.getKey(), target);

//                                tpd.getInfo().set("Souvenirs." + souvenir.getKey() + ".doLock", true);
//                                tpd.getInfo().set("Souvenirs." + souvenir.getKey() + ".time", System.currentTimeMillis());

                                target.sendMessage(Component.text("§8[§b!§8] §7你解锁了藏品 §r" + ChatColor.translateAlternateColorCodes('&', souvenir.getTitle()) + " §7点击这里查看.")
                                        .clickEvent(ClickEvent.runCommand("/te souvenir")));

                                new PlayerUser(target)
                                        .infoActionBar("你已解锁了一个藏品！").playSound(Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
                            }).open();
                            return false;
                        }

                    }

                    user.errorActionBar("藏品获取暂未开启！");
                    return false;
                }
            });

            if ( (slot + 2 ) % 9 == 0 ) slot += 2;
            else if ( slot >= 44 ) break;
            else slot += 1;

        }

    }
}
