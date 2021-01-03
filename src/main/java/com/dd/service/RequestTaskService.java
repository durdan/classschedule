package com.dd.service;

import com.dd.domain.RequestTask;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link RequestTask}.
 */
public interface RequestTaskService {

    /**
     * Save a requestTask.
     *
     * @param requestTask the entity to save.
     * @return the persisted entity.
     */
    RequestTask save(RequestTask requestTask);

    /**
     * Get all the requestTasks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RequestTask> findAll(Pageable pageable);


    /**
     * Get the "id" requestTask.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RequestTask> findOne(Long id);

    /**
     * Delete the "id" requestTask.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
