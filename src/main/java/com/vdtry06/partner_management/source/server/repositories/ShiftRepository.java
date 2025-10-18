package com.vdtry06.partner_management.source.server.repositories;

import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.source.server.entities.Shift;
import com.vdtry06.partner_management.source.server.entities.TaskContract;
import com.vdtry06.partner_management.source.server.payload.shift.ShiftTimeResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ShiftRepository extends BaseRepository<Shift, Integer> {

    boolean existsByWorkDate(LocalDate workDate);


    boolean existsBystartTime(LocalTime startTime);


    boolean existsByTaskContractId(TaskContract taskContractId);

    @Query(
            value = "SELECT s.start_time AS startTime, s.end_time AS endTime " +
                    "FROM public.tbl_shift s " +
                    "WHERE s.task_contract_id = :taskContractId " +
                    "AND s.work_date = :workDate",
            nativeQuery = true
    )
    List<ShiftTimeResponse> findAllShiftTimeByTaskContractIdAndWorkDate(
            @Param("taskContractId") Integer taskContractId,
            @Param("workDate") LocalDate workDate
    );

    @Query(
            value = "SELECT * " +
                    "FROM public.tbl_shift s " +
                    "WHERE s.task_contract_id = :taskContractId",
            nativeQuery = true
    )
    List<Shift> findAllShiftByTaskContractId(
            @Param("taskContractId") Integer taskContractId
    );

    boolean existsByTaskContractIdAndWorkDate(TaskContract taskContractId, LocalDate workDate);

    @Query(
            value = "SELECT COUNT(*) " +
                    "FROM public.tbl_shift s " +
                    "WHERE s.task_contract_id = :taskContractId",
            nativeQuery = true
    )
    long countAllShiftByTaskContractId(
            @Param("taskContractId") Integer taskContractId
    );
}
