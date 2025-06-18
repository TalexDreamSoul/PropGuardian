package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.talexessential;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.AdminCommands;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.Commands;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.GlobalListener;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.PlaceholdeHook;
import com.talexs.soultech.TalexSoulTech;
import com.talexs.talexessential.base.channel.BaseChannel;
import com.talexs.talexessential.data.MySQLSource;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.OnlinePlayerData;
import com.talexs.talexessential.modules.ModuleManager;
import com.talexs.talexessential.modules.realm.RealmModule;
import com.talexs.talexessential.modules.union.UnionData;
import com.talexs.talexessential.modules.union.UnionMember;
import com.talexs.talexessential.utils.LogUtil;
import com.talexs.talexessential.utils.inventory.UIListener;
import com.talexs.talexessential.utils.inventory.base.TalexUIListener;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;

@Slf4j
@Getter
public final class TalexEssential extends JavaPlugin {

    @Getter
    private static TalexEssential instance;

    private MySQLSource mySQLSource;

    private Economy econ;

    private PlaceholdeHook papiHook;

    public static boolean debug;

    public boolean done = false;

    private HeadDatabaseAPI api;

    public HeadDatabaseAPI getApi() {
        while ( api == null ) api = new HeadDatabaseAPI();

        return api;
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        debug = getConfig().getBoolean("Settings.debug", false);
        if ( debug ) {
            getLogger().setLevel(Level.ALL);
            getLogger().severe("*** Debug mode enabled.");
        }

        setupMysql();
        setupCorrespond();
        setupEconomy();
        setupModules();

        Bukkit.getOnlinePlayers().forEach(OnlinePlayerData::new);
        new BukkitRunnable() {
            @Override
            public void run() {
                saveAllPlayerData();
            }
        }.runTaskTimer(this, 600, 20 * 60 * 15);

        done = true;

        LogUtil.log("[TalexEssential] All data loaded, started!");
    }

    private void saveAllPlayerData() {

        new ArrayList<>(PlayerData.map.values()).forEach(PlayerData::save);

        LogUtil.log("&7[TalexEssential] Saved &b&l" + PlayerData.map.size() + "/" + Bukkit.getOnlinePlayers().size() + " &7players data.");

        Bukkit.getOnlinePlayers().stream().filter(Objects::nonNull).filter(p -> p.hasPermission("te.admin")).forEach(p -> {
            p.sendMessage(ChatColor.GOLD + "TalexEssential: " + ChatColor.WHITE + "All player data saved. (" + PlayerData.map.size() + "/" + Bukkit.getOnlinePlayers().size() + ")");
        });

    }

    @SneakyThrows
    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(Player::closeInventory);

        ModuleManager ins = ModuleManager.INS;

        ins.disableAll();

//        if ( SoulTech.getInstance() != null)
//            SoulTech.getInstance().onDisable();

        if ( this.tst != null )
            tst.onDisable();

        PlaceholderAPI.unregisterExpansion(papiHook);

        PlayerData.map.values().forEach(PlayerData::save);

        this.mySQLSource.getDs().getConnection().close();
    }

    private TalexSoulTech tst;

    private void setupModules() {
        tst = new TalexSoulTech();

        PlayerData.injectTable();
        UnionData.injectTable();
        UnionMember.injectTable();
        RealmModule.injectTable();

        // Module Automatically scanner
        ModuleManager ins = ModuleManager.INS;

        try {

            ins.scanFor(this, "com.talexs.talexessential.modules");

        } catch (NoSuchFieldException | IllegalAccessException e) {

            throw new RuntimeException(e);

        }

        tst.onEnable();

        ins.allModulesDone();

        log.info("Registered all this modules.");
    }

    private void setupCorrespond() {
        getServer().getPluginManager().registerEvents(new GlobalListener(), this);
        getServer().getPluginManager().registerEvents(new UIListener(this), this);
        getServer().getPluginManager().registerEvents(new TalexUIListener(this), this);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BaseChannel());

        Objects.requireNonNull(getServer().getPluginCommand("te")).setExecutor(new Commands());
        Objects.requireNonNull(getServer().getPluginCommand("tea")).setExecutor(new AdminCommands());

        PlaceholderAPI.registerExpansion(papiHook = new PlaceholdeHook());

        log.info("Registered all this events.");
    }

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            throw new RuntimeException("Vault not found.");
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            throw new RuntimeException("Economy not found.");
        }
        econ = rsp.getProvider();
    }

    private void setupMysql() {
        String url = getConfig().getString("Settings.mysql.url");
        String name = getConfig().getString("Settings.mysql.user");
        String pass = getConfig().getString("Settings.mysql.pass");

        this.mySQLSource = new MySQLSource(url, name, pass);

        try {
            this.mySQLSource.connect();
        } catch ( Exception e ) {
            log("&c[MySQL Source] Failed to connect to MySQL Source!");
            e.printStackTrace();

            setEnabled(false);

            return;
        }

        log("&7[MySQL Source] Connected to MySQL Source!");
    }

    public void log(String message) {

        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));

    }

    @SneakyThrows
    public void writeByIS(String name) {
        InputStream is = super.getResource(name);

        File file = new File(getDataFolder(), name);
        FileOutputStream fos = new FileOutputStream(file);

        byte[] bytes = new byte[1024];

        int len;

        while( true ) {
            assert is != null;
            if ( ( len = is.read(bytes) ) == -1 ) break;

            fos.write(bytes, 0, len);

        }

        fos.close();

        is.close();

    }
}
