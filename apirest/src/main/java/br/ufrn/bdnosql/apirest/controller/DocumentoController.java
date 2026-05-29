package br.ufrn.bdnosql.apirest.controller;

import java.util.List;
import java.util.Map;
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
		Object resposta = service.criarDocumentos(collection, documentos);

		return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
	}
	
	@GetMapping(value = "/", produces = "text/html")
	public ResponseEntity<String> paginaHtml() {

	    String html = """
	        <html>
	            <body>
	                <h1>API funcionando</h1>
	                <h2 style=color:green>Status OK</h2>
					<p>Coloque: localhost:8080/&lt;nome-da-colecao&gt;/&lt;filtros&gt;</p>
	            </body>
	        </html>
	        """;

	    return ResponseEntity
	            .status(HttpStatus.OK)
	            .body(html);
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

	@DeleteMapping("/deletar-todos/{collection}")
	public ResponseEntity<Object> removerTodosDocumentosSelecao(@PathVariable String collection) {
		Object resposta = service.removerTodosDocumentosSelecao(collection);

	    return ResponseEntity.ok(resposta);
	}

	
	@DeleteMapping("/{collection}/{id}")
	public ResponseEntity<Object> remover(@PathVariable String collection, @PathVariable String id) {
		Object resposta = service.removerDocumento(collection, id);
		return ResponseEntity.ok().body(resposta);
	}

	@PutMapping("/{collection}/{id}")
	public ResponseEntity<Object> atualizar(@PathVariable String collection, @PathVariable String id,
			@RequestBody Map<String, Object> documento) {

		Object resposta = service.atualizarDocumento(collection, id, documento);
		return ResponseEntity.ok().body(resposta);
	}

}
