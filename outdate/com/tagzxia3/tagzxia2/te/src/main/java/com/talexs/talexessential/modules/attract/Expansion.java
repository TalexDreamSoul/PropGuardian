package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.modules.attract;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.attract.IAttractAddon;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.attract.PlayerAttractData;
import lombok.Getter;

public class Expansion implements IAttractAddon {

    @Getter
    private final PlayerAttractData playerAttractData;

    @Getter
    private int level;

    public Expansion(PlayerAttractData playerAttractData, int level) {

        this.playerAttractData = playerAttractData;
        this.level = level;

    }

    public Expansion(PlayerAttractData playerAttractData) {

        this.playerAttractData = playerAttractData;
        this.level = 0;

    }

    @Override
    public int hashCode() {

        return ( this.level + "" ).hashCode();

    }

    @Override
    public boolean equals(Object obj) {

        if ( !( obj instanceof Expansion ) ) {

            return false;

        }

        Expansion target = (Expansion) obj;

        return target.hashCode() == this.hashCode();

    }

    @Override
    public String getLevelDisplayed(int level) {
        return (level + 3) + "x" + (level + 3) + "x" + (level + 3);
    }

    @Override
    public String getLevelDisplayed() {
        return getLevelDisplayed(this.level);
    }

    @Override
    public int getLevelCostMoney(int level) {
        return (int) ( Math.pow(1.75, level) * 100 + 50 * level );
    }

    @Override
    public int getLevelCostExp(int level) {
        return (int) ( Math.pow(1.75, level) * 2.5 + 2 * level );
    }

    @Override
    public int getLevelCostEnderPearl(int level) {
        return (int) ( Math.pow(1.25, level) * 16 + 16 * level );
    }

    @Override
    public void levelUp() {
        this.level++;
    }
}
