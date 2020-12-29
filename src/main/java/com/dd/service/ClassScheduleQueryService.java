package com.dd.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.dd.domain.ClassSchedule;
import com.dd.domain.*; // for static metamodels
import com.dd.repository.ClassScheduleRepository;
import com.dd.service.dto.ClassScheduleCriteria;

/**
 * Service for executing complex queries for {@link ClassSchedule} entities in the database.
 * The main input is a {@link ClassScheduleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClassSchedule} or a {@link Page} of {@link ClassSchedule} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClassScheduleQueryService extends QueryService<ClassSchedule> {

    private final Logger log = LoggerFactory.getLogger(ClassScheduleQueryService.class);

    private final ClassScheduleRepository classScheduleRepository;

    public ClassScheduleQueryService(ClassScheduleRepository classScheduleRepository) {
        this.classScheduleRepository = classScheduleRepository;
    }

    /**
     * Return a {@link List} of {@link ClassSchedule} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClassSchedule> findByCriteria(ClassScheduleCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ClassSchedule> specification = createSpecification(criteria);
        return classScheduleRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ClassSchedule} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClassSchedule> findByCriteria(ClassScheduleCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ClassSchedule> specification = createSpecification(criteria);
        return classScheduleRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClassScheduleCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ClassSchedule> specification = createSpecification(criteria);
        return classScheduleRepository.count(specification);
    }

    /**
     * Function to convert {@link ClassScheduleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ClassSchedule> createSpecification(ClassScheduleCriteria criteria) {
        Specification<ClassSchedule> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ClassSchedule_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ClassSchedule_.name));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), ClassSchedule_.created));
            }
            if (criteria.getSchedule() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSchedule(), ClassSchedule_.schedule));
            }
            if (criteria.getUpdated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdated(), ClassSchedule_.updated));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), ClassSchedule_.createdBy));
            }
            if (criteria.getUpdatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUpdatedBy(), ClassSchedule_.updatedBy));
            }
            if (criteria.getConfirmedByStudent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfirmedByStudent(), ClassSchedule_.confirmedByStudent));
            }
            if (criteria.getConfirmedByTeacher() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfirmedByTeacher(), ClassSchedule_.confirmedByTeacher));
            }
            if (criteria.getComment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComment(), ClassSchedule_.comment));
            }
            if (criteria.getConnected() != null) {
                specification = specification.and(buildSpecification(criteria.getConnected(), ClassSchedule_.connected));
            }
            if (criteria.getReoccurring() != null) {
                specification = specification.and(buildSpecification(criteria.getReoccurring(), ClassSchedule_.reoccurring));
            }
            if (criteria.getStudentId() != null) {
                specification = specification.and(buildSpecification(criteria.getStudentId(),
                    root -> root.join(ClassSchedule_.student, JoinType.LEFT).get(Student_.id)));
            }
            if (criteria.getTeacherId() != null) {
                specification = specification.and(buildSpecification(criteria.getTeacherId(),
                    root -> root.join(ClassSchedule_.teacher, JoinType.LEFT).get(Teacher_.id)));
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(buildSpecification(criteria.getParentId(),
                    root -> root.join(ClassSchedule_.parent, JoinType.LEFT).get(Parent_.id)));
            }
            if (criteria.getCourseId() != null) {
                specification = specification.and(buildSpecification(criteria.getCourseId(),
                    root -> root.join(ClassSchedule_.course, JoinType.LEFT).get(Course_.id)));
            }
        }
        return specification;
    }
}
