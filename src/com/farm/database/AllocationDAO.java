
package com.farm.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.farm.model.Allocation;

public class AllocationDAO {

    public void saveAllocation(Allocation allocation) {

        String insertSQL = """
                INSERT INTO allocations
                (allocation_id, request_id, slot_id, allocated_water)
                VALUES (?, ?, ?, ?)
                """;

        String requestSQL =
                "UPDATE irrigation_requests SET status = 'APPROVED' WHERE request_id = ?";

        String slotSQL =
                "UPDATE time_slots SET available = FALSE WHERE slot_id = ?";

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {
                try (PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
                    stmt.setString(1, allocation.getAllocationId());
                    stmt.setString(2, allocation.getRequest().getRequestId());
                    stmt.setString(3, allocation.getTimeSlot().getSlotId());
                    stmt.setDouble(4, allocation.getAllocatedWater());
                    stmt.executeUpdate();
                }

                try (PreparedStatement stmt = conn.prepareStatement(requestSQL)) {
                    stmt.setString(1, allocation.getRequest().getRequestId());
                    stmt.executeUpdate();
                }

                try (PreparedStatement stmt = conn.prepareStatement(slotSQL)) {
                    stmt.setString(1, allocation.getTimeSlot().getSlotId());
                    stmt.executeUpdate();
                }

                conn.commit();
                System.out.println("Allocation saved successfully!");

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Allocation failed. Changes rolled back.");
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
