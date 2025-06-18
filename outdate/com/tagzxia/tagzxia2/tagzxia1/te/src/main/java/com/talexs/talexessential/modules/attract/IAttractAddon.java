package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.modules.attract;

public interface IAttractAddon {
    String getLevelDisplayed(int level);

    String getLevelDisplayed();

    int getLevel();

    int getLevelCostMoney(int level);

    int getLevelCostExp(int level);

    int getLevelCostEnderPearl(int level);

    void levelUp();
}
