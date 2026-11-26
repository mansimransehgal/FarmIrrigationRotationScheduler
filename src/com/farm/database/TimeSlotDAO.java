
package com.farm.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.farm.model.TimeSlot;

public class TimeSlotDAO {

    // Save time slot
    public boolean saveTimeSlot(TimeSlot slot) {

        String sql = """
                INSERT INTO time_slots
                (slot_id, start_time, end_time, available, slot_date)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, slot.getSlotId());
            stmt.setTime(2, Time.valueOf(slot.getStartTime()));
            stmt.setTime(3, Time.valueOf(slot.getEndTime()));
            stmt.setBoolean(4, slot.isAvailable());
            stmt.setDate(5, Date.valueOf(slot.getSlotDate()));

            stmt.executeUpdate();

            System.out.println("Time slot saved successfully!");
            return true;

        } catch (SQLException e) {

            System.out.println(
                    "Time slot save failed: " + e.getMessage());

            return false;
        }
    }

    // Retrieve all time slots
    public List<TimeSlot> getAllTimeSlots() {

        List<TimeSlot> slots = new ArrayList<>();

        String sql = "SELECT * FROM time_slots";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Date sqlDate = rs.getDate("slot_date");

                // Old records have NULL dates.
                // Assign today's date for compatibility.
                java.time.LocalDate slotDate =
                        sqlDate == null
                        ? java.time.LocalDate.now()
                        : sqlDate.toLocalDate();

                TimeSlot slot = new TimeSlot(
                        rs.getString("slot_id"),
                        rs.getTime("start_time").toLocalTime(),
                        rs.getTime("end_time").toLocalTime(),
                        slotDate
                );

                slot.setAvailable(rs.getBoolean("available"));

                slots.add(slot);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Could not load time slots: " + e.getMessage());
        }

        return slots;
    }
}
