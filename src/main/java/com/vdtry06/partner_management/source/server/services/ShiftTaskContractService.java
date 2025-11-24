package com.vdtry06.partner_management.source.server.services;

import com.vdtry06.partner_management.lib.enumerated.ShiftType;
import com.vdtry06.partner_management.source.server.dto.shift.ShiftResponse;
import com.vdtry06.partner_management.source.server.dto.shift_task_contract.ShiftTaskContractRequest;
import com.vdtry06.partner_management.source.server.dto.shift_task_contract.ShiftTaskContractResponse;
import com.vdtry06.partner_management.source.server.entities.ShiftTaskContract;
import com.vdtry06.partner_management.source.server.entities.TaskContract;
import com.vdtry06.partner_management.source.server.repositories.ShiftRepository;
import com.vdtry06.partner_management.source.server.repositories.ShiftTaskContractRepository;
import com.vdtry06.partner_management.source.server.repositories.TaskContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftTaskContractService {
    private final ShiftTaskContractRepository shiftTaskContractRepository;
    private final ShiftRepository shiftRepository;
    private final TaskContractRepository taskContractRepository;

    @Transactional
    public void deleteByTaskContractId(Integer taskContractId) {
        List<ShiftTaskContract> shiftTaskContracts = shiftTaskContractRepository
                .findByTaskContractId_Id(taskContractId);

        for (ShiftTaskContract stc : shiftTaskContracts) {
            if (stc.getShiftId() != null) {
                shiftRepository.deleteById(stc.getShiftId().getId());
            }
        }

        shiftTaskContractRepository.deleteAll(shiftTaskContracts);
    }

    public List<ShiftResponse> getShiftsByShiftTaskContractId(Integer id) {
        ShiftTaskContract shiftTaskContract = getShiftTaskContractById(id);
        List<ShiftTaskContract> allShiftTaskContracts = shiftTaskContractRepository
                .findByTaskContractId_Id(shiftTaskContract.getTaskContractId().getId());
        return allShiftTaskContracts.stream()
                .filter(stc -> stc.getShiftId() != null)
                .map(stc -> toShiftResponse(stc))
                .collect(Collectors.toList());
    }

    @Transactional
    public Integer updateShiftTaskContract(Integer id, ShiftTaskContractRequest shiftTaskContractRequest) {
        ShiftTaskContract shiftTaskContract = getShiftTaskContractById(id);
        shiftTaskContract.setShiftId(shiftTaskContractRequest.getShiftId());
        shiftTaskContract.setShiftUnitPrice(shiftTaskContractRequest.getShiftUnitPrice());
        shiftTaskContract.setWorkerCount(shiftTaskContractRequest.getWorkerCount());

        shiftTaskContract = shiftTaskContractRepository.save(shiftTaskContract);
        return shiftTaskContract.getId();
    }

    public List<ShiftTaskContractResponse> getListShiftTaskContractByTaskContractId(Integer taskContractId) {
        return shiftTaskContractRepository.findByTaskContractId_Id(taskContractId).stream()
                .map(shiftTaskContract -> toShiftTaskContractResponse(shiftTaskContract))
                .collect(Collectors.toList());
    }

    public ShiftTaskContract getShiftTaskContractById(Integer id) {
        return shiftTaskContractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id ca làm trong đầu việc hợp đồng"));
    }

    public Integer createShiftTaskContract(Integer taskContractId) {
        TaskContract taskContract = taskContractRepository.findById(taskContractId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id của đầu việc trong hợp đồng"));

        ShiftTaskContract shiftTaskContract = ShiftTaskContract.builder()
                .taskContractId(taskContract)
                .shiftId(null)
                .shiftUnitPrice(0L)
                .workerCount(0)
                .build();

        shiftTaskContract = shiftTaskContractRepository.save(shiftTaskContract);
        return shiftTaskContract.getId();
    }

    public boolean checkDuplicateShift(Integer taskContractId, LocalDate workDate, ShiftType shiftType) {
        List<ShiftTaskContract> existingShifts = shiftTaskContractRepository
                .findByTaskContractId_Id(taskContractId);

        return existingShifts.stream()
                .filter(stc -> stc.getShiftId() != null)
                .anyMatch(stc ->
                        stc.getShiftId().getWorkDate().equals(workDate) &&
                                stc.getShiftId().getShiftType().equals(shiftType)
                );
    }

    @Transactional
    public void deleteNullShiftTaskContractsByContractId(Integer contractId) {
        List<TaskContract> taskContracts = taskContractRepository.findByContractId_Id(contractId);
        for (TaskContract taskContract : taskContracts) {
            List<ShiftTaskContract> shiftTaskContracts = shiftTaskContractRepository.findByTaskContractId_Id(taskContract.getId())
                    .stream()
                    .filter(stc -> stc.getShiftId() == null)
                    .collect(Collectors.toList());

            if (!shiftTaskContracts.isEmpty()) {
                shiftTaskContractRepository.deleteAll(shiftTaskContracts);
            }
        }
    }

    public long computeTotalValueByTaskContractId(Integer taskContractId) {
        return shiftTaskContractRepository.findByTaskContractId_Id(taskContractId).stream()
                .filter(stc -> stc.getShiftId() != null)
                .mapToLong(stc -> (long) stc.getWorkerCount() * stc.getShiftUnitPrice())
                .sum();
    }

    public Integer findLastShiftId(Integer taskContractId) {
        return shiftTaskContractRepository.findByTaskContractId_IdOrderByIdDesc(taskContractId).stream()
                .findFirst()
                .map(stc -> stc.getId())
                .orElse(null);
    }

    public long countShiftsByTaskContractId(Integer taskContractId) {
        return shiftTaskContractRepository.countByTaskContractId_IdAndShiftIsNotNull(taskContractId);
    }

    public boolean hasAtLeastOneShift(Integer taskContractId) {
        return countShiftsByTaskContractId(taskContractId) > 0;
    }

    private ShiftTaskContractResponse toShiftTaskContractResponse(ShiftTaskContract shiftTaskContract) {
        return ShiftTaskContractResponse.builder()
                .id(shiftTaskContract.getId())
                .workerCount(shiftTaskContract.getWorkerCount())
                .shiftUnitPrice(shiftTaskContract.getShiftUnitPrice())
                .taskContractId(shiftTaskContract.getTaskContractId())
                .shiftId(shiftTaskContract.getShiftId())
                .build();
    }

    private ShiftResponse toShiftResponse(ShiftTaskContract shiftTaskContract) {
        return ShiftResponse.builder()
                .id(shiftTaskContract.getShiftId().getId())
                .shiftType(shiftTaskContract.getShiftId().getShiftType())
                .description(shiftTaskContract.getShiftId().getDescription())
                .workDate(shiftTaskContract.getShiftId().getWorkDate())
                .workerCount(shiftTaskContract.getWorkerCount())
                .shiftUnitPrice(shiftTaskContract.getShiftUnitPrice())
                .build();
    }
}
