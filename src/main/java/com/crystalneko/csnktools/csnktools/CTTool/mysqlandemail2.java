package com.crystalneko.csnktools.csnktools.CTTool;

import com.crystalneko.csnktools.csnktools.CSNKTools;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class mysqlandemail2 {
    private CSNKTools plugin;
    private boolean useMySQL;
    private String mysqlHost;
    private int mysqlPort;
    private String mysqlDatabase;
    private String mysqlUsername;
    private String mysqlPassword;
    private String mysqltime;
    private Boolean mysqlusessl;
    private String mysqlchar;
    public Connection mysqlconnection;
    public mysqlandemail2(CSNKTools plugin) {
        this.plugin = plugin;
    }
    public void loadConfig(){
        // 解析MySQL配置
        useMySQL = plugin.getConfig().getBoolean("mysql.Enable");
        mysqlHost = plugin.getConfig().getString("mysql.host");
        mysqlPort = plugin.getConfig().getInt("mysql.port");
        mysqlDatabase = plugin.getConfig().getString("mysql.database");
        mysqlUsername = plugin.getConfig().getString("mysql.username");
        mysqlPassword = plugin.getConfig().getString("mysql.password");
        mysqltime = plugin.getConfig().getString("mysql.time");
        mysqlchar = plugin.getConfig().getString("mysql.char");
        mysqlusessl = plugin.getConfig().getBoolean("mysql.usessl");
        String sqlitepath = plugin.getConfig().getString("sqlite.path");

        try {
            mysqlconnection = createConnection(sqlitepath);
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(plugin.getMessage("Error.cantmysql"));
        }

        // 执行 SQL 语句
            String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = ? AND table_name = ?";
            try (PreparedStatement statement = mysqlconnection.prepareStatement(sql)) {
                statement.setString(1, mysqlDatabase);
                statement.setString(2, "test");
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        // 处理结果
                    }
                }
            } catch (SQLException e) {
                // 处理异常
            }
    }


    //mysql连接方法
    public Connection createConnection(String sqlitepath) throws SQLException {
        if (useMySQL) {
            try {
                Class.forName(plugin.getConfig().getString("mysql.drive"));
                Bukkit.getConsoleSender().sendMessage(plugin.getMessage("Plugins.mysql.mysqldriveloaded"));
            } catch (ClassNotFoundException e) {
                Bukkit.getConsoleSender().sendMessage(plugin.getMessage("Plugins.mysql.cantmysqldrive"));
                e.printStackTrace();
            }

            // 构建MySQL连接字符串
            String mysqlConnectionUrl = "jdbc:mysql://" + mysqlHost + ":" + mysqlPort + "/" + mysqlDatabase+ "?user="+mysqlUsername+ "&password="+mysqlPassword+"password&characterEncoding="+ mysqlchar +"&useSSL="+ mysqlusessl +"&serverTimezone="+mysqltime;
            return DriverManager.getConnection(mysqlConnectionUrl, mysqlUsername, mysqlPassword);
        } else {
            // 检查SQLite数据库文件是否存在
            File sqliteFile = new File(sqlitepath);

            if (!sqliteFile.exists()) {
                // 如果数据库文件不存在，尝试创建它
                try {
                    sqliteFile.createNewFile();
                }catch (IOException e) {
                    Bukkit.getConsoleSender().sendMessage(plugin.getMessage("Error.cantcreatsqlite"));

                }

            }

            // 构建SQLite连接字符串
            String sqliteConnectionUrl = "jdbc:sqlite:" + sqlitepath;
            return DriverManager.getConnection(sqliteConnectionUrl);
        }
    }

    //创建数据库操作
    public void creatdatabase(String databasename) {
        try (Statement statement = mysqlconnection.createStatement()) {
            String createDatabaseSql = "CREATE DATABASE IF NOT EXISTS "+ databasename;
            statement.executeUpdate(createDatabaseSql);
        } catch (SQLException e) {
            // 处理异常
        }
    }
    //创建表操作
    public void create_table(String table_name,String table_line) {
        try (Statement statement = mysqlconnection.createStatement()) {
            String createTableSql = "CREATE TABLE IF NOT EXISTS "+table_name+" ("+table_line+")";
            statement.executeUpdate(createTableSql);
        } catch (SQLException e) {
            // 处理异常
            String cantcreattable = plugin.getMessage("mysql.cantcreattable");
            Bukkit.getConsoleSender().sendMessage( cantcreattable+e);
        }
    }
    //写入mysql表
    public void writetable(String tablename,String line,String[] value){
        try (Statement statement = mysqlconnection.createStatement()) {
            String result_value=args_toString(value);
            String insertSql = "INSERT INTO "+tablename+" ("+line+") VALUES ("+result_value+")";
            statement.executeUpdate(insertSql);
        } catch (SQLException e) {
            // 处理异常
            String cantcreattable = plugin.getMessage("mysql.cantcreattable");
            Bukkit.getConsoleSender().sendMessage( cantcreattable+e);
        }
    }
    //读取数据
    public String read_table_data(String columnName, String tableName) {
        try (Statement statement = mysqlconnection.createStatement()) {
            String selectSql = "SELECT " + columnName + " FROM " + tableName;
            ResultSet resultSet = statement.executeQuery(selectSql);

            while (resultSet.next()) {
                String value = resultSet.getString(columnName);
                return value;
            }

            // 如果没有数据，则返回一个默认值
            return null;
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(plugin.getMessage("mysql.cantreaddata") + e);
            return null;
        }
    }
    //hash操作
    public String read_password_and_salt(String username,String tableName) {
        try (Statement statement = mysqlconnection.createStatement()) {
            String selectSql = "SELECT password FROM " + tableName + " WHERE username='" + username + "'";
            ResultSet resultSet = statement.executeQuery(selectSql);

            if (resultSet.next()) {
                String passwordHash = resultSet.getString("password");
                return passwordHash;
            } else {
                return null;
            }
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(plugin.getMessage("mysql.cantreaddata") + e);
            return null;
        }
    }


    //将数组转换为String
    public String args_toString(String[] args) {
        String result = Arrays.stream(args)
                .map(s -> "'" + s + "'")
                .collect(Collectors.joining(","));
        return result;
    }


}
