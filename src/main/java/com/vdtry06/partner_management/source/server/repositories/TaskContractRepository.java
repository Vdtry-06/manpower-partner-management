package com.vdtry06.partner_management.source.server.repositories;

import com.vdtry06.partner_management.source.server.entities.Contract;
import com.vdtry06.partner_management.source.server.entities.Task;
import com.vdtry06.partner_management.source.server.entities.TaskContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskContractRepository extends JpaRepository<TaskContract, Integer> {
    List<TaskContract> findByContractId_Id(Integer contractId);
    List<TaskContract> findByTaskId_Id(Integer taskId);
    boolean existsByContractIdAndTaskId(Contract contractId, Task taskId);
}