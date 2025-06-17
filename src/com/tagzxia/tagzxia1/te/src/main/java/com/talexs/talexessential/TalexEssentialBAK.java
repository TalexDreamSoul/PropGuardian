package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.talexessential;//package com.talexs.talexessential;
//
//import com.talexs.talexessential.base.channel.BaseChannel;
//import com.talexs.talexessential.modules.chat.ChatModule;
//import com.talexs.talexessential.data.MySQLSource;
//import com.talexs.talexessential.data.PlayerData;
//import com.talexs.talexessential.data.player.OnlinePlayerData;
//import com.talexs.talexessential.modules.actions.HeadCmd;
//import com.talexs.talexessential.modules.arena.ArenaModule;
//import com.talexs.talexessential.modules.attract.AttractModule;
//import com.talexs.talexessential.modules.common.RandomTeleportModule;
//import com.talexs.talexessential.modules.common.RespawnModule;
//import com.talexs.talexessential.modules.dropper.DropperModule;
//import com.talexs.talexessential.modules.environment.VillagerModule;
//import com.talexs.talexessential.modules.rank.RankModule;
//import com.talexs.talexessential.modules.realm.PlayerRealm;
//import com.talexs.talexessential.modules.realm.RealmModule;
//import com.talexs.talexessential.modules.resource.ResModule;
//import com.talexs.talexessential.modules.shop.ShopModule;
//import com.talexs.talexessential.modules.souvenir.SouvenirModule;
//import com.talexs.talexessential.modules.union.UnionData;
//import com.talexs.talexessential.modules.union.UnionMember;
//import com.talexs.soultech.SoulTech;
//import com.talexs.talexessential.utils.LogUtil;
//import com.talexs.talexessential.utils.inventory.UIListener;
//import com.talexs.talexessential.utils.inventory.base.TalexUIListener;
//import lombok.Getter;
//import lombok.SneakyThrows;
//import me.arcaniax.hdb.api.DatabaseLoadEvent;
//import me.arcaniax.hdb.api.HeadDatabaseAPI;
//import me.clip.placeholderapi.PlaceholderAPI;
//import net.milkbowl.vault.economy.Economy;
//import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.plugin.RegisteredServiceProvider;
//import org.bukkit.plugin.java.JavaPlugin;
//import org.bukkit.scheduler.BukkitRunnable;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.util.Objects;
//
//@Getter
//public final class TalexEssentialBAK extends JavaPlugin implements Listener {
//
//    @Getter
//    private static TalexEssentialBAK instance;
//
//    private MySQLSource mySQLSource;
//
//    private SoulTech soulTech;
//
//    private Economy econ;
//
//    private PlaceholdeHook papiHook;
//
//    public static boolean resourceEnable = false, villagers = false;
//
//    private boolean started, registered;
//
//    public boolean done = false;
//
//    @Getter
//    private HeadDatabaseAPI api;
//
//    @Override
//    public void onEnable() {
//        instance = this;
//        if ( !getServer().getPluginManager().isPluginEnabled("HeadDatabase") ) {
//            LogUtil.log("[TalexEssential] HeadDatabase none load, try load plugin later.");
//            new BukkitRunnable() {
//                @Override
//                public void run() {
//                    onEnable();
//                }
//            }.runTaskLater(this, 20);
//            return;
//        }
//
//        saveDefaultConfig();
//        initSQL();
//
//        PlayerData.injectTable();
//        UnionData.injectTable();
//        UnionMember.injectTable();
//        RealmModule.injectTable();
//        setupEconomy();
//
//        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
//        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BaseChannel());
//
//        getServer().getPluginManager().registerEvents(new GlobalListener(), this);
//        getServer().getPluginManager().registerEvents(new UIListener(this), this);
//        getServer().getPluginManager().registerEvents(new TalexUIListener(this), this);
//
//        Objects.requireNonNull(getServer().getPluginCommand("te")).setExecutor(new Commands());
//        Objects.requireNonNull(getServer().getPluginCommand("tea")).setExecutor(new AdminCommands());
//
//        if ( !registered ) {
//            registered = true;
//            getServer().getPluginManager().registerEvents(TalexEssentialBAK.this, TalexEssentialBAK.this);
//            LogUtil.log("[TalexEssential] Registered all this events.");
//        }
//
//        api = new HeadDatabaseAPI();
//        if ( api == null ) {
//            LogUtil.log("[TalexEssential] HeadDatabase null, try later load all this events.");
//            new BukkitRunnable() {
//                @Override
//                public void run() {
//                    onEnable();
//                }
//            }.runTaskLater(this, 100);
//            return;
//        }
//
//        onDatabaseLoad(new DatabaseLoadEvent(0));
//
//        start();
//        LogUtil.log("[TalexEssential] Start all this events.");
//
//    }
//
//    private void start() {
//        if ( started ) return;
//        started = true;
//
//        PlaceholderAPI.registerExpansion(papiHook = new PlaceholdeHook());
//
//        resourceEnable = getConfig().getBoolean("Modules.resource.enable", false);
//        villagers = getConfig().getBoolean("Modules.villagers.enable", false);
//
//        if ( resourceEnable ) new ResModule();
//        if ( villagers ) new VillagerModule();
//
//        new HeadCmd();
//        new AttractModule();
//        new ChatModule();
//        new DropperModule();
//        new RespawnModule();
//        new RankModule();
//        new ShopModule();
////        new SpecModule();
//        new SouvenirModule();
//        new RealmModule();
////        new ProtectorModule();
//        new ArenaModule();
//        new RandomTeleportModule();
//
//        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
//
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                saveAllPlayerData();
//            }
//        }.runTaskTimer(this, 600, 20 * 60 * 15);
//
////        new BukkitRunnable() {
////            @Override
////            public void run() {
////
////                Bukkit.getOnlinePlayers().forEach(player -> {
////                    if ( player.hasPermission("tea.admin") ) return;
////
////                    InventoryView openInventory = player.getOpenInventory();
////                    if ( openInventory.getTopInventory() != null ) {
////                        Inventory open = openInventory.getTopInventory();
////
////                        int first = open.first(Material.DRAGON_EGG);
////                        if ( first > -1 ) {
////                            ItemStack item = open.getItem(first);
////                            if ( item != null ) {
////                                if( item.getItemMeta() != null && item.getItemMeta().hasEnchants() ) return;
////                                player.getWorld().dropItem(player.getLocation(), item);
////
////                                open.setItem(first, null);
////                            }
////                        }
////                    }
////
////                    PlayerInventory inventory = player.getInventory();
////                    if ( inventory.first(Material.DRAGON_EGG) > -1 ) {
////                        int c = changes.getOrDefault(player.getName(), 0) + 1;
////
////                        damageItemStack(player, inventory.getBoots());
////                        damageItemStack(player, inventory.getHelmet());
////                        damageItemStack(player, inventory.getChestplate());
////                        damageItemStack(player, inventory.getLeggings());
////
////                        if ( c >= 4 ) {
////                            processItemStack(player, inventory.getHelmet());
////                            processItemStack(player, inventory.getChestplate());
////                            processItemStack(player, inventory.getLeggings());
////                            processItemStack(player, inventory.getBoots());
////                        }
////
////                        changes.put(player.getName(), c);
////                    }
////                });
////
////            }
////        }.runTaskTimer(this, 0, 20);
//    }
//
////    private void processItemStack(Player player, ItemStack stack) {
////        if ( stack == null ) {
////            player.setSaturation(20);
////            player.setHealth(1);
////            new PlayerUser(player).triggerDarkness();
////            return;
////        }
////
////        if ( stack.getType() == Material.NETHERITE_BOOTS ) stack.setType(Material.DIAMOND_BOOTS);
////        else if ( stack.getType() == Material.DIAMOND_BOOTS ) stack.setType(Material.IRON_BOOTS);
////        else if ( stack.getType() == Material.IRON_BOOTS ) stack.setType(Material.GOLDEN_BOOTS);
////        else if ( stack.getType() == Material.GOLDEN_BOOTS ) stack.setType(Material.LEATHER_BOOTS);
////        else if ( stack.getType() == Material.LEATHER_BOOTS ) stack.setType(Material.CHAINMAIL_BOOTS);
////
////        if ( stack.getType() == Material.NETHERITE_CHESTPLATE) stack.setType(Material.DIAMOND_CHESTPLATE);
////        else if ( stack.getType() == Material.DIAMOND_CHESTPLATE ) stack.setType(Material.IRON_CHESTPLATE);
////        else if ( stack.getType() == Material.IRON_CHESTPLATE ) stack.setType(Material.GOLDEN_CHESTPLATE);
////        else if ( stack.getType() == Material.GOLDEN_CHESTPLATE ) stack.setType(Material.LEATHER_CHESTPLATE);
////        else if ( stack.getType() == Material.LEATHER_CHESTPLATE ) stack.setType(Material.CHAINMAIL_CHESTPLATE);
////
////        if ( stack.getType() == Material.NETHERITE_LEGGINGS ) stack.setType(Material.DIAMOND_LEGGINGS);
////        else if ( stack.getType() == Material.DIAMOND_LEGGINGS ) stack.setType(Material.IRON_LEGGINGS);
////        else if ( stack.getType() == Material.IRON_LEGGINGS ) stack.setType(Material.GOLDEN_LEGGINGS);
////        else if ( stack.getType() == Material.GOLDEN_LEGGINGS ) stack.setType(Material.LEATHER_LEGGINGS);
////        else if ( stack.getType() == Material.LEATHER_LEGGINGS ) stack.setType(Material.CHAINMAIL_LEGGINGS);
////
////        if ( stack.getType() == Material.NETHERITE_HELMET ) stack.setType(Material.DIAMOND_HELMET);
////        else if ( stack.getType() == Material.DIAMOND_HELMET ) stack.setType(Material.IRON_HELMET);
////        else if ( stack.getType() == Material.IRON_HELMET ) stack.setType(Material.GOLDEN_HELMET);
////        else if ( stack.getType() == Material.GOLDEN_HELMET ) stack.setType(Material.LEATHER_HELMET);
////        else if ( stack.getType() == Material.LEATHER_HELMET ) stack.setType(Material.CHAINMAIL_HELMET);
////        else if ( stack.getType() == Material.CHAINMAIL_HELMET ) stack.setType(Material.AIR);
////
////        player.setVelocity(player.getVelocity().setY(1.5));
////        player.setHealth(1);
////    }
////
////    private void damageItemStack(Player player, ItemStack stack) {
////        if ( stack == null ) return;
////        player.damageItemStack(stack, 60);
////    }
////
////    static Map<String, Integer> changes = new HashMap<>();
//
//    private void saveAllPlayerData() {
//        PlayerData.map.values().forEach(PlayerData::save);
//    }
//
//    @SneakyThrows
//    @Override
//    public void onDisable() {
//        Bukkit.getOnlinePlayers().forEach(Player::closeInventory);
//
//        RealmModule.realms.forEach(PlayerRealm::save);
//
//        SoulTech.getInstance().disable();
//
//        PlaceholderAPI.unregisterExpansion(papiHook);
//
//        PlayerData.map.values().forEach(PlayerData::save);
//
//        this.mySQLSource.getDs().getConnection().close();
//    }
//
//    @EventHandler
//    public void onDatabaseLoad(DatabaseLoadEvent e) {
////        HeadDatabaseAPI api = new HeadDatabaseAPI();
//        try {
//
//            SoulTech.init(TalexEssentialBAK.this);
//
//            Bukkit.getOnlinePlayers().forEach(OnlinePlayerData::new);
//
//        } catch (NullPointerException nullPointerException) {
//            getLogger().info("Could not find the head you were looking for");
//            nullPointerException.printStackTrace();
//        }
//
//        done = true;
//        LogUtil.log("[TalexEssential] All data loaded, started!");
//    }
//
//    private void setupEconomy() {
//        if (getServer().getPluginManager().getPlugin("Vault") == null) {
//            throw new RuntimeException("Vault not found.");
//        }
//        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
//        if (rsp == null) {
//            throw new RuntimeException("Economy not found.");
//        }
//        econ = rsp.getProvider();
//    }
//
//    private void initSQL() {
//        String url = getConfig().getString("Settings.mysql.url");
//        String name = getConfig().getString("Settings.mysql.user");
//        String pass = getConfig().getString("Settings.mysql.pass");
//
//        this.mySQLSource = new MySQLSource(url, name, pass);
//
//        try {
//            this.mySQLSource.connect();
//        } catch ( Exception e ) {
//            log("&c[MySQL Source] Failed to connect to MySQL Source!");
//            e.printStackTrace();
//
//            setEnabled(false);
//
//            return;
//        }
//
//        log("&7[MySQL Source] Connected to MySQL Source!");
//    }
//
//    public void log(String message) {
//
//        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
//
//    }
//
//    @SneakyThrows
//    public void writeByIS(String name) {
//
//        InputStream is = super.getResource(name);
//
//        File file = new File(getDataFolder(), name);
//
//        FileOutputStream fos = new FileOutputStream(file);
//
//        byte[] bytes = new byte[1024];
//
//        int len;
//
//        while( true ) {
//            assert is != null;
//            if ( ( len = is.read(bytes) ) == -1 ) break;
//
//            fos.write(bytes, 0, len);
//
//        }
//
//        fos.close();
//
//        is.close();
//
//    }
//}
