package com.tagzxia.te.src.main.java.com.talexs.soultech.items.machine;

import com.talexs.soultech.internal.entity.preposition.IPreposition;
import com.talexs.soultech.internal.entity.unlock.IUnlockCondition;
import com.talexs.soultech.internal.entity.unlock.LevelCondition;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.talexessential.data.player.PlayerDataRunnable;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.machine.advanced_workbench.WorkBenchRecipe;
import com.talexs.talexessential.utils.block.TalexBlock;
import com.talexs.talexessential.utils.item.ItemBuilder;
import com.talexs.talexessential.utils.item.MineCraftItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemFlag;

import java.util.List;
import java.util.Set;

public class MachineCore extends BaseMachineItem {

    public MachineCore() {

        super("machine_core", new ItemBuilder(Material.OBSERVER).addEnchant(Enchantment.DURABILITY, 1).addFlag(ItemFlag.HIDE_ENCHANTS).setName("§b机器核心")
                .setLore("", "§8| §c是心中的炽焰.", "&8| &4拆卸后产生巨额爆炸", "").toItemStack());

    }

    /**
     * 当放置的方块被破坏时
     *
     * @param playerData 玩家数据
     * @param event      事件传递
     *
     * @return 返回真则从BlockManager中删除
     */
    @Override
    public boolean onItemBlockBreak(PlayerData playerData, TalexBlock tb, BlockBreakEvent event) {

        for ( int i = 0; i < 5; ++i ) {

            tb.getLoc().getWorld().createExplosion(tb.getLoc(), 3.5F, true, true);

            int finalI = i;
            new PlayerUser(event.getPlayer()).delayRun(new PlayerDataRunnable() {
                @Override
                public void run() {
                    tb.getLoc().getWorld().createExplosion(tb.getLoc(), 15.5F, false, false);

                    new PlayerUser(event.getPlayer()).delayRun(new PlayerDataRunnable() {
                        @Override
                        public void run() {
                            tb.getLoc().getWorld().createExplosion(tb.getLoc(), 25.5F, false, false);
                        }
                    }, 80 * finalI);
                }
            }, 40 * i);

        }

        return true;
    }

    @Override
    public WorkBenchRecipe getRecipe() {

        return new WorkBenchRecipe("st_machine_core", this)

                .addRequired(new MineCraftItem(Material.OBSIDIAN))
                .addRequired(new MineCraftItem(Material.TUFF))
                .addRequired(new MineCraftItem(Material.OBSIDIAN))
                .addRequired(new MineCraftItem(Material.DEEPSLATE))
                .addRequired(new MineCraftItem(Material.END_CRYSTAL))
                .addRequired(new MineCraftItem(Material.FURNACE))
                .addRequired(new MineCraftItem(Material.OBSIDIAN))
                .addRequired(new MineCraftItem(Material.TUFF))
                .addRequired(new MineCraftItem(Material.OBSIDIAN))
                ;

    }

    @Override
    public Set<IUnlockCondition> getUnlockConditions() {
        return Set.of(new LevelCondition(10));
    }
}
