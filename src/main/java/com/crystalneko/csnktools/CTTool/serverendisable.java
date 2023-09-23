package com.crystalneko.csnktools.CTTool;


import com.crystalneko.csnktools.CSNKTools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class serverendisable {
    private CSNKTools csnkTools;
    private mysqlandemail plugin;
    private mysqlandemail2 mysqlAndemail2;
    private sqlite sqLite;
    private String table_name;
    public serverendisable(mysqlandemail plugin,CSNKTools csnkTools,mysqlandemail2 mysqlAndemail2,sqlite sqLite) {
        this.sqLite = sqLite;
        this.plugin = plugin;
        this.csnkTools = csnkTools;
        this.mysqlAndemail2 = mysqlAndemail2;
        table_name = csnkTools.getConfig().getString("serverendisable.table_name");
        //sqlite
        if(!sqLite.isTableExists(table_name)){
            sqLite.createTable(table_name);
        }
        sqLite.addColumn(table_name, "time");
        sqLite.addColumn(table_name, "status");
    }

    public void serverenable(){
        //这里是需要传入的所有参数
        String emailto= csnkTools.getConfig().getString("serverendisable.email");
        String subject = csnkTools.getConfig().getString("serverendisable.enable_subject");
        String[] placeholder = {"<endisabletime />"};
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String[] replacement = {time};
        String htmlpath = "plugins/CSNKTools/email/serverenable.html";
        plugin.setsendmessage(emailto,subject,placeholder,replacement,htmlpath);
        //数据库功能
        if(csnkTools.getConfig().getBoolean("serverendisable.savedatabase")){
            if (csnkTools.getConfig().getBoolean("serverendisable.usesqlit")){
                sqLite.saveData(table_name, "time,status",time +"enable");
            }else{
                String table_line = "time VARCHAR(255),status VARCHAR(100)";
                mysqlAndemail2.create_table(table_name,table_line);
                String mysqlline = "time,status";
                String[] mysqlvalue = {time,"enable"};
                mysqlAndemail2.writetable(table_name,mysqlline,mysqlvalue);
            }
        }
    }
    public void serverdisable(){
        //这里是需要传入的所有参数
        String emailto= csnkTools.getConfig().getString("serverendisable.email");
        String subject = csnkTools.getConfig().getString("serverendisable.disable_subject");
        String[] placeholder = {"<endisabletime />"};
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String[] replacement = {time};
        String htmlpath = "plugins/CSNKTools/email/serverdisable.html";
        plugin.setsendmessage(emailto,subject,placeholder,replacement,htmlpath);
        //数据库功能
        if(csnkTools.getConfig().getBoolean("serverendisable.savedatabase")){
            if (csnkTools.getConfig().getBoolean("serverendisable.usesqlit")){
                sqLite.saveData(table_name, "time,status",time +"disable");
            }else{
                String table_line = "time VARCHAR(255),status VARCHAR(100)";
                mysqlAndemail2.create_table(table_name,table_line);
                String mysqlline = "time,status";
                String[] mysqlvalue = {time,"disable"};
                mysqlAndemail2.writetable(table_name,mysqlline,mysqlvalue);
            }
        }
    }
}
