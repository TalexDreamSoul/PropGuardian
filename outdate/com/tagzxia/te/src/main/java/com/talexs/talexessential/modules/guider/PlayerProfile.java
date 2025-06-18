package com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.guider;

import cn.hutool.core.codec.Base32;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.gender.GenderType;
import com.talexs.talexessential.modules.guider.privileges.PrivilegeManager;
import com.talexs.talexessential.modules.rank.RankGUI;
import com.talexs.talexessential.modules.realm.gui.RealmListMenu;
import com.talexs.talexessential.modules.resource.ResGUI;
import com.talexs.talexessential.utils.TotpUtil;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PlayerProfile extends MenuBasic {
    public PlayerProfile(Player player) {
        super(player, "&e个人档案", 6);

        if ( showDetail.getOrDefault(player.getName(), false) )
            inventoryUI.setAutoRefresh(true);
        inventoryUI.setCouldClickPlayerInventory(false);
        player.playSound(player, Sound.ENTITY_BAT_TAKEOFF, 1, 1);
    }

    private static final Map<String, Boolean> showDetail = new HashMap<>();

    private Map<Integer, InventoryUI.ClickableItem> items = new HashMap<>();

    public static InventoryUI.ClickableItem getMainItem(Player player) {
        PlayerData pd = PlayerData.g(player);
        return new InventoryUI.AbstractSuperClickableItem() {

            @Override
            public ItemStack getItemStack() {

                String gender = pd.getInfo().getString("Manifest.Gender.Type", "UNKNOWN");
                GenderType gt = GenderType.valueOf(gender);

                ItemBuilder ib = new ItemBuilder(Material.PLAYER_HEAD)
                        .setSkullOwner(player.getName())
                        .setName("&a" + player.getName())
                        .setLore(
                                "",
                                "&8| &e等级: &a" + player.getLevel(),
                                "&8| &e经验: &a" + player.getExp(),
                                "&8| &e生命值: &a" + player.getHealth(),
                                "&8| &e饱食度: &a" + player.getFoodLevel(),
                                "",
                                "&8| &e性别： &" + gt.getColor() + gt.getDisplayName(),
                                "&8| &e伴侣: &e孤寡",
                                ""
                        );

                if (showDetail.getOrDefault(player.getName(), false)) {

                    ib.addLoreLine("&8| &a验证码：&c" + TotpUtil.totp(Base32.decode(player.getName() + "TalexEssential"),
                            "HmacSHA1"
                            , 30
                            , 0));
                    ib.addLoreLine("&8| &e验证码非常重要，请勿随意泄露！");

                } else ib.addLoreLine("&8| &7右键按钮查看详细信息.");

                ib.addLoreLine("");
                ib.addLoreLine("&8#" + player.getUniqueId());

                return ib
                        .toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                if ( e.isRightClick() ) {
                    showDetail.put(player.getName(), !showDetail.getOrDefault(player.getName(), false));
                    return false;
                }
                new PlayerProfile(player).open();
                return false;
            }
        };
    }

    private void init(Player player) {
        items.put(4, getMainItem(player));

        items.put(28, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.CHEST_MINECART)
                        .setName("&e世界商店")
                        .setLore(
                                "",
                                "&8| &7在这里可以出售，购买物品",
                                "&8| &7还有随机限时刷新的神秘商店",
                                "&8| &7亦或者是容纳所有玩家的世界市场",
                                "",
                                "&8| &e点击进入 ...",
                                ""
                        )
                        .toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                new ShopSelector(player).open();
                return false;
            }
        });

        items.put(29, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.BOOK)
                        .setName("&e通行等级")
                        .setLore(
                                "",
                                "&8| &7在这里快速管理你的通行等级",
                                "&8| &7快速升级，查阅，浏览通行等级",
                                "&8| &7更多待探索...",
                                "",
                                "&8| &e点击进入 ...",
                                ""
                        )
                        .toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                new RankGUI(player).open();
                return false;
            }
        });

        items.put(30, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.ENDER_PEARL)
                        .setName("&e跃迁传送")
                        .setLore(
                                "",
                                "&8| &7去到任何世界，来往任意地方",
                                "&8| &7世界那么大，多出去走走吧！",
                                "&8| &7世界需要随等级逐步解锁",
                                "",
                                "&8| &e点击进入 ...",
                                ""
                        )
                        .toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                new TeleportProfile(player).open();
                return false;
            }
        });

        items.put(31, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.GRASS)
                        .setName("&e资源挑战")
                        .setLore(
                                "",
                                "&8| &7每日更新的资源世界",
                                "&8| &7限时 &c&l1.5倍 &7矿率",
                                "&8| &7更多待探索...",
                                "",
                                "&8| &e点击进入 ...",
                                ""
                        )
                        .toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                new ResGUI(player).open();
                return false;
            }
        });

        items.put(40, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.REPEATER)
                        .setName("&e个人设置")
                        .setLore(
                                "",
                                "&8| &7在这里快速管理你的个人设置",
                                "&8| &7是否启用一些快捷命令或动作",
                                "&8| &7更多待探索...",
                                "",
                                "&8| &a龙年春节新福利，点击尽情探索",
                                "&8| &a新春龙年晨曦照，福袋礼包开放",
                                "",
                                "&8| &e点击进入 ...",
                                ""
                        )
                        .toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                new PersonalSettings(player).open();
                return false;
            }
        });

        inventoryUI.setItem(32, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                ItemBuilder ib = new ItemBuilder(Material.NETHER_STAR)
                        .setName("&f&l综合权益")
                        .setLore(
                                "",
                                "&8| &7这里展示你是否拥有某些权益",
                                "&8| &7");

                boolean admin = player.hasPermission("te.vip.admin");
                boolean pro = player.hasPermission("te.vip.pro");
                boolean plus = player.hasPermission("te.vip.plus");

                PrivilegeManager.list.forEach(p -> {
                    ib.addLoreLine("&8| &7" + p.line(player, pro, plus, admin) + "&r &8(&r" + p.source(player) + "&8)");
                });

                ib.addLoreLine("&8| &7");
                ib.addLoreLine("&8| &3PLUS &e&l限时9.9&8&m14.9&e&l￥/月");
                ib.addLoreLine("&8| &cPRO &e&l限时19.9&8&m29.9&e&l￥/月");
                ib.addLoreLine("&8| &e点击查看详情...");
                ib.addLoreLine("&8| &7");
                ib.addLoreLine("&8| &a您对服务器的赞助至少有70%用于服务器的日常维护");
                ib.addLoreLine("&8| &a您对服务器的赞助至少有20%用于管理员的开发开销");
                ib.addLoreLine("&8| &7");

                return ib.toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                player.chat("/signin gui");
                return false;
            }
        });

//        inventoryUI.setItem(32, new InventoryUI.AbstractSuperClickableItem() {
//            @Override
//            public ItemStack getItemStack() {
//                return new ItemBuilder(Material.JUNGLE_SIGN)
//                        .setName("&e每日签到")
//                        .setLore(
//                                "",
//                                "&8| &7在这里完成每日签到",
//                                "&8| &7每日在线时间必须至少 &b15min",
//                                "&8| &7特殊日期和特殊日周会有额外福利",
//                                "",
//                                "&8| &e点击进入 ...",
//                                ""
//                        )
//                        .toItemStack();
//            }
//
//            @Override
//            public boolean onClick(InventoryClickEvent e) {
//                player.chat("/signin gui");
//                return false;
//            }
//        });

        inventoryUI.setItem(33, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.WOODEN_HOE)
                        .setName("&e个人领域")
                        .setLore(
                                "",
                                "&8| &7在这里快速管理你的个人领域",
                                "&8| &7快速设置权限，删除，赠送领域",
                                "&8| &7更多待探索...",
                                "",
                                "&8| &e点击进入 ...",
                                ""
                        )
                        .toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                new RealmListMenu().open(player);
                return false;
            }
        });

        items.put(34, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.EMERALD)
                        .setName("&e领衔位标&b(&c&lBETA&b)")
                        .setLore(
                                "",
                                "&8| &7这里展示了玩家们的领衔位标",
                                "&8| &7当玩家愿意主动公开自身领地",
                                "&8| &7你可以在这里查找并申请传送",
                                "",
                                "&8| &e测试版 &b仅邀请 &c通行等级 12+ &b的用户参与",
                                "",
                                "&8| &e点击进入 ...",
                                ""
                        )
                        .toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                PlayerUser user = new PlayerUser(player);

                PlayerData pd = PlayerData.g(player);
                int rankLevel = pd.getInfo().getInt("Rank.Level", 0);
                if ( rankLevel < 12 ) {
                    user.errorActionBar("你不是 领衔位标 的受邀用户!").playSound(Sound.BLOCK_ANVIL_FALL, 1, 1);
                    return false;
                }

//                new LeaderSpecProfile(player).open();
                return false;
            }
        });

    }

    private void defaults() {

        items.forEach((slot, item) -> inventoryUI.setItem(slot, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return item.getItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {

                inventoryUI.setItem(4, new InventoryUI.AbstractClickableItem(item.getItemStack()) {
                    @Override
                    public boolean onClick(InventoryClickEvent e) {
                        inventoryUI.getCurrentPage().clear();
                        new InventoryPainter(PlayerProfile.this).drawLineFull(10);

                        defaults();
                        return false;
                    }
                });

                item.onClick(e);

                return false;
            }
        }));

    }

    @Override
    public void SetupForPlayer(Player player) {
        new InventoryPainter(this).drawLineFull(10);

        init(player);

        defaults();
    }
}
