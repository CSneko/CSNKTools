package com.crystalneko.csnktools.csnktools;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.crystalneko.csnktools.csnktools.CTTool.CTScoreboard;

public class PlayerJoinListener implements Listener {

    private final CTScoreboard ctscoreboard;

    public PlayerJoinListener(CTScoreboard ctscoreboard) {
        this.ctscoreboard = ctscoreboard;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ctscoreboard.createScoreboard(event.getPlayer());
    }
}
