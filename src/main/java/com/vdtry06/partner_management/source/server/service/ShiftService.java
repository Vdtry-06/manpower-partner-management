package com.vdtry06.partner_management.source.server.service;

import com.vdtry06.partner_management.lib.api.PaginationResponse;
import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.lib.service.BaseService;
import com.vdtry06.partner_management.source.server.entities.Shift;
import com.vdtry06.partner_management.source.server.entities.TaskContract;
import com.vdtry06.partner_management.source.server.payload.shift.ShiftRequest;
import com.vdtry06.partner_management.source.server.payload.shift.ShiftResponse;
import com.vdtry06.partner_management.source.server.payload.shift.ShiftTimeResponse;
import com.vdtry06.partner_management.source.server.repositories.ShiftRepository;
import com.vdtry06.partner_management.source.server.repositories.TaskContractRepository;
import com.vdtry06.partner_management.source.server.repositories.spec.ShiftSpecification;
import org.apache.coyote.BadRequestException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShiftService extends BaseService<Shift, Integer> {
    private final ShiftRepository shiftRepository;
    private final TaskContractRepository taskContractRepository;

    public ShiftService(BaseRepository<Shift, Integer> repository, ShiftRepository shiftRepository, TaskContractRepository taskContractRepository) {
        super(repository);
        this.shiftRepository = shiftRepository;
        this.taskContractRepository = taskContractRepository;
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

    @Transactional(rollbackFor = BadRequestException.class)
    public ShiftResponse createShift(Integer taskContractId, ShiftRequest shiftRequest) {
        TaskContract taskContract = taskContractRepository.findById(taskContractId).orElse(null);

        Specification<Shift> spec = ShiftSpecification.hasTaskContractId(taskContractId)
                .and(ShiftSpecification.hasWorkDate(shiftRequest.getWorkDate()));

        List<Shift> shifts = shiftRepository.findAll(spec);

        List<ShiftTimeResponse> existingShifts = shifts.stream()
                .map(s -> new ShiftTimeResponse(s.getStartTime(), s.getEndTime()))
                .toList();

        LocalTime startTime = shiftRequest.getStartTime();
        LocalTime endTime = shiftRequest.getEndTime();

        if (!isNonOverlapping(startTime, endTime, existingShifts)) {
            throw new RuntimeException("Bị trùng ca làm trước đó");
        }

//        if (shiftRepository.existsByTaskContractIdAndWorkDate(taskContract, shiftRequest.getWorkDate())) {
//            LocalTime startTime = shiftRequest.getStartTime();
//            LocalTime endTime = shiftRequest.getEndTime();
//            List<ShiftTimeResponse> shifts = shiftRepository.findAllShiftTimeByTaskContractIdAndWorkDate(taskContractId, shiftRequest.getWorkDate());
//            if (!isNonOverlapping(startTime, endTime, shifts)) {
//                throw new RuntimeException("Bị trùng ca làm trước đó");
//            }
//        }

        Shift shift = toShift(shiftRequest);
        shift.setTaskContractId(taskContract);
        shift = shiftRepository.save(shift);
        return toShiftResponse(shift);
    }

    public PaginationResponse<ShiftResponse> getAllShiftByTaskContractId(Integer taskContractId) {
        TaskContract taskContract = taskContractRepository.findById(taskContractId).orElse(null);
        if (!shiftRepository.existsByTaskContractId(taskContract)) {
            throw new RuntimeException("Không tìm thấy id của đầu việc trong hợp đồng");
        }
        Specification<Shift> spec = ShiftSpecification.hasTaskContractId(taskContractId);

        List<Shift> shifts = shiftRepository.findAll(spec);

        List<ShiftResponse> shiftResponses = new ArrayList<>();
        if (shifts != null) {
            shiftResponses = shifts
                    .stream()
                    .map(this::toShiftResponse)
                    .toList()
            ;
        }
        return PaginationResponse.<ShiftResponse>builder()
                .data(shiftResponses)
                .build();
    }

    private boolean isNonOverlapping(LocalTime startTime, LocalTime endTime, List<ShiftTimeResponse> shifts) {
        for (ShiftTimeResponse shift : shifts) {
            LocalTime s = shift.getStartTime();
            LocalTime e = shift.getEndTime();

            if (!(endTime.isBefore(s) || startTime.isAfter(e))) {
                return false;
            }
        }
        return true;
    }

    private Shift toShift(ShiftRequest shiftRequest) {
        new Shift();
        return Shift.builder()
                .workDate(shiftRequest.getWorkDate())
                .startTime(shiftRequest.getStartTime())
                .endTime(shiftRequest.getEndTime())
                .workerCount(shiftRequest.getWorkerCount())
                .shiftUnitPrice(shiftRequest.getShiftUnitPrice())
                .remainingAmount(0)
                .description(shiftRequest.getDescription())
                .build();
    }

    private ShiftResponse toShiftResponse(Shift shift) {
        new ShiftResponse();
        return ShiftResponse.builder()
                .id(shift.getId())
                .workDate(shift.getWorkDate())
                .startTime(shift.getStartTime())
                .endTime(shift.getEndTime())
                .workerCount(shift.getWorkerCount())
                .shiftUnitPrice(shift.getShiftUnitPrice())
                .remainingAmount(shift.getRemainingAmount())
                .description(shift.getDescription())
                .build();
    }
}
