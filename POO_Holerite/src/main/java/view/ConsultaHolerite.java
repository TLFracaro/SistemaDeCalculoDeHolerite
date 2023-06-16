package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import model.dao.HoleriteDAO;
import model.entidade.Desconto;
import model.entidade.Empregador;
import model.entidade.Funcionario;
import model.entidade.Holerite;

public class ConsultaHolerite implements Printable {

	JFrame frame;

	public ConsultaHolerite(Empregador usuario, Funcionario funcionario) {
		initialize(usuario, funcionario);
	}

	private void initialize(Empregador usuario, Funcionario funcionario) {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBackground(Color.WHITE);
		frame.setIconImage(
				Toolkit.getDefaultToolkit().getImage(ConsultaHolerite.class.getResource("/Imagem/rabisco.png")));
		frame.setTitle("• Rascunho Holerite | Consulta Holerite");
		frame.setSize(600, 650);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);

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
			new MenuBusca(usuario, funcionario);
		});
		frame.getContentPane().add(voltarButton);

		painelHolerite(usuario, funcionario);

		JButton imprimirButton = new JButton();
		imprimirButton.setText("Imprimir");
		imprimirButton.setBounds(60, 540, 200, 50);
		imprimirButton.setContentAreaFilled(false);
		imprimirButton.setFocusPainted(false);
		imprimirButton.setOpaque(true);
		imprimirButton.setBackground(Color.black);
		imprimirButton.setForeground(Color.white);
		imprimirButton.setFont(new Font("Arial", Font.BOLD, 17));
		frame.getContentPane().add(imprimirButton);

		imprimirButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PrinterJob job = PrinterJob.getPrinterJob();
				Printable printable = new Printable() {
					@Override
					public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
						if (pageIndex > 0) {
							return NO_SUCH_PAGE;
						}
						Graphics2D g2d = (Graphics2D) graphics;
						g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
						painelHolerite(usuario, funcionario).printAll(g2d);
						return PAGE_EXISTS;
					}
				};
				job.setPrintable(printable);
				if (job.printDialog()) {
					try {
						job.print();
					} catch (PrinterException ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		JButton salvarButton = new JButton();
		salvarButton.setText("Salvar");
		salvarButton.setBounds(321, 540, 200, 50);
		salvarButton.setContentAreaFilled(false);
		salvarButton.setFocusPainted(false);
		salvarButton.setOpaque(true);
		salvarButton.setBackground(Color.black);
		salvarButton.setForeground(Color.white);
		salvarButton.setFont(new Font("Arial", Font.BOLD, 17));
		frame.getContentPane().add(salvarButton);
		salvarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Salvar holerite");
				int userSelection = fileChooser.showSaveDialog(null);
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					if (!file.getName().endsWith(".pdf")) {
						file = new File(file.getPath() + ".pdf");
					}
					try {
						file.createNewFile();
					} catch (IOException ex) {
						ex.printStackTrace();
					}

					FileOutputStream fos = null;
					try {
						fos = new FileOutputStream(file);
					} catch (FileNotFoundException ex) {
						ex.printStackTrace();
					}

					com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4, 36, 36, 36, 36);
					try {
						PdfWriter writer = PdfWriter.getInstance(document, fos);
						document.open();

						PdfContentByte canvas = writer.getDirectContent();

						JLayeredPane painel = painelHolerite(usuario, funcionario);
						PdfTemplate template = canvas.createTemplate(painel.getWidth(), painel.getHeight());

						Graphics2D g2d = template.createGraphics(painel.getWidth(), painel.getHeight());
						painel.printAll(g2d);
						g2d.dispose();

						canvas.addTemplate(template, 0, PageSize.A4.getHeight() - painel.getHeight());

						document.close();
						fos.close();
					} catch (DocumentException | IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
	}

	public JLayeredPane painelHolerite(Empregador usuario, Funcionario funcionario) {
		JLayeredPane painelGeral = new JLayeredPane();
		painelGeral.setBounds(14, 46, 556, 483);
		frame.getContentPane().add(painelGeral);
		JPanel cabecalhoHolerite = new JPanel();
		cabecalhoHolerite.setForeground(Color.BLACK);
		cabecalhoHolerite.setBounds(10, 11, 535, 33);
		painelGeral.add(cabecalhoHolerite);
		cabecalhoHolerite.setBackground(Color.WHITE);
		cabecalhoHolerite.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		cabecalhoHolerite.setLayout(null);
		JLabel holeriteLabel = new JLabel("HOLERITE DE PAGAMENTO MENSAL");
		holeriteLabel.setForeground(Color.BLACK);
		holeriteLabel.setBounds(144, 9, 247, 17);
		holeriteLabel.setFont(new Font("Arial", Font.BOLD, 14));
		holeriteLabel.setBackground(new Color(255, 255, 255));
		cabecalhoHolerite.add(holeriteLabel);

		JPanel referencia_Empregador = new JPanel();
		referencia_Empregador.setForeground(Color.BLACK);
		referencia_Empregador.setBounds(10, 41, 314, 97);
		painelGeral.add(referencia_Empregador);
		referencia_Empregador.setBackground(Color.WHITE);
		referencia_Empregador.setLayout(null);
		referencia_Empregador.setBorder(new LineBorder(new Color(0, 0, 0), 2));

		JLabel empregadorLabel = new JLabel("EMPREGADOR");
		empregadorLabel.setForeground(Color.BLACK);
		empregadorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		empregadorLabel.setBounds(10, 10, 85, 14);
		referencia_Empregador.add(empregadorLabel);

		JLabel nomeEmpregLabel = new JLabel("Nome: ");
		nomeEmpregLabel.setForeground(Color.BLACK);
		nomeEmpregLabel.setFont(new Font("Arial", Font.PLAIN, 10));
		nomeEmpregLabel.setBounds(10, 28, 34, 13);
		referencia_Empregador.add(nomeEmpregLabel);

		JLabel endeEmpregLabel = new JLabel("Endereço: ");
		endeEmpregLabel.setForeground(Color.BLACK);
		endeEmpregLabel.setFont(new Font("Arial", Font.PLAIN, 10));
		endeEmpregLabel.setBounds(10, 48, 52, 13);
		referencia_Empregador.add(endeEmpregLabel);

		JLabel cnpjEmpregLabel = new JLabel("CNPJ: ");
		cnpjEmpregLabel.setForeground(Color.BLACK);
		cnpjEmpregLabel.setFont(new Font("Arial", Font.PLAIN, 10));
		cnpjEmpregLabel.setBounds(10, 68, 32, 13);
		referencia_Empregador.add(cnpjEmpregLabel);

		JLabel cnpjEmpreSet = new JLabel("XX. XXX. XXX/0001-XX");
		cnpjEmpreSet.setForeground(Color.BLACK);
		cnpjEmpreSet.setFont(new Font("Arial", Font.PLAIN, 10));
		cnpjEmpreSet.setBounds(46, 68, 121, 13);
		referencia_Empregador.add(cnpjEmpreSet);

		JLabel endrecoEmpreSet = new JLabel("XXXXXXXXXXXXXXXXXX");
		endrecoEmpreSet.setForeground(Color.BLACK);
		endrecoEmpreSet.setFont(new Font("Arial", Font.PLAIN, 10));
		endrecoEmpreSet.setBounds(66, 48, 238, 13);
		referencia_Empregador.add(endrecoEmpreSet);

		JLabel nomeEmpreSet = new JLabel("XXXXXXXXXXXXXX");
		nomeEmpreSet.setForeground(Color.BLACK);
		nomeEmpreSet.setFont(new Font("Arial", Font.PLAIN, 10));
		nomeEmpreSet.setBounds(46, 28, 258, 13);
		referencia_Empregador.add(nomeEmpreSet);

		JPanel referencia_DiaMesPag = new JPanel();
		referencia_DiaMesPag.setForeground(Color.BLACK);
		referencia_DiaMesPag.setBounds(321, 41, 224, 97);
		painelGeral.add(referencia_DiaMesPag);
		referencia_DiaMesPag.setBackground(Color.WHITE);
		referencia_DiaMesPag.setLayout(null);
		referencia_DiaMesPag.setBorder(new LineBorder(new Color(0, 0, 0), 2));

		JLabel refeMesLabel = new JLabel("Referente ao Mês / Ano");
		refeMesLabel.setForeground(Color.BLACK);
		refeMesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		refeMesLabel.setBounds(36, 25, 151, 17);
		referencia_DiaMesPag.add(refeMesLabel);

		JLabel referenciaFuncSet = new JLabel("janeiro-XX");
		referenciaFuncSet.setForeground(Color.BLACK);
		referenciaFuncSet.setHorizontalAlignment(SwingConstants.CENTER);
		referenciaFuncSet.setFont(new Font("Arial", Font.BOLD, 16));
		referenciaFuncSet.setBounds(36, 53, 151, 19);
		referencia_DiaMesPag.add(referenciaFuncSet);

		JPanel referencia_Func = new JPanel();
		referencia_Func.setForeground(Color.BLACK);
		referencia_Func.setBounds(10, 137, 535, 45);
		painelGeral.add(referencia_Func);
		referencia_Func.setBackground(Color.WHITE);
		referencia_Func.setLayout(null);
		referencia_Func.setBorder(new LineBorder(new Color(0, 0, 0), 2));

		JLabel codLabel = new JLabel("CPF");
		codLabel.setHorizontalAlignment(SwingConstants.LEFT);
		codLabel.setForeground(Color.BLACK);
		codLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		codLabel.setBounds(15, 10, 48, 14);
		referencia_Func.add(codLabel);

		JLabel nomeFuncLabel = new JLabel("NOME DO FUNCIONÁRIO");
		nomeFuncLabel.setForeground(Color.BLACK);
		nomeFuncLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		nomeFuncLabel.setBounds(100, 9, 142, 14);
		referencia_Func.add(nomeFuncLabel);

		JLabel cboFuncLabel = new JLabel("CBO");
		cboFuncLabel.setForeground(Color.BLACK);
		cboFuncLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		cboFuncLabel.setBounds(303, 9, 26, 14);
		referencia_Func.add(cboFuncLabel);

		JLabel funcaoFuncLabel = new JLabel("FUNÇÃO");
		funcaoFuncLabel.setForeground(Color.BLACK);
		funcaoFuncLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		funcaoFuncLabel.setBounds(377, 9, 50, 14);
		referencia_Func.add(funcaoFuncLabel);

		JLabel codFuncSet = new JLabel("XXXX");
		codFuncSet.setHorizontalAlignment(SwingConstants.LEFT);
		codFuncSet.setForeground(Color.BLACK);
		codFuncSet.setFont(new Font("Arial", Font.PLAIN, 10));
		codFuncSet.setBounds(15, 24, 80, 13);
		referencia_Func.add(codFuncSet);

		JLabel nomeFuncSet = new JLabel("XXXXXXXXXXXXXXXX");
		nomeFuncSet.setForeground(Color.BLACK);
		nomeFuncSet.setFont(new Font("Arial", Font.PLAIN, 10));
		nomeFuncSet.setBounds(100, 24, 142, 13);
		referencia_Func.add(nomeFuncSet);

		JLabel cboFuncSet = new JLabel("XXXXXXX");
		cboFuncSet.setHorizontalAlignment(SwingConstants.CENTER);
		cboFuncSet.setForeground(Color.BLACK);
		cboFuncSet.setFont(new Font("Arial", Font.PLAIN, 10));
		cboFuncSet.setBounds(292, 24, 49, 13);
		referencia_Func.add(cboFuncSet);

		JLabel funcaoFuncSet = new JLabel("XXXXXXXXXXXXXX");
		funcaoFuncSet.setForeground(Color.BLACK);
		funcaoFuncSet.setFont(new Font("Arial", Font.PLAIN, 10));
		funcaoFuncSet.setBounds(377, 24, 148, 13);
		referencia_Func.add(funcaoFuncSet);

		JPanel painel_codigo = new JPanel();
		painel_codigo.setForeground(Color.BLACK);
		painel_codigo.setBounds(10, 181, 50, 22);
		painelGeral.add(painel_codigo);
		painel_codigo.setLayout(null);
		painel_codigo.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		painel_codigo.setBackground(Color.WHITE);

		JLabel codHoleriteLabel = new JLabel("Cód.");
		codHoleriteLabel.setForeground(Color.BLACK);
		codHoleriteLabel.setHorizontalAlignment(SwingConstants.CENTER);
		codHoleriteLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		codHoleriteLabel.setBounds(12, 5, 26, 14);
		painel_codigo.add(codHoleriteLabel);

		JPanel painel_descricao = new JPanel();
		painel_descricao.setForeground(Color.BLACK);
		painel_descricao.setBounds(58, 181, 201, 22);
		painelGeral.add(painel_descricao);
		painel_descricao.setLayout(null);
		painel_descricao.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		painel_descricao.setBackground(Color.WHITE);

		JLabel descHoleriteLabel = new JLabel("descrição");
		descHoleriteLabel.setForeground(Color.BLACK);
		descHoleriteLabel.setHorizontalAlignment(SwingConstants.CENTER);
		descHoleriteLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		descHoleriteLabel.setBounds(72, 4, 56, 14);
		painel_descricao.add(descHoleriteLabel);

		JPanel painel_Referencia = new JPanel();
		painel_Referencia.setForeground(Color.BLACK);
		painel_Referencia.setBounds(255, 181, 87, 22);
		painelGeral.add(painel_Referencia);
		painel_Referencia.setLayout(null);
		painel_Referencia.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		painel_Referencia.setBackground(Color.WHITE);

		JLabel refHoleriteLabel = new JLabel("Referência");
		refHoleriteLabel.setForeground(Color.BLACK);
		refHoleriteLabel.setHorizontalAlignment(SwingConstants.CENTER);
		refHoleriteLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		refHoleriteLabel.setBounds(13, 5, 60, 14);
		painel_Referencia.add(refHoleriteLabel);

		JPanel painel_Proventos = new JPanel();
		painel_Proventos.setForeground(Color.BLACK);
		painel_Proventos.setBounds(340, 181, 104, 22);
		painelGeral.add(painel_Proventos);
		painel_Proventos.setLayout(null);
		painel_Proventos.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		painel_Proventos.setBackground(Color.WHITE);

		JLabel provHoleriteLabel = new JLabel("Proventos");
		provHoleriteLabel.setForeground(Color.BLACK);
		provHoleriteLabel.setHorizontalAlignment(SwingConstants.CENTER);
		provHoleriteLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		provHoleriteLabel.setBounds(24, 5, 55, 14);
		painel_Proventos.add(provHoleriteLabel);

		JPanel painel_descontos = new JPanel();
		painel_descontos.setForeground(Color.BLACK);
		painel_descontos.setBounds(442, 181, 103, 22);
		painelGeral.add(painel_descontos);
		painel_descontos.setLayout(null);
		painel_descontos.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		painel_descontos.setBackground(Color.WHITE);

		JLabel descontHoleriteLabel = new JLabel("descontos");
		descontHoleriteLabel.setForeground(Color.BLACK);
		descontHoleriteLabel.setHorizontalAlignment(SwingConstants.CENTER);
		descontHoleriteLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		descontHoleriteLabel.setBounds(21, 5, 60, 14);
		painel_descontos.add(descontHoleriteLabel);

		JPanel painel_Mensagem = new JPanel();
		painel_Mensagem.setForeground(Color.BLACK);
		painel_Mensagem.setBounds(10, 357, 332, 71);
		painelGeral.add(painel_Mensagem);
		painel_Mensagem.setLayout(null);
		painel_Mensagem.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		painel_Mensagem.setBackground(Color.WHITE);

		JLabel mensagemFuncLabel = new JLabel("MENSAGENS");
		mensagemFuncLabel.setForeground(Color.BLACK);
		mensagemFuncLabel.setFont(new Font("Arial", Font.PLAIN, 10));
		mensagemFuncLabel.setBounds(10, 10, 67, 13);
		painel_Mensagem.add(mensagemFuncLabel);

		JTextArea mensagemSet = new JTextArea();
		mensagemSet.setForeground(Color.BLACK);
		mensagemSet.setBounds(87, 10, 235, 50);
		painel_Mensagem.add(mensagemSet);

		JPanel painel_Rodape = new JPanel();
		painel_Rodape.setForeground(Color.BLACK);
		painel_Rodape.setBounds(10, 426, 535, 48);
		painelGeral.add(painel_Rodape);
		painel_Rodape.setLayout(null);
		painel_Rodape.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		painel_Rodape.setBackground(Color.WHITE);

		JLabel salBaseLabel = new JLabel("Salário Base");
		salBaseLabel.setHorizontalAlignment(SwingConstants.CENTER);
		salBaseLabel.setForeground(Color.BLACK);
		salBaseLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		salBaseLabel.setBounds(20, 9, 64, 13);
		painel_Rodape.add(salBaseLabel);

		JLabel baseInssLabel = new JLabel("Base Cál. INSS");
		baseInssLabel.setHorizontalAlignment(SwingConstants.CENTER);
		baseInssLabel.setForeground(Color.BLACK);
		baseInssLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		baseInssLabel.setBounds(101, 9, 76, 13);
		painel_Rodape.add(baseInssLabel);

		JLabel baseFGTSLabel = new JLabel("Base Cál. FGTS");
		baseFGTSLabel.setHorizontalAlignment(SwingConstants.CENTER);
		baseFGTSLabel.setForeground(Color.BLACK);
		baseFGTSLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		baseFGTSLabel.setBounds(193, 9, 82, 13);
		painel_Rodape.add(baseFGTSLabel);

		JLabel fgtsMesLabel = new JLabel("FGTS do Mês");
		fgtsMesLabel.setHorizontalAlignment(SwingConstants.CENTER);
		fgtsMesLabel.setForeground(Color.BLACK);
		fgtsMesLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		fgtsMesLabel.setBounds(289, 9, 70, 13);
		painel_Rodape.add(fgtsMesLabel);

		JLabel baseIRFFLabel = new JLabel("Base Cál. IRFF");
		baseIRFFLabel.setHorizontalAlignment(SwingConstants.CENTER);
		baseIRFFLabel.setForeground(Color.BLACK);
		baseIRFFLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		baseIRFFLabel.setBounds(374, 9, 77, 13);
		painel_Rodape.add(baseIRFFLabel);

		JLabel faixaIRFFLabel = new JLabel("Faixa IRRF");
		faixaIRFFLabel.setHorizontalAlignment(SwingConstants.CENTER);
		faixaIRFFLabel.setForeground(Color.BLACK);
		faixaIRFFLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		faixaIRFFLabel.setBounds(464, 9, 56, 13);
		painel_Rodape.add(faixaIRFFLabel);

		JLabel salarioBaseFuncSet = new JLabel();
		salarioBaseFuncSet.setForeground(Color.BLACK);
		salarioBaseFuncSet.setHorizontalAlignment(SwingConstants.CENTER);
		salarioBaseFuncSet.setFont(new Font("Arial", Font.PLAIN, 11));
		salarioBaseFuncSet.setBounds(20, 28, 64, 13);
		painel_Rodape.add(salarioBaseFuncSet);

		JLabel baseINSSFuncSet = new JLabel();
		baseINSSFuncSet.setForeground(Color.BLACK);
		baseINSSFuncSet.setHorizontalAlignment(SwingConstants.CENTER);
		baseINSSFuncSet.setFont(new Font("Arial", Font.PLAIN, 11));
		baseINSSFuncSet.setBounds(101, 28, 76, 13);
		painel_Rodape.add(baseINSSFuncSet);

		JLabel baseFGTSFuncSet = new JLabel();
		baseFGTSFuncSet.setForeground(Color.BLACK);
		baseFGTSFuncSet.setHorizontalAlignment(SwingConstants.CENTER);
		baseFGTSFuncSet.setFont(new Font("Arial", Font.PLAIN, 11));
		baseFGTSFuncSet.setBounds(193, 28, 82, 13);
		painel_Rodape.add(baseFGTSFuncSet);

		JLabel fgtsMesFuncSet = new JLabel();
		fgtsMesFuncSet.setForeground(Color.BLACK);
		fgtsMesFuncSet.setHorizontalAlignment(SwingConstants.CENTER);
		fgtsMesFuncSet.setFont(new Font("Arial", Font.PLAIN, 11));
		fgtsMesFuncSet.setBounds(289, 28, 70, 13);
		painel_Rodape.add(fgtsMesFuncSet);

		JLabel baseIRRFFuncSet = new JLabel();
		baseIRRFFuncSet.setForeground(Color.BLACK);
		baseIRRFFuncSet.setHorizontalAlignment(SwingConstants.CENTER);
		baseIRRFFuncSet.setFont(new Font("Arial", Font.PLAIN, 11));
		baseIRRFFuncSet.setBounds(374, 28, 77, 13);
		painel_Rodape.add(baseIRRFFuncSet);

		JLabel faixaIRFFuncSet = new JLabel();
		faixaIRFFuncSet.setForeground(Color.BLACK);
		faixaIRFFuncSet.setHorizontalAlignment(SwingConstants.CENTER);
		faixaIRFFuncSet.setFont(new Font("Arial", Font.PLAIN, 11));
		faixaIRFFuncSet.setBounds(464, 28, 56, 13);
		painel_Rodape.add(faixaIRFFuncSet);

		JPanel painel_cod = new JPanel();
		painel_cod.setForeground(Color.BLACK);
		painel_cod.setBounds(10, 201, 50, 159);
		painelGeral.add(painel_cod);
		painel_cod.setLayout(null);
		painel_cod.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		painel_cod.setBackground(Color.WHITE);

		JLabel cod_01 = new JLabel();
		cod_01.setForeground(Color.BLACK);
		cod_01.setHorizontalAlignment(SwingConstants.CENTER);
		cod_01.setFont(new Font("Arial", Font.PLAIN, 12));
		cod_01.setBounds(14, 5, 21, 14);
		painel_cod.add(cod_01);

		JLabel cod_02 = new JLabel();
		cod_02.setForeground(Color.BLACK);
		cod_02.setHorizontalAlignment(SwingConstants.CENTER);
		cod_02.setFont(new Font("Arial", Font.PLAIN, 12));
		cod_02.setBounds(14, 20, 21, 14);
		painel_cod.add(cod_02);

		JLabel cod_03 = new JLabel();
		cod_03.setForeground(Color.BLACK);
		cod_03.setHorizontalAlignment(SwingConstants.CENTER);
		cod_03.setFont(new Font("Arial", Font.PLAIN, 12));
		cod_03.setBounds(14, 35, 21, 14);
		painel_cod.add(cod_03);

		JLabel cod_04 = new JLabel();
		cod_04.setForeground(Color.BLACK);
		cod_04.setHorizontalAlignment(SwingConstants.CENTER);
		cod_04.setFont(new Font("Arial", Font.PLAIN, 12));
		cod_04.setBounds(14, 50, 21, 14);
		painel_cod.add(cod_04);

		JLabel cod_05 = new JLabel();
		cod_05.setForeground(Color.BLACK);
		cod_05.setHorizontalAlignment(SwingConstants.CENTER);
		cod_05.setFont(new Font("Arial", Font.PLAIN, 12));
		cod_05.setBounds(14, 65, 21, 14);
		painel_cod.add(cod_05);

		JLabel cod_06 = new JLabel();
		cod_06.setForeground(Color.BLACK);
		cod_06.setHorizontalAlignment(SwingConstants.CENTER);
		cod_06.setFont(new Font("Arial", Font.PLAIN, 12));
		cod_06.setBounds(14, 80, 21, 14);
		painel_cod.add(cod_06);

		JLabel cod_07 = new JLabel();
		cod_07.setForeground(Color.BLACK);
		cod_07.setHorizontalAlignment(SwingConstants.CENTER);
		cod_07.setFont(new Font("Arial", Font.PLAIN, 12));
		cod_07.setBounds(14, 95, 21, 14);
		painel_cod.add(cod_07);

		JLabel cod_08 = new JLabel();
		cod_08.setForeground(Color.BLACK);
		cod_08.setHorizontalAlignment(SwingConstants.CENTER);
		cod_08.setFont(new Font("Arial", Font.PLAIN, 12));
		cod_08.setBounds(14, 110, 21, 14);
		painel_cod.add(cod_08);

		JLabel cod_09 = new JLabel();
		cod_09.setForeground(Color.BLACK);
		cod_09.setHorizontalAlignment(SwingConstants.CENTER);
		cod_09.setFont(new Font("Arial", Font.PLAIN, 12));
		cod_09.setBounds(14, 125, 21, 14);
		painel_cod.add(cod_09);

		JLabel cod_10 = new JLabel();
		cod_10.setForeground(Color.BLACK);
		cod_10.setHorizontalAlignment(SwingConstants.CENTER);
		cod_10.setFont(new Font("Arial", Font.PLAIN, 12));
		cod_10.setBounds(14, 140, 21, 14);
		painel_cod.add(cod_10);

		JPanel painel_desc = new JPanel();
		painel_desc.setForeground(Color.BLACK);
		painel_desc.setBounds(58, 201, 201, 159);
		painelGeral.add(painel_desc);
		painel_desc.setLayout(null);
		painel_desc.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		painel_desc.setBackground(Color.WHITE);

		JLabel desc_01 = new JLabel();
		desc_01.setForeground(Color.BLACK);
		desc_01.setHorizontalAlignment(SwingConstants.CENTER);
		desc_01.setFont(new Font("Arial", Font.PLAIN, 12));
		desc_01.setBounds(7, 5, 184, 14);
		painel_desc.add(desc_01);

		JLabel desc_02 = new JLabel();
		desc_02.setForeground(Color.BLACK);
		desc_02.setHorizontalAlignment(SwingConstants.CENTER);
		desc_02.setFont(new Font("Arial", Font.PLAIN, 12));
		desc_02.setBounds(7, 20, 184, 14);
		painel_desc.add(desc_02);

		JLabel desc_03 = new JLabel();
		desc_03.setForeground(Color.BLACK);
		desc_03.setHorizontalAlignment(SwingConstants.CENTER);
		desc_03.setFont(new Font("Arial", Font.PLAIN, 12));
		desc_03.setBounds(7, 35, 184, 14);
		painel_desc.add(desc_03);

		JLabel desc_04 = new JLabel();
		desc_04.setForeground(Color.BLACK);
		desc_04.setHorizontalAlignment(SwingConstants.CENTER);
		desc_04.setFont(new Font("Arial", Font.PLAIN, 12));
		desc_04.setBounds(7, 50, 184, 14);
		painel_desc.add(desc_04);

		JLabel desc_05 = new JLabel();
		desc_05.setForeground(Color.BLACK);
		desc_05.setHorizontalAlignment(SwingConstants.CENTER);
		desc_05.setFont(new Font("Arial", Font.PLAIN, 12));
		desc_05.setBounds(7, 65, 184, 14);
		painel_desc.add(desc_05);

		JLabel desc_06 = new JLabel();
		desc_06.setForeground(Color.BLACK);
		desc_06.setHorizontalAlignment(SwingConstants.CENTER);
		desc_06.setFont(new Font("Arial", Font.PLAIN, 12));
		desc_06.setBounds(7, 80, 184, 14);
		painel_desc.add(desc_06);

		JLabel desc_07 = new JLabel();
		desc_07.setForeground(Color.BLACK);
		desc_07.setHorizontalAlignment(SwingConstants.CENTER);
		desc_07.setFont(new Font("Arial", Font.PLAIN, 12));
		desc_07.setBounds(7, 95, 184, 14);
		painel_desc.add(desc_07);

		JLabel desc_08 = new JLabel();
		desc_08.setForeground(Color.BLACK);
		desc_08.setHorizontalAlignment(SwingConstants.CENTER);
		desc_08.setFont(new Font("Arial", Font.PLAIN, 12));
		desc_08.setBounds(7, 110, 184, 14);
		painel_desc.add(desc_08);

		JLabel desc_09 = new JLabel();
		desc_09.setForeground(Color.BLACK);
		desc_09.setHorizontalAlignment(SwingConstants.CENTER);
		desc_09.setFont(new Font("Arial", Font.PLAIN, 12));
		desc_09.setBounds(7, 125, 184, 14);
		painel_desc.add(desc_09);

		JLabel desc_10 = new JLabel();
		desc_10.setForeground(Color.BLACK);
		desc_10.setHorizontalAlignment(SwingConstants.CENTER);
		desc_10.setFont(new Font("Arial", Font.PLAIN, 12));
		desc_10.setBounds(7, 140, 184, 14);
		painel_desc.add(desc_10);

		JPanel painel_Ref = new JPanel();
		painel_Ref.setForeground(Color.BLACK);
		painel_Ref.setBounds(255, 201, 87, 159);
		painelGeral.add(painel_Ref);
		painel_Ref.setLayout(null);
		painel_Ref.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		painel_Ref.setBackground(Color.WHITE);

		JLabel ref_01 = new JLabel();
		ref_01.setForeground(Color.BLACK);
		ref_01.setHorizontalAlignment(SwingConstants.CENTER);
		ref_01.setFont(new Font("Arial", Font.PLAIN, 12));
		ref_01.setBounds(10, 5, 67, 14);
		painel_Ref.add(ref_01);

		JLabel ref_02 = new JLabel();
		ref_02.setForeground(Color.BLACK);
		ref_02.setHorizontalAlignment(SwingConstants.CENTER);
		ref_02.setFont(new Font("Arial", Font.PLAIN, 12));
		ref_02.setBounds(10, 20, 67, 14);
		painel_Ref.add(ref_02);

		JLabel ref_03 = new JLabel();
		ref_03.setForeground(Color.BLACK);
		ref_03.setHorizontalAlignment(SwingConstants.CENTER);
		ref_03.setFont(new Font("Arial", Font.PLAIN, 12));
		ref_03.setBounds(10, 35, 67, 14);
		painel_Ref.add(ref_03);

		JLabel ref_04 = new JLabel();
		ref_04.setForeground(Color.BLACK);
		ref_04.setHorizontalAlignment(SwingConstants.CENTER);
		ref_04.setFont(new Font("Arial", Font.PLAIN, 12));
		ref_04.setBounds(10, 50, 67, 14);
		painel_Ref.add(ref_04);

		JLabel ref_05 = new JLabel();
		ref_05.setForeground(Color.BLACK);
		ref_05.setHorizontalAlignment(SwingConstants.CENTER);
		ref_05.setFont(new Font("Arial", Font.PLAIN, 12));
		ref_05.setBounds(10, 65, 67, 14);
		painel_Ref.add(ref_05);

		JLabel ref_06 = new JLabel();
		ref_06.setForeground(Color.BLACK);
		ref_06.setHorizontalAlignment(SwingConstants.CENTER);
		ref_06.setFont(new Font("Arial", Font.PLAIN, 12));
		ref_06.setBounds(10, 80, 67, 14);
		painel_Ref.add(ref_06);

		JLabel ref_07 = new JLabel();
		ref_07.setForeground(Color.BLACK);
		ref_07.setHorizontalAlignment(SwingConstants.CENTER);
		ref_07.setFont(new Font("Arial", Font.PLAIN, 12));
		ref_07.setBounds(10, 95, 67, 14);
		painel_Ref.add(ref_07);

		JLabel ref_08 = new JLabel();
		ref_08.setForeground(Color.BLACK);
		ref_08.setHorizontalAlignment(SwingConstants.CENTER);
		ref_08.setFont(new Font("Arial", Font.PLAIN, 12));
		ref_08.setBounds(10, 110, 67, 14);
		painel_Ref.add(ref_08);

		JLabel ref_09 = new JLabel();
		ref_09.setForeground(Color.BLACK);
		ref_09.setHorizontalAlignment(SwingConstants.CENTER);
		ref_09.setFont(new Font("Arial", Font.PLAIN, 12));
		ref_09.setBounds(10, 125, 67, 14);
		painel_Ref.add(ref_09);

		JLabel ref_10 = new JLabel();
		ref_10.setForeground(Color.BLACK);
		ref_10.setHorizontalAlignment(SwingConstants.CENTER);
		ref_10.setFont(new Font("Arial", Font.PLAIN, 12));
		ref_10.setBounds(10, 140, 67, 14);
		painel_Ref.add(ref_10);

		JPanel painel_Prov = new JPanel();
		painel_Prov.setForeground(Color.BLACK);
		painel_Prov.setBounds(340, 200, 104, 159);
		painelGeral.add(painel_Prov);
		painel_Prov.setLayout(null);
		painel_Prov.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		painel_Prov.setBackground(Color.WHITE);

		JLabel prov_01 = new JLabel();
		prov_01.setForeground(Color.BLACK);
		prov_01.setHorizontalAlignment(SwingConstants.CENTER);
		prov_01.setFont(new Font("Arial", Font.PLAIN, 12));
		prov_01.setBounds(10, 6, 84, 14);
		painel_Prov.add(prov_01);

		JLabel prov_02 = new JLabel();
		prov_02.setForeground(Color.BLACK);
		prov_02.setHorizontalAlignment(SwingConstants.CENTER);
		prov_02.setFont(new Font("Arial", Font.PLAIN, 12));
		prov_02.setBounds(10, 21, 84, 14);
		painel_Prov.add(prov_02);

		JLabel prov_03 = new JLabel();
		prov_03.setForeground(Color.BLACK);
		prov_03.setHorizontalAlignment(SwingConstants.CENTER);
		prov_03.setFont(new Font("Arial", Font.PLAIN, 12));
		prov_03.setBounds(10, 36, 84, 14);
		painel_Prov.add(prov_03);

		JLabel prov_04 = new JLabel();
		prov_04.setForeground(Color.BLACK);
		prov_04.setHorizontalAlignment(SwingConstants.CENTER);
		prov_04.setFont(new Font("Arial", Font.PLAIN, 12));
		prov_04.setBounds(10, 51, 84, 14);
		painel_Prov.add(prov_04);

		JLabel prov_05 = new JLabel();
		prov_05.setForeground(Color.BLACK);
		prov_05.setHorizontalAlignment(SwingConstants.CENTER);
		prov_05.setFont(new Font("Arial", Font.PLAIN, 12));
		prov_05.setBounds(10, 66, 84, 14);
		painel_Prov.add(prov_05);

		JLabel prov_06 = new JLabel();
		prov_06.setForeground(Color.BLACK);
		prov_06.setHorizontalAlignment(SwingConstants.CENTER);
		prov_06.setFont(new Font("Arial", Font.PLAIN, 12));
		prov_06.setBounds(10, 81, 84, 14);
		painel_Prov.add(prov_06);

		JLabel prov_07 = new JLabel();
		prov_07.setForeground(Color.BLACK);
		prov_07.setHorizontalAlignment(SwingConstants.CENTER);
		prov_07.setFont(new Font("Arial", Font.PLAIN, 12));
		prov_07.setBounds(10, 96, 84, 14);
		painel_Prov.add(prov_07);

		JLabel prov_08 = new JLabel();
		prov_08.setForeground(Color.BLACK);
		prov_08.setHorizontalAlignment(SwingConstants.CENTER);
		prov_08.setFont(new Font("Arial", Font.PLAIN, 12));
		prov_08.setBounds(10, 111, 84, 14);
		painel_Prov.add(prov_08);

		JLabel prov_09 = new JLabel();
		prov_09.setForeground(Color.BLACK);
		prov_09.setHorizontalAlignment(SwingConstants.CENTER);
		prov_09.setFont(new Font("Arial", Font.PLAIN, 12));
		prov_09.setBounds(10, 126, 84, 14);
		painel_Prov.add(prov_09);

		JLabel prov_10 = new JLabel();
		prov_10.setForeground(Color.BLACK);
		prov_10.setHorizontalAlignment(SwingConstants.CENTER);
		prov_10.setFont(new Font("Arial", Font.PLAIN, 12));
		prov_10.setBounds(10, 141, 84, 14);
		painel_Prov.add(prov_10);

		JPanel painel_descont = new JPanel();
		painel_descont.setForeground(Color.BLACK);
		painel_descont.setBounds(442, 200, 103, 159);
		painelGeral.add(painel_descont);
		painel_descont.setLayout(null);
		painel_descont.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		painel_descont.setBackground(Color.WHITE);

		JLabel descont_01 = new JLabel();
		descont_01.setForeground(Color.BLACK);
		descont_01.setHorizontalAlignment(SwingConstants.CENTER);
		descont_01.setFont(new Font("Arial", Font.PLAIN, 12));
		descont_01.setBounds(10, 6, 83, 14);
		painel_descont.add(descont_01);

		JLabel descont_02 = new JLabel();
		descont_02.setForeground(Color.BLACK);
		descont_02.setHorizontalAlignment(SwingConstants.CENTER);
		descont_02.setFont(new Font("Arial", Font.PLAIN, 12));
		descont_02.setBounds(10, 21, 83, 14);
		painel_descont.add(descont_02);

		JLabel descont_03 = new JLabel();
		descont_03.setForeground(Color.BLACK);
		descont_03.setHorizontalAlignment(SwingConstants.CENTER);
		descont_03.setFont(new Font("Arial", Font.PLAIN, 12));
		descont_03.setBounds(10, 36, 83, 14);
		painel_descont.add(descont_03);

		JLabel descont_04 = new JLabel();
		descont_04.setForeground(Color.BLACK);
		descont_04.setHorizontalAlignment(SwingConstants.CENTER);
		descont_04.setFont(new Font("Arial", Font.PLAIN, 12));
		descont_04.setBounds(10, 51, 83, 14);
		painel_descont.add(descont_04);

		JLabel descont_05 = new JLabel();
		descont_05.setForeground(Color.BLACK);
		descont_05.setHorizontalAlignment(SwingConstants.CENTER);
		descont_05.setFont(new Font("Arial", Font.PLAIN, 12));
		descont_05.setBounds(10, 66, 83, 14);
		painel_descont.add(descont_05);

		JLabel descont_06 = new JLabel();
		descont_06.setForeground(Color.BLACK);
		descont_06.setHorizontalAlignment(SwingConstants.CENTER);
		descont_06.setFont(new Font("Arial", Font.PLAIN, 12));
		descont_06.setBounds(10, 81, 83, 14);
		painel_descont.add(descont_06);

		JLabel descont_07 = new JLabel();
		descont_07.setForeground(Color.BLACK);
		descont_07.setHorizontalAlignment(SwingConstants.CENTER);
		descont_07.setFont(new Font("Arial", Font.PLAIN, 12));
		descont_07.setBounds(10, 96, 83, 14);
		painel_descont.add(descont_07);

		JLabel descont_08 = new JLabel();
		descont_08.setForeground(Color.BLACK);
		descont_08.setHorizontalAlignment(SwingConstants.CENTER);
		descont_08.setFont(new Font("Arial", Font.PLAIN, 12));
		descont_08.setBounds(10, 111, 83, 14);
		painel_descont.add(descont_08);

		JLabel descont_09 = new JLabel();
		descont_09.setForeground(Color.BLACK);
		descont_09.setHorizontalAlignment(SwingConstants.CENTER);
		descont_09.setFont(new Font("Arial", Font.PLAIN, 12));
		descont_09.setBounds(10, 126, 83, 14);
		painel_descont.add(descont_09);

		JLabel descont_10 = new JLabel();
		descont_10.setForeground(Color.BLACK);
		descont_10.setHorizontalAlignment(SwingConstants.CENTER);
		descont_10.setFont(new Font("Arial", Font.PLAIN, 12));
		descont_10.setBounds(10, 141, 83, 14);
		painel_descont.add(descont_10);

		JPanel painel_TotVenc = new JPanel();
		painel_TotVenc.setForeground(Color.BLACK);
		painel_TotVenc.setBounds(340, 357, 104, 45);
		painelGeral.add(painel_TotVenc);
		painel_TotVenc.setLayout(null);
		painel_TotVenc.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		painel_TotVenc.setBackground(Color.WHITE);

		JLabel totVencFuncSet = new JLabel("R$");
		totVencFuncSet.setForeground(Color.BLACK);
		totVencFuncSet.setHorizontalAlignment(SwingConstants.CENTER);
		totVencFuncSet.setFont(new Font("Arial", Font.PLAIN, 12));
		totVencFuncSet.setBounds(9, 23, 89, 16);
		painel_TotVenc.add(totVencFuncSet);

		JLabel totalVencLabel = new JLabel("Total de Vencimentos");
		totalVencLabel.setForeground(Color.BLACK);
		totalVencLabel.setBounds(6, 7, 90, 12);
		painel_TotVenc.add(totalVencLabel);
		totalVencLabel.setHorizontalAlignment(SwingConstants.CENTER);
		totalVencLabel.setFont(new Font("Arial", Font.PLAIN, 9));

		JPanel painel_Totdesc = new JPanel();
		painel_Totdesc.setForeground(Color.BLACK);
		painel_Totdesc.setBounds(442, 357, 103, 45);
		painelGeral.add(painel_Totdesc);
		painel_Totdesc.setLayout(null);
		painel_Totdesc.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		painel_Totdesc.setBackground(Color.WHITE);

		JLabel totdescLabel = new JLabel("Total de descontos");
		totdescLabel.setForeground(Color.BLACK);
		totdescLabel.setHorizontalAlignment(SwingConstants.CENTER);
		totdescLabel.setFont(new Font("Arial", Font.PLAIN, 9));
		totdescLabel.setBounds(11, 7, 81, 12);
		painel_Totdesc.add(totdescLabel);

		JLabel totdescFuncSet = new JLabel("R$");
		totdescFuncSet.setForeground(Color.BLACK);
		totdescFuncSet.setHorizontalAlignment(SwingConstants.CENTER);
		totdescFuncSet.setFont(new Font("Arial", Font.PLAIN, 12));
		totdescFuncSet.setBounds(9, 23, 88, 16);
		painel_Totdesc.add(totdescFuncSet);

		JPanel painel_Vl = new JPanel();
		painel_Vl.setForeground(Color.BLACK);
		painel_Vl.setBounds(340, 399, 104, 30);
		painelGeral.add(painel_Vl);
		painel_Vl.setLayout(null);
		painel_Vl.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		painel_Vl.setBackground(Color.WHITE);

		JLabel valorLiquidoLabel = new JLabel("Valor Líquido ->");
		valorLiquidoLabel.setForeground(Color.BLACK);
		valorLiquidoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		valorLiquidoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		valorLiquidoLabel.setBounds(5, 5, 99, 20);
		painel_Vl.add(valorLiquidoLabel);

		JPanel painel_VlR$ = new JPanel();
		painel_VlR$.setForeground(Color.BLACK);
		painel_VlR$.setBounds(442, 399, 103, 30);

		painel_VlR$.setLayout(null);
		painel_VlR$.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		painel_VlR$.setBackground(Color.WHITE);
		painelGeral.add(painel_VlR$);

		JLabel salarioLiquidoFuncSet = new JLabel("R$");
		salarioLiquidoFuncSet.setForeground(Color.BLACK);
		salarioLiquidoFuncSet.setHorizontalAlignment(SwingConstants.CENTER);
		salarioLiquidoFuncSet.setFont(new Font("Arial", Font.PLAIN, 12));
		salarioLiquidoFuncSet.setBounds(5, 5, 92, 20);
		painel_VlR$.add(salarioLiquidoFuncSet);

		String logradouto = (usuario.getEndereco().getLogradouro());
		String bairro = (usuario.getEndereco().getBairro());
		String numero = (usuario.getEndereco().getNumero());

		HoleriteDAO holeriteDao = new HoleriteDAO();
		Holerite holerite = holeriteDao.obterHoleritePorCpf(funcionario.getCpf());
		double fgtsMes = 0.0;

		for (Desconto descontoFor : holerite.getDesconto()) {
			if (descontoFor.getCod().equals("004")) {
				double ref = descontoFor.getReferencia();
				fgtsMes = (holerite.getSalarioBase()*(ref/100));
			}
		}

		nomeEmpreSet.setText(usuario.getRazaoSocial());
		endrecoEmpreSet.setText(logradouto + ", " + bairro + ", " + numero);
		cnpjEmpreSet.setText(usuario.getCnpj());
		referenciaFuncSet.setText(obterMesEAno());
		codFuncSet.setText(funcionario.getCpf());
		nomeFuncSet.setText(funcionario.getNome());
		cboFuncSet.setText(funcionario.getCbo());
		funcaoFuncSet.setText(funcionario.getFuncao());
		mensagemSet.setText(holerite.getMensagem());
		NumberFormat formatadorMoeda = NumberFormat.getCurrencyInstance(Locale.getDefault());
		totVencFuncSet.setText(formatadorMoeda.format(holerite.getTotVencimento()));
		totdescFuncSet.setText(formatadorMoeda.format(holerite.getTotDesconto()));
		salarioLiquidoFuncSet.setText(formatadorMoeda.format(holerite.getValorLiquido()));
		salarioBaseFuncSet.setText(formatadorMoeda.format(holerite.getSalarioBase()));
		baseINSSFuncSet.setText(formatadorMoeda.format(holerite.getBaseInss()));
		baseFGTSFuncSet.setText(formatadorMoeda.format(holerite.getBaseFgts()));
		fgtsMesFuncSet.setText(formatadorMoeda.format(fgtsMes));
		baseIRRFFuncSet.setText(formatadorMoeda.format(holerite.getBaseIrff()));
		faixaIRFFuncSet.setText(String.valueOf((double) holerite.getFaixaIrrf()));
		List<Desconto> descontos = holerite.getDesconto();

		for (int i = 0; i < descontos.size(); i++) {
		    Desconto descontoC = descontos.get(i);

		    String codigo = descontoC.getCod();
		    String descricao = descontoC.getDescricao();
		    String referencia = String.valueOf((double)descontoC.getReferencia());
		    String proventos = String.valueOf((double)descontoC.getProventos());
		    String descontoS = String.valueOf((double)descontoC.getDescontos());
		    
		    String referenciaFormatada = (referencia + "%") ;
		    String proventosFormatados = ("R$ " + proventos);
		    String descontosFormatados = ("R$ " + descontoS);

		    if (i == 0) {
		        cod_01.setText(codigo);
		    } else if (i == 1) {
		        cod_02.setText(codigo);
		    } else if (i == 2) {
		        cod_03.setText(codigo);
		    } else if (i == 3) {
		        cod_04.setText(codigo);
		    } else if (i == 4) {
		        cod_05.setText(codigo);
		    } else if (i == 5) {
		        cod_06.setText(codigo);
		    } else if (i == 6) {
		        cod_07.setText(codigo);
		    } else if (i == 7) {
		        cod_08.setText(codigo);
		    } else if (i == 8) {
		        cod_09.setText(codigo);
		    } else if (i == 9) {
		        cod_10.setText(codigo);
		    }

		    if (i == 0) {
		        desc_01.setText(descricao);
		    } else if (i == 1) {
		        desc_02.setText(descricao);
		    } else if (i == 2) {
		        desc_03.setText(descricao);
		    } else if (i == 3) {
		        desc_04.setText(descricao);
		    } else if (i == 4) {
		        desc_05.setText(descricao);
		    } else if (i == 5) {
		        desc_06.setText(descricao);
		    } else if (i == 6) {
		        desc_07.setText(descricao);
		    } else if (i == 7) {
		        desc_08.setText(descricao);
		    } else if (i == 8) {
		        desc_09.setText(descricao);
		    } else if (i == 9) {
		        desc_10.setText(descricao);
		    }

		    if (i == 0) {
		        ref_01.setText(String.valueOf(referenciaFormatada));
		    } else if (i == 1) {
		        ref_02.setText(String.valueOf(referenciaFormatada));
		    } else if (i == 2) {
		        ref_03.setText(String.valueOf(referenciaFormatada));
		    } else if (i == 3) {
		        ref_04.setText(String.valueOf(referenciaFormatada));
		    } else if (i == 4) {
		        ref_05.setText(String.valueOf(referenciaFormatada));
		    } else if (i == 5) {
		        ref_06.setText(String.valueOf(referenciaFormatada));
		    } else if (i == 6) {
		        ref_07.setText(String.valueOf(referenciaFormatada));
		    } else if (i == 7) {
		        ref_08.setText(String.valueOf(referenciaFormatada));
		    } else if (i == 8) {
		        ref_09.setText(String.valueOf(referenciaFormatada));
		    } else if (i == 9) {
		        ref_10.setText(String.valueOf(referenciaFormatada));
		    }

		    if (i == 0) {
		        prov_01.setText(String.valueOf(proventosFormatados));
		    } else if (i == 1) {
		        prov_02.setText(String.valueOf(proventosFormatados));
		    } else if (i == 2) {
		        prov_03.setText(String.valueOf(proventosFormatados));
		    } else if (i == 3) {
		        prov_04.setText(String.valueOf(proventosFormatados));
		    } else if (i == 4) {
		        prov_05.setText(String.valueOf(proventosFormatados));
		    } else if (i == 5) {
		        prov_06.setText(String.valueOf(proventosFormatados));
		    } else if (i == 6) {
		        prov_07.setText(String.valueOf(proventosFormatados));
		    } else if (i == 7) {
		        prov_08.setText(String.valueOf(proventosFormatados));
		    } else if (i == 8) {
		        prov_09.setText(String.valueOf(proventosFormatados));
		    } else if (i == 9) {
		        prov_10.setText(String.valueOf(proventosFormatados));
		    }

		    if (i == 0) {
		        descont_01.setText(String.valueOf(descontosFormatados));
		    } else if (i == 1) {
		        descont_02.setText(String.valueOf(descontosFormatados));
		    } else if (i == 2) {
		        descont_03.setText(String.valueOf(descontosFormatados));
		    } else if (i == 3) {
		        descont_04.setText(String.valueOf(descontosFormatados));
		    } else if (i == 4) {
		        descont_05.setText(String.valueOf(descontosFormatados));
		    } else if (i == 5) {
		        descont_06.setText(String.valueOf(descontosFormatados));
		    } else if (i == 6) {
		        descont_07.setText(String.valueOf(descontosFormatados));
		    } else if (i == 7) {
		        descont_08.setText(String.valueOf(descontosFormatados));
		    } else if (i == 8) {
		        descont_09.setText(String.valueOf(descontosFormatados));
		    } else if (i == 9) {
		        descont_10.setText(String.valueOf(descontosFormatados));
		    }
		}
		return painelGeral;
	}

	public String obterMesEAno() {
		String[] meses = { "janeiro", "fevereiro", "março", "abril", "maio", "junho", "julho", "agosto", "setembro",
				"outubro", "novembro", "dezembro" };
		LocalDate dataAtual = LocalDate.now();
		int mesAtual = dataAtual.getMonthValue();
		int anoAtual = dataAtual.getYear();
		String mes = meses[mesAtual - 1];
		return mes + " / " + anoAtual;
	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		return 0;
	}
}
