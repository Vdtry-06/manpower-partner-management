package com.vdtry06.partner_management.source.server.dto.shift;

import com.vdtry06.partner_management.lib.enumerated.ShiftType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftResponse {
    private Integer id;
    private LocalDate workDate;
    private ShiftType shiftType;
    private Integer workerCount;
    private Long shiftUnitPrice;
    private String description;
}
