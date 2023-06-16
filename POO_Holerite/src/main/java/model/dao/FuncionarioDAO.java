package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.entidade.Empregador;
import model.entidade.Funcionario;

public class FuncionarioDAO {
	private Connection connection;

	public FuncionarioDAO() {
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

	public void criarFuncionario(Funcionario funcionario) throws SQLException {
		String query = "INSERT INTO Funcionario (cpf, nome, cbo, funcao, empregador_cnpj) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, funcionario.getCpf());
			statement.setString(2, funcionario.getNome());
			statement.setString(3, funcionario.getCbo());
			statement.setString(4, funcionario.getFuncao());
			statement.setString(5, funcionario.getEmpregador().getCnpj());
			statement.executeUpdate();
		}
	}

	public Funcionario buscarFuncionarioPorCpf(String cpf, Empregador usuarioLogado) throws SQLException {
		String query = "SELECT * FROM Funcionario WHERE cpf = ? AND empregador_cnpj = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, cpf);
			statement.setString(2, usuarioLogado.getCnpj());
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					Funcionario funcionario = new Funcionario();
					funcionario.setCpf(resultSet.getString("cpf"));
					funcionario.setNome(resultSet.getString("nome"));
					funcionario.setCbo(resultSet.getString("cbo"));
					funcionario.setFuncao(resultSet.getString("funcao"));
					String empregadorCnpj = resultSet.getString("empregador_cnpj");
					EmpregadorDAO empregadorDAO = new EmpregadorDAO();
					Empregador empregador = empregadorDAO.buscarEmpregadorPorCnpj(empregadorCnpj);
					funcionario.setEmpregador(empregador);
					return funcionario;
				} else {
					return null;
				}
			}
		}
	}
	
	public boolean cpfCadastrado(String cpf) throws SQLException {
	    String query = "SELECT * FROM funcionario WHERE cpf = ?";
	    try (PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setString(1, cpf);
	        try (ResultSet resultSet = statement.executeQuery()) {
	            if (resultSet.next()) {
	            	String cpfBd = resultSet.getString("cpf");
	            	if(cpfBd.equals(cpf)) {
	            		return true;
	            	}
	            }
	        }
	    }
	    return false;
	}


	public void atualizarFuncionario(Funcionario funcionario, Empregador usuarioLogado) throws SQLException {
		String query = "UPDATE Funcionario SET nome = ?, cbo = ?, funcao = ? WHERE cpf = ? AND empregador_cnpj = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, funcionario.getNome());
			statement.setString(2, funcionario.getCbo());
			statement.setString(3, funcionario.getFuncao());
			statement.setString(4, funcionario.getCpf());
			statement.setString(5, usuarioLogado.getCnpj());
			statement.executeUpdate();
		}
	}

	public void excluirFuncionario(String cpf, Empregador usuarioLogado) throws SQLException {
	    String query = "DELETE FROM Funcionario WHERE cpf = ? AND empregador_cnpj = ?";
	    try (PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setString(1, cpf);
	        statement.setString(2, usuarioLogado.getCnpj());
	        excluirHoleritesPorCPF(cpf);
	        statement.executeUpdate();
	    }
	}

	public void excluirHoleritesPorCPF(String cpf) throws SQLException {
	    String selectSql = "SELECT id FROM holerite WHERE cpf_funcionario = ?";
	    try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
	        selectStatement.setString(1, cpf);
	        ResultSet resultSet = selectStatement.executeQuery();

	        while (resultSet.next()) {
	            int holeriteId = resultSet.getInt("id");
	            excluirHolerite(holeriteId);
	        }
	    }
	}

	public void excluirHolerite(int id) throws SQLException {
	    String deleteDescontoSql = "DELETE FROM desconto WHERE holerite_id = ?";
	    try (PreparedStatement deleteDescontoStatement = connection.prepareStatement(deleteDescontoSql)) {
	        deleteDescontoStatement.setInt(1, id);
	        deleteDescontoStatement.executeUpdate();
	    }

	    String sql = "DELETE FROM holerite WHERE id = ?";
	    try (PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setInt(1, id);
	        statement.executeUpdate();
	    }
	}

	public List<Funcionario> listarFuncionarios(Empregador usuarioLogado) throws SQLException {
	    List<Funcionario> funcionarios = new ArrayList<>();

	    String query = "SELECT * FROM Funcionario WHERE empregador_cnpj = ?";
	    try (PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setString(1, usuarioLogado.getCnpj());
	        try (ResultSet resultSet = statement.executeQuery()) {
	            while (resultSet.next()) {
	                Funcionario funcionario = new Funcionario();
	                funcionario.setCpf(resultSet.getString("cpf"));
	                funcionario.setNome(resultSet.getString("nome"));
	                funcionario.setCbo(resultSet.getString("cbo"));
	                funcionario.setFuncao(resultSet.getString("funcao"));
	                String empregadorCnpj = resultSet.getString("empregador_cnpj");
	                EmpregadorDAO empregadorDAO = new EmpregadorDAO();
	                Empregador empregador = empregadorDAO.buscarEmpregadorPorCnpj(empregadorCnpj);
	                funcionario.setEmpregador(empregador);
	                funcionarios.add(funcionario);
	            }
	        }
	        return funcionarios;
	    }
	}
}
