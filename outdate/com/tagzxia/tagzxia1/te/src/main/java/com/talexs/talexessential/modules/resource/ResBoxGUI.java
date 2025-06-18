package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.resource;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.resource.PlayerRes;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.resource.ResModule;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.MenuBasic;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import com.talexs.talexessential.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ResBoxGUI extends MenuBasic {
    public ResBoxGUI(Player player) {
        super(player, "&7[&e战利品&7]", 5);
    }

    @Override
    public void SetupForPlayer(Player player) {
        player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

        PlayerData pd = PlayerData.g(player);
        com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.resource.PlayerRes pr = pd.getPlayerRes();

        new InventoryPainter(this) {

            @Override
            public InventoryUI.ClickableItem onDrawFull(int slot) {

                if ( com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.resource.PlayerRes.slots.contains(slot) )
                    return new InventoryUI.AbstractSuperClickableItem() {
                        @Override
                        public ItemStack getItemStack() {

                            PlayerRes.ResSlot resSlot = pr.getSlotMapper().get(slot);
                            if ( resSlot.getStack() == null ) {
                                return new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("&7空槽位").toItemStack();
                            }

                            return new ItemBuilder(resSlot.getStack().clone())
                                    .setLore("", "&8| &7这是你探索的资源", "&8| 当你成功退出挑战后即可获取", "")
                                    .toItemStack();
                        }

                        @Override
                        public boolean onClick(InventoryClickEvent e) {
                            return false;
                        }
                    };

                return super.onDrawFull(slot);
            }

        }.drawFull().drawBorder();

        if ( player.getWorld() != ResModule.INS.getWorld() )
          inventoryUI.setItem(40, new InventoryUI.AbstractSuperClickableItem() {
            @Override
            public ItemStack getItemStack() {
                return new ItemBuilder(Material.SUNFLOWER).setName("&e获得")
                        .addEnchant(Enchantment.DAMAGE_ALL, 1)
                        .addFlag(ItemFlag.HIDE_ENCHANTS)
                        .setLore("", "&8| &7这是你的奖励列表", "&8| &7请在一个安全的地方点击", "&8| &7所有战利品会掉落在你的脚下", "", "&7&k|&a 点击获取.", "").toItemStack();
            }

            @Override
            public boolean onClick(InventoryClickEvent e) {
                player.closeInventory();

                pr.getSlotMapper().values().forEach(slot -> {
                    ItemStack stack = slot.getStack();
                    if ( stack != null ) {

                        if ( stack.getType().name().contains("ORE") ) {
                            stack.setAmount((int) (stack.getAmount() * 1.5));
                        } else if ( stack.getType() == Material.GRAVEL || stack.getType() == Material.SAND ) {
                            stack.setAmount((int) (stack.getAmount() * 2));
                        }

                        player.getWorld().dropItem(player.getLocation(), stack);
//                        player.getWorld().dropItemNaturally(player.getLocation(), slot.getStack());
//                        slot.setStack(null);
                    }
                });

                pr.clear();
                pr.save();

                return false;
            }
        });

    }
}
