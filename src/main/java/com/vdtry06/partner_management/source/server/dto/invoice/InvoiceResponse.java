package com.vdtry06.partner_management.source.server.dto.invoice;

import com.vdtry06.partner_management.source.server.entities.Invoice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponse {
    private Integer id;
    private String invoiceDate;
    private Integer paymentAmount;
    private String paymentMethod;
    private String invoiceStatus;

    // Thông tin ca làm việc
    private Integer shiftId;
    private String workDate;
    private String startTime;
    private String endTime;
    private Integer workerCount;
    private Long shiftUnitPrice;
    private Integer remainingAmount;

    // Thông tin task
    private String taskName;

    // Thông tin accountant
    private String accountantName;

    public static InvoiceResponse fromEntity(Invoice invoice) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return InvoiceResponse.builder()
                .id(invoice.getId())
                .invoiceDate(invoice.getInvoiceDate() != null ? invoice.getInvoiceDate().format(timeFormatter) : "")
                .paymentAmount(invoice.getPaymentAmount())
                .paymentMethod(invoice.getPaymentMethod() != null ? invoice.getPaymentMethod().name() : "")
                .invoiceStatus(invoice.getInvoiceStatus() != null ? invoice.getInvoiceStatus().name() : "")
                .shiftId(invoice.getShiftId() != null ? invoice.getShiftId().getId() : null)
                .workDate(invoice.getShiftId() != null && invoice.getShiftId().getWorkDate() != null
                        ? invoice.getShiftId().getWorkDate().format(dateFormatter) : "")
                .startTime(invoice.getShiftId() != null && invoice.getShiftId().getStartTime() != null
                        ? invoice.getShiftId().getStartTime().format(timeFormatter) : "")
                .endTime(invoice.getShiftId() != null && invoice.getShiftId().getEndTime() != null
                        ? invoice.getShiftId().getEndTime().format(timeFormatter) : "")
                .workerCount(invoice.getShiftId() != null ? invoice.getShiftId().getWorkerCount() : null)
                .shiftUnitPrice(invoice.getShiftId() != null ? invoice.getShiftId().getShiftUnitPrice() : null)
                .remainingAmount(invoice.getShiftId() != null ? invoice.getShiftId().getRemainingAmount() : null)
                .taskName(invoice.getShiftId() != null && invoice.getShiftId().getTaskContractId() != null
                        && invoice.getShiftId().getTaskContractId().getTaskId() != null
                        ? invoice.getShiftId().getTaskContractId().getTaskId().getNameTask() : "")
                .accountantName(invoice.getAccountantId() != null ? invoice.getAccountantId().getFullname() : "")
                .build();
    }
}