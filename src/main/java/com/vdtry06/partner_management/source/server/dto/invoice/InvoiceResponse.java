package com.vdtry06.partner_management.source.server.dto.invoice;

import java.time.LocalDateTime;
import java.util.List;

import com.vdtry06.partner_management.lib.enumerated.InvoiceStatus;
import com.vdtry06.partner_management.lib.enumerated.PaymentMethod;
import com.vdtry06.partner_management.source.server.dto.shift.ShiftDetailResponse;

import com.vdtry06.partner_management.source.server.entities.Accountant;
import com.vdtry06.partner_management.source.server.entities.Shift;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponse {
    private Integer id;
    private LocalDateTime invoiceDate;
    private Long paymentAmount;
    private Long remainingAmount;
    private PaymentMethod paymentMethod;
    private InvoiceStatus invoiceStatus;
    private Accountant accountantId;
    private List<Shift> shifts;
}
