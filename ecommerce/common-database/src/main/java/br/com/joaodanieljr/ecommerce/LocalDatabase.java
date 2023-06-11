package br.com.joaodanieljr.ecommerce;

import java.sql.*;
import java.util.UUID;

public class LocalDatabase {
    private final Connection connection;

    LocalDatabase(String name) throws SQLException {
        String url = "jdbc:sqlite:service-users/target/"+ name +".db";
        this.connection = DriverManager.getConnection(url);
    }

    public void createIfNotExists(String sql){
        try {
            connection.createStatement().execute(sql);
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public void update(String statement, String... params) throws SQLException {
        var preparedStatement = prepare(statement, params);
        preparedStatement.execute();
    }



    public ResultSet query(String query, String... params) throws SQLException {
        var preparedStatement = prepare(query, params);
        return preparedStatement.executeQuery();
    }

    private PreparedStatement prepare(String statement, String[] params) throws SQLException {
        var preparedStatement = connection.prepareStatement(statement);
        for(int i = 0; i < params.length; i++ ){
            preparedStatement.setString(i+1, params[i]);
        }
        return preparedStatement;
    }
}
