package org.example.spring_kafka.event;

public class NotificationEvent {
    private OperationType operation;
    private String email;

    public NotificationEvent() {
    }

    public NotificationEvent(OperationType operation, String email) {
        this.operation = operation;
        this.email = email;
    }

    public OperationType getOperation() {
        return operation;
    }

    public void setOperation(OperationType operation) {
        this.operation = operation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
