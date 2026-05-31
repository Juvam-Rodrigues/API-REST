package br.ufrn.bdnosql.apirest.service;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.bson.Document;
import org.springframework.stereotype.Service;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import br.ufrn.bdnosql.apirest.exception.custom.BadRequestException;
import br.ufrn.bdnosql.apirest.exception.custom.NotFoundException;

import java.util.HashMap;
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

	public List<Document> listarDocumentos(String collection, Map<String, String> filtro, String atributosVisiveis,
			String paginaAtual, String limite) {

		Query query = new Query();

		MetodosListar.adicionarFiltro(filtro, query);
		MetodosListar.adicionarProjecao(atributosVisiveis, query);
		MetodosListar.adicionarLimite(limite, query);
		MetodosListar.adicionarPagina(paginaAtual, query);

		List<Document> documentosListados = mongoTemplate.find(query, Document.class, collection);
		for (Document documentoAtual : documentosListados) {

			ObjectId id = documentoAtual.getObjectId("_id");

			if (id != null) {
				documentoAtual.put("_id", id.toHexString());
			}
		}

		return documentosListados;

	}

	public Object removerDocumento(String collection, String id) {

		try {
			ObjectId objectId = new ObjectId(id); // Como o ID está armazenado no banco
			Query query = Query.query(Criteria.where("_id").is(objectId));
			DeleteResult resultado = mongoTemplate.remove(query, collection);

			if (resultado.getDeletedCount() == 0) {
				throw new NotFoundException("Documento não encontrado na collection " + collection + ".");
			}

			return resultado;

		} catch (IllegalArgumentException ex) {
			throw new BadRequestException("O ID do documento é inválido.");
		}

	}

	public Object removerTodosDocumentosColecao(String collection) {
		Query query = new Query();
		return mongoTemplate.remove(query, collection);
	}

	public Object atualizarDocumento(String collection, String id, Map<String, Object> documento) {

		try {
			if (documento == null || documento.isEmpty()) {
				throw new BadRequestException("Body está vazio");
			}

			ObjectId objectId = new ObjectId(id); // Como o ID está armazenado no banco

			Query query = Query.query(Criteria.where("_id").is(objectId));

			Update update = new Update();

			documento.forEach(update::set);

			UpdateResult resultado = mongoTemplate.updateFirst(query, update, collection);

			if (resultado.getMatchedCount() == 0) {
				throw new NotFoundException("Documento não encontrado na collection " + collection + ".");
			}

			Map<String, String> filtro = new HashMap<>();
			filtro.put("_id", id);
			return listarDocumentos(collection, filtro, "", "", "");
			
		} catch (IllegalArgumentException ex) {
			throw new BadRequestException("O ID do documento é inválido.");

		}
	}

	public long contarDocumentos(String collection) {
		return (mongoTemplate.count(new Query(), collection));
	}
}
