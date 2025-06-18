package com.tagzxia3.te.src.main.java.com.talexs.talexessential.modules.dropper;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.dropper.DropperWorld;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.IPlayerUser;
import com.talexs.talexessential.data.player.OnlinePlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.BaseModule;
import com.talexs.talexessential.utils.LogUtil;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class DropperModule extends BaseModule {

    private Map<World, com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.dropper.DropperWorld> worldDropperWorldMap = new HashMap<>();

    public DropperModule() {
        super("dropper");
    }

    @EventHandler
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        if ( !worldDropperWorldMap.containsKey(world) ) return;

        new PlayerUser(player)
                .title("&4&l!", "&e世界 &c死亡掉落 &e请小心行事!", 0, 80, 20)
                .actionBar("&c这个世界死亡掉落, 请小心行事!")
                .playSound(Sound.ENTITY_VILLAGER_WORK_FISHERMAN, 1, 1)
        ;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if ( player.hasPermission("te.dropper.admin") ) return;

        com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.dropper.DropperWorld dw = worldDropperWorldMap.get(player.getWorld());
        if ( dw == null ) return;

        OnlinePlayerData pd = PlayerData.g(player);
        String today = Calendar.getInstance().get(Calendar.YEAR) + "-" + Calendar.getInstance().get(Calendar.MONTH) + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        int dayAmo = dw.getDayAmo();
        int maxAmo = (int) (player.hasPermission("te.vip.pro") ? 1.5 : (player.hasPermission("te.vip.plus") ? 1.2 : 1) * dayAmo);

        String path = "Manifest.Dropper." + dw.getWorld().getUID() + "." + today;
        int dropped = pd.getInfo().getInt(path, 0) + 1;

        pd.getInfo().set(path, dropped);

        double cost = dw.getDeathCost(dropped, maxAmo);

        IPlayerUser user = pd.getUser();
        if ( (dropped - 1) < maxAmo ) {

            if ( dropped == maxAmo ) {

                user
                        .sendMessage("")
                        .sendMessage("")
                        .sendMessage("  &8[&c&lDropper&e&l*&4死亡惩罚&8]")
                        .sendMessage("")
                        .sendMessage("  &e请注意&7, 您的今日免费死亡不掉落机会已耗尽!")
                        .sendMessage("  &7您的下一次死亡将会消耗金币! &8(" + dropped + ")")
                        .title("&4&l!", "&e今日死亡不掉落机会已耗尽!", 0, 80, 20)
                        .actionBar("&c你的死亡不掉落机会已耗尽，接下来死亡物品将会掉落！")
                        .playSound(Sound.ENTITY_ENDERMAN_DEATH, 1, 1)
                ;

                if ( maxAmo != dayAmo ) {

                    user.sendMessage("  &2您是会员，享受每日额外的免罚金牌倍率!");

                }

                user.sendMessage("  ")
                        .sendMessage("  &7预计花费 &e&l" + String.format("%.2f", cost) + " &7金币!")
                        .sendMessage("  &b若金币不足, 背包物品将会自动 &c掉落 &b!")
                        .sendMessage("")
                        .sendMessage("");

            } else {
                new PlayerUser(player)
                        .playSound(Sound.ENTITY_PARROT_IMITATE_ENDERMITE, 1, 1)
                        .title("&4&l!", "&e今日免除惩罚机会还剩 &c" + (maxAmo - dropped) + " &e次!", 0, 80, 20);
            }

        } else {

            EconomyResponse economyResponse = TalexEssential.getInstance().getEcon().withdrawPlayer(player, cost);
            if ( economyResponse.transactionSuccess() ) {

                double nextCost = dw.getDeathCost(dropped + 1, maxAmo);

                user
                        .sendMessage("")
                        .sendMessage("")
                        .sendMessage("  &8[&c&lDropper&e&l*&4死亡惩罚&8]")
                        .sendMessage("")
                        .sendMessage("  &e请注意&7, 您的今日免费死亡不掉落机会已耗尽!")
                        .sendMessage("  &7您的下一次死亡将会消耗金币! &8(" + dropped + ")")
                        .sendMessage("  ")
                        .sendMessage("  &7预计花费 &e&l" + String.format("%.2f", nextCost) + " &7金币!")
                        .sendMessage("  &b若金币不足, 背包物品将会自动掉落!")
                        .sendMessage("")
                        .sendMessage("")
                        .title("&a&l✓", "&e已自动消耗金币购买免罚金牌!", 0, 80, 20)
                        .infoActionBar("已自动消耗金币购买免罚金牌!")
                        .playSound(Sound.ENTITY_ENDERMAN_DEATH, 1, 1)
                ;
                return;

            }

            user
                    .sendMessage("")
                    .sendMessage("")
                    .sendMessage("  &8[&c&lDropper&e&l*&4死亡惩罚&8]")
                    .sendMessage("")
                    .sendMessage("  &e请注意&7, 您的今日免费死亡不掉落机会已耗尽!")
                    .sendMessage("  &c您无力支付免罚金牌，已处理惩罚 &8(" + dropped + ")")
                    .sendMessage("  ")
                    .sendMessage("  &7购买花费 &e&l" + String.format("%.2f", cost) + " &7金币!")
                    .sendMessage("  &b金币不足, 背包物品自动掉落!")
                    .sendMessage("")
                    .sendMessage("")
                    .title("&c&l✘", "&e您的财力无法购买免罚金牌!", 0, 80, 20)
                    .errorActionBar("您的财力无法购买免罚金牌!")
                    .playSound(Sound.ENTITY_WANDERING_TRADER_NO, 1, 1)
            ;

            for (ItemStack content : player.getInventory().getContents()) {
                if (content != null && content.getType() != Material.AIR) {
                    player.getWorld().dropItem(player.getLocation(), content);
                }
            }

            player.getInventory().clear();

        }
    }

    @Override
    public void onEnable() {
        Set<String> keys = Objects.requireNonNull(super.yaml.getConfigurationSection("Worlds")).getKeys(false);

        keys.forEach(worldName -> {
            com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.dropper.DropperWorld dw = new DropperWorld(yaml, worldName);
            worldDropperWorldMap.put(dw.getWorld(), dw);
        });

        LogUtil.log("[Dropper] Loaded " + keys + " dropper world(s).");
    }
}
