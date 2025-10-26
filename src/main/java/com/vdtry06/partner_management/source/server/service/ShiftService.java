package com.vdtry06.partner_management.source.server.service;

import com.vdtry06.partner_management.lib.api.PaginationResponse;
import com.vdtry06.partner_management.lib.exceptions.BadRequestException;
import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.lib.service.BaseService;
import com.vdtry06.partner_management.lib.utils.PagingUtil;
import com.vdtry06.partner_management.source.server.config.language.MessageSourceHelper;
import com.vdtry06.partner_management.source.server.entities.Shift;
import com.vdtry06.partner_management.source.server.entities.TaskContract;
import com.vdtry06.partner_management.source.server.payload.shift.ShiftRequest;
import com.vdtry06.partner_management.source.server.payload.shift.ShiftResponse;
import com.vdtry06.partner_management.source.server.payload.shift.ShiftTimeResponse;
import com.vdtry06.partner_management.source.server.repositories.ShiftRepository;
import com.vdtry06.partner_management.source.server.repositories.spec.ShiftSpecification;
import com.vdtry06.partner_management.source.server.service.helpers.TaskContractHelperService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ShiftService extends BaseService<Shift, Integer> {
    private final ShiftRepository shiftRepository;
    private final TaskContractHelperService taskContractHelperService;
    private final MessageSourceHelper messageSourceHelper;

    protected ShiftService(BaseRepository<Shift, Integer> repository, ShiftRepository shiftRepository, TaskContractHelperService taskContractHelperService, MessageSourceHelper messageSourceHelper) {
        super(repository);
        this.shiftRepository = shiftRepository;
        this.taskContractHelperService = taskContractHelperService;
        this.messageSourceHelper = messageSourceHelper;
    }

    protected List<Shift> findAll(Specification<Shift> spec) {
        return shiftRepository.findAll(spec);
    }

    protected boolean existsByTaskContractId(Integer taskContractId) {
        TaskContract taskContract = taskContractHelperService.findById(taskContractId);
        return shiftRepository.existsByTaskContractId(taskContract);
    }

    protected Shift findById(Integer shiftId) {
        return shiftRepository.findById(shiftId)
                .orElseThrow(() -> new BadRequestException(messageSourceHelper.getMessage("error.not_found.shift")));
    }

    protected void updateRemainingAmount(Integer shiftId, Integer paymentAmount) {
        Shift shift = shiftRepository.findById(shiftId).orElse(null);
        Objects.requireNonNull(shift).setRemainingAmount(shift.getRemainingAmount() - paymentAmount);
        shiftRepository.save(shift);
    }

    @Transactional(rollbackFor = BadRequestException.class)
    public ShiftResponse createShift(Integer taskContractId, ShiftRequest shiftRequest) {
        TaskContract taskContract = taskContractHelperService.findById(taskContractId);

        Specification<Shift> spec = ShiftSpecification.hasTaskContractId(taskContractId)
                .and(ShiftSpecification.hasWorkDate(shiftRequest.getWorkDate()));

        List<Shift> shifts = shiftRepository.findAll(spec);

        List<ShiftTimeResponse> existingShifts = shifts.stream()
                .map(s -> new ShiftTimeResponse(s.getStartTime(), s.getEndTime()))
                .toList();

        LocalTime startTime = shiftRequest.getStartTime();
        LocalTime endTime = shiftRequest.getEndTime();

        boolean isPastTime = shiftRequest.getWorkDate().isBefore(LocalDate.now())
                || (shiftRequest.getWorkDate().isEqual(LocalDate.now())
                && (shiftRequest.getStartTime().isBefore(LocalTime.now())
                || shiftRequest.getEndTime().isBefore(LocalTime.now())));

        if (isPastTime) {
            throw new BadRequestException(messageSourceHelper.getMessage("error.time.must_be_future"));
        }

        if (shiftRequest.getStartTime().isAfter(shiftRequest.getEndTime())) {
            throw new BadRequestException(messageSourceHelper.getMessage("error.time.start_after_end"));
        }

        if (!isNonOverlapping(startTime, endTime, existingShifts)) {
            throw new BadRequestException(messageSourceHelper.getMessage("error.shift.overlap"));
        }

        if (shiftRequest.getWorkerCount() <= 0) {
            throw new BadRequestException("Số lượng nhân công phải > 0");
        }

        Shift shift = toShift(shiftRequest);
        shift.setTaskContractId(taskContract);
        shift = shiftRepository.save(shift);
        return toShiftResponse(shift);
    }

    protected List<ShiftResponse> getShiftListByTaskContractId(Integer taskContractId) {
        TaskContract taskContract = taskContractHelperService.findById(taskContractId);
        if (!shiftRepository.existsByTaskContractId(taskContract)) {
            throw new BadRequestException(messageSourceHelper.getMessage("error.not_found.task_contract"));
        }

        List<Shift> shifts = shiftRepository.findShiftListByTaskContractId(taskContractId);
        List<ShiftResponse> shiftResponses = new ArrayList<>();
        if (shifts != null) {
            shiftResponses = shifts
                    .stream()
                    .map(this::toShiftResponse)
                    .toList()
            ;
        }
        return shiftResponses;
    }

    public PaginationResponse<ShiftResponse> getAllShiftByTaskContractId(int page, int perPage, Integer taskContractId) {
        TaskContract taskContract = taskContractHelperService.findById(taskContractId);
        if (!shiftRepository.existsByTaskContractId(taskContract)) {
            throw new BadRequestException(messageSourceHelper.getMessage("error.not_found.task_contract"));
        }

        long totalRecord = shiftRepository.countAllShiftByTaskContractId(taskContractId);
        int offset = PagingUtil.getOffSet(page, perPage);
        int totalPage = PagingUtil.getTotalPage(totalRecord, perPage);
        List<Shift> shifts = shiftRepository.findAllShiftByTaskContractId(offset, perPage, taskContractId);

        List<ShiftResponse> shiftResponses = new ArrayList<>();
        if (shifts != null) {
            shiftResponses = shifts
                    .stream()
                    .map(this::toShiftResponse)
                    .toList()
            ;
        }
        return PaginationResponse.<ShiftResponse>builder()
                .page(page)
                .perPage(perPage)
                .data(shiftResponses)
                .totalPage(totalPage)
                .totalRecord(totalRecord)
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
                .remainingAmount(shiftRequest.getWorkerCount() * shiftRequest.getShiftUnitPrice())
                .description(shiftRequest.getDescription())
                .build();
    }

    protected ShiftResponse toShiftResponse(Shift shift) {
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
