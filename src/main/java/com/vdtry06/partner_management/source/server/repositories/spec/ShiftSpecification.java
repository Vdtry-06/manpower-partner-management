package com.vdtry06.partner_management.source.server.repositories.spec;

import com.vdtry06.partner_management.source.server.entities.Shift;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ShiftSpecification {
    public static Specification<Shift> hasTaskContractId(Integer taskContractId) {
        return (root, query, cb)
                -> cb.equal(root.get("taskContractId").get("id"), taskContractId);
    }

    public static Specification<Shift> hasWorkDate(LocalDate workDate) {
        return (root, query, cb)
                -> cb.equal(root.get("workDate"), workDate);
    }
}
