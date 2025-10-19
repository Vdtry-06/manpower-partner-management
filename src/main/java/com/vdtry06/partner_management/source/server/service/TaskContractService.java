package com.vdtry06.partner_management.source.server.service;

import com.vdtry06.partner_management.lib.api.PaginationResponse;
import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.lib.service.BaseService;
import com.vdtry06.partner_management.lib.utils.PagingUtil;
import com.vdtry06.partner_management.source.server.entities.Contract;
import com.vdtry06.partner_management.source.server.entities.Shift;
import com.vdtry06.partner_management.source.server.entities.Task;
import com.vdtry06.partner_management.source.server.entities.TaskContract;
import com.vdtry06.partner_management.source.server.payload.shift.ShiftResponse;
import com.vdtry06.partner_management.source.server.payload.task.TaskResponse;
import com.vdtry06.partner_management.source.server.payload.taskcontract.ShiftTaskListContractResponse;
import com.vdtry06.partner_management.source.server.payload.taskcontract.TaskContractResponse;
import com.vdtry06.partner_management.source.server.repositories.TaskContractRepository;
import com.vdtry06.partner_management.source.server.repositories.spec.ShiftSpecification;
import com.vdtry06.partner_management.source.server.service.helpers.ContractHelperService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskContractService extends BaseService<TaskContract, Integer> {
    private final TaskContractRepository taskContractRepository;
    private final ShiftService shiftService;
    private final TaskService taskService;
    private final ContractHelperService contractHelperService;

    protected TaskContractService(BaseRepository<TaskContract, Integer> repository, TaskContractRepository taskContractRepository, ShiftService shiftService, TaskService taskService, ContractHelperService contractHelperService) {
        super(repository);
        this.taskContractRepository = taskContractRepository;
        this.shiftService = shiftService;
        this.taskService = taskService;
        this.contractHelperService = contractHelperService;
    }

    private Integer totalUnitPriceByTaskContractId(Integer taskContractId) {
        if (!shiftService.existsByTaskContractId(taskContractId)) {
            throw new RuntimeException("Không tìm thấy id của đầu việc trong hợp đồng");
        }
        Specification<Shift> spec = ShiftSpecification.hasTaskContractId(taskContractId);

        List<Shift> shifts = shiftService.findAll(spec);

        int sumPrice = 0;
        for (Shift shift : shifts) {
            sumPrice += shift.getShiftUnitPrice() * shift.getWorkerCount();
        }
        return sumPrice;
    }

    private LocalDate endDateShiftOfTaskContractId(Integer taskContractId) {
        if (!shiftService.existsByTaskContractId(taskContractId)) {
            throw new RuntimeException("Không tìm thấy id của đầu việc trong hợp đồng");
        }
        Specification<Shift> spec = ShiftSpecification.hasTaskContractId(taskContractId);

        List<Shift> shifts = shiftService.findAll(spec);
        LocalDate endDate = LocalDate.now();
        for (Shift shift : shifts) {
            if (endDate.isBefore(shift.getWorkDate())) {
                endDate = shift.getWorkDate();
            }
        }
        return endDate;
    }

    public PaginationResponse<ShiftTaskListContractResponse> getShiftTaskListOfContract(int page, int perPage, Integer contractId) {
        if (!contractHelperService.existsById(contractId)) throw new RuntimeException("Không tìm thấy id của hợp đồng");
        List<TaskContract> taskContracts = taskContractRepository.findByContractId(contractHelperService.findById(contractId));

        List<ShiftTaskListContractResponse> shiftTaskListContractResponses = new ArrayList<>();
        for (TaskContract taskContract : taskContracts) {
            TaskResponse taskResponse = taskService.getTaskById(taskContract.getTaskId().getId());
            List<ShiftResponse> shiftResponses = shiftService.getShiftListByTaskContractId(taskContract.getId());

            for (ShiftResponse shiftResponse : shiftResponses) {
                shiftTaskListContractResponses.add(
                        ShiftTaskListContractResponse.builder()
                                .taskContractId(taskContract.getId())
                                .taskResponse(taskResponse)
                                .shiftResponse(shiftResponse)
                                .build()
                );
            }
        }

        long totalRecord = shiftTaskListContractResponses.size();
        int offset = PagingUtil.getOffSet(page, perPage);
        int totalPage = PagingUtil.getTotalPage(totalRecord, perPage);

        List<ShiftTaskListContractResponse> data = shiftTaskListContractResponses
                .stream()
                .skip(offset)
                .limit(perPage)
                .toList();

        return PaginationResponse.<ShiftTaskListContractResponse>builder()
                .page(page)
                .perPage(perPage)
                .data(data)
                .totalPage(totalPage)
                .totalRecord(totalRecord)
                .build();
    }

    protected String getListNameTasks(Integer contractId) {
        Contract contract = contractHelperService.findById(contractId);
        List<TaskContract> taskContracts = taskContractRepository.findByContractId(contract);
        String description = "";
        for (TaskContract taskContract : taskContracts) {
            description += taskService.getNameTask(taskContract.getTaskId().getId()) + ", ";
        }
        description = description.substring(0, description.length() - 2);
        return description;
    }

    protected Integer totalTasksUnitPrice(Integer contractId) {
        Contract contract = contractHelperService.findById(contractId);
        List<TaskContract> taskContracts = taskContractRepository.findByContractId(contract);
        Integer totalPrice = 0;
        for (TaskContract taskContract : taskContracts) {
            totalPrice += updateTaskUnitPrice(taskContract.getId());
        }
        return totalPrice;
    }

    private Integer updateTaskUnitPrice(Integer taskContractId) {
        TaskContract taskContract = taskContractRepository.findById(taskContractId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id của đầu việc trong hợp đồng"));

        taskContract.setTaskUnitPrice(totalUnitPriceByTaskContractId(taskContractId));
        taskContract = taskContractRepository.save(taskContract);
        return taskContract.getTaskUnitPrice();
    }

    protected LocalDate endDateContract(Integer contractId) {
        Contract contract = contractHelperService.findById(contractId);
        List<TaskContract> taskContracts = taskContractRepository.findByContractId(contract);
        LocalDate endDate = LocalDate.now();
        for (TaskContract taskContract : taskContracts) {
            LocalDate endDateByTask = endDateShiftOfTaskContractId(taskContract.getId());
            if (endDate.isBefore(endDateByTask)) {
                endDate = endDateByTask;
            }
        }
        return endDate;
    }

    public TaskContractResponse createTaskContract(Integer contractId, Integer taskId) {
        Contract contract = contractHelperService.findById(contractId);
        Task task = taskService.findById(taskId);
        TaskContract taskContract = TaskContract.builder()
                .taskUnitPrice(0)
                .build();
        taskContract.setContractId(contract);
        taskContract.setTaskId(task);
        taskContract = taskContractRepository.save(taskContract);
        return toTaskContractResponse(taskContract);
    }

    public TaskContractResponse toTaskContractResponse(TaskContract taskContract) {
        new TaskContractResponse();
        return TaskContractResponse.builder()
                .id(taskContract.getId())
                .contractId(taskContract.getContractId().getId())
                .taskId(taskContract.getTaskId().getId())
                .taskUnitPrice(taskContract.getTaskUnitPrice())
                .build();
    }
}
