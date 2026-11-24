
package com.farm.model;

public class Field {

    // Attributes
    private String fieldId;
    private String fieldName;
    private double area;
    private double waterRequirement;
    private String priority;

    // Constructor
    public Field(String fieldId, String fieldName,
                 double area, double waterRequirement,
                 String priority) {

        this.fieldId = fieldId;
        this.fieldName = fieldName;
        this.area = area;
        this.waterRequirement = waterRequirement;
        this.priority = priority;
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

    // Setters
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public void setWaterRequirement(double waterRequirement) {
        this.waterRequirement = waterRequirement;
    }

    public void setPriority(String priority) {
        this.priority = priority;
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
