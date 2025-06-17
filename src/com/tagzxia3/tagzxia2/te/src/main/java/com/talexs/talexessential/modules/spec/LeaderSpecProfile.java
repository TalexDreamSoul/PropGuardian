package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.spec;//package com.talexs.talexessential.modules.spec;
//
//import com.bekvon.bukkit.residence.protection.ClaimedResidence;
//import com.talexs.talexessential.data.PlayerData;
//import com.talexs.talexessential.modules.guider.PlayerProfile;
//import com.talexs.talexessential.utils.inventory.InventoryPainter;
//import com.talexs.talexessential.utils.inventory.InventoryUI;
//import com.talexs.talexessential.utils.inventory.MenuBasic;
//import com.talexs.talexessential.utils.item.ItemBuilder;
//import org.bukkit.Material;
//import org.bukkit.entity.Player;
//import org.bukkit.event.inventory.InventoryClickEvent;
//import org.bukkit.inventory.ItemStack;
//
//import java.util.Map;
//
//public class LeaderSpecProfile extends MenuBasic {
//    public LeaderSpecProfile(Player player) {
//        super(player, "&e领衔位标", 6);
//    }
//
//    @Override
//    public void SetupForPlayer(Player player) {
//        new InventoryPainter(this).drawFull().drawBorder();
//
//        inventoryUI.setItem(4, PlayerProfile.getMainItem(player));
//
//        int slot = 10;
//        for (Map.Entry<String, ResSpec> entry : SpecModule.specs.entrySet()) {
//            ResSpec spec = entry.getValue();
//
//            ClaimedResidence res = spec.getRes();
//            if ( res.isTrusted(player) ) continue;
//
//            inventoryUI.setItem(slot, new InventoryUI.AbstractSuperClickableItem() {
//                @Override
//                public ItemStack getItemStack() {
//
//                    return new ItemBuilder(Material.ENDER_PEARL).setName("&e" + entry.getKey())
//                            .setLore(
//                                    "",
//                                    "&8| &7这是默认领先位标宣传语",
//                                    "&8| &7这是默认领先位标宣传语",
//                                    "&8| &7这是默认领先位标宣传语",
//                                    "",
//                                    "&8| &7累计人气: &c0",
//                                    "&8| &7用户评价: &b♥♥♥♥♥",
//                                    "&8| &7每次收费：&e" + spec.getMoney().getStartPrice(),
//                                    "&8| &7分钟收费：&e" + spec.getMoney().getPricePerMinute(),
//                                    "",
//                                    "&8| &7每次进入缴纳初次金额(维持15min)",
//                                    "&8| &715min 后按每分钟收费",
//                                    "&8| &7在领衔位标内退出自动停止计费",
//                                    "&8| &7金币不足时将会自动踢出领先位标",
//                                    "&8| &e请勿在内长时间挂机，避免过度消耗金币",
//                                    "",
//                                    "&7&ki&e 点击进入此 领先位标 ...",
//                                    ""
//                            )
//                            .toItemStack();
//                }
//
//                @Override
//                public boolean onClick(InventoryClickEvent e) {
//                    PlayerData pd = PlayerData.g(player);
//                    pd.getResSpec().start(player.getGameMode(), player.getLocation(), spec);
//                    return false;
//                }
//            });
//
//            if ( (slot + 2) % 9 == 0 ) slot += 2;
//            else slot += 1;
//
//        }
//    }
//}
