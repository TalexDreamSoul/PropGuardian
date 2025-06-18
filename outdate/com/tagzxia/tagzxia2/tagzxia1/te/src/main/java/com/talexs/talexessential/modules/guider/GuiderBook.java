package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.guider;//package com.talexs.talexessential.modules.guider;
//
//import com.talexs.soultech.addon.AddonHolder;
//import com.talexs.soultech.internal.entity.classfies.Classifies;
//import com.talexs.soultech.internal.machine.MachineAddon;
//import com.talexs.talexessential.data.PlayerData;
//import com.talexs.talexessential.data.player.PlayerUser;
//import com.talexs.soultech.internal.PlayerSoul;
//import com.talexs.soultech.internal.StNameSpace;
//import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
//import com.talexs.soultech.machine.bsae.BaseMachine;
//import com.talexs.soultech.machine.bsae.IMachineBuilder;
//import com.talexs.soultech.machine.machine_info.MachineList;
//import com.talexs.soultech.internal.RecipeObject;
//import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
//import com.talexs.talexessential.utils.inventory.InventoryPainter;
//import com.talexs.talexessential.utils.inventory.InventoryUI;
//import com.talexs.talexessential.utils.inventory.MenuBasic;
//import com.talexs.talexessential.utils.item.ItemBuilder;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.experimental.Accessors;
//import net.kyori.adventure.text.minimessage.MiniMessage;
//import org.bukkit.Material;
//import org.bukkit.Sound;
//import org.bukkit.enchantments.Enchantment;
//import org.bukkit.entity.Player;
//import org.bukkit.event.inventory.InventoryClickEvent;
//import org.bukkit.inventory.ItemFlag;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.PlayerInventory;
//
//import java.util.Objects;
//
//@Setter
//@Accessors(chain = true)
//public class GuiderBook extends MenuBasic {
//
//    @Getter
//    private Classifies categoryObject = SoulTech.getInstance().getCategoryManager().getRootCategory(), lastCategoryObject;
//
//    public GuiderBook setCategoryObject(CategoryObject co) {
//        if ( co == null || co == categoryObject ) {
//            categoryObject = SoulTech.getInstance().getCategoryManager().getRootCategory();
//        } else {
//            if ( co != lastCategoryObject )
//                this.lastCategoryObject = this.categoryObject;
//            this.categoryObject = co;
//        }
//
//        refreshHand();
//
//        return this;
//    }
//
//    @Getter
//    protected PlayerUser user;
//
//    @Getter
//    protected PlayerData pd;
//
//    public GuiderBook(Player player, String title) {
//        super(player, title, 5);
//
//        this.user = new PlayerUser(player);
//        this.pd = PlayerData.g(player);
//
//        pd.getPlayerSoul().setGuiderBook(this);
//    }
//
//    public GuiderBook(Player player) {
//        this(player, "&7[&5灵魂&b科技&7]");
//    }
//
//    @Override
//    public void SetupForPlayer(Player player) {
//        user = new PlayerUser(player);
//        String owner = user.getMetaOnHandItem(StNameSpace.OWNER);
//        // TODO
//        if (!Objects.equals(owner, player.getUniqueId().toString())) return;
//
//        new InventoryPainter(this).drawFull().drawBorder();
//
//        pd = PlayerData.g(player);
//        PlayerSoul playerSoul = pd.getPlayerSoul();
//
//        if ( !playerSoul.isEnabled() ) {
//            user.playSound(Sound.ENTITY_ALLAY_DEATH, 1, .2f);
//            inventoryUI.setItem(22, new InventoryUI.AbstractSuperClickableItem() {
//                @Override
//                public ItemStack getItemStack() {
//                    return new ItemBuilder(Material.GLOWSTONE_DUST)
//                            .addEnchant(Enchantment.ARROW_FIRE, 1)
//                            .addFlag(ItemFlag.HIDE_ENCHANTS)
//                            .setName("&5灵魂&7&ki&b科技")
//                            .setLore(
//                                    "",
//                                    "&8| &7赋新朔史鉴，魂迁技起经",
//                                    "&8| &7讽古科刻传，今安似明安",
//                                    "&8| &7夜起风惊笔，梦回雨湿衣",
//                                    "&8| &7一生不求闻，惟愿得见君",
//                                    "&8| &7已点缺澜壮，封愿得灵暗",
//                                    "",
//                                    "&8| &e千载 经 千载 · 以君复福晗",
//                                    "",
//                                    "&7&ki&a 壮阔执笔 · 启安",
//                                    "",
//                                    "&8#" + player.getUniqueId()
//                            )
//                            .toItemStack();
//                }
//
//                @Override
//                public boolean onClick(InventoryClickEvent e) {
//                    int unlockLevel = playerSoul.getUnlockLevel();
//
//                    if ( player.getLevel() < unlockLevel ) {
//
//                        user.closeInventory()
//                                .actionBar("&e你需要最少 &c" + unlockLevel + " &e级来打开 &5灵魂&b科技")
//                                .playSound(Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 1, 1)
//                                .playSound(Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1, 1)
//                                .playSound(Sound.ENTITY_ELDER_GUARDIAN_DEATH, 1, 1)
//                                .title("&5灵魂&7&ki&b科技", "&7赋新朔史鉴，魂迁技起经", 20, 60, 20);
//
//                        return false;
//
//                    }
//
//                    player.setLevel(player.getLevel() - unlockLevel);
//
//                    playerSoul.setEnabled(true);
//
//                    user.closeInventory();
//
//                    user.playSound(Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 1, 1)
//                            .playSound(Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1, 1)
//                            .playSound(Sound.ENTITY_ELDER_GUARDIAN_DEATH, 1, 1)
//                            .playSound(Sound.ENTITY_PLAYER_LEVELUP, 1, 1)
//                            .playSound(Sound.BLOCK_NOTE_BLOCK_BIT, 1, 1)
//                            .playSound(Sound.BLOCK_NOTE_BLOCK_GUITAR, 1, 1)
//                            .actionBar(MiniMessage.miniMessage().deserialize("<gradient:#1DE9B6:#26BBCD>您已解锁 灵魂科技 ！"))
//                            .title("&5灵魂&7&ki&b科技", "&7赋新朔史鉴，魂迁技起经", 20, 60, 20);
//
//                    return false;
//                }
//            });
//
//            return;
//        }
//
//        user.playSound(Sound.BLOCK_NOTE_BLOCK_FLUTE, 1, .2f);
//
//        if ( categoryObject != null && getCategoryObject().getCategoryType() == CategoryObject.CategoryType.OBJECT ) {
//            AddonHolder.getINSTANCE().get(MachineAddon.class).onRecipeView(GuiderBook.this);
//        } else {
//            drawCategories(player, user, pd);
//
//            if ( player.hasPermission("talex.essential.soultech.admin"))
//                drawMachineCategory();
//        }
//
//        if ( lastCategoryObject != null && categoryObject != SoulTech.getInstance().getCategoryManager().getRootCategory() ) drawLastLevel();
//    }
//
//    private void drawMachineCategory() {
//
//        inventoryUI.setItem(36, new InventoryUI.AbstractSuperClickableItem() {
//
//            @Override
//            public ItemStack getItemStack() {
//
//                return new ItemBuilder(Material.DROPPER).setName("§e机器列表").setLore("", "§8> §e查看已注册的机器列表.", "").toItemStack();
//            }
//
//            @Override
//            public boolean onClick(InventoryClickEvent e) {
//
//                new MachineList(player, 0).open();
//
//                return true;
//
//            }
//        });
//
//    }
//
//    private void drawCategories(Player player, PlayerUser user, PlayerData pd) {
//        PlayerSoul ps = pd.getPlayerSoul();
//
//        int slot = 10;
//
//        for (CategoryObject category : categoryObject.getChildren()) {
//
//            if ( category == null ) continue;
//
//            boolean doLock = ps.doCategoryUnlock(category.getID());
//            boolean prepositionLock = category.getPreposition().stream().anyMatch(categoryObject -> !ps.doCategoryUnlock(categoryObject.getID()));
//            if ( !prepositionLock )
//                inventoryUI.setItem(slot, new InventoryUI.AbstractSuperClickableItem() {
//                @Override
//                public ItemStack getItemStack() {
//                    ItemStack clone = category.getDisplayStack().clone();
//                    if ( !doLock ) {
//                        clone.setType(Material.BARRIER);
//                    }
//                    ItemBuilder itemBuilder = new ItemBuilder(clone);
//
//                    itemBuilder.addLoreLine("");
//
//                    for (CategoryObject categoryObject : category.getPreposition()) {
//
//                        if ( !ps.doCategoryUnlock(categoryObject.getID()) )
//                            itemBuilder.addLoreLine("&c✘ &7前置学科 &e" + categoryObject.getName() + " &7未解锁");
//                        else {
//                            itemBuilder.addLoreLine("&a✔ &7前置学科 &e" + categoryObject.getName() + " &7已解锁");
//                        }
//
//                        if ( doLock ) itemBuilder.addLoreLine("");
//
//                    }
//
//                    if ( !doLock ) {
//
//                        category.getUnlockConditions().forEach(condition -> {
//                            String name = condition.getDisplay(player, pd);
//                            if ( !condition.onCheck(player, pd, user) ) {
//                                itemBuilder.addLoreLine("&c✘ &e" + name);
//                            } else {
//                                itemBuilder.addLoreLine("&a✔ &e" + name);
//                            }
//                        });
//
//                        itemBuilder.addLoreLine("");
//
//                    }
//
//                    itemBuilder.addLoreLine(doLock ? "&a● 已解锁" : "&8○ &e未解锁");
//                    itemBuilder.addLoreLine("");
//
//                    if ( player.hasPermission("talex.essential.soultech.admin") ) {
//                        if ( category.getCategoryType() == CategoryObject.CategoryType.MENU )
//                            itemBuilder.addLoreLine("&eSHIFT + 左键 &a强制进入");
//                        else if ( category.getCategoryType() == CategoryObject.CategoryType.OBJECT )
//                            itemBuilder.addLoreLine("&eSHIFT + 左键 &a获得物品");
//                        else if ( category.getCategoryType() == CategoryObject.CategoryType.MACHINE )
//                            itemBuilder.addLoreLine("&eSHIFT + 左键 &a强制放置");
//                    }
//
//                    itemBuilder.addLoreLine("&8#" + category.getCategoryType().getDisplayName());
//
//                    return itemBuilder
//                            .toItemStack();
//                }
//
//                @Override
//                public boolean onClick(InventoryClickEvent e) {
//                    boolean hasPer = player.hasPermission("talex.essential.soultech.admin") && e.isShiftClick();
//
//                    if ( doLock || hasPer ) {
//
//                        if ( category.getCategoryType() == CategoryObject.CategoryType.MACHINE ) {
//
//                            String id = category.getID();
//                            String machineId = id.substring(11);
//
//                            BaseMachine machine = AddonHolder.getINSTANCE().get(MachineAddon.class).getMachine(machineId);
//                            if ( machine == null ) {
//                                player.closeInventory();
//                                return false;
//                            }
//
//                            if( !(machine instanceof IMachineBuilder) ) {
//
//                                user.errorActionBar("该机器不支持自动建造！");
//
//                                return false;
//                            }
//
//                            IMachineBuilder machineBuilder = (IMachineBuilder) machine;
//
//                            if ( !hasPer && !machineBuilder.checkMaterials(player, pd, user) ) return false;
//
//                            if ( !hasPer )
//                                machineBuilder.consumeMaterials(player, pd, user);
//
//                            user
//                                    .closeInventory()
//                                    .playSound(Sound.ENTITY_PLAYER_LEVELUP, .7f, .5f)
//                                    .addMetaOnHandItem(StNameSpace.GUIDE_AUTO_BUILD, machineId)
//                                    .infoActionBar("现在，右键一个方块来完成自动建造！");
//
//                            return false;
//
//                        } else if ( category.getCategoryType() == CategoryObject.CategoryType.OBJECT ) {
//
//                            if ( hasPer ) {
//                                RecipeObject recipeObject = category.getRecipeObject();
//
//                                if ( recipeObject instanceof WorkBenchRecipe ) {
//                                    WorkBenchRecipe wbr = (WorkBenchRecipe) recipeObject;
//                                    user.addItem( wbr.getExport().getItemBuilder().toItemStack() );
//                                } else {
//                                    user.addItem( category.getDisplayStack() );
//                                }
//
//                            }
////                                user.addItem(category.getDisplayStack());
//                            else {
//                                setCategoryObject(category);
//
//                                new InventoryPainter(GuiderBook.this)
//                                        .drawLine(10)
//                                        .drawLine(20)
//                                        .drawLine(30);
//
//                                AddonHolder.getINSTANCE().get(MachineAddon.class).onRecipeView(GuiderBook.this);
//                            }
//
//                            return false;
//                        }
//
//                        setCategoryObject(category).open();
//                        return false;
//                    }
//
//                    boolean could = category.getUnlockConditions().stream().allMatch(condition -> condition.onCheck(player, pd, user));
//                    if ( !could ) {
//                        user
//                                .playSound(Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 1, 1)
//                                .playSound(Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1, 1)
//                                .playSound(Sound.ENTITY_ELDER_GUARDIAN_DEATH, 1, 1)
//                                .errorActionBar("你还不能解锁这个.");
//                        return false;
//                    }
//
//                    for (IUnlockCondition unlockCondition : category.getUnlockConditions()) {
//
//                        unlockCondition.consume(player, pd);
//
//                    }
//
//                    if ( category.getCategoryType() == CategoryObject.CategoryType.MACHINE ) {
//                        ps.unlockMachine(category.getID().substring(11));
//                    }
//
//                    ps.unlockCategory(category.getID());
//
//                    String name = category.getName();
//
//                    user
//                            .firework()
//                            .title("&b✿", "&7你解锁了 &e" + name, 0 , 60, 40)
//                            .infoActionBar("你解锁了 " + name);
//
//                    open();
//
//                    return false;
//                }
//            });
//
//            if ( (slot + 2) % 9 == 0 ) {
//                slot += 3;
//            } else {
//                slot++;
//            }
//
//        }
//
//        inventoryUI.setItem(0, new InventoryUI.AbstractSuperClickableItem() {
//            @Override
//            public ItemStack getItemStack() {
//                return new ItemBuilder(Material.KNOWLEDGE_BOOK).setName("&e百科")
//                        .setLore(
//                                "",
//                                "&e你需要解锁 &c元素学 &e来查看.",
//                                ""
//                        )
//                        .toItemStack();
//            }
//
//            @Override
//            public boolean onClick(InventoryClickEvent e) {
//                return false;
//            }
//        });
//
//        inventoryUI.setItem(40, new InventoryUI.AbstractSuperClickableItem() {
//            @Override
//            public ItemStack getItemStack() {
//                // TODO: modify to owner name
//                return new ItemBuilder(Material.PLAYER_HEAD).setName(MiniMessage.miniMessage().deserialize("<gradient:#1DE9B6:#26BBCD>" + player.getName()))
//                        .setLore(
//                                "",
//                                "&8| &7赋新朔史鉴，魂迁技起经",
//                                "&8| &d诡异的魔力令你无法查看.",
//                                "&8| &d诡异的魔力令你无法查看.",
//                                "&8| &d诡异的魔力令你无法查看.",
//                                "&8| &d诡异的魔力令你无法查看.",
//                                "&8| &d诡异的魔力令你无法查看.",
//                                "&8| &d诡异的魔力令你无法查看.",
//                                "",
//                                ""
//                        ).toItemStack();
//            }
//
//            @Override
//            public boolean onClick(InventoryClickEvent e) {
//                return false;
//            }
//        });
//    }
//
//    public GuiderBook lastLevel() {
//        return setCategoryObject(lastCategoryObject);
//    }
//
//    private void drawLastLevel() {
//        inventoryUI.setItem(40, new InventoryUI.AbstractSuperClickableItem() {
//            @Override
//            public ItemStack getItemStack() {
//                // TODO: modify to owner name
//                return new ItemBuilder(Material.ARROW).setName("§b◀ 返回上个界面")
//                        .setLore(
//                                "",
//                                "&8| &7返回上一级",
//                                "",
//                                ""
//                        ).toItemStack();
//            }
//
//            @Override
//            public boolean onClick(InventoryClickEvent e) {
//                lastLevel().open();
//                return false;
//            }
//        });
//    }
//
//    private void refreshHand() {
//        if( categoryObject == null ) return;
//        PlayerInventory inventory = player.getInventory();
//
//        ItemBuilder ib = new ItemBuilder(inventory.getItemInMainHand());
//
//        ib.setLoreLine("&7正在查询: &e" + categoryObject.getName(), 7);
//
//        inventory.setItemInMainHand(ib.toItemStack());
//    }
//}
