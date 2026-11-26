package com.farm.database;

import java.sql.*;
import java.util.*;

import com.farm.model.Field;

public class FieldDAO {

    public boolean saveField(Field field) {
        String sql = """
                INSERT INTO fields
                (field_id, field_name, area, water_requirement, priority)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, field.getFieldId());
            stmt.setString(2, field.getFieldName());
            stmt.setDouble(3, field.getArea());
            stmt.setDouble(4, field.getWaterRequirement());
            stmt.setString(5, field.getPriority());

            stmt.executeUpdate();
            System.out.println("Field saved successfully!");
            return true;

        } catch (SQLException e) {
            System.out.println("Field save failed: " + e.getMessage());
            return false;
        }
    }

    public List<Field> getAllFields() {
        List<Field> fields = new ArrayList<>();

        String sql = "SELECT * FROM fields";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Field field = new Field(
                    rs.getString("field_id"),
                    rs.getString("field_name"),
                    rs.getDouble("area"),
                    rs.getDouble("water_requirement"),
                    rs.getString("priority")
                );

                fields.add(field);
            }

        } catch (SQLException e) {
            System.out.println("Could not load fields: " + e.getMessage());
        }

        return fields;
    }
}	