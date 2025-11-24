package com.vdtry06.partner_management.source.server.dto.shift;

import com.vdtry06.partner_management.lib.enumerated.ShiftType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftRequest {
    @NotNull(message = "Ngày làm việc không được để trống")
    @FutureOrPresent(message = "Ngày làm việc phải từ hôm nay trở đi")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate workDate;

    private ShiftType shiftType;

    @NotNull(message = "Số lượng nhân công không được để trống")
    @Min(value = 1, message = "Số lượng nhân công phải lớn hơn 0")
    private Integer workerCount;

    @NotNull(message = "Đơn giá không được để trống")
    @Min(value = 0, message = "Đơn giá phải lớn hơn hoặc bằng 0")
    private Long shiftUnitPrice;

    private String description;
}
