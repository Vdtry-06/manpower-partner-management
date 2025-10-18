package com.vdtry06.partner_management.source.server.service;

import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.lib.service.BaseService;
import com.vdtry06.partner_management.source.server.entities.Accountant;
import com.vdtry06.partner_management.source.server.entities.Employee;
import com.vdtry06.partner_management.source.server.repositories.AccountantRepository;
import com.vdtry06.partner_management.source.server.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountantService extends BaseService<Accountant, Integer> {
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;
    private final AccountantRepository accountantRepository;

    public AccountantService(BaseRepository<Accountant, Integer> repository, EmployeeService employeeService, EmployeeRepository employeeRepository, AccountantRepository accountantRepository) {
        super(repository);
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
        this.accountantRepository = accountantRepository;
    }

    protected Accountant getCurrentAccountant() {
        String currentUsername = employeeService.getCurrentUsername();
        Employee employee = employeeRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("không tìm thấy tài khoản nhân viên"));
        Accountant accountant = accountantRepository.findById(employee.getId())
                .orElseThrow(() -> new RuntimeException("Chỉ nhân viên kế toán mới được vào"));
        return accountant;
    }

}
