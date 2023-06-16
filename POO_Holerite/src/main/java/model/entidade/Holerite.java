package model.entidade;

import java.util.List;

public class Holerite {
	private int id;
	private List<Desconto> desconto;
	private String mensagem;
	private double totVencimento;
	private double totDesconto;
	private double valorLiquido;
	private Double salarioBase;
	private double baseInss;
	private double baseIrff;
	private double baseFgts;
	private double faixaIrrf;
	private Funcionario funcionario;
	
	public Holerite() {
		super();
	}
	
	public Holerite(int id, List<Desconto> desconto, String mensagem, double totVencimento, double totDesconto,
			double valorLiquido, double salarioBase, double baseInss, double baseIrff, double baseFgts,
			double faixaIrrf, Funcionario funcionario) {
		super();
		this.id = id;
		this.desconto = desconto;
		this.mensagem = mensagem;
		this.totVencimento = totVencimento;
		this.totDesconto = totDesconto;
		this.valorLiquido = valorLiquido;
		this.salarioBase = salarioBase;
		this.baseInss = baseInss;
		this.baseIrff = baseIrff;
		this.baseFgts = baseFgts;
		this.faixaIrrf = faixaIrrf;
		this.funcionario = funcionario;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<Desconto> getDesconto() {
		return desconto;
	}
	public void setDesconto(List<Desconto> desconto) {
		this.desconto = desconto;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public double getTotVencimento() {
		return totVencimento;
	}
	public void setTotVencimento(double totVencimento) {
		this.totVencimento = totVencimento;
	}
	public double getTotDesconto() {
		return totDesconto;
	}
	public void setTotDesconto(double totDesconto) {
		this.totDesconto = totDesconto;
	}
	public double getValorLiquido() {
		return valorLiquido;
	}
	public void setValorLiquido(double valorLiquido) {
		this.valorLiquido = valorLiquido;
	}
	public Double getSalarioBase() {
		return salarioBase;
	}
	public void setSalarioBase(Double salarioBase) {
		this.salarioBase = salarioBase;
	}
	public double getBaseInss() {
		return baseInss;
	}
	public void setBaseInss(double baseInss) {
		this.baseInss = baseInss;
	}
	public double getBaseIrff() {
		return baseIrff;
	}
	public void setBaseIrff(double baseIrff) {
		this.baseIrff = baseIrff;
	}
	public double getBaseFgts() {
		return baseFgts;
	}
	public void setBaseFgts(double baseFgts) {
		this.baseFgts = baseFgts;
	}
	public double getFaixaIrrf() {
		return faixaIrrf;
	}
	public void setFaixaIrrf(double faixaIrrf) {
		this.faixaIrrf = faixaIrrf;
	}
	public Funcionario getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
}
