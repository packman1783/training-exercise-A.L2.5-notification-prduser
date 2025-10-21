package org.example.spring_kafka.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import org.openapitools.jackson.nullable.JsonNullable;

@Schema(description = "DTO for updating a user")
public class UserUpdateDTO {
    private JsonNullable<String> firstName;

    private JsonNullable<String> lastName;

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
