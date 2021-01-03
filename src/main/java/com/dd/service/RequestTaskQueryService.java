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

import com.dd.domain.RequestTask;
import com.dd.domain.*; // for static metamodels
import com.dd.repository.RequestTaskRepository;
import com.dd.service.dto.RequestTaskCriteria;

/**
 * Service for executing complex queries for {@link RequestTask} entities in the database.
 * The main input is a {@link RequestTaskCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RequestTask} or a {@link Page} of {@link RequestTask} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RequestTaskQueryService extends QueryService<RequestTask> {

    private final Logger log = LoggerFactory.getLogger(RequestTaskQueryService.class);

    private final RequestTaskRepository requestTaskRepository;

    public RequestTaskQueryService(RequestTaskRepository requestTaskRepository) {
        this.requestTaskRepository = requestTaskRepository;
    }

    /**
     * Return a {@link List} of {@link RequestTask} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RequestTask> findByCriteria(RequestTaskCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RequestTask> specification = createSpecification(criteria);
        return requestTaskRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link RequestTask} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RequestTask> findByCriteria(RequestTaskCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RequestTask> specification = createSpecification(criteria);
        return requestTaskRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RequestTaskCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RequestTask> specification = createSpecification(criteria);
        return requestTaskRepository.count(specification);
    }

    /**
     * Function to convert {@link RequestTaskCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RequestTask> createSpecification(RequestTaskCriteria criteria) {
        Specification<RequestTask> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RequestTask_.id));
            }
            if (criteria.getRequestedUserId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRequestedUserId(), RequestTask_.requestedUserId));
            }
            if (criteria.getRequestCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRequestCode(), RequestTask_.requestCode));
            }
            if (criteria.getRequestType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRequestType(), RequestTask_.requestType));
            }
            if (criteria.getRequiredActionFromUserId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRequiredActionFromUserId(), RequestTask_.requiredActionFromUserId));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), RequestTask_.createdBy));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), RequestTask_.created));
            }
            if (criteria.getUpdated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdated(), RequestTask_.updated));
            }
            if (criteria.getRequestStatusId() != null) {
                specification = specification.and(buildSpecification(criteria.getRequestStatusId(),
                    root -> root.join(RequestTask_.requestStatus, JoinType.LEFT).get(RequestStatus_.id)));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(RequestTask_.user, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
