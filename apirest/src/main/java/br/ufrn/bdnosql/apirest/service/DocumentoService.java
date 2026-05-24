package br.ufrn.bdnosql.apirest.service;


import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import br.ufrn.bdnosql.apirest.exception.custom.BadRequestException;
import br.ufrn.bdnosql.apirest.exception.custom.NotFoundException;

import java.util.Map;
import java.util.List;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DocumentoService {
	
	private final MongoTemplate mongoTemplate;

    public DocumentoService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
	
	
    public Object criarDocumento(String collection, Map<String, Object> documento) {
    	
    	
        return mongoTemplate.save(documento, collection);
    }

    public List<Object> listarDocumentos(String collection) {
        return mongoTemplate.findAll(Object.class, collection);
    }
    
    public Object listarDocumentoPorId(String collection, String id) {
    	Object resultado = mongoTemplate.findById(id, Object.class, collection);
    	
    	if(resultado == null) {
    		throw new NotFoundException(
    	            "Documento não encontrado na collection " + collection);
    	}
    	
    	return resultado;

    }
    
    public DeleteResult removerDocumento(String collection, String id) {
    	Query query = Query.query(Criteria.where("_id").is(id));
    	
    	DeleteResult resultado = mongoTemplate.remove(query, collection);
    	
    	if (resultado.getDeletedCount() == 0) {
            throw new NotFoundException(
                "Documento não encontrado na collection " + collection);
    	}
    	
    	return resultado;

    }
    
    public Object atualizarDocumento(String collection, String id, Map<String, Object> documento) {

    	if (documento == null || documento.isEmpty()) {
	        throw new BadRequestException("Body está vazio");
	    }
    	
        Query query = new Query(Criteria.where("_id").is(id));

        Update update = new Update();

        documento.forEach(update::set);

        UpdateResult resultado = mongoTemplate.updateFirst(query, update, collection);
        
        if (resultado.getMatchedCount() == 0) {
            throw new NotFoundException(
                "Documento não encontrado na collection " + collection
            );
        }

        return listarDocumentoPorId(collection, id);
    }
    
}
