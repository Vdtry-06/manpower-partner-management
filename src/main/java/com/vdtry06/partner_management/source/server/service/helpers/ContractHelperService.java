package com.vdtry06.partner_management.source.server.service.helpers;

import com.vdtry06.partner_management.source.server.entities.Contract;
import com.vdtry06.partner_management.source.server.repositories.ContractRepository;
import org.springframework.stereotype.Service;

@Service
public class ContractHelperService {
    private final ContractRepository contractRepository;

    protected ContractHelperService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    public Contract findById(Integer id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hợp đồng"));
    }

    public boolean existsById(Integer contractId) {
        return contractRepository.existsById(contractId);
    }
}