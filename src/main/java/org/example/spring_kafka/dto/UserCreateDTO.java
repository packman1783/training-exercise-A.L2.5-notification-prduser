package org.example.spring_kafka.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO for creating a user")
public class UserCreateDTO {

    @Schema(description = "User's firstName", example = "Bob")
    @NotBlank
    private String firstName;

    @Schema(description = "User's lastName", example = "Marley")
    @NotBlank
    private String lastName;

    @Schema(description = "User's email address", example = "bob.marley@mail.com")
    @NotBlank
    private String email;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
