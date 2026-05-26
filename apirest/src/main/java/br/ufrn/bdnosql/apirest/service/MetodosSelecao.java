package br.ufrn.bdnosql.apirest.service;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class MetodosSelecao {
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
			int limiteAtual = Integer.parseInt(limite); // Pode lançar exceção

			if (limiteAtual > 0) {
				query.limit(limiteAtual);

			} else {
				// Lançar exceção
			}
		}
	}

	public static void adicionarPagina(String paginaAtual, Query query) {

		// Verificação de qual limite caso modificado
		if (paginaAtual != null && !paginaAtual.isEmpty()) {
			int paginas = Integer.parseInt(paginaAtual); // Pode lançar exceção
			paginas--; // Menos um porque as paginas começam no zero
			if (paginas >= 0) {
				query.skip(paginas);
			} else {
				// Lançar exceção
			}
		}
	}

	public static void adicionarFiltro(String filtro, Query query) {

		// Verificação de filtro
		if (filtro != null && !filtro.isEmpty()) {

			Document document = Document.parse(filtro);

			document.forEach((campo, valor) -> {

				if (valor instanceof Document operador) {

					operador.forEach((op, val) -> {

						switch (op) {

						case "$gt":
							query.addCriteria(Criteria.where(campo).gt(val));
							break;

						case "$gte":
							query.addCriteria(Criteria.where(campo).gte(val));
							break;

						case "$lt":
							query.addCriteria(Criteria.where(campo).lt(val));
							break;

						case "$regex":
							query.addCriteria(Criteria.where(campo).regex(val.toString()));
							break;
						}
					});

				} else {
					query.addCriteria(Criteria.where(campo).is(valor));
				}
			});
		}
	}

}
