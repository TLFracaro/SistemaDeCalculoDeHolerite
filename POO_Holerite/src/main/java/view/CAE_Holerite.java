package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableModel;

import model.dao.FuncionarioDAO;
import model.entidade.Empregador;
import model.entidade.Funcionario;

public class CAE_Holerite {
	private JFrame frame, frameBusca;
	private JTable table;
	private String selecionaCpf;
	private Empregador usuarioLogado;

	public CAE_Holerite(Empregador usuario) {
		usuarioLogado = usuario;
		initialize(usuario);
	}

	private void initialize(Empregador usuario) {
		frame = new JFrame();
		frame.setTitle("• Rabisco Holerite | CAE Holerite");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(CAE_Holerite.class.getResource("/imagem/rabisco.png")));
		frame.setBackground(Color.WHITE);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.getContentPane().setLayout(null);
		frame.setBounds(100, 100, 800, 600);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

		JButton cadatroButton = new JButton();
		cadatroButton.setText("Cadastrar");
		cadatroButton.setBounds(36, 471, 150, 50);
		cadatroButton.setOpaque(true);
		cadatroButton.setBackground(Color.black);
		cadatroButton.setForeground(Color.white);
		cadatroButton.setFont(new Font("Arial", Font.BOLD, 17));
		frame.getContentPane().add(cadatroButton);
		cadatroButton.addActionListener(e -> {
			frame.dispose();
			new CadastroFuncionario(usuario);
		});

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
		voltarButton.addActionListener(e -> {
			frame.dispose();
			new MenuAdm(usuario);
		});
		frame.getContentPane().add(voltarButton);

		JButton jb1 = new JButton();
		jb1.setText("Imprimir");
		jb1.setBounds(60, 540, 200, 50);
		jb1.setContentAreaFilled(false);
		jb1.setFocusPainted(false);
		jb1.setOpaque(true);
		cadatroButton.setBackground(Color.black);
		cadatroButton.setForeground(Color.white);
		cadatroButton.setFont(new Font("Arial", Font.BOLD, 17));

		JButton excluirButton = new JButton();
		excluirButton.setText("Excluir");
		excluirButton.setOpaque(true);
		excluirButton.setForeground(Color.WHITE);
		excluirButton.setFont(new Font("Arial", Font.BOLD, 17));
		excluirButton.setBackground(Color.BLACK);
		excluirButton.setBounds(222, 471, 150, 50);
		excluirButton.addActionListener(e -> {
			if (selecionaCpf != null) {
				JFrame caixaDialogo = new JFrame();
				caixaDialogo.setIconImage(
						Toolkit.getDefaultToolkit().getImage(CAE_Holerite.class.getResource("/imagem/java.png")));
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
						funcionarioDAO.excluirFuncionario(selecionaCpf, usuarioLogado);
						caixaDialogo.dispose();
						caixaOk("Funcionário excluído!");
						atualizarTabela();
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
			}
		});
		frame.getContentPane().add(excluirButton);

		JButton alterarButton = new JButton();
		alterarButton.setText("Alterar");
		alterarButton.setOpaque(true);
		alterarButton.setForeground(Color.WHITE);
		alterarButton.setFont(new Font("Arial", Font.BOLD, 17));
		alterarButton.setBackground(Color.BLACK);
		alterarButton.setBounds(408, 471, 150, 50);
		alterarButton.addActionListener(e -> {
			new AlterarFuncionario(usuario, selecionaCpf);
			frame.dispose();
		});
		frame.getContentPane().add(alterarButton);
		frame.getContentPane().add(cadatroButton);

		JButton buscarButton = new JButton();
		buscarButton.setText("Buscar");
		buscarButton.setOpaque(true);
		buscarButton.setForeground(Color.WHITE);
		buscarButton.setFont(new Font("Arial", Font.BOLD, 17));
		buscarButton.setBackground(Color.BLACK);
		buscarButton.setBounds(594, 471, 150, 50);
		buscarButton.addActionListener(e -> {
			frameBusca = new JFrame();
			frameBusca.setIconImage(
					Toolkit.getDefaultToolkit().getImage(CAE_Holerite.class.getResource("/imagem/rabisco.png")));
			frameBusca.setTitle("• Rabisco Holerite | Pesquisar Funcionario");
			frameBusca.getContentPane().setBackground(Color.WHITE);
			frameBusca.getContentPane().setLayout(null);
			frameBusca.setVisible(true);
			frameBusca.setSize(500, 300);
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
					Funcionario funcionario = funcionarioDAO.buscarFuncionarioPorCpf(cpf, usuarioLogado);
					if(funcionario != null) {
						new MenuBusca(usuario, funcionario);
					frame.dispose();
					frameBusca.dispose();
					} else {
						caixaOk("Funcionario não encontrado!");
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
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
				new MenuAdm(usuario);
			});
			frameBusca.getContentPane().add(voltarButtonBusca);
		});
		frame.getContentPane().add(buscarButton);

		table = new JTable();
		table.setSurrendersFocusOnKeystroke(true);
		table.setBackground(Color.WHITE);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "CPF", "Nome", "CBO", "Função" }));
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(125);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(150);
		table.setFont(new Font("Arial", Font.PLAIN, 12));

		LineBorder blackBorder = new LineBorder(Color.BLACK);
		table.setBorder(blackBorder);
		table.getTableHeader().setBorder(blackBorder);
		table.getTableHeader().setBackground(Color.BLACK);
		table.getTableHeader().setForeground(Color.WHITE);
		table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(117, 55, 550, 375);

		JScrollBar scrollBar = new JScrollBar(JScrollBar.VERTICAL);
		scrollBar.setForeground(Color.WHITE);
		scrollBar.setUnitIncrement(100);
		scrollBar.setBackground(Color.WHITE);
		scrollBar.setUI(new BasicScrollBarUI() {
			@Override
			protected void configureScrollBarColors() {
				this.thumbColor = Color.BLACK;
			}
		});
		scrollPane.setVerticalScrollBar(scrollBar);

		frame.getContentPane().add(scrollPane);

		scrollBar.setUI(new BasicScrollBarUI() {
			@Override
			protected void configureScrollBarColors() {
				this.thumbColor = Color.BLACK;
			}

			@Override
			protected JButton createDecreaseButton(int orientation) {
				return createZeroButton();
			}

			@Override
			protected JButton createIncreaseButton(int orientation) {
				return createZeroButton();
			}

			private JButton createZeroButton() {
				JButton button = new JButton();
				Dimension zeroDim = new Dimension(0, 0);
				button.setPreferredSize(zeroDim);
				button.setMinimumSize(zeroDim);
				button.setMaximumSize(zeroDim);
				return button;
			}
		});

		scrollPane.setVerticalScrollBar(scrollBar);

		table.getSelectionModel().addListSelectionListener(e -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow != -1) {
				selecionaCpf = table.getValueAt(selectedRow, 0).toString();
			} else {
				selecionaCpf = null;
			}
		});
		frame.setVisible(true);
		atualizarTabela();
	}

	private void atualizarTabela() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		List<Funcionario> funcionarios;
		Connection conexao = null;
		try {
			FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
			funcionarios = funcionarioDAO.listarFuncionarios(usuarioLogado);
			for (Funcionario funcionario : funcionarios) {
				String cpf = funcionario.getCpf();
				String nome = funcionario.getNome();
				String cbo = funcionario.getCbo();
				String funcao = funcionario.getFuncao();
				Object[] rowData = { cpf, nome, cbo, funcao };
				model.addRow(rowData);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			caixaOk("Ocorreu um erro ao atualizar a tabela.");
		} finally {
			if (conexao != null) {
				try {
					conexao.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void caixaOk(String texto) {
		JFrame caixaDialogo = new JFrame();
		caixaDialogo.setIconImage(Toolkit.getDefaultToolkit().getImage(Cadastro.class.getResource("/imagem/java.png")));
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
