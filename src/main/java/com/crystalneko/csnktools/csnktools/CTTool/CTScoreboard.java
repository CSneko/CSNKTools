package com.crystalneko.csnktools.csnktools.CTTool;

import com.crystalneko.csnktools.csnktools.CSNKTools;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scheduler.*;
import java.util.List;

public class CTScoreboard implements Listener {


    private List<String> ScoreboardLine;
    private String ScoreboardTitle;
    private int taskID;
    FileConfiguration config = JavaPlugin.getPlugin(com.crystalneko.csnktools.csnktools.CSNKTools.class).getConfig();
    private final JavaPlugin plugin;
    public CTScoreboard(FileConfiguration config, JavaPlugin plugin) {
        this.config = config;
        this.plugin = plugin;
        loadConfig();
    }

    public List<String> loadConfig() {
        FileConfiguration config = JavaPlugin.getPlugin(com.crystalneko.csnktools.csnktools.CSNKTools.class).getConfig();
        List<String> ScoreboardLine = config.getStringList("Scoreboard.line");
        ScoreboardTitle = config.getString("Scoreboard.title");
        return ScoreboardLine;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        ScoreboardLine = loadConfig();
        //获取加入的玩家
        Player player = event.getPlayer();
        // 使用"setPlaceholders"解析占位符。
        ScoreboardLine = PlaceholderAPI.setPlaceholders(event.getPlayer(), ScoreboardLine);
        ScoreboardTitle = PlaceholderAPI.setPlaceholders(event.getPlayer(), ScoreboardTitle);
        setScoreboard(player,ScoreboardLine);
        startTask(); // 启动定时任务
    }
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);

        // 在玩家退出时取消定时任务
        Bukkit.getScheduler().cancelTask(taskID);
    }
    private void setScoreboard(Player player, List<String> scoreboardLine1) {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        //创建计分板
        Objective objective = scoreboard.getObjective("CTScoreboard");
        if (objective == null) {  // 如果没有现有的目标对象，则创建一个新的目标对象
            objective = scoreboard.registerNewObjective("CTScoreboard", "dummy", ScoreboardTitle);
            objective.setDisplaySlot(DisplaySlot.SIDEBAR); // 设置显示位置
        } else {
            objective.unregister(); // 取消注册旧的目标对象
            objective = scoreboard.registerNewObjective("CTScoreboard", "dummy", ScoreboardTitle);
            objective.setDisplaySlot(DisplaySlot.SIDEBAR); // 设置显示位置
        }

        // 添加计分板内容
        int score = scoreboardLine1.size();
        for (String line : ScoreboardLine) {
            objective.getScore(line).setScore(score--);
        }
        // 设置玩家的计分板
        player.setScoreboard(scoreboard);
    }

    public void startTask() {
        int updateInterval = config.getInt("Scoreboard.update");
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            loadConfig();
            Bukkit.getServer().getOnlinePlayers().forEach(player -> setScoreboard(player, ScoreboardLine));
        }, updateInterval, updateInterval);
    }
}




