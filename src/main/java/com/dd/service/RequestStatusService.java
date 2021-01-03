package com.dd.service;

import com.dd.domain.RequestStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link RequestStatus}.
 */
public interface RequestStatusService {

    /**
     * Save a requestStatus.
     *
     * @param requestStatus the entity to save.
     * @return the persisted entity.
     */
    RequestStatus save(RequestStatus requestStatus);

    /**
     * Get all the requestStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RequestStatus> findAll(Pageable pageable);


    /**
     * Get the "id" requestStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RequestStatus> findOne(Long id);

    /**
     * Delete the "id" requestStatus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
