
package com.farm.model;

public class Field {

    private String fieldId;
    private String fieldName;
    private double area;
    private double waterRequirement;
    private String priority;

    // Validate priority
    private static boolean isValidPriority(String priority) {
        return priority != null &&
                (priority.equalsIgnoreCase("HIGH") ||
                 priority.equalsIgnoreCase("MEDIUM") ||
                 priority.equalsIgnoreCase("LOW"));
    }

    // Constructor
    public Field(String fieldId, String fieldName,
                 double area, double waterRequirement,
                 String priority) {

        if (fieldId == null || fieldId.trim().isEmpty())
            throw new IllegalArgumentException("Field ID cannot be empty.");

        if (fieldName == null || fieldName.trim().isEmpty())
            throw new IllegalArgumentException("Field name cannot be empty.");

        if (area <= 0 || !Double.isFinite(area))
            throw new IllegalArgumentException("Area must be positive.");

        if (waterRequirement <= 0 ||
                !Double.isFinite(waterRequirement))
            throw new IllegalArgumentException("Water requirement must be positive.");

        if (!isValidPriority(priority))
            throw new IllegalArgumentException("Priority must be HIGH, MEDIUM or LOW.");

        this.fieldId = fieldId.trim();
        this.fieldName = fieldName.trim();
        this.area = area;
        this.waterRequirement = waterRequirement;
        this.priority = priority.toUpperCase();
    }

    // Getters
    public String getFieldId() {
        return fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public double getArea() {
        return area;
    }

    public double getWaterRequirement() {
        return waterRequirement;
    }

    public String getPriority() {
        return priority;
    }

    // Setters with validation
    public void setFieldName(String fieldName) {
        if (fieldName == null || fieldName.trim().isEmpty())
            throw new IllegalArgumentException("Field name cannot be empty.");

        this.fieldName = fieldName.trim();
    }

    public void setArea(double area) {
        if (area <= 0 || !Double.isFinite(area))
            throw new IllegalArgumentException("Area must be positive.");

        this.area = area;
    }

    public void setWaterRequirement(double waterRequirement) {
        if (waterRequirement <= 0 ||
                !Double.isFinite(waterRequirement))
            throw new IllegalArgumentException("Water requirement must be positive.");

        this.waterRequirement = waterRequirement;
    }

    public void setPriority(String priority) {
        if (!isValidPriority(priority))
            throw new IllegalArgumentException("Invalid priority.");

        this.priority = priority.toUpperCase();
    }

    // Display field information
    public void displayFieldDetails() {
        System.out.println("Field ID: " + fieldId);
        System.out.println("Field Name: " + fieldName);
        System.out.println("Area: " + area + " acres");
        System.out.println("Water Requirement: "
                + waterRequirement + " litres");
        System.out.println("Priority: " + priority);
    }
}
