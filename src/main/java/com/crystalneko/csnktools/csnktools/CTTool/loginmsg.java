package com.crystalneko.csnktools.csnktools.CTTool;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class loginmsg implements Listener {

    private String joinMessage;

    public void loadConfig() {
        FileConfiguration config = JavaPlugin.getPlugin(com.crystalneko.csnktools.csnktools.CSNKTools.class).getConfig();
        joinMessage = config.getString("player.join.Message");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        String joinText = joinMessage;

        /*
         * 使用"setPlaceholders"解析占位符。
         */
        joinText = PlaceholderAPI.setPlaceholders(event.getPlayer(), joinText);

        event.setJoinMessage(joinText);
    }
}
