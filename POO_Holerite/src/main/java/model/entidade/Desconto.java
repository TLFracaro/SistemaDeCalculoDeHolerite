package model.entidade;

public class Desconto {
	private String cod;
	private String descricao;
	private double referencia;
	private double proventos;
	private double descontos;
	
	public Desconto() {
		super();
	}

	public Desconto(String cod, String descricao, double referencia, double proventos, double descontos) {
		this.cod = cod;
		this.descricao = descricao;
		this.referencia = referencia;
		this.proventos = proventos;
		this.descontos = descontos;
	}

	public String getCod() {
		return cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public double getReferencia() {
		return referencia;
	}

	public void setReferencia(double referencia) {
		this.referencia = referencia;
	}

	public double getProventos() {
		return proventos;
	}

	public void setProventos(double proventos) {
		this.proventos = proventos;
	}

	public double getDescontos() {
		return descontos;
	}

	public void setDescontos(double descontos) {
		this.descontos = descontos;
	}

	@Override
	public String toString() {
		return "Desconto [cod=" + cod + ", descricao=" + descricao + ", referencia=" + referencia + ", proventos="
				+ proventos + ", descontos=" + descontos + "]";
	}
	
}
