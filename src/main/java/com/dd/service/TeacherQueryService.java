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

import com.dd.domain.Teacher;
import com.dd.domain.*; // for static metamodels
import com.dd.repository.TeacherRepository;
import com.dd.service.dto.TeacherCriteria;

/**
 * Service for executing complex queries for {@link Teacher} entities in the database.
 * The main input is a {@link TeacherCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Teacher} or a {@link Page} of {@link Teacher} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TeacherQueryService extends QueryService<Teacher> {

    private final Logger log = LoggerFactory.getLogger(TeacherQueryService.class);

    private final TeacherRepository teacherRepository;

    public TeacherQueryService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    /**
     * Return a {@link List} of {@link Teacher} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Teacher> findByCriteria(TeacherCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Teacher> specification = createSpecification(criteria);
        return teacherRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Teacher} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Teacher> findByCriteria(TeacherCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Teacher> specification = createSpecification(criteria);
        return teacherRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TeacherCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Teacher> specification = createSpecification(criteria);
        return teacherRepository.count(specification);
    }

    /**
     * Function to convert {@link TeacherCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Teacher> createSpecification(TeacherCriteria criteria) {
        Specification<Teacher> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Teacher_.id));
            }
            if (criteria.getProfileContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProfileContent(), Teacher_.profileContent));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Teacher_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Teacher_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Teacher_.email));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Teacher_.phone));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Teacher_.createdBy));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), Teacher_.created));
            }
            if (criteria.getUpdated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdated(), Teacher_.updated));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Teacher_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getParentId() != null) {
                specification = specification.and(buildSpecification(criteria.getParentId(),
                    root -> root.join(Teacher_.parent, JoinType.LEFT).get(Parent_.id)));
            }
            if (criteria.getStudentId() != null) {
                specification = specification.and(buildSpecification(criteria.getStudentId(),
                    root -> root.join(Teacher_.student, JoinType.LEFT).get(Student_.id)));
            }
        }
        return specification;
    }
}
