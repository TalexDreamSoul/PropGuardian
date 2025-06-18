package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.residence;//package com.talexs.talexessential.modules.residence;
//
//import com.bekvon.bukkit.residence.api.ResidenceApi;
//import com.bekvon.bukkit.residence.containers.ResidencePlayer;
//import com.bekvon.bukkit.residence.protection.ClaimedResidence;
//import com.bekvon.bukkit.residence.protection.ResidenceManager;
//import com.talexs.talexessential.data.PlayerData;
//import com.talexs.talexessential.modules.guider.PlayerProfile;
//import com.talexs.talexessential.modules.rank.RankGUI;
//import com.talexs.talexessential.modules.rank.RankLevel;
//import com.talexs.talexessential.utils.inventory.InventoryPainter;
//import com.talexs.talexessential.utils.inventory.InventoryUI;
//import com.talexs.talexessential.utils.inventory.MenuBasic;
//import com.talexs.talexessential.utils.item.ItemBuilder;
//import org.bukkit.Material;
//import org.bukkit.Sound;
//import org.bukkit.enchantments.Enchantment;
//import org.bukkit.entity.Player;
//import org.bukkit.event.inventory.InventoryClickEvent;
//import org.bukkit.inventory.ItemFlag;
//import org.bukkit.inventory.ItemStack;
//import org.checkerframework.checker.units.qual.C;
//
//import java.text.SimpleDateFormat;
//import java.util.List;
//
//public class PlayerResProfileGUI extends MenuBasic {
//    public PlayerResProfileGUI(Player player) {
//        super(player, "&e领地一览", 6);
//
//        player.playSound(player, Sound.ENTITY_BAT_TAKEOFF, 1, 1);
//    }
//
//    public static InventoryUI.ClickableItem getResSymbol(Player player, PlayerResProfileGUI gui, ClaimedResidence res, boolean back) {
//        return new InventoryUI.AbstractSuperClickableItem() {
//            @Override
//            public ItemStack getItemStack() {
//                long createdTime = res.getCreateTime();
//
//                String created = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createdTime);
//
//                ItemBuilder ib = new ItemBuilder(Material.GRASS_BLOCK).setName("§a" + res.getResidenceName())
//                        .setLore(
//                                "§8#" + res.getOwnerUUID(),
//                                "",
//                                "&8| &7大小: &e" + res.getTotalSize(),
//                                "§8| &7创建: &e" + created,
//                                "&8| &7玩家: ",
//                                "    &7- &e" + player.getName()
//                        );
//
//                res.getPlayersInResidence().forEach(p -> {
//                    ib.addLoreLine("    &7- &e" + p.getName());
//                });
//
//                ib.addLoreLine("");
//                ib.addLoreLine("&7&ki&a 点击管理 ...");
//                ib.addLoreLine("");
//
//                return ib.toItemStack();
//            }
//
//            @Override
//            public boolean onClick(InventoryClickEvent e) {
//                if ( back ) {
//                    gui.open(true);
//                    player.playSound(player, Sound.ENTITY_BAT_TAKEOFF, 1, 1);
//                }
//                else new PlayerResControlGUI(player, res, gui).open();
//                return false;
//            }
//        };
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
//        inventoryUI.setItem(4, PlayerProfile.getMainItem(player));
//
//        ResidencePlayer residencePlayer = ResidenceApi.getPlayerManager().getResidencePlayer(player.getName());
//
//        List<ClaimedResidence> resList = residencePlayer.getResList();
//
//        int startSlot = 20;
//
//        for (ClaimedResidence res : resList ) {
//
//            inventoryUI.setItem(startSlot, getResSymbol(player, this, res, false));
//
//            if ( (startSlot + 3) % 9 == 0 ) startSlot += 5;
//            else startSlot += 1;
//
//        }
//
//    }
//}
