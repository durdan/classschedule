package com.dd.service;

import com.dd.domain.Teacher;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Teacher}.
 */
public interface TeacherService {

    /**
     * Save a teacher.
     *
     * @param teacher the entity to save.
     * @return the persisted entity.
     */
    Teacher save(Teacher teacher);

    /**
     * Get all the teachers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Teacher> findAll(Pageable pageable);


    /**
     * Get the "id" teacher.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Teacher> findOne(Long id);

    /**
     * Delete the "id" teacher.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
