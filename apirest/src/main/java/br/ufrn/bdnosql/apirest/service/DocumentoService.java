package br.ufrn.bdnosql.apirest.service;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import br.ufrn.bdnosql.apirest.exception.custom.BadRequestException;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DocumentoService {

	private final MongoTemplate mongoTemplate;

	public List<Object> criarDocumentos(String collection, List<Map<String, Object>> documentos) {
		List<Object> documentosAdicionados = new ArrayList<>();
		for (Map<String, Object> documento : documentos) {
			documentosAdicionados.add(mongoTemplate.save(documento, collection));
		}
		return documentosAdicionados;
	}

	public List<Object> listarDocumentos(String collection, String filtro, String atributosVisiveis, String paginaAtual,
			String limite) {

		Query query = new Query();

		MetodosSelecao.adicionarFiltro(filtro, query);
		MetodosSelecao.adicionarProjecao(atributosVisiveis, query);
		MetodosSelecao.adicionarLimite(limite, query);
		MetodosSelecao.adicionarPagina(paginaAtual, query);

		return mongoTemplate.find(query, Object.class, collection);

	}

	public Object removerDocumento(String collection, String id) {

		ObjectId objectId = new ObjectId(id); // Como o ID está armazenado no banco

		Query query = Query.query(Criteria.where("_id").is(objectId));

		return mongoTemplate.remove(query, collection);

	}
	
	public Object removerTodosDocumentosSelecao(String collection) {
		  Query query = new Query();
		  return mongoTemplate.remove(query, collection);
	}
	
	public Object atualizarDocumento(String collection, String id, Map<String, Object> documento) {

		if (documento == null || documento.isEmpty()) {
			throw new BadRequestException("Body está vazio");
		}

		ObjectId objectId = new ObjectId(id); // Como o ID está armazenado no banco

		Query query = Query.query(Criteria.where("_id").is(objectId));

		Update update = new Update();

		documento.forEach(update::set);

		mongoTemplate.updateFirst(query, update, collection);

		return listarDocumentos(collection, id, "", "", "");
	}
}
