package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.union;

import cn.hutool.core.codec.Base64;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.union.MemberPermission;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.union.UnionData;
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
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.util.UUID;

@Getter
@Setter
public class UnionMember {

    public static void injectTable() {
        // auto gen table
        SqlTableBuilder stb = new SqlTableBuilder()
                .setTableName("union_member")
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull("null").setMain(true).setSubParamName("te_uuid").setType("VARCHAR(64)"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull("null").setMain(true).setSubParamName("te_union_uuid").setType("VARCHAR(64)"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull(null).setMain(false).setSubParamName("te_name").setType("VARCHAR(32)"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull(null).setMain(false).setSubParamName("te_permission").setType("INT(11)"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull(null).setMain(false).setSubParamName("te_dummy").setType("DOUBLE"))
                // timestamp (created and updated)
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setSubParamName("u").setLine("`te_updated` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setSubParamName("c").setLine("`te_created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP"))
                .addTableParamIgnore(new SqlTableBuilder.TableParam().setDefaultNull(null).setMain(false).setSubParamName("te_info").setType("MEDIUMTEXT"))
                ;

        TalexEssential.getInstance().getMySQLSource().table(stb);
    }

    private String name;

    private final UUID uuid;

    private double dummy;

    private final YamlConfiguration info = new YamlConfiguration();

    private long created = System.currentTimeMillis(), updated = System.currentTimeMillis();

    private MemberPermission permission = MemberPermission.MEMBER;

    private final com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.union.UnionData ownUnion;

    private UnionMember(@NotNull com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.union.UnionData union, UUID uuid) {;
        this.uuid = uuid;
        this.ownUnion = union;
    }

    public UnionMember(@NotNull com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.union.UnionData union, Player player) {;
        this.uuid = player.getUniqueId();
        this.ownUnion = union;
        this.name = player.getName();
    }

    @SneakyThrows
    public static UnionMember getFromResultSet(UnionData ud, @NotNull ResultSet rs) {
        UnionMember um = new UnionMember(ud, UUID.fromString(rs.getString("te_uuid")));

        um.name = rs.getString("te_name");
        um.dummy = rs.getDouble("te_dummy");
        um.created = rs.getTimestamp("te_created").getTime();
        um.updated = rs.getTimestamp("te_updated").getTime();
        um.permission = MemberPermission.g(rs.getInt("te_permission"));

        String data = rs.getString("te_info");
        data = Base64.decodeStr(data);
        um.info.loadFromString(data);

        return um;
    }

    @SneakyThrows
    public void kickSelf() {
        this.ownUnion.getMembers().remove(this);

        TalexEssential.getInstance().getMySQLSource().ps(
                "DELETE FROM `union_member` WHERE `te_uuid` = \"" + this.uuid.toString() + "\""
        ).execute();
    }

    @SneakyThrows
    public void save() {
        SqlBuilder sb;

        ResultSet rs = TalexEssential.getInstance().getMySQLSource().r("union_member", "te_uuid", this.uuid.toString());

        if ( rs == null || !rs.next() ) {

            sb = new SqlAddBuilder().setTableName("union_member")
                    .setType(SqlAddBuilder.AddType.IGNORE)
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_uuid").setSubParamValue(this.uuid.toString()))
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_union_uuid").setSubParamValue(this.ownUnion.getUuid().toString()))
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_name").setSubParamValue(this.name))
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_permission").setSubParamValue(String.valueOf(this.permission.ordinal())))
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_dummy").setSubParamValue(String.valueOf(this.dummy)))
                    .addTableParam(new SqlAddBuilder.AddParam().setSubParamName("te_info").setSubParamValue(Base64.encode(this.info.saveToString())))
                    ;

        } else {
            // update

            sb = new SqlUpdBuilder().setTableName("union_member")
                    .addTableParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_name").setSubParamValue(this.name))
                    .addTableParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_union_uuid").setSubParamValue(this.ownUnion.getUuid().toString()))
                    .addTableParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_dummy").setSubParamValue(String.valueOf(this.dummy)))
                    .addTableParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_permission").setSubParamValue(String.valueOf(this.permission.ordinal())))
                    .addTableParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_info").setSubParamValue(Base64.encode(this.info.saveToString())))
                    .setWhereParam(new SqlUpdBuilder.UpdParam().setSubParamName("te_uuid").setSubParamValue(this.uuid.toString()))
                    ;
        }

        TalexEssential.getInstance().getMySQLSource().ps(sb.toString()).execute();

    }

}
