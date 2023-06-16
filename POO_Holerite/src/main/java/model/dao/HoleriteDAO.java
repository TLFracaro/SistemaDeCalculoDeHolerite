package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.entidade.Desconto;
import model.entidade.Holerite;

public class HoleriteDAO {
    private Connection connection;

    public HoleriteDAO() {
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

    public void criarHolerite(Holerite holerite) {
        try {
            String sql = "INSERT INTO holerite (id, mensagem, tot_vencimento, tot_desconto, valor_liquido, salario_base, base_inss, base_irff, base_fgts, faixa_irrf, cpf_funcionario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, holerite.getId());
            statement.setString(2, holerite.getMensagem());
            statement.setDouble(3, holerite.getTotVencimento());
            statement.setDouble(4, holerite.getTotDesconto());
            statement.setDouble(5, holerite.getValorLiquido());
            statement.setDouble(6, holerite.getSalarioBase());
            statement.setDouble(7, holerite.getBaseInss());
            statement.setDouble(8, holerite.getBaseIrff());
            statement.setDouble(9, holerite.getBaseFgts());
            statement.setDouble(10, holerite.getFaixaIrrf());
            statement.setString(11, holerite.getFuncionario().getCpf());
            statement.executeUpdate();
            statement.close();

            List<Desconto> descontos = holerite.getDesconto();
            if (descontos != null) {
                for (Desconto desconto : descontos) {
                    String descontoSql = "INSERT INTO desconto (cod, descricao, referencia, proventos, descontos, holerite_id) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement descontoStatement = connection.prepareStatement(descontoSql);
                    descontoStatement.setString(1, desconto.getCod());
                    descontoStatement.setString(2, desconto.getDescricao());
                    descontoStatement.setDouble(3, desconto.getReferencia());
                    descontoStatement.setDouble(4, desconto.getProventos());
                    descontoStatement.setDouble(5, desconto.getDescontos());
                    descontoStatement.setInt(6, holerite.getId());
                    descontoStatement.executeUpdate();
                    descontoStatement.close();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Holerite obterHoleritePorCpf(String cpf) {
        Holerite holerite = null;
        try {
            String sql = "SELECT * FROM holerite WHERE cpf_funcionario = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, cpf);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                holerite = new Holerite();
                holerite.setId(resultSet.getInt("id"));
                holerite.setMensagem(resultSet.getString("mensagem"));
                holerite.setTotVencimento(resultSet.getDouble("tot_vencimento"));
                holerite.setTotDesconto(resultSet.getDouble("tot_desconto"));
                holerite.setValorLiquido(resultSet.getDouble("valor_liquido"));
                holerite.setSalarioBase(resultSet.getDouble("salario_base"));
                holerite.setBaseInss(resultSet.getDouble("base_inss"));
                holerite.setBaseIrff(resultSet.getDouble("base_irff"));
                holerite.setBaseFgts(resultSet.getDouble("base_fgts"));
                holerite.setFaixaIrrf(resultSet.getDouble("faixa_irrf"));

                String descontoSql = "SELECT * FROM desconto WHERE holerite_id = ?";
                PreparedStatement descontoStatement = connection.prepareStatement(descontoSql);
                descontoStatement.setInt(1, holerite.getId());
                ResultSet descontoResultSet = descontoStatement.executeQuery();

                List<Desconto> descontos = new ArrayList<>();
                while (descontoResultSet.next()) {
                    Desconto desconto = new Desconto();
                    desconto.setCod(descontoResultSet.getString("cod"));
                    desconto.setDescricao(descontoResultSet.getString("descricao"));
                    desconto.setReferencia(descontoResultSet.getDouble("referencia"));
                    desconto.setProventos(descontoResultSet.getDouble("proventos"));
                    desconto.setDescontos(descontoResultSet.getDouble("descontos"));

                    descontos.add(desconto);
                }
                holerite.setDesconto(descontos);

                descontoResultSet.close();
                descontoStatement.close();
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return holerite;
    }

    public void atualizarHolerite(Holerite holerite, String cpf) {
        try {
            String sql = "UPDATE holerite SET mensagem = ?, tot_vencimento = ?, tot_desconto = ?, valor_liquido = ?, salario_base = ?, base_inss = ?, base_irff = ?, base_fgts = ?, faixa_irrf = ? WHERE cpf_funcionario = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, holerite.getMensagem());
            statement.setDouble(2, holerite.getTotVencimento());
            statement.setDouble(3, holerite.getTotDesconto());
            statement.setDouble(4, holerite.getValorLiquido());
            statement.setDouble(5, holerite.getSalarioBase());
            statement.setDouble(6, holerite.getBaseInss());
            statement.setDouble(7, holerite.getBaseIrff());
            statement.setDouble(8, holerite.getBaseFgts());
            statement.setDouble(9, holerite.getFaixaIrrf());
            statement.setString(10, cpf);
            statement.executeUpdate();
            statement.close();

            String deleteDescontoSql = "DELETE FROM desconto WHERE holerite_id = ?";
            PreparedStatement deleteDescontoStatement = connection.prepareStatement(deleteDescontoSql);
            deleteDescontoStatement.setInt(1, holerite.getId());
            deleteDescontoStatement.executeUpdate();
            deleteDescontoStatement.close();

            List<Desconto> descontos = holerite.getDesconto();
            if (descontos != null) {
                for (Desconto desconto : descontos) {
                    String descontoSql = "INSERT INTO desconto (cod, descricao, referencia, proventos, descontos, holerite_id) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement descontoStatement = connection.prepareStatement(descontoSql);
                    descontoStatement.setString(1, desconto.getCod());
                    descontoStatement.setString(2, desconto.getDescricao());
                    descontoStatement.setDouble(3, desconto.getReferencia());
                    descontoStatement.setDouble(4, desconto.getProventos());
                    descontoStatement.setDouble(5, desconto.getDescontos());
                    descontoStatement.setInt(6, holerite.getId());
                    descontoStatement.executeUpdate();
                    descontoStatement.close();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluirHolerite(int id) {
        try {
            String deleteDescontoSql = "DELETE FROM desconto WHERE holerite_id = ?";
            PreparedStatement deleteDescontoStatement = connection.prepareStatement(deleteDescontoSql);
            deleteDescontoStatement.setInt(1, id);
            deleteDescontoStatement.executeUpdate();
            deleteDescontoStatement.close();

            String sql = "DELETE FROM holerite WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Holerite> consultarHolerites() {
        List<Holerite> holerites = new ArrayList<>();
        try {
            String sql = "SELECT * FROM holerite";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Holerite holerite = new Holerite();
                holerite.setId(resultSet.getInt("id"));
                holerite.setMensagem(resultSet.getString("mensagem"));
                holerite.setTotVencimento(resultSet.getDouble("totVencimento"));
                holerite.setTotDesconto(resultSet.getDouble("totDesconto"));
                holerite.setValorLiquido(resultSet.getDouble("valorLiquido"));
                holerite.setSalarioBase(resultSet.getDouble("salarioBase"));
                holerite.setBaseInss(resultSet.getDouble("baseInss"));
                holerite.setBaseIrff(resultSet.getDouble("baseIrff"));
                holerite.setBaseFgts(resultSet.getDouble("baseFgts"));
                holerite.setFaixaIrrf(resultSet.getDouble("faixaIrrf"));

                String descontoSql = "SELECT * FROM desconto WHERE idHolerite = ?";
                PreparedStatement descontoStatement = connection.prepareStatement(descontoSql);
                descontoStatement.setInt(1, holerite.getId());
                ResultSet descontoResultSet = descontoStatement.executeQuery();

                List<Desconto> descontos = new ArrayList<>();
                while (descontoResultSet.next()) {
                    Desconto desconto = new Desconto();
                    desconto.setCod(descontoResultSet.getString("cod"));
                    desconto.setDescricao(descontoResultSet.getString("descricao"));
                    desconto.setReferencia(descontoResultSet.getDouble("referencia"));
                    desconto.setProventos(descontoResultSet.getDouble("proventos"));
                    desconto.setDescontos(descontoResultSet.getDouble("descontos"));

                    descontos.add(desconto);
                }
                holerite.setDesconto(descontos);

                descontoResultSet.close();
                descontoStatement.close();

                holerites.add(holerite);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return holerites;
    }
}
