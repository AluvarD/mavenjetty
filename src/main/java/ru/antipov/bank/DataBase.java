package ru.antipov.bank;

import org.sqlite.SQLiteDataSource;

import java.sql.*;

public class DataBase {

    /*
    CREATE TABLE "data" (
	    "id"	INTEGER NOT NULL UNIQUE,
	    "data"	TEXT,
	    PRIMARY KEY("id" AUTOINCREMENT)
    );
     */

    private static String url = "jdbc:sqlite:src/db/db.s3db";
    public static SQLiteDataSource dataSource = new SQLiteDataSource();

    public static boolean insertData(String data) {
        dataSource.setUrl(url);
        boolean error = false;
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(5)) {
                System.out.println("Connection is valid.");
                String sqlInsert = "insert into data(data) values (?);";
                PreparedStatement preparedStatementInsert = connection.prepareStatement(sqlInsert);
                preparedStatementInsert.setString(1, data);
                preparedStatementInsert.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            error = true;
            //throw new RuntimeException(e);
        }
        return error;
    }

    public static String selectData() {
        dataSource.setUrl(url);
        StringBuilder result = new StringBuilder();
        try (Connection connection = dataSource.getConnection()){
            if(connection.isValid(5)) {
                System.out.println("Connection is valid.");
                String sqlSelect = "select * from data;";
                PreparedStatement preparedStatementSelect = connection.prepareStatement(sqlSelect);
                ResultSet resultSet = preparedStatementSelect.executeQuery();
                while (resultSet.next()) {
                    result.append("{\"ID\": \"").append(resultSet.getInt("id")).append("\", \"data\": \" ").append(resultSet.getString("data")).append("\"},");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return result.toString();
    }

    public static boolean deleteData(int id) {
        dataSource.setUrl(url);
        boolean error = false;
        try (Connection connection = dataSource.getConnection()){
            System.out.println("Connection is valid.");
            String sqlDelete = "delete from data where id = (?);";
            PreparedStatement preparedStatementDelete = connection.prepareStatement(sqlDelete);
            preparedStatementDelete.setInt(1, id);
            preparedStatementDelete.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            error = true;
        }
        return error;
    }
}