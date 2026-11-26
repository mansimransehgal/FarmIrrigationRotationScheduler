
package com.farm.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.farm.model.Allocation;
import com.farm.model.IrrigationRequest;
import com.farm.model.TimeSlot;

public class AllocationDAO {

    // 1. Save allocation to database
    public boolean saveAllocation(Allocation allocation) {

        String insertSQL = """
                INSERT INTO allocations
                (allocation_id, request_id, slot_id,
                 allocated_water, allocation_date)
                VALUES (?, ?, ?, ?, ?)
                """;

        String requestSQL =
                "UPDATE irrigation_requests SET status = 'APPROVED' WHERE request_id = ?";

        String slotSQL =
                "UPDATE time_slots SET available = FALSE WHERE slot_id = ?";

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            try {
            	
            	// Check whether another allocation overlaps on the same date
            	String overlapSQL = """
            	        SELECT COUNT(*)
            	        FROM allocations a
            	        JOIN time_slots t ON a.slot_id = t.slot_id
            	        WHERE a.allocation_date = ?
            	        AND t.start_time < ?
            	        AND t.end_time > ?
            	        """;

            	try (PreparedStatement check =
            	        conn.prepareStatement(overlapSQL)) {

            	    check.setDate(1, java.sql.Date.valueOf(
            	            allocation.getAllocationDate()));

            	    check.setTime(2, java.sql.Time.valueOf(
            	            allocation.getTimeSlot().getEndTime()));

            	    check.setTime(3, java.sql.Time.valueOf(
            	            allocation.getTimeSlot().getStartTime()));

            	    try (ResultSet rs = check.executeQuery()) {

            	        if (rs.next() && rs.getInt(1) > 0) {
            	            throw new SQLException(
            	                    "This time slot overlaps with an existing allocation."
            	            );
            	        }
            	    }
            	}

                // Save allocation
                try (PreparedStatement stmt =
                        conn.prepareStatement(insertSQL)) {

                    stmt.setString(1, allocation.getAllocationId());
                    stmt.setString(2, allocation.getRequest().getRequestId());
                    stmt.setString(3, allocation.getTimeSlot().getSlotId());
                    stmt.setDouble(4, allocation.getAllocatedWater());

                    stmt.setDate(5, java.sql.Date.valueOf(
                            allocation.getAllocationDate()));

                    stmt.executeUpdate();
                }

                // Update request status
                try (PreparedStatement stmt =
                        conn.prepareStatement(requestSQL)) {

                    stmt.setString(1, allocation.getRequest().getRequestId());

                    if (stmt.executeUpdate() != 1) {
                        throw new SQLException("Request was not updated.");
                    }
                }

                // Mark time slot unavailable
                try (PreparedStatement stmt =
                        conn.prepareStatement(slotSQL)) {

                    stmt.setString(1, allocation.getTimeSlot().getSlotId());

                    if (stmt.executeUpdate() != 1) {
                        throw new SQLException("Time slot was not updated.");
                    }
                }

                conn.commit();

                System.out.println("Allocation saved successfully!");
                return true;

            } catch (SQLException e) {

                conn.rollback();

                System.out.println(
                        "Allocation failed: " + e.getMessage());

                return false;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Database error: " + e.getMessage());

            return false;
        }
    }

    // 2. Retrieve all allocations from database
    public List<Allocation> getAllAllocations(
            Map<String, IrrigationRequest> requests,
            Map<String, TimeSlot> slots) {

        List<Allocation> allocations = new ArrayList<>();

        String sql = "SELECT * FROM allocations";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                String requestId = rs.getString("request_id");
                String slotId = rs.getString("slot_id");

                IrrigationRequest request = requests.get(requestId);
                TimeSlot slot = slots.get(slotId);

                if (request == null || slot == null) {

                    System.out.println(
                            "Skipping allocation: Request or slot not found.");

                    continue;
                }

                java.sql.Date date =
                        rs.getDate("allocation_date");

                // Skip old records with unknown dates
                if (date == null) {
                    continue;
                }

                Allocation allocation = new Allocation(
                        rs.getString("allocation_id"),
                        request,
                        slot,
                        rs.getDouble("allocated_water"),
                        date.toLocalDate()
                );

                allocations.add(allocation);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Could not load allocations: " + e.getMessage());
        }

        return allocations;
    }
}
 