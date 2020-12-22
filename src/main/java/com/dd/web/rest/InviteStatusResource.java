package com.dd.web.rest;

import com.dd.domain.InviteStatus;
import com.dd.service.InviteStatusService;
import com.dd.web.rest.errors.BadRequestAlertException;
import com.dd.service.dto.InviteStatusCriteria;
import com.dd.service.InviteStatusQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.dd.domain.InviteStatus}.
 */
@RestController
@RequestMapping("/api")
public class InviteStatusResource {

    private final Logger log = LoggerFactory.getLogger(InviteStatusResource.class);

    private static final String ENTITY_NAME = "inviteStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InviteStatusService inviteStatusService;

    private final InviteStatusQueryService inviteStatusQueryService;

    public InviteStatusResource(InviteStatusService inviteStatusService, InviteStatusQueryService inviteStatusQueryService) {
        this.inviteStatusService = inviteStatusService;
        this.inviteStatusQueryService = inviteStatusQueryService;
    }

    /**
     * {@code POST  /invite-statuses} : Create a new inviteStatus.
     *
     * @param inviteStatus the inviteStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inviteStatus, or with status {@code 400 (Bad Request)} if the inviteStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/invite-statuses")
    public ResponseEntity<InviteStatus> createInviteStatus(@RequestBody InviteStatus inviteStatus) throws URISyntaxException {
        log.debug("REST request to save InviteStatus : {}", inviteStatus);
        if (inviteStatus.getId() != null) {
            throw new BadRequestAlertException("A new inviteStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InviteStatus result = inviteStatusService.save(inviteStatus);
        return ResponseEntity.created(new URI("/api/invite-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /invite-statuses} : Updates an existing inviteStatus.
     *
     * @param inviteStatus the inviteStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inviteStatus,
     * or with status {@code 400 (Bad Request)} if the inviteStatus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inviteStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/invite-statuses")
    public ResponseEntity<InviteStatus> updateInviteStatus(@RequestBody InviteStatus inviteStatus) throws URISyntaxException {
        log.debug("REST request to update InviteStatus : {}", inviteStatus);
        if (inviteStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InviteStatus result = inviteStatusService.save(inviteStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inviteStatus.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /invite-statuses} : get all the inviteStatuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inviteStatuses in body.
     */
    @GetMapping("/invite-statuses")
    public ResponseEntity<List<InviteStatus>> getAllInviteStatuses(InviteStatusCriteria criteria, Pageable pageable) {
        log.debug("REST request to get InviteStatuses by criteria: {}", criteria);
        Page<InviteStatus> page = inviteStatusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /invite-statuses/count} : count all the inviteStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/invite-statuses/count")
    public ResponseEntity<Long> countInviteStatuses(InviteStatusCriteria criteria) {
        log.debug("REST request to count InviteStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(inviteStatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /invite-statuses/:id} : get the "id" inviteStatus.
     *
     * @param id the id of the inviteStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inviteStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/invite-statuses/{id}")
    public ResponseEntity<InviteStatus> getInviteStatus(@PathVariable Long id) {
        log.debug("REST request to get InviteStatus : {}", id);
        Optional<InviteStatus> inviteStatus = inviteStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inviteStatus);
    }

    /**
     * {@code DELETE  /invite-statuses/:id} : delete the "id" inviteStatus.
     *
     * @param id the id of the inviteStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/invite-statuses/{id}")
    public ResponseEntity<Void> deleteInviteStatus(@PathVariable Long id) {
        log.debug("REST request to delete InviteStatus : {}", id);
        inviteStatusService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
