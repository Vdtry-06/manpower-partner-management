package com.vdtry06.partner_management.source.server.payload.partnermanager;
import com.vdtry06.partner_management.lib.enumerated.EmployeePostion;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartnerManagerResponse {
    private Integer id;
    private String username;
    private String fullname;
    @Enumerated(EnumType.STRING)
    private EmployeePostion position;
}
