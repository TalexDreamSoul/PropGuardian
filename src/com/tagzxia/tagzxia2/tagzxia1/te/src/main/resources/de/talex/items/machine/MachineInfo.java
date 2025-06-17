package com.tagzxia2.tagzxia1.te.src.main.resources.de.talex.items.machine;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import com.talexs.talexessential.TalexEssential;

import java.util.HashMap;
import java.util.Map;

public abstract class MachineInfo {

    protected Hologram hologram;
    @Getter
    @Setter
    private MachineStatus machineStatus = MachineStatus.PREPARING;
    @Getter
    @Setter
    private Map<String, Object> meta = new HashMap<>();

    public MachineInfo(Location loc) {

//        registerHolograms(loc);

    }

    public void registerHolograms(Location loc) {

        if ( hologram != null && !hologram.isDeleted() ) {
            return;
        }

        this.hologram = HologramsAPI.createHologram(TalexEssential.getInstance(), loc.clone());

        updateHologram();

    }

    public void unRegisterHolograms() {

        if ( hologram == null || hologram.isDeleted() ) {
            return;
        }

        hologram.delete();

    }

    public abstract void updateHologram();

    public enum MachineStatus {

        STOPPED("§c已停止!"),
        RUNNING("§e运行中.."),
        PREPARING("§f准备中..."),
        NEED_STH("§e§l缺失物品!!"),
        ERROR("§c§l发生错误!!!"),
        BROKEN("§3§l已损坏 >_( ");

        @Getter
        private final String displayName;

        MachineStatus(String displayName) {

            this.displayName = displayName;

        }

    }

}
