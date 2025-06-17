package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.common;

import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerDataRunnable;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.BaseModule;
import com.talexs.talexessential.modules.resource.ResModule;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.inventory.LocationFloat;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.utils.item.ItemBuilder;
import fr.xephi.authme.events.LoginEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class LoginBackModule extends BaseModule {

    private Location loc;

    private String spawn;

    public LoginBackModule() {
        super("loginback");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().teleport(loc);
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        Player player = event.getPlayer();
        PlayerData pd = PlayerData.g(player);

        pd.setDoLogin(true);

        if ( pd.getLastLocation() == null ) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.chat(spawn);
                }
            }.runTaskLater(TalexEssential.getInstance(), 60L);
            return;
        }

        new LoginBackGUI(player).open();

    }

    @Override
    public void onEnable() {

        loc = new Location(
                Bukkit.getWorld(Objects.requireNonNull(yaml.getString("Settings.login.world"))),
                yaml.getDouble("Settings.login.x"),
                yaml.getDouble("Settings.login.y"),
                yaml.getDouble("Settings.login.z"),
                (float) yaml.getDouble("Settings.login.yaw"),
                (float) yaml.getDouble("Settings.login.pitch")
        );

        spawn = yaml.getString("Settings.spawn");

    }

    public class LoginBackGUI extends MenuBasic {

        private int diff = 1000 * 5;

        private final long opened = System.currentTimeMillis();

        private long ready = opened + diff;

        public LoginBackGUI(Player player) {
            super(player, "&e点击 石镐⛏ 取消返回", 5);

            inventoryUI.setAutoRefresh(true);
        }

        @Override
        public void onTryCloseMenu(InventoryCloseEvent e) {
            if ( System.currentTimeMillis() > ready ) return;

            new PlayerUser(player).delayRun(new PlayerDataRunnable() {
                @Override
                public void run() {
                    player.openInventory(e.getInventory());
                }
            }, 10);

        }

        @Override
        public void SetupForPlayer(Player player) {
            inventoryUI.setCanClose(false);
            long now = System.currentTimeMillis();
            double percent = now > ready ? 100 : (now - opened) / (double) diff;

            if ( percent >= 100 ) {
                PlayerData pd = PlayerData.g(player);
                LoginBackGUI.this.inventoryUI.setClosed(true);

                player.closeInventory();

                Location loc = pd.getLastLocation();
                if ( loc.getWorld() == ResModule.INS.getWorld() ) {
                    player.chat(spawn);
                } else player.teleport(loc);
                return;
            }

            new InventoryPainter(this).drawFull().drawBorder()
                    .drawProgressBarHorizontal(
                            3, 7, (1 - percent), LocationFloat.FLOAT_CENTER,
                            new InventoryUI.EmptyCancelledClickableItem(new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName("&e点击 石镐⛏ 取消返回")
                                    .setLore("",
                                            "&8| &7计时完成后将自动传输到登录之前的位置",
                                            "",
                                            "&a点击 石镐⛏ 返回到主城",
                                            "")
                                    .toItemStack()),
                            new InventoryUI.EmptyCancelledClickableItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("&e点击 石镐⛏ 取消返回")
                                    .setLore("",
                                            "&8| &7计时完成后将自动传输到登录之前的位置",
                                            "",
                                            "&a点击 石镐⛏ 返回到主城",
                                            "")
                                    .toItemStack()
                    ))
            ;

            inventoryUI.setItem(13, new InventoryUI.AbstractSuperClickableItem() {
                @Override
                public ItemStack getItemStack() {
                    return new ItemBuilder(Material.STONE_PICKAXE)
                            .setName("&e进入主城").setLore("",
                                    "&8| &7计时完成后将自动传输到登录之前的位置",
                                    "",
                                    "&a点击返回到主城.",
                                    "").toItemStack();
                }

                @Override
                public boolean onClick(InventoryClickEvent e) {
                    LoginBackGUI.this.inventoryUI.setClosed(true);

                    player.closeInventory();

                    player.chat(spawn);

                    return false;
                }
            });
        }
    }
}
