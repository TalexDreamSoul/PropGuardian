package com.tagzxia3.te.src.main.java.com.talexs.talexessential.modules.attract;

import cn.hutool.core.date.DateUtil;
import com.google.gson.JsonParser;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.attract.CoolDown;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.attract.Expansion;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.attract.Particles;
import com.talexs.talexessential.data.PlayerData;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.Objects;

public class PlayerAttractData {

    @Getter
    private final PlayerData playerData;

    @Getter
    private com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.attract.Expansion expansion;

    @Getter
    private com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.attract.CoolDown coolDown;

    @Getter
    private com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.attract.Particles particles;

    @Getter
    @Setter
    private boolean enable;

    @Getter
    private int useAmo = 0, accItems = 0;

    @SneakyThrows
    public PlayerAttractData(PlayerData playerData) {

        this.playerData = playerData;

        YamlConfiguration yaml = playerData.getInfo();

        if ( !yaml.contains("AttractData") ) {

            this.expansion = new com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.attract.Expansion(this);
            this.coolDown = new com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.attract.CoolDown(this);
            this.particles = new com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.attract.Particles(this);
            this.enable = false;

        } else {

            this.enable = yaml.getBoolean("AttractData.enable");
            this.useAmo = yaml.getInt("AttractData.useAmo");
            this.accItems = yaml.getInt("AttractData.accItems");

            this.expansion = new Expansion(this, yaml.getInt("AttractData.Expansion.level"));
            this.coolDown = new CoolDown(this, yaml.getInt("AttractData.CoolDown", 0));

            this.particles = Particles.unSerialize(this, new JsonParser().parse(Objects.requireNonNull(yaml.getString("AttractData.Particles"))).getAsJsonObject());

        }

    }

    public void save() {
        YamlConfiguration yaml = playerData.getInfo();

        yaml.set("AttractData.enable", this.enable);
        yaml.set("AttractData.useAmo", this.useAmo);
        yaml.set("AttractData.accItems", this.accItems);
        yaml.set("AttractData.Expansion.level", this.expansion.getLevel());
        yaml.set("AttractData.CoolDown", this.coolDown.getLevel());
        yaml.set("AttractData.Particles", this.particles.serialize());

    }

    @Override
    public int hashCode() {

        return playerData.getName().hashCode();

    }

    @Override
    public boolean equals(Object obj) {

        if ( !( obj instanceof PlayerAttractData ) ) {
            return false;
        }

        PlayerAttractData target = (PlayerAttractData) obj;

        return target.hashCode() == this.hashCode();

    }

    long lastExecute = -1;

    public void trigger(Player player) {
        if ( !this.enable ) {
            return;
        }

        long now = System.currentTimeMillis();
        long diff = now - lastExecute;
        if ( diff < coolDown.getLevelCD() * 1000 ) {
            // 保留  2 位小数
            String ms = String.format("%.2f", ( coolDown.getLevelCD() - (diff / 1000)));
            player.sendActionBar("§7引波吸呐冷却中 §e" + ms + " §7秒!");
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }
        lastExecute = now;

        useAmo += 1;

        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        player.getWorld().playEffect(player.getLocation(), org.bukkit.Effect.ENDER_SIGNAL, 1);
//        player.playEffect(player.getLocation(), org.bukkit.Effect.ENDER_SIGNAL, 1);

        player.getWorld().getNearbyEntities(player.getLocation(), expansion.getLevel() + 3, expansion.getLevel() + 3, expansion.getLevel() + 3).forEach(entity -> {
            if ( !(entity instanceof Item) && (entity.getType() != EntityType.EXPERIENCE_ORB) ) {
                return;
            }

            accItems += 1;
            entity.teleport(player);
        });

    }

}
