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
public class InvoiceDetailResponse {
    private Integer invoiceId;
    private String invoiceDate;
    private Integer paymentAmount;
    private String paymentMethod;
    private String invoiceStatus;

    // Thông tin đối tác
    private String partnerName;
    private String partnerRepresentative;
    private String partnerPhone;

    // Thông tin hợp đồng
    private Integer contractId;
    private String contractName;

    // Thông tin ca làm việc
    private Integer shiftId;
    private String workDate;
    private String startTime;
    private String endTime;
    private Integer workerCount;
    private Long shiftUnitPrice;
    private Long totalShiftValue;
    private Integer remainingAmountAfter;

    // Thông tin task
    private String taskName;

    // Thông tin accountant
    private String accountantName;

    public static InvoiceDetailResponse fromEntity(Invoice invoice) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Long totalShiftValue = invoice.getShiftId().getShiftUnitPrice() * invoice.getShiftId().getWorkerCount();

        return InvoiceDetailResponse.builder()
                .invoiceId(invoice.getId())
                .invoiceDate(invoice.getInvoiceDate() != null ? invoice.getInvoiceDate().format(timeFormatter) : "")
                .paymentAmount(invoice.getPaymentAmount())
                .paymentMethod(invoice.getPaymentMethod() != null ? invoice.getPaymentMethod().name() : "")
                .invoiceStatus(invoice.getInvoiceStatus() != null ? invoice.getInvoiceStatus().name() : "")
                .partnerName(invoice.getShiftId().getTaskContractId().getContractId().getPartnerId().getNamePartner())
                .partnerRepresentative(invoice.getShiftId().getTaskContractId().getContractId().getPartnerId().getPartnerRepresentative())
                .partnerPhone(invoice.getShiftId().getTaskContractId().getContractId().getPartnerId().getPhoneNumber())
                .contractId(invoice.getShiftId().getTaskContractId().getContractId().getId())
                .contractName(invoice.getShiftId().getTaskContractId().getContractId().getContractName())
                .shiftId(invoice.getShiftId().getId())
                .workDate(invoice.getShiftId().getWorkDate().format(dateFormatter))
                .startTime(invoice.getShiftId().getStartTime().format(timeFormatter))
                .endTime(invoice.getShiftId().getEndTime().format(timeFormatter))
                .workerCount(invoice.getShiftId().getWorkerCount())
                .shiftUnitPrice(invoice.getShiftId().getShiftUnitPrice())
                .totalShiftValue(totalShiftValue)
                .remainingAmountAfter(invoice.getShiftId().getRemainingAmount())
                .taskName(invoice.getShiftId().getTaskContractId().getTaskId().getNameTask())
                .accountantName(invoice.getAccountantId().getFullname())
                .build();
    }
}