package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import model.dao.FuncionarioDAO;
import model.entidade.Empregador;
import model.entidade.Funcionario;

public class MenuAdm {
	
	JFrame frame;
	JButton consultHolerite, cadastrarFunc, pesquisarFunc, caeHolerite,histoHolerite, botaoSair, botaoSuporte;
	JLabel bemVindo, labelHora;
	
	public MenuAdm(Empregador usuario) {
		initialize(usuario);
	}

	private void initialize(Empregador usuario) {
		
		frame = new JFrame();
		frame.setTitle(" • Rabisco Holerite | Menu Funcionario");
		frame.setSize(600, 380);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(Color.white);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		bemVindo = criarTexto("Bem vindo(a) "+ usuario.getRazaoSocial() +" !", 35, 25, 400, 35);
		botaoSair = criarBotaoSair(525, 10);
		pesquisarFunc = new JButton("Pesquisar Holerite");
		pesquisarFunc.setBounds(92, 105, 400, 50);
		pesquisarFunc.setContentAreaFilled(false);
		pesquisarFunc.setFocusPainted(false);
		pesquisarFunc.setOpaque(true);
		pesquisarFunc.setBackground(Color.black);
		pesquisarFunc.setForeground(Color.white);
		pesquisarFunc.setFont(new Font("Arial", Font.BOLD, 17));
		pesquisarFunc.addActionListener(e -> {
			frame.dispose();
			JFrame frameBusca = new JFrame();
			frameBusca.setIconImage(
					Toolkit.getDefaultToolkit().getImage(CAE_Holerite.class.getResource("/imagem/rabisco.png")));
			frameBusca.setTitle("• Rabisco Holerite | Pesquisar Funcionario");
			frameBusca.getContentPane().setBackground(Color.WHITE);
			frameBusca.getContentPane().setLayout(null);
			frameBusca.setVisible(true);
			frameBusca.setSize(500, 300);
			frameBusca.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frameBusca.setLocationRelativeTo(null);

			JTextField cpfCaixa = new JTextField();
			cpfCaixa.setForeground(Color.BLACK);
			cpfCaixa.setBounds(207, 125, 200, 25);
			frameBusca.getContentPane().add(cpfCaixa);
			cpfCaixa.setColumns(10);

			JLabel cpfLabel = new JLabel("CPF Funcionario:");
			cpfLabel.setForeground(Color.BLACK);
			cpfLabel.setFont(new Font("Arial", Font.BOLD, 14));
			cpfLabel.setBounds(67, 128, 130, 17);
			frameBusca.getContentPane().add(cpfLabel);

			JButton buscarButtonBusca = new JButton("Buscar");
			buscarButtonBusca.setForeground(Color.WHITE);
			buscarButtonBusca.setBackground(Color.BLACK);
			buscarButtonBusca.setFont(new Font("Arial", Font.BOLD, 14));
			buscarButtonBusca.setBounds(125, 179, 250, 50);
			buscarButtonBusca.addActionListener(q -> {
				String cpf = cpfCaixa.getText();
				try {
					FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
					Funcionario funcionario = funcionarioDAO.buscarFuncionarioPorCpf(cpf, usuario);
					if (funcionario != null) {
						frame.dispose();
						frameBusca.dispose();
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
							new AlterarFuncionario(usuario, cpf);
							frame.dispose();
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
									funcionarioDAO.excluirFuncionario(cpf, usuario);
									caixaDialogo.dispose();
									caixaOk("Funcionário excluído!");
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
							new ConsultaHolerite(usuario, funcionario);
							frameBuscou.dispose();
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
				} catch (SQLException ex) {
					ex.printStackTrace();
					caixaOk("Ocorreu um erro ao buscar o funcionário.");
				}
			});
			frameBusca.getContentPane().add(buscarButtonBusca);

			JLabel pesqFuncLabel = new JLabel("• Pesquisa de Funcionario:");
			pesqFuncLabel.setForeground(Color.BLACK);
			pesqFuncLabel.setFont(new Font("Arial", Font.BOLD, 20));
			pesqFuncLabel.setBounds(67, 59, 350, 24);
			frameBusca.getContentPane().add(pesqFuncLabel);

			JButton voltarButtonBusca = new JButton();
			voltarButtonBusca.setBounds(-10, 10, 130, 25);
			voltarButtonBusca.setText("Voltar");
			voltarButtonBusca.setVisible(true);
			voltarButtonBusca.setOpaque(false);
			voltarButtonBusca.setContentAreaFilled(false);
			voltarButtonBusca.setBorderPainted(false);
			voltarButtonBusca.setForeground(Color.black);
			voltarButtonBusca.setFont(new Font("Arial", Font.BOLD, 18));
			ImageIcon voltarIconBusca = new ImageIcon(CAE_Holerite.class.getResource("/imagem/voltar.png"));
			voltarButtonBusca.setIcon(voltarIconBusca);
			voltarButtonBusca.addActionListener(a -> {
				frame.dispose();
				frameBusca.dispose();
				new MenuAdm(usuario);
			});
			frameBusca.getContentPane().add(voltarButtonBusca);
		});
		
		caeHolerite = new JButton("CAE Holerite");
		caeHolerite.setBounds(92, 205, 400, 50);
		caeHolerite.setContentAreaFilled(false);
		caeHolerite.setFocusPainted(false);
		caeHolerite.setOpaque(true);
		caeHolerite.setBackground(Color.black);
		caeHolerite.setForeground(Color.white);
		caeHolerite.setFont(new Font("Arial", Font.BOLD, 17));
		labelHora = iniciarRelogio(35, 615, 250, 35);
		
		frame.getContentPane().add(botaoSair);
		frame.getContentPane().add(bemVindo);
		frame.getContentPane().add(labelHora);
		frame.getContentPane().add(pesquisarFunc);
		frame.getContentPane().add(caeHolerite);
		
		caeHolerite.addActionListener(e -> {
			frame.dispose();
			new CAE_Holerite(usuario);
		});
		
		botaoSair.addActionListener(e -> {
			frame.dispose();
			new Login();
		});
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(MenuAdm.class.getResource("/imagem/rabisco.png")));
	}
	
	public JButton criarBotaoSair(int esq, int topo) {
		JButton jb = new JButton();
        jb.setLocation(esq, topo);
        jb.setSize(50,50);
        jb.setVisible(true);
        jb.setOpaque(false);
        jb.setContentAreaFilled(false);
        jb.setBorderPainted(false);
        jb.setIcon(new ImageIcon(MenuAdm.class.getResource("/imagem/sair.png")));
        return jb;
	}
	
	public JButton criarBotao(String texto, int esq, int topo, int larg, int alt) {
		JButton jb = new JButton(texto);
		jb.setBounds(esq, topo, larg, alt);
		jb.setContentAreaFilled(false);
		jb.setFocusPainted(false);
		jb.setOpaque(true);
		jb.setBackground(Color.black);
		jb.setForeground(Color.white);
		jb.setFont(new Font("Arial", Font.BOLD, 17));
		return jb;
	}
	
	public JLabel iniciarRelogio(int esq, int topo, int larg, int alt) {
	    labelHora = new JLabel();
	    labelHora.setFont(new Font("Arial", Font.BOLD, 15));
	    labelHora.setForeground(Color.BLACK);
	    labelHora.setBounds(35, 295, larg, alt);
	    
	    Date horaAtual = new Date();
	    labelHora.setText(horaAtual.toString());

	    Timer timer = new Timer(1000, new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            Date horaAtual = new Date();
	            labelHora.setText(horaAtual.toString());
	        }
	    });
	    timer.start();
	    return labelHora;
	}
	
	public JLabel criarTexto(String texto, int esq, int topo, int larg, int alt) {
		JLabel jl = new JLabel(texto);
		jl.setOpaque(false);
		jl.setBounds(esq, topo, larg, alt);
		jl.setForeground(Color.black);
		jl.setFont(new Font("Arial", Font.BOLD, 19));
		jl.setVisible(true);
		return jl;
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

