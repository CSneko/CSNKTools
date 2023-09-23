package com.crystalneko.csnktools.CTTool;

import com.crystalneko.csnktools.CSNKTools;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class sqlite {
    public Connection sqliteconnection;
    private CSNKTools plugin;
    public sqlite(CSNKTools plugin) {
        this.plugin = plugin;
        try {
            createConnection();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    public Connection createConnection() throws SQLException {
        if (sqliteconnection != null && !sqliteconnection.isClosed()) {
            return sqliteconnection;
        }

        String sqlitePath = plugin.getConfig().getString("sqlite.path");

        // 检查SQLite数据库文件是否存在
        File sqliteFile = new File(sqlitePath);

        if (!sqliteFile.exists()) {
            // 如果数据库文件不存在，尝试创建它
            try {
                if (sqliteFile.createNewFile()) {
                    Bukkit.getConsoleSender().sendMessage(plugin.getMessage("Plugins.sqlite.createFile"));
                } else {
                    Bukkit.getConsoleSender().sendMessage(plugin.getMessage("Plugins.sqlite.FailedCreateFile"));
                }
            } catch (IOException e) {
                Bukkit.getConsoleSender().sendMessage(plugin.getMessage("Plugins.sqlite.UnableCreateFile"));
                e.printStackTrace();
            }
        }

        // 构建SQLite连接字符串
        String sqliteConnectionUrl = "jdbc:sqlite:" + sqlitePath;
        sqliteconnection = DriverManager.getConnection(sqliteConnectionUrl);
        return sqliteconnection;
    }


    /**
     * 保存数据到数据库
     *
     * @param tableName 表名
     * @param columnName 列名
     * @param data      要保存的数据
     */
    public void saveData(String tableName, String columnName, String data) {
        // 检查表是否存在，如果不存在则创建表
        if (!isTableExists(tableName)) {
            createTable(tableName);
        }

        // 检查列是否存在，如果不存在则添加列
        //if (!isColumnExists(tableName, columnName)) {
            addColumn(tableName, columnName);
        //}

        String query = "INSERT INTO " + tableName + " (" + columnName + ") VALUES (?)";

        try (PreparedStatement statement = sqliteconnection.prepareStatement(query)) {
            statement.setString(1, data);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     *在tableName表中，需要更改columnName,当满足whereName = data时，则执行update
     * @param tableName 表名
     * @param columnName 要更改的列（如: balance）
     * @param whereName 要更改的条件列 (如：username)
     * @param whereValue 要更改的条件值 (如：CrystalNeko)
     * @param columnValue 更改后的值(如：123)
     */
    public void saveDataWhere(String tableName, String columnName, String whereName, String whereValue, String columnValue) {
        // 检查表是否存在，如果不存在则创建表
        if (!isTableExists(tableName)) {
            createTable(tableName);
        }

        String query = "UPDATE " + tableName + " SET " + columnName + " = ? WHERE " + whereName + " = ?";
        try (PreparedStatement statement = sqliteconnection.prepareStatement(query)) {
            statement.setString(1, columnValue);
            statement.setString(2, whereValue);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    /**
     * 创建一行数据
     * @param tableName 表名
     * @param columnName 列名（a,b,c）
     * @param columnValue 创建的值(1,2,crystalneko)
     */
    public void saveColumn(String tableName, String columnName, Object columnValue) {
        // 检查表是否存在，如果不存在则创建表
        if (!isTableExists(tableName)) {
            createTable(tableName);
        }
        // 检查列是否存在，如果不存在则添加列
        //if (!isColumnExists(tableName, columnName)) {
            addColumn(tableName, columnName);
        //}
        String query = "INSERT OR IGNORE INTO " + tableName + "(" + columnName + ") VALUES (?)";
        try (PreparedStatement statement = sqliteconnection.prepareStatement(query)) {
            statement.setObject(1, columnValue);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 检查表是否存在
     *
     * @param tableName 表名
     * @return 表是否存在
     */
    public boolean isTableExists(String tableName) {
        try  {
            DatabaseMetaData metaData = sqliteconnection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, tableName, null);
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    /**
     * 创建表
     *
     * @param tableName 表名
     */
    public void createTable(String tableName) {
        String query = "CREATE TABLE " + tableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT)";
        try (PreparedStatement statement = sqliteconnection.prepareStatement(query)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * 检查列是否存在
     *
     * @param tableName  表名
     * @param columnName 列名
     * @return 列是否存在
     */
    private boolean isColumnExists(String tableName, String columnName) {
        try {
            DatabaseMetaData metaData = sqliteconnection.getMetaData();
            ResultSet resultSet = metaData.getColumns(null, null, tableName, columnName);
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    /**
     * 添加列
     *
     * @param tableName  表名
     * @param columnName 列名
     */
    public void addColumn(String tableName, String columnName) {
        if (checkColumnExists(tableName, columnName)) {
            return;
        }

        String query = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " TEXT";
        try (PreparedStatement statement = sqliteconnection.prepareStatement(query)) {

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    public boolean checkColumnExists(String tableName, String columnName) {
        try (ResultSet resultSet = sqliteconnection.getMetaData().getColumns(null, null, tableName, columnName)) {
            return resultSet.next();  // 如果结果集有下一个元素，则表示列存在
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }
    /**
    *检查数据库的某个列中是否有某个值
     *
     * @param tableName 表名
     * @param columnName 列名
     * @param value 要检查的值
     */
    public boolean checkValueExists(String tableName, String columnName, String value) {
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + " = ?";
        try (PreparedStatement statement = sqliteconnection.prepareStatement(query)) {

            statement.setString(1, value);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return (count > 0);
            }

        } catch (SQLException e) {
            System.out.println(e);
        }

        return false;
    }

    /**
     * 读取某个特定的值
     * @param tableName 表名
     * @param columnName 列名
     * @param whereName 条件列名
     * @param whereValue 条件值
     * @return 查询结果
     */
    public String getColumnValue(String tableName, String columnName, String whereName, String whereValue) {
        String query = "SELECT " + columnName + " FROM " + tableName + " WHERE " + whereName + "=?";
        try (PreparedStatement statement = sqliteconnection.prepareStatement(query)) {
            statement.setString(1, whereValue);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString(columnName);
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }



}
