package com.card.cardapi.taskamo.utils;

public enum StatusCode {
    SUCCESS("SUCCESS", "SUCCESSFUL"),
    ERROR("FAILED", "AN ERROR OCCURRED"),
    CONFLICT("CONFLICT", "Relation already exists");


    private String code;
    private String description;

    StatusCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
