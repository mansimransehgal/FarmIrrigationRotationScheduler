
package com.farm.scheduler;

import java.util.Comparator;
import com.farm.model.IrrigationRequest;

public class PriorityComparator
        implements Comparator<IrrigationRequest> {

    @Override
    public int compare(IrrigationRequest r1,
                       IrrigationRequest r2) {

        return getPriorityValue(r2.getPriority())
             - getPriorityValue(r1.getPriority());
    }

    private int getPriorityValue(String priority) {

        switch (priority.toUpperCase()) {

            case "HIGH":
                return 3;

            case "MEDIUM":
                return 2;

            case "LOW":
                return 1;

            default:
                return 0;
        }
    }
}
