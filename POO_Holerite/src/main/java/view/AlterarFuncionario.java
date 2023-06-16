package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.MaskFormatter;
import javax.swing.text.PlainDocument;

import controller.Controller;
import model.dao.FuncionarioDAO;
import model.dao.HoleriteDAO;
import model.entidade.Desconto;
import model.entidade.Empregador;
import model.entidade.Funcionario;
import model.entidade.Holerite;
import util.Validar;

public class AlterarFuncionario {

	private JFrame frameAlterar;

	public AlterarFuncionario(Empregador usuario, String cpfFunc) {
		initialize(usuario, cpfFunc);
	}

	private JRadioButton jovemRadio, carteiraAssRadio;
	private ButtonGroup empregadoGrupo, horaGrupo;
	private JTextField nomeCaixa;
	private JFormattedTextField cpfCaixa;
	private JFormattedTextField cboCaixa;
	private JTextField funcaoCaixa;
	private JTextArea mensagemCaixa;
	private JTextField salarioCaixa;
	private JLabel nomeLabel;
	private JLabel cpfLabel;
	private JLabel cadastroLabel;
	private JLabel dadosLabel;
	private JButton alterarButton, voltarButton, outroDescontoButton;
	private boolean cpfV, cboV;
	private double descontosFgts;
	private JFormattedTextField valorHoraCaixa;
	private JLabel valorHoraLabel;
	private JLabel astValorHora;
	private JLabel horasLabel;
	private JFormattedTextField horasCaixa;
	private JLabel asthoras;
	private JRadioButton naoHoraRadio, simHoraRadio;
	private JLabel simnaoLabel;
	private JPanel contentPanel;
	private List<Desconto> listaDescontos = new ArrayList<>();
	private JPanel painelDadosHolerite;
	private JPanel painelDescontos;
	private JPanel painelDadosFunc;
	private JTable table;
	private JButton excluirDesconto;
	private boolean desconV;
	private double[] inss = new double[3];
	private double irrf[] = new double[5];
	private double fgts[] = new double[3];

	private void initialize(Empregador usuario, String cpfFunc) {
		frameAlterar = new JFrame();
		frameAlterar.getContentPane().setFont(new Font("Dialog", Font.BOLD, 12));
		frameAlterar.setTitle("• Rabisco Holerite| Alterar Funcionario");
		frameAlterar.setIconImage(
				Toolkit.getDefaultToolkit().getImage(AlterarFuncionario.class.getResource("/imagem/rabisco.png")));
		frameAlterar.getContentPane().setBackground(Color.WHITE);
		frameAlterar.setBounds(100, 100, 615, 500);
		frameAlterar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameAlterar.getContentPane().setLayout(null);
		frameAlterar.setLocationRelativeTo(null);
		frameAlterar.setResizable(false);
		frameAlterar.setVisible(true);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new LineBorder(Color.WHITE));
		scrollPane.setBounds(0, 0, 600, 464);
		frameAlterar.getContentPane().add(scrollPane);

		contentPanel = new JPanel();
		contentPanel.setBorder(new LineBorder(new Color(255, 255, 255), 0));
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setLayout(null);
		contentPanel.setPreferredSize(new Dimension(0, 1060));
		scrollPane.setViewportView(contentPanel);

		voltarButton = new JButton();
		voltarButton.setBounds(-10, 10, 130, 25);
		voltarButton.setText("Voltar");
		voltarButton.setVisible(true);
		voltarButton.setOpaque(false);
		voltarButton.setContentAreaFilled(false);
		voltarButton.setBorderPainted(false);
		voltarButton.setForeground(Color.BLACK);
		voltarButton.setFont(new Font("Arial", Font.BOLD, 18));
		voltarButton.setIcon(new ImageIcon(AlterarFuncionario.class.getResource("/imagem/voltar.png")));
		voltarButton.addActionListener(e -> {
			frameAlterar.dispose();
			new MenuAdm(usuario);
		});
		contentPanel.add(voltarButton);

		cadastroLabel = new JLabel("Alterar de Funcionario");
		cadastroLabel.setHorizontalAlignment(SwingConstants.CENTER);
		cadastroLabel.setForeground(Color.BLACK);
		cadastroLabel.setBackground(Color.WHITE);
		cadastroLabel.setFont(new Font("Arial", Font.BOLD, 24));
		cadastroLabel.setBounds(133, 45, 348, 30);
		contentPanel.add(cadastroLabel);

		JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
		verticalScrollBar.setUnitIncrement(25);

		JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
		horizontalScrollBar.setUnitIncrement(25);

		horaGrupo = new javax.swing.ButtonGroup();

		MaskFormatter formatarCpf = null;
		try {
			formatarCpf = new MaskFormatter("#########-##");
		} catch (Exception e) {
			e.printStackTrace();
		}

		MaskFormatter formatarCbo = null;
		try {
			formatarCbo = new MaskFormatter("####-##");
		} catch (Exception e) {
			e.printStackTrace();
		}

		painelDadosFunc = new JPanel();
		painelDadosFunc.setBackground(Color.WHITE);
		painelDadosFunc.setBounds(64, 86, 450, 214);
		contentPanel.add(painelDadosFunc);
		painelDadosFunc.setLayout(null);

		JLabel astNome = new JLabel("*");
		astNome.setBounds(38, 48, 13, 17);
		painelDadosFunc.add(astNome);
		astNome.setToolTipText("");
		astNome.setOpaque(false);
		astNome.setHorizontalAlignment(SwingConstants.CENTER);
		astNome.setForeground(Color.RED);
		astNome.setBackground((Color) null);

		JLabel astCPF = new JLabel("*");
		astCPF.setBounds(38, 88, 13, 17);
		painelDadosFunc.add(astCPF);
		astCPF.setToolTipText("");
		astCPF.setOpaque(false);
		astCPF.setHorizontalAlignment(SwingConstants.CENTER);
		astCPF.setForeground(Color.RED);
		astCPF.setBackground((Color) null);

		JLabel astCBO = new JLabel("*");
		astCBO.setBounds(38, 128, 13, 17);
		painelDadosFunc.add(astCBO);
		astCBO.setToolTipText("");
		astCBO.setOpaque(false);
		astCBO.setHorizontalAlignment(SwingConstants.CENTER);
		astCBO.setForeground(Color.RED);
		astCBO.setBackground((Color) null);

		JLabel astFuncao = new JLabel("*");
		astFuncao.setBounds(38, 168, 13, 17);
		painelDadosFunc.add(astFuncao);
		astFuncao.setToolTipText("");
		astFuncao.setOpaque(false);
		astFuncao.setHorizontalAlignment(SwingConstants.CENTER);
		astFuncao.setForeground(Color.RED);
		astFuncao.setBackground((Color) null);

		JLabel cboLabel = new JLabel("CBO:");
		cboLabel.setBounds(51, 128, 100, 17);
		painelDadosFunc.add(cboLabel);
		cboLabel.setForeground(Color.BLACK);
		cboLabel.setHorizontalAlignment(SwingConstants.LEFT);
		cboLabel.setFont(new Font("Arial", Font.PLAIN, 14));

		JLabel funcaoLabel = new JLabel("Função:");
		funcaoLabel.setBounds(51, 168, 100, 17);
		painelDadosFunc.add(funcaoLabel);
		funcaoLabel.setForeground(Color.BLACK);
		funcaoLabel.setHorizontalAlignment(SwingConstants.LEFT);
		funcaoLabel.setFont(new Font("Arial", Font.PLAIN, 14));

		nomeCaixa = new JTextField();
		nomeCaixa.setBounds(159, 44, 240, 25);
		painelDadosFunc.add(nomeCaixa);
		nomeCaixa.setForeground(Color.BLACK);
		nomeCaixa.setBorder(BorderFactory.createLineBorder(Color.black));

		cpfCaixa = new JFormattedTextField();
		cpfCaixa.setBounds(159, 84, 240, 25);
		painelDadosFunc.add(cpfCaixa);
		cpfCaixa.setForeground(Color.BLACK);
		cpfCaixa.setBorder(BorderFactory.createLineBorder(Color.black));
		formatarCpf.install(cpfCaixa);

		nomeLabel = new JLabel("Nome:");
		nomeLabel.setBounds(51, 48, 100, 17);
		painelDadosFunc.add(nomeLabel);
		nomeLabel.setForeground(Color.BLACK);
		nomeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		nomeLabel.setFont(new Font("Arial", Font.PLAIN, 14));

		cpfLabel = new JLabel("CPF:");
		cpfLabel.setBounds(51, 88, 100, 17);
		painelDadosFunc.add(cpfLabel);
		cpfLabel.setForeground(Color.BLACK);
		cpfLabel.setHorizontalAlignment(SwingConstants.LEFT);
		cpfLabel.setFont(new Font("Arial", Font.PLAIN, 14));

		cboCaixa = new JFormattedTextField();
		cboCaixa.setBounds(159, 124, 240, 25);
		cboCaixa.setForeground(Color.BLACK);
		cboCaixa.setBorder(BorderFactory.createLineBorder(Color.black));
		painelDadosFunc.add(cboCaixa);

		cboCaixa.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				cboCaixa.setCaretPosition(0);
			}

			@Override
			public void focusLost(FocusEvent e) {
				try {
					String cbo = cboCaixa.getText();
					cbo = cbo.replaceAll("-", "");
					cboV = Validar.validarCBO(cbo);
					if (cboV) {
						cboCaixa.setBorder(BorderFactory.createLineBorder(Color.green));
						cboV = true;
					} else {
						cboCaixa.setBorder(BorderFactory.createLineBorder(Color.red));
						cboV = false;
					}
				} catch (Exception e1) {
					cboCaixa.setBorder(BorderFactory.createLineBorder(Color.red));
				}
			}
		});
		formatarCbo.install(cboCaixa);

		funcaoCaixa = new JTextField();
		funcaoCaixa.setBounds(159, 164, 240, 25);
		painelDadosFunc.add(funcaoCaixa);
		funcaoCaixa.setForeground(Color.BLACK);
		funcaoCaixa.setBorder(BorderFactory.createLineBorder(Color.black));

		dadosLabel = new JLabel("• Dados Funcionario:");
		dadosLabel.setBounds(16, 2, 228, 30);
		painelDadosFunc.add(dadosLabel);
		dadosLabel.setForeground(Color.BLACK);
		dadosLabel.setFont(new Font("Arial", Font.BOLD, 20));

		painelDadosHolerite = new JPanel();
		painelDadosHolerite.setBackground(Color.WHITE);
		painelDadosHolerite.setBounds(64, 311, 450, 343);
		contentPanel.add(painelDadosHolerite);
		painelDadosHolerite.setLayout(null);

		JLabel lblNewLabel = new JLabel("• Dados Holerite:");
		lblNewLabel.setBounds(16, 2, 228, 30);
		painelDadosHolerite.add(lblNewLabel);
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 20));

		JLabel salarioLabel = new JLabel("Salario Bruto:");
		salarioLabel.setBounds(51, 52, 100, 17);
		painelDadosHolerite.add(salarioLabel);
		salarioLabel.setForeground(Color.BLACK);
		salarioLabel.setFont(new Font("Arial", Font.PLAIN, 14));

		JLabel empregadoLabel = new JLabel("Empregado:");
		empregadoLabel.setBounds(51, 311, 100, 17);
		painelDadosHolerite.add(empregadoLabel);
		empregadoLabel.setHorizontalAlignment(SwingConstants.LEFT);
		empregadoLabel.setForeground(Color.BLACK);
		empregadoLabel.setFont(new Font("Arial", Font.PLAIN, 14));

		JLabel astEmpregado = new JLabel("*");
		astEmpregado.setBounds(38, 311, 13, 17);
		painelDadosHolerite.add(astEmpregado);
		astEmpregado.setToolTipText("");
		astEmpregado.setOpaque(false);
		astEmpregado.setHorizontalAlignment(SwingConstants.CENTER);
		astEmpregado.setForeground(Color.RED);
		astEmpregado.setBackground((Color) null);

		JLabel astSalario = new JLabel("*");
		astSalario.setBounds(38, 52, 13, 17);
		painelDadosHolerite.add(astSalario);
		astSalario.setToolTipText("");
		astSalario.setOpaque(false);
		astSalario.setHorizontalAlignment(SwingConstants.CENTER);
		astSalario.setForeground(Color.RED);
		astSalario.setBackground((Color) null);

		JLabel pagoLabel_1_1_1 = new JLabel("Mensagem:");
		pagoLabel_1_1_1.setBounds(51, 168, 100, 17);
		painelDadosHolerite.add(pagoLabel_1_1_1);
		pagoLabel_1_1_1.setForeground(Color.BLACK);
		pagoLabel_1_1_1.setFont(new Font("Arial", Font.PLAIN, 14));

		try {
			MaskFormatter formatarTelefone = new MaskFormatter("(##) ####-####");
			formatarTelefone.setPlaceholderCharacter(' ');
		} catch (ParseException e) {
			e.printStackTrace();
		}

		mensagemCaixa = new JTextArea();
		mensagemCaixa.setBounds(159, 168, 240, 125);
		painelDadosHolerite.add(mensagemCaixa);
		mensagemCaixa.setForeground(Color.BLACK);
		mensagemCaixa.setFont(new Font("Arial", Font.PLAIN, 12));
		mensagemCaixa.setBorder(BorderFactory.createLineBorder(Color.black));

		carteiraAssRadio = new JRadioButton("Carteira Assinada");
		carteiraAssRadio.setBounds(156, 308, 138, 23);
		painelDadosHolerite.add(carteiraAssRadio);
		carteiraAssRadio.setForeground(Color.BLACK);
		carteiraAssRadio.setFont(new Font("Dialog", Font.PLAIN, 12));
		carteiraAssRadio.setBackground(Color.WHITE);

		simHoraRadio = new JRadioButton("Sim");
		simHoraRadio.setBounds(292, 117, 51, 21);
		painelDadosHolerite.add(simHoraRadio);
		simHoraRadio.setForeground(Color.BLACK);
		simHoraRadio.setFont(new Font("Arial", Font.PLAIN, 10));
		simHoraRadio.setBackground(Color.WHITE);

		horaGrupo.add(simHoraRadio);

		naoHoraRadio = new JRadioButton("Não");
		naoHoraRadio.setBounds(345, 117, 51, 21);
		painelDadosHolerite.add(naoHoraRadio);
		naoHoraRadio.setForeground(Color.BLACK);
		naoHoraRadio.setFont(new Font("Arial", Font.PLAIN, 10));
		naoHoraRadio.setBackground(Color.WHITE);
		naoHoraRadio.setSelected(true);
		horaGrupo.add(naoHoraRadio);

		valorHoraCaixa = new JFormattedTextField();
		valorHoraCaixa.setBounds(159, 88, 115, 25);
		painelDadosHolerite.add(valorHoraCaixa);
		valorHoraCaixa.setForeground(Color.BLACK);
		valorHoraCaixa.setFont(new Font("Arial", Font.PLAIN, 12));
		valorHoraCaixa.setColumns(10);
		valorHoraCaixa.setEditable(false);
		valorHoraCaixa.setBorder(BorderFactory.createLineBorder(Color.black));

		valorHoraLabel = new JLabel("Valor hora:");
		valorHoraLabel.setBounds(51, 92, 100, 17);
		painelDadosHolerite.add(valorHoraLabel);
		valorHoraLabel.setForeground(Color.BLACK);
		valorHoraLabel.setFont(new Font("Arial", Font.PLAIN, 14));

		astValorHora = new JLabel("*");
		astValorHora.setBounds(38, 92, 13, 17);
		painelDadosHolerite.add(astValorHora);
		astValorHora.setToolTipText("");
		astValorHora.setOpaque(false);
		astValorHora.setHorizontalAlignment(SwingConstants.CENTER);
		astValorHora.setForeground(Color.RED);
		astValorHora.setBackground((Color) null);

		horasLabel = new JLabel("Horas:");
		horasLabel.setBounds(51, 132, 100, 17);
		painelDadosHolerite.add(horasLabel);
		horasLabel.setForeground(Color.BLACK);
		horasLabel.setFont(new Font("Arial", Font.PLAIN, 14));

		horasCaixa = new JFormattedTextField();
		horasCaixa.setBounds(159, 128, 114, 25);
		painelDadosHolerite.add(horasCaixa);
		horasCaixa.setForeground(Color.BLACK);
		horasCaixa.setFont(new Font("Arial", Font.PLAIN, 12));
		horasCaixa.setColumns(10);
		horasCaixa.setEditable(false);
		horasCaixa.setBorder(BorderFactory.createLineBorder(Color.black));

		asthoras = new JLabel("*");
		asthoras.setBounds(39, 132, 13, 17);
		painelDadosHolerite.add(asthoras);
		asthoras.setToolTipText("");
		asthoras.setOpaque(false);
		asthoras.setHorizontalAlignment(SwingConstants.CENTER);
		asthoras.setForeground(Color.RED);
		asthoras.setBackground((Color) null);

		simnaoLabel = new JLabel("Pago por hora:");
		simnaoLabel.setBounds(292, 96, 100, 13);
		painelDadosHolerite.add(simnaoLabel);
		simnaoLabel.setForeground(Color.BLACK);
		simnaoLabel.setFont(new Font("Arial", Font.BOLD, 11));

		jovemRadio = new JRadioButton("Jovem Aprendiz");
		jovemRadio.setBounds(296, 308, 127, 23);
		painelDadosHolerite.add(jovemRadio);
		jovemRadio.setForeground(Color.BLACK);
		jovemRadio.setFont(new Font("Dialog", Font.PLAIN, 12));
		jovemRadio.setBackground(Color.WHITE);

		salarioCaixa = new JTextField();
		salarioCaixa.setBounds(159, 48, 240, 25);
		painelDadosHolerite.add(salarioCaixa);
		salarioCaixa.setForeground(Color.BLACK);
		salarioCaixa.setFont(new Font("Arial", Font.PLAIN, 12));
		salarioCaixa.setColumns(10);
		salarioCaixa.setBorder(BorderFactory.createLineBorder(Color.black));

		painelDescontos = new JPanel();
		painelDescontos.setBackground(Color.WHITE);
		painelDescontos.setBounds(64, 665, 450, 300);
		contentPanel.add(painelDescontos);
		painelDescontos.setLayout(null);

		JLabel lblDescontos = new JLabel("• Descontos e Proventos:");
		lblDescontos.setBounds(16, 2, 316, 30);
		painelDescontos.add(lblDescontos);
		lblDescontos.setForeground(Color.BLACK);
		lblDescontos.setFont(new Font("Arial", Font.BOLD, 20));

		table = new JTable();
		table.setForeground(Color.BLACK);
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "C\u00F3digo", "Descri\u00E7\u00E3o", "Refer\u00EAncia", "Proventos", "Descontos" }));
		table.getColumnModel().getColumn(1).setPreferredWidth(107);

		DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
		headerRenderer.setBackground(Color.BLACK);
		headerRenderer.setForeground(Color.WHITE);
		headerRenderer.setFont(new Font("Arial", Font.BOLD, 12));
		for (int i = 0; i < table.getModel().getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
		}

		DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setForeground(Color.BLACK);
		table.setDefaultRenderer(Object.class, cellRenderer);

		JScrollPane scrollPaneDesconto = new JScrollPane(table);
		scrollPaneDesconto.setBounds(25, 43, 400, 190);
		painelDescontos.add(scrollPaneDesconto);

		salarioCaixa.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				try {
					String salario = salarioCaixa.getText();

					salarioCaixa.setText("R$ " + salario);

					salario = salario.replaceAll("\\D", "");
					double salarioBruto = Double.parseDouble(salario);
					if (salario != null || salarioBruto != 0) {

						double inssC[] = Controller.calcularINSS(salarioBruto);
						inss[0] = inssC[0];
						inss[1] = inssC[1];
						inss[2] = inssC[2];

						double irrfC[] = Controller.calcularIRRF(salarioBruto, inss[2]);
						irrf[0] = irrfC[0];
						irrf[1] = irrfC[1];
						irrf[2] = irrfC[2];
						irrf[3] = irrfC[3];

						String inssValor = ("R$ " + inss[2]);
						String irrfValor = ("R$ " + irrf[2]);
						String salarioValor = ("R$ " + salarioBruto);

						int salarioRowIndex = 0;
						int inssRowIndex = 1;
						int irrfRowIndex = 2;

						DefaultTableModel model = (DefaultTableModel) table.getModel();

						model.setValueAt("001", salarioRowIndex, 0);
						model.setValueAt("Salario Base", salarioRowIndex, 1);
						model.setValueAt("0", salarioRowIndex, 2);
						model.setValueAt(salarioValor, salarioRowIndex, 3);
						model.setValueAt("0", salarioRowIndex, 4);

						model.setValueAt("002", inssRowIndex, 0);
						model.setValueAt("INSS", inssRowIndex, 1);
						model.setValueAt(inss[0] + "%", inssRowIndex, 2);
						model.setValueAt("0", inssRowIndex, 3);
						model.setValueAt(inssValor, inssRowIndex, 4);

						model.setValueAt("003", irrfRowIndex, 0);
						model.setValueAt("IRRF", irrfRowIndex, 1);
						model.setValueAt(irrf[0] + "%", irrfRowIndex, 2);
						model.setValueAt("0", irrfRowIndex, 3);
						model.setValueAt(irrfValor, irrfRowIndex, 4);
					}
				} catch (Exception e1) {
				}
			}
		});

		empregadoGrupo = new javax.swing.ButtonGroup();

		jovemRadio.addActionListener(e -> {
			descontosFgts = 0.02;
			String salario = salarioCaixa.getText();
			if (!salario.isEmpty()) {
				salario = salario.replaceAll("\\D", "");
				double salarioBruto = Double.parseDouble(salario);
				double fgtsC[] = Controller.calcularFGTS(salarioBruto, descontosFgts);
				fgts[0] = fgtsC[0];
				fgts[1] = fgtsC[1];
				String fgtsForma = ("R$ " + fgts[1]);
				int fgtsRowIndex = 3;
				table.setValueAt("004", fgtsRowIndex, 0);
				table.setValueAt("FGTS", fgtsRowIndex, 1);
				table.setValueAt("2%", fgtsRowIndex, 2);
				table.setValueAt("0", fgtsRowIndex, 3);
				table.setValueAt(fgtsForma, fgtsRowIndex, 4);
			}
		});

		carteiraAssRadio.addActionListener(e -> {
			descontosFgts = 0.08;
			String salario = salarioCaixa.getText();
			if (!salario.isEmpty()) {
				salario = salario.replaceAll("\\D", "");
				double salarioBruto = Double.parseDouble(salario);
				double fgtsC[] = Controller.calcularFGTS(salarioBruto, descontosFgts);
				fgts[0] = fgtsC[0];
				fgts[1] = fgtsC[1];
				String fgtsForma = ("R$ " + fgts[1]);
				int fgtsRowIndex = 3;
				table.setValueAt("004", fgtsRowIndex, 0);
				table.setValueAt("FGTS", fgtsRowIndex, 1);
				table.setValueAt("8%", fgtsRowIndex, 2);
				table.setValueAt("0", fgtsRowIndex, 3);
				table.setValueAt(fgtsForma, fgtsRowIndex, 4);
			}
		});

		empregadoGrupo.add(carteiraAssRadio);
		empregadoGrupo.add(jovemRadio);

		horasCaixa.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				try {
					String valorM = valorHoraCaixa.getText();
					String horaM = horasCaixa.getText();
					if (!valorM.isEmpty() && !horaM.isEmpty()) {
						int valor = Integer.parseInt(valorM);
						int horas = Integer.parseInt(horaM);
						double total = valor * horas;
						String salario = Double.toString(total);

						salarioCaixa.setText("R$ " + salario);

						salario = salario.replaceAll("[R$\\s]+", "");

						double salarioBruto = Double.parseDouble(salario);
						if (salario != null || salarioBruto != 0) {

							double inssC[] = Controller.calcularINSS(salarioBruto);
							inss[0] = inssC[0];
							inss[1] = inssC[1];
							inss[2] = inssC[2];

							double irrfC[] = Controller.calcularIRRF(salarioBruto, inss[2]);
							irrf[0] = irrfC[0];
							irrf[1] = irrfC[1];
							irrf[2] = irrfC[2];
							irrf[3] = irrfC[3];

							String inssValor = ("R$ " + inss[2]);
							String irrfValor = ("R$ " + irrf[2]);

							int salarioRowIndex = 0;
							int inssRowIndex = 1;
							int irrfRowIndex = 2;

							DefaultTableModel model = (DefaultTableModel) table.getModel();

							model.setValueAt("001", salarioRowIndex, 0);
							model.setValueAt("Salario Base", salarioRowIndex, 1);
							model.setValueAt("0", salarioRowIndex, 2);
							model.setValueAt(salario, salarioRowIndex, 3);
							model.setValueAt("0", salarioRowIndex, 4);

							model.setValueAt("002", inssRowIndex, 0);
							model.setValueAt("INSS", inssRowIndex, 1);
							model.setValueAt(inss[0] + "%", inssRowIndex, 2);
							model.setValueAt("0", inssRowIndex, 3);
							model.setValueAt(inssValor, inssRowIndex, 4);

							model.setValueAt("003", irrfRowIndex, 0);
							model.setValueAt("IRRF", irrfRowIndex, 1);
							model.setValueAt(irrf[0] + "%", irrfRowIndex, 2);
							model.setValueAt("0", irrfRowIndex, 3);
							model.setValueAt(irrfValor, irrfRowIndex, 4);
						}
					}
				} catch (Exception e1) {
				}
			}
		});

		valorHoraCaixa.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				try {
					String valorM = valorHoraCaixa.getText();
					String horaM = horasCaixa.getText();
					if (!valorM.isEmpty() && !horaM.isEmpty()) {
						int valor = Integer.parseInt(valorM);
						int horas = Integer.parseInt(horaM);
						double total = valor * horas;
						String salario = Double.toString(total);

						salarioCaixa.setText("R$ " + salario);

						salario = salario.replaceAll("[R$\\s]+", "");

						double salarioBruto = Double.parseDouble(salario);
						if (salario != null || salarioBruto != 0) {

							double inssC[] = Controller.calcularINSS(salarioBruto);
							inss[0] = inssC[0];
							inss[1] = inssC[1];
							inss[2] = inssC[2];

							double irrfC[] = Controller.calcularIRRF(salarioBruto, inss[2]);
							irrf[0] = irrfC[0];
							irrf[1] = irrfC[1];
							irrf[2] = irrfC[2];
							irrf[3] = irrfC[3];

							String inssValor = ("R$ " + inss[2]);
							String irrfValor = ("R$ " + irrf[2]);

							int salarioRowIndex = 0;
							int inssRowIndex = 1;
							int irrfRowIndex = 2;

							DefaultTableModel model = (DefaultTableModel) table.getModel();

							model.setValueAt("001", salarioRowIndex, 0);
							model.setValueAt("Salario Base", salarioRowIndex, 1);
							model.setValueAt("0", salarioRowIndex, 2);
							model.setValueAt(salario, salarioRowIndex, 3);
							model.setValueAt("0", salarioRowIndex, 4);

							model.setValueAt("002", inssRowIndex, 0);
							model.setValueAt("INSS", inssRowIndex, 1);
							model.setValueAt(inss[0] + "%", inssRowIndex, 2);
							model.setValueAt("0", inssRowIndex, 3);
							model.setValueAt(inssValor, inssRowIndex, 4);

							model.setValueAt("003", irrfRowIndex, 0);
							model.setValueAt("IRRF", irrfRowIndex, 1);
							model.setValueAt(irrf[0] + "%", irrfRowIndex, 2);
							model.setValueAt("0", irrfRowIndex, 3);
							model.setValueAt(irrfValor, irrfRowIndex, 4);
						}
					}
				} catch (Exception e1) {
				}
			}
		});

		naoHoraRadio.addActionListener(e -> {
			salarioCaixa.setEditable(true);
			valorHoraCaixa.setText(null);
			valorHoraCaixa.setEditable(false);
			horasCaixa.setEditable(false);
			horasCaixa.setText(null);
			salarioCaixa.setText(null);
		});

		simHoraRadio.addActionListener(e -> {
			salarioCaixa.setEditable(false);
			valorHoraCaixa.setEditable(true);
			horasCaixa.setEditable(true);
		});

		cboCaixa.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				cboCaixa.setCaretPosition(0);
			}

			@Override
			public void focusLost(FocusEvent e) {
				try {
					String cbo = cboCaixa.getText();
					cbo = cbo.replaceAll("-", "");
					cboV = Validar.validarCBO(cbo);
					if (cboV) {
						cboCaixa.setBorder(BorderFactory.createLineBorder(Color.green));
					} else {
						cboCaixa.setBorder(BorderFactory.createLineBorder(Color.red));
					}
				} catch (Exception e1) {
					cboCaixa.setBorder(BorderFactory.createLineBorder(Color.red));
				}
			}
		});

		cpfCaixa.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				cpfCaixa.setCaretPosition(0);
			}

			@Override
			public void focusLost(FocusEvent e) {
				try {
					String cpf = cpfCaixa.getText();
					cpf = cpf.replaceAll("-", "");
					cpfV = Validar.validarCPF(cpf);
					if (cpfV) {
						cpfCaixa.setBorder(BorderFactory.createLineBorder(Color.green));
						cpfV = true;
					} else {
						cpfCaixa.setBorder(BorderFactory.createLineBorder(Color.red));
						cpfV = false;
					}
				} catch (Exception e1) {
					cpfCaixa.setBorder(BorderFactory.createLineBorder(Color.red));
				}
			}
		});

		outroDescontoButton = new JButton();
		outroDescontoButton.setBackground(Color.BLACK);
		outroDescontoButton.setBounds(18, 254, 125, 35);
		painelDescontos.add(outroDescontoButton);
		outroDescontoButton.setText("Novo Desc.");
		outroDescontoButton.setForeground(Color.WHITE);
		outroDescontoButton.setFont(new Font("Arial", Font.BOLD, 14));
		outroDescontoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				if (model.getRowCount() == 10) {
					outroDescontoButton.setEnabled(false);
				} else {
					JFrame frameAlterarDesconto = new JFrame();
					frameAlterarDesconto.getContentPane().setFont(new Font("Dialog", Font.BOLD, 12));
					frameAlterarDesconto.setTitle("• Rabisco Holerite| Cadastrar Funcionario");
					frameAlterarDesconto.setIconImage(Toolkit.getDefaultToolkit()
							.getImage(AlterarFuncionario.class.getResource("/imagem/rabisco.png")));
					frameAlterarDesconto.getContentPane().setBackground(Color.WHITE);
					frameAlterarDesconto.setBounds(100, 100, 400, 370);
					frameAlterarDesconto.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frameAlterarDesconto.getContentPane().setLayout(null);

					JTextField idCaixa = new JTextField();
					idCaixa.setForeground(Color.BLACK);
					idCaixa.setBounds(182, 77, 150, 25);
					idCaixa.setBorder(BorderFactory.createLineBorder(Color.black));
					frameAlterarDesconto.getContentPane().add(idCaixa);
					idCaixa.setColumns(10);

					JTextField nomeCaixaDes = new JTextField();
					nomeCaixaDes.setForeground(Color.BLACK);
					nomeCaixaDes.setColumns(10);
					nomeCaixaDes.setBorder(BorderFactory.createLineBorder(Color.black));
					nomeCaixaDes.setBounds(182, 113, 150, 25);
					frameAlterarDesconto.getContentPane().add(nomeCaixaDes);

					JTextField referenciaCaixa = new JTextField();
					referenciaCaixa.setForeground(Color.BLACK);
					referenciaCaixa.setColumns(10);
					referenciaCaixa.setBorder(BorderFactory.createLineBorder(Color.black));
					referenciaCaixa.setBounds(182, 149, 150, 25);
					frameAlterarDesconto.getContentPane().add(referenciaCaixa);

					JTextField vencimentoCaixa = new JTextField();
					vencimentoCaixa.setForeground(Color.BLACK);
					vencimentoCaixa.setColumns(10);
					vencimentoCaixa.setBorder(BorderFactory.createLineBorder(Color.black));
					vencimentoCaixa.setBounds(182, 185, 150, 25);
					frameAlterarDesconto.getContentPane().add(vencimentoCaixa);

					JTextField descontoCaixa = new JTextField();
					descontoCaixa.setForeground(Color.BLACK);
					descontoCaixa.setColumns(10);
					descontoCaixa.setBorder(BorderFactory.createLineBorder(Color.black));
					descontoCaixa.setBounds(182, 221, 150, 25);
					frameAlterarDesconto.getContentPane().add(descontoCaixa);

					JButton salvarButton = new JButton("Salvar");
					salvarButton.setFont(new Font("Arial", Font.BOLD, 14));
					salvarButton.setBackground(Color.BLACK);
					salvarButton.setForeground(Color.WHITE);
					salvarButton.setBounds(55, 271, 125, 35);
					frameAlterarDesconto.getContentPane().add(salvarButton);
					salvarButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							String id = idCaixa.getText();
							String nome = nomeCaixaDes.getText();
							String referencia = referenciaCaixa.getText();
							String vencimento = vencimentoCaixa.getText();
							String desconto = descontoCaixa.getText();

							if (id.isEmpty() || nome.isEmpty() || referencia.isEmpty() || vencimento.isEmpty()
									|| desconto.isEmpty()) {
								caixaOk("Por favor, preencha todos os campos.");
							} else {
								DefaultTableModel model = (DefaultTableModel) table.getModel();
								model.addRow(new Object[] { id, nome, referencia, vencimento, desconto });
								try {
									frameAlterarDesconto.dispose();
								} catch (NumberFormatException ex) {
									caixaOk("Por favor, insira valores numéricos válidos para referência, vencimento e desconto.");
								}
							}
						}
					});

					JButton cancelarButton = new JButton("Cancelar");
					cancelarButton.setForeground(Color.WHITE);
					cancelarButton.setFont(new Font("Arial", Font.BOLD, 14));
					cancelarButton.setBackground(Color.BLACK);
					cancelarButton.setBounds(207, 271, 125, 35);
					frameAlterarDesconto.getContentPane().add(cancelarButton);
					cancelarButton.addActionListener(c -> {
						frameAlterarDesconto.dispose();
					});

					JLabel lblNewLabel = new JLabel("ID:");
					lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 12));
					lblNewLabel.setBounds(55, 77, 117, 25);
					frameAlterarDesconto.getContentPane().add(lblNewLabel);

					JLabel lblNome = new JLabel("Nome:");
					lblNome.setFont(new Font("Arial", Font.PLAIN, 12));
					lblNome.setBounds(55, 113, 117, 25);
					frameAlterarDesconto.getContentPane().add(lblNome);

					JLabel lblReferncia = new JLabel("Referência:");
					lblReferncia.setFont(new Font("Arial", Font.PLAIN, 12));
					lblReferncia.setBounds(55, 149, 117, 25);
					frameAlterarDesconto.getContentPane().add(lblReferncia);

					JLabel lblVencimento = new JLabel("Vencimento:");
					lblVencimento.setFont(new Font("Arial", Font.PLAIN, 12));
					lblVencimento.setBounds(55, 185, 117, 25);
					frameAlterarDesconto.getContentPane().add(lblVencimento);

					JLabel lblDesconto = new JLabel("Desconto:");
					lblDesconto.setFont(new Font("Arial", Font.PLAIN, 12));
					lblDesconto.setBounds(55, 221, 117, 25);
					frameAlterarDesconto.getContentPane().add(lblDesconto);

					JLabel lblNovoDesconto = new JLabel("• Novo desconto:");
					lblNovoDesconto.setFont(new Font("Arial", Font.BOLD, 20));
					lblNovoDesconto.setBounds(35, 30, 181, 25);
					frameAlterarDesconto.getContentPane().add(lblNovoDesconto);

					DocumentFilter filter = new DocumentFilter() {
						@Override
						public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
								throws BadLocationException {
							StringBuilder builder = new StringBuilder(text.length());
							for (int i = 0; i < text.length(); i++) {
								char ch = text.charAt(i);
								if (Character.isDigit(ch) || ch == '.' || ch == ',') {
									builder.append(ch);
								}
							}
							super.insertString(fb, offset, builder.toString(), attr);
						}

						@Override
						public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
								throws BadLocationException {
							if (text == null) {
								super.replace(fb, offset, length, text, attrs);
								return;
							}

							StringBuilder builder = new StringBuilder(text.length());
							for (int i = 0; i < text.length(); i++) {
								char ch = text.charAt(i);
								if (Character.isDigit(ch) || ch == '.' || ch == ',') {
									builder.append(ch);
								}
							}
							super.replace(fb, offset, length, builder.toString(), attrs);
						}
					};

					referenciaCaixa.setDocument(new PlainDocument());
					((AbstractDocument) referenciaCaixa.getDocument()).setDocumentFilter(filter);

					vencimentoCaixa.setDocument(new PlainDocument());
					((AbstractDocument) vencimentoCaixa.getDocument()).setDocumentFilter(filter);

					descontoCaixa.setDocument(new PlainDocument());
					((AbstractDocument) descontoCaixa.getDocument()).setDocumentFilter(filter);

					frameAlterarDesconto.setLocationRelativeTo(null);
					frameAlterarDesconto.setResizable(false);
					frameAlterarDesconto.setVisible(true);
				}
			}
		});
		painelDescontos.add(outroDescontoButton);

		excluirDesconto = new JButton();
		excluirDesconto.setText("Excluir");
		excluirDesconto.setForeground(Color.WHITE);
		excluirDesconto.setFont(new Font("Arial", Font.BOLD, 14));
		excluirDesconto.setBackground(Color.BLACK);
		excluirDesconto.setBounds(304, 254, 125, 35);
		excluirDesconto.addActionListener(v -> {
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
				int linhaSelecionada = table.getSelectedRow();
				if (linhaSelecionada != -1) {
					if (linhaSelecionada >= 4) {
						for (Desconto desconto : listaDescontos) {
							System.out.println(desconto);
						}
						DefaultTableModel model = (DefaultTableModel) table.getModel();
						model.removeRow(linhaSelecionada);
						caixaDialogo.dispose();
					} else {
						caixaOk("Você as primeiras 4 linhas!.");
						caixaDialogo.dispose();
					}
				} else {
					caixaOk("Por favor, selecione uma linha para excluir.");
					caixaDialogo.dispose();
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
		painelDescontos.add(excluirDesconto);

		alterarButton = new JButton();
		alterarButton.setText("Alterar Desc.");
		alterarButton.setForeground(Color.WHITE);
		alterarButton.setFont(new Font("Arial", Font.BOLD, 14));
		alterarButton.setBackground(Color.BLACK);
		alterarButton.setBounds(161, 254, 125, 35);
		alterarButton.addActionListener(e -> {
			int linhaSelecionada = table.getSelectedRow();
			if (linhaSelecionada != -1) {
				if (linhaSelecionada >= 4) {
					String id = (String) table.getValueAt(linhaSelecionada, 0);
					String nome = (String) table.getValueAt(linhaSelecionada, 1);
					String referencia = (String) table.getValueAt(linhaSelecionada, 2);
					String vencimento = (String) table.getValueAt(linhaSelecionada, 3);
					String desconto = (String) table.getValueAt(linhaSelecionada, 4);

					JFrame framAlterar = new JFrame();
					framAlterar.getContentPane().setFont(new Font("Dialog", Font.BOLD, 12));
					framAlterar.setTitle("• Rabisco Holerite| Cadastrar Funcionario");
					framAlterar.setIconImage(Toolkit.getDefaultToolkit()
							.getImage(AlterarFuncionario.class.getResource("/imagem/rabisco.png")));
					framAlterar.getContentPane().setBackground(Color.WHITE);
					framAlterar.setBounds(100, 100, 400, 370);
					framAlterar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					framAlterar.getContentPane().setLayout(null);

					JTextField idCaixa = new JTextField();
					idCaixa.setForeground(Color.BLACK);
					idCaixa.setBounds(182, 77, 150, 25);
					idCaixa.setBorder(BorderFactory.createLineBorder(Color.black));
					framAlterar.getContentPane().add(idCaixa);
					idCaixa.setColumns(10);

					JTextField nomeCaixaDes = new JTextField();
					nomeCaixaDes.setForeground(Color.BLACK);
					nomeCaixaDes.setColumns(10);
					nomeCaixaDes.setBorder(BorderFactory.createLineBorder(Color.black));
					nomeCaixaDes.setBounds(182, 113, 150, 25);
					framAlterar.getContentPane().add(nomeCaixaDes);

					JTextField referenciaCaixa = new JTextField();
					referenciaCaixa.setForeground(Color.BLACK);
					referenciaCaixa.setColumns(10);
					referenciaCaixa.setBorder(BorderFactory.createLineBorder(Color.black));
					referenciaCaixa.setBounds(182, 149, 150, 25);
					framAlterar.getContentPane().add(referenciaCaixa);

					JTextField vencimentoCaixa = new JTextField();
					vencimentoCaixa.setForeground(Color.BLACK);
					vencimentoCaixa.setColumns(10);
					vencimentoCaixa.setBorder(BorderFactory.createLineBorder(Color.black));
					vencimentoCaixa.setBounds(182, 185, 150, 25);
					framAlterar.getContentPane().add(vencimentoCaixa);

					JTextField descontoCaixa = new JTextField();
					descontoCaixa.setForeground(Color.BLACK);
					descontoCaixa.setColumns(10);
					descontoCaixa.setBorder(BorderFactory.createLineBorder(Color.black));
					descontoCaixa.setBounds(182, 221, 150, 25);
					framAlterar.getContentPane().add(descontoCaixa);

					idCaixa.setText(id);
					nomeCaixaDes.setText(nome);
					referenciaCaixa.setText(referencia);
					vencimentoCaixa.setText(vencimento);
					descontoCaixa.setText(desconto);

					JButton salvarButton = new JButton("Salvar");
					salvarButton.setFont(new Font("Arial", Font.BOLD, 14));
					salvarButton.setBackground(Color.BLACK);
					salvarButton.setForeground(Color.WHITE);
					salvarButton.setBounds(55, 271, 125, 35);
					framAlterar.getContentPane().add(salvarButton);
					salvarButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							String id = idCaixa.getText();
							String nome = nomeCaixaDes.getText();
							String referencia = referenciaCaixa.getText();
							String vencimento = vencimentoCaixa.getText();
							String desconto = descontoCaixa.getText();
							if (id.isEmpty() || nome.isEmpty() || referencia.isEmpty() || vencimento.isEmpty()
									|| desconto.isEmpty()) {
								caixaOk("Por favor, preencha todos os campos.");
							} else {
								DefaultTableModel model = (DefaultTableModel) table.getModel();
								int linhaSelecionada = table.getSelectedRow();

								try {
									model.setValueAt(id, linhaSelecionada, 0);
									model.setValueAt(nome, linhaSelecionada, 1);
									model.setValueAt(referencia, linhaSelecionada, 2);
									model.setValueAt(vencimento, linhaSelecionada, 3);
									model.setValueAt(desconto, linhaSelecionada, 4);
									caixaOk("Registro alterado com sucesso.");
									framAlterar.dispose();
								} catch (NumberFormatException ex) {
									caixaOk("Por favor, insira valores numéricos válidos para referência, vencimento e desconto.");
								}
							}
						}
					});

					JButton cancelarButton = new JButton("Cancelar");
					cancelarButton.setForeground(Color.WHITE);
					cancelarButton.setFont(new Font("Arial", Font.BOLD, 14));
					cancelarButton.setBackground(Color.BLACK);
					cancelarButton.setBounds(207, 271, 125, 35);
					framAlterar.getContentPane().add(cancelarButton);
					cancelarButton.addActionListener(c -> {
						framAlterar.dispose();
					});

					JLabel idLabel = new JLabel("ID:");
					idLabel.setFont(new Font("Arial", Font.PLAIN, 12));
					idLabel.setBounds(55, 77, 117, 25);
					framAlterar.getContentPane().add(idLabel);

					JLabel lblNome = new JLabel("Nome:");
					lblNome.setFont(new Font("Arial", Font.PLAIN, 12));
					lblNome.setBounds(55, 113, 117, 25);
					framAlterar.getContentPane().add(lblNome);

					JLabel lblReferncia = new JLabel("Referência:");
					lblReferncia.setFont(new Font("Arial", Font.PLAIN, 12));
					lblReferncia.setBounds(55, 149, 117, 25);
					framAlterar.getContentPane().add(lblReferncia);

					JLabel lblVencimento = new JLabel("Vencimento:");
					lblVencimento.setFont(new Font("Arial", Font.PLAIN, 12));
					lblVencimento.setBounds(55, 185, 117, 25);
					framAlterar.getContentPane().add(lblVencimento);

					JLabel lblDesconto = new JLabel("Desconto:");
					lblDesconto.setFont(new Font("Arial", Font.PLAIN, 12));
					lblDesconto.setBounds(55, 221, 117, 25);
					framAlterar.getContentPane().add(lblDesconto);

					JLabel lblNovoDesconto = new JLabel("• Alterar desconto:");
					lblNovoDesconto.setFont(new Font("Arial", Font.BOLD, 20));
					lblNovoDesconto.setBounds(35, 30, 181, 25);
					framAlterar.getContentPane().add(lblNovoDesconto);

					DocumentFilter filter = new DocumentFilter() {
						@Override
						public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
								throws BadLocationException {
							StringBuilder builder = new StringBuilder(text.length());
							for (int i = 0; i < text.length(); i++) {
								char ch = text.charAt(i);
								if (Character.isDigit(ch) || ch == '.' || ch == ',') {
									builder.append(ch);
								}
							}
							super.insertString(fb, offset, builder.toString(), attr);
						}

						@Override
						public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
								throws BadLocationException {
							if (text == null) {
								super.replace(fb, offset, length, text, attrs);
								return;
							}

							StringBuilder builder = new StringBuilder(text.length());
							for (int i = 0; i < text.length(); i++) {
								char ch = text.charAt(i);
								if (Character.isDigit(ch) || ch == '.' || ch == ',') {
									builder.append(ch);
								}
							}
							super.replace(fb, offset, length, builder.toString(), attrs);
						}
					};

					referenciaCaixa.setDocument(new PlainDocument());
					((AbstractDocument) referenciaCaixa.getDocument()).setDocumentFilter(filter);

					vencimentoCaixa.setDocument(new PlainDocument());
					((AbstractDocument) vencimentoCaixa.getDocument()).setDocumentFilter(filter);

					descontoCaixa.setDocument(new PlainDocument());
					((AbstractDocument) descontoCaixa.getDocument()).setDocumentFilter(filter);

					framAlterar.setLocationRelativeTo(null);
					framAlterar.setResizable(false);
					framAlterar.setVisible(true);
				} else {
					caixaOk("Você não pode alterar as 4 primeiras linhas!.");
				}
			} else {
				caixaOk("Por favor, selecione uma linha para alterar.");
			}
		});
		painelDescontos.add(alterarButton);

		alterarButton = new JButton("Alterar");
		alterarButton.setBounds(189, 985, 200, 45);
		alterarButton.setFont(new Font("Arial", Font.BOLD, 14));
		alterarButton.setForeground(Color.WHITE);
		alterarButton.setBackground(Color.BLACK);
		alterarButton.addActionListener(e -> {
			String nome = nomeCaixa.getText();
			String cpf = cpfCaixa.getText();
			cpf = cpf.replaceAll("\\D", "");
			String mensagem = mensagemCaixa.getText();
			String funcao = funcaoCaixa.getText();
			String cbo = cboCaixa.getText();
			cbo = cbo.replaceAll("\\D", "");
			String salario = salarioCaixa.getText();
			salario = salario.replaceAll("\\D", "");
			cpfV = Validar.validarCPF(cpf);
			cboV = Validar.validarCBO(cbo);
			if (nome.isEmpty() || cpf.isEmpty() || cbo.isEmpty() || funcao.isEmpty() || salario.isEmpty()
					|| empregadoGrupo.getSelection() == null || horaGrupo.getSelection() == null) {
				caixaOk("A dados a serem preenchidos!");
			} else {
				if (cpfV == false || cboV == false) {
					if (cpfV == false) {
						caixaOk("O CPF é inválido!");
					}
					if (cboV == false) {
						caixaOk("O CBO é inválido!!");
					}
				} else {

					DefaultTableModel model = (DefaultTableModel) table.getModel();
					int rowCount = model.getRowCount();

					for (int i = 0; i < rowCount; i++) {
						String codigo = (String) model.getValueAt(i, 0);
						String descricao = (String) model.getValueAt(i, 1);
						String referenciaS = (String) model.getValueAt(i, 2);
						String proventosS = (String) model.getValueAt(i, 3);
						String descontosS = (String) model.getValueAt(i, 4);

						String refeS = referenciaS.replaceAll("[R$,%\\s]", "").replace(",", ".");
						String proveS = proventosS.replaceAll("[R$,%\\s]", "").replace(",", ".");
						String descoS = descontosS.replaceAll("[R$,%\\s]", "").replace(",", ".");

						Double referencia = Double.parseDouble(refeS);
						Double proventos = Double.parseDouble(proveS);
						Double descontos = Double.parseDouble(descoS);

						if (codigo.equals("004")) {
							proventos = 0.0;
							descontos = 0.0;
						}

						Desconto desconto = new Desconto(codigo, descricao, referencia, proventos, descontos);
						listaDescontos.add(desconto);
					}
				}

				Double salarioDouble = Double.parseDouble(salario);

				double inssC[] = Controller.calcularINSS(salarioDouble);
				inss[0] = inssC[0];
				inss[1] = inssC[1];
				inss[2] = inssC[2];

				double irrfC[] = Controller.calcularIRRF(salarioDouble, inss[2]);
				irrf[0] = irrfC[0];
				irrf[1] = irrfC[1];
				irrf[2] = irrfC[2];
				irrf[3] = irrfC[3];

				double totDesconto = 0.0;
				double totVencimento = 0.0;
				double salarioLiquido = 0.0;

				double baseIrrf = irrf[3];
				double faixaIrrf = irrf[1];

				for (Desconto desconto : listaDescontos) {
					totDesconto += desconto.getDescontos();
				}

				for (Desconto vencimento : listaDescontos) {
					totVencimento += vencimento.getProventos();
				}

				Funcionario funcionario = new Funcionario(cpf, nome, cbo, funcao, usuario);

				salarioLiquido = totVencimento - totDesconto;

				try {
					FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
					funcionarioDAO.atualizarFuncionario(funcionario, usuario);
					HoleriteDAO holeriteDAO = new HoleriteDAO();
					Holerite holeriteB = holeriteDAO.obterHoleritePorCpf(cpf);
					Holerite holerite = new Holerite(holeriteB.getId(), listaDescontos, mensagem, totVencimento,
							totDesconto, salarioLiquido, salarioDouble, salarioDouble, baseIrrf, salarioDouble,
							faixaIrrf, funcionario);
					holeriteDAO.atualizarHolerite(holerite, funcionario.getCpf());
					new CAE_Holerite(usuario);
					frameAlterar.dispose();
				} catch (SQLException y) {
					y.printStackTrace();
				}
			}
		});
		contentPanel.add(alterarButton);

		FuncionarioDAO funcionarioDao = new FuncionarioDAO();
		HoleriteDAO holeriteDao = new HoleriteDAO();
		Funcionario func = null;
		try {
			func = funcionarioDao.buscarFuncionarioPorCpf(cpfFunc, usuario);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		Holerite hole = holeriteDao.obterHoleritePorCpf(cpfFunc);

		String salarioString = String.valueOf(hole.getSalarioBase());
		int posicaoVirgula = salarioString.indexOf(".");
		if (posicaoVirgula != -1) {
			salarioString = salarioString.substring(0, posicaoVirgula);
		}

		nomeCaixa.setText(func.getNome());
		cpfCaixa.setText(func.getCpf());
		cpfCaixa.setEditable(false);
		cboCaixa.setText(func.getCbo());
		funcaoCaixa.setText(func.getFuncao());
		mensagemCaixa.setText(hole.getMensagem());
		salarioCaixa.setText(salarioString);
		atualizarTabela(cpfFunc);
	}

	public static void caixaOk(String texto) {
		JFrame caixaDialogo = new JFrame();
		caixaDialogo.setIconImage(
				Toolkit.getDefaultToolkit().getImage(AlterarFuncionario.class.getResource("/imagem/java.png")));
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

	private void atualizarTabela(String cpf) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		List<Desconto> listaDescontos = new ArrayList<>();
		Connection conexao = null;
		try {
			HoleriteDAO holeriteDao = new HoleriteDAO();
			Holerite hole = holeriteDao.obterHoleritePorCpf(cpf);
			List<Desconto> descontos = hole.getDesconto();
			for (int i = 0; i < descontos.size(); i++) {
				Desconto descontoC = descontos.get(i);

				String codigo = descontoC.getCod();
				String descricao = descontoC.getDescricao();
				String referencia = String.valueOf(descontoC.getReferencia());
				String proventos = String.valueOf(descontoC.getProventos());
				String descontoS = String.valueOf(descontoC.getDescontos());

				NumberFormat formatadorMoeda = NumberFormat.getCurrencyInstance();
				formatadorMoeda.setMinimumFractionDigits(2);

				Object[] rowData = { codigo, descricao, referencia, proventos, descontoS };
				model.addRow(rowData);

			}
		} catch (Exception e) {
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

}
