package com.vdtry06.partner_management.source.server.repositories.spec;

import com.vdtry06.partner_management.source.server.entities.Contract;
import org.springframework.data.jpa.domain.Specification;

public class ContractSpecification {
    public static Specification<Contract> hasPartnerId(Integer partnerId) {
        return (root, query, cb)
                -> cb.equal(root.get("partnerId"), partnerId);
    }

    public static Specification<Contract> hasContractId(Integer contractId) {
        return (root, query, cb)
                -> cb.equal(root.get("contractId"), contractId);
    }
}
