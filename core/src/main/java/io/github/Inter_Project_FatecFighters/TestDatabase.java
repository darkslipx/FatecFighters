package io.github.Inter_Project_FatecFighters;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDatabase {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/jogo";
        String user = "jogo_user";
        String password = "310195";

        try {
            // Registrar o driver do PostgreSQL manualmente
            Class.forName("org.postgresql.Driver");

            // Testar conexão
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Conexão bem-sucedida com o banco de dados");

            // Fechar conexão
            connection.close();
        } catch (ClassNotFoundException e) {
            System.err.println("Driver do PostgreSQL não encontrado!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Erro na conexão com o banco de dados!");
            e.printStackTrace();
        }
    }
}
