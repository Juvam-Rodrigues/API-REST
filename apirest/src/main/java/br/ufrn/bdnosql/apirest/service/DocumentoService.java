package br.ufrn.bdnosql.apirest.service;

//import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import br.ufrn.bdnosql.apirest.exception.custom.BadRequestException;

import java.util.Map;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DocumentoService {

	private final MongoTemplate mongoTemplate;

	public Object criarDocumento(String collection, Map<String, Object> documento) {
		return mongoTemplate.save(documento, collection);
	}

	/*
	 * public List<Object> listarDocumentos(String collection) { return
	 * mongoTemplate.findAll(Object.class, collection); }
	 * 
	 * public Object listarDocumentoPorId(String collection, String id) { return
	 * mongoTemplate.findById(id, Object.class, collection);
	 * 
	 * }
	 */

	public List<Object> listarDocumentos(String collection, String filtro, String atributosVisiveis) {

		Query query = new Query();

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

		// Verificação de fields = atributos visíveis = projeção
		if (atributosVisiveis != null && !atributosVisiveis.isEmpty()) {
			List<String> campos = Arrays.asList(atributosVisiveis.split(","));
			for (String campo : campos) {
				if(campo.charAt(0) == '-') { //Removendo da projeção
				    query.fields().exclude(campo.substring(1)); //Esse substring 1, pula o sinal negativo
				}else {
					query.fields().include(campo); //Adicionando na projeção
				}
			}
		}

		System.out.println("QUERY: " + query.toString());
		return mongoTemplate.find(query, Object.class, collection);
	}

	public Object removerDocumento(String collection, String id) {
		Query query = Query.query(Criteria.where("_id").is(id));

		return mongoTemplate.remove(query, collection);

	}

	public Object atualizarDocumento(String collection, String id, Map<String, Object> documento) {

		if (documento == null || documento.isEmpty()) {
			throw new BadRequestException("Body está vazio");
		}

		Query query = new Query(Criteria.where("_id").is(id));

		Update update = new Update();

		documento.forEach(update::set);

		mongoTemplate.updateFirst(query, update, collection);

		return listarDocumentos(collection, id, "");
	}

}
