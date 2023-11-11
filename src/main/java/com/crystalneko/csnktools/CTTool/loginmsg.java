package com.crystalneko.csnktools.CTTool;

import com.crystalneko.csnktools.CSNKTools;
import com.crystalneko.csnktools.CTcommand.download;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class loginmsg implements Listener {
    private download Download; // 添加私有的Download对象

    private String joinMessage;
    private Boolean papiEnable;


    Plugin CSNKTOOLS = Bukkit.getPluginManager().getPlugin("CSNKTools");



    public void loadConfig(Boolean papienable) {
            FileConfiguration config = JavaPlugin.getPlugin(CSNKTools.class).getConfig();
            joinMessage = config.getString("player.join.Message");
            papiEnable = papienable;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event ) {
        String joinText = joinMessage;
        //当PAPI启用时解析占位符
        if (papiEnable) {
            joinText = PlaceholderAPI.setPlaceholders(event.getPlayer(), joinText);
        }

        event.setJoinMessage(joinText);
    }

}
