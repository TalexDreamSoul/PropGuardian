package com.tagzxia2.te.src.main.java.com.talexs.talexessential;

import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.rank.RankLevel;
import com.talexs.talexessential.modules.realm.PlayerRealm;
import com.talexs.talexessential.modules.realm.RealmModule;
import com.talexs.talexessential.modules.shop.entity.ShopProduct;
import com.talexs.talexessential.modules.shop.gui.ShopNew;
import com.talexs.talexessential.modules.union.UnionProfileGUI;
import com.talexs.soultech.internal.environment.blood_moon.BloodMoonCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class AdminCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if ( !(sender instanceof Player) ) return false;
        Player player = (Player) sender;
        if ( !player.hasPermission("talex.admin") ) return false;

        if (args != null) {

            // tea shop buy/sell manage
            // tea shop buy/sell price
            if ( args.length == 3 && args[0].equalsIgnoreCase("shop") ) {
                ShopProduct.ProductType productType = ShopProduct.ProductType.valueOf(args[1]);

                if ( args[2].equalsIgnoreCase("manage") ) {
                    new ShopNew(productType).open(player);
                } else {

                    ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
                    if (itemInMainHand.getType() == Material.AIR) return false;

                    ShopProduct shopProduct = ShopProduct.defaultProduct();

                    shopProduct.setPrice(Double.parseDouble(args[2]));
                    shopProduct.setItemStack(itemInMainHand);
                    shopProduct.setTransactionType(productType);

                    shopProduct.update();

                }
            }

            // tea tp 1 2 3
            if ( args.length == 4 && args[0].equalsIgnoreCase("tp") ) {
                int x = Integer.parseInt(args[1]);
                int y = Integer.parseInt(args[2]);
                int z = Integer.parseInt(args[3]);

                player.teleport(player.getWorld().getBlockAt(x, y, z).getLocation());
            }

            // tea realm transmit <player> <owner>
            if ( args.length == 4 && args[0].equalsIgnoreCase("realm") ) {
                if ( !args[1].equalsIgnoreCase("transmit") ) return false;

                String targetName = args[2];
                String realm = args[3];

                PlayerRealm pr = RealmModule.getRealmByName(realm);
                if ( pr == null ) return false;

                Player target = Bukkit.getPlayer(targetName);
                if ( target == null || !target.isOnline() ) return false;

                pr.transmitOwner(target);

                new PlayerUser(target)
                        .playSound(Sound.UI_TOAST_CHALLENGE_COMPLETE, .75f, .25f)
                        .infoActionBar("你有新的领域转让！").sendMessage("你现在拥有了领域： " + realm);
            }

            // tea union
            if ( args.length == 1 && args[0].equalsIgnoreCase("union") ) {
                new UnionProfileGUI(player).open();
            }

            // tea hand
            if ( args.length == 1 && args[0].equalsIgnoreCase("hand") ) {
                ItemStack stack = player.getInventory().getItemInMainHand();
                ItemMeta meta = stack.getItemMeta();
                if ( meta == null ) return false;

                PersistentDataContainer container = meta.getPersistentDataContainer();
                container.getKeys().forEach(key -> {
                    player.sendMessage(key.toString() + " : " + container.get(key, PersistentDataType.STRING));
                });
            }

            if ( args.length == 1 && args[0].equalsIgnoreCase("blood-moon") ) {

                new BloodMoonCreator(player.getWorld()).setStartTime(System.currentTimeMillis()).start();

            }

            // tea rank <player> <level>
            if ( args.length == 3 && args[0].equalsIgnoreCase("rank") ) {
                Player target = player.getServer().getPlayer(args[1]);
                if ( target == null || !target.isOnline() ) return false;

                PlayerData data = PlayerData.g(target);

                int level = Integer.parseInt(args[2]);
                RankLevel rank = RankLevel.values()[level];

                data.getInfo().set("Rank.Level", level);
                data.getInfo().set("Rank.Title", rank.name());

                player.sendMessage("§a设置成功");
            }

        }

        return false;
    }
}
