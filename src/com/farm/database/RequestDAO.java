
package com.farm.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.farm.model.IrrigationRequest;

public class RequestDAO {

    public void saveRequest(IrrigationRequest request) {

        String sql = """
                INSERT INTO irrigation_requests
                (request_id, field_id, priority, status)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, request.getRequestId());
            stmt.setString(2, request.getField().getFieldId());
            stmt.setString(3, request.getPriority());
            stmt.setString(4, request.getStatus());

            stmt.executeUpdate();

            System.out.println("Request saved successfully!");

        } catch (SQLException e) {
            System.out.println("Request save failed: " + e.getMessage());
        }
    }
}
