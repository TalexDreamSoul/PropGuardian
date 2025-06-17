package com.tagzxia2.tagzxia1.te.src.main.java.com.talexs.talexessential.data.player;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerDataRunnable;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser;
import com.talexs.talexessential.modules.chat.ChatFunction;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IPlayerUser {
    PlayerUser addCoolDown(String type, long time);

    /**
     * Default value for 15s
     * @param type cool-down type
     * @return THIS
     */
    PlayerUser addCoolDown(String type);

    /**
     * @param type
     * @param diff
     * @return the left milliseconds
     */
    long autoCoolDownCount(String type, long diff);

    boolean isCoolDown(String type);

    boolean autoCoolDown(String type, long diff);

    PlayerUser delCoolDown(String type);

    PlayerUser teleport(@NotNull Location location);

    PlayerUser addItem(@NotNull ItemStack stack);

    PlayerUser triggerDarkness();

    PlayerUser triggerEffect(PotionEffectType effect);

    PlayerUser sendMessage(String msg);

    PlayerUser addMetaOnHandItem(NamespacedKey key, String value);

    PlayerUser delMetaOnHandItem(NamespacedKey key);

    @Nullable String getMetaOnHandItem(NamespacedKey key);

    ItemStack reducePlayerHandItem(int amo);

    PlayerUser dropItem(ItemStack stack, int amo);

    PlayerUser dropItem(ItemStack stack);

    PlayerUser delayRunTimerAsync(PlayerDataRunnable runnable, long delay, long timer);

    PlayerUser delayRunAsync(PlayerDataRunnable runnable, long delay);

    PlayerUser delayRunTimer(PlayerDataRunnable runnable, long delay, long timer);

    PlayerUser delayRun(PlayerDataRunnable runnable, long delay);

    PlayerUser runTask(PlayerDataRunnable runnable);

    PlayerUser runTaskAsync(PlayerDataRunnable runnable);

    PlayerUser closeInventory();

    PlayerUser title(String title, String subTitle, int fadeIn, int stay, int fadeOut, int delay);

    PlayerUser title(String title, String subTitle, int fadeIn, int stay, int fadeOut);

    PlayerUser throwHandItem();

    PlayerUser playSound(Sound sound, float f, float v);

    PlayerUser playSound(Sound sound);

    PlayerUser firework();

    PlayerUser actionBar(String message);

    PlayerUser actionBar(Component message);

    PlayerUser errorActionBar(String message);

    PlayerUser infoActionBar(String message);

    void sendToServer(String name);

    PlayerUser chatFunc(ChatFunction cf);
}
