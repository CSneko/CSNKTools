package com.crystalneko.csnktools.CTcommand;



import com.crystalneko.csnktools.CTTool.HtmlPlaceholderConverter;
import com.crystalneko.csnktools.CTTool.mysqlandemail;
import com.crystalneko.csnktools.CTTool.Music;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;
import com.crystalneko.csnktools.CSNKTools;


public class csnktools implements CommandExecutor {


    private CSNKTools plugin;
    private Boolean NBAPI;
    private String nopermissions;
    private feedback feedBack;
    private mysqlandemail mysqlAndemail;
    private HtmlPlaceholderConverter htmlPlaceholderConverter;
    private Boolean musicE;
    public csnktools(CSNKTools plugin,Boolean nbapi,mysqlandemail mysqlAndemail,HtmlPlaceholderConverter htmlPlaceholderConverter) {
        this.plugin = plugin;

        NBAPI = nbapi;
        nopermissions = plugin.getMessage("Command.ct_nopermissions");
        //检查反馈是否启用
        if(plugin.getConfig().getBoolean("feedback.Enable")){
            feedBack = new feedback(plugin,mysqlAndemail,htmlPlaceholderConverter);
        }

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
            //反馈命令
            if (sender.hasPermission("ct.command.feedback")){
                if (plugin.getConfig().getBoolean("feedback.Enable")) {
                    feedBack.feedbackcommand(player, args);
                }
            }
        } else {
            // 无效的子命令
            String nocommand = plugin.getMessage("Command.nocommand");
            player.sendMessage(nocommand);
        }

        return true;
    }

     }

