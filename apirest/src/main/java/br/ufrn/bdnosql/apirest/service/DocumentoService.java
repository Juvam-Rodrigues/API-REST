package br.ufrn.bdnosql.apirest.service;


import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import br.ufrn.bdnosql.apirest.exception.custom.BadRequestException;


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
    	
    	if (!collection.matches("^[a-zA-Z0-9_]+$")) {
    	    throw new BadRequestException("Nome de collection contém caracteres inválidos");
    	}
    	
        return mongoTemplate.save(documento, collection);
    }

    public List<Object> listarDocumentos(String collection) {
        return mongoTemplate.findAll(Object.class, collection);
    }
    
    public Object listarDocumentoPorId(String collection, String id) {
    	return mongoTemplate.findById(id, Object.class, collection);

    }
    
    public Object removerDocumento(String collection, String id) {
    	Query query = Query.query(Criteria.where("_id").is(id));
    	
    	return mongoTemplate.remove(query, collection);

    }
    
    public Object atualizarDocumento(String collection, String id, Map<String, Object> documento) {

    	if (documento == null || documento.isEmpty()) {
	        throw new BadRequestException("Body não pode estar vazio");
	    }
    	
        Query query = new Query(Criteria.where("_id").is(id));

        Update update = new Update();

        documento.forEach(update::set);

        mongoTemplate.updateFirst(query, update, collection);


        return listarDocumentoPorId(collection, id);
    }
    
}
