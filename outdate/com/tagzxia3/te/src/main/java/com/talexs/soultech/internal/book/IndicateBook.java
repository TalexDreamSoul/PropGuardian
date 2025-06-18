package com.tagzxia3.te.src.main.java.com.talexs.soultech.internal.book;

import com.talexs.soultech.addon.AddonHolder;
import com.talexs.soultech.internal.RecipeObject;
import com.talexs.soultech.internal.entity.classfies.Classifies;
import com.talexs.soultech.internal.entity.classfies.ClassifiesAddon;
import com.talexs.soultech.internal.entity.preposition.IPreposition;
import com.talexs.soultech.internal.entity.tech_object.TechObject;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.machine.MachineAddon;
import com.talexs.soultech.items.base.BaseMachineRecipe;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.soultech.internal.PlayerSoul;
import com.talexs.soultech.internal.StNameSpace;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.utils.item.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;
import java.util.stream.Stream;

@Accessors(chain = true)
public class IndicateBook extends MenuBasic {

    @Getter
    private Classifies classifies;

    public IndicateBook(Player player, String title) {
        super(player, title, 6);
    }

    public IndicateBook(Player player) {
        this(player, "&5灵魂&b科技 &8> &f一览");
    }

    List<String> historyList = new ArrayList<>();

    @Getter
    private PlayerUser user;

    private String nowBookOwner;

    @Override
    protected boolean onInvClick(InventoryClickEvent event) {
        boolean res = event.getWhoClicked().hasPermission("te.admin") || event.getWhoClicked().getUniqueId().toString().equals(nowBookOwner);

        if ( !res ) {
            new PlayerUser((Player) event.getWhoClicked())
                    .errorActionBar("你无法操纵别人的书！")
                    .playSound(Sound.ENTITY_VILLAGER_NO)
                    .closeInventory();
        }

        return res;
    }

    @Override
    public void SetupForPlayer(Player player) {
        user = new PlayerUser(player);
        nowBookOwner = user.getMetaOnHandItem(StNameSpace.OWNER);
        String guideNow = user.getMetaOnHandItem(StNameSpace.GUIDE_NOW);
        String guideHistory = user.getMetaOnHandItem(StNameSpace.GUIDE_HISTORY);
        String[] guides = guideHistory == null ? new String[] {} : guideHistory.split("‘");

        historyList = new ArrayList<>(Stream.of(guides).map(String::trim).toList());

        new InventoryPainter(this).drawFull().drawBorder();

        if (guideNow != null && !guideNow.equals("root") && guideNow.length() > 3)
            classifies = AddonHolder.getINSTANCE().get(ClassifiesAddon.class).getClassifies(guideNow);
        else
            classifies = AddonHolder.getINSTANCE().get(ClassifiesAddon.class).getRoot();

        if ( classifies == null ) {
            user
                    .closeInventory()
                    .playSound(Sound.BLOCK_ANVIL_LAND).errorActionBar("发现错误，请联系管理员：" + guideNow);

            return;
        }

        PlayerData pd = PlayerData.g(player);
        PlayerSoul playerSoul = pd.getPlayerSoul();
        playerSoul.setIndicateBook(this);

        user.playSound(Sound.ITEM_BOOK_PAGE_TURN, .5f, .2f);

        if ( classifies instanceof TechObject ) {

            RecipeObject recipe = ((TechObject) classifies).getRecipe();

            if ( recipe != null ) {

                if (recipe instanceof BaseMachineRecipe bmr) {

                    user
                            .closeInventory()
                            .infoActionBar("这个机器不可以自动建造！");

                    lastLevel().open();

                    return;
                }

            }

            AddonHolder.getINSTANCE().get(MachineAddon.class).onRecipeView(IndicateBook.this);
        } else {
            drawCategories(player, user, pd);
        }

        if (guideHistory != null && guideHistory.length() > 2 ) drawLastLevel();
    }

    private void drawCategories(Player player, PlayerUser user, PlayerData pd) {
        PlayerSoul ps = pd.getPlayerSoul();

        int slot = 10;

        Stream<Classifies> sorted = classifies.getChildren().stream().sorted(Comparator.comparingInt(Classifies::getPriority));

        for (Classifies classifies : sorted.toList()) {
            boolean doLock;

            if ( classifies instanceof TechObject to ) {

                RecipeObject recipe = to.getRecipe();

                if ( recipe instanceof BaseMachineRecipe bmr ) {

                    String machineId = classifies.getID().substring(11);

                    doLock = ps.doMachineUnlock(machineId);

                } else doLock = ps.doCategoryUnlock(classifies.getID());

            } else doLock = ps.doCategoryUnlock(classifies.getID());

            boolean prepositionLock = classifies.getPreposition().stream().anyMatch(preposition -> !preposition.allow(ps));
            if ( !prepositionLock )
                inventoryUI.setItem(slot, new InventoryUI.AbstractSuperClickableItem() {
                @Override
                public ItemStack getItemStack() {
                    ItemStack clone = classifies.getDisplayStack().clone();

                    ItemBuilder itemBuilder = new ItemBuilder(clone);

                    for (IPreposition preposition : classifies.getPreposition()) {

                        if ( !preposition.allow(ps) )
                            itemBuilder.addLoreLine("&c✘ &7前置学科 &e" + preposition.display() + " &7未解锁");
                        else {
                            itemBuilder.addLoreLine("&a✔ &7前置学科 &e" + preposition.display() + " &7已解锁");
                        }

                        if ( doLock ) itemBuilder.addLoreLine("");

                    }

                    if ( !doLock ) {
                        itemBuilder.setType(Material.BARRIER);

                        classifies.getUnlockConditions().forEach(condition -> {
                            String name = condition.getDisplay(player, pd);
                            if ( !condition.onCheck(player, pd, user) ) {
                                itemBuilder.addLoreLine("&c✘ &e" + name);
                            } else {
                                itemBuilder.addLoreLine("&a✔ &e" + name);
                            }
                        });

                        itemBuilder.addLoreLine("");

                    }

                    itemBuilder.addLoreLine(doLock ? "&a● 已解锁" : "&8○ &e未解锁");
                    itemBuilder.addLoreLine("");

                    if ( player.hasPermission("talex.essential.soultech.admin") ) {
                        if ( classifies instanceof TechObject ) {
                            itemBuilder.addLoreLine("&eSHIFT + 左键 &a获得物品");
                        } else itemBuilder.addLoreLine("&eSHIFT + 左键 &a强制进入");
                    }

                    itemBuilder.addLoreLine("&8#" + classifies.getID());

                    return itemBuilder
                            .toItemStack();
                }

                @Override
                public boolean onClick(InventoryClickEvent e) {
                    boolean hasPer = player.hasPermission("talex.essential.soultech.admin") && e.isShiftClick();

                    if ( doLock || hasPer ) {

                       if ( classifies instanceof TechObject techObject ) {

                           RecipeObject recipeObject = techObject.getRecipe();

                           if ( recipeObject == null ) {

                               user
                                       .playSound(Sound.ENTITY_VILLAGER_NO)
                                       .errorActionBar("此项研究暂时不可深究...");

                               return false;
                           }

                            if ( hasPer ) {

                                if (recipeObject instanceof WorkBenchRecipe wbr) {
                                    user.addItem( wbr.getExport().getItemBuilder().toItemStack() );
                                } else {
                                    user.addItem( classifies.getDisplayStack() );
                                }

                                return false;

                            } else {

//                                AddonHolder.getINSTANCE().get(MachineAddon.class).onRecipeView(IndicateBook.this);
                            }

                        }

                        setClassifies(classifies);
                        open();
                        return false;
                    }

                    boolean could = classifies.getUnlockConditions().stream().allMatch(condition -> condition.onCheck(player, pd, user));
                    if ( !could ) {
                        user
                                .playSound(Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 1, 1)
                                .playSound(Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1, 1)
                                .playSound(Sound.ENTITY_ELDER_GUARDIAN_DEATH, 1, 1)
                                .errorActionBar("你还不能解锁这个.");
                        return false;
                    }

                    for (IUnlockCondition unlockCondition : classifies.getUnlockConditions()) {

                        unlockCondition.consume(player, pd);

                    }

                    if ( classifies instanceof TechObject to ) {

                        RecipeObject recipe = to.getRecipe();

                        if ( recipe instanceof BaseMachineRecipe bmr ) {

                            String machineId = classifies.getID().substring(11);

                            ps.unlockMachine(machineId);

                            user
                                    .firework()
                                    .title("&b✿", "&b已发现新机器", 0 , 60, 40)
                                    .infoActionBar("灵魂科技进度已更新!");

                        } else {

                            user
                                    .firework()
                                    .title("&b✿", "&b已发现新物品", 0 , 60, 40)
                                    .infoActionBar("灵魂科技进度已更新!");

                            ps.unlockCategory(classifies.getID());

                        }

                    } else {

                        ps.unlockCategory(classifies.getID());

                        user
                                .firework()
                                .title("&b✿", "&7已解锁 &b" + classifies.getName(), 0 , 60, 40)
                                .infoActionBar("灵魂科技进度已更新!");

                    }

                    open();

                    return false;
                }
            });

            if ( (slot + 2) % 9 == 0 ) {
                slot += 3;
            } else {
                slot++;
            }

        }

        inventoryUI.setItem(49, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                // TODO: modify to owner name
                return new ItemBuilder(Material.PLAYER_HEAD).setName(MiniMessage.miniMessage().deserialize("<gradient:#1DE9B6:#26BBCD>" + player.getName()))
                        .setLore(
                                "",
                                "&8| &7赋新朔史鉴，魂迁技起经",
                                "",
                                "&8| &d诡异的魔力令你无法查看.",
                                "&8| &d诡异的魔力令你无法查看.",
                                "&8| &d诡异的魔力令你无法查看.",
                                "&8| &d诡异的魔力令你无法查看.",
                                "&8| &d诡异的魔力令你无法查看.",
                                "",
                                ""
                        ).toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                return false;
            }
        });
    }

    public IndicateBook setClassifies(Classifies classifies) {
        historyList.add(this.classifies.getID());
        this.classifies = classifies;

        user
                .addMetaOnHandItem(StNameSpace.GUIDE_NOW, classifies.getID())
                .addMetaOnHandItem(StNameSpace.GUIDE_HISTORY, String.join("‘", historyList));

        return this;
    }

    public IndicateBook lastLevel() {
        if (historyList.isEmpty()) return this;

        String last = historyList.get(historyList.size() - 1);

        // remove last one
        historyList.remove(historyList.size() - 1);

        String history = String.join("‘", historyList);
        user.addMetaOnHandItem(StNameSpace.GUIDE_HISTORY, history);
        user.addMetaOnHandItem(StNameSpace.GUIDE_NOW, last);
        classifies = AddonHolder.getINSTANCE().get(ClassifiesAddon.class).getClassifies(last);

//        System.out.println("now classifies = " + classifies + " | " + last);

        if ( classifies == null ) {

            return lastLevel();
        }

        refreshHand();

        return this;
    }

    private void drawLastLevel() {
        inventoryUI.setItem(49, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                // TODO: modify to owner name
                return new ItemBuilder(Material.ARROW).setName("§b◀ 返回上个界面")
                        .setLore(
                                "",
                                "&8| &7返回上一级",
                                "",
                                ""
                        ).toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {

                lastLevel().open();
                return false;
            }
        });
    }

    private void refreshHand() {
        if( classifies == null ) return;
        PlayerData playerData = PlayerData.g(player);
        PlayerSoul playerSoul = playerData.getPlayerSoul();
        PlayerInventory inventory = player.getInventory();

        ItemBuilder ib = new ItemBuilder(inventory.getItemInMainHand());

        ib.setLoreLine("&7正在查询: &e" + classifies.getName(), 7);
        ib.setLoreLine("&7解锁进度: &e" + playerSoul.getUnlockedCategories() + "/146852", 8);

        inventory.setItemInMainHand(ib.toItemStack());
    }
}
