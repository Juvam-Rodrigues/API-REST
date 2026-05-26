package br.ufrn.bdnosql.apirest.controller;

import java.util.List;
import java.util.Map;

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

	/*
	 * @GetMapping("/{collection}") public ResponseEntity<List<Object>>
	 * listarTodos(@PathVariable String collection) { List<Object> resposta =
	 * service.listarDocumentos(collection); return
	 * ResponseEntity.ok().body(resposta); }
	 */

	@GetMapping("/{collection}")
	public ResponseEntity<List<Object>> listarTodos(@PathVariable String collection,
			@RequestParam(required = false) String query, @RequestParam(required = false) String fields, 
			@RequestParam(required = false) String page, @RequestParam(required = false) String limit) {

		List<Object> resultado = service.listarDocumentos(collection, query, fields, page, limit);

		return ResponseEntity.ok(resultado);
	}

	/*
	 * @GetMapping("/{collection}/{id}") public Object listarPorId(@PathVariable
	 * String collection, @PathVariable String id) { Object resposta =
	 * service.listarDocumentoPorId(collection, id); return
	 * ResponseEntity.ok().body(resposta); }
	 */

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
