package com.crystalneko.csnktools.sql;

import com.crystalneko.csnktools.CSNKTools;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import com.crystalneko.csnktools.CTTool.sqlite;

public class PlayerJoinListener implements Listener {

    private sqlite sqLite;
    private CSNKTools plugin;
    private String tableName;
    private String[] columName = {"name","regip","uuid"};
    public PlayerJoinListener(sqlite sqLite,CSNKTools plugin) {
        this.sqLite = sqLite;
        this.plugin = plugin;
        tableName = plugin.getConfig().getString("sqlite.player_data_table");
        if(!sqLite.isTableExists(tableName)){
            sqLite.createTable(tableName);
        }
        addSqliteColum();
    }
    // 玩家加入事件
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //获取玩家对象
        Player player = event.getPlayer();
        //获取玩家名
        String name = player.getName();
        //获取玩家uuid
        String uuid = String.valueOf(player.getUniqueId());
        //获取玩家注册ip
        String regip = String.valueOf(player.getAddress());
        //合并数据
        String[] columnValue = {name,regip,uuid};
        //写入uuid
        if(!sqLite.checkValueExists(tableName,"uuid",uuid)) {
            sqLite.saveData(tableName, "uuid", uuid);
        }
        int i =0;
        while (i <= 1) {
            sqLite.saveDataWhere(tableName,columName[i],"uuid",uuid,columnValue[i]);
            i++;
        }
    }
    private void addSqliteColum(){
        int i =0;
        while (i <= 2) {
            sqLite.addColumn(tableName, columName[i]);
            i ++;
        }
    }
}