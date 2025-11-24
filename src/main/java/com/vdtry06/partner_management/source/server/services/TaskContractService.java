package com.vdtry06.partner_management.source.server.services;

import com.vdtry06.partner_management.source.server.dto.task_contract.TaskContractResponse;
import com.vdtry06.partner_management.source.server.dto.task_contract.TaskContractSelectionResponse;
import com.vdtry06.partner_management.source.server.entities.Contract;
import com.vdtry06.partner_management.source.server.entities.ShiftTaskContract;
import com.vdtry06.partner_management.source.server.entities.Task;
import com.vdtry06.partner_management.source.server.entities.TaskContract;
import com.vdtry06.partner_management.source.server.repositories.ContractRepository;
import com.vdtry06.partner_management.source.server.repositories.TaskContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskContractService {
    private final TaskContractRepository taskContractRepository;
    private final ContractRepository contractRepository;
    private final TaskService taskService;

    private final ShiftTaskContractService shiftTaskContractService;

    public TaskContractSelectionResponse getSelectionData(Integer contractId) {
        List<TaskContractResponse> taskContractResponses = getTaskContractByContractId(contractId);

        Map<Integer, Integer> taskContractMap = taskContractResponses.stream()
                .collect(Collectors.toMap(s -> s.getTaskId(), s -> s.getId()));

        Map<Integer, Integer> shiftMap = taskContractResponses.stream()
                .collect(Collectors.toMap(s -> s.getTaskId(), s -> shiftTaskContractService.findLastShiftId(s.getId())));

        return TaskContractSelectionResponse.builder()
                .selectedTasks(taskContractResponses)
                .taskContractMap(taskContractMap)
                .shiftTaskContractMap(shiftMap)
                .build();
    }

    @Transactional
    public void deleteByContractId(Integer contractId) {
        List<TaskContract> taskContracts = taskContractRepository.findByContractId_Id(contractId);

        for (TaskContract taskContract : taskContracts) {
            deleteTaskContract(taskContract.getId());
        }
    }

    @Transactional
    public void deleteTaskContract(Integer id) {
        shiftTaskContractService.deleteByTaskContractId(id);
        taskContractRepository.deleteById(id);
    }

    public List<TaskContractResponse> getTaskContractByContractId(Integer contractId) {
        return taskContractRepository.findByContractId_Id(contractId).stream()
                .map(taskContract -> toTaskContractResponse(taskContract))
                .collect(Collectors.toList());
    }

    public Integer createTaskContract(Integer contractId, Integer taskId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id của hợp đồng"));

        Task task = taskService.getTaskById(taskId);

        boolean exits = taskContractRepository.existsByContractIdAndTaskId(contract, task);
        // có thể xóa bỏ để update
        if (exits) throw new RuntimeException("Đầu việc này đã được thêm vào hợp đồng");

        TaskContract taskContract = TaskContract.builder()
                .contractId(contract)
                .taskId(task)
                .taskUnitPrice(0L)
                .build();

        taskContract = taskContractRepository.save(taskContract);
        return taskContract.getId();
    }

    public TaskContractResponse getTaskContractResponseById(Integer id) {
        return toTaskContractResponse(getTaskContractById(id));
    }

    public TaskContract getTaskContractById(Integer id) {
        return taskContractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id của đầu việc trong hợp đồng"));
    }

    @Transactional
    public void updateTaskUnitPrice(Integer taskContractId) {
        long total = shiftTaskContractService.computeTotalValueByTaskContractId(taskContractId);
        TaskContract taskContract = getTaskContractById(taskContractId);
        taskContract.setTaskUnitPrice(total);
        taskContractRepository.save(taskContract);
    }

    private TaskContractResponse toTaskContractResponse(TaskContract taskContract) {
        return TaskContractResponse.builder()
                .id(taskContract.getId())
                .taskId(taskContract.getTaskId().getId())
                .taskName(taskContract.getTaskId().getNameTask())
                .taskDescription(taskContract.getTaskId().getDescription())
                .taskUnitPrice(taskContract.getTaskUnitPrice())
                .build();
    }
}
