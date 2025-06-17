package com.tagzxia.tagzxia1.te.src.main.resources.de.talex.machine.fermentation;

import com.tagzxia.te.src.main.resources.de.talex.machine.fermentation.FermentationObject;
import com.tagzxia.te.src.main.resources.de.talex.machine.fermentation.FermentationRecipe;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import pubsher.talexsoultech.data.enums.LocationFloat;
import pubsher.talexsoultech.entity.PlayerData;
import pubsher.talexsoultech.inventory.InventoryPainter;
import pubsher.talexsoultech.inventory.guider.BaseGuider;
import pubsher.talexsoultech.talex.BaseTalex;
import pubsher.talexsoultech.talex.guider.category.CategoryObject;
import pubsher.talexsoultech.utils.inventory.InventoryUI;
import pubsher.talexsoultech.utils.item.ItemBuilder;
import pubsher.talexsoultech.utils.item.SoulTechItem;
import pubsher.talexsoultech.utils.item.TalexItem;

import java.util.ArrayList;

/**
 * <p>
 * {@link # pubsher.talexsoultech.talex.machine.furnace_cauldron }
 *
 * @author TalexDreamSoul
 * @date 2021/8/14 4:22
 * <p>
 * Project: TalexSoulTech
 * <p>
 */
public class FermentationGUI extends BaseGuider {

    private final FermentationObject object;

    public FermentationGUI(PlayerData activePlayerData, FermentationObject fermentationObject) {

        super(activePlayerData, "§c冶炼熔炉 ", 6);

        this.object = fermentationObject;
        ;

        inventoryUI.setAutoRefresh(true);

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

        InventoryPainter inventoryPainter = new InventoryPainter(this).drawFull().drawArena9(20, new InventoryUI.AbstractSuperClickableItem() {

            @Override
            public ItemStack getItemStack() {

                return new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short) 14)
                        .setName("§e放置冶炼物品").toItemStack();

            }

            @Override
            public boolean onClick(InventoryClickEvent e) {

                return true;
            }

        });

        double percent = object.getProgressPercent();

        if ( object.getSaveItems().size() >= 9 ) {

            new InventoryPainter(this) {

                @Override
                public InventoryUI.ClickableItem onDrawLine(int slot) {

                    return new InventoryUI.EmptyCancelledClickableItem(new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short) 14).setName("§c空位不足").toItemStack());

                }
            }.drawLineRow(6);

        } else {

            inventoryPainter.drawProgressBarHorizontal(5, 7, percent, LocationFloat.FLOAT_CENTER, new InventoryUI.EmptyCancelledClickableItem(

                    new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short) 3).setName("§a*").toItemStack()

            ), new InventoryUI.EmptyCancelledClickableItem(new ItemBuilder(Material.STAINED_GLASS_PANE).setName("§a*").setDurability((short) 4).toItemStack()));

        }

        inventoryUI.setItem(20, new InventoryUI.AbstractSuperClickableItem() {

            @Override
            public ItemStack getItemStack() {

                ItemBuilder builder;

                if ( object.getProcessingItem() == null ) {

                    builder = new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short) 8);

                    builder.setName("§b点击放置物品");

                } else {

                    builder = new ItemBuilder(object.getProcessingItem());

                }

                return builder.toItemStack();

            }

            @Override
            public boolean onClick(InventoryClickEvent e) {

                ItemStack stack = e.getCursor().clone();

                if ( object.getProcessingItem() == null ) {

                    if ( stack == null || stack.getType() == Material.AIR ) {
                        return true;
                    }

                    FermentationRecipe recipe = searchCategory(stack);

                    if ( recipe == null ) {

                        playerData.actionBar("§c§l这个物品无法冶炼!");

                        playerData.closeInventory();

                        return true;

                    }

                    object.setOwnRecipe(recipe);
                    e.setCursor(null);
                    object.setProcessingItem(stack);

                } else {

                    playerData.getPlayer().getInventory().addItem(object.getProcessingItem());

                    object.setProcessingItem(null);
                    object.setRun(false);

                }

                open(true);

                return true;

            }
        });

        int startSlot = 14;

        for ( TalexItem item : new ArrayList<>(object.getSaveItems()) ) {

            inventoryUI.setItem(startSlot, new InventoryUI.AbstractClickableItem(item.getItemBuilder().toItemStack()) {

                @Override
                public boolean onClick(InventoryClickEvent e) {

                    if ( object.getSaveItems().remove(item) ) {

                        if ( item instanceof SoulTechItem ) {

                            ( (SoulTechItem) item ).onCrafted(playerData);

                        }

                        e.getWhoClicked().getInventory().addItem(item.getItemBuilder().toItemStack());
                        playerData.playSound(Sound.ITEM_ARMOR_EQUIP_CHAIN, 1.1f, 1.2f);

                    }

                    return false;

                }
            });

            startSlot++;

            if ( ( startSlot + 1 ) % 9 == 0 ) {

                startSlot += 6;

            }

            if ( startSlot >= 44 ) {
                break;
            }

        }

    }

    private FermentationRecipe searchCategory(ItemStack stack) {

//        if(stack == null || stack.getItemMeta() == null) { return false; }

        for ( CategoryObject categoryObject : BaseTalex.getInstance().getCategoryManager().getCategories() ) {

            if ( categoryObject.getCategoryType() == CategoryObject.CategoryType.OBJECT && categoryObject.getRecipeObject() instanceof FermentationRecipe ) {

                FermentationRecipe fcd = (FermentationRecipe) categoryObject.getRecipeObject();

                TalexItem ti = fcd.getNeed();

                if ( ti != null && ti.getItemBuilder().toItemStack().getType() != Material.AIR ) {

                    ti = new TalexItem(ti.getItemBuilder()).addIgnoreType(TalexItem.VerifyIgnoreTypes.IgnoreAmount);

                    if ( ti.verify(stack) ) {

                        return fcd;

                    }

                }

            }

        }

        return null;

    }

}
