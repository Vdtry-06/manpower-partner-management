package com.vdtry06.partner_management.source.server.service;

import com.vdtry06.partner_management.lib.enumerated.EmployeePostion;
import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.lib.service.BaseService;
import com.vdtry06.partner_management.source.server.entities.PartnerManager;
import com.vdtry06.partner_management.source.server.payload.employee.EmployeeResponse;
import com.vdtry06.partner_management.source.server.repositories.PartnerManagerRepository;
import org.springframework.stereotype.Service;

@Service
public class PartnerManagerService extends BaseService<PartnerManager, Integer> {
    private final EmployeeService employeeService;
    private final PartnerManagerRepository partnerManagerRepository;

    public PartnerManagerService(BaseRepository<PartnerManager, Integer> repository, EmployeeService employeeService, PartnerManagerRepository partnerManagerRepository) {
        super(repository);
        this.employeeService = employeeService;
        this.partnerManagerRepository = partnerManagerRepository;
    }


}
