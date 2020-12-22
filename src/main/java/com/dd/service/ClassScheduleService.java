package com.dd.service;

import com.dd.domain.ClassSchedule;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link ClassSchedule}.
 */
public interface ClassScheduleService {

    /**
     * Save a classSchedule.
     *
     * @param classSchedule the entity to save.
     * @return the persisted entity.
     */
    ClassSchedule save(ClassSchedule classSchedule);

    /**
     * Get all the classSchedules.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClassSchedule> findAll(Pageable pageable);


    /**
     * Get the "id" classSchedule.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClassSchedule> findOne(Long id);

    /**
     * Delete the "id" classSchedule.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
