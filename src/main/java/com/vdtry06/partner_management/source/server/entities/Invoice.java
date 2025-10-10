package com.vdtry06.partner_management.source.server.entities;

import com.vdtry06.partner_management.lib.enumerated.InvoiceStatus;
import jakarta.persistence.*;
import lombok.Builder;

import java.io.Serializable;
import java.util.Date;

@Table(name = "tblInvoice", schema = "public")
@Builder
@Entity
public class Invoice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "invoice_date")
    private Date invoiceDate;

    @Column(name = "payment_amount")
    private Integer paymentAmount;

    @Column(name = "invoice_status")
    @Enumerated(EnumType.STRING)
    private InvoiceStatus invoiceStatus;

    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shiftId;

    @ManyToOne
    @JoinColumn(name = "accountant_id")
    private Accountant accountantId;

    public Invoice() {}

    public Invoice(Integer id, Date invoiceDate, Integer paymentAmount, InvoiceStatus invoiceStatus, Shift shiftId, Accountant accountantId) {
        this.id = id;
        this.invoiceDate = invoiceDate;
        this.paymentAmount = paymentAmount;
        this.invoiceStatus = invoiceStatus;
        this.shiftId = shiftId;
        this.accountantId = accountantId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Integer getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Integer paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public InvoiceStatus getInvoiceStatus() { return invoiceStatus; }

    public void setInvoiceStatus(InvoiceStatus invoiceStatus) { this.invoiceStatus = invoiceStatus; }

    public Shift getShiftId() {
        return shiftId;
    }

    public void setShiftId(Shift shiftId) {
        this.shiftId = shiftId;
    }

    public Accountant getAccountantId() {
        return accountantId;
    }

    public void setAccountantId(Accountant accountantId) {
        this.accountantId = accountantId;
    }
}
