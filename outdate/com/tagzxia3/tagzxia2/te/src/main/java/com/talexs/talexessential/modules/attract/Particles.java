package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.attract;

import com.google.gson.JsonObject;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.attract.IAttractAddon;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.attract.PlayerAttractData;
import lombok.Getter;
import org.bukkit.Particle;

import java.io.Serializable;

public class Particles implements Serializable, IAttractAddon {

    @Getter
    private int level;

    @Getter
    private Particle particle;

    @Getter
    private PlayerAttractData playerAttractData;

    private Particles(PlayerAttractData playerAttractData, int level, Particle particle) {

        this.playerAttractData = playerAttractData;
        this.level = level;
        this.particle = particle;

    }

    public Particles(PlayerAttractData playerAttractData) {

        this.playerAttractData = playerAttractData;
        this.level = 0;
        this.particle = null;

    }

    public static Particles unSerialize(PlayerAttractData playerAttractData, JsonObject json) {

        if ( !json.has("level") ) {
            return null;
        }
        if ( !json.has("particle") ) {
            return null;
        }

        String p = json.get("particle").getAsString();

        return new Particles(playerAttractData, json.get("level").getAsInt(), ( p == null || p.equalsIgnoreCase("null") ) ? null : Particle.valueOf(p));

    }

    @Override
    public int hashCode() {

        return ( this.level + particle.name() ).hashCode();

    }

    @Override
    public boolean equals(Object obj) {

        if ( !( obj instanceof Particles ) ) {
            return false;
        }

        Particles target = (Particles) obj;

        return target.hashCode() == this.hashCode();

    }

    public String serialize() {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("level", this.level);
        jsonObject.addProperty("particle", particle == null ? "null" : particle.name());

        return jsonObject.toString();

    }

    @Override
    public String getLevelDisplayed(int level) {
        return "§e" + level + " §7级 >--<";
    }

    @Override
    public String getLevelDisplayed() {
        return getLevelDisplayed(this.level);
    }

    @Override
    public int getLevelCostMoney(int level) {
        return (int) ( Math.pow(1.5, level) * 5000 + 5000 * level );
    }

    @Override
    public int getLevelCostExp(int level) {
        return (int) ( Math.pow(1.5, level) * 20 + 20 * level );
    }

    @Override
    public int getLevelCostEnderPearl(int level) {
        return (int) ( Math.pow(1.5, level) * 4 + 16 * level );
    }

    @Override
    public void levelUp() {
        this.level++;
    }
}
