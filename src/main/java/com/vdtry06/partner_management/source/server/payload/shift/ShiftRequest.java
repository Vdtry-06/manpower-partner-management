package com.vdtry06.partner_management.source.server.payload.shift;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShiftRequest {
    private LocalDate workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer workerCount;
    private Integer shiftUnitPrice;
    private String description;
}
