package com.vdtry06.partner_management.source.server.service.helpers;

import com.vdtry06.partner_management.source.server.entities.TaskContract;
import com.vdtry06.partner_management.source.server.repositories.TaskContractRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskContractHelperService {
    private final TaskContractRepository taskContractRepository;

    protected TaskContractHelperService(TaskContractRepository taskContractRepository) {
        this.taskContractRepository = taskContractRepository;
    }

    public TaskContract findById(Integer taskContractId) {
        return taskContractRepository.findById(taskContractId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id của đầu việc trong hợp đồng"));
    }
}
