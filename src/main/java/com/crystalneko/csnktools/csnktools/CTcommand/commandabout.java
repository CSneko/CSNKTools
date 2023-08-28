package com.crystalneko.csnktools.csnktools.CTcommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class commandabout implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage("§e作者:CrystalNeko §6当前版本:§cV0.0.1 §a项目地址: §bhttps://github.com/CSneko/CSNKTools");
        } else {
            sender.sendMessage("§e作者:CrystalNeko §6当前版本:§cV0.0.1 §a项目地址: §bhttps://github.com/CSneko/CSNKTools");
        }
        return true;
    }
}
