package util;

import org.apache.commons.validator.routines.EmailValidator;

public class Validar {

	public static boolean validarCBO(String cbo) {
		return cbo.length() == 6 && cbo.matches("\\d+");
	}

	public static boolean validarCNPJ(String cnpj) {
		try {
			cnpj = cnpj.replaceAll("\\D", "");

			if (cnpj.length() != 14) {
				return false;
			}

			int[] digitos = new int[14];
			for (int i = 0; i < 14; i++) {
				digitos[i] = Integer.parseInt(cnpj.substring(i, i + 1));
			}

			int soma = 0;
			int[] peso1 = { 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
			for (int i = 0; i < 12; i++) {
				soma += digitos[i] * peso1[i];
			}

			int digito1 = 11 - (soma % 11);
			if (digito1 >= 10) {
				digito1 = 0;
			}

			soma = 0;
			int[] peso2 = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
			for (int i = 0; i < 13; i++) {
				soma += digitos[i] * peso2[i];
			}

			int digito2 = 11 - (soma % 11);
			if (digito2 >= 10) {
				digito2 = 0;
			}
			return digito1 == digitos[12] && digito2 == digitos[13];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean validarCPF(String cpf) {
		try {
			cpf = cpf.replaceAll("\\D", "");
			if (cpf.length() != 11) {
				return false;
			}
			if (cpf.matches("(\\d)\\1*")) {
				return false;
			}
			int[] digitos = new int[11];
			for (int i = 0; i < 11; i++) {
				digitos[i] = Integer.parseInt(cpf.substring(i, i + 1));
			}
			int soma = 0;
			for (int i = 0; i < 9; i++) {
				soma += digitos[i] * (10 - i);
			}
			int resto = soma % 11;
			int digito1 = (resto < 2) ? 0 : (11 - resto);

			soma = 0;
			for (int i = 0; i < 10; i++) {
				soma += digitos[i] * (11 - i);
			}
			resto = soma % 11;
			int digito2 = (resto < 2) ? 0 : (11 - resto);
			return (digitos[9] == digito1) && (digitos[10] == digito2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean validaremail(String email) {
		if (email == null || email.trim().isEmpty()) {
			return false;
		}
		if (email.length() > 255) {
			return false;
		}
		if (email.matches(".*[A-Z].*")) {
			return false;
		}
		if (!email.matches(
				"^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")) {
			return false;
		}
		String[] parts = email.split("@");
		String domain = parts[1];
		if (!domain.matches("^[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
			return false;
		}
		EmailValidator validator = EmailValidator.getInstance(false);
		return validator.isValid(email);
	}
}
