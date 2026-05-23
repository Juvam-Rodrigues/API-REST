package br.ufrn.bdnosql.apirest.controller;

import java.util.List;
import java.util.Map;

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
	public Object criar(@PathVariable String collection, @RequestBody Map<String, Object> documento) {
		return service.criarDocumento(collection, documento);
	}

	@GetMapping("/{collection}")
	public List<Object> listarTodos(@PathVariable String collection) {
		return service.listarDocumentos(collection);
	}

	@GetMapping("/{collection}/{id}")
	public Object listarPorId(@PathVariable String collection, @PathVariable String id) {
		return service.listarDocumentoPorId(collection, id);
	}

	@DeleteMapping("/{collection}/{id}")
	public Object remover(@PathVariable String collection, @PathVariable String id) {
		return service.removerDocumento(collection, id);
	}

	@PutMapping("/{collection}/{id}")
	public Object atualizar(@PathVariable String collection, @PathVariable String id,
			@RequestBody Map<String, Object> documento) {

		return service.atualizarDocumento(collection, id, documento);

	}

}
