package br.ufrn.bdnosql.apirest.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import br.ufrn.bdnosql.apirest.exception.custom.BadRequestException;

public class MetodosListar {
	public static void adicionarProjecao(String atributosVisiveis, Query query) {

		// Verificação de fields = atributos visíveis = projeção
		if (atributosVisiveis != null && !atributosVisiveis.isEmpty()) {
			List<String> campos = Arrays.asList(atributosVisiveis.split(","));
			for (String campo : campos) {
				if (campo.charAt(0) == '-') { // Removendo da projeção
					query.fields().exclude(campo.substring(1)); // Esse substring 1, pula o sinal negativo
				} else {
					query.fields().include(campo); // Adicionando na projeção
				}
			}
		}
	}

	public static void adicionarLimite(String limite, Query query) {

		// Verificação de qual limite caso modificado
		if (limite != null && !limite.isEmpty()) {

			try {
				int limiteAtual = Integer.parseInt(limite); 

				if (limiteAtual > 0 && limiteAtual <=1000) { //Limite máximo e mínimo;
					query.limit(limiteAtual);

				} else {
					throw new BadRequestException("O número do limite deve estar entre 1 e 1000.");
				}
			} catch (NumberFormatException ex) {
				throw new BadRequestException("O número do limite é inválido.");
			}
		}else{
			query.limit(100); //Limite padrão
		}
	}

	public static void adicionarPagina(String paginaAtual, Query query) {

		// Verificação de qual limite caso modificado
		if (paginaAtual != null && !paginaAtual.isEmpty()) {
			try {
				int paginas = Integer.parseInt(paginaAtual);
				paginas--; // Menos um porque as paginas começam no zero
				
				if (paginas >= 0) {
					query.skip(paginas);
				} else {
					throw new BadRequestException("O número da página deve ser maior que 0.");
				}
			} catch (NumberFormatException ex) {
				throw new BadRequestException("O número da página é inválido.");
			}

		}
	}

	public static void adicionarFiltro(Map<String, String> filtros, Query query) {

		filtros.forEach((campo, valor) -> {

			// maior igual que
			if (campo.endsWith("_gte")) {
				String nomeCampo = campo.replace("_gte", ""); // remove o operador do campo

				query.addCriteria(Criteria.where(nomeCampo).gte(converterValor(valor)));
			}

			// maior que
			else if (campo.endsWith("_gt")) {
				String nomeCampo = campo.replace("_gt", ""); // remove o operador do campo

				query.addCriteria(Criteria.where(nomeCampo).gt(converterValor(valor)));
			}

			// menor igual que
			else if (campo.endsWith("_lte")) {
				String nomeCampo = campo.replace("_lte", ""); // remove o operador do campo

				query.addCriteria(Criteria.where(nomeCampo).lte(converterValor(valor)));
			}

			// menor que
			else if (campo.endsWith("_lt")) {
				String nomeCampo = campo.replace("_lt", ""); // remove o operador do campo

				query.addCriteria(Criteria.where(nomeCampo).lt(converterValor(valor)));
			}

			// contém
			else if (campo.endsWith("_like")) {

				String nomeCampo = campo.replace("_like", "");

				query.addCriteria(Criteria.where(nomeCampo).regex(valor, "i"));
			}

			// igual
			else {

				query.addCriteria(Criteria.where(campo).is(converterValor(valor)));
			}

		});
	}

	private static Object converterValor(String valor) {

		try {
			return Integer.parseInt(valor);
		} catch (NumberFormatException e) {
		}

		try {
			return Double.parseDouble(valor);
		} catch (NumberFormatException e) {
		}

		if (valor.equalsIgnoreCase("true")) {
			return true;
		}

		if (valor.equalsIgnoreCase("false")) {
			return false;
		}

		return valor;
	}
}
