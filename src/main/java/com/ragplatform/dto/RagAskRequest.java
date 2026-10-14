package com.ragplatform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RagAskRequest {

    private String question;

    @JsonProperty("document_id")
    private String documentId;

    public RagAskRequest() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}