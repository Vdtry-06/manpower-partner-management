package com.vdtry06.partner_management.source.server.services;

import com.vdtry06.partner_management.source.server.dto.shift.ShiftRequest;
import com.vdtry06.partner_management.source.server.dto.shift.ShiftResponse;
import com.vdtry06.partner_management.source.server.entities.Shift;
import com.vdtry06.partner_management.source.server.entities.TaskContract;
import com.vdtry06.partner_management.source.server.repositories.ShiftRepository;
import com.vdtry06.partner_management.source.server.repositories.TaskContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final TaskContractRepository taskContractRepository;
    private final TaskContractService taskContractService;

    public List<ShiftResponse> getShiftsByTaskContractId(Integer taskContractId) {
        return shiftRepository.findByTaskContractId_Id(taskContractId).stream()
                .map(ShiftResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public Shift getShiftById(Integer id) {
        return shiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shift not found with id: " + id));
    }

    @Transactional
    public Shift createShift(ShiftRequest request, Integer taskContractId) {
        TaskContract taskContract = taskContractRepository.findById(taskContractId)
                .orElseThrow(() -> new RuntimeException("TaskContract not found with id: " + taskContractId));

        // Tính remaining amount = worker count (ban đầu chưa thanh toán)
        Integer remainingAmount = request.getWorkerCount();

        Shift shift = Shift.builder()
                .workDate(request.getWorkDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .workerCount(request.getWorkerCount())
                .shiftUnitPrice(request.getShiftUnitPrice())
                .remainingAmount(remainingAmount)
                .description(request.getDescription())
                .taskContractId(taskContract)
                .build();

        Shift savedShift = shiftRepository.save(shift);

        // Cập nhật lại taskUnitPrice của TaskContract
        taskContractService.updateTaskUnitPrice(taskContractId);

        return savedShift;
    }

    @Transactional
    public Shift updateShift(Integer id, ShiftRequest request) {
        Shift shift = getShiftById(id);

        shift.setWorkDate(request.getWorkDate());
        shift.setStartTime(request.getStartTime());
        shift.setEndTime(request.getEndTime());
        shift.setWorkerCount(request.getWorkerCount());
        shift.setShiftUnitPrice(request.getShiftUnitPrice());
        shift.setDescription(request.getDescription());

        // Cập nhật remaining amount nếu thay đổi số lượng công nhân
        Integer paid = shift.getWorkerCount() - shift.getRemainingAmount();
        shift.setRemainingAmount(request.getWorkerCount() - paid);

        Shift savedShift = shiftRepository.save(shift);

        // Cập nhật lại taskUnitPrice của TaskContract
        taskContractService.updateTaskUnitPrice(shift.getTaskContractId().getId());

        return savedShift;
    }

    @Transactional
    public void deleteShift(Integer id) {
        Shift shift = getShiftById(id);
        Integer taskContractId = shift.getTaskContractId().getId();

        shiftRepository.deleteById(id);

        // Cập nhật lại taskUnitPrice của TaskContract
        taskContractService.updateTaskUnitPrice(taskContractId);
    }

    public Long calculateShiftTotalValue(Integer shiftId) {
        Shift shift = getShiftById(shiftId);
        return shift.getShiftUnitPrice() * shift.getWorkerCount();
    }

    public Integer countShiftsByTaskContract(Integer taskContractId) {
        return shiftRepository.countByTaskContractId_Id(taskContractId);
    }
}
