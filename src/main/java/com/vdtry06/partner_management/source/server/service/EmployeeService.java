package com.vdtry06.partner_management.source.server.service;

import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.lib.service.BaseService;
import com.vdtry06.partner_management.source.server.entities.Employee;
import com.vdtry06.partner_management.source.server.repositories.EmployeeRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService extends BaseService<Employee, Integer> {
    private final EmployeeRepository employeeRepository;

    protected EmployeeService(BaseRepository<Employee, Integer> repository, EmployeeRepository employeeRepository) {
        super(repository);
        this.employeeRepository = employeeRepository;
    }

    protected Employee findByUsername(String username) {
        return employeeRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("không tìm thấy tài khoản nhân viên"));
    }

    protected String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated())
            throw new RuntimeException("Người dùng chưa đăng nhập");

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails)
            return ((UserDetails) principal).getUsername();

        throw new RuntimeException("Không thể xác định người dùng hiện tại");
    }
}
