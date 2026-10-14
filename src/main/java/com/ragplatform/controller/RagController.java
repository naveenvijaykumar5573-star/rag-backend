package com.ragplatform.controller;

import com.ragplatform.dto.RagAskRequest;
import com.ragplatform.dto.RagAskResponse;
import com.ragplatform.service.RagService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/ask")
    public ResponseEntity<RagAskResponse> askQuestion(
            @RequestBody RagAskRequest request,
            Authentication authentication,
            @RequestHeader("Authorization") String authorization
    ) {

        RagAskResponse response =
                ragService.askQuestion(
                        request,
                        authorization
                );

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/documents")
    public ResponseEntity<Object> getDocuments(
            @RequestHeader("Authorization") String authorization
    ) {
        Object response =
                ragService.getDocuments(
                        authorization
                );

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/documents/{documentId}")
    public ResponseEntity<Object> getDocument(
            @PathVariable String documentId,
            @RequestHeader("Authorization") String authorization
    ) {
        Object response =
                ragService.getDocument(
                        documentId,
                        authorization
                );

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<Object> deleteDocument(
            @PathVariable String documentId,
            @RequestHeader("Authorization") String authorization
    ) {
        return ResponseEntity.ok(
                ragService.deleteDocument(
                        documentId,
                        authorization
                )
        );
    }

    @PostMapping(
            value = "/documents/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String authorization
    ) {

        return ResponseEntity.ok(
                ragService.uploadDocument(
                        file,
                        authorization
                )
        );
    }
}