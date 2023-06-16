package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import model.dao.FuncionarioDAO;
import model.entidade.Empregador;
import model.entidade.Funcionario;

public class MenuBusca {

    private FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

    public MenuBusca(Empregador usuario, Funcionario funcionario) {
        initialize(usuario, funcionario);
    }

    private void initialize(Empregador usuario, Funcionario funcionario) {
    	try {
			if (funcionario != null) {
    	JFrame frameBuscou = new JFrame();
		frameBuscou.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(PesquisarFuncionario.class.getResource("/imagem/rabisco.png")));
		frameBuscou.setTitle("• Rabisco Holerite | Menu Busca");
		frameBuscou.getContentPane().setBackground(Color.WHITE);
		frameBuscou.getContentPane().setLayout(null);
		frameBuscou.setSize(500, 380);
		frameBuscou.setVisible(true);
		frameBuscou.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameBuscou.setLocationRelativeTo(null);

		JLabel nomeLabel = new JLabel("Menu do funcionário " + funcionario.getNome() + "!");
		nomeLabel.setOpaque(false);
		nomeLabel.setBounds(45, 45, 427, 35);
		nomeLabel.setForeground(Color.BLACK);
		nomeLabel.setFont(new Font("Arial", Font.BOLD, 19));
		nomeLabel.setVisible(true);
		frameBuscou.getContentPane().add(nomeLabel);

		JButton alterarButtonBuscou = new JButton();
		alterarButtonBuscou.setText("Alterar Funcionário/Holerite");
		alterarButtonBuscou.setBounds(75, 105, 350, 50);
		alterarButtonBuscou.setContentAreaFilled(false);
		alterarButtonBuscou.setFocusPainted(false);
		alterarButtonBuscou.setOpaque(true);
		alterarButtonBuscou.setBackground(Color.BLACK);
		alterarButtonBuscou.setForeground(Color.WHITE);
		alterarButtonBuscou.setFont(new Font("Arial", Font.BOLD, 17));
		alterarButtonBuscou.addActionListener(t -> {
			frameBuscou.dispose();
			new AlterarFuncionario(usuario, funcionario.getCpf());
		});
		frameBuscou.getContentPane().add(alterarButtonBuscou);

		JButton excluirButtonBuscou = new JButton();
		excluirButtonBuscou.setText("Excluir Funcionário");
		excluirButtonBuscou.setBounds(75, 180, 350, 50);
		excluirButtonBuscou.setContentAreaFilled(false);
		excluirButtonBuscou.setFocusPainted(false);
		excluirButtonBuscou.setOpaque(true);
		excluirButtonBuscou.setBackground(Color.BLACK);
		excluirButtonBuscou.setForeground(Color.WHITE);
		excluirButtonBuscou.setFont(new Font("Arial", Font.BOLD, 17));
		excluirButtonBuscou.addActionListener(p -> {
			JFrame caixaDialogo = new JFrame();
			caixaDialogo.setIconImage(Toolkit.getDefaultToolkit()
					.getImage(CAE_Holerite.class.getResource("/imagem/java.png")));
			caixaDialogo.getContentPane().setBackground(Color.WHITE);
			caixaDialogo.setBounds(100, 100, 400, 200);
			caixaDialogo.setLocationRelativeTo(null);
			caixaDialogo.setVisible(true);
			caixaDialogo.setResizable(false);
			caixaDialogo.setTitle("• Rabisco Holerite | Caixa de Dialogo");
			caixaDialogo.getContentPane().setLayout(null);

			JButton confirmaButton = new JButton("Confirmar");
			confirmaButton.setBackground(Color.BLACK);
			confirmaButton.setForeground(Color.WHITE);
			confirmaButton.setFont(new Font("Arial", Font.BOLD, 12));
			confirmaButton.setBounds(40, 95, 150, 40);
			caixaDialogo.getContentPane().add(confirmaButton);
			confirmaButton.addActionListener(a -> {
				try {
					funcionarioDAO.excluirFuncionario(funcionario.getCpf(), usuario);
					caixaDialogo.dispose();
					frameBuscou.dispose();
					new CAE_Holerite(usuario);
				} catch (SQLException ex) {
					ex.printStackTrace();
					caixaDialogo.dispose();
					caixaOk("Ocorreu um erro ao excluir o funcionário.");
				}
			});

			JLabel mensagemLabel = new JLabel("Deseja prosseguir com a ação?");
			mensagemLabel.setFont(new Font("Arial", Font.BOLD, 16));
			mensagemLabel.setHorizontalAlignment(SwingConstants.CENTER);
			mensagemLabel.setForeground(new Color(0, 0, 0));
			mensagemLabel.setBounds(50, 28, 300, 50);
			caixaDialogo.getContentPane().add(mensagemLabel);

			JButton cancelarButton = new JButton("Cancelar");
			cancelarButton.setForeground(Color.WHITE);
			cancelarButton.setFont(new Font("Arial", Font.BOLD, 12));
			cancelarButton.setBackground(Color.BLACK);
			cancelarButton.setBounds(201, 95, 150, 40);
			cancelarButton.addActionListener(z -> {
				caixaDialogo.dispose();
			});
			caixaDialogo.getContentPane().add(cancelarButton);
		});
		frameBuscou.getContentPane().add(excluirButtonBuscou);

		JButton verHoleritesButton = new JButton();
		verHoleritesButton.setText("Ver Holerites");
		verHoleritesButton.setBounds(75, 255, 350, 50);
		verHoleritesButton.setContentAreaFilled(false);
		verHoleritesButton.setFocusPainted(false);
		verHoleritesButton.setOpaque(true);
		verHoleritesButton.setBackground(Color.BLACK);
		verHoleritesButton.setForeground(Color.WHITE);
		verHoleritesButton.setFont(new Font("Arial", Font.BOLD, 17));
		verHoleritesButton.addActionListener(v -> {
			frameBuscou.dispose();
			new ConsultaHolerite(usuario, funcionario);
		});
		frameBuscou.getContentPane().add(verHoleritesButton);

		JButton voltarButtonBuscou = new JButton();
		voltarButtonBuscou.setBounds(-10, 10, 130, 25);
		voltarButtonBuscou.setText("Voltar");
		voltarButtonBuscou.setVisible(true);
		voltarButtonBuscou.setOpaque(false);
		voltarButtonBuscou.setContentAreaFilled(false);
		voltarButtonBuscou.setBorderPainted(false);
		voltarButtonBuscou.setForeground(Color.BLACK);
		voltarButtonBuscou.setFont(new Font("Arial", Font.BOLD, 18));
		voltarButtonBuscou
				.setIcon(new ImageIcon(CadastroFuncionario.class.getResource("/imagem/voltar.png")));
		voltarButtonBuscou.addActionListener(c -> {
			frameBuscou.dispose();
			new CAE_Holerite(usuario);
		});
		frameBuscou.getContentPane().add(voltarButtonBuscou);
			} else {
				caixaOk("Funcionário não encontrado!");
			}
    	} catch (Exception ex) {
	ex.printStackTrace();
	caixaOk("Ocorreu um erro ao buscar o funcionário.");
	}
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
