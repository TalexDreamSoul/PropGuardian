package com.tagzxia.tagzxia1.te.src.main.java.com.talexs.soultech.machine.break_hammer;

import com.talexs.soultech.internal.entity.items.TalexItem;
import com.talexs.soultech.items.tools.pickaxe.breakhammer.BaseBreakHammer;
import com.talexs.soultech.items.tools.pickaxe.breakhammer.StoneHammer;
import com.talexs.soultech.internal.RecipeObject;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BreakHammerRecipe extends RecipeObject {

    @Getter
    @Setter
    private TalexItem require, export;

    @Getter
    private BaseBreakHammer displayRequireHammerTool = new StoneHammer();

    public BreakHammerRecipe(String recipeID, TalexItem require, TalexItem displayItem) {

        super(recipeID, displayItem);

        this.require = require;
        this.export = displayItem;

    }

    public BreakHammerRecipe(String recipeID, ItemStack require, TalexItem displayItem) {

        super(recipeID, displayItem);

        this.require = new TalexItem(require);
        this.export = displayItem;

    }

    public BreakHammerRecipe(String recipeID, TalexItem require, ItemStack displayItem) {

        super(recipeID, new TalexItem(displayItem));

        this.require = require;
        this.export = new TalexItem(displayItem);

    }

    public BreakHammerRecipe(String recipeID, ItemStack require, ItemStack displayItem) {

        super(recipeID, new TalexItem(displayItem));

        this.require = new TalexItem(require);
        this.export = new TalexItem(displayItem);

    }

    public BreakHammerRecipe(String recipeID, Material require, Material displayItem) {

        super(recipeID, new TalexItem(new ItemStack(displayItem)));

        this.require = new TalexItem(new ItemStack(require));
        this.export = new TalexItem(new ItemStack(displayItem));

    }

    public BreakHammerRecipe(String recipeID, Material require, ItemStack displayItem) {

        super(recipeID, new TalexItem(displayItem));

        this.require = new TalexItem(new ItemStack(require));
        this.export = new TalexItem(displayItem);

    }

    public BreakHammerRecipe setDisplayRequireHammerTool(BaseBreakHammer baseBreakHammer) {

        this.displayRequireHammerTool = baseBreakHammer;

        return this;

    }

}
