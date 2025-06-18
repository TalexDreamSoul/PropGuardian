package com.tagzxia2.te.src.main.java.com.talexs.talexessential.event;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

@Getter
public class WorldCoulJoinEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;

    private Location to, from;

    public WorldCoulJoinEvent(@NotNull Player player, Location to, Location from) {
        super(player);

        this.to = to;
        this.from = from;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static @NotNull HandlerList getHandlerList() {
        return handlers;
    }

}
