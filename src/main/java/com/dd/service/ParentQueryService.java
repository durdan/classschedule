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

import com.dd.domain.Parent;
import com.dd.domain.*; // for static metamodels
import com.dd.repository.ParentRepository;
import com.dd.service.dto.ParentCriteria;

/**
 * Service for executing complex queries for {@link Parent} entities in the database.
 * The main input is a {@link ParentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Parent} or a {@link Page} of {@link Parent} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ParentQueryService extends QueryService<Parent> {

    private final Logger log = LoggerFactory.getLogger(ParentQueryService.class);

    private final ParentRepository parentRepository;

    public ParentQueryService(ParentRepository parentRepository) {
        this.parentRepository = parentRepository;
    }

    /**
     * Return a {@link List} of {@link Parent} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Parent> findByCriteria(ParentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Parent> specification = createSpecification(criteria);
        return parentRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Parent} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Parent> findByCriteria(ParentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Parent> specification = createSpecification(criteria);
        return parentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ParentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Parent> specification = createSpecification(criteria);
        return parentRepository.count(specification);
    }

    /**
     * Function to convert {@link ParentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Parent> createSpecification(ParentCriteria criteria) {
        Specification<Parent> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Parent_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Parent_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Parent_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Parent_.email));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Parent_.phone));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Parent_.createdBy));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), Parent_.created));
            }
            if (criteria.getUpdated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdated(), Parent_.updated));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Parent_.user, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
