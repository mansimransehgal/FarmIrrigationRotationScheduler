
package com.farm.model;

public class IrrigationRequest {

    private String requestId;
    private Field field;
    private String priority;
    private String status;

    // Constructor
    public IrrigationRequest(String requestId, Field field,
                             String priority) {

        this.requestId = requestId;
        this.field = field;
        this.priority = priority;
        this.status = "PENDING";
    }

    // Getters
    public String getRequestId() {
        return requestId;
    }

    public Field getField() {
        return field;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    // Update request status
    public void setStatus(String status) {
        this.status = status;
    }

    // Display request
    public void displayRequest() {

        System.out.println("Request ID: " + requestId);
        System.out.println("Field: " + field.getFieldName());
        System.out.println("Priority: " + priority);
        System.out.println("Status: " + status);
    }
}
