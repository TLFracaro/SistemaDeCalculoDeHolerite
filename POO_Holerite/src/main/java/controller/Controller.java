package controller;

import model.entidade.Endereco;
import viacep.ViaCEP;
import viacep.ViaCEPException;

public class Controller {

	public Endereco buscarCep(String cep) {
		Endereco enderecoComple = new Endereco();
		ViaCEP viaCep = new ViaCEP();
		try {
			viaCep.buscar(cep);
			enderecoComple.setLogradouro(viaCep.getLogradouro());
			enderecoComple.setBairro(viaCep.getBairro());
			enderecoComple.setCidade(viaCep.getLocalidade());
			enderecoComple.setUf(viaCep.getUf());
			enderecoComple.setPais("Brasil");
		} catch (ViaCEPException e1) {

		}
		return enderecoComple;
	}

	public static double[] calcularINSS(double salarioBruto) {
		double[] inss = new double[3];
		double aliquota = 0.0;

		if (salarioBruto <= 1320.0) {
			inss[2] = salarioBruto * 0.075;
			aliquota = 7.5;
		} else if (salarioBruto <= 2571.29) {
			double faixa1 = 1320.0 * 0.075;
			double faixa2 = (salarioBruto - 1320.0) * 0.09;
			inss[2] = faixa1 + faixa2;
			aliquota = 9.0;
		} else if (salarioBruto <= 3856.94) {
			double faixa1 = 1320.0 * 0.075;
			double faixa2 = (2571.29 - 1320.0) * 0.09;
			double faixa3 = (salarioBruto - 2571.29) * 0.12;
			inss[2] = faixa1 + faixa2 + faixa3;
			aliquota = 12.0;
		} else if (salarioBruto <= 7507.49) {
			double faixa1 = 1320.0 * 0.075;
			double faixa2 = (2571.29 - 1320.0) * 0.09;
			double faixa3 = (3856.94 - 2571.29) * 0.12;
			double faixa4 = (salarioBruto - 3856.94) * 0.14;
			inss[2] = faixa1 + faixa2 + faixa3 + faixa4;
			aliquota = 14.0;
		} else {
			double faixa1 = 1320.0 * 0.075;
			double faixa2 = (2571.29 - 1320.0) * 0.09;
			double faixa3 = (3856.94 - 2571.29) * 0.12;
			double faixa4 = (7507.49 - 3856.94) * 0.14;
			inss[2] = faixa1 + faixa2 + faixa3 + faixa4;
			aliquota = 14.0;
		}

		double proventos = 0;

		inss[0] = aliquota;
		inss[1] = proventos;

		return inss;
	}

	public static double[] calcularIRRF(double salarioBruto, double inss) {
	    double[] irrf = new double[4];
	    double baseCalculo = salarioBruto - inss;
	    double aliquota = 0.0;
	    double faixa = 0;
	    double desconto = 0;

	    if (baseCalculo <= 2112.0) {
	        aliquota = 0.0;
	        desconto = 0;
	        faixa = 1;
	    } else if (baseCalculo <= 2826.65) {
	        aliquota = 7.5;
	        desconto = baseCalculo * 0.075 - 158.40;
	        faixa = 2;
	    } else if (baseCalculo <= 3751.05) {
	        aliquota = 15.0;
	        desconto = baseCalculo * 0.15 - 370.40;
	        faixa = 3;
	    } else if (baseCalculo <= 4664.68) {
	        aliquota = 22.5;
	        desconto = baseCalculo * 0.225 - 651.73;
	        faixa = 4;
	    } else {
	        aliquota = 27.5;
	        desconto = baseCalculo * 0.275 - 884.96;
	        faixa = 5;
	    }

	    irrf[0] = aliquota;
	    irrf[1] = faixa;
	    irrf[2] = desconto;
	    irrf[3] = baseCalculo;

	    return irrf;
	}

	public static double[] calcularFGTS(double salarioBruto, double desconto) {
		double[] fgts = new double[3];
		fgts[0] = desconto;
		fgts[1] = salarioBruto * desconto;
		return fgts;
	}
}