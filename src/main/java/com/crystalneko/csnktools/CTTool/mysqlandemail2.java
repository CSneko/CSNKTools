package com.crystalneko.csnktools.CTTool;

import com.crystalneko.csnktools.CSNKTools;
import org.bukkit.Bukkit;

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


    }


    //mysql连接方法
    public Connection createConnection(String sqlitepath) throws SQLException {
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
    // 写入mysql表
    public void writetable(String tablename, String line, String[] value) {
        try (PreparedStatement statement = mysqlconnection.prepareStatement("INSERT INTO " + tablename + " (" + line + ") VALUES (" + getPlaceholder(value.length) + ")")) {
            for (int i = 0; i < value.length; i++) {
                statement.setString(i + 1, value[i]);
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            // 处理异常
            String cantcreattable = plugin.getMessage("mysql.cantcreattable");
            Bukkit.getConsoleSender().sendMessage(cantcreattable + e);
        }
    }

    private String getPlaceholder(int count) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append("?");
            if (i < count - 1) {
                builder.append(",");
            }
        }
        return builder.toString();
    }

    // 读取数据
    public String read_table_data(String columnName, String tableName) {
        try (PreparedStatement statement = mysqlconnection.prepareStatement("SELECT " + columnName + " FROM " + tableName)) {
            ResultSet resultSet = statement.executeQuery();

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

    public String read_table_where_data(String columnName, String tableName, String where, String where_name) {
        try (PreparedStatement statement = mysqlconnection.prepareStatement("SELECT " + columnName + " FROM " + tableName + " WHERE " + where + " = ?")) {
            statement.setString(1, where_name);
            ResultSet resultSet = statement.executeQuery();

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
    //写入表的操作
    public void set_table_data(String tableName, String columnName, String columnValue, String whereName, String newWhereValue) {
        try (PreparedStatement statement = mysqlconnection.prepareStatement("UPDATE " + tableName + " SET " + whereName + " = ? WHERE " + columnName + " = ?")) {
            statement.setString(1, newWhereValue);
            statement.setString(2, columnValue);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //创建列
    public void createColumn(String tableName, String columnName, String columnType) {
        try (Statement statement = mysqlconnection.createStatement()) {
            // 检查列是否存在
            String checkQuery = "DESCRIBE " + tableName + " " + columnName;
            ResultSet resultSet = statement.executeQuery(checkQuery);

            // 如果结果集为空，则列不存在，执行ALTER TABLE语句添加新列
            if (!resultSet.next()) {
                String alterQuery = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnType;
                statement.executeUpdate(alterQuery);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
