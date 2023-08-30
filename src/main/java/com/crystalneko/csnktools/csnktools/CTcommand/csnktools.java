package com.crystalneko.csnktools.csnktools.CTcommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class csnktools implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 检查是否为玩家执行命令
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c只有玩家才能执行该命令");
            return true;
        }

        // 检查是否有足够的参数
        if (args.length < 1) {
            sender.sendMessage("§a使用方法: \n§b/csnktools help §3获取帮助\n§b/csnktools about §3关于");
            return true;
        }

        // 获取玩家对象
        Player player = (Player) sender;

        // 处理子命令
        if (args[0].equalsIgnoreCase("help")) {
            // 执行子命令1的逻辑
            player.sendMessage("§a使用方法: \n§b/csnktools help §3获取帮助\n§b/csnktools about §3关于");
        } else if (args[0].equalsIgnoreCase("about")) {
            // 执行子命令2的逻辑
            player.sendMessage("§6版本:§aV0.0.3 §e作者:§6CrystalNeko §a项目地址:§bhttps://github.com/CSneko/CSNKTools");
        } else {
            // 无效的子命令
            player.sendMessage("§c无效的子命令");
        }

        return true;
    }
}
