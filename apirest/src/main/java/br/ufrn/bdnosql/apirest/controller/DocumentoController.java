package br.ufrn.bdnosql.apirest.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.bdnosql.apirest.service.DocumentoService;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class DocumentoController {

	private final DocumentoService service;

	public DocumentoController(DocumentoService service) {
		this.service = service;
	}

	@PostMapping("/{collection}")
	public ResponseEntity<Object> criar(@PathVariable String collection, @RequestBody Map<String, Object> documento) {
		Object resposta = service.criarDocumento(collection, documento);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
	}

	@GetMapping("/{collection}")
	public ResponseEntity<List<Object>> listarTodos(@PathVariable String collection) {
		List<Object> resposta = service.listarDocumentos(collection);
		return ResponseEntity.ok().body(resposta);
	}

	@GetMapping("/{collection}/{id}")
	public Object listarPorId(@PathVariable String collection, @PathVariable String id) {
		Object resposta = service.listarDocumentoPorId(collection, id);
		return ResponseEntity.ok().body(resposta);
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
