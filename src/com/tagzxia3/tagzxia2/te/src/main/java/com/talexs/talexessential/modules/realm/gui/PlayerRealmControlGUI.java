package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.realm.gui;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.realm.gui.RealmIconMenu;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.realm.gui.RealmMemberGUI;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.realm.gui.RealmSetsMenu;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.player.PlayerDataRunnable;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.chat.ChatFunction;
import com.talexs.talexessential.modules.guider.PlayerProfile;
import com.talexs.talexessential.modules.realm.PlayerRealm;
import com.talexs.talexessential.modules.realm.RealmModule;
import com.talexs.talexessential.utils.LocationUtil;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.utils.item.ItemBuilder;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import top.zoyn.particlelib.pobject.EffectGroup;
import top.zoyn.particlelib.pobject.Line;

public class PlayerRealmControlGUI extends MenuBasic {

    private PlayerRealm pr;

    public PlayerRealmControlGUI(Player player, PlayerRealm pr) {
        super(player, "&e领域 &8> &e管理", 6);

        this.pr = pr;
        player.playSound(player, Sound.ENTITY_BAT_TAKEOFF, 1, 1);
    }

    @Override
    public void SetupForPlayer(Player player) {
        new InventoryPainter(this).drawLineFull(10);

        inventoryUI.setItem(12, new InventoryUI.EmptyCancelledClickableItem(
           new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDurability((short) 7).setName("§7*").toItemStack()
        ));

        inventoryUI.setItem(4, PlayerProfile.getMainItem(player));

        if ( !player.getUniqueId().toString().equals(pr.getOwnerUUID().toString()) && !player.hasPermission("te.relam.admin") ) return;

        inventoryUI.setItem(21, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.COMPARATOR).setName("§e设置领域权限")
                        .setLore(
                                "",
                                "&8| &7设置此领域的权限",
                                "",
                                "&7&ki&a 点击设置 ...",
                                ""
                        ).toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                new RealmSetsMenu(pr).open(player);
                return false;
            }
        });

        inventoryUI.setItem(22, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.ENDER_PEARL).setName("§e领域传送")
                        .setLore(
                                "",
                                "&8| &7将领地传送设置为当前位置(需在领地内)",
                                "",
                                "&7&ki&a 左键传送 &8| &e右键设置",
                                ""
                        ).toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                player.closeInventory();
                if ( e.isLeftClick() )
                    player.chat("/realm tp " + pr.getName());
                else {
                    Location loc = player.getLocation();
                    if ( !pr.getRealmArea().isInArea(loc) ) {
                        new PlayerUser(player)
                                .playSound(Sound.ENTITY_VILLAGER_NO, 1, 1)
                                .errorActionBar("这个位置不在领地内");
                        return false;
                    }

                    pr.setTeleportLocation(loc);

                    new PlayerUser(player)
                            .playSound(Sound.ENTITY_PLAYER_LEVELUP, 1.2f, 1)
                            .infoActionBar("已设置领地传送点");
                }
                return false;
            }
        });

        inventoryUI.setItem(23, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.NAME_TAG).setName("§e重命名领域")
                        .setLore(
                                "",
                                "&8| &7重命名领域允许你后悔",
                                "&8| &7再次设置你领域的名字",
                                "",
                                "&8| &7每次命名收费 &e19.99 &7金币",
                                "&8| &7命名没有任何间隔或限制.",
                                ""
                        ).toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                player.closeInventory();

                PlayerUser user = new PlayerUser(player);

                user.chatFunc(new ChatFunction() {
                    @Override
                    public void onBefore() {
                        user.closeInventory()
                                .sendMessage("")
                                .sendMessage("")
                                .sendMessage("  &8[&e领域重命名&8]")
                                .sendMessage("  &7你正在进行领域重命名")
                                .sendMessage("  &e请 完整无误 输入领域的新名字")
                                .sendMessage("  &7尽量以英文（拼音）为主，请勿添加特殊字符！")
                                .sendMessage("")
                                .sendMessage("")
                                .playSound(Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    }

                    @Override
                    public void execute(String msg) {

                        if ( RealmModule.getRealmByName(msg) != null ) {
                            user.sendMessage("  &c领域名字已被占用，请重新输入");
                            return;
                        }

                        if ( msg.contains(" ") ) {
                            user.errorActionBar("领域名字不能包含空格.")
                                    .playSound(Sound.ENTITY_VILLAGER_NO, 1, 1)
                            ;
                            return;
                        }

                        EconomyResponse economyResponse = TalexEssential.getInstance().getEcon().withdrawPlayer(player, 19.99);
                        if ( !economyResponse.transactionSuccess() ) {
                            user.playSound(Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 1)
                                    .errorActionBar("金币不足！(" + economyResponse.errorMessage + ")");
                            return;
                        }

                        pr.setName(msg);

                        user.infoActionBar("领域名字已重命名！")
                                .playSound(Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 1);

                    }

                    @Override
                    public void rejected() {
                        user.errorActionBar("领域重命名已取消!")
                                .playSound(Sound.ENTITY_VILLAGER_NO, 1, 1)
                        ;
                    }
                });

                return false;
            }
        });

        inventoryUI.setItem(30, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.CHEST).setName("§e设置领地团队")
                        .setLore(
                                "",
                                "&8| &7点击添加玩家为信任玩家",
                                "&8| &7设置为信任玩家后其默认",
                                "&8| &7拥有完整的使用和管理权",
                                "",
                                "&8| &e点击管理领域",
//                                "&8| &e轻点 &7以 &b暂时 &7送权",
//                                "&8| &eSHIFT点击 &7以 &b永久 &7送权",
                                ""
                        ).toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                new RealmMemberGUI(player, pr).open();
                return false;
            }
        });

        inventoryUI.setItem(31, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(pr.getRealmIcon().getIcon()).setName("§e图标设置")
                        .setLore(
                                "",
                                "&8| &7修改领域的个性图标",
                                "&8| &e领域图标展示个人领域的个性化",
                                "&8| &7您可以随时修改您的领域个人图标",
                                "&8| &7每次切换您都需要重新购买这个图标",
                                "",
                                "&8| &b邀请 &c领域拥有者 &b玩家参与",
                                "",
                                "&7&ki&e 左键打开 ...",
                                ""
                        ).toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                new RealmIconMenu(pr).open(player);
                return false;
            }
        });
        inventoryUI.setItem(39, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(pr.getRealmIcon().getIcon()).setName("§e显示领域大小")
                        .setLore(
                                "",
                                "&8| &7显示你当前领域的大小",
                                "&8| &7点击显示 5秒，只有你可见",
                                "",
                                "&7&ki&e 左键显示 ...",
                                ""
                        ).toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {

                Location[] locs = LocationUtil.fixShafts(pr.getRealmArea().getLoc1(), pr.getRealmArea().getLoc2());
                Line[] lines = LocationUtil.locs2Lines(locs);

                EffectGroup eg = new EffectGroup();

                for (Line line : lines) {
                    eg.addEffect(line);
                }

                final int[] amo = {0};

                new PlayerUser(player).delayRunTimer(new PlayerDataRunnable() {
                    @Override
                    public void run() {
                        if ( amo[0] > 10 ) {
                            cancel();

                            return;
                        }

                        amo[0]++;

                        eg.show();

                    }
                }, 0, 10);

                return false;
            }
        });

//        inventoryUI.setItem(31, new InventoryUI.AbstractSuperClickableItem() {
//            @Override
//            public ItemStack getItemStack() {
//                return new ItemBuilder(Material.JUNGLE_SIGN).setName("§e领衔位标&b(&c&lBETA&b)")
//                        .setLore(
//                                "",
//                                "&8| &7将领地设置为 &e领衔位标",
//                                "&8| &e设置后至少需要 &c3天 &e才可以取消",
//                                "&8| &7设置为领衔位标后领地将向所有人公开",
//                                "&8| &7领衔位标允许你向进入者 &b收费",
//                                "",
//                                "&8| &e测试版 &b仅邀请 &c通行等级 11+ &b的用户参与",
//                                "",
//                                "&7&ki&e 左键管理 | 右键申请 ...",
//                                ""
//                        ).toItemStack();
//            }
//
//            @Override
//            public boolean onClick(InventoryClickEvent e) {
//                PlayerUser user = new PlayerUser(player);
//
//                PlayerData pd = PlayerData.g(player);
//                if ( !SpecModule.instance.doResSpec(res) && e.isRightClick() ) {
//                    int rankLevel = pd.getInfo().getInt("Rank.Level", 0);
//                    if ( rankLevel < 11 ) {
//                        user.errorActionBar("你的通行等级不足以申请领衔位标!").playSound(Sound.BLOCK_ANVIL_FALL, 1, 1);
//                        return false;
//                    }
//
//                    ResSpec rs = new ResSpec(
//                            res, res.getName(), res.getOwner(), res.getOwnerUUID(), ResSpecMoney.FREE
//                    );
//
//                    SpecModule.instance.addResSpec(res, rs);
//                    return false;
//                }
//
//                if (SpecModule.instance.doResSpec(res)) {
//                    new ResSpecGUI(player, SpecModule.instance.getResSpec(res)).open();
//                    return false;
//                }
//
//                return false;
//            }
//        });

        double size = pr.getRealmArea().calcSize();
        double money = calcVault((int) size, pr.getCreated());

        inventoryUI.setItem(32, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.REDSTONE_BLOCK).setName("§c删除领域")
                        .setLore(
                                "",
                                "&8| &c删除这个领域",
                                "&8| &e删除后&l永久&e无法找回，请注意",
                                "&8| &e领域时间越久，退款越少",
                                "&8| &7预计退款：&e" + money,
                                "&8| &7*预计退款仅供参考，具体以实际退款为准",
                                "",
                                "&7&ki&e 点击删除 ...",
                                ""
                        ).toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                player.closeInventory();

                PlayerUser user = new PlayerUser(player);

                pr.delete();

                double size = pr.getRealmArea().calcSize();
                double money = calcVault((int) size, pr.getCreated());

                RealmModule.realms.remove(pr);

                TalexEssential.getInstance().getEcon().depositPlayer(player, money);

                user
                        .title("&7已退回 &b" + (String.format("%.2f", money)), "&e根据圈地时间和大小所计算", 0, 40, 20)
                        .infoActionBar("领域已成功删除！").playSound(Sound.BLOCK_ANVIL_BREAK, 1, 1);
                return false;
            }
        });

    }

    static double calcVault(int size, long createdTime) {
        double returnMoney = size * .75 * 0.03;
        long diff = System.currentTimeMillis() - createdTime;

        int hours = (int) ((diff / 1000) / 3600);

        double rate = 1 - Math.min((hours * 0.005), 0.95);

        return returnMoney * rate;
    }
}
