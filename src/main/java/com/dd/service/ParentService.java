package com.dd.service;

import com.dd.domain.Parent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Parent}.
 */
public interface ParentService {

    /**
     * Save a parent.
     *
     * @param parent the entity to save.
     * @return the persisted entity.
     */
    Parent save(Parent parent);

    /**
     * Get all the parents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Parent> findAll(Pageable pageable);


    /**
     * Get the "id" parent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Parent> findOne(Long id);

    /**
     * Delete the "id" parent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
