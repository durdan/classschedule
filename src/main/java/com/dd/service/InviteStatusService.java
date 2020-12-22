package com.dd.service;

import com.dd.domain.InviteStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link InviteStatus}.
 */
public interface InviteStatusService {

    /**
     * Save a inviteStatus.
     *
     * @param inviteStatus the entity to save.
     * @return the persisted entity.
     */
    InviteStatus save(InviteStatus inviteStatus);

    /**
     * Get all the inviteStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InviteStatus> findAll(Pageable pageable);


    /**
     * Get the "id" inviteStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InviteStatus> findOne(Long id);

    /**
     * Delete the "id" inviteStatus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
