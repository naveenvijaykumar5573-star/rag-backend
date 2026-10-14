package com.ragplatform.service;

import com.ragplatform.dto.RagAskRequest;
import com.ragplatform.dto.RagAskResponse;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.core.io.ByteArrayResource;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.server.ResponseStatusException;


@Service
public class RagService {

    private final RestTemplate restTemplate;

    @Value("${python.ai.url}")
    private String pythonAiUrl;


    public RagService() {

        this.restTemplate = new RestTemplate();

    }


    // =========================================================
    // ASK QUESTION
    // =========================================================

 // =========================================================
 // ASK QUESTION
 // =========================================================

 public RagAskResponse askQuestion(
         RagAskRequest request,
         String authorization
 ) {

     try {

         HttpHeaders headers = new HttpHeaders();

         headers.setContentType(
                 MediaType.APPLICATION_JSON
         );

         headers.set(
                 "Authorization",
                 authorization
         );

         HttpEntity<RagAskRequest> entity =
                 new HttpEntity<>(
                         request,
                         headers
                 );

         return restTemplate.postForObject(
                 pythonAiUrl + "/ask",
                 entity,
                 RagAskResponse.class
         );

     } catch (HttpClientErrorException.NotFound e) {

         throw new ResponseStatusException(
                 HttpStatus.NOT_FOUND,
                 "Document not found"
         );

     } catch (HttpClientErrorException.Unauthorized e) {

         throw new ResponseStatusException(
                 HttpStatus.UNAUTHORIZED,
                 "Unauthorized"
         );

     } catch (HttpClientErrorException e) {

         throw new ResponseStatusException(
                 HttpStatus.BAD_GATEWAY,
                 "RAG service returned an error"
         );

     } catch (Exception e) {

         throw new ResponseStatusException(
                 HttpStatus.SERVICE_UNAVAILABLE,
                 "RAG service temporarily unavailable"
         );
     }
 }
 // =========================================================
 // DELETE DOCUMENT
 // =========================================================
 public Object deleteDocument(
         String documentId,
         String authorization
 ) {
     try {
         HttpHeaders headers = new HttpHeaders();
         headers.set("Authorization", authorization);

         HttpEntity<Void> entity =
                 new HttpEntity<>(headers);

         ResponseEntity<Object> response =
                 restTemplate.exchange(
                         pythonAiUrl + "/documents/" + documentId,
                         HttpMethod.DELETE,
                         entity,
                         Object.class
                 );

         return response.getBody();

     } catch (HttpClientErrorException.NotFound e) {
         throw new ResponseStatusException(
                 HttpStatus.NOT_FOUND,
                 "Document not found"
         );

     } catch (HttpClientErrorException.Unauthorized e) {
         throw new ResponseStatusException(
                 HttpStatus.UNAUTHORIZED,
                 "Unauthorized"
         );

     } catch (HttpClientErrorException e) {
         throw new ResponseStatusException(
                 HttpStatus.BAD_GATEWAY,
                 "RAG service returned an error"
         );

     } catch (Exception e) {
         throw new ResponseStatusException(
                 HttpStatus.SERVICE_UNAVAILABLE,
                 "RAG service temporarily unavailable"
         );
     }
 }
    // =========================================================
    // UPLOAD DOCUMENT
    // =========================================================

    public Object uploadDocument(
            MultipartFile file,
            String authorization
    ) {

        try {

            // -------------------------------------------------
            // 1. Validate file
            // -------------------------------------------------

            if (file == null || file.isEmpty()) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "File is required"
                );
            }


            if (file.getOriginalFilename() == null ||
                    file.getOriginalFilename().isBlank()) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Filename is required"
                );
            }


            // -------------------------------------------------
            // 2. Create multipart headers
            // -------------------------------------------------

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(
                    MediaType.MULTIPART_FORM_DATA
            );


            // Forward JWT to Python

            headers.set(
                    "Authorization",
                    authorization
            );


            // -------------------------------------------------
            // 3. Create file resource
            // -------------------------------------------------

            ByteArrayResource resource =
                    new ByteArrayResource(file.getBytes()) {

                        @Override
                        public String getFilename() {

                            return file.getOriginalFilename();
                        }
                    };


            // -------------------------------------------------
            // 4. Create multipart body
            // -------------------------------------------------

            MultiValueMap<String, Object> body =
                    new LinkedMultiValueMap<>();


            body.add(
                    "file",
                    resource
            );


            // -------------------------------------------------
            // 5. Create HTTP request
            // -------------------------------------------------

            HttpEntity<MultiValueMap<String, Object>> entity =
                    new HttpEntity<>(
                            body,
                            headers
                    );


            // -------------------------------------------------
            // 6. Send file to Python
            // -------------------------------------------------

            ResponseEntity<Object> response =
                    restTemplate.postForEntity(
                            pythonAiUrl + "/documents/upload",
                            entity,
                            Object.class
                    );
        

            // -------------------------------------------------
            // 7. Return Python response
            // -------------------------------------------------

            return response.getBody();



    } catch (HttpClientErrorException.Unauthorized e) {

        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Unauthorized"
        );

    } catch (HttpClientErrorException.BadRequest e) {

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid document upload"
        );

    } catch (HttpClientErrorException.NotFound e) {

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Document not found"
        );

    } catch (HttpClientErrorException e) {

        // Python returned another HTTP error
        throw new ResponseStatusException(
                HttpStatus.BAD_GATEWAY,
                "RAG service returned an error"
        );

    } catch (ResponseStatusException e) {

        // Preserve our own validation errors
        throw e;

    } catch (Exception e) {

        // Do not expose internal exception details
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "RAG service temporarily unavailable"
        );
    }
    }


    // =========================================================
    // GET DOCUMENTS
    // =========================================================

    public Object getDocuments(
            String authorization
    ) {
        try {
            HttpHeaders headers = new HttpHeaders();

            // Forward user's JWT to Python
            headers.set(
                    "Authorization",
                    authorization
            );

            HttpEntity<Void> entity =
                    new HttpEntity<>(headers);

            return restTemplate.exchange(
                    pythonAiUrl + "/documents",
                    HttpMethod.GET,
                    entity,
                    Object.class
            ).getBody();

        } catch (HttpClientErrorException.Unauthorized e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Unauthorized"
            );

        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Documents endpoint not found"
            );

        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "RAG service returned an error"
            );

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "RAG service temporarily unavailable"
            );
        }
    }
 public Object getDocument(
         String documentId,
         String authorization
 ) {

     try {

         HttpHeaders headers = new HttpHeaders();

         headers.set(
                 "Authorization",
                 authorization
         );

         HttpEntity<Void> entity =
                 new HttpEntity<>(headers);

         ResponseEntity<Object> response =
                 restTemplate.exchange(
                         pythonAiUrl + "/documents/" + documentId,
                         HttpMethod.GET,
                         entity,
                         Object.class
                 );

         return response.getBody();

     } catch (HttpClientErrorException.NotFound e) {

         throw new ResponseStatusException(
                 HttpStatus.NOT_FOUND,
                 "Document not found"
         );

     } catch (HttpClientErrorException.Unauthorized e) {

         throw new ResponseStatusException(
                 HttpStatus.UNAUTHORIZED,
                 "Unauthorized"
         );

     } catch (Exception e) {
    	    throw new ResponseStatusException(
    	            HttpStatus.SERVICE_UNAVAILABLE,
    	            "RAG service temporarily unavailable"
    	    );
    	}
 }
}