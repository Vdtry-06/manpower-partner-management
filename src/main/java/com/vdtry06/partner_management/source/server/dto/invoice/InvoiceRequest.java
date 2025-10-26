package com.vdtry06.partner_management.source.server.dto.invoice;

import com.vdtry06.partner_management.lib.enumerated.PaymentMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequest {

    @NotNull(message = "Số tiền thanh toán không được để trống")
    @Min(value = 1, message = "Số tiền thanh toán phải lớn hơn 0")
    private Integer paymentAmount;

    @NotNull(message = "Phương thức thanh toán không được để trống")
    private PaymentMethod paymentMethod;

    private Integer shiftId;
}