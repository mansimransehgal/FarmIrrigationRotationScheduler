package com.farm.database;

import java.sql.*;
import java.util.*;

import com.farm.model.Field;
import com.farm.model.IrrigationRequest;

public class RequestDAO {

    public boolean saveRequest(IrrigationRequest request) {
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
            return true;

        } catch (SQLException e) {
            System.out.println("Request save failed: " + e.getMessage());
            return false;
        }
    }

    public List<IrrigationRequest> getAllRequests(
            Map<String, Field> fields) {

        List<IrrigationRequest> requests = new ArrayList<>();

        String sql = "SELECT * FROM irrigation_requests";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                String fieldId = rs.getString("field_id");
                Field field = fields.get(fieldId);

                if (field == null) {
                    System.out.println(
                        "Skipping request: Field not found - " + fieldId
                    );
                    continue;
                }

                IrrigationRequest request = new IrrigationRequest(
                    rs.getString("request_id"),
                    field,
                    rs.getString("priority")
                );

                request.setStatus(rs.getString("status"));

                requests.add(request);
            }

        } catch (SQLException e) {
            System.out.println("Could not load requests: " + e.getMessage());
        }

        return requests;
    }
}