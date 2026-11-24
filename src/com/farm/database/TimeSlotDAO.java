
package com.farm.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;

import com.farm.model.TimeSlot;

public class TimeSlotDAO {

    public void saveTimeSlot(TimeSlot slot) {

        String sql = """
                INSERT INTO time_slots
                (slot_id, start_time, end_time, available)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, slot.getSlotId());
            stmt.setTime(2, Time.valueOf(slot.getStartTime()));
            stmt.setTime(3, Time.valueOf(slot.getEndTime()));
            stmt.setBoolean(4, slot.isAvailable());

            stmt.executeUpdate();

            System.out.println("Time slot saved successfully!");

        } catch (SQLException e) {
            System.out.println("Time slot save failed: " + e.getMessage());
        }
    }
}
