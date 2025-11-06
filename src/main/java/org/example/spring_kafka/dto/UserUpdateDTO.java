package org.example.spring_kafka.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import org.openapitools.jackson.nullable.JsonNullable;

@Schema(description = "DTO for updating user details. Supports partial updates.")
public class UserUpdateDTO {
    @Schema(description = "Updated firstName (nullable if not changing)", example = "Bill")
    private JsonNullable<String> firstName;

    @Schema(description = "Updated lastName (nullable if not changing)", example = "Cipher")
    private JsonNullable<String> lastName;

    @Schema(description = "Updated email (nullable if not changing)", example = "bill.cipher@mail.com")
    private JsonNullable<String> email;

    public JsonNullable<String> getFirstName() {
        return firstName;
    }

    public void setFirstName(JsonNullable<String> firstName) {
        this.firstName = firstName;
    }

    public JsonNullable<String> getEmail() {
        return email;
    }

    public void setEmail(JsonNullable<String> email) {
        this.email = email;
    }

    public JsonNullable<String> getLastName() {
        return lastName;
    }

    public void setLastName(JsonNullable<String> lastName) {
        this.lastName = lastName;
    }
}
