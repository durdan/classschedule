package com.dd.web.rest;

import com.dd.domain.RequestStatus;
import com.dd.service.RequestStatusService;
import com.dd.web.rest.errors.BadRequestAlertException;
import com.dd.service.dto.RequestStatusCriteria;
import com.dd.service.RequestStatusQueryService;

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
 * REST controller for managing {@link com.dd.domain.RequestStatus}.
 */
@RestController
@RequestMapping("/api")
public class RequestStatusResource {

    private final Logger log = LoggerFactory.getLogger(RequestStatusResource.class);

    private static final String ENTITY_NAME = "requestStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequestStatusService requestStatusService;

    private final RequestStatusQueryService requestStatusQueryService;

    public RequestStatusResource(RequestStatusService requestStatusService, RequestStatusQueryService requestStatusQueryService) {
        this.requestStatusService = requestStatusService;
        this.requestStatusQueryService = requestStatusQueryService;
    }

    /**
     * {@code POST  /request-statuses} : Create a new requestStatus.
     *
     * @param requestStatus the requestStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requestStatus, or with status {@code 400 (Bad Request)} if the requestStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/request-statuses")
    public ResponseEntity<RequestStatus> createRequestStatus(@RequestBody RequestStatus requestStatus) throws URISyntaxException {
        log.debug("REST request to save RequestStatus : {}", requestStatus);
        if (requestStatus.getId() != null) {
            throw new BadRequestAlertException("A new requestStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RequestStatus result = requestStatusService.save(requestStatus);
        return ResponseEntity.created(new URI("/api/request-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /request-statuses} : Updates an existing requestStatus.
     *
     * @param requestStatus the requestStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requestStatus,
     * or with status {@code 400 (Bad Request)} if the requestStatus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requestStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/request-statuses")
    public ResponseEntity<RequestStatus> updateRequestStatus(@RequestBody RequestStatus requestStatus) throws URISyntaxException {
        log.debug("REST request to update RequestStatus : {}", requestStatus);
        if (requestStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RequestStatus result = requestStatusService.save(requestStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, requestStatus.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /request-statuses} : get all the requestStatuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requestStatuses in body.
     */
    @GetMapping("/request-statuses")
    public ResponseEntity<List<RequestStatus>> getAllRequestStatuses(RequestStatusCriteria criteria, Pageable pageable) {
        log.debug("REST request to get RequestStatuses by criteria: {}", criteria);
        Page<RequestStatus> page = requestStatusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /request-statuses/count} : count all the requestStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/request-statuses/count")
    public ResponseEntity<Long> countRequestStatuses(RequestStatusCriteria criteria) {
        log.debug("REST request to count RequestStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(requestStatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /request-statuses/:id} : get the "id" requestStatus.
     *
     * @param id the id of the requestStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requestStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/request-statuses/{id}")
    public ResponseEntity<RequestStatus> getRequestStatus(@PathVariable Long id) {
        log.debug("REST request to get RequestStatus : {}", id);
        Optional<RequestStatus> requestStatus = requestStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(requestStatus);
    }

    /**
     * {@code DELETE  /request-statuses/:id} : delete the "id" requestStatus.
     *
     * @param id the id of the requestStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/request-statuses/{id}")
    public ResponseEntity<Void> deleteRequestStatus(@PathVariable Long id) {
        log.debug("REST request to delete RequestStatus : {}", id);
        requestStatusService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
