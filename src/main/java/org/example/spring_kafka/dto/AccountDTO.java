package org.example.spring_kafka.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Account in the system")
public class AccountDTO {
    @Schema(description = "Unique account identifier", example = "1001")
    private Long id;

    @Schema(description = "Associated user ID", example = "1")
    private Long userId;

    @Schema(description = "User's first name", example = "Max")
    private String userFirstName;

    @Schema(description = "User's last name", example = "Mad")
    private String userLastName;

    @Schema(description = "Account title", example = "Main savings account")
    private String title;

    @Schema(description = "User's email", example = "max.mad@mail.com")
    private String userEmail;

    @Schema(description = "Account creation date", example = "2025-11-06")
    private LocalDate createdAt;

    @Schema(description = "Date when account was last updated", example = "2025-12-01")
    private LocalDate updatedAt;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
}
