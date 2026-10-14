package com.ragplatform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RagAskResponse {

    private String question;

    @JsonProperty("tenant_id")
    private String tenantId;

    private String answer;

    private List<RagSource> sources;

    public RagAskResponse() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<RagSource> getSources() {
        return sources;
    }

    public void setSources(List<RagSource> sources) {
        this.sources = sources;
    }
}