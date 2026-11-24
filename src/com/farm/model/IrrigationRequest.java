
package com.farm.model;

public class IrrigationRequest {

    private String requestId;
    private Field field;
    private String priority;
    private String status;

    // Constructor
    public IrrigationRequest(String requestId, Field field,
                             String priority) {

        if (requestId == null || requestId.trim().isEmpty())
            throw new IllegalArgumentException("Request ID cannot be empty.");

        if (field == null)
            throw new IllegalArgumentException("Field cannot be null.");

        if (priority == null ||
                !(priority.equalsIgnoreCase("HIGH") ||
                  priority.equalsIgnoreCase("MEDIUM") ||
                  priority.equalsIgnoreCase("LOW"))) {

            throw new IllegalArgumentException("Invalid request priority.");
        }

        this.requestId = requestId.trim();
        this.field = field;
        this.priority = priority.toUpperCase();
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

        if (status == null ||
                !(status.equalsIgnoreCase("PENDING") ||
                  status.equalsIgnoreCase("APPROVED") ||
                  status.equalsIgnoreCase("COMPLETED"))) {

            throw new IllegalArgumentException("Invalid request status.");
        }

        this.status = status.toUpperCase();
    }

    // Display request
    public void displayRequest() {
        System.out.println("Request ID: " + requestId);
        System.out.println("Field: " + field.getFieldName());
        System.out.println("Priority: " + priority);
        System.out.println("Status: " + status);
    }
}
