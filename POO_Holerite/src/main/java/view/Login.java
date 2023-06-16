package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import model.dao.ContextoUsuario;
import model.dao.EmpregadorDAO;
import model.entidade.Empregador;

public class Login {

	JFrame frame = new JFrame();
	JLabel email, senha, imagem, ajuda1, ajuda2;
	JFrame telaLogin, caixa;
	JTextField caixaEmail, caixaSenha;
	JButton botaoLogin, botaoSair, jbOk, cadastro;

	public Login() {
		initialize();
	}

	private void initialize() {
		email = criarTexto("Email: ", "Insira seu email", 100, 425, 70, 25);
		senha = criarTexto("Senha: ", "Insira sua senha", 100, 475, 72, 25);
		caixaEmail = criarCaixaDeTexto(175, 425, 300, 25);
		caixaSenha = criarCaixaSenha(175, 475, 300, 25);
		imagem = adicionarImagem("/imagem/pessoa.png", 150, 60, 300, 300);
		botaoLogin = criarBotao("Login", 100, 550, 175, 35);
		botaoSair = criarBotao("Sair", 300, 550, 175, 35);
		cadastro = criarBotaoCadastro("Clique aqui para se cadastrar!", 150, 600, 300, 20);

		botaoLogin.addActionListener(e -> {
		    String email = caixaEmail.getText();
		    String senha = caixaSenha.getText();
		    if (email.isEmpty() || senha.isEmpty()) {
		        caixaOk("Preencha todos os dados para logar!");
		    } else {
		        EmpregadorDAO empregadorDAO = null;
		        try {
		            empregadorDAO = new EmpregadorDAO();
		        } catch (Exception ex) {
		            ex.printStackTrace();
		            caixaOk("Erro ao obter conexão com o banco de dados!");
		        }
		        
		        if (empregadorDAO != null) {
		            ContextoUsuario contextoUsuario = ContextoUsuario.getInstance();
		            try {
		                Empregador empregador = empregadorDAO.loginEmpregador(email, senha);
		                if (empregador != null) {
		                    contextoUsuario.login(empregador);
		                    frame.dispose();
		                    new MenuAdm(empregador);
		                } else {
		                    caixaOk("Email ou senha incorretos!");
		                }
		            } catch (SQLException ex) {
		                ex.printStackTrace();
		                caixaOk("Erro ao verificar o login!");
		            }
		        }
		    }
		});



		cadastro.addActionListener(e -> {
			frame.dispose();
			new Cadastro();
		});

		botaoSair.addActionListener(e -> {
			System.exit(0);
		});

		frame.setTitle("• Rabisco Holerite | Login");
		frame.setSize(600, 700);
		frame.setLayout(null);
		frame.getContentPane().setBackground(Color.white);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		frame.getContentPane().add(imagem);
		frame.getContentPane().add(email);
		frame.getContentPane().add(senha);
		frame.getContentPane().add(caixaEmail);
		frame.getContentPane().add(caixaSenha);
		frame.getContentPane().add(botaoSair);
		frame.getContentPane().add(botaoLogin);
		frame.getContentPane().add(cadastro);
		frame.getContentPane().add(cadastro);

		ImageIcon iconetela = new ImageIcon(Login.class.getResource("/imagem/cadeado.png"));
		frame.setIconImage(iconetela.getImage());
	}

	public JButton criarBotao(String texto, int esq, int topo, int larg, int alt) {
		JButton jb = new JButton();
		jb.setBounds(esq, topo, larg, alt);
		jb.setText(texto);
		jb.setVisible(true);
		jb.setBackground(Color.black);
		jb.setForeground(Color.white);
		jb.setFont(new Font("Arial", Font.BOLD, 18));
		return jb;
	}

	public JButton criarBotaoCadastro(String texto, int esq, int topo, int larg, int alt) {
		JButton jb = new JButton();
		jb.setBounds(esq, topo, larg, alt);
		jb.setText(texto);
		jb.setVisible(true);
		jb.setOpaque(false);
		jb.setContentAreaFilled(false);
		jb.setBorderPainted(false);
		jb.setBackground(Color.white);
		jb.setForeground(Color.black);
		jb.setFont(new Font("Arial", Font.BOLD, 12));
		return jb;
	}

	public JLabel adicionarImagem(String endImagem, int esq, int topo, int larg, int alt) {
		ImageIcon image = new ImageIcon(Login.class.getResource("/imagem/pessoa.png"));
		JLabel label = new JLabel();
		label.setIcon(image);
		label.setBounds(esq, topo, larg, alt);
		return label;
	}

	public JTextField criarCaixaDeTexto(int esq, int topo, int larg, int alt) {
		JTextField jt = new JTextField();
		jt.setBounds(esq, topo, larg, alt);
		jt.setFont(new Font("Arial", Font.PLAIN, 15));
		jt.setHorizontalAlignment(JTextField.LEFT);
		jt.setBorder(BorderFactory.createLineBorder(Color.black));
		frame.add(jt);
		jt.setVisible(true);
		return jt;
	}

	public JPasswordField criarCaixaSenha(int esq, int topo, int larg, int alt) {
		JPasswordField jpf = new JPasswordField();
		jpf.setBounds(esq, topo, larg, alt);
		jpf.setFont(new Font("Arial", Font.PLAIN, 15));
		jpf.setBorder(BorderFactory.createLineBorder(Color.black));
		frame.add(jpf);
		jpf.setVisible(true);
		return jpf;
	}

	public JLabel criarTexto(String texto, String textoEscondido, int esq, int topo, int larg, int alt) {
		JLabel jl = new JLabel(texto);
		jl.setBounds(esq, topo, larg, alt);
		jl.setOpaque(false);
		jl.setForeground(new Color(0, 0, 0));
		jl.setFont(new Font("Arial", Font.BOLD, 19));
		jl.setToolTipText(textoEscondido);
		jl.setHorizontalAlignment(SwingConstants.LEFT);
		jl.setVerticalAlignment(SwingConstants.TOP);
		frame.add(jl);
		jl.setVisible(true);
		return jl;
	}

	public JLabel criarTextoAjuda(String texto, String textoEscondido, int esq, int topo, int larg, int alt) {
		JLabel jl = new JLabel(texto);
		jl.setBounds(esq, topo, larg, alt);
		jl.setOpaque(false);
		jl.setForeground(Color.red);
		jl.setFont(new Font("Arial", Font.BOLD, 15));
		jl.setToolTipText(textoEscondido);
		jl.setHorizontalAlignment(SwingConstants.LEFT);
		jl.setVerticalAlignment(SwingConstants.TOP);
		frame.add(jl);
		jl.setVisible(true);
		return jl;
	}

	public JFrame caixaDeMensagem(String texto) {
		JFrame caixa = new JFrame();
		caixa.setTitle("Caixa de mensagem");
		caixa.setSize(400, 200);
		caixa.setLayout(null);
		caixa.getContentPane().setBackground(Color.white);
		caixa.setResizable(false);
		caixa.setVisible(true);
		caixa.setLocationRelativeTo(null);
		ImageIcon java = new ImageIcon("src/imagem/java.png");
		caixa.setIconImage(java.getImage());
		jbOk = new JButton("Ok");
		jbOk.setBounds(50, 100, 300, 35);
		jbOk.setVisible(true);
		jbOk.setBackground(Color.black);
		jbOk.setForeground(Color.white);
		jbOk.setFont(new Font("Arial", Font.BOLD, 18));
		JLabel jl = new JLabel(texto);
		jl.setBounds(50, 50, 300, 35);
		jl.setOpaque(false);
		jl.setVerticalAlignment(JLabel.CENTER);
		jl.setForeground(new Color(0, 0, 0));
		jl.setFont(new Font("Arial", Font.BOLD, 16));
		jl.setHorizontalAlignment(JLabel.CENTER);
		jl.setVerticalAlignment(JLabel.TOP);
		caixa.add(jbOk);
		caixa.add(jl);
		return caixa;
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
