package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.text.MaskFormatter;

import controller.Controller;
import model.dao.EmpregadorDAO;
import model.dao.EnderecoDAO;
import model.entidade.Empregador;
import model.entidade.Endereco;
import util.Validar;

public class Cadastro {

	private JFrame frame;
	private JButton cadastrarButton;
	private JTextField emailCaixa;
	private JTextField senhaCaixa;
	private JFormattedTextField cepCaixa;
	private JTextField logradouroCaixa;
	private JTextField bairroCaixa;
	private JTextField cidadeCaixa;
	private JTextField ufCaixa;
	private JTextField numeroCaixa;
	private JTextField paisCaixa;
	private JTextField complementoCaixa;
	private JTextField confirmaSenhaCaixa;
	private JTextField confirmaEmailCaixa;
	private JFormattedTextField cnpjCaixa;
	private JTextField razaoSocialCaixa;
	private JPanel painelDadosCadastro, painelEnredecoEmpresa, contentPanel;
	private JLayeredPane painelGeral;
	private JScrollPane scrollPane;
	private Endereco endereco;
	private Controller controller = new Controller();
	private boolean cnpjV, cepV, emailV;
	private boolean cnpjBd;

	public Cadastro() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setTitle("• Rabisco Holerite | Cadastro");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Cadastro.class.getResource("/imagem/cadeado.png")));
		frame.setBackground(Color.WHITE);
		frame.setVisible(true);
		frame.setBounds(100, 100, 615, 500);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new LineBorder(Color.WHITE));
		scrollPane.setBounds(0, 0, 600, 464);
		frame.getContentPane().add(scrollPane);

		contentPanel = new JPanel();
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setLayout(null);
		contentPanel.setPreferredSize(new Dimension(0, 775));
		scrollPane.setViewportView(contentPanel);

		painelGeral = new JLayeredPane();
		painelGeral.setLayout(null);
		painelGeral.setBackground(Color.WHITE);
		painelGeral.setBounds(98, 46, 400, 709);
		contentPanel.add(painelGeral);

		painelDadosCadastro = new JPanel();
		painelDadosCadastro.setLayout(null);
		painelDadosCadastro.setForeground(Color.BLACK);
		painelDadosCadastro.setBorder(new LineBorder(Color.WHITE));
		painelDadosCadastro.setBackground(Color.WHITE);
		painelDadosCadastro.setBounds(7, 4, 386, 271);
		painelGeral.add(painelDadosCadastro);

		JLabel emailLabel = new JLabel("Email:");
		emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		emailLabel.setForeground(Color.BLACK);
		emailLabel.setBounds(39, 131, 100, 17);
		painelDadosCadastro.add(emailLabel);

		JLabel senhaLabel = new JLabel("Senha:");
		senhaLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		senhaLabel.setForeground(Color.BLACK);
		senhaLabel.setBounds(39, 205, 100, 17);
		painelDadosCadastro.add(senhaLabel);

		emailCaixa = new JTextField();
		emailCaixa.setFont(new Font("Arial", Font.PLAIN, 11));
		emailCaixa.setForeground(Color.BLACK);
		emailCaixa.setColumns(10);
		emailCaixa.setBounds(144, 127, 240, 25);
		emailCaixa.setBorder(BorderFactory.createLineBorder(Color.black));
		painelDadosCadastro.add(emailCaixa);

		emailCaixa.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				try {
					String email = emailCaixa.getText();
					emailV = Validar.validaremail(email);
					if (emailV) {
						emailCaixa.setBorder(BorderFactory.createLineBorder(Color.green));
					} else {
						emailCaixa.setBorder(BorderFactory.createLineBorder(Color.red));
					}
				} catch (Exception e1) {
					emailCaixa.setBorder(BorderFactory.createLineBorder(Color.red));
				}
			}
		});

		confirmaEmailCaixa = new JTextField();
		confirmaEmailCaixa.setFont(new Font("Arial", Font.PLAIN, 11));
		confirmaEmailCaixa.setForeground(Color.BLACK);
		confirmaEmailCaixa.setColumns(10);
		confirmaEmailCaixa.setBorder(BorderFactory.createLineBorder(Color.black));
		confirmaEmailCaixa.setBounds(144, 164, 240, 25);
		painelDadosCadastro.add(confirmaEmailCaixa);

		confirmaEmailCaixa.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				try {
					String email = confirmaEmailCaixa.getText();
					emailV = Validar.validaremail(email);
					if (emailV && confirmaEmailCaixa.getText().equals(emailCaixa.getText())) {
						confirmaEmailCaixa.setBorder(BorderFactory.createLineBorder(Color.green));
					} else {
						confirmaEmailCaixa.setBorder(BorderFactory.createLineBorder(Color.red));
					}
				} catch (Exception e1) {
					confirmaEmailCaixa.setBorder(BorderFactory.createLineBorder(Color.red));
				}
			}
		});

		senhaCaixa = new JTextField();
		senhaCaixa.setFont(new Font("Arial", Font.PLAIN, 11));
		senhaCaixa.setForeground(Color.BLACK);
		senhaCaixa.setColumns(10);
		senhaCaixa.setBounds(144, 201, 240, 25);
		senhaCaixa.setBorder(BorderFactory.createLineBorder(Color.black));
		painelDadosCadastro.add(senhaCaixa);

		JLabel astEmail = new JLabel("*");
		astEmail.setToolTipText("");
		astEmail.setOpaque(false);
		astEmail.setHorizontalAlignment(SwingConstants.CENTER);
		astEmail.setForeground(Color.RED);
		astEmail.setBackground((Color) null);
		astEmail.setBounds(26, 132, 13, 14);
		painelDadosCadastro.add(astEmail);

		JLabel astSenha = new JLabel("*");
		astSenha.setToolTipText("");
		astSenha.setOpaque(false);
		astSenha.setHorizontalAlignment(SwingConstants.CENTER);
		astSenha.setForeground(Color.RED);
		astSenha.setBackground((Color) null);
		astSenha.setBounds(26, 206, 13, 14);
		painelDadosCadastro.add(astSenha);

		JLabel confirmaSenhaLabel = new JLabel("Confirma Senha:");
		confirmaSenhaLabel.setForeground(Color.BLACK);
		confirmaSenhaLabel.setHorizontalAlignment(SwingConstants.LEFT);
		confirmaSenhaLabel.setFont(new Font("Arial", Font.PLAIN, 13));
		confirmaSenhaLabel.setBounds(39, 242, 100, 17);
		painelDadosCadastro.add(confirmaSenhaLabel);

		confirmaSenhaCaixa = new JTextField();
		confirmaSenhaCaixa.setFont(new Font("Arial", Font.PLAIN, 11));
		confirmaSenhaCaixa.setForeground(Color.BLACK);
		confirmaSenhaCaixa.setColumns(10);
		confirmaSenhaCaixa.setBounds(144, 238, 240, 25);
		confirmaSenhaCaixa.setBorder(BorderFactory.createLineBorder(Color.black));
		painelDadosCadastro.add(confirmaSenhaCaixa);

		JLabel astConfirmaSenha = new JLabel("*");
		astConfirmaSenha.setToolTipText("");
		astConfirmaSenha.setOpaque(false);
		astConfirmaSenha.setHorizontalAlignment(SwingConstants.CENTER);
		astConfirmaSenha.setForeground(Color.RED);
		astConfirmaSenha.setBackground((Color) null);
		astConfirmaSenha.setBounds(26, 243, 13, 14);
		painelDadosCadastro.add(astConfirmaSenha);

		JLabel confirmaEmailLabel = new JLabel("Confirma Email:");
		confirmaEmailLabel.setForeground(Color.BLACK);
		confirmaEmailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		confirmaEmailLabel.setBounds(39, 168, 100, 17);
		painelDadosCadastro.add(confirmaEmailLabel);

		JLabel astConfirmaEmail = new JLabel("*");
		astConfirmaEmail.setToolTipText("");
		astConfirmaEmail.setOpaque(false);
		astConfirmaEmail.setHorizontalAlignment(SwingConstants.CENTER);
		astConfirmaEmail.setForeground(Color.RED);
		astConfirmaEmail.setBackground((Color) null);
		astConfirmaEmail.setBounds(26, 169, 13, 14);
		painelDadosCadastro.add(astConfirmaEmail);

		JLabel dadosEmpresaLabel = new JLabel("• Dados para Cadastro:");
		dadosEmpresaLabel.setBounds(10, 10, 251, 30);
		painelDadosCadastro.add(dadosEmpresaLabel);
		dadosEmpresaLabel.setFont(new Font("Arial", Font.BOLD, 20));
		dadosEmpresaLabel.setForeground(Color.BLACK);

		JLabel cnpjEmpreLabel = new JLabel("CNPJ:");
		cnpjEmpreLabel.setBounds(39, 57, 100, 17);
		painelDadosCadastro.add(cnpjEmpreLabel);
		cnpjEmpreLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		cnpjEmpreLabel.setForeground(Color.BLACK);

		JLabel razaoSocialEmpreLabel = new JLabel("Razão Social:");
		razaoSocialEmpreLabel.setBounds(39, 94, 100, 17);
		painelDadosCadastro.add(razaoSocialEmpreLabel);
		razaoSocialEmpreLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		razaoSocialEmpreLabel.setForeground(Color.BLACK);
		
		MaskFormatter formatarCnpj = null;
		try {
			formatarCnpj = new MaskFormatter("##.###.###/####-##");
			formatarCnpj.setPlaceholderCharacter(' ');
			formatarCnpj.install(cnpjCaixa);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		cnpjCaixa = new JFormattedTextField();
		cnpjCaixa.setBounds(144, 53, 240, 25);
		painelDadosCadastro.add(cnpjCaixa);
		cnpjCaixa.setFont(new Font("Arial", Font.PLAIN, 11));
		cnpjCaixa.setForeground(Color.BLACK);
		cnpjCaixa.setColumns(10);
		cnpjCaixa.setBorder(BorderFactory.createLineBorder(Color.black));
		formatarCnpj.install(cnpjCaixa);

		cnpjCaixa.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				cnpjCaixa.setCaretPosition(0);
			}

			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				try {
					String cnpj = cnpjCaixa.getText();
					cnpjV = Validar.validarCNPJ(cnpj);
					if (cnpjV) {
						cnpjCaixa.setBorder(BorderFactory.createLineBorder(Color.green));
					} else {
						cnpjCaixa.setBorder(BorderFactory.createLineBorder(Color.red));
					}
				} catch (Exception e1) {
					cnpjCaixa.setBorder(BorderFactory.createLineBorder(Color.red));
				}
			}
		});
		
		razaoSocialCaixa = new JTextField();
		razaoSocialCaixa.setBounds(144, 90, 240, 25);
		painelDadosCadastro.add(razaoSocialCaixa);
		razaoSocialCaixa.setFont(new Font("Arial", Font.PLAIN, 11));
		razaoSocialCaixa.setForeground(Color.BLACK);
		razaoSocialCaixa.setColumns(10);
		razaoSocialCaixa.setBorder(BorderFactory.createLineBorder(Color.black));

		JLabel astCpnj = new JLabel("*");
		astCpnj.setBounds(26, 58, 13, 14);
		painelDadosCadastro.add(astCpnj);
		astCpnj.setToolTipText("");
		astCpnj.setOpaque(false);
		astCpnj.setHorizontalAlignment(SwingConstants.CENTER);
		astCpnj.setForeground(Color.RED);
		astCpnj.setBackground((Color) null);

		JLabel astRazaoSocial = new JLabel("*");
		astRazaoSocial.setBounds(26, 95, 13, 14);
		painelDadosCadastro.add(astRazaoSocial);
		astRazaoSocial.setToolTipText("");
		astRazaoSocial.setOpaque(false);
		astRazaoSocial.setHorizontalAlignment(SwingConstants.CENTER);
		astRazaoSocial.setForeground(Color.RED);
		astRazaoSocial.setBackground((Color) null);

		cnpjCaixa.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				cnpjCaixa.setCaretPosition(0);
			}

			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				try {
					String cnpj = cnpjCaixa.getText();
					cnpjV = Validar.validarCNPJ(cnpj);
					if (cnpjV) {
						cnpjCaixa.setBorder(BorderFactory.createLineBorder(Color.green));
					} else {
						cnpjCaixa.setBorder(BorderFactory.createLineBorder(Color.red));
					}
				} catch (Exception e1) {
					cnpjCaixa.setBorder(BorderFactory.createLineBorder(Color.red));
				}
			}
		});

		painelEnredecoEmpresa = new JPanel();
		painelEnredecoEmpresa.setLayout(null);
		painelEnredecoEmpresa.setBorder(new LineBorder(Color.WHITE));
		painelEnredecoEmpresa.setBackground(Color.WHITE);
		painelEnredecoEmpresa.setBounds(7, 286, 386, 346);
		painelGeral.add(painelEnredecoEmpresa);

		JLabel cepLabel = new JLabel("CEP:");
		cepLabel.setHorizontalAlignment(SwingConstants.LEFT);
		cepLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		cepLabel.setForeground(Color.BLACK);
		cepLabel.setBounds(31, 57, 100, 17);
		painelEnredecoEmpresa.add(cepLabel);

		JLabel lograLabel = new JLabel("Logradouro:");
		lograLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lograLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		lograLabel.setForeground(Color.BLACK);
		lograLabel.setBounds(31, 94, 100, 17);
		painelEnredecoEmpresa.add(lograLabel);

		JLabel bairroLabel = new JLabel("Bairro:");
		bairroLabel.setHorizontalAlignment(SwingConstants.LEFT);
		bairroLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		bairroLabel.setForeground(Color.BLACK);
		bairroLabel.setBounds(31, 131, 100, 17);
		painelEnredecoEmpresa.add(bairroLabel);

		JLabel cidadeLabel = new JLabel("Cidade:");
		cidadeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		cidadeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		cidadeLabel.setForeground(Color.BLACK);
		cidadeLabel.setBounds(31, 168, 100, 17);
		painelEnredecoEmpresa.add(cidadeLabel);

		JLabel ufLabel = new JLabel("UF:");
		ufLabel.setHorizontalAlignment(SwingConstants.LEFT);
		ufLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		ufLabel.setForeground(Color.BLACK);
		ufLabel.setBounds(31, 205, 100, 17);
		painelEnredecoEmpresa.add(ufLabel);

		JLabel numeroLabel = new JLabel("Número:");
		numeroLabel.setHorizontalAlignment(SwingConstants.LEFT);
		numeroLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		numeroLabel.setForeground(Color.BLACK);
		numeroLabel.setBounds(31, 279, 100, 17);
		painelEnredecoEmpresa.add(numeroLabel);

		JLabel paisLabel = new JLabel("País:");
		paisLabel.setHorizontalAlignment(SwingConstants.LEFT);
		paisLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		paisLabel.setForeground(Color.BLACK);
		paisLabel.setBounds(31, 242, 100, 17);
		painelEnredecoEmpresa.add(paisLabel);

		JLabel enderecoEmpreLabel = new JLabel("• Endereço de Cobrança Empresa:");
		enderecoEmpreLabel.setFont(new Font("Arial", Font.BOLD, 20));
		enderecoEmpreLabel.setForeground(Color.BLACK);
		enderecoEmpreLabel.setBounds(10, 10, 325, 24);
		painelEnredecoEmpresa.add(enderecoEmpreLabel);

		cepCaixa = new JFormattedTextField();
		cepCaixa.setFont(new Font("Arial", Font.PLAIN, 11));
		cepCaixa.setForeground(Color.BLACK);
		cepCaixa.setColumns(10);
		cepCaixa.setBounds(144, 53, 240, 25);
		cepCaixa.setBorder(BorderFactory.createLineBorder(Color.black));
		painelEnredecoEmpresa.add(cepCaixa);

		logradouroCaixa = new JTextField();
		logradouroCaixa.setFont(new Font("Arial", Font.PLAIN, 11));
		logradouroCaixa.setForeground(Color.BLACK);
		logradouroCaixa.setEditable(false);
		logradouroCaixa.setColumns(10);
		logradouroCaixa.setBounds(144, 90, 240, 25);
		logradouroCaixa.setBorder(BorderFactory.createLineBorder(Color.black));
		painelEnredecoEmpresa.add(logradouroCaixa);

		bairroCaixa = new JTextField();
		bairroCaixa.setFont(new Font("Arial", Font.PLAIN, 11));
		bairroCaixa.setForeground(Color.BLACK);
		bairroCaixa.setEditable(false);
		bairroCaixa.setColumns(10);
		bairroCaixa.setBounds(144, 127, 240, 25);
		bairroCaixa.setBorder(BorderFactory.createLineBorder(Color.black));
		painelEnredecoEmpresa.add(bairroCaixa);

		cidadeCaixa = new JTextField();
		cidadeCaixa.setFont(new Font("Arial", Font.PLAIN, 11));
		cidadeCaixa.setForeground(Color.BLACK);
		cidadeCaixa.setEditable(false);
		cidadeCaixa.setColumns(10);
		cidadeCaixa.setBounds(144, 164, 240, 25);
		cidadeCaixa.setBorder(BorderFactory.createLineBorder(Color.black));
		painelEnredecoEmpresa.add(cidadeCaixa);

		ufCaixa = new JTextField();
		ufCaixa.setFont(new Font("Arial", Font.PLAIN, 11));
		ufCaixa.setForeground(Color.BLACK);
		ufCaixa.setEditable(false);
		ufCaixa.setColumns(10);
		ufCaixa.setBounds(144, 201, 240, 25);
		ufCaixa.setBorder(BorderFactory.createLineBorder(Color.black));
		painelEnredecoEmpresa.add(ufCaixa);

		numeroCaixa = new JTextField();
		numeroCaixa.setFont(new Font("Arial", Font.PLAIN, 11));
		numeroCaixa.setForeground(Color.BLACK);
		numeroCaixa.setColumns(10);
		numeroCaixa.setBounds(144, 275, 240, 25);
		numeroCaixa.setBorder(BorderFactory.createLineBorder(Color.black));
		painelEnredecoEmpresa.add(numeroCaixa);

		paisCaixa = new JTextField();
		paisCaixa.setFont(new Font("Arial", Font.PLAIN, 11));
		paisCaixa.setForeground(Color.BLACK);
		paisCaixa.setEditable(false);
		paisCaixa.setColumns(10);
		paisCaixa.setBounds(144, 238, 240, 25);
		paisCaixa.setBorder(BorderFactory.createLineBorder(Color.black));
		painelEnredecoEmpresa.add(paisCaixa);

		JLabel astCep = new JLabel("*");
		astCep.setToolTipText("");
		astCep.setOpaque(false);
		astCep.setHorizontalAlignment(SwingConstants.CENTER);
		astCep.setForeground(Color.RED);
		astCep.setBackground((Color) null);
		astCep.setBounds(18, 57, 13, 17);
		painelEnredecoEmpresa.add(astCep);

		JLabel astNumero = new JLabel("*");
		astNumero.setToolTipText("");
		astNumero.setOpaque(false);
		astNumero.setHorizontalAlignment(SwingConstants.CENTER);
		astNumero.setForeground(Color.RED);
		astNumero.setBackground((Color) null);
		astNumero.setBounds(18, 280, 13, 14);
		painelEnredecoEmpresa.add(astNumero);

		complementoCaixa = new JTextField();
		complementoCaixa.setFont(new Font("Arial", Font.PLAIN, 11));
		complementoCaixa.setForeground(Color.BLACK);
		complementoCaixa.setColumns(10);
		complementoCaixa.setBounds(144, 312, 240, 25);
		complementoCaixa.setBorder(BorderFactory.createLineBorder(Color.black));
		painelEnredecoEmpresa.add(complementoCaixa);

		JLabel complementoLabel = new JLabel("Complemento: ");
		complementoLabel.setHorizontalAlignment(SwingConstants.LEFT);
		complementoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		complementoLabel.setForeground(Color.BLACK);
		complementoLabel.setBounds(31, 316, 100, 17);
		painelEnredecoEmpresa.add(complementoLabel);
		
		cadastrarButton = new JButton("Cadastrar");
		cadastrarButton.setBounds(75, 653, 250, 45);
		painelGeral.add(cadastrarButton);
		cadastrarButton.setFont(new Font("Arial", Font.BOLD, 14));
		cadastrarButton.setForeground(Color.WHITE);
		cadastrarButton.setBackground(Color.BLACK);
		cadastrarButton.addActionListener(e -> {
		    String email = emailCaixa.getText();
		    String senha = senhaCaixa.getText();
		    String cep = cepCaixa.getText();
		    String logra = logradouroCaixa.getText();
		    String bairro = bairroCaixa.getText();
		    String cidade = cidadeCaixa.getText();
		    String uf = ufCaixa.getText();
		    String numero = numeroCaixa.getText();
		    String pais = paisCaixa.getText();
		    String comple = complementoCaixa.getText();
		    String confirmaSenha = confirmaSenhaCaixa.getText();
		    String confirmaEmail = confirmaEmailCaixa.getText();
		    String cnpj = cnpjCaixa.getText();
		    cnpj = cnpj.replaceAll("\\D", "");
		    EmpregadorDAO empregadorDao = new EmpregadorDAO();
		    try {
				cnpjBd = empregadorDao.existeCnpj(cnpj);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		    String razaoSocial = razaoSocialCaixa.getText();

		    if (email.isEmpty() || senha.isEmpty() || logra.isEmpty() || bairro.isEmpty() || cidade.isEmpty()
		            || uf.isEmpty() || numero.isEmpty() || pais.isEmpty() || confirmaSenha.isEmpty()
		            || confirmaEmail.isEmpty() || cnpj.isEmpty() || razaoSocial.isEmpty()) {
		        caixaOk("Todos os campos devem ser preenchidos!");
		    } else if (cnpjV == false || cepV == false || emailV == false || cnpjBd == true ||!(senha.equals(confirmaSenha))
		            || !(email.equals(confirmaEmail))) {
		        String mensagem = "";
		        if (cnpjV == false) {
		            mensagem = "CNPJ inválido";
		        }
		        if (cepV == false) {
		            mensagem = "CEP inválido";
		        }
		        if (emailV == false) {
		            mensagem = "Email inválido";
		        }
		        if (cnpjBd == true) {
		        	mensagem = "Cnpj ja cadastrado";
		        }
		        if (!(senha.equals(confirmaSenha))) {
		            mensagem = "Senhas não coincidem";
		        }
		        if (!(email.equals(confirmaEmail))) {
		            mensagem = "Emails não coincidem";
		        }
		        caixaOk(mensagem);
		    } else {
		        try {
		            EmpregadorDAO empregadorDAO = new EmpregadorDAO();
		            EnderecoDAO enderecoDAO = new EnderecoDAO();
		            
		            Empregador empregador = new Empregador();
		            empregador.setEmail(email);
		            empregador.setSenha(senha);

		            Endereco endereco = new Endereco();
		            endereco.setCep(cep);
		            endereco.setLogradouro(logra);
		            endereco.setBairro(bairro);
		            endereco.setCidade(cidade);
		            endereco.setUf(uf);
		            endereco.setNumero(numero);
		            endereco.setPais(pais);
		            endereco.setComplemento(comple);
		            
		            enderecoDAO.criarEndereco(endereco);
		            int enderecoId = enderecoDAO.obterUltimoIdInserido();
		            
		            endereco.setId(enderecoId);
		            empregador.setEndereco(endereco);

		            empregador.setCnpj(cnpj);
		            empregador.setRazaoSocial(razaoSocial);
		            
		            empregadorDAO.criarEmpregador(empregador);

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
		    			frame.dispose();
			            new Login();
		    		});
		    		caixaDialogo.getContentPane().add(okButton);

		    		JLabel mensagemLabel1 = new JLabel();
		    		mensagemLabel1.setText("Cadastro realizado com sucesso!");
		    		mensagemLabel1.setFont(new Font("Arial", Font.BOLD, 16));
		    		mensagemLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		    		mensagemLabel1.setForeground(new Color(0, 0, 0));
		    		mensagemLabel1.setBounds(50, 28, 300, 50);
		    		caixaDialogo.getContentPane().add(mensagemLabel1);
		            
		        } catch (SQLException ex) {
		            ex.printStackTrace();
		            caixaOk("Erro ao cadastrar o empregador!");
		        }
		    }
		});



		JButton jbv = new JButton();
		jbv.setBounds(-10, 10, 130, 25);
		contentPanel.add(jbv);
		jbv.setText("Voltar");
		jbv.setVisible(true);
		jbv.setOpaque(false);
		jbv.setContentAreaFilled(false);
		jbv.setBorderPainted(false);
		jbv.setForeground(Color.black);
		jbv.setFont(new Font("Arial", Font.BOLD, 18));
		ImageIcon voltarIcon = new ImageIcon(Cadastro.class.getResource("/imagem/voltar.png"));
		jbv.setIcon(voltarIcon);
		jbv.addActionListener(e -> {
			frame.dispose();
			new Login();
		});

		JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
		verticalScrollBar.setUnitIncrement(50);

		JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
		horizontalScrollBar.setUnitIncrement(50);

		cepCaixa.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				cepCaixa.setCaretPosition(0);
			}
			@Override
			public void focusLost(java.awt.event.FocusEvent e) {
				try {
					String cep = cepCaixa.getText();
					cep = cep.replaceAll("-", "");
					cep = cep.replaceAll("_", "");
					if (cep.length() == 8) {
						cepCaixa.setText(cep.substring(0, 5) + "-" + cep.substring(5, 8));
						endereco = controller.buscarCep(cep);
						logradouroCaixa.setText(endereco.getLogradouro());
						bairroCaixa.setText(endereco.getBairro());
						cidadeCaixa.setText(endereco.getCidade());
						ufCaixa.setText(endereco.getUf());
						paisCaixa.setText(endereco.getPais());
						cepV = true;
						cepCaixa.setBorder(BorderFactory.createLineBorder(Color.green));
					} else {
						logradouroCaixa.setText("");
						bairroCaixa.setText("");
						cidadeCaixa.setText("");
						ufCaixa.setText("");
						paisCaixa.setText("");
						cepV = false;
						cepCaixa.setBorder(BorderFactory.createLineBorder(Color.red));
					}
				} catch (Exception e1) {
					cepCaixa.setBorder(BorderFactory.createLineBorder(Color.red));
				}
			}
		});
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Cadastro();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
