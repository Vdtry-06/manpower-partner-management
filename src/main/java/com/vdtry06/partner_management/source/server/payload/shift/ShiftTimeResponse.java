package com.vdtry06.partner_management.source.server.payload.shift;

import lombok.*;

import java.time.LocalTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShiftTimeResponse {
    private LocalTime startTime;
    private LocalTime endTime;
}
