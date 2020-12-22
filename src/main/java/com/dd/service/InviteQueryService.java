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

import com.dd.domain.Invite;
import com.dd.domain.*; // for static metamodels
import com.dd.repository.InviteRepository;
import com.dd.service.dto.InviteCriteria;

/**
 * Service for executing complex queries for {@link Invite} entities in the database.
 * The main input is a {@link InviteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Invite} or a {@link Page} of {@link Invite} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InviteQueryService extends QueryService<Invite> {

    private final Logger log = LoggerFactory.getLogger(InviteQueryService.class);

    private final InviteRepository inviteRepository;

    public InviteQueryService(InviteRepository inviteRepository) {
        this.inviteRepository = inviteRepository;
    }

    /**
     * Return a {@link List} of {@link Invite} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Invite> findByCriteria(InviteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Invite> specification = createSpecification(criteria);
        return inviteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Invite} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Invite> findByCriteria(InviteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Invite> specification = createSpecification(criteria);
        return inviteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InviteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Invite> specification = createSpecification(criteria);
        return inviteRepository.count(specification);
    }

    /**
     * Function to convert {@link InviteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Invite> createSpecification(InviteCriteria criteria) {
        Specification<Invite> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Invite_.id));
            }
            if (criteria.getRequestedUserId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRequestedUserId(), Invite_.requestedUserId));
            }
            if (criteria.getInvitedUserId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvitedUserId(), Invite_.invitedUserId));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Invite_.createdBy));
            }
            if (criteria.getCreated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated(), Invite_.created));
            }
            if (criteria.getUpdated() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdated(), Invite_.updated));
            }
            if (criteria.getInvitestatusId() != null) {
                specification = specification.and(buildSpecification(criteria.getInvitestatusId(),
                    root -> root.join(Invite_.invitestatus, JoinType.LEFT).get(InviteStatus_.id)));
            }
        }
        return specification;
    }
}
