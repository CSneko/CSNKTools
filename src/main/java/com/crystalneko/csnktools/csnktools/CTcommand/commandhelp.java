package com.crystalneko.csnktools.csnktools.CTcommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class commandhelp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage("§a用法:\n§b/csnktools help\t§f获取帮助\n§b/csnktools about\t§f关于");
        } else {
            sender.sendMessage("§a用法:\n§b/csnktools help\t§f获取帮助\n§b/csnktools about\t§f关于");
        }
        return true;
    }
}
