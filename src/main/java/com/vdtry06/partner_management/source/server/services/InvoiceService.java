package com.vdtry06.partner_management.source.server.services;

import com.vdtry06.partner_management.lib.enumerated.InvoiceStatus;
import com.vdtry06.partner_management.source.server.dto.invoice.InvoiceDetailResponse;
import com.vdtry06.partner_management.source.server.dto.invoice.InvoiceRequest;
import com.vdtry06.partner_management.source.server.dto.invoice.InvoiceResponse;
import com.vdtry06.partner_management.source.server.entities.Accountant;
import com.vdtry06.partner_management.source.server.entities.Invoice;
import com.vdtry06.partner_management.source.server.entities.Shift;
import com.vdtry06.partner_management.source.server.repositories.AccountantRepository;
import com.vdtry06.partner_management.source.server.repositories.InvoiceRepository;
import com.vdtry06.partner_management.source.server.repositories.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ShiftRepository shiftRepository;
    private final AccountantRepository accountantRepository;

    public List<InvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(InvoiceResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<InvoiceResponse> getInvoicesByAccountantId(Integer accountantId) {
        return invoiceRepository.findByAccountantId_Id(accountantId).stream()
                .map(InvoiceResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public Invoice getInvoiceById(Integer id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
    }

    public InvoiceDetailResponse getInvoiceDetail(Integer id) {
        Invoice invoice = getInvoiceById(id);
        return InvoiceDetailResponse.fromEntity(invoice);
    }

    @Transactional
    public Integer createDraftInvoice(Integer shiftId, Integer accountantId) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new RuntimeException("Shift not found with id: " + shiftId));

        Accountant accountant = accountantRepository.findById(accountantId)
                .orElseThrow(() -> new RuntimeException("Accountant not found with id: " + accountantId));

        // Kiểm tra ca làm việc còn số tiền chưa thanh toán không
        if (shift.getRemainingAmount() == null || shift.getRemainingAmount() <= 0) {
            throw new RuntimeException("Ca làm việc này đã được thanh toán đầy đủ!");
        }

        // Tạo invoice nháp
        Invoice invoice = Invoice.builder()
                .invoiceDate(LocalTime.now())
                .paymentAmount(0)
                .invoiceStatus(InvoiceStatus.UNPAID)
                .shiftId(shift)
                .accountantId(accountant)
                .build();

        Invoice savedInvoice = invoiceRepository.save(invoice);
        return savedInvoice.getId();
    }

    @Transactional
    public Invoice updateInvoice(Integer invoiceId, InvoiceRequest request) {
        Invoice invoice = getInvoiceById(invoiceId);
        Shift shift = invoice.getShiftId();

        // Kiểm tra số tiền thanh toán không vượt quá số tiền còn thiếu
        Long totalShiftValue = shift.getShiftUnitPrice() * shift.getWorkerCount();
        Integer maxPayment = shift.getRemainingAmount() * shift.getShiftUnitPrice().intValue();

        if (request.getPaymentAmount() > maxPayment) {
            throw new RuntimeException("Số tiền thanh toán vượt quá số tiền còn thiếu của ca làm việc!");
        }

        invoice.setPaymentAmount(request.getPaymentAmount());
        invoice.setPaymentMethod(request.getPaymentMethod());

        return invoiceRepository.save(invoice);
    }

    @Transactional
    public InvoiceDetailResponse confirmInvoice(Integer invoiceId) {
        Invoice invoice = getInvoiceById(invoiceId);

        // Kiểm tra invoice đã có đầy đủ thông tin chưa
        if (invoice.getPaymentAmount() == null || invoice.getPaymentAmount() <= 0) {
            throw new RuntimeException("Vui lòng nhập số tiền thanh toán!");
        }

        if (invoice.getPaymentMethod() == null) {
            throw new RuntimeException("Vui lòng chọn phương thức thanh toán!");
        }

        // Cập nhật trạng thái invoice
        invoice.setInvoiceStatus(InvoiceStatus.PAID);
        invoice.setInvoiceDate(LocalTime.now());

        // Cập nhật remaining amount của shift
        Shift shift = invoice.getShiftId();
        Integer paidWorkers = invoice.getPaymentAmount() / shift.getShiftUnitPrice().intValue();
        shift.setRemainingAmount(shift.getRemainingAmount() - paidWorkers);

        shiftRepository.save(shift);
        Invoice savedInvoice = invoiceRepository.save(invoice);

        return InvoiceDetailResponse.fromEntity(savedInvoice);
    }

    @Transactional
    public void deleteInvoice(Integer id) {
        Invoice invoice = getInvoiceById(id);

        // Chỉ cho phép xóa invoice chưa xác nhận
        if (invoice.getInvoiceStatus() == InvoiceStatus.PAID) {
            throw new RuntimeException("Không thể xóa hóa đơn đã xác nhận!");
        }

        invoiceRepository.deleteById(id);
    }

    public Integer calculateRemainingAmount(Integer shiftId) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new RuntimeException("Shift not found"));
        return shift.getRemainingAmount();
    }
}