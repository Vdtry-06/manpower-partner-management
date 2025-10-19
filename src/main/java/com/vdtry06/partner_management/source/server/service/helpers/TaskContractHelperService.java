package com.vdtry06.partner_management.source.server.service.helpers;

import com.vdtry06.partner_management.lib.exceptions.BadRequestException;
import com.vdtry06.partner_management.source.server.config.language.MessageSourceHelper;
import com.vdtry06.partner_management.source.server.entities.TaskContract;
import com.vdtry06.partner_management.source.server.repositories.TaskContractRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskContractHelperService {
    private final TaskContractRepository taskContractRepository;
    private final MessageSourceHelper messageSourceHelper;

    protected TaskContractHelperService(TaskContractRepository taskContractRepository, MessageSourceHelper messageSourceHelper) {
        this.taskContractRepository = taskContractRepository;
        this.messageSourceHelper = messageSourceHelper;
    }

    public TaskContract findById(Integer taskContractId) {
        return taskContractRepository.findById(taskContractId)
                .orElseThrow(() -> new BadRequestException(messageSourceHelper.getMessage("error.not_found.task_contract")));
    }
}
