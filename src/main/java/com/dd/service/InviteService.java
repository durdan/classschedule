package com.dd.service;

import com.dd.domain.Invite;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Invite}.
 */
public interface InviteService {

    /**
     * Save a invite.
     *
     * @param invite the entity to save.
     * @return the persisted entity.
     */
    Invite save(Invite invite);

    /**
     * Get all the invites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Invite> findAll(Pageable pageable);


    /**
     * Get the "id" invite.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Invite> findOne(Long id);

    /**
     * Delete the "id" invite.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
