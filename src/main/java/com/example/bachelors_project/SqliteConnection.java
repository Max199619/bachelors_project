package com.example.bachelors_project;

import java.sql.Connection;
import java.sql.DriverManager;

public class SqliteConnection {
    public Connection databaseLink;
    public String databaseName = "";

    public Connection getConnection() {
        String url = "jdbc:sqlite:C:\\Users\\19dpr\\projects\\bachelors project\\src\\main\\resources\\db\\" + databaseName;
        try {
            Class.forName("org.sqlite.JDBC");
            databaseLink = DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return databaseLink;
    }
}

