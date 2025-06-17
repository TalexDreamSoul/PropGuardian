package com.tagzxia2.te.src.main.java.com.talexs.talexessential.data.player;

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
    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser addCoolDown(String type, long time);

    /**
     * Default value for 15s
     * @param type cool-down type
     * @return THIS
     */
    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser addCoolDown(String type);

    /**
     * @param type
     * @param diff
     * @return the left milliseconds
     */
    long autoCoolDownCount(String type, long diff);

    boolean isCoolDown(String type);

    boolean autoCoolDown(String type, long diff);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser delCoolDown(String type);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser teleport(@NotNull Location location);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser addItem(@NotNull ItemStack stack);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser triggerDarkness();

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser triggerEffect(PotionEffectType effect);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser sendMessage(String msg);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser addMetaOnHandItem(NamespacedKey key, String value);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser delMetaOnHandItem(NamespacedKey key);

    @Nullable String getMetaOnHandItem(NamespacedKey key);

    ItemStack reducePlayerHandItem(int amo);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser dropItem(ItemStack stack, int amo);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser dropItem(ItemStack stack);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser delayRunTimerAsync(PlayerDataRunnable runnable, long delay, long timer);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser delayRunAsync(PlayerDataRunnable runnable, long delay);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser delayRunTimer(PlayerDataRunnable runnable, long delay, long timer);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser delayRun(PlayerDataRunnable runnable, long delay);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser runTask(PlayerDataRunnable runnable);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser runTaskAsync(PlayerDataRunnable runnable);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser closeInventory();

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser title(String title, String subTitle, int fadeIn, int stay, int fadeOut, int delay);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser title(String title, String subTitle, int fadeIn, int stay, int fadeOut);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser throwHandItem();

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser playSound(Sound sound, float f, float v);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser playSound(Sound sound);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser firework();

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser actionBar(String message);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser actionBar(Component message);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser errorActionBar(String message);

    com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerUser infoActionBar(String message);

    void sendToServer(String name);

    PlayerUser chatFunc(ChatFunction cf);
}
