package com.vdtry06.partner_management.source.server.repositories;

import com.vdtry06.partner_management.source.server.entities.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Integer> {
    List<Shift> findByTaskContractId_Id(Integer taskContractId);

    Integer countByTaskContractId_Id(Integer taskContractId);

    void deleteByTaskContractId_Id(Integer taskContractId);

    @Query("SELECT AVG(s.shiftUnitPrice) FROM Shift s WHERE s.taskContractId.id = :taskContractId")
    Long calculateAverageUnitPrice(@Param("taskContractId") Integer taskContractId);

    @Query("SELECT SUM(s.shiftUnitPrice * s.workerCount) FROM Shift s WHERE s.taskContractId.id = :taskContractId")
    Integer calculateTotalValueByTaskContract(@Param("taskContractId") Integer taskContractId);

    @Query("SELECT s FROM Shift s WHERE s.remainingAmount > 0")
    List<Shift> findUnpaidShifts();

    @Query("SELECT s FROM Shift s WHERE s.taskContractId.contractId.id = :contractId")
    List<Shift> findByContractId(@Param("contractId") Integer contractId);
}