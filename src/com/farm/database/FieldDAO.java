
package com.farm.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.farm.model.Field;

public class FieldDAO {

    public void saveField(Field field) {

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

            System.out.println("Field saved to MySQL successfully!");

        } catch (SQLException e) {
            System.out.println("Failed to save field: " + e.getMessage());
        }
    }
}
