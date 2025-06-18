package com.tagzxia3.te.src.main.java.com.talexs.soultech.internal;

import com.talexs.soultech.internal.entity.items.SoulTechItem;
import com.talexs.soultech.internal.entity.items.TalexItem;
import lombok.Getter;

@Getter
public class RecipeObject {

    private final String recipeID;

    private final TalexItem displayItem;

    public RecipeObject(String recipeID, TalexItem displayItem) {

        this.recipeID = recipeID;
        this.displayItem = displayItem;

    }

    public RecipeObject(SoulTechItem displayItem) {
        this(displayItem.getID(), displayItem);
    }

    @Override
    public int hashCode() {

        return this.recipeID.hashCode();

    }

    @Override
    public boolean equals(Object obj) {

        if ( obj instanceof RecipeObject ) {

            return obj.hashCode() == hashCode();

        }

        return super.equals(obj);
    }

}
