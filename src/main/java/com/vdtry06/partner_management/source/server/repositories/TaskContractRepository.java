package com.vdtry06.partner_management.source.server.repositories;

import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.source.server.entities.Contract;
import com.vdtry06.partner_management.source.server.entities.TaskContract;

import java.util.List;

public interface TaskContractRepository extends BaseRepository<TaskContract, Integer> {

    List<TaskContract> findByContractId(Contract contract);
}
