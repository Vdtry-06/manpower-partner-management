package com.vdtry06.partner_management.source.server.dto.shift;

import com.vdtry06.partner_management.source.server.entities.Shift;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftResponse {
    private Integer id;
    private String workDate;
    private String startTime;
    private String endTime;
    private Integer workerCount;
    private Long shiftUnitPrice;
    private Long totalValue;
    private Integer remainingAmount;
    private String description;
    private String taskName;

    public static ShiftResponse fromEntity(Shift shift) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Long totalValue = shift.getShiftUnitPrice() * shift.getWorkerCount();

        return ShiftResponse.builder()
                .id(shift.getId())
                .workDate(shift.getWorkDate() != null ? shift.getWorkDate().format(dateFormatter) : "")
                .startTime(shift.getStartTime() != null ? shift.getStartTime().format(timeFormatter) : "")
                .endTime(shift.getEndTime() != null ? shift.getEndTime().format(timeFormatter) : "")
                .workerCount(shift.getWorkerCount())
                .shiftUnitPrice(shift.getShiftUnitPrice())
                .totalValue(totalValue)
                .remainingAmount(shift.getRemainingAmount())
                .description(shift.getDescription())
                .taskName(shift.getTaskContractId() != null && shift.getTaskContractId().getTaskId() != null
                        ? shift.getTaskContractId().getTaskId().getNameTask()
                        : "")
                .build();
    }
}