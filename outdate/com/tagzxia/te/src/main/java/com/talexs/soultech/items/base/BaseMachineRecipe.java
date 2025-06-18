package com.tagzxia.te.src.main.java.com.talexs.soultech.items.base;

import com.talexs.soultech.internal.RecipeObject;
import com.talexs.soultech.internal.entity.items.TalexItem;

public class BaseMachineRecipe extends RecipeObject {
    public BaseMachineRecipe(BaseItem item) {
        super(item.getID(), item);
    }
}
