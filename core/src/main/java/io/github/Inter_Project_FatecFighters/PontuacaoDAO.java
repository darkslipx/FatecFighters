package io.github.Inter_Project_FatecFighters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PontuacaoDAO {

    // Método para salvar pontuação no banco
    public boolean salvarPontuacao(String nomeUsuario, int pontuacao) {
        // Certifique-se de que o usuário existe antes de salvar a pontuação
        if (!verificarUsuarioExiste(nomeUsuario)) {
            criarUsuario(nomeUsuario);
        }

        String sql = "INSERT INTO pontuacoes (id_usuario, pontuacao) VALUES ((SELECT id FROM usuarios WHERE nome_usuario = ?), ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeUsuario);
            stmt.setInt(2, pontuacao);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para listar todas as pontuações
    public List<String> listarPontuacoes() {
        String sql = "SELECT u.nome_usuario, p.pontuacao, p.data FROM pontuacoes p JOIN usuarios u ON p.id_usuario = u.id ORDER BY p.pontuacao DESC";
        List<String> pontuacoes = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String linha = rs.getString("nome_usuario") + ": " +
                    rs.getInt("pontuacao") + " - " +
                    rs.getTimestamp("data");
                pontuacoes.add(linha);
            }
            System.out.println("Pontuações listadas: " + pontuacoes);
        } catch (SQLException e) {
            System.err.println("Erro ao listar pontuações: " + e.getMessage());
        }
        return pontuacoes;
    }

    // Método para listar as pontuações de um único jogador
// Método para listar pontuações do usuário conectado
    public List<String> listarPontuacoesPorUsuario(String nomeUsuario) {
        String sql = "SELECT p.pontuacao FROM pontuacoes p " +
            "JOIN usuarios u ON p.id_usuario = u.id " +
            "WHERE u.nome_usuario = ? ORDER BY p.pontuacao DESC";
        List<String> pontuacoes = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeUsuario); // Filtra pelo nome do usuário conectado
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Adiciona somente a pontuação no formato "Pontuação: [valor]"
                    pontuacoes.add("" + rs.getInt("pontuacao"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar pontuações do usuário: " + e.getMessage());
        }
        return pontuacoes;
    }

    // Método para verificar se um usuário existe no banco
    private boolean verificarUsuarioExiste(String nomeUsuario) {
        String sql = "SELECT 1 FROM usuarios WHERE nome_usuario = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeUsuario);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Retorna true se encontrar o usuário
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para criar um novo usuário (caso não exista)
    private boolean criarUsuario(String nomeUsuario) {
        String sql = "INSERT INTO usuarios (nome_usuario, senha) VALUES (?, 'default')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeUsuario);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
