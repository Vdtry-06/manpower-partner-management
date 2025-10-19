package com.vdtry06.partner_management.source.server.service;

import com.vdtry06.partner_management.lib.enumerated.InvoiceStatus;
import com.vdtry06.partner_management.lib.enumerated.PaymentMethod;
import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.lib.service.BaseService;
import com.vdtry06.partner_management.source.server.entities.*;
import com.vdtry06.partner_management.source.server.payload.invoice.ConfirmInvoiceResponse;
import com.vdtry06.partner_management.source.server.payload.invoice.InvoiceRequest;
import com.vdtry06.partner_management.source.server.payload.invoice.InvoiceResponse;
import com.vdtry06.partner_management.source.server.repositories.InvoiceRepository;
import com.vdtry06.partner_management.source.server.repositories.ShiftRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class InvoiceService extends BaseService<Invoice, Integer> {
    private final InvoiceRepository invoiceRepository;
    private final AccountantService accountantService;
    private final ShiftService shiftService;
    private final ShiftRepository shiftRepository;

    public InvoiceService(BaseRepository<Invoice, Integer> repository, InvoiceRepository invoiceRepository, AccountantService accountantService, ShiftService shiftService, ShiftRepository shiftRepository) {
        super(repository);
        this.invoiceRepository = invoiceRepository;
        this.accountantService = accountantService;
        this.shiftService = shiftService;
        this.shiftRepository = shiftRepository;
    }

    public ConfirmInvoiceResponse getInvoice(Integer invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id của hóa đơn"));
        Shift shift = invoice.getShiftId();
        TaskContract taskContract = shift.getTaskContractId();
        Contract contract = taskContract.getContractId();
        Partner partner = contract.getPartnerId();
        return ConfirmInvoiceResponse.builder()
                .invoiceResponse(toInvoiceResponse(invoice))
                .shiftResponse(shiftService.toShiftResponse(shift))
                .nameAccountant(accountantService.getCurrentAccountant().getFullname())
                .nameContract(contract.getContractName())
                .namePatner(partner.getNamePartner())
                .build();
    }

    public InvoiceResponse createInvoice(Integer shiftId) {
        Accountant accountant = accountantService.getCurrentAccountant();
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id ca làm"));

        Integer remainingAmount = shift.getShiftUnitPrice() * shift.getWorkerCount();

        InvoiceStatus status;
        if (shift.getRemainingAmount() == 0) {
            throw new RuntimeException("Ca làm đã thanh toán hết!");
        } else if (shift.getRemainingAmount() < remainingAmount) {
            status = InvoiceStatus.PARTIALLY_PAID;
        } else {
            status = InvoiceStatus.UNPAID;
        }

        Invoice invoice = Invoice.builder()
                .accountantId(accountant)
                .shiftId(shift)
                .paymentMethod(PaymentMethod.DEFAULT)
                .invoiceStatus(status)
                .build();
        invoice = invoiceRepository.save(invoice);

        return toInvoiceResponse(invoice);
    }

    public InvoiceResponse updateInvoice(Integer invoiceId, InvoiceRequest invoiceRequest) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id hóa đơn"));

        if (invoice.getPaymentAmount() != null) {
            throw new RuntimeException("Hóa đơn này đã được cập nhật hoặc thanh toán, vui lòng tạo hóa đơn mới.");
        }

        Shift shift = invoice.getShiftId();
        int remainingAmount = shift.getRemainingAmount();
        int paymentAmount = invoiceRequest.getPaymentAmount();

        if (paymentAmount > remainingAmount) {
            throw new RuntimeException("Tiền trả phải nhỏ hơn hoặc bằng số tiền còn lại phải trả");
        }

        if (paymentAmount < remainingAmount) {
            invoice.setInvoiceStatus(InvoiceStatus.PARTIALLY_PAID);
        } else {
            invoice.setInvoiceStatus(InvoiceStatus.PAID);
        }

        invoice.setPaymentAmount(paymentAmount);
        invoice.setPaymentMethod(invoiceRequest.getPaymentMethod());
        invoice.setInvoiceDate(LocalDateTime.now());

        invoiceRepository.save(invoice);

        shiftService.updateRemainingAmount(invoice.getShiftId().getId(), invoiceRequest.getPaymentAmount());

        return toInvoiceResponse(invoice);
    }

    private InvoiceResponse toInvoiceResponse(Invoice invoice) {
        new InvoiceResponse();
        return InvoiceResponse.builder()
                .id(invoice.getId())
                .paymentMethod(invoice.getPaymentMethod())
                .invoiceStatus(invoice.getInvoiceStatus())
                .accountantId(invoice.getAccountantId().getId())
                .shiftId(invoice.getShiftId().getId())
                .remainingAmount(invoice.getShiftId().getRemainingAmount())
                .invoiceDate(invoice.getInvoiceDate())
                .paymentAmount(invoice.getPaymentAmount())
                .build();
    }
}
