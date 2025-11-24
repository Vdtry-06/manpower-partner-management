package com.vdtry06.partner_management.source.server.repositories;

import com.vdtry06.partner_management.source.server.entities.Shift;
import com.vdtry06.partner_management.source.server.entities.ShiftTaskContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftTaskContractRepository extends JpaRepository<ShiftTaskContract, Integer> {
    List<ShiftTaskContract> findByTaskContractId_Id(Integer taskContractId);

    List<ShiftTaskContract> findByTaskContractId_IdOrderByIdDesc(Integer taskContractId);

//    List<ShiftTaskContract> findByShiftId_Id(Integer shiftId);

    Optional<ShiftTaskContract> findByShiftId(Shift shift);

    @Query(value = "SELECT COUNT(*) FROM tbl_shift_task_contract stc " +
            "WHERE stc.task_contract_id = :taskContractId " +
            "AND stc.shift_id IS NOT NULL",
            nativeQuery = true)
    long countByTaskContractId_IdAndShiftIsNotNull(@Param("taskContractId") Integer taskContractId);
}
