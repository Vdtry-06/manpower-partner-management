package com.vdtry06.partner_management.source.server.repositories;

import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.source.server.entities.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends BaseRepository<Task, Integer> {
    Task findByNameTask(String nameTask);


    boolean existsByNameTask(String nameTask);

    @Query(
            value = "SELECT COUNT(*) " +
                    "FROM public.tbl_task t ",
            nativeQuery = true
    )
    long countAllTask();

    @Query(
            value = "SELECT * FROM public.tbl_task t " +
                    "LIMIT :limit OFFSET :offset ",
            nativeQuery = true
    )
    List<Task> findAllTask(
            @Param("offset") int offset,
            @Param("limit") int limit
    );
}
