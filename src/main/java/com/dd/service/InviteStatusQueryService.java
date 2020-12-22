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

import com.dd.domain.InviteStatus;
import com.dd.domain.*; // for static metamodels
import com.dd.repository.InviteStatusRepository;
import com.dd.service.dto.InviteStatusCriteria;

/**
 * Service for executing complex queries for {@link InviteStatus} entities in the database.
 * The main input is a {@link InviteStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InviteStatus} or a {@link Page} of {@link InviteStatus} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InviteStatusQueryService extends QueryService<InviteStatus> {

    private final Logger log = LoggerFactory.getLogger(InviteStatusQueryService.class);

    private final InviteStatusRepository inviteStatusRepository;

    public InviteStatusQueryService(InviteStatusRepository inviteStatusRepository) {
        this.inviteStatusRepository = inviteStatusRepository;
    }

    /**
     * Return a {@link List} of {@link InviteStatus} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InviteStatus> findByCriteria(InviteStatusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InviteStatus> specification = createSpecification(criteria);
        return inviteStatusRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link InviteStatus} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InviteStatus> findByCriteria(InviteStatusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InviteStatus> specification = createSpecification(criteria);
        return inviteStatusRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InviteStatusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InviteStatus> specification = createSpecification(criteria);
        return inviteStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link InviteStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InviteStatus> createSpecification(InviteStatusCriteria criteria) {
        Specification<InviteStatus> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InviteStatus_.id));
            }
            if (criteria.getStatusCode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatusCode(), InviteStatus_.statusCode));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), InviteStatus_.status));
            }
        }
        return specification;
    }
}
