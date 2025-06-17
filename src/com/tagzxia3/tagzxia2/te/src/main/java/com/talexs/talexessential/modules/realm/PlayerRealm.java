package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.realm;

import cn.hutool.core.codec.Base64;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.realm.RealmArea;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.realm.RealmSets;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.builder.SqlAddBuilder;
import com.talexs.talexessential.builder.SqlBuilder;
import com.talexs.talexessential.builder.SqlUpdBuilder;
import com.talexs.talexessential.modules.realm.entity.RealmIcon;
import com.talexs.talexessential.utils.LogUtil;
import com.talexs.talexessential.utils.NBTsUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Accessors(chain = true )
public class PlayerRealm {

    private UUID uuid, ownerUUID;

    private String name, owner;

    private String serverName;

    private RealmArea realmArea;

    private RealmIcon realmIcon;

    private long created = System.currentTimeMillis(), updated;

    private YamlConfiguration info = new YamlConfiguration();

    public boolean doVisible(Player player) {
        if ( doPlayerHasPer(player) ) return true;

        return getFlag(RealmSets.VISIBLE);
    }

    public boolean use(Player player) {
        if ( doPlayerHasPer(player) ) return true;

        return getFlag(RealmSets.USE);
    }

    public void transmitOwner(Player newOwner) {
        this.ownerUUID = newOwner.getUniqueId();
        this.owner = newOwner.getName();
    }

    public boolean doPlayerHasPer(Player player) {
        if ( player.hasPermission("talex.realm.admin") ) return true;

        String per = info.getString("Permissions." + player.getUniqueId() + ".per", "");

        if (per.equals("temp")) {
            Player ownerPlayer = Bukkit.getPlayer(ownerUUID);
            if ( ownerPlayer == null ) return false;
            return ownerPlayer.isOnline();
        } else if (per.equals("has")) {
            return true;
        }

        return player.getUniqueId().equals(this.ownerUUID);
    }

    public boolean getFlag(String flag, boolean def) {
        return info.getBoolean("Flag." + flag, def);
    }

    public boolean getFlag(String flag) {
        return getFlag(flag, false);
    }

    @Deprecated
    public boolean getFlag(RealmSets set) {
        return getFlag(set.name());
    }

    @Deprecated
    public void setFlag(RealmSets set, boolean value) {
        info.set("Flag." + set.name(), value);
    }

    public void setFlag(String name, boolean value) {
        info.set("Flag." + name, value);
    }

    public void setPlayerPer(Player player, String per) {
        info.set("Permissions." + player.getUniqueId() + ".per", per);
        info.set("Permissions." + player.getUniqueId() + ".name", player.getName());
    }

    public Location getTeleportLocation() {
        String loc = info.getString("TeleportLocation", "");
        if ( !loc.isEmpty() ) {
            Location l = NBTsUtil.getLocation(loc);
            if ( l != null ) {
                return l;
            }
        }

        return realmArea.getLoc1();
    }

    public void setTeleportLocation(Location loc) {
        info.set("TeleportLocation", NBTsUtil.Location2String(loc));
    }

    public boolean allowOthersTeleport() {
        return getFlag(RealmSets.VISIBLE);
//        return info.getBoolean("Flags.allowOthersTeleport", false);
    }

    public boolean allowPlayerTeleport(Player player) {
        if ( allowOthersTeleport() ) return true;

        if ( player.getUniqueId().equals(ownerUUID) ) return true;

        if ( doPlayerHasPer(player) ) return true;

        return info.getBoolean("PlayerFlags." + name + ".allowPlayerTeleport", false);
    }

    static boolean DENY = false, AGREE = true;

    static List<EntityType> animals = Arrays.asList(
            EntityType.COW, EntityType.CHICKEN, EntityType.PIG, EntityType.SHEEP, EntityType.HORSE, EntityType.MUSHROOM_COW,
            EntityType.OCELOT, EntityType.WOLF, EntityType.SQUID, EntityType.RABBIT, EntityType.POLAR_BEAR, EntityType.LLAMA,
            EntityType.PARROT, EntityType.DONKEY, EntityType.MULE, EntityType.VILLAGER, EntityType.TURTLE, EntityType.PANDA,
            EntityType.CAT, EntityType.FOX, EntityType.BEE, EntityType.DOLPHIN, EntityType.PUFFERFISH, EntityType.SALMON,
            EntityType.COD, EntityType.TROPICAL_FISH, EntityType.TRADER_LLAMA, EntityType.SQUID, EntityType.GLOW_SQUID,
            EntityType.VILLAGER, EntityType.IRON_GOLEM, EntityType.GOAT, EntityType.BEE
    );

    public static List<EntityType> SHOOTER_SOURCE = Arrays.asList(
     /* EntityType.SKELETON, EntityType.WITCH, EntityType.WITHER, EntityType.STRAY, EntityType.PILLAGER, EntityType.VINDICATOR,
            EntityType.DROWNED, EntityType.PIGLIN, EntityType.ENDER_CRYSTAL, */EntityType.ARROW, EntityType.SPECTRAL_ARROW
    );

    public boolean onEntityEscape(EntityType type) {
        // if type equals animals -> deny
        if (animals.contains(type)) return this.getFlag(RealmSets.ANIMAL.name(), DENY);

        return !this.getFlag(RealmSets.MOB.name(), DENY);
    }

    public boolean onEntitySpawn(EntityType type) {

        return this.getFlag(RealmSets.MOB);
    }

    @SneakyThrows
    public PlayerRealm de(ResultSet rs) {
        String data = rs.getString("te_info");
        data = Base64.decodeStr(data);

        this.info.loadFromString(data);

        Time teCreated = rs.getTime("te_created");
        if ( teCreated != null ) {
            this.created = teCreated.getTime();
        }

        Time teUpdated = rs.getTime("te_updated");
        if ( teUpdated != null ) {
            this.updated = teUpdated.getTime();
        }

        this.uuid = UUID.fromString(rs.getString("te_uuid"));
        this.ownerUUID = UUID.fromString(rs.getString("te_owner_uuid"));

        this.name = rs.getString("te_name");
        this.owner = rs.getString("te_owner");
        this.serverName = rs.getString("te_server");

        this.realmArea = new RealmArea(this);
        this.realmIcon = new RealmIcon(this);

        return this;
    }

    @SneakyThrows
    public void delete() {
        TalexEssential.getInstance().getMySQLSource().ps(
                "DELETE FROM `realm_list` WHERE `te_uuid` = \"" + this.uuid.toString() + "\""
        ).execute();
    }

    @SneakyThrows
    public void save() {
        realmIcon.save(this);
        realmArea.save();

        SqlBuilder sb;

        ResultSet rs = TalexEssential.getInstance().getMySQLSource().r("realm_list", "te_uuid", this.uuid.toString());

        if ( rs == null || !rs.next() ) {

            sb = new SqlAddBuilder().setTableName("realm_list")
                    .setType(SqlAddBuilder.AddType.IGNORE)
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_uuid").setSubParamValue(this.uuid.toString()))
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_name").setSubParamValue(this.name))
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_owner").setSubParamValue(this.owner))
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_owner_uuid").setSubParamValue(this.ownerUUID.toString()))
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_server").setSubParamValue(this.serverName))
//                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_created").setSubParamValue(String.valueOf(this.created)))
//                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_updated").setSubParamValue(String.valueOf(System.currentTimeMillis())))
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_info").setSubParamValue(Base64.encode(this.info.saveToString())))
            ;

        } else {
            // update

            sb = new SqlUpdBuilder().setTableName("realm_list")
                    .addTableParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_name").setSubParamValue(this.name))
                    .addTableParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_owner").setSubParamValue(this.owner))
                    .addTableParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_owner_uuid").setSubParamValue(this.ownerUUID.toString()))
                    .addTableParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_server").setSubParamValue(this.serverName))
//                    .addTableParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_created").setSubParamValue(String.valueOf(this.created)))
//                    .addTableParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_updated").setSubParamValue(String.valueOf(System.currentTimeMillis())))
                    .addTableParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_info").setSubParamValue(Base64.encode(this.info.saveToString())))
                    .setWhereParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_uuid").setSubParamValue(this.uuid.toString()))
            ;
        }

        boolean b = TalexEssential.getInstance().getMySQLSource().ps(sb.toString()).execute();
    }

}
