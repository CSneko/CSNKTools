package com.crystalneko.csnktools.csnktools.CTcommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.crystalneko.csnktools.csnktools.CSNKTools;

public class csnktools implements CommandExecutor {
    private CSNKTools plugin;
    public csnktools(CSNKTools plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 检查是否为玩家执行命令
        if (!(sender instanceof Player)) {
            String onlyplayercandocommand = plugin.getMessage("Command.ct_OPCDC");
            sender.sendMessage(onlyplayercandocommand);
            return true;
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
            // 执行子命令的逻辑
            String ct_help = plugin.getMessage("Command.ct_help");
            player.sendMessage(ct_help);
        } else if (args[0].equalsIgnoreCase("about")) {
            String ct_about = plugin.getMessage("Command.ct_about");
            player.sendMessage(ct_about);
        } else {
            // 无效的子命令
            String nocommand = plugin.getMessage("Command.nocommand");
            player.sendMessage(nocommand);
        }

        return true;
    }
}
