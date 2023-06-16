package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.entidade.Empregador;
import model.entidade.Endereco;

public class EmpregadorDAO {
	private Connection connection;

    public EmpregadorDAO() {
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

    public void criarEmpregador(Empregador empregador) throws SQLException {
        String query = "INSERT INTO Empregador (cnpj, razaoSocial, email, senha, endereco_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, empregador.getCnpj());
            statement.setString(2, empregador.getRazaoSocial());
            statement.setString(3, empregador.getEmail());
            statement.setString(4, empregador.getSenha());
            statement.setInt(5, empregador.getEndereco().getId());
            statement.executeUpdate();
        }
    }

    public Empregador buscarEmpregadorPorCnpj(String cnpj) throws SQLException {
        String query = "SELECT * FROM Empregador WHERE cnpj = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, cnpj);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Empregador empregador = new Empregador();
                    empregador.setCnpj(resultSet.getString("cnpj"));
                    empregador.setRazaoSocial(resultSet.getString("razaoSocial"));
                    empregador.setEmail(resultSet.getString("email"));
                    empregador.setSenha(resultSet.getString("senha"));
                    int enderecoId = resultSet.getInt("endereco_id");
                    EnderecoDAO enderecoDAO = new EnderecoDAO();
                    Endereco endereco = enderecoDAO.buscarEnderecoPorId(enderecoId);
                    empregador.setEndereco(endereco);
                    return empregador;
                } else {
                    return null;
                }
            }
        }
    }
    
    public boolean existeCnpj(String cnpj) throws SQLException {
        String query = "SELECT * FROM Empregador WHERE cnpj = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, cnpj);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                	String cnpjBd = resultSet.getString("cnpj");
                	if(cnpjBd.equals(cnpj)) {
                		return true;
                	}
                } 
            }
        }
        return false;
    }

    public void atualizarEmpregador(Empregador empregador) throws SQLException {
        String query = "UPDATE Empregador SET razaoSocial = ?, email = ?, senha = ?, endereco_id = ? WHERE cnpj = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, empregador.getRazaoSocial());
            statement.setString(2, empregador.getEmail());
            statement.setString(3, empregador.getSenha());
            statement.setInt(4, empregador.getEndereco().getId());
            statement.setString(5, empregador.getCnpj());
            statement.executeUpdate();
        }
    }

    public void excluirEmpregador(String cnpj) throws SQLException {
        String query = "DELETE FROM Empregador WHERE cnpj = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, cnpj);
            statement.executeUpdate();
        }
    }

    public Empregador loginEmpregador(String email, String senha) throws SQLException {
        String query = "SELECT * FROM Empregador WHERE email = ? AND senha = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, senha);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Empregador empregador = new Empregador();
                    empregador.setCnpj(resultSet.getString("cnpj"));
                    empregador.setRazaoSocial(resultSet.getString("razaoSocial"));
                    empregador.setEmail(resultSet.getString("email"));
                    empregador.setSenha(resultSet.getString("senha"));
                    int enderecoId = resultSet.getInt("endereco_id");
                    EnderecoDAO enderecoDAO = new EnderecoDAO();
                    Endereco endereco = enderecoDAO.buscarEnderecoPorId(enderecoId);
                    empregador.setEndereco(endereco);
                    return empregador;
                } else {
                    return null;
                }
            }
        }
    }
}
