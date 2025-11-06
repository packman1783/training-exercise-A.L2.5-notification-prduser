package org.example.spring_kafka.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO for creating an account")
public class AccountCreateDTO {
    @Schema(description = "ID of the user to whom the account belongs", example = "1")
    @NotNull
    private Long userId;

    @Schema(description = "Account title", example = "User account")
    @NotBlank
    private String title;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
