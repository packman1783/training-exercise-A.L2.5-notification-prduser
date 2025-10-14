package org.example.spring_kafka.dto;

import jakarta.validation.constraints.NotNull;

import org.openapitools.jackson.nullable.JsonNullable;

public class AccountUpdateDTO {
    @NotNull
    JsonNullable<Long> userId;

    JsonNullable<String> title;

    public JsonNullable<Long> getUserId() {
        return userId;
    }

    public void setUserId(JsonNullable<Long> userId) {
        this.userId = userId;
    }

    public JsonNullable<String> getTitle() {
        return title;
    }

    public void setTitle(JsonNullable<String> title) {
        this.title = title;
    }
}
