package com.example.clinic.app.document.controller;

import java.net.URI;

import com.example.clinic.app.document.dto.DocumentCreationDTO;
import com.example.clinic.model.PageArgument;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.clinic.app.document.dto.DocumentDto;
import com.example.clinic.app.document.entity.Document;
import com.example.clinic.app.document.mapper.DocumentMapper;
import com.example.clinic.app.document.service.DocumentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentMapper documentMapper;

    @PostMapping
    public ResponseEntity<DocumentCreationDTO> createDocument(@RequestBody DocumentCreationDTO documentDto, @RequestParam Long patientId) {
        Document document = documentService.createDocument(documentDto, patientId);
        return ResponseEntity.created(URI.create("/api/documents/" + document.getId())).body(documentDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentDto> updateDocument(@PathVariable Long id, @RequestBody DocumentDto documentDto) {
        Document updatedDocument = documentService.updateDocument(id, documentDto);
        DocumentDto updatedDocumentDto = documentMapper.entityToDocumentDto(updatedDocument);
        return ResponseEntity.ok(updatedDocumentDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok("Document with id " + id + " successfully deleted.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDto> getDocumentById(@PathVariable Long id) {
        Document document = documentService.getDocumentById(id);
        DocumentDto documentDto = documentMapper.entityToDocumentDto(document);
        return ResponseEntity.ok(documentDto);
    }

    @GetMapping
    public ResponseEntity<Page<DocumentDto>> getDocuments(
            PageArgument page
    ) {
        Page<Document> documentPage = documentService.getDocuments(page.getPageRequest());
        Page<DocumentDto> response = documentPage.map(documentMapper::entityToDocumentDto);
        return ResponseEntity.ok(response);
    }
}
