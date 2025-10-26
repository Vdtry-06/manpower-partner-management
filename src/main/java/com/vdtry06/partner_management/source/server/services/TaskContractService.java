package com.vdtry06.partner_management.source.server.services;

import com.vdtry06.partner_management.source.server.dto.taskcontract.TaskContractResponse;
import com.vdtry06.partner_management.source.server.entities.Contract;
import com.vdtry06.partner_management.source.server.entities.Task;
import com.vdtry06.partner_management.source.server.entities.TaskContract;
import com.vdtry06.partner_management.source.server.repositories.ContractRepository;
import com.vdtry06.partner_management.source.server.repositories.TaskContractRepository;
import com.vdtry06.partner_management.source.server.repositories.TaskRepository;
import com.vdtry06.partner_management.source.server.repositories.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskContractService {

    private final TaskContractRepository taskContractRepository;
    private final ContractRepository contractRepository;
    private final TaskRepository taskRepository;
    private final ShiftRepository shiftRepository;

    public List<TaskContractResponse> getTaskContractsByContractId(Integer contractId) {
        return taskContractRepository.findByContractId_Id(contractId).stream()
                .map(tc -> TaskContractResponse.fromEntity(tc, shiftRepository))
                .collect(Collectors.toList());
    }

    public TaskContract getTaskContractById(Integer id) {
        return taskContractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TaskContract not found with id: " + id));
    }

    @Transactional
    public Integer createTaskContract(Integer contractId, Integer taskId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found with id: " + contractId));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

        // Kiểm tra task đã được thêm vào contract chưa
        boolean exists = taskContractRepository.existsByContractIdAndTaskId(contract, task);
        if (exists) {
            throw new RuntimeException("Đầu việc này đã được thêm vào hợp đồng!");
        }

        TaskContract taskContract = TaskContract.builder()
                .contractId(contract)
                .taskId(task)
                .taskUnitPrice(0L) // Sẽ được tính từ shifts
                .build();

        TaskContract saved = taskContractRepository.save(taskContract);
        return saved.getId();
    }

    @Transactional
    public void updateTaskUnitPrice(Integer taskContractId) {
        TaskContract taskContract = getTaskContractById(taskContractId);

        // Tính đơn giá trung bình từ các shifts
        Long avgPrice = shiftRepository.calculateAverageUnitPrice(taskContractId);

        taskContract.setTaskUnitPrice(avgPrice != null ? avgPrice : 0L);
        taskContractRepository.save(taskContract);
    }

    public Integer calculateContractTotalValue(Integer contractId) {
        List<TaskContract> taskContracts = taskContractRepository.findByContractId_Id(contractId);

        int total = 0;
        for (TaskContract tc : taskContracts) {
            // Tính tổng từ các shifts
            Integer taskTotal = shiftRepository.calculateTotalValueByTaskContract(tc.getId());
            total += (taskTotal != null ? taskTotal : 0);
        }

        return total;
    }

    @Transactional
    public void deleteTaskContract(Integer id) {
        // Xóa tất cả shifts liên quan trước
        shiftRepository.deleteByTaskContractId_Id(id);

        taskContractRepository.deleteById(id);
    }

    @Transactional
    public void deleteByContractId(Integer contractId) {
        List<TaskContract> taskContracts = taskContractRepository.findByContractId_Id(contractId);

        for (TaskContract tc : taskContracts) {
            deleteTaskContract(tc.getId());
        }
    }
}