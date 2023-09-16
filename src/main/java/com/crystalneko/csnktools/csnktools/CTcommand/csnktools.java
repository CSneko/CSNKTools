package com.crystalneko.csnktools.csnktools.CTcommand;



import com.crystalneko.csnktools.csnktools.CTTool.CTScoreboard;
import com.crystalneko.csnktools.csnktools.CTTool.Music;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import com.crystalneko.csnktools.csnktools.CSNKTools;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.bukkit.Bukkit.getServer;


public class csnktools implements CommandExecutor {


    private CSNKTools plugin;
    private Boolean NBAPI;
    private String nopermissions;
    private Boolean musicE;
    public csnktools(CSNKTools plugin,Boolean nbapi) {
        this.plugin = plugin;
        NBAPI = nbapi;
        nopermissions = plugin.getMessage("Command.ct_nopermissions");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Music musicobj = new Music(plugin);
        // 检查是否为玩家执行命令
        if (!(sender instanceof Player)) {
            // 处理子命令
            if (args[0].equalsIgnoreCase("help")) {
                // 执行子命令的逻辑
                String ct_help = plugin.getMessage("Command.ct_help");
                Bukkit.getConsoleSender().sendMessage(ct_help);
            } else if (args[0].equalsIgnoreCase("about")) {
                String ct_about = plugin.getMessage("Command.ct_about");
                Bukkit.getConsoleSender().sendMessage(ct_about);
            } else if (args[0].equalsIgnoreCase("check")) {
                plugin.checkUpdates();
            } else {
                // 无效的子命令
                String nocommand = plugin.getMessage("Command.nocommand");
                Bukkit.getConsoleSender().sendMessage(nocommand);
            }
        }

        // 检查是否有足够的参数
        if (args.length < 1) {
            String ct_help = plugin.getMessage("Command.ct_help");
            sender.sendMessage(ct_help);
            return true;
        }

        // 获取玩家对象
        Player player = (Player) sender;

        // 检查是否启用了音乐功能
        Boolean musicEnable = plugin.getConfig().getBoolean("Music.Enable");
        //判断是否启用了NoteBlockAPI
        if(musicEnable){
            if(NBAPI){
                musicE = true;
                download.copyfile("plugins/CSNKTools/music","plugins/CSNKTools/temp");
            }
        }


        // 处理子命令
        if (args[0].equalsIgnoreCase("help")) {
            if (sender.hasPermission("ct.command.help")) { // 检查是否拥有ct.help权限
                // 执行子命令的逻辑
                String ct_help = plugin.getMessage("Command.ct_help");
                player.sendMessage(ct_help);
            } else {
                sender.sendMessage(nopermissions);
            }

        } else if (args[0].equalsIgnoreCase("about")) {
            if (sender.hasPermission("ct.command.about")) {
                String ct_about = plugin.getMessage("Command.ct_about");
                player.sendMessage(ct_about);
            } else {
                sender.sendMessage(nopermissions);
            }

        } else if (args[0].equalsIgnoreCase("check")) {
            if (sender.hasPermission("ct.command.check")) {
                plugin.checkUpdates2(player);
            } else {
                sender.sendMessage(nopermissions);
            }

        }else if (args.length == 2 && args[0].equalsIgnoreCase("playsound")) {
            if (sender.hasPermission("ct.command.playsound")) {
                if (musicE) {
                    String songName = args[1]; // 获取玩家提供的曲名
                    //传入参数
                    musicobj.getCommandValue(songName, player);
                }
            } else {
                sender.sendMessage(nopermissions);
            }

        }else if (args.length == 2 && args[0].equalsIgnoreCase("soundurl")) {
            if (sender.hasPermission("ct.command.soundurl")) {
                if (musicE) {
                    String songURL = args[1]; // 获取玩家提供的URL
                    //传入参数
                    musicobj.getCommandURL(songURL, player);
                }
            } else {
                sender.sendMessage(nopermissions);
            }
        }else if(args.length == 3 && args[0].equalsIgnoreCase("feedback")){
            if (sender.hasPermission("ct.command.feedback")){
                String title = args[1];
                String content = args[2];
                // 获取当前时间
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                //获取当前坐标
                Location location = player.getLocation();
                double x = location.getX();
                double y = location.getY();
                double z = location.getZ();
                // 获取反馈数据节点
                FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "data/feedback.yml"));
                ConfigurationSection feedbackSection = config.getConfigurationSection("feedback");
                if (feedbackSection == null) {
                    feedbackSection = config.createSection("feedback");
                }

                // 创建新的反馈节点
                ConfigurationSection newFeedbackSection = feedbackSection.createSection(title);
                newFeedbackSection.set("name", player.getName());
                newFeedbackSection.set("location",x+","+y+","+z);
                newFeedbackSection.set("time", time);
                newFeedbackSection.set("feedback", content);

                // 保存反馈文件
                try {
                    config.save(new File(plugin.getDataFolder(), "data/feedback.yml"));
                } catch (IOException e) {
                    e.printStackTrace();
                    String unfeedback = plugin.getMessage("Command.ct_unfeedback");
                    sender.sendMessage(unfeedback);
                    return true;
                }
                String feedback = plugin.getMessage("Command.ct_feedback");
                sender.sendMessage(feedback);
            }
        } else {
            // 无效的子命令
            String nocommand = plugin.getMessage("Command.nocommand");
            player.sendMessage(nocommand);
        }

        return true;
    }

     }

