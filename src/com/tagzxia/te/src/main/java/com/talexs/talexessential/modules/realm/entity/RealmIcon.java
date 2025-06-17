package com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.realm.entity;

import com.talexs.talexessential.modules.realm.PlayerRealm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Define a realm icon.
 *
 * @author TalexDreamSoul
 * @since 1.0.0
 * 23/08/24 下午 10:30
 * Copyright (C) 2023-current TalexDreamSoul - All Rights Reserved
 */
public class RealmIcon {

    @Getter
    private String material;

    public RealmIcon(PlayerRealm pr) {
        String str = pr.getInfo().getString("Manifest.Flag.Icon.Type");
        if ( str != null && !str.isEmpty() ) this.material = str;
    }

    public RealmIcon(String material) {
        this.material = material;
    }

    public void setIcon(@NotNull Material material) {
        this.material = material.name();
    }

    public void setIcon(String hdbId) {
        material = hdbId;
    }

    public @NotNull ItemStack getIcon() {
        if ( this.material == null || this.material.isEmpty() ) return new ItemStack(Material.GRASS_BLOCK);

        try {

            return new ItemStack(Material.valueOf(this.material));

        } catch ( Exception e ) {

            // TODO hdb head id parser

            e.printStackTrace();

            return new ItemStack(Material.GRASS_BLOCK);

        }

    }

    public void save(PlayerRealm pr) {
        pr.getInfo().set("Manifest.Flag.Icon.Type", this.material);
    }

}
