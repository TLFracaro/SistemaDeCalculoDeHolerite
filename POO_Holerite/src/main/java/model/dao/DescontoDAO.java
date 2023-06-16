package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.entidade.Desconto;

public class DescontoDAO {
    private Connection connection;

    public DescontoDAO(Connection connection) {
        this.connection = connection;
    }

    public void criarDesconto(Desconto desconto) throws SQLException {
        String query = "INSERT INTO Desconto (cod, descricao, referencia, proventos, descontos) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, desconto.getCod());
            statement.setString(2, desconto.getDescricao());
            statement.setDouble(3, desconto.getReferencia());
            statement.setDouble(4, desconto.getProventos());
            statement.setDouble(5, desconto.getDescontos());
            statement.executeUpdate();
        }
    }

    public Desconto buscarDescontoPorCod(String cod) throws SQLException {
        String query = "SELECT * FROM Desconto WHERE cod = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, cod);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapearDesconto(resultSet);
                }
            }
        }
        return null;
    }

    public List<Desconto> buscarTodosDescontos() throws SQLException {
        List<Desconto> descontos = new ArrayList<>();
        String query = "SELECT * FROM Desconto";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                descontos.add(mapearDesconto(resultSet));
            }
        }
        return descontos;
    }

    public void atualizarDesconto(Desconto desconto) throws SQLException {
        String query = "UPDATE Desconto SET descricao = ?, referencia = ?, proventos = ?, descontos = ? WHERE cod = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, desconto.getDescricao());
            statement.setDouble(2, desconto.getReferencia());
            statement.setDouble(3, desconto.getProventos());
            statement.setDouble(4, desconto.getDescontos());
            statement.setString(5, desconto.getCod());
            statement.executeUpdate();
        }
    }

    public void deletarDesconto(String cod) throws SQLException {
        String query = "DELETE FROM Desconto WHERE cod = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, cod);
            statement.executeUpdate();
        }
    }

    private Desconto mapearDesconto(ResultSet resultSet) throws SQLException {
        String cod = resultSet.getString("cod");
        String descricao = resultSet.getString("descricao");
        double referencia = resultSet.getDouble("referencia");
        double proventos = resultSet.getDouble("proventos");
        double descontos = resultSet.getDouble("descontos");
        return new Desconto(cod, descricao, referencia, proventos, descontos);
    }
}
