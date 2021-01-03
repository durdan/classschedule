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

import com.dd.domain.RequestStatus;
import com.dd.domain.*; // for static metamodels
import com.dd.repository.RequestStatusRepository;
import com.dd.service.dto.RequestStatusCriteria;

/**
 * Service for executing complex queries for {@link RequestStatus} entities in the database.
 * The main input is a {@link RequestStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RequestStatus} or a {@link Page} of {@link RequestStatus} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RequestStatusQueryService extends QueryService<RequestStatus> {

    private final Logger log = LoggerFactory.getLogger(RequestStatusQueryService.class);

    private final RequestStatusRepository requestStatusRepository;

    public RequestStatusQueryService(RequestStatusRepository requestStatusRepository) {
        this.requestStatusRepository = requestStatusRepository;
    }

    /**
     * Return a {@link List} of {@link RequestStatus} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RequestStatus> findByCriteria(RequestStatusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RequestStatus> specification = createSpecification(criteria);
        return requestStatusRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link RequestStatus} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RequestStatus> findByCriteria(RequestStatusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RequestStatus> specification = createSpecification(criteria);
        return requestStatusRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RequestStatusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RequestStatus> specification = createSpecification(criteria);
        return requestStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link RequestStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RequestStatus> createSpecification(RequestStatusCriteria criteria) {
        Specification<RequestStatus> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RequestStatus_.id));
            }
            if (criteria.getStatusCode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatusCode(), RequestStatus_.statusCode));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), RequestStatus_.status));
            }
        }
        return specification;
    }
}
