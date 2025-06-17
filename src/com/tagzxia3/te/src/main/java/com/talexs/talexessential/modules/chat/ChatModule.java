package com.tagzxia3.te.src.main.java.com.talexs.talexessential.modules.chat;

import com.tagzxia.te.src.main.java.com.talexs.talexessential.modules.chat.ChatFunction;
import com.talexs.talexessential.TalexEssential;
import com.talexs.talexessential.modules.BaseModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ChatModule extends BaseModule {

    private static Map<String, ChatFunction> chatFuncLists = new HashMap<>();

    public ChatModule() {
        super("chat");
    }

    @Override
    public void onEnable() {
        new BukkitRunnable() {
            @Override
            public void run() {
                new HashSet<>(chatFuncLists.entrySet()).forEach(ChatModule.this::process);
            }
        }.runTaskTimer(TalexEssential.getInstance(), 0, 20);
    }

    @Override
    protected boolean configurable() {
        return false;
    }

    public static void add(Player player, ChatFunction cf) {
        chatFuncLists.put(player.getName(), cf);

        cf.onBefore();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if ( !chatFuncLists.containsKey(player.getName()) ) return;

        event.setCancelled(true);
        ChatFunction chatFunction = chatFuncLists.get(player.getName());

        chatFuncLists.remove(player.getName());
        chatFunction.onBeforeCall();
        chatFunction.execute(event.getMessage());
    }

    private void process(Map.Entry<String, ChatFunction> entry) {
        ChatFunction cf = entry.getValue();

        long now = System.currentTimeMillis();
        long diff = now - cf.getCreated();

        if ( diff >= cf.getTimeOut() ) {
            cf.setStatus(ChatFunction.ChatFuncStatus.REJECT);
            chatFuncLists.remove(entry.getKey());

            cf.onBeforeCall();
            cf.rejected();
        }
    }

}
