package com.vdtry06.partner_management.source.server.service.helpers;

import com.vdtry06.partner_management.lib.exceptions.BadRequestException;
import com.vdtry06.partner_management.source.server.config.language.MessageSourceHelper;
import com.vdtry06.partner_management.source.server.entities.Contract;
import com.vdtry06.partner_management.source.server.repositories.ContractRepository;
import org.springframework.stereotype.Service;

@Service
public class ContractHelperService {
    private final ContractRepository contractRepository;
    private final MessageSourceHelper messageSourceHelper;

    protected ContractHelperService(ContractRepository contractRepository, MessageSourceHelper messageSourceHelper) {
        this.contractRepository = contractRepository;
        this.messageSourceHelper = messageSourceHelper;
    }

    public Contract findById(Integer id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(messageSourceHelper.getMessage("error.not_found.contract")));
    }

    public boolean existsById(Integer contractId) {
        return contractRepository.existsById(contractId);
    }
}