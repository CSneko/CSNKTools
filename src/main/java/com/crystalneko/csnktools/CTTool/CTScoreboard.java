package com.crystalneko.csnktools.CTTool;

import com.crystalneko.csnktools.CSNKTools;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;
import java.util.List;

public class CTScoreboard implements Listener {
    private List<String> scoreboardLine;
    private String scoreboardTitle;
    private int taskID;
    private JavaPlugin plugin;
    private Boolean papiEnable;

    public CTScoreboard(JavaPlugin plugin,Boolean papienable) {
        this.plugin = plugin;
        lloadConfig();
        //设置papiEnable的值
        papiEnable = papienable;
    }

    public List<String> lloadConfig() {

            FileConfiguration config = plugin.getConfig();
            this.scoreboardTitle = config.getString("Scoreboard.title");
            List<String> scoreboardLine = config.getStringList("Scoreboard.line");
            this.scoreboardLine = scoreboardLine;

        return scoreboardLine;
    }
    private List<String> parsePlaceholders(List<String> scoreboardLines, Player player) {
        List<String> parsedLines = new ArrayList<>();
        for (String line : scoreboardLines) {
            String parsedLine = PlaceholderAPI.setPlaceholders(player, line);
            parsedLines.add(parsedLine);
        }
        return parsedLines;
    }



    public String getMessage(String key) {
        CSNKTools csnkTools = (CSNKTools) plugin;
        return csnkTools.getMessage(key);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        scoreboardLine = lloadConfig();
        if (scoreboardLine == null) {
            // 处理scoreboardLine为null的情况
            String noline = getMessage("Scoreboard.noline");
            Bukkit.getConsoleSender().sendMessage(noline);
        } else {
            scoreboardLine = PlaceholderAPI.setPlaceholders(player, scoreboardLine);
        }

        scoreboardTitle = PlaceholderAPI.setPlaceholders(player, scoreboardTitle);
        setScoreboard(player, scoreboardLine);
        startTask();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        Bukkit.getScheduler().cancelTask(taskID);
    }

    private void setScoreboard(Player player, List<String> scoreboardLine1) {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        Objective objective = scoreboard.getObjective("CTScoreboard");
        if (objective == null) {
            objective = scoreboard.registerNewObjective("CTScoreboard", "dummy", scoreboardTitle);
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        } else {
            objective.unregister();
            objective = scoreboard.registerNewObjective("CTScoreboard", "dummy", scoreboardTitle);
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        //判断是否启用PAPI
        if (papiEnable) {
            List<String> parsedLines = parsePlaceholders(scoreboardLine1, player); // 解析占位符

            int score = parsedLines.size();
            for (String line : parsedLines) {
                objective.getScore(line).setScore(score--);
            }
        }else{
            //没有启动PAPI，不解析占位符
            int score = scoreboardLine1.size();
            for (String line : scoreboardLine1) {
                objective.getScore(line).setScore(score--);
            }
        }
        player.setScoreboard(scoreboard);
    }



    public void startTask() {
        FileConfiguration config = plugin.getConfig();
        int updateInterval = config.getInt("Scoreboard.update");
        Bukkit.getScheduler().cancelTask(taskID);
        List<String> scoreboardLine = lloadConfig();
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            Bukkit.getServer().getOnlinePlayers().forEach(player -> setScoreboard(player, scoreboardLine));
        }, updateInterval, updateInterval);
    }

    private boolean isPluginLoaded(String pluginName) {
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        Plugin targetPlugin = pluginManager.getPlugin(pluginName);
        if (targetPlugin != null && targetPlugin.isEnabled()) {
            return true;
        }
        return false;
    }

}
