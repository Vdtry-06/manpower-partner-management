package com.vdtry06.partner_management.source.server.payload.shift;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShiftResponse {
    private Integer id;
    private LocalDate workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer workerCount;
    private Integer shiftUnitPrice;
    private Integer remainingAmount;
    private String description;
}