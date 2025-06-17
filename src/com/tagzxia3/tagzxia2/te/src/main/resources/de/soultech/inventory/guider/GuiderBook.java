package com.tagzxia3.tagzxia2.te.src.main.resources.de.soultech.inventory.guider;

import com.tagzxia.te.src.main.resources.de.soultech.inventory.guider.BaseGuider;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import com.talexs.talexessential.TalexEssential;
import pubsher.talexsoultech.entity.PlayerData;
import pubsher.talexsoultech.inventory.InventoryPainter;
import pubsher.talexsoultech.inventory.machine_info.MachineList;
import pubsher.talexsoultech.talex.BaseTalex;
import pubsher.talexsoultech.talex.guider.category.CategoryObject;
import pubsher.talexsoultech.utils.inventory.InventoryUI;
import pubsher.talexsoultech.utils.item.ItemBuilder;
import pubsher.talexsoultech.utils.item.TalexItem;

import java.util.ArrayList;
import java.util.HashSet;

@Getter
public class GuiderBook extends BaseGuider {

    private final int start;

    private final CategoryObject category;

    private final GuiderBook lastGuiderBook;

    private final GuiderBook instance = this;

    public GuiderBook(PlayerData playerData, int start, CategoryObject category, GuiderBook last) {

        super(playerData, "一览", 5);

        this.start = start;
        this.category = category;
        this.lastGuiderBook = last;

        playerData.playSound(Sound.BLOCK_NOTE_PLING, 1.0F, 1.0F).setLastGuider(this);

    }

    public GuiderBook(PlayerData playerData) {

        this(playerData, 0, playerData.getTalex().getCategoryManager().getRootCategory(), null);

    }

    @Override
    public boolean allowPutItem(String inventorySymbol) {

        return false;
    }

    @Override
    public void onCloseMenu(InventoryCloseEvent e) {

    }

    @Override
    public void onTryCloseMenu(InventoryCloseEvent e) {

    }

    @Override
    public void SetupForPlayer(Player player, PlayerData playerData) {

        if ( category == null || category.getChildren() == null ) {

            new InventoryPainter(this) {

                @Override
                public InventoryUI.ClickableItem onDrawFull(int slot) {

                    return new InventoryUI.EmptyClickableItem(new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short) 14).setName("§c无法正确加载分类信息,请联系管理员!").toItemStack());

                }

            }.drawFull();
            return;

        }

        new InventoryPainter(this) {

            @Override
            public InventoryUI.ClickableItem onDrawFull(int slot) {

                return new InventoryUI.EmptyClickableItem(new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short) 8).setName("§7").toItemStack());

            }

        }.drawFull().drawBorder();

        if ( lastGuiderBook != null ) {

            inventoryUI.setItem(40, new InventoryUI.AbstractSuperClickableItem() {

                @Override
                public ItemStack getItemStack() {

                    return new ItemBuilder(Material.ARROW).setName("§b◀ 返回上个界面").toItemStack();

                }

                @Override
                public boolean onClick(InventoryClickEvent e) {

                    playerData.playSound(Sound.ENTITY_BAT_TAKEOFF, 1.0F, 1.0F).setLastGuider(lastGuiderBook);

                    if ( start != 0 ) {

                        GuiderBook gb = lastGuiderBook.getLastGuiderBook();

                        if ( gb != null ) {

                            gb.open(true);
                            return true;

                        }

                    }

                    lastGuiderBook.open(true);

                    return true;

                }
            });

        }

        inventoryUI.setItem(0, new InventoryUI.AbstractSuperClickableItem() {

            @Override
            public ItemStack getItemStack() {

                return new ItemBuilder(Material.BOOK).setName("§e一览").setLore("", "§8> §e快速返回主菜单.", "").toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {

                new GuiderBook(playerData, 0, BaseTalex.getInstance().getCategoryManager().getRootCategory(), getInstance()).open();

                return true;

            }
        });

        inventoryUI.setItem(36, new InventoryUI.AbstractSuperClickableItem() {

            @Override
            public ItemStack getItemStack() {

                return new ItemBuilder(Material.DROPPER).setName("§e机器列表").setLore("", "§8> §e查看已注册的机器列表.", "").toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {

                new MachineList(playerData, 0).open();

                return true;

            }
        });

        if ( category.getCategoryType() == CategoryObject.CategoryType.OBJECT ) {

            BaseTalex.getInstance().getMachineManager().onRecipeView(playerData, this);

            return;

        }

        int startSlot = 10, i = -1;

        for ( CategoryObject categoryObject : new ArrayList<>(category.getChildren()) ) {

            ++i;
            if ( i < start - 1 ) {
                continue;
            }

            final boolean[] lock = { playerData.isCategoryUnLock(categoryObject.getID()) };

            inventoryUI.setItem(startSlot, new InventoryUI.AbstractSuperClickableItem() {

                @Override
                public boolean onClick(InventoryClickEvent e) {

                    if ( e.isShiftClick() && e.isRightClick() && categoryObject.getCategoryType() == CategoryObject.CategoryType.OBJECT && player.hasPermission("talex.soultech.admin") ) {

                        categoryObject.getRecipeObject().getDisplayItem().addToPlayer(player);

                        playerData.actionBar("§a§l你获得了: §r" + categoryObject.getRecipeObject().getRecipeID()).playSound(Sound.ENTITY_VILLAGER_YES, 1.0F, 1.0F);

                        return true;

                    }

                    if ( !lock[0] && !( e.isShiftClick() && e.isRightClick() && player.hasPermission("talex.soultech.admin") ) ) {

                        playerData.actionBar("&c&l抱歉,这个类别你还未解锁!").playSound(Sound.BLOCK_ANVIL_LAND, 1.1F, 1.1F);

                    } else {

                        new GuiderBook(playerData, 0, categoryObject, instance).open();

                    }

                    return true;

                }

                @Override
                public ItemStack getItemStack() {

                    ItemStack stack = categoryObject.getDisplayStack();

                    if ( stack.getType() == Material.AIR ) {

                        TalexEssential.getInstance().getLogger().warning("--> CategoryObject: " + categoryObject.getID() + " # 触发了异常 | Display的物品类型不可以为 AIR !");
                        return null;

                    }

                    ItemBuilder ib = new ItemBuilder(TalexItem.reSerialize(categoryObject.getDisplayStack().clone()));

                    ib.addLoreLine("§7--------------------------------");

                    if ( categoryObject.getPreposition() != null && categoryObject.getPreposition().size() > 0 ) {

                        ib.addLoreLine("§f前置学科: ");

                        int i = 0;

                        for ( CategoryObject preposition : new HashSet<>(categoryObject.getPreposition()) ) {

                            i++;

                            boolean unlock = playerData.isCategoryUnLock(preposition.getID());

                            ib.addLoreLine(( unlock ? "  §a✦ §7" : "  §8✧ §7" ) + preposition.getDisplayStack().getItemMeta().getDisplayName());

                            if ( i >= 3 ) {

                                break;

                            }

                        }

                        if ( categoryObject.getPreposition().size() > 3 ) {

                            ib.addLoreLine("§7  等更多 " + ( categoryObject.getPreposition().size() - 3 ) + " 项...");

                        }

                    } else {

                        playerData.addCategoryUnlock(categoryObject.getID());
                        lock[0] = true;

                    }

                    ib.addLoreLine("");
                    ib.addLoreLine(lock[0] ? "§a✔ 已解锁" : "§c✘ 未解锁");

                    if ( lock[0] ) {

                        ib.addLoreLine("");
                        ib.addLoreLine("§7§k|§a 点击打开...");

                    }

                    if ( categoryObject.getCategoryType() == CategoryObject.CategoryType.OBJECT && player.hasPermission("talex.soultech.admin") ) {

                        ib.addLoreLine("");
                        ib.addLoreLine("§eSHIFT + 右键 §7来获得这个物品!");

                    }

                    if ( categoryObject.getCategoryType() == CategoryObject.CategoryType.MENU && player.hasPermission("talex.soultech.admin") ) {

                        ib.addLoreLine("");
                        ib.addLoreLine("§eSHIFT + 右键 §a强制进入类别.");

                    }

                    ib.isTrueAccessEnchant(lock[0], Enchantment.DURABILITY, 1);
                    ib.addFlag(ItemFlag.HIDE_ENCHANTS);

                    return ib.toItemStack();

                }

            });

            startSlot++;

            if ( ( startSlot + 1 ) % 9 == 0 ) {

                startSlot += 2;

            }

            if ( startSlot >= 35 ) {

                break;

            }

        }

        int maxPage = category.getChildren().size() / 21;

        if ( category.getChildren().size() % 21 != 0 ) {
            maxPage++;
        }

        if ( maxPage != 0 ) {

            int nowPage = start / 21;

            if ( startSlot % 21 != 0 ) {
                nowPage++;
            }

            if ( nowPage == 1 && maxPage != 1 ) {

                placeNextPage(playerData, nowPage, maxPage);

            } else if ( nowPage == maxPage ) {

                placePreviousPage(playerData, nowPage, maxPage);

            } else if ( maxPage != 1 ) {

                placeNextPage(playerData, nowPage, maxPage);

                placePreviousPage(playerData, nowPage, maxPage);

            }

        }

//        System.out.println("总数: " + category.getChilds().size() + " | 当前: " + nowPage + "/" + maxPage);

    }

    private void placeNextPage(PlayerData playerData, int now, int max) {

        if ( now == max ) {
            return;
        }

        inventoryUI.setItem(41, new InventoryUI.AbstractSuperClickableItem() {

            @Override
            public ItemStack getItemStack() {

                return new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short) 3).setName("§a下一页   §8(§a" + now + "§7/§e" + max + "§8)").toItemStack();

            }

            @Override
            public boolean onClick(InventoryClickEvent e) {

                new GuiderBook(playerData, now + 21, getCategory(), getLastGuiderBook()).open();

                return true;

            }
        });

    }

    private void placePreviousPage(PlayerData playerData, int now, int max) {

        if ( now == 1 ) {
            return;
        }

        inventoryUI.setItem(39, new InventoryUI.AbstractSuperClickableItem() {

            @Override
            public ItemStack getItemStack() {

                return new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short) 3).setName("§a上一页   §8(§a" + now + "§7/§e" + max + "§8)").toItemStack();

            }

            @Override
            public boolean onClick(InventoryClickEvent e) {

                new GuiderBook(playerData, now - 21, getCategory(), getLastGuiderBook()).open();

                return true;

            }
        });

    }

}
