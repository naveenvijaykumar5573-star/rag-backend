package com.ragplatform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RagSource {

    @JsonProperty("document_id")
    private String documentId;

    private String filename;

    @JsonProperty("chunk_id")
    private Long chunkId;

    @JsonProperty("chunk_index")
    private Integer chunkIndex;

    private Double distance;

    public RagSource() {
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getChunkId() {
        return chunkId;
    }

    public void setChunkId(Long chunkId) {
        this.chunkId = chunkId;
    }

    public Integer getChunkIndex() {
        return chunkIndex;
    }

    public void setChunkIndex(Integer chunkIndex) {
        this.chunkIndex = chunkIndex;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}