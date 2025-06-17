package com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.souvenir;

import com.talexs.talexessential.modules.BaseModule;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class SouvenirModule extends BaseModule {

    public static SouvenirModule ins;

    public static List<Souvenir> souvenirs = new ArrayList<>();

    public SouvenirModule() {
        super("souvenirs");

        ins = this;
    }

    public boolean doPlayerUnlock(Player player, String key) {
        return super.yaml.contains("Manifest.Souvenirs." + key + ".players." + player.getUniqueId() + ".name");
    }

    public long getPlayerUnlockTime(Player player, String key) {
        return super.yaml.getLong("Manifest.Souvenirs." + key + ".players." + player.getUniqueId() + ".time", -1);
    }

    public int getSouvenirAmount(String key) {
        return super.yaml.getInt("Manifest.Souvenirs." + key + ".amo", 0);
    }

    @SneakyThrows
    public void addSouvenirPlayer(String key, Player player) {
        int anInt = super.yaml.getInt("Manifest.Souvenirs." + key + ".amo", 0);

        super.yaml.set("Manifest.Souvenirs." + key + ".amo", anInt + 1);
        yaml.set("Manifest.Souvenirs." + key + ".players." + player.getUniqueId() + ".name", player.getName());
        yaml.set("Manifest.Souvenirs." + key + ".players." + player.getUniqueId() + ".time", System.currentTimeMillis());

        yaml.save(super.file);
    }

    private void _de(@NotNull Set<String> souvenirs) {
        if (souvenirs.isEmpty()) return;

        souvenirs.forEach(souvenir -> {
            String path = "Souvenirs." + souvenir;

            SouvenirType type = SouvenirType.valueOf(super.yaml.getString(path + ".type"));
            String title = super.yaml.getString(path + ".title");
            List<String> description = super.yaml.getStringList(path + ".lore");
            String id = super.yaml.getString(path + ".head");

            Material material = yaml.contains(path + ".item") ?
                    Material.valueOf(super.yaml.getString(path + ".item")) : null;

            SouvenirModule.souvenirs.add(
                    new Souvenir().setKey(souvenir)
                            .setMaterial(material)
                            .setType(type).setTitle(title).setDesc(description).setHeadId(id)
            );
        });
    }

    @Override
    public void onEnable() {
        _de(Objects.requireNonNull(super.yaml.getConfigurationSection("Souvenirs")).getKeys(false));
    }
}
