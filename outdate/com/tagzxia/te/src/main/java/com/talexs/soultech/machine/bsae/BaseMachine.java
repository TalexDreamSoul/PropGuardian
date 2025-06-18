package com.tagzxia.te.src.main.java.com.talexs.soultech.machine.bsae;

import com.talexs.soultech.addon.AddonHolder;
import com.talexs.soultech.internal.book.IndicateBook;
import com.talexs.soultech.internal.machine.MachineAddon;
import com.talexs.soultech.internal.protector.ProtectorAddon;
import com.talexs.soultech.internal.RecipeObject;
import com.talexs.talexessential.utils.NBTsUtil;
import lombok.Getter;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public abstract class BaseMachine implements IMachine {

    @Getter
    protected static HashMap<String, MachineGUI> guis = new HashMap<>();

    protected String loc2Str(Location loc) { return NBTsUtil.Location2String(loc); }

    protected Location str2Loc(String str) { return NBTsUtil.String2Location(str); }

    @Getter
    protected final IMachineChecker machineChecker;

    @Getter
    protected final String machineName;

    @Getter
    protected final ItemStack displayItem;
    @Getter
    protected Set<RecipeObject> recipes = new HashSet<>();

    public BaseMachine(String machineName, ItemStack displayItem, IMachineChecker checker) {

        this.machineChecker = checker;
        this.machineName = machineName;
        this.displayItem = displayItem;

    }

    public abstract void onOpenMachineInfoViewer(Player player);

    protected void addRecipe(RecipeObject recipe) {

        recipes.add(recipe);

    }

    protected void delRecipe(RecipeObject recipe) {

        recipes.remove(recipe);

    }

    public BaseMachine placeBlock(Location loc, Material material) {
//         if ( loc.getBlock().getType() != Material.AIR ) throw new RuntimeException("The location is not air!");

        loc.getBlock().setType(material, true);
        loc.getWorld().playEffect(loc, Effect.WITHER_BREAK_BLOCK, 1);

        return this;
    }

    public boolean couldPlace(Player player, Location loc) {
        return AddonHolder.getINSTANCE().get(ProtectorAddon.class).couldPlace(player, loc);
    }

    public boolean couldBreak(Player player, Location loc) {
        return AddonHolder.getINSTANCE().get(ProtectorAddon.class).couldBreak(player, loc);
    }

    protected void deepRecipeView(IndicateBook book) {
        AddonHolder.getINSTANCE().get(MachineAddon.class).onRecipeView(book);
    }

}
