package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import model.dao.FuncionarioDAO;
import model.entidade.Empregador;
import model.entidade.Funcionario;

public class PesquisarFuncionario {

	private JFrame frame;
	private JTextField cpfCaixa;

	public PesquisarFuncionario(Empregador usuario) {
		initialize(usuario);
	}
	
	private void initialize(Empregador usuario) {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(PesquisarFuncionario.class.getResource("/imagem/rabisco.png")));
		frame.setTitle("• Rabisco Holerite | Pesquisar Funcionario");
		frame.getContentPane().setBackground(Color.WHITE);
		frame.getContentPane().setLayout(null);
		frame.setSize(500, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		cpfCaixa = new JTextField();
		cpfCaixa.setForeground(Color.BLACK);
		cpfCaixa.setBounds(207, 125, 200, 25);
		frame.getContentPane().add(cpfCaixa);
		cpfCaixa.setColumns(10);
		cpfCaixa.addActionListener(e -> {
		    String cpf = cpfCaixa.getText();
		    try {
		        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
		        Funcionario funcionario = funcionarioDAO.buscarFuncionarioPorCpf(cpf, usuario);
		        if (funcionario != null) {
		            new MenuBusca(usuario, funcionario);
		        } else {
		            caixaOk("Funcionario não encontrado!");
		        }
		    } catch (SQLException ex) {
		    	caixaOk("Erro no banco de dados!");
		    }
		});
		
		JLabel cpfLabel = new JLabel("CPF Funcionario:");
		cpfLabel.setForeground(Color.BLACK);
		cpfLabel.setFont(new Font("Arial", Font.BOLD, 14));
		cpfLabel.setBounds(67, 128, 130, 17);
		frame.getContentPane().add(cpfLabel);
		
		JButton buscarButton = new JButton("Buscar");
		buscarButton.setForeground(Color.WHITE);
		buscarButton.setBackground(Color.BLACK);
		buscarButton.setFont(new Font("Arial", Font.BOLD, 14));
		buscarButton.setBounds(125, 179, 250, 50);
		frame.getContentPane().add(buscarButton);
		
		JLabel pesqFuncLabel = new JLabel("• Pesquisa de Funcionario:");
		pesqFuncLabel.setForeground(Color.BLACK);
		pesqFuncLabel.setFont(new Font("Arial", Font.BOLD, 20));
		pesqFuncLabel.setBounds(67, 59, 350, 24);
		frame.getContentPane().add(pesqFuncLabel);
		
		JButton voltarButton = new JButton();
		voltarButton.setBounds(-10, 10, 130, 25);
		voltarButton.setText("Voltar");
		voltarButton.setVisible(true);
		voltarButton.setOpaque(false);
        voltarButton.setContentAreaFilled(false);
        voltarButton.setBorderPainted(false);
        voltarButton.setForeground(Color.black);
        voltarButton.setFont(new Font("Arial", Font.BOLD, 18));
        ImageIcon voltarIcon = new ImageIcon(CAE_Holerite.class.getResource("/imagem/voltar.png"));
        voltarButton.setIcon(voltarIcon);
        voltarButton.addActionListener(e ->{
				frame.dispose();
				new MenuAdm(usuario);
        });
        frame.getContentPane().add(voltarButton);
		
		frame.setVisible(true);
	}
	public static void caixaOk(String texto) {
		JFrame caixaDialogo = new JFrame();
		caixaDialogo
				.setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/imagem/java.png")));
		caixaDialogo.getContentPane().setBackground(Color.WHITE);
		caixaDialogo.setBounds(100, 100, 400, 200);
		caixaDialogo.setLocationRelativeTo(null);
		caixaDialogo.setVisible(true);
		caixaDialogo.setResizable(false);
		caixaDialogo.setTitle("• Rabisco Holerite | Caixa de Dialogo");
		caixaDialogo.getContentPane().setLayout(null);

		JButton okButton = new JButton("OK");
		okButton.setBackground(Color.BLACK);
		okButton.setForeground(Color.WHITE);
		okButton.setFont(new Font("Arial", Font.BOLD, 12));
		okButton.setBounds(125, 95, 150, 40);
		okButton.addActionListener(c -> {
			caixaDialogo.dispose();
		});
		caixaDialogo.getContentPane().add(okButton);

		JLabel mensagemLabel1 = new JLabel();
		mensagemLabel1.setText(texto);
		mensagemLabel1.setFont(new Font("Arial", Font.BOLD, 16));
		mensagemLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		mensagemLabel1.setForeground(new Color(0, 0, 0));
		mensagemLabel1.setBounds(50, 28, 300, 50);
		caixaDialogo.getContentPane().add(mensagemLabel1);
	}
}
