package com.vdtry06.partner_management.source.server.service;

import com.vdtry06.partner_management.lib.exceptions.UnAuthorizationException;
import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.lib.service.BaseService;
import com.vdtry06.partner_management.source.server.config.language.MessageSourceHelper;
import com.vdtry06.partner_management.source.server.entities.Employee;
import com.vdtry06.partner_management.source.server.entities.PartnerManager;
import com.vdtry06.partner_management.source.server.repositories.PartnerManagerRepository;
import org.springframework.stereotype.Service;

@Service
public class PartnerManagerService extends BaseService<PartnerManager, Integer> {
    private final EmployeeService employeeService;
    private final PartnerManagerRepository partnerManagerRepository;
    private final MessageSourceHelper messageSourceHelper;

    protected PartnerManagerService(BaseRepository<PartnerManager, Integer> repository, EmployeeService employeeService, PartnerManagerRepository partnerManagerRepository, MessageSourceHelper messageSourceHelper) {
        super(repository);
        this.employeeService = employeeService;
        this.partnerManagerRepository = partnerManagerRepository;
        this.messageSourceHelper = messageSourceHelper;
    }

    protected PartnerManager getCurrentPartnerManager() {
        String currentUsername = employeeService.getCurrentUsername();
        Employee employee = employeeService.findByUsername(currentUsername);
        return partnerManagerRepository.findById(employee.getId())
                .orElseThrow(() -> new UnAuthorizationException(messageSourceHelper.getMessage("error.access.partner_manager_only")));
    }
}
