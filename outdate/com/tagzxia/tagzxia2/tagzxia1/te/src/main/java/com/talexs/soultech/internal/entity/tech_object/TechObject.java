package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.soultech.internal.entity.tech_object;

import com.talexs.soultech.internal.RecipeObject;
import com.talexs.soultech.internal.entity.classfies.Classifies;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter
public abstract class TechObject extends Classifies {

    @Setter
    private RecipeObject recipe;

    @Override
    public ItemStack getDisplayStack() {
        return getItemStack();
    }

    public abstract ItemStack getItemStack();

    public TechObject(String ID, String name, RecipeObject recipe) {
        super(ID, name);
        this.recipe = recipe;
    }
}
