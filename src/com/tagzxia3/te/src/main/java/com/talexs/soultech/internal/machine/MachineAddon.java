package com.tagzxia3.te.src.main.java.com.talexs.soultech.internal.machine;

import cn.hutool.core.codec.Base64;
import com.talexs.soultech.addon.AddonHolder;
import com.talexs.soultech.addon.BaseAddon;
import com.talexs.soultech.internal.book.IndicateBook;
import com.talexs.soultech.internal.protector.ProtectorAddon;
import com.talexs.soultech.machine.synthesizer.SynthesizerMachine;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.data.PlayerData;
import com.talexs.soultech.machine.advanced_workbench.AdvancedWorkBench;
import com.talexs.soultech.machine.break_hammer.BreakHammerMachine;
import com.talexs.soultech.machine.bsae.BaseMachine;
import com.talexs.soultech.machine.compressor.NormalCompressor;
import com.talexs.soultech.machine.drawer.VanityDrawer;
import com.talexs.soultech.machine.furnace_cauldron.FurnaceCauldronMachine;
import com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.utils.LogUtil;
import com.talexs.talexessential.utils.block.TalexBlock;
import com.talexs.talexessential.utils.inventory.InventoryPainter;
import com.talexs.talexessential.utils.inventory.InventoryUI;
import lombok.SneakyThrows;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MachineAddon extends BaseAddon {

    private final HashMap<String, BaseMachine> machines = new HashMap<>(32);

    public HashSet<Map.Entry<String, BaseMachine>> getMachinesClone() {

        return new HashSet<>(machines.entrySet());

    }

    public @Nullable BaseMachine getMachine(@NotNull String machineName) {

        return machines.get(machineName);

    }

    public void registerMachine(BaseMachine baseMachine) {

        this.machines.put(baseMachine.getMachineName(), baseMachine);

    }

    public void unRegisterMachine(BaseMachine baseMachine) {

        this.machines.remove(baseMachine.getMachineName(), baseMachine);

    }

    public void onRecipeView(IndicateBook guiderBook) {

        new InventoryPainter(guiderBook)
                .drawLine(10)
                .drawLine(20)
                .drawLine(30);

        for ( Map.Entry<String, BaseMachine> entry : machines.entrySet() ) {

            BaseMachine bm = entry.getValue();

            if ( bm.onOpenRecipeView(guiderBook) ) {

                guiderBook.inventoryUI.setItem(19, new InventoryUI.AbstractClickableItem(bm.getDisplayItem()) {

                    @Override
                    public boolean onClick(InventoryClickEvent e) {

                        bm.onOpenMachineInfoViewer((Player) e.getWhoClicked());

                        return true;

                    }

                });

                return;

            }

        }

    }

    public void onEvent(PlayerEvent event) {

        PlayerData playerData = PlayerData.g(event);

        for ( Map.Entry<String, BaseMachine> entry : machines.entrySet() ) {

            if ( entry.getValue().getMachineChecker().onCheck(event) ) {

                event.getPlayer().closeInventory();

                if ( !AddonHolder.getINSTANCE().get(ProtectorAddon.class).checkProtect(playerData, event) ) {
                    new PlayerUser(event.getPlayer())
                            .playSound(Sound.BLOCK_ANVIL_HIT, 1, 1)
                            .errorActionBar("你无法使用这个！");
                    return;
                }

                boolean doLock = event.getPlayer().hasPermission("te.soultech.machine.bypassunlock") || playerData.getPlayerSoul().doMachineUnlock(entry.getValue().getMachineName());

                if ( doLock )
                    entry.getValue().onOpenMachine(playerData, event);

                return;

            }

        }

    }

    public boolean onBlockBreak(TalexBlock tblock) {
        for ( Map.Entry<String, BaseMachine> entry : machines.entrySet() ) {

            if ( entry.getValue().onBreakMachine(tblock) ) {

                return true;

            }

        }

        return false;
    }

    @SneakyThrows
    public void loadAllMachines() {
        File file = new File(TalexEssential.getInstance().getDataFolder() + "/caches/Machines.yml");

        if ( !file.exists() ) {
            return;
        }

        YamlConfiguration yaml = new YamlConfiguration();

        yaml.load(file);

        for ( Map.Entry<String, BaseMachine> item : getMachinesClone() ) {

            try {

                item.getValue().onLoad(Base64.decodeStr(yaml.getString("Machines." + item.getKey() + ".data", "")));

//                LogUtil.log("§7[§5灵魂§b科技§7] §8[存储] §e" + item.getKey() + " §7机器加载完毕!");

            } catch ( Exception e ) {

                LogUtil.log("§c[§4SoulTech§c] §c机器数据加载失败 " + item.getKey());
                e.printStackTrace();

            }

        }

    }

    @SneakyThrows
    public void SaveAllMachines() {
        File file = new File(TalexEssential.getInstance().getDataFolder() + "/caches/Machines.yml");

        if ( !file.exists() ) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        YamlConfiguration yaml = new YamlConfiguration();

        for ( Map.Entry<String, BaseMachine> item : getMachinesClone() ) {

            try {

                yaml.set("Machines." + item.getKey() + ".data", Base64.encode(item.getValue().onSave()));

                LogUtil.log("§7[§5灵魂§b科技§7] §8[存储] §e" + item.getKey() + " §7机器保存完毕!");
            } catch ( Exception e ) {

                LogUtil.log("§c[§4SoulTech§c] §c机器数据保存失败 " + item.getKey());
                e.printStackTrace();

            }

        }

        yaml.save(file);

    }

    @Override
    public void onEnable() {
        registerMachine(new AdvancedWorkBench());
        registerMachine(new FurnaceCauldronMachine());
        registerMachine(new BreakHammerMachine());
        registerMachine(new VanityDrawer());
        registerMachine(new NormalCompressor());
        registerMachine(new SynthesizerMachine());

        loadAllMachines();
    }

    @Override
    public void onDisable() {
        SaveAllMachines();
    }
}
