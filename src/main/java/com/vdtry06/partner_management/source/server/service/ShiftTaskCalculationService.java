package com.vdtry06.partner_management.source.server.service;

import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.lib.service.BaseService;
import com.vdtry06.partner_management.source.server.entities.Shift;
import com.vdtry06.partner_management.source.server.entities.TaskContract;
import com.vdtry06.partner_management.source.server.repositories.ShiftRepository;
import com.vdtry06.partner_management.source.server.repositories.TaskContractRepository;
import com.vdtry06.partner_management.source.server.repositories.spec.ShiftSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ShiftTaskCalculationService extends BaseService<Shift, Integer> {
    private final TaskContractRepository taskContractRepository;
    private final ShiftRepository shiftRepository;

    public ShiftTaskCalculationService(BaseRepository<Shift, Integer> repository, TaskContractRepository taskContractRepository, ShiftRepository shiftRepository) {
        super(repository);
        this.taskContractRepository = taskContractRepository;
        this.shiftRepository = shiftRepository;
    }


    protected Integer totalUnitPriceByTaskContractId(Integer taskContractId) {
        TaskContract taskContract = taskContractRepository.findById(taskContractId).orElse(null);
        if (!shiftRepository.existsByTaskContractId(taskContract)) {
            throw new RuntimeException("Không tìm thấy id của đầu việc trong hợp đồng");
        }
        Specification<Shift> spec = ShiftSpecification.hasTaskContractId(taskContractId);

        List<Shift> shifts = shiftRepository.findAll(spec);

        Integer sumPrice = 0;
        for (Shift shift : shifts) {
            sumPrice += shift.getShiftUnitPrice() * shift.getWorkerCount();
        }
        return sumPrice;
    }

    protected LocalDate endDateShiftOfTaskContractId(Integer taskContractId) {
        TaskContract taskContract = taskContractRepository.findById(taskContractId).orElse(null);
        if (!shiftRepository.existsByTaskContractId(taskContract)) {
            throw new RuntimeException("Không tìm thấy id của đầu việc trong hợp đồng");
        }
        Specification<Shift> spec = ShiftSpecification.hasTaskContractId(taskContractId);

        List<Shift> shifts = shiftRepository.findAll(spec);
        LocalDate endDate = LocalDate.now();
        for (Shift shift : shifts) {
            if (endDate.isBefore(shift.getWorkDate())) {
                endDate = shift.getWorkDate();
            }
        }
        return endDate;
    }
}
