package com.vdtry06.partner_management.source.server.service;

import com.vdtry06.partner_management.lib.exceptions.UnAuthorizationException;
import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.lib.service.BaseService;
import com.vdtry06.partner_management.source.server.config.language.MessageSourceHelper;
import com.vdtry06.partner_management.source.server.entities.Accountant;
import com.vdtry06.partner_management.source.server.entities.Employee;
import com.vdtry06.partner_management.source.server.repositories.AccountantRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountantService extends BaseService<Accountant, Integer> {
    private final EmployeeService employeeService;
    private final AccountantRepository accountantRepository;
    private final MessageSourceHelper messageSourceHelper;

    public AccountantService(BaseRepository<Accountant, Integer> repository, EmployeeService employeeService, AccountantRepository accountantRepository, MessageSourceHelper messageSourceHelper) {
        super(repository);
        this.employeeService = employeeService;
        this.accountantRepository = accountantRepository;
        this.messageSourceHelper = messageSourceHelper;
    }

    protected Accountant getCurrentAccountant() {
        String currentUsername = employeeService.getCurrentUsername();
        Employee employee = employeeService.findByUsername(currentUsername);
        return accountantRepository.findById(employee.getId())
                .orElseThrow(() -> new UnAuthorizationException(messageSourceHelper.getMessage("error.access.accountant_only")));
    }

}
