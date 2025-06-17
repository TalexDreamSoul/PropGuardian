package com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.union;

import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.IPlayerUser;
import com.talexs.talexessential.data.player.OnlinePlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.chat.ChatFunction;
import com.talexs.talexessential.modules.guider.PlayerProfile;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.player.InventoryUtils;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class UnionControlGUI extends MenuBasic {

    private UnionData ud;

    public UnionControlGUI(Player player, UnionData ud) {
        super(player, "&a联盟管理", 6);

        this.ud = ud;
        player.playSound(player, Sound.ENTITY_BAT_TAKEOFF, 1, 1);
    }

    @Override
    public void SetupForPlayer(Player player) {
        new InventoryPainter(this).drawLineFull(10);

        inventoryUI.setItem(14, new InventoryUI.EmptyCancelledClickableItem(
           new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDurability((short) 7).setName("§7*").toItemStack()
        ));

        inventoryUI.setItem(4, PlayerProfile.getMainItem(player));

        if ( ud == null ) {
            inventoryUI.setItem(31, new InventoryUI.AbstractSuperClickableItem() {
                @Override
                public ItemStack getItemStack() {
                    return new ItemBuilder(Material.ENDER_PEARL).setName("§e创建联盟")
                            .setLore(
                                    "",
                                    "&8| &7创建一个新的联盟",
                                    "",
                                    "&7消耗：",
                                    "&7  - &e15,000 金币",
                                    "&7  - &e谛(10) 通行等级",
                                    "&7  - &ex160 末影珍珠",
                                    "",
                                    "&7&ki&a 点击创建 ...",
                                    ""
                            ).toItemStack();
                }

                @Override
                public boolean onClick(InventoryClickEvent e) {
                    OnlinePlayerData pd = PlayerData.g(player);
                    IPlayerUser user = pd.getUser();
                    user.chatFunc(new ChatFunction() {
                        @Override
                        public void execute(String msg) {

                            int rankLevel = pd.getInfo().getInt("Rank.Level", 0);
                            if ( rankLevel < 10 ) {
                                user.errorActionBar("请提升您的通行等级！")
                                        .playSound(Sound.BLOCK_ANVIL_STEP, 1f ,1f);
                                return;
                            }

                            int amo = InventoryUtils.getPlayerItemInInventory(player, Material.ENDER_PEARL);
                            if ( amo < 160 ) {
                                user.errorActionBar("您没有足够的末影珍珠！")
                                        .playSound(Sound.BLOCK_ANVIL_STEP, 1f ,1f);
                                return;
                            }

                            if ( msg.equalsIgnoreCase("取消") ) {
                                user.infoActionBar("您取消了创建联盟！")
                                        .playSound(Sound.BLOCK_ANVIL_STEP, 1f ,1f);
                                return;
                            }

                            if (msg.isEmpty() || msg.length() > 10 ) {
                                user.errorActionBar("联盟名字应在 1-10 个字符！")
                                        .playSound(Sound.BLOCK_ANVIL_STEP, 1f ,1f);
                                return;
                            }

                            if ( msg.contains("会") || msg.contains("公会") || msg.contains("工会") ) {
                                user.errorActionBar("非法的联盟名字！")
                                        .playSound(Sound.BLOCK_ANVIL_STEP, 1f ,1f);
                                return;
                            }

                            if ( player.getLevel() < 30 ) {
                                user.errorActionBar("您不是 联盟模式 的受邀用户！")
                                        .playSound(Sound.BLOCK_ANVIL_STEP, 1f ,1f);
                                return;
                            }

                            EconomyResponse economyResponse = TalexEssential.getInstance().getEcon().withdrawPlayer(player, 15000);
                            if ( !economyResponse.transactionSuccess() ) {
                                user.errorActionBar("您没有足够的金币！(" + economyResponse.errorMessage + ")")
                                        .playSound(Sound.BLOCK_ANVIL_STEP, 1f ,1f);
                                return;
                            }

                            InventoryUtils.deletePlayerItem(player, Material.ENDER_PEARL, 160);

                            UnionData ud = new UnionData(player);
                            ud.setName(msg);

                            ud.save();

                            pd.getInfo().set("Union.UUID", ud.getUuid().toString());
                            pd.getInfo().set("Union.name", ud.getName());

                            user.infoActionBar("您成功创建了联盟！")
                                    .playSound(Sound.ENTITY_PLAYER_LEVELUP, 1f ,1f)
                                    .playSound(Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1)
                                    .firework()
                                    .playSound(Sound.ENTITY_PLAYER_LEVELUP, 1f ,1f);

                            UnionControlGUI.this.ud = ud;
                            open(true);

                        }

                        @Override
                        public void rejected() {

                            user.infoActionBar("联盟创建已取消！")
                                    .playSound(Sound.BLOCK_ANVIL_STEP, 1f ,1f);

                        }

                        @Override
                        public void onBefore() {
                            user.closeInventory()
                                    .infoActionBar("在聊天栏完整输入联盟名字!")
                                    .sendMessage("")
                                    .sendMessage("")
                                    .sendMessage("  &8[&b联盟&8✦&eUnion&8]")
                                    .sendMessage("  &7你正在创建一个新的联盟")
                                    .sendMessage("  &7请输入 &e联盟名字 &7(输入 &c取消 &7取消创建)")
                                    .sendMessage("  &e联盟名字 &7应在 1-10 个字符")
                                    .sendMessage("  &e联盟名字 &7只会展示 &c前4个字符")
                                    .sendMessage("")
                                    .sendMessage("")
                                    .playSound(Sound.ENTITY_PLAYER_LEVELUP, .5f, 1)
                            ;
                        }
                    });

                    return false;
                }
            });
            return;
        }

        if ( !ud.isExist() ) {
            inventoryUI.setItem(31, new InventoryUI.AbstractSuperClickableItem() {
                @Override
                public ItemStack getItemStack() {
                    return new ItemBuilder(Material.BARRIER).setName("§c联盟已被解散")
                            .setLore(
                                    "",
                                    "&8| &7你之前创建过联盟",
                                    "&8| &7由于联盟被解散，您失去了联盟模式的资格",
                                    "&8| &7请等待重新受邀联盟模式！",
                                    "",
                                    "&8| &7如果你不想保留联盟数据",
                                    "&8| &7请再次点击，将重新邀请",
                                    "",
                                    "&7&ki &e彻底解散联盟",
                                    ""
                            ).toItemStack();
                }

                @Override
                public boolean onClick(InventoryClickEvent e) {
                    ud.delUnion();

                    new PlayerUser(player).infoActionBar("你已彻底解散你的联盟。")
                            .closeInventory()
                            .playSound(Sound.ENTITY_WANDERING_TRADER_YES, 1f ,1f);

                    Bukkit.broadcastMessage("§8[§c!§8] §e" + player.getName() + " §7彻底解散了他的联盟 §b" + ud.getName());
                    return false;
                }
            });
            return;
        }

        inventoryUI.setItem(21, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.COMPARATOR).setName("§e设置联盟权限")
                        .setLore(
                                "",
                                "&8| &7设置联盟的权限",
                                "",
                                "&7&ki&a 点击设置 ...",
                                ""
                        ).toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                player.closeInventory();
                return false;
            }
        });

        inventoryUI.setItem(22, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.ENDER_PEARL).setName("§e联盟传送")
                        .setLore(
                                "",
                                "&8| &7将联盟传送设置为当前位置(需在领地内)",
                                "",
                                "&7&ki&a 左键传送 &8| &e右键设置",
                                ""
                        ).toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                player.closeInventory();
                return false;
            }
        });

        inventoryUI.setItem(23, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.NAME_TAG).setName("§e重命名联盟")
                        .setLore(
                                "",
                                "&8| &7暂不开放",
                                ""
                        ).toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                player.closeInventory();
                return false;
            }
        });

        inventoryUI.setItem(30, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                ItemBuilder ib = new ItemBuilder(Material.CHEST)
                        .setAmount(ud.getMembers().size())
                        .setName("§e设置联盟团队")
                        .setLore(
                                "",
                                "&8| &7在这里, 管理你的联盟团队",
                                "&8| &7每个联盟团队基础容纳 10 人",
                                "",
                                "&7团队列表："
                        );

                ud.getMembers().forEach(member -> {
                    String name = member.getName();
                    Player player = Bukkit.getPlayer(name);

                    if ( player != null && player.isOnline() ) {
                        ib.addLoreLine("&8● &b" + name);
                    } else
                        ib.addLoreLine("&8○ &7" + name);
                });

                ib.addLoreLine("");

                return ib.toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                new UnionMembersGUI(player, ud).open();
                return false;
            }
        });

        inventoryUI.setItem(31, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.PAPER).setName("§e联盟日志")
                        .setLore(
                                "",
                                "&8| &c查阅联盟日志",
                                "&8| &e成员变更，权限变更",
                                "",
                                "&7&ki&e 点击查看 ...",
                                ""
                        ).toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                player.closeInventory();
                return false;
            }
        });

        inventoryUI.setItem(32, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                ItemBuilder ib = new ItemBuilder(Material.REDSTONE_BLOCK).setName("§c删除联盟")
                        .setLore(
                                "",
                                "&8| &c删除这个联盟",
                                "&8| &e删除后&l永久&e无法找回，请注意",
                                "");

                ib.addLoreLine(
                        doDisSolution ? "&7&k&i&e 按下 SHIFT 再次点击 &c确认解散" : "&7&ki&e 点击解散 ..."
                );

                ib.addLoreLine("");

                if ( doDisSolution ) {
                    ib.addEnchant(Enchantment.ARROW_FIRE, 1).addFlag(ItemFlag.HIDE_ENCHANTS);
                }

                return ib.toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                UnionMember um = ud.getMember(player);
                if ( um == null ) return false;

                if ( um.getPermission() != MemberPermission.OWNER ) {
                    new PlayerUser(player).errorActionBar("你不可以这么做。")
                            .playSound(Sound.BLOCK_ANVIL_STEP, 1f ,1f);
                    return false;
                }

                if ( ud.getMembers().size() > 1 ) {
                    new PlayerUser(player).errorActionBar("还有其他成员在联盟中，无法解散联盟。")
                            .playSound(Sound.BLOCK_ANVIL_STEP, 1f ,1f);
                    return false;
                }

                if ( !doDisSolution ) {
                    doDisSolution = true;
                    open(true);
                    new PlayerUser(player).errorActionBar("你正在解散联盟，退出菜单取消解散。")
                            .playSound(Sound.BLOCK_ANVIL_STEP, 1f ,1f);
                    return false;
                }

                player.closeInventory();

                ud.disSolution();

                PlayerData pd = PlayerData.g(player);

                pd.getInfo().set("Union.UUID", "");
                pd.getInfo().set("Union.name", "");

                new PlayerUser(player).errorActionBar("你已成功解散你的联盟。")
                        .playSound(Sound.BLOCK_ANVIL_STEP, 1f ,1f);

                return false;
            }
        });

    }

    private boolean doDisSolution = false;
}
