package br.ufrn.bdnosql.apirest.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.bson.Document;

import br.ufrn.bdnosql.apirest.message.CustomMessage;
import br.ufrn.bdnosql.apirest.service.DocumentoService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor

public class DocumentoController {

	private final DocumentoService service;

        @GetMapping("/")
	public ResponseEntity<Object> inicioApi() {

        return ResponseEntity.ok(
                new CustomMessage(
                        HttpStatus.OK.value(),
                        "API Rest Java + Sping Boot com MongoDB. " +
                                "Crie uma coleção via POST: localhost:8080/<nome_da_colecao>. " +
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

	@DeleteMapping("/deletar-todos/{collection}")
	public ResponseEntity<Object> removerTodosDocumentosColecao(@PathVariable String collection) {
		Object resposta = service.removerTodosDocumentosColecao(collection);

        return ResponseEntity.ok(
                new CustomMessage(
                        HttpStatus.OK.value(),
                        "Documentos da coleção " + collection + " deletados com sucesso!",
                        resposta
                )
        );
	}

	@DeleteMapping("/{collection}/{id}")
	public ResponseEntity<Object> remover(@PathVariable String collection, @PathVariable String id) {
		Object resposta = service.removerDocumento(collection, id);
        return ResponseEntity.ok(
                new CustomMessage(
                        HttpStatus.OK.value(),
                        "Documento " + id + " da coleção " + collection + " deletado com sucesso!",
                        resposta
                )
        );
	}

	@PatchMapping("/{collection}/{id}")
	public ResponseEntity<Object> atualizarCampos(@PathVariable String collection, @PathVariable String id,
			@RequestBody Map<String, Object> documento) {

		Object resposta = service.atualizarCamposEspecificos(collection, id, documento);
        return ResponseEntity.ok(
                new CustomMessage(
                        HttpStatus.OK.value(),
                        "Documento " + id + " da coleção " + collection + " atualizado com sucesso!",
                        resposta
                )
        );
	}
	
	@PutMapping("/{collection}/{id}")
	public ResponseEntity<Object> atualizarDocumento(@PathVariable String collection, @PathVariable String id,
			@RequestBody Map<String, Object> documento) {

		Object resposta = service.atualizarDocumentoInteiro(collection, id, documento);
        return ResponseEntity.ok(
                new CustomMessage(
                        HttpStatus.OK.value(),
                        "Documento " + id + " da coleção " + collection + " atualizado com sucesso!",
                        resposta
                )
        );
	}

}
