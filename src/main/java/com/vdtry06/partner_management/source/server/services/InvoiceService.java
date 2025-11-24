package com.vdtry06.partner_management.source.server.services;

import com.vdtry06.partner_management.lib.enumerated.InvoiceStatus;
import com.vdtry06.partner_management.source.server.dto.invoice.InvoiceConfirmResponse;
import com.vdtry06.partner_management.source.server.dto.invoice.InvoiceCreateRequest;
import com.vdtry06.partner_management.source.server.dto.invoice.InvoiceResponse;
import com.vdtry06.partner_management.source.server.dto.invoice.ShiftInvoiceDetail;
import com.vdtry06.partner_management.source.server.entities.*;
import com.vdtry06.partner_management.source.server.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final ShiftRepository shiftRepository;
    private final ContractRepository contractRepository;
    private final AccountantRepository accountantRepository;
    private final ShiftTaskContractRepository shiftTaskContractRepository;

    @Transactional
    public InvoiceConfirmResponse createInvoice(InvoiceCreateRequest request, Integer accountantId) {
        Contract contract = contractRepository.findById(request.getContractId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hợp đồng"));

        Accountant accountant = accountantRepository.findById(accountantId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kế toán"));

        long totalPaidBefore = invoiceRepository.sumPaymentAmountByContract(contract.getId());


        List<Shift> shifts = shiftRepository.findAllById(request.getShiftIds());

        if (shifts.isEmpty()) {
            throw new RuntimeException("Vui lòng chọn ít nhất một ca làm việc");
        }

        for (Shift shift : shifts) {
            ShiftTaskContract stc = shiftTaskContractRepository.findByShiftId(shift)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin ca làm việc"));

            if (!stc.getTaskContractId().getContractId().getId().equals(contract.getId())) {
                throw new RuntimeException("Ca làm việc không thuộc hợp đồng này");
            }
        }

        long totalAmount = 0;
        List<ShiftInvoiceDetail> shiftDetails = new ArrayList<>();

        for (Shift shift : shifts) {
            ShiftTaskContract stc = shiftTaskContractRepository.findByShiftId(shift)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin ca làm việc"));

            long shiftValue = (long) stc.getWorkerCount() * stc.getShiftUnitPrice();
            totalAmount += shiftValue;

            ShiftInvoiceDetail detail = ShiftInvoiceDetail.builder()
                    .shiftId(shift.getId())
                    .workDate(shift.getWorkDate().toString())
                    .startTime(getStartTime(shift.getShiftType().name()))
                    .endTime(getEndTime(shift.getShiftType().name()))
                    .workerCount(stc.getWorkerCount())
                    .shiftUnitPrice(stc.getShiftUnitPrice())
                    .totalValue(shiftValue)
                    .build();

            shiftDetails.add(detail);
        }

        long newTotalPaid = totalPaidBefore + totalAmount;

        InvoiceStatus status;
        if (newTotalPaid >= contract.getTotalContractValue()) {
            status = InvoiceStatus.PAID;
        } else if (newTotalPaid > 0) {
            status = InvoiceStatus.PARTIALLY_PAID;
        } else {
            status = InvoiceStatus.UNPAID;
        }

        long remainingAmount = contract.getTotalContractValue() - newTotalPaid;

        return InvoiceConfirmResponse.builder()
                .contractId(contract.getId())
                .contractName(contract.getContractName())
                .partnerName(contract.getPartnerId().getNamePartner())
                .partnerRepresentative(contract.getPartnerId().getPartnerRepresentative())
                .partnerPhone(contract.getPartnerId().getPhoneNumber())
                .accountantName(accountant.getFullname())
                .shifts(shiftDetails)
                .totalAmount(contract.getTotalContractValue())
                .paymentAmount(totalAmount)
                .remainingAmount(remainingAmount)
                .invoiceStatus(status)
                .paymentMethod(request.getPaymentMethod())
                .invoiceDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .build();
    }


    @Transactional
    public InvoiceResponse saveInvoice(InvoiceConfirmResponse response, List<Integer> shiftIds, Integer accountantId) {
        Accountant accountant = accountantRepository.findById(accountantId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kế toán"));

        Invoice invoice = Invoice.builder()
                .accountantId(accountant)
                .paymentAmount(response.getPaymentAmount())
                .remainingAmount(response.getRemainingAmount())
                .paymentMethod(response.getPaymentMethod())
                .invoiceStatus(response.getInvoiceStatus())
                .invoiceDate(LocalDateTime.now())
                .build();

        invoice = invoiceRepository.save(invoice);

        List<Shift> shifts = shiftRepository.findAllById(shiftIds);

        for (Shift shift : shifts) {
            shift.setInvoice(invoice);
            shiftRepository.save(shift);
        }

        return toInvoiceResponse(invoice);
    }

    private InvoiceResponse toInvoiceResponse(Invoice invoice) {
        return InvoiceResponse.builder()
                .id(invoice.getId())
                .invoiceStatus(invoice.getInvoiceStatus())
                .invoiceDate(invoice.getInvoiceDate())
                .accountantId(invoice.getAccountantId())
                .paymentMethod(invoice.getPaymentMethod())
                .shifts(invoice.getShifts())
                .invoiceStatus(invoice.getInvoiceStatus())
                .paymentAmount(invoice.getPaymentAmount())
                .build();
    }

    private String getStartTime(String shiftType) {
        return switch (shiftType) {
            case "MORNING" -> "06:00";
            case "AFTERNOON" -> "13:00";
            case "EVENING" -> "18:00";
            default -> "00:00";
        };
    }

    private String getEndTime(String shiftType) {
        return switch (shiftType) {
            case "MORNING" -> "12:00";
            case "AFTERNOON" -> "17:00";
            case "EVENING" -> "22:00";
            default -> "00:00";
        };
    }
}