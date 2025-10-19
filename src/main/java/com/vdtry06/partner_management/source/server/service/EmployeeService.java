package com.vdtry06.partner_management.source.server.service;

import com.vdtry06.partner_management.lib.exceptions.BadRequestException;
import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.lib.service.BaseService;
import com.vdtry06.partner_management.source.server.config.language.MessageSourceHelper;
import com.vdtry06.partner_management.source.server.entities.Employee;
import com.vdtry06.partner_management.source.server.repositories.EmployeeRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService extends BaseService<Employee, Integer> {
    private final EmployeeRepository employeeRepository;
    private final MessageSourceHelper messageSourceHelper;

    protected EmployeeService(BaseRepository<Employee, Integer> repository, EmployeeRepository employeeRepository, MessageSourceHelper messageSourceHelper) {
        super(repository);
        this.employeeRepository = employeeRepository;
        this.messageSourceHelper = messageSourceHelper;
    }

    protected Employee findByUsername(String username) {
        return employeeRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException(messageSourceHelper.getMessage("error.not_found.username")));
    }

    protected String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated())
            throw new BadRequestException(messageSourceHelper.getMessage("warning.unauthorized"));

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails)
            return ((UserDetails) principal).getUsername();

        throw new BadRequestException(messageSourceHelper.getMessage("warning.unauthorized.current_username"));
    }
}
