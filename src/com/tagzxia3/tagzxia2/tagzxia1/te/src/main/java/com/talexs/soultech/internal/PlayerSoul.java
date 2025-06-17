package com.tagzxia3.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.internal;

import cn.hutool.core.util.RandomUtil;
import com.tagzxia.te.src.main.java.com.talexs.soultech.internal.StNameSpace;
import com.talexs.soultech.addon.AddonHolder;
import com.talexs.soultech.internal.book.IndicateBook;
import com.talexs.soultech.internal.machine.MachineAddon;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerDataRunnable;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.soultech.machine.bsae.BaseMachine;
import com.talexs.soultech.machine.bsae.IMachineBuilder;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
public class PlayerSoul {

    public static final String SOULTECH_PREFIX = "soultech";

    private final PlayerData pd;

    private boolean enabled;

    private boolean given;

    private int unlockLevel;

    private IndicateBook indicateBook;

    public PlayerSoul(PlayerData pd) {
        this.pd = pd;

        this.enabled = pd.getInfo().getBoolean(SOULTECH_PREFIX + ".enabled", false);
        this.given = pd.getInfo().getBoolean(SOULTECH_PREFIX + ".given_new", false);
        this.unlockLevel = pd.getInfo().getInt(SOULTECH_PREFIX + ".unlock_level", RandomUtil.randomInt(120, 180));

        init();
    }

    public boolean doCategoryUnlock(String id) {
        return pd.getInfo().getBoolean(SOULTECH_PREFIX + ".Categories." + id, false);
    }

    public boolean doMachineUnlock(String machineId) {
        return pd.getInfo().getBoolean(SOULTECH_PREFIX + ".Machines." + machineId, false);
    }

    public void unlockCategory(String id) {
        pd.getInfo().set(SOULTECH_PREFIX + ".Categories." + id, true);
    }

    public void unlockMachine(String machineId) {
        pd.getInfo().set(SOULTECH_PREFIX + ".Machines." + machineId, true);
    }

    public int getUnlockedCategories() {
        int count = 0;

        YamlConfiguration info = pd.getInfo();

        if ( info.contains(SOULTECH_PREFIX + ".Categories") ) {

            Set<String> keys = Objects.requireNonNull(pd.getInfo().getConfigurationSection(SOULTECH_PREFIX + ".Categories")).getKeys(false);

            count = keys.size();

        }

//        for ( String id : Objects.requireNonNull(pd.getInfo().getConfigurationSection(SOULTECH_PREFIX + ".Categories")).getKeys(false) ) {
//            if ( pd.getInfo().getBoolean(SOULTECH_PREFIX + ".Categories." + id, false) ) {
//                count++;
//            }
//        }
        return count;
    }

    private void init() {
        if ( !this.given ) {
            Player player = pd.getOfflinePlayer().getPlayer();
            if ( player != null && player.getLevel() > 100 ) {

                PlayerUser user = new PlayerUser(player);

                user.delayRun(new PlayerDataRunnable() {
                    @Override
                    public void run() {

                        addGuideToPlayer(player);
                        setGiven(true);

                        user.actionBar("&e你收到了一本 &c奇书 &e，似乎有怨灵在其中诡叫！")
                                .title("&5&l₪", "&e一本奇怪的封印显现在你眼前.", 0, 80, 40);

                        user.playSound(Sound.ENTITY_ALLAY_ITEM_THROWN, 1, 1);
                        user.playSound(Sound.ENTITY_ALLAY_ITEM_THROWN, 1, 1);
                        user.playSound(Sound.ENTITY_ALLAY_ITEM_THROWN, 1, 1);
                        user.playSound(Sound.ENTITY_ALLAY_ITEM_THROWN, 1, 1);
                        user.playSound(Sound.ENTITY_ALLAY_ITEM_THROWN, 1, 1);
                        user.playSound(Sound.ENTITY_ALLAY_ITEM_THROWN, 1, 1);

                    }
                }, 20 * 60);

            }
        }
    }

    public void save() {
        pd.getInfo().set(SOULTECH_PREFIX + ".enabled", enabled);
        pd.getInfo().set(SOULTECH_PREFIX + ".given_new", given);
        pd.getInfo().set(SOULTECH_PREFIX + ".unlock_level", unlockLevel);
    }

    public void addGuideToPlayer(Player player) {
        guideBook.addToPlayer(player, true);
    }

    private static SoulGuideBook guideBook = new SoulGuideBook();

    public static class SoulGuideBook extends SoulTechItem {

        public SoulGuideBook() {
            super("guide_book", new ItemBuilder(Material.ENCHANTED_BOOK)
//                .addEnchant(Enchantment.ARROW_FIRE, 1).addFlag(ItemFlag.HIDE_ENCHANTS)
                    .setName("&7[&5灵魂&b科技&7]")
                    .setLore(
                            "",
                            "&8| &5灵魂&b科技&7绝响，一妄虚无决章",
                            "&8| &7增易殇之镝诗，涌圣光之明翼",
                            "&8| &7破虚空之地狱，炼灵魂之灵魄",
                            "&8| &7引不动烨铧声，吸不尽炎翼光",
                            "&8| &7但那送之新神，却道不尽永恒",
                            "",
                            "&7正在查阅: &e尚未启用",
                            "&7解锁进度: &e0/146852",
                            "",
                            "&7&k|&a 破 · 波 · 诗 · 灵 · 魂 · 科 · 技 &7&k|",
                            ""
                    )
                    .toItemStack());
        }

        @Override
        public void onInteract(PlayerData playerData, PlayerInteractEvent event) {
            Action action = event.getAction();

            if ( action == Action.RIGHT_CLICK_BLOCK ) {
                PlayerUser user = new PlayerUser(event.getPlayer());
                String machineId = user.getMetaOnHandItem(StNameSpace.GUIDE_AUTO_BUILD);
                if ( machineId != null ) {
                    BaseMachine machine = AddonHolder.getINSTANCE().get(MachineAddon.class).getMachine(machineId);
                    if ((machine instanceof IMachineBuilder)) {
                        event.setCancelled(true);

                        user.delayRun(new PlayerDataRunnable() {
                            @Override
                            public void run() {
                                user.closeInventory();
                                if ( !playerData.getPlayerSoul().doMachineUnlock(machineId) ) {
                                    user.errorActionBar("诡异的魔力阻止了你！").playSound(Sound.BLOCK_ANVIL_LAND, 1, 1)
                                            .playSound(Sound.ENTITY_ALLAY_ITEM_THROWN, 1, 1)
                                            .playSound(Sound.ENTITY_ALLAY_DEATH, 1, 1)
                                            .playSound(Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 1, 1)
                                            .playSound(Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1, 1)
                                            .playSound(Sound.ENTITY_ELDER_GUARDIAN_DEATH, 1, 1)
                                            .throwHandItem()
                                    ;
                                    return;
                                }
                                ((IMachineBuilder) machine).build(event.getPlayer(), playerData, user, Objects.requireNonNull(event.getClickedBlock()).getLocation());
                            }
                        }, 2);
                    }

                    return;
                }
            }

            if ( action.name().contains("RIGHT") ) {
                if ( !playerData.getPlayerSoul().isEnabled() ) {
                    String owner = getOwnerUUID(event.getItem());
                    if ( owner == null || !owner.equals(playerData.getUuid().toString())) {
                       PlayerUser user = new PlayerUser(event.getPlayer());

                       user
                               .actionBar(MiniMessage.miniMessage().deserialize("<gradient:#E43D30:#A98E2D>你不可以打开别人的书!"))
                               .playSound(Sound.BLOCK_ANVIL_LAND, 1, 1)
                               .playSound(Sound.ENTITY_ALLAY_ITEM_THROWN, 1, 1)
                               .playSound(Sound.ENTITY_ALLAY_DEATH, 1, 1)
                       ;

                       return;
                    }
                }

//                GuiderBook guider = playerData.getPlayerSoul().getGuiderBook();
//                if ( guider != null ) guider.open(true);
//                else new GuiderBook(event.getPlayer()).open(true);

                IndicateBook guider = playerData.getPlayerSoul().getIndicateBook();
                if ( guider != null ) guider.open(true);
                else new IndicateBook(event.getPlayer()).open(true);
            }

        }
    }

}
