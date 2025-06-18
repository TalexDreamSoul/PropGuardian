package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.residence;//package com.talexs.talexessential.modules.residence;
//
//import com.bekvon.bukkit.residence.protection.ClaimedResidence;
//import com.talexs.talexessential.GlobalListener;
//import com.talexs.talexessential.data.PlayerData;
//import com.talexs.talexessential.data.player.PlayerDataRunnable;
//import com.talexs.talexessential.data.player.PlayerUser;
//import com.talexs.talexessential.base.PlayerPicker;
//import com.talexs.talexessential.modules.spec.ResSpec;
//import com.talexs.talexessential.modules.spec.ResSpecGUI;
//import com.talexs.talexessential.modules.spec.ResSpecMoney;
//import com.talexs.talexessential.modules.spec.SpecModule;
//import com.talexs.talexessential.utils.inventory.InventoryPainter;
//import com.talexs.talexessential.utils.inventory.InventoryUI;
//import com.talexs.talexessential.utils.inventory.MenuBasic;
//import com.talexs.talexessential.utils.item.ItemBuilder;
//import org.bukkit.Material;
//import org.bukkit.Sound;
//import org.bukkit.entity.Player;
//import org.bukkit.event.inventory.InventoryClickEvent;
//import org.bukkit.inventory.ItemStack;
//
//public class PlayerResControlGUI extends MenuBasic {
//
//    private ClaimedResidence res;
//
//    private PlayerResProfileGUI gui;
//
//    public PlayerResControlGUI(Player player, ClaimedResidence res, PlayerResProfileGUI gui) {
//        super(player, "&a领地管理", 6);
//
//        this.res = res;
//        this.gui = gui;
//        player.playSound(player, Sound.ENTITY_BAT_TAKEOFF, 1, 1);
//    }
//
//    @Override
//    public void SetupForPlayer(Player player) {
//        new InventoryPainter(this).drawLineFull(10);
//
//        inventoryUI.setItem(12, new InventoryUI.EmptyCancelledClickableItem(
//           new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDurability((short) 7).setName("§7*").toItemStack()
//        ));
//
//        inventoryUI.setItem(4, PlayerResProfileGUI.getResSymbol(player, gui, res, true));
//
//        inventoryUI.setItem(21, new InventoryUI.AbstractSuperClickableItem() {
//            @Override
//            public ItemStack getItemStack() {
//                return new ItemBuilder(Material.COMPARATOR).setName("§e设置领地权限")
//                        .setLore(
//                                "",
//                                "&8| &7设置此领地的权限",
//                                "",
//                                "&7&ki&a 点击设置 ...",
//                                ""
//                        ).toItemStack();
//            }
//
//            @Override
//            public boolean onClick(InventoryClickEvent e) {
//                player.closeInventory();
//                player.chat("/res set " + res.getResidenceName());
//                return false;
//            }
//        });
//
//        inventoryUI.setItem(22, new InventoryUI.AbstractSuperClickableItem() {
//            @Override
//            public ItemStack getItemStack() {
//                return new ItemBuilder(Material.ENDER_PEARL).setName("§e领地传送")
//                        .setLore(
//                                "",
//                                "&8| &7将领地传送设置为当前位置(需在领地内)",
//                                "",
//                                "&7&ki&a 左键传送 &8| &e右键设置",
//                                ""
//                        ).toItemStack();
//            }
//
//            @Override
//            public boolean onClick(InventoryClickEvent e) {
//                player.closeInventory();
//                if ( e.isLeftClick() )
//                    player.chat("/res tp " + res.getResidenceName());
//                else player.chat("/res tpset " + res.getResidenceName());
//                return false;
//            }
//        });
//
//        inventoryUI.setItem(23, new InventoryUI.AbstractSuperClickableItem() {
//            @Override
//            public ItemStack getItemStack() {
//                return new ItemBuilder(Material.NAME_TAG).setName("§e重命名领地")
//                        .setLore(
//                                "",
//                                "&8| &7点击重命名领地名字",
//                                ""
//                        ).toItemStack();
//            }
//
//            @Override
//            public boolean onClick(InventoryClickEvent e) {
//                player.closeInventory();
//
//                PlayerUser user = new PlayerUser(player)
//                        .closeInventory()
//                        .sendMessage("")
//                        .sendMessage("")
//                        .sendMessage("  &8[&e领地重命名&8]")
//                        .sendMessage("  &7你正在进行领地重命名")
//                        .sendMessage("  &e请 完整无误 输入领地的新名字")
//                        .sendMessage("  &7尽量以英文（拼音）为主，请勿添加特殊字符！")
//                        .sendMessage("")
//                        .sendMessage("")
//                        .playSound(Sound.ENTITY_PLAYER_LEVELUP, 1, 1)
//                        ;
//
//                GlobalListener.chatFuncs.put(player.getName(), (msg) -> {
//
//                    player.chat("/res rename " + res.getResidenceName() + " " + msg);
//
//                    user.playSound(Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 1);
//
//                });
//
//                return false;
//            }
//        });
//
//        inventoryUI.setItem(30, new InventoryUI.AbstractSuperClickableItem() {
//            @Override
//            public ItemStack getItemStack() {
//                return new ItemBuilder(Material.CHEST).setName("§e设置领地团队")
//                        .setLore(
//                                "",
//                                "&8| &7点击添加玩家为信任玩家",
//                                ""
//                        ).toItemStack();
//            }
//
//            @Override
//            public boolean onClick(InventoryClickEvent e) {
//                player.closeInventory();
//
//                new PlayerPicker(player, 0, player1 -> {
//                    player1.closeInventory();
//                    player1.chat("/res padd " + res.getResidenceName() + " " + player1.getName());
//                }).open();
//
//                return false;
//            }
//        });
//
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
//
//        inventoryUI.setItem(32, new InventoryUI.AbstractSuperClickableItem() {
//            @Override
//            public ItemStack getItemStack() {
//                return new ItemBuilder(Material.REDSTONE_BLOCK).setName("§c删除领地")
//                        .setLore(
//                                "",
//                                "&8| &c删除这个领地",
//                                "&8| &e删除后&l永久&e无法找回，请注意",
//                                "",
//                                "&7&ki&e 点击删除 ...",
//                                ""
//                        ).toItemStack();
//            }
//
//            @Override
//            public boolean onClick(InventoryClickEvent e) {
//                player.closeInventory();
//                player.chat("/res remove " + res.getResidenceName());
//                PlayerUser user = new PlayerUser(player);
//
//                user.delayRun(new PlayerDataRunnable() {
//                    @Override
//                    public void run() {
//                        player.chat("/res confirm");
//                        user.infoActionBar("领地已成功删除！").playSound(Sound.BLOCK_ANVIL_BREAK, 1, 1);
//                        gui.open();
//                    }
//                }, 10);
//                return false;
//            }
//        });
//
//    }
//}
