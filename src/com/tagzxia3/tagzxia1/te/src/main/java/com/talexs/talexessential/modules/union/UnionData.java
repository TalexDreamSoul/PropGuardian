package com.tagzxia3.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.union;

import cn.hutool.core.codec.Base64;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.union.MemberPermission;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.union.UnionMember;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.builder.SqlAddBuilder;
import com.talexs.talexessential.builder.SqlBuilder;
import com.talexs.talexessential.builder.SqlTableBuilder;
import com.talexs.talexessential.builder.SqlUpdBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.util.*;

@Getter
@Setter
public class UnionData {

    public static void injectTable() {
        // auto gen table
        SqlTableBuilder stb = new SqlTableBuilder()
                .setTableName("union_data")
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull("null").setMain(true).setSubParamName("te_uuid").setType("VARCHAR(64)"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull(null).setMain(false).setSubParamName("te_name").setType("VARCHAR(10)"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull(null).setMain(false).setSubParamName("te_owner").setType("VARCHAR(64)"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull(null).setMain(false).setSubParamName("te_owner_uuid").setType("VARCHAR(64)"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull(null).setMain(false).setSubParamName("te_level").setType("INT(11)"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull(null).setMain(false).setSubParamName("te_dummy").setType("DOUBLE"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull(null).setMain(false).setSubParamName("te_exist").setType("INT(11)"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull(null).setMain(false).setSubParamName("te_overdue").setType("INT(11)"))
                // timestamp (created and updated)
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setSubParamName("u").setLine("`te_updated` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setSubParamName("c").setLine("`te_created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull(null).setMain(false).setSubParamName("te_info").setType("MEDIUMTEXT"))
                ;

        TalexEssential.getInstance().getMySQLSource().table(stb);
    }

    @SneakyThrows
    public static List<UnionData> getAllUnion() {
        ResultSet r = TalexEssential.getInstance().getMySQLSource().r("union_data", "te_exist", "1");

        List<UnionData> list = new ArrayList<>();

        while ( r != null && r.next() ) {
            list.add(getFromResultSet(r));
        }

        return list;

    }

    private String name, owner, ownerUUID;

    private final UUID uuid;

    private List<UnionMember> members = new ArrayList<>();

    private boolean exist = true;

    private int level;

    private double dummy;

    private YamlConfiguration info = new YamlConfiguration();

    private long created, updated, overdue;

    private UnionData(UUID uuid) {;
        this.uuid = uuid;
    }

    public UnionData(Player owner) {
        this(UUID.randomUUID());

        this.owner = owner.getName();
        this.ownerUUID = owner.getUniqueId().toString();

        UnionMember um = new UnionMember(this, owner);

        um.setPermission(MemberPermission.OWNER);
        um.save();

        members.add(um);
    }

    public static @Nullable UnionData getFromPlayer(UUID owner) {
        ResultSet rs = TalexEssential.getInstance().getMySQLSource().r("union_data", "te_owner_uuid", owner.toString());

        try {
            if ( rs != null && rs.next() ) {
                return getFromResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @SneakyThrows
    public static UnionData getFromResultSet(ResultSet rs) {
        UnionData ud = new UnionData(UUID.fromString(rs.getString("te_uuid")));

        String data = rs.getString("te_info");
        data = Base64.decodeStr(data);

        ud.info.loadFromString(data);

        ud.name = rs.getString("te_name");
        ud.owner = rs.getString("te_owner");
        ud.ownerUUID = rs.getString("te_owner_uuid");
        ud.level = rs.getInt("te_level");
        ud.dummy = rs.getDouble("te_dummy");
        ud.exist = rs.getInt("te_exist") == 1;
        ud.created = rs.getTimestamp("te_created").getTime();
        ud.updated = rs.getTimestamp("te_updated").getTime();
        ud.overdue = rs.getLong("te_overdue");

        ud.loadAllMembers();

        return ud;
    }

    public @Nullable UnionMember getMember(Player player) {
        UnionMember gum = this.members.stream().findAny().filter(um -> um.getUuid().equals(player.getUniqueId())).orElse(null);

        // 再扫一遍 好像有毛病
        if ( gum == null)
            for ( UnionMember m : this.members ) {
                if ( m.getUuid().equals(player.getUniqueId()) ) {
                    gum = m;
                    break;
                }
            }

        return gum;
    }

    public UnionData addLog(String msg) {
        List<String> logs = getInfo().getStringList("Manifest.Logs");
        logs.add(msg + "@" + System.currentTimeMillis());
        getInfo().set("Manifest.Logs", logs);

        return this;
    }

    public void disSolution() {
        this.exist = false;

        this.addLog("联盟已被解散!");

        this.save();
    }

    @SneakyThrows
    public void delUnion() {
        // del owner
        this.members.get(0).kickSelf();

        // del self
        TalexEssential.getInstance().getMySQLSource().ps(
                "DELETE FROM `union_data` WHERE `te_uuid` = \"" + this.uuid.toString() + "\""
        ).execute();
    }

    @SneakyThrows
    private void loadAllMembers() {
        ResultSet rs = TalexEssential.getInstance().getMySQLSource().r("union_member", "te_union_uuid", this.uuid.toString());

        if ( rs == null ) return;

        while ( rs.next() ) {
            UnionMember member = UnionMember.getFromResultSet(this, rs);
            this.members.add(member);
        }

    }

    @SneakyThrows
    public void save() {
        SqlBuilder sb;

        ResultSet rs = TalexEssential.getInstance().getMySQLSource().r("union_data", "te_uuid", this.uuid.toString());

        if ( rs == null || !rs.next() ) {

            sb = new SqlAddBuilder().setTableName("union_data")
                    .setType(SqlAddBuilder.AddType.IGNORE)
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_uuid").setSubParamValue(this.uuid.toString()))
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_name").setSubParamValue(this.name))
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_owner").setSubParamValue(this.owner))
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_owner_uuid").setSubParamValue(this.ownerUUID))
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_level").setSubParamValue(String.valueOf(this.level)))
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_dummy").setSubParamValue(String.valueOf(this.dummy)))
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_exist").setSubParamValue(this.exist ? "1" : "0"))
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_overdue").setSubParamValue(String.valueOf(this.overdue)))
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_info").setSubParamValue(Base64.encode(this.info.saveToString())))
                    ;

        } else {
            // update

            sb = new SqlUpdBuilder().setTableName("union_data")
                    .addTableParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_name").setSubParamValue(this.name))
                    .addTableParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_owner").setSubParamValue(this.owner))
                    .addTableParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_owner_uuid").setSubParamValue(this.ownerUUID))
                    .addTableParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_level").setSubParamValue(String.valueOf(this.level)))
                    .addTableParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_dummy").setSubParamValue(String.valueOf(this.dummy)))
                    .addTableParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_exist").setSubParamValue(this.exist ? "1" : "0"))
                    .addTableParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_overdue").setSubParamValue(String.valueOf(this.overdue)))
                    .setWhereParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_uuid").setSubParamValue(this.uuid.toString()))
                    ;
        }

        TalexEssential.getInstance().getMySQLSource().ps(sb.toString()).execute();
    }

}
