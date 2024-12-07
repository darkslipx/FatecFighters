package io.github.Inter_Project_FatecFighters;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Configure os dados do banco aqui
    private static final String URL = "jdbc:postgresql://localhost:5432/jogo";
    private static final String USER = "jogo_user";
    private static final String PASSWORD = "310195"; // Alteere aqui para a senha correta

    // Método para obter conexão
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
