package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.data;

import cn.hutool.core.codec.Base64;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.builder.*;
import com.talexs.talexessential.data.player.OnlinePlayerData;
import com.talexs.talexessential.data.player.PlayerDataRunnable;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.attract.PlayerAttractData;
import com.talexs.talexessential.modules.resource.PlayerRes;
import com.talexs.soultech.internal.PlayerSoul;
import com.talexs.talexessential.utils.LogUtil;
import com.talexs.talexessential.utils.item.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Getter
public class PlayerData {

    public static void injectTable() {
        // auto gen table
        SqlTableBuilder stb = new SqlTableBuilder()
                .setTableName("player_data")
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull("null").setMain(true).setSubParamName("te_uuid").setType("VARCHAR(64)"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull(null).setMain(false).setSubParamName("te_name").setType("VARCHAR(32)"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull(null).setMain(false).setSubParamName("te_level").setType("INT(11)"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull(null).setMain(false).setSubParamName("te_exp").setType("INT(11)"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull(null).setMain(false).setSubParamName("te_dummy").setType("DOUBLE"))
                // timestamp (created and updated)
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setSubParamName("u").setLine("`te_updated` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setSubParamName("c").setLine("`te_created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull(null).setMain(false).setSubParamName("te_info").setType("MEDIUMTEXT"))
                ;

        TalexEssential.getInstance().getMySQLSource().table(stb);
    }

    public static Map<String, PlayerData> map = new HashMap<>();

    public static PlayerData g(String name) {
        return map.get(name);
    }

    public static PlayerData g(UUID uuid) {
        return g(Bukkit.getOfflinePlayer(uuid).getName());
    }

    public static OnlinePlayerData g(Player player) {
        return (OnlinePlayerData) g(player.getName());
    }

    public static PlayerData g(PlayerEvent event) { return g(event.getPlayer()); }

    private final String name;

    private UUID uuid;

    private OfflinePlayer offlinePlayer;

    private int level, exp;

    private double dummy;

    @Setter
    private boolean doLogin;

    @Setter
    private Location lastLocation;

    private final YamlConfiguration info = new YamlConfiguration();

    private long created, updated;

    @Setter
    private Location deathLocation;

    private PlayerRes playerRes;

    private PlayerAttractData attractData;

    private PlayerSoul playerSoul;

//    private PlayerResSpec resSpec;

    @Setter
    private long lastPlace = -1;

    public PlayerData(String name) {;
        this.name = name;

        this.offlinePlayer = Bukkit.getOfflinePlayer(name);
        this.uuid = this.offlinePlayer.getUniqueId();

        map.put(name, this);
        _de();
    }

    @SneakyThrows
    private void _de() {
        try ( ResultSet rs = TalexEssential.getInstance().getMySQLSource().r("player_data", "te_uuid", this.uuid.toString()) ) {

            if ( rs == null || !rs.next() ) {
                _n();
            } else {
                String data = rs.getString("te_info");
                data = Base64.decodeStr(data);

                this.info.loadFromString(data);

                Time teCreated = rs.getTime("te_created");
                if ( teCreated != null ) {
                    this.created = teCreated.getTime();
                }

                Time teUpdated = rs.getTime("te_updated");
                if ( teUpdated != null ) {
                    this.updated = teUpdated.getTime();
                }

                this.level = rs.getInt("te_level");
                this.exp = rs.getInt("te_exp");
                this.dummy = rs.getDouble("te_dummy");

                int rankLevel = this.info.getInt("Rank.Level", 0);
                this.level = Math.max(rankLevel, this.level);

//                Economy econ = TalexEssential.getInstance().getEcon();

                /*double balance = econ.getBalance(this.offlinePlayer);

                // fix balance to dummy
                if ( balance > this.dummy ) {
                    econ.withdrawPlayer(this.offlinePlayer, balance - this.dummy);
                } else if ( balance < this.dummy ) {
                    econ.depositPlayer(this.offlinePlayer, this.dummy - balance);
                }*/

            }

        } catch ( Exception e ) {

            LogUtil.log("Error on load data of " + this.name + " (" + this.uuid + ")");
            e.printStackTrace();

        }

        if ( this.info.contains("Manifest.LastLocation") )
            this.lastLocation = this.info.getLocation("Manifest.LastLocation");

        this.playerRes = new PlayerRes(this);
        this.attractData = new PlayerAttractData(this);
        this.playerSoul = new PlayerSoul(this);
//        this.resSpec = new PlayerResSpec(this);
    }

    private void _n() {
        // new
        Player player = this.offlinePlayer.getPlayer();
        if ( player == null ) return;

        PlayerUser user = new PlayerUser(player);

        user.delayRun(new PlayerDataRunnable() {
            @Override
            public void run() {

                TalexEssential.getInstance().getEcon().depositPlayer(player, 500);

                user.addItem(new ItemStack(Material.STONE_SWORD))
                        .addItem(new ItemStack(Material.STONE_PICKAXE))
                        .addItem(new ItemStack(Material.STONE_AXE))
                        .addItem(new ItemStack(Material.STONE_SHOVEL))
                        .addItem(new ItemBuilder(Material.APPLE).setAmount(32).toItemStack())
                        .addItem(
                                new ItemBuilder(Material.WOODEN_HOE)
                                        .setName(MiniMessage.miniMessage().deserialize("<gradient:#F8BB0C:#16A6FF>圈地工具"))
                                        .setLore(
                                                "",
                                                "&8| &7使用这个 &e工具 &7来圈地",
                                                "&8| &7圈地是保护你领域的一种方式",
                                                "",
                                                "&a&l✓ &7用锄头轻点两个方块 &e来圈地",
                                                "&e请一定圈地较大的区域, 防止被人破坏",
                                                "",
                                                "&8#" + player.getUniqueId()
                                        )
                                        .toItemStack()
                        )
//                        .addItem(
//                                new ItemBuilder(Material.CLOCK)
//                                        .setName(MiniMessage.miniMessage().deserialize("<gradient:#7EDDFF:#0175FF>菜单"))
//                                        .setLore(
//                                                "",
//                                                "&8| &7菜单就像是助手一样帮助你",
//                                                "&8| &7你可以在菜单中查看你的 &e信息",
//                                                "&8| &7你可以在菜单中查看你的 &e领地",
//                                                "&8| &7你可以在菜单中查看你的 &e技能",
//                                                "&8| &7你可以在菜单中查看你的 &e物品",
//                                                "&8| &7你可以在菜单中查看你的 &e钱包",
//                                                "",
//                                                "&e抬头 + 蹲下起立 &7可以在任何地方打开菜单",
//                                                "",
//                                                "&a&l✓ &7点击 &e菜单 &7来打开菜单",
//                                                "",
//                                                "&8#" + player.getUniqueId()
//                                        )
//                                        .toItemStack()
//                        )
                        .playSound(Sound.ENTITY_PLAYER_LEVELUP, 1, 1)
                ;

            }
        }, 20 * 15);

    }

    @SneakyThrows
    public void save() {

        this.dummy = TalexEssential.getInstance().getEcon().getBalance(this.offlinePlayer);

        int rankLevel = info.getInt("Rank.Level", 0);
        this.level = Math.max(rankLevel, this.level);

        this.info.set("Rank.Level", this.level);

        this.attractData.save();
        this.playerRes.save();
        this.playerSoul.save();
//        this.resSpec.save();

        if ( offlinePlayer.isOnline() )
            this.info.set("Manifest.LastLocation", Objects.requireNonNull(offlinePlayer.getPlayer()).getLocation());
        else {
            // 删除
            PlayerData.map.remove(this.name);
        }

        SqlBuilder sb;

        ResultSet rs = TalexEssential.getInstance().getMySQLSource().r("player_data", "te_uuid", this.uuid.toString());

        if ( rs == null || !rs.next() ) {

            sb = new SqlAddBuilder().setTableName("player_data")
                    .setType(SqlAddBuilder.AddType.IGNORE)
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_uuid").setSubParamValue(this.uuid.toString()))
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_name").setSubParamValue(this.name))
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_level").setSubParamValue(String.valueOf(this.level)))
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_exp").setSubParamValue(String.valueOf(this.exp)))
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_dummy").setSubParamValue(String.valueOf(this.dummy)))
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_info").setSubParamValue(Base64.encode(this.info.saveToString())))
                    ;

        } else {
            // update

            sb = new SqlUpdBuilder().setTableName("player_data")
                    .addTableParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_name").setSubParamValue(this.name))
                    .addTableParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_level").setSubParamValue(String.valueOf(this.level)))
                    .addTableParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_exp").setSubParamValue(String.valueOf(this.exp)))
                    .addTableParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_dummy").setSubParamValue(String.valueOf(this.dummy)))
                    .addTableParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_info").setSubParamValue(Base64.encode(this.info.saveToString())))
                    .setWhereParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_uuid").setSubParamValue(this.uuid.toString()))
                    ;
        }

        boolean b = TalexEssential.getInstance().getMySQLSource().ps(sb.toString()).execute();

//        if ( !b ) {
            // TODO: boom backup save
//            TalexEssential.getInstance().getLogger().warning("Failed to save player data: " + this.name);
//
//            throw new RuntimeException("Failed to save player data: " + this.name);
//        }
    }

    public boolean getOptionsEnabled(String name) {
        return getOptionsEnabled(name, false);
    }

    public boolean getOptionsEnabled(String name, boolean defaultVal) {
        return this.info.getBoolean("Settings.Options." + name + ".enabled", defaultVal);
    }

    public void setOptionsEnabled(String name, boolean enabled) {
        this.info.set("Settings.Options." + name + ".enabled", enabled);
    }

    public ConfigurationSection getOptionsSection(String name) {
        String path = "Settings.Options." + name;

        if ( !this.info.isConfigurationSection(path) ) {
            this.info.set(path + "._section", true);
        }

        return this.info.getConfigurationSection(path);
    }

    public static abstract class PlayerModuleData {

        public abstract void onSave();

    }

}
