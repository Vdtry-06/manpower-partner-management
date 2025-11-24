package com.vdtry06.partner_management.source.server.services;

import com.vdtry06.partner_management.source.server.dto.shift.ShiftRequest;
import com.vdtry06.partner_management.source.server.dto.shift.ShiftResponse;
import com.vdtry06.partner_management.source.server.dto.shift_task_contract.ShiftTaskContractRequest;
import com.vdtry06.partner_management.source.server.entities.Shift;
import com.vdtry06.partner_management.source.server.entities.ShiftTaskContract;
import com.vdtry06.partner_management.source.server.repositories.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final ShiftTaskContractService shiftTaskContractService;

    public List<ShiftResponse> getShiftsByShiftTaskContractId(Integer id) {
        return shiftTaskContractService.getShiftsByShiftTaskContractId(id);
    }

    public Integer createShift(Integer shiftTaskContractId, ShiftRequest shiftRequest) {
        ShiftTaskContract shiftTaskContract = shiftTaskContractService.getShiftTaskContractById(shiftTaskContractId);

        Integer taskContractId = shiftTaskContract.getTaskContractId().getId();

        boolean isDuplicate = shiftTaskContractService.checkDuplicateShift(
                taskContractId,
                shiftRequest.getWorkDate(),
                shiftRequest.getShiftType()
        );

        if (isDuplicate) {
            throw new RuntimeException("Ca làm việc " + shiftRequest.getShiftType().name() +
                    " vào ngày " + shiftRequest.getWorkDate() + " đã tồn tại trong đầu việc này!");
        }

        Shift shift = toShift(shiftRequest);
        shift = shiftRepository.save(shift);

        ShiftTaskContractRequest shiftTaskContractRequest = ShiftTaskContractRequest.builder()
                .shiftId(shift)
                .shiftUnitPrice(shiftRequest.getShiftUnitPrice())
                .workerCount(shiftRequest.getWorkerCount())
                .build();

        shiftTaskContractService.updateShiftTaskContract(shiftTaskContract.getId(), shiftTaskContractRequest);

        return shiftTaskContractService.createShiftTaskContract(taskContractId);
    }

    private Shift toShift(ShiftRequest shiftRequest) {
        return Shift.builder()
                .workDate(shiftRequest.getWorkDate())
                .shiftType(shiftRequest.getShiftType())
                .description(shiftRequest.getDescription())
                .invoice(null)
                .build();
    }
}
