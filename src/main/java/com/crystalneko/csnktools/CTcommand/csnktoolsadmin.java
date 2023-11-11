package com.crystalneko.csnktools.CTcommand;


import com.crystalneko.csnktools.CSNKTools;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;


public class csnktoolsadmin implements CommandExecutor {


    private CSNKTools plugin;
    private String nopermissions;
    public csnktoolsadmin(CSNKTools plugin) {
        this.plugin = plugin;
        nopermissions = plugin.getMessage("Command.ct_nopermissions");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 检查是否为玩家执行命令
        if (!(sender instanceof Player)) {
            // 处理子命令
            if (args[0].equalsIgnoreCase("help")) {
                // 执行子命令的逻辑
                String cta_help = plugin.getMessage("Command.cta_help");
                Bukkit.getConsoleSender().sendMessage(cta_help);
            } else if(args.length == 3 && args[0].equalsIgnoreCase("download")){
                //获取链接
                String downloadurl = args[1];
                //获取路径
                String downloadpath = args[2];
                try {
                    //下载文件
                    String startdownloadfile = plugin.getMessage("Command.cta_download_start");
                    Bukkit.getConsoleSender().sendMessage(startdownloadfile);
                    download.downloadFile(downloadurl,downloadpath);
                    String finishdownloadfile = plugin.getMessage("Command.cta_download_finish");
                    Bukkit.getConsoleSender().sendMessage(finishdownloadfile);
                } catch (IOException e) {
                    String cantdownloadfile = plugin.getMessage("Error.cantdownloadfile");
                    Bukkit.getConsoleSender().sendMessage(cantdownloadfile + e);
                }
            }else {
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



        // 处理子命令
        if (args[0].equalsIgnoreCase("help")) {
            if (sender.hasPermission("cta.command.help")) { // 检查是否拥有cta.help权限
                // 执行子命令的逻辑
                String cta_help = plugin.getMessage("Command.cta_help");
                player.sendMessage(cta_help);
            } else {
                sender.sendMessage(nopermissions);
            }

        }else if(args[0].equalsIgnoreCase("download")){
            if(sender.hasPermission("cta.command.download")){
                //获取链接
                String downloadurl = args[1];
                //获取路径
                String downloadpath = args[2];
                try {
                    //下载文件
                    String startdownloadfile = plugin.getMessage("Command.cta_download_start");
                    player.sendMessage(startdownloadfile);
                    download.downloadFile(downloadurl,downloadpath);
                    String finishdownloadfile = plugin.getMessage("Command.cta_download_finish");
                    player.sendMessage(finishdownloadfile);
                } catch (IOException e) {
                    String cantdownloadfile = plugin.getMessage("Error.cantdownloadfile");
                    player.sendMessage(cantdownloadfile + e);
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

