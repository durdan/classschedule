package com.dd.repository;

import com.dd.domain.ClassSchedule;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ClassSchedule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, Long>, JpaSpecificationExecutor<ClassSchedule> {
}
