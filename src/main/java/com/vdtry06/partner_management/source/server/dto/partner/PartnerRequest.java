package com.vdtry06.partner_management.source.server.dto.partner;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerRequest {
    @NotBlank(message = "Tên đối tác không được để trống")
    private String namePartner;

    @NotBlank(message = "Người đại diện không được để trống")
    private String partnerRepresentative;

    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại phải có 10 chữ số và bắt đầu bằng 0")
    private String phoneNumber;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;
    private String address;
    private String taxCode;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate connperationDate;
    private String description;
}
