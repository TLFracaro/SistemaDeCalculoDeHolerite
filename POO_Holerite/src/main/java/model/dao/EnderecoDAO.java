package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.entidade.Endereco;

public class EnderecoDAO {
	private Connection connection;

    public EnderecoDAO() {
        connection = getConnection();
    }

    private Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/bancoDeDadosPOO";
            String username = "root";
            String password = "senha";
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public void criarEndereco(Endereco endereco) throws SQLException {
        String query = "INSERT INTO Endereco (cep, logradouro, bairro, complemento, cidade, uf, pais, numero) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, endereco.getCep());
            statement.setString(2, endereco.getLogradouro());
            statement.setString(3, endereco.getBairro());
            statement.setString(4, endereco.getComplemento());
            statement.setString(5, endereco.getCidade());
            statement.setString(6, endereco.getUf());
            statement.setString(7, endereco.getPais());
            statement.setString(8, endereco.getNumero());
            statement.executeUpdate();
        }
    }

    public Endereco buscarEnderecoPorId(int id) throws SQLException {
        String query = "SELECT * FROM Endereco WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Endereco endereco = new Endereco();
                    endereco.setId(resultSet.getInt("id"));
                    endereco.setCep(resultSet.getString("cep"));
                    endereco.setLogradouro(resultSet.getString("logradouro"));
                    endereco.setBairro(resultSet.getString("bairro"));
                    endereco.setComplemento(resultSet.getString("complemento"));
                    endereco.setCidade(resultSet.getString("cidade"));
                    endereco.setUf(resultSet.getString("uf"));
                    endereco.setPais(resultSet.getString("pais"));
                    endereco.setNumero(resultSet.getString("numero"));
                    return endereco;
                } else {
                    return null;
                }
            }
        }
    }

    public void atualizarEndereco(Endereco endereco) throws SQLException {
        String query = "UPDATE Endereco SET cep = ?, logradouro = ?, bairro = ?, complemento = ?, cidade = ?, uf = ?, pais = ?, numero = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, endereco.getCep());
            statement.setString(2, endereco.getLogradouro());
            statement.setString(3, endereco.getBairro());
            statement.setString(4, endereco.getComplemento());
            statement.setString(5, endereco.getCidade());
            statement.setString(6, endereco.getUf());
            statement.setString(7, endereco.getPais());
            statement.setString(8, endereco.getNumero());
            statement.setInt(9, endereco.getId());
            statement.executeUpdate();
        }
    }

    public void excluirEndereco(int id) throws SQLException {
        String query = "DELETE FROM Endereco WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
    
    public int obterUltimoIdInserido() throws SQLException {
        int ultimoId = -1;
        String query = "SELECT LAST_INSERT_ID()";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ultimoId = rs.getInt(1);
                }
            }
        }

        return ultimoId;
    }

}
