package model.entidade;

public class Funcionario {
	private String cpf;
    private String nome;
    private String cbo;
    private String funcao;
    private Empregador empregador;
    
	public Funcionario() {
		super();
	}

	public Funcionario(String cpf, String nome, String cbo, String funcao, Empregador empregador) {
		this.cpf = cpf;
		this.nome = nome;
		this.cbo = cbo;
		this.funcao = funcao;
		this.empregador = empregador;
	}
	
	public String getCpf() {
		return cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getCbo() {
		return cbo;
	}
	
	public void setCbo(String cbo) {
		this.cbo = cbo;
	}
	
	public String getFuncao() {
		return funcao;
	}
	
	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}
	
	public Empregador getEmpregador() {
		return empregador;
	}
	
	public void setEmpregador(Empregador empregador) {
		this.empregador = empregador;
	}
}
