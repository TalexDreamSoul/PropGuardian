package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.attract;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.attract.IAttractAddon;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.attract.PlayerAttractData;
import lombok.Getter;

@Getter
public class CoolDown implements IAttractAddon {

    private final PlayerAttractData playerAttractData;
    private int level;

    public CoolDown(PlayerAttractData playerAttractData) {

        this.level = 0;
        this.playerAttractData = playerAttractData;

    }

    public CoolDown(PlayerAttractData playerAttractData, int level) {

        this.level = level;
        this.playerAttractData = playerAttractData;

    }

    @Override
    public String getLevelDisplayed(int level) {
        double time = getLevelCD(level);

        return String.format("%.2f", time);
    }

    @Override
    public String getLevelDisplayed() {
       return getLevelDisplayed(this.level);
    }

    public double getLevelCD() {
        return getLevelCD(this.level);
    }

    public double getLevelCD(int level) {
        double a = -0.375;
        int b = -1;
        int c = 32;

        // level = 9
        // -0.8 * 9 * 9 - 1 * 9 + 32

        return a * level * level + b * level + c;
    }

    public IAttractAddon setLevel(int level) {

        this.level = level;
        return this;

    }

    public IAttractAddon addLevel(int level) {

        this.level += level;

        return this;

    }

    @Override
    public int hashCode() {

        return ( this.level + "" ).hashCode();

    }

    @Override
    public boolean equals(Object obj) {

        if ( !( obj instanceof CoolDown ) ) {
            return false;
        }

        CoolDown target = (CoolDown) obj;

        return target.level == this.level;

    }

    @Override
    public int getLevelCostMoney(int level) {
        return (int) ( Math.pow(1.75, level) * 100 + 150 * level );
    }

    @Override
    public int getLevelCostExp(int level) {
        return (int) ( Math.pow(1.75, level) * 3 + 12 * level );
    }

    @Override
    public int getLevelCostEnderPearl(int level) {
        return (int) ( Math.pow(1.25, level) * 8 + 24 * level );
    }

    @Override
    public void levelUp() {
        this.level++;
    }

}
