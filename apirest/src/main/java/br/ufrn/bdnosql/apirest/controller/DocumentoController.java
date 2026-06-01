package br.ufrn.bdnosql.apirest.controller;

import java.util.List;
import java.util.Map;

import br.ufrn.bdnosql.apirest.message.CustomMessage;
import org.bson.Document;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.ufrn.bdnosql.apirest.service.DocumentoService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor

public class DocumentoController {

	private final DocumentoService service;

	@PostMapping("/{collection}")
	public ResponseEntity<Object> criar(@PathVariable String collection, @RequestBody List<Map<String, Object>> documentos) {
		List<Object> resposta = service.criarDocumentos(collection, documentos);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CustomMessage(
                        HttpStatus.CREATED.value(),
                        resposta.size() + " documento(s) criado(s) na coleção " + collection + ".",
                        resposta
                )
        );
	}
	
	@GetMapping("/")
	public ResponseEntity<Object> inicioApi() {

        return ResponseEntity.ok(
                new CustomMessage(
                        HttpStatus.OK.value(),
                        "API Rest Java + Sping Boot com MongoDB. " +
                                "Crie uma coleção via POST com ou sem documentos: localhost:8080/<nome_da_colecao>. " +
                                "Consulte uma coleção via GET: localhost:8080/<nome_da_colecao>?<filtro>&fields=<>&page=<>&limit<>",
                        ""
                )
        );
	}

	@GetMapping("/{collection}")
	public ResponseEntity<List<Document>> listar(@PathVariable String collection,
            @RequestParam Map<String, String> filter, @RequestParam(required = false) String fields,
			@RequestParam(required = false) String page, @RequestParam(required = false) String limit) {

        filter.remove("fields");
        filter.remove("page");
        filter.remove("limit");

		List<Document> resultado = service.listarDocumentos(collection, filter, fields, page, limit);

		return ResponseEntity.ok(resultado);
	}

	@GetMapping("/contar-documentos/{collection}")
	public ResponseEntity<Object> contar(@PathVariable String collection) {
       	long resposta = service.contarDocumentos(collection);
		return ResponseEntity.ok(
                new CustomMessage(
                        HttpStatus.OK.value(),
                        "Contagem realizada com sucesso",
                        resposta
                )
        );
	}

	@DeleteMapping("/deletar-todos/{collection}")
	public ResponseEntity<Object> removerTodosDocumentosColecao(@PathVariable String collection) {
		Object resposta = service.removerTodosDocumentosColecao(collection);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                new CustomMessage(
                        HttpStatus.NO_CONTENT.value(),
                        "Documentos da coleção " + collection + " deletados com sucesso!",
                        resposta
                )
        );
	}

	@DeleteMapping("/{collection}/{id}")
	public ResponseEntity<Object> remover(@PathVariable String collection, @PathVariable String id) {
		Object resposta = service.removerDocumento(collection, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                new CustomMessage(
                        HttpStatus.NO_CONTENT.value(),
                        "Documento " + id + " da coleção " + collection + " deletado com sucesso!",
                        resposta
                )
        );
	}

	@PutMapping("/{collection}/{id}")
	public ResponseEntity<Object> atualizar(@PathVariable String collection, @PathVariable String id,
			@RequestBody Map<String, Object> documento) {

		Object resposta = service.atualizarDocumento(collection, id, documento);
        return ResponseEntity.ok(
                new CustomMessage(
                        HttpStatus.OK.value(),
                        "Documento " + id + " da coleção " + collection + " atualizado com sucesso!",
                        resposta
                )
        );
	}

}
