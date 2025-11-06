package org.example.spring_kafka.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;

import org.openapitools.jackson.nullable.JsonNullable;

@Schema(description = "DTO for updating an account")
public class AccountUpdateDTO {
    @Schema(description = "User ID (nullable if not updated)", example = "1")
    @NotNull
    private JsonNullable<Long> userId;

    @Schema(description = "Account title (nullable if not updated)", example = "Updated account title")
    private JsonNullable<String> title;

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
