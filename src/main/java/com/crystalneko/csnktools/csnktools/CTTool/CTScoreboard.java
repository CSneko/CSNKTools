package com.crystalneko.csnktools.csnktools.CTTool;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.UUID;

public class CTScoreboard implements Listener {

    private final JavaPlugin plugin;
    private final ScoreboardManager scoreboardManager;
    private final Scoreboard scoreboard;
    private Objective objective;

    public CTScoreboard(JavaPlugin plugin) {
        this.plugin = plugin;
        this.scoreboardManager = Bukkit.getScoreboardManager();
        this.scoreboard = scoreboardManager.getNewScoreboard();
    }

    public void createScoreboard(Player player) {
        if (scoreboard.getObjective("ctscoreboard") != null) {
            objective = scoreboard.getObjective("ctscoreboard");
        } else {
            objective = scoreboard.registerNewObjective("ctscoreboard", "dummy");
        }
        String displayName = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Scoreboard.title"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        List<String> lines = plugin.getConfig().getStringList("Scoreboard.line");
        int score = lines.size();

        for (String line : lines) {
            String replacedLine = replaceVariables(player, line);

            if (scoreboard.getTeam(replacedLine) == null) {
                Team team = scoreboard.registerNewTeam(getUniqueTeamName(score));
                team.addEntry(replacedLine);
                team.setPrefix(replacedLine);
            }

            objective.getScore(replacedLine).setScore(score);
            score--;
        }

        player.setScoreboard(scoreboard);
        startScoreboardUpdateTask(player);
    }

    public void updateScoreboard(Player player) {
        objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Scoreboard.title")));

        List<String> lines = plugin.getConfig().getStringList("Scoreboard.line");
        int score = lines.size();

        for (String line : lines) {
            String replacedLine = replaceVariables(player, line);
            Team team = scoreboard.getTeam(replacedLine);
            if (team != null) {
                team.setPrefix(replacedLine);
                objective.getScore(replacedLine).setScore(score);
                score--;
            }
        }
    }

    public void removeScoreboard(Player player) {
        player.setScoreboard(scoreboardManager.getMainScoreboard());
    }

    public void startScoreboardUpdateTask(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    updateScoreboard(player);
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, plugin.getConfig().getInt("Scoreboard.update") * 20L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        createScoreboard(event.getPlayer());
    }

    private String replaceVariables(Player player, String line) {
        // 使用 PlaceholderAPI 替换变量
        String replacedLine = PlaceholderAPI.setPlaceholders(player, line);

        return ChatColor.translateAlternateColorCodes('&', replacedLine);
    }


    private String getUniqueTeamName(int score) {
        return "line" + score + "_" + UUID.randomUUID().toString();
    }

    // 省略 getPlayerVariable 方法，因为逻辑已经使用 PAPI 替换完成
}
