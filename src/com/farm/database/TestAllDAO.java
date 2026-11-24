
package com.farm.database;

import java.time.LocalTime;

import com.farm.model.*;

public class TestAllDAO {

    public static void main(String[] args) {

        // 1. Create and save a field
        Field field = new Field(
                "F102", "South Field", 3.0, 700, "HIGH"
        );

        new FieldDAO().saveField(field);

        // 2. Create and save an irrigation request
        IrrigationRequest request =
                new IrrigationRequest("R002", field, "HIGH");

        new RequestDAO().saveRequest(request);

        // 3. Create and save a time slot
        TimeSlot slot = new TimeSlot(
                "T02",
                LocalTime.of(7, 0),
                LocalTime.of(8, 0)
        );

        new TimeSlotDAO().saveTimeSlot(slot);

        // 4. Create and save an allocation
        Allocation allocation = new Allocation(
                "A002", request, slot, 700
        );

        new AllocationDAO().saveAllocation(allocation);
    }
}
