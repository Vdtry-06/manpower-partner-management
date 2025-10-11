package com.vdtry06.partner_management.source.server.service;

import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.lib.service.BaseService;
import com.vdtry06.partner_management.source.server.entities.Employee;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService extends BaseService<Employee, Integer> {
    protected EmployeeService(BaseRepository<Employee, Integer> repository) {
        super(repository);
    }

}
