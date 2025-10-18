package com.vdtry06.partner_management.source.server.service;

import com.vdtry06.partner_management.lib.api.PaginationResponse;
import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.lib.service.BaseService;
import com.vdtry06.partner_management.lib.utils.PagingUtil;
import com.vdtry06.partner_management.source.server.entities.Contract;
import com.vdtry06.partner_management.source.server.entities.Task;
import com.vdtry06.partner_management.source.server.entities.TaskContract;
import com.vdtry06.partner_management.source.server.payload.shift.ShiftResponse;
import com.vdtry06.partner_management.source.server.payload.task.TaskResponse;
import com.vdtry06.partner_management.source.server.payload.taskcontract.ShiftTaskListContractResponse;
import com.vdtry06.partner_management.source.server.payload.taskcontract.TaskContractResponse;
import com.vdtry06.partner_management.source.server.repositories.ContractRepository;
import com.vdtry06.partner_management.source.server.repositories.TaskContractRepository;
import com.vdtry06.partner_management.source.server.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskContractService extends BaseService<TaskContract, Integer> {
    public final TaskContractRepository taskContractRepository;
    public final ContractRepository contractRepository;
    public final TaskRepository taskRepository;
    public final ShiftService shiftService;
    public final TaskService taskService;
    public final ShiftTaskCalculationService shiftTaskCalculationService;

    public TaskContractService(BaseRepository<TaskContract, Integer> repository, TaskContractRepository taskContractRepository, ContractRepository contractRepository, TaskRepository taskRepository, ShiftService shiftService, TaskService taskService, ShiftTaskCalculationService shiftTaskCalculationService) {
        super(repository);
        this.taskContractRepository = taskContractRepository;
        this.contractRepository = contractRepository;
        this.taskRepository = taskRepository;
        this.shiftService = shiftService;
        this.taskService = taskService;
        this.shiftTaskCalculationService = shiftTaskCalculationService;
    }

    public PaginationResponse<ShiftTaskListContractResponse> getShiftTaskListOfContract(int page, int perPage, Integer contractId) {
        if (!contractRepository.existsById(contractId)) throw new RuntimeException("Không tìm thấy id của hợp đồng");
        List<TaskContract> taskContracts = taskContractRepository.findByContractId(contractRepository.findById(contractId).orElse(null));

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
        Contract contract = contractRepository.findById(contractId).orElse(null);
        List<TaskContract> taskContracts = taskContractRepository.findByContractId(contract);
        String description = "";
        for (TaskContract taskContract : taskContracts) {
            description += taskService.getNameTask(taskContract.getTaskId().getId()) + ", ";
        }
        description = description.substring(0, description.length() - 2);
        return description;
    }

    protected Integer totalTasksUnitPrice(Integer contractId) {
        Contract contract = contractRepository.findById(contractId).orElse(null);
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

        taskContract.setTaskUnitPrice(shiftTaskCalculationService.totalUnitPriceByTaskContractId(taskContractId));
        taskContract = taskContractRepository.save(taskContract);
        return taskContract.getTaskUnitPrice();
    }

    protected LocalDate endDateContract(Integer contractId) {
        Contract contract = contractRepository.findById(contractId).orElse(null);
        List<TaskContract> taskContracts = taskContractRepository.findByContractId(contract);
        LocalDate endDate = LocalDate.now();
        for (TaskContract taskContract : taskContracts) {
            LocalDate endDateByTask = shiftTaskCalculationService.endDateShiftOfTaskContractId(taskContract.getId());
            if (endDate.isBefore(endDateByTask)) {
                endDate = endDateByTask;
            }
        }
        return endDate;
    }

    public TaskContractResponse createTaskContract(Integer contractId, Integer taskId) {
        Contract contract = contractRepository.findById(contractId).orElse(null);
        Task task = taskRepository.findById(taskId).orElse(null);
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
