package com.dd.repository;

import com.dd.domain.RequestTask;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the RequestTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequestTaskRepository extends JpaRepository<RequestTask, Long>, JpaSpecificationExecutor<RequestTask> {

    @Query("select requestTask from RequestTask requestTask where requestTask.user.login = ?#{principal.username}")
    List<RequestTask> findByUserIsCurrentUser();
}
