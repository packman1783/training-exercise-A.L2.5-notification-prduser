package org.example.spring_kafka.event;

public class NotificationEvent {
    private String operation;
    private String email;

    public NotificationEvent() {
    }

    public NotificationEvent(String operation, String email) {
        this.operation = operation;
        this.email = email;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
