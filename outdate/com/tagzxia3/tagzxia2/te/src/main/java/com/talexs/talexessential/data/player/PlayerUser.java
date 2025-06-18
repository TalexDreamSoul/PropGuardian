package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.data.player;

import cn.hutool.cache.impl.TimedCache;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.IPlayerUser;
import com.tagzxia.te.src.main.java.com.talexs.talexessential.data.player.PlayerDataRunnable;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.modules.chat.ChatFunction;
import com.talexs.talexessential.modules.chat.ChatModule;
import com.talexs.talexessential.utils.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerUser implements IPlayerUser {

    // max time for 12 hours
    public static TimedCache<String, Long> coolDownTimer = new TimedCache<>(1000 * 60 * 60 * 12);
    
    private Player player;
    
    public PlayerUser(@NotNull Player player) {
        if ( !player.isOnline() ) throw new IllegalArgumentException("Player is not online!");
        
        this.player = player;
    }

    @Override
    public PlayerUser addCoolDown(String type, long time) {
        coolDownTimer.put(player.getUniqueId() + "#" + type, System.currentTimeMillis(), time);

        return this;
    }

    @Override
    public boolean isCoolDown(String type) {
        return coolDownTimer.containsKey(player.getUniqueId() + "#" + type);
    }

    @Override
    public boolean autoCoolDown(String type, long diff) {
        if ( !isCoolDown(type) ) {
            addCoolDown(type, diff);
            return true;
        }

        return false;
    }

    @Override
    public PlayerUser addCoolDown(String type) {
        return addCoolDown(type, 15 * 1000);
    }

    @Override
    public long autoCoolDownCount(String type, long diff) {
        if ( !isCoolDown(type) ) {
            addCoolDown(type, System.currentTimeMillis() + diff);
            return 0;
        }

        Long aLong = coolDownTimer.get(type, false);

        long tDiff = System.currentTimeMillis() - aLong;

        return diff - tDiff;
    }

    @Override
    public PlayerUser delCoolDown(String type) {
        coolDownTimer.remove(player.getUniqueId() + "#" + type);

        return this;
    }

    @Override
    public PlayerUser teleport(@NotNull Location location) {
        player.teleport(location);

        return this;
    }

    public PlayerUser lighting(Location loc) {
        loc.getWorld().strikeLightning(loc);

        return this;
    }

    @Override
    public PlayerUser addItem(@NotNull ItemStack stack) {

        if (stack.getType() == Material.AIR) {
            return this;
        }

        player.getWorld().dropItem(player.getLocation().add(0, 0.5, 0), stack);

        return this;

    }

    @Override
    public PlayerUser triggerDarkness() {
        player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 20 * 5, 2));

        return this;
    }

    @Override
    public PlayerUser triggerEffect(PotionEffectType effect) {
        player.addPotionEffect(new PotionEffect(effect, 20 * 5, 2));

        return this;
    }

    public PlayerUser triggerEffect(PotionEffect effect) {
        player.addPotionEffect(effect);

        return this;
    }

    @Override
    public PlayerUser sendMessage(String msg) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        return this;
    }

    @Override
    public PlayerUser addMetaOnHandItem(NamespacedKey key, String value) {
        ItemStack stack = player.getItemInHand();

        player.getInventory().setItemInMainHand(
                new ItemBuilder(stack).addPRCData(key, value).toItemStack()
        );

        return this;
    }

    @Override
    public PlayerUser delMetaOnHandItem(NamespacedKey key) {
        ItemStack stack = player.getItemInHand();

        player.getInventory().setItemInMainHand(
                new ItemBuilder(stack).delPRCData(key).toItemStack()
        );

        return this;
    }

    @Override
    public @Nullable String getMetaOnHandItem(NamespacedKey key) {
        ItemStack stack = player.getItemInHand();

        return stack == null ? null : new ItemBuilder(stack).getPRCData(key);
    }

    @Override
    public ItemStack reducePlayerHandItem(int amo) {

        ItemStack stack = player.getInventory().getItemInMainHand();

        if (stack.getType() == Material.AIR) {
            return null;
        }

        ItemStack stack2 = stack.clone();

        stack.setAmount(stack.getAmount() - amo);

        if ( stack.getAmount() < 1 ) {

            stack.setType(Material.AIR);

        }

        return stack2;

    }

//    public PlayerUser delCategoryUnlock(String ID) {
//
//        if ( isCategoryUnLock(ID) ) {
//
//            this.jsonData.addProperty("category_unlock", this.jsonData.get("category_unlock").getAsString().replaceFirst(ID + ", ", ""));
//
//        }
//
//        return this;
//
//    }
//
//    public PlayerUser addCategoryUnlock(String ID) {
//
//        if ( !isCategoryUnLock(ID) ) {
//
//            this.jsonData.addProperty("category_unlock", this.jsonData.get("category_unlock").getAsString() + ID + ", ");
//
////            ParticleUtil.CircleJumpUp(player, Particle.FIREWORKS_SPARK, 5);
//
//        }
//
//        return this;
//
//    }
//
//    public boolean isCategoryUnLock(String ID) {
//
//        String str = this.jsonData.get("category_unlock").getAsString();
//
//        return str.contains(ID + ",");
//
//    }

    @Override
    public PlayerUser dropItem(ItemStack stack, int amo) {
        if ( stack == null || stack.getType() == Material.AIR ) {
            return this;
        }

        ItemStack tStack = stack.clone();
        if ( amo <= 64 ) {
            tStack.setAmount(amo);
            player.getWorld().dropItem(player.getLocation().add(0, 0.5, 0), tStack);
        } else {
            dropItem(stack, 64);
            return dropItem(stack, amo - 64);
        }

        return this;

    }

    @Override
    public PlayerUser dropItem(ItemStack stack) {
        if ( stack == null || stack.getType() == Material.AIR ) {
            return this;
        }

        player.getWorld().dropItem(player.getLocation().add(0, 0.5, 0), stack);

        return this;

    }

    @Override
    public PlayerUser delayRunTimerAsync(PlayerDataRunnable runnable, long delay, long timer) {

        new BukkitRunnable() {

            @Override
            public void run() {

                if ( runnable.isCancelled() ) {

                    cancel();
                    return;

                }

                runnable.run();

            }

        }.runTaskTimerAsynchronously(TalexEssential.getInstance(), delay, timer);

        return this;

    }

    @Override
    public PlayerUser delayRunAsync(PlayerDataRunnable runnable, long delay) {

        new BukkitRunnable() {

            @Override
            public void run() {

                if ( runnable.isCancelled() ) {

                    cancel();
                    return;

                }

                runnable.run();

            }

        }.runTaskLaterAsynchronously(TalexEssential.getInstance(), delay);

        return this;

    }

    @Override
    public PlayerUser delayRunTimer(PlayerDataRunnable runnable, long delay, long timer) {

        new BukkitRunnable() {

            @Override
            public void run() {

                if ( runnable.isCancelled() ) {

                    cancel();
                    return;

                }

                runnable.run();

            }

        }.runTaskTimer(TalexEssential.getInstance(), delay, timer);

        return this;

    }

    @Override
    public PlayerUser delayRun(PlayerDataRunnable runnable, long delay) {

        new BukkitRunnable() {

            @Override
            public void run() {

                if ( runnable.isCancelled() ) {

                    cancel();
                    return;

                }

                runnable.run();

            }

        }.runTaskLater(TalexEssential.getInstance(), delay);

        return this;

    }

    @Override
    public PlayerUser runTask(PlayerDataRunnable runnable) {

        new BukkitRunnable() {

            @Override
            public void run() {

                if ( runnable.isCancelled() ) {

                    cancel();
                    return;

                }

                runnable.run();

            }

        }.runTask(TalexEssential.getInstance());

        return this;

    }

    @Override
    public PlayerUser runTaskAsync(PlayerDataRunnable runnable) {

        new BukkitRunnable() {

            @Override
            public void run() {

                if ( runnable.isCancelled() ) {

                    cancel();
                    return;

                }

                runnable.run();

            }

        }.runTaskAsynchronously(TalexEssential.getInstance());

        return this;

    }

    @Override
    public PlayerUser closeInventory() {

        player.closeInventory();

        return this;

    }

    @Override
    public PlayerUser title(String title, String subTitle, int fadeIn, int stay, int fadeOut, int delay) {

        new BukkitRunnable() {

            @Override
            public void run() {

                title(title, subTitle, fadeIn, stay, fadeOut);

            }

        }.runTaskLater(TalexEssential.getInstance(), delay);

        return this;

    }

    @Override
    public PlayerUser title(String title, String subTitle, int fadeIn, int stay, int fadeOut) {

        this.player.sendTitle(ChatColor.translateAlternateColorCodes('&', title), ChatColor.translateAlternateColorCodes('&', subTitle), fadeIn, stay, fadeOut);

        return this;

    }

    @Override
    public PlayerUser throwHandItem() {
        ItemStack stack = player.getInventory().getItemInMainHand().clone();
        if ( stack.getType() == Material.AIR ) return this;

        player.getInventory().setItemInMainHand(null);

        dropItem(stack);

        return this;
    }

    @Override
    public PlayerUser playSound(Sound sound, float f, float v) {

        new BukkitRunnable() {

            @Override
            public void run() {

                player.playSound(player.getLocation(), sound, f, v);

            }

        }.runTask(TalexEssential.getInstance());

        return this;

    }

    @Override
    public PlayerUser playSound(Sound sound) {
        return playSound(sound, 1, 1);
    }

    @Override
    public PlayerUser firework() {

        runTask(new PlayerDataRunnable() {
            @Override
            public void run() {

                player.getWorld().spawn(player.getLocation(), Firework.class, firework -> {

                    FireworkMeta meta = firework.getFireworkMeta();

                    meta.addEffect(FireworkEffect.builder().withColor(Color.AQUA).withFade(Color.BLUE).with(FireworkEffect.Type.BALL_LARGE).trail(true).build());

                    firework.setFireworkMeta(meta);

                });

            }
        });

        return this;

    }

    @Override
    public PlayerUser actionBar(String message) {

      return actionBar(Component.text(ChatColor.translateAlternateColorCodes('&', message)));

    }

    @Override
    public PlayerUser actionBar(Component message) {

        new BukkitRunnable() {

            @Override
            public void run() {

                player.sendActionBar(message);

            }

        }.runTask(TalexEssential.getInstance());

        return this;

    }

    @Override
    public PlayerUser errorActionBar(String message) {

        return actionBar(MiniMessage.miniMessage().deserialize("<gradient:#E43D30:#A98E2D>" + message));

    }

    @Override
    public PlayerUser infoActionBar(String message) {

        return actionBar(MiniMessage.miniMessage().deserialize("<gradient:#6580BA:#03719E>" + message));

    }

    @Override
    public void sendToServer(String name) {

        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Connect");
        out.writeUTF(name);

        player.sendPluginMessage(TalexEssential.getInstance(), "BungeeCord", out.toByteArray());

    }

    @Override
    public PlayerUser chatFunc(ChatFunction cf) {
        ChatModule.add(player, cf);
        return this;
    }

}
