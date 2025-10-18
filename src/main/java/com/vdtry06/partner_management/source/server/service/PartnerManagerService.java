package com.vdtry06.partner_management.source.server.service;

import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.lib.service.BaseService;
import com.vdtry06.partner_management.source.server.entities.Employee;
import com.vdtry06.partner_management.source.server.entities.PartnerManager;
import com.vdtry06.partner_management.source.server.repositories.EmployeeRepository;
import com.vdtry06.partner_management.source.server.repositories.PartnerManagerRepository;
import org.springframework.stereotype.Service;

@Service
public class PartnerManagerService extends BaseService<PartnerManager, Integer> {
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;
    private final PartnerManagerRepository partnerManagerRepository;

    public PartnerManagerService(BaseRepository<PartnerManager, Integer> repository, EmployeeService employeeService, EmployeeRepository employeeRepository, PartnerManagerRepository partnerManagerRepository) {
        super(repository);
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
        this.partnerManagerRepository = partnerManagerRepository;
    }

    protected PartnerManager getCurrentPartnerManager() {
        String currentUsername = employeeService.getCurrentUsername();
        Employee employee = employeeRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("không tìm thấy tài khoản nhân viên"));
        PartnerManager partnerManager = partnerManagerRepository.findById(employee.getId())
                .orElseThrow(() -> new RuntimeException("Chỉ nhân viên quản lý đối tác mới được vào"));
        return partnerManager;
    }
}
