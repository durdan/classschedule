package com.dd.web.rest;

import com.dd.domain.RequestTask;
import com.dd.service.RequestTaskService;
import com.dd.web.rest.errors.BadRequestAlertException;
import com.dd.service.dto.RequestTaskCriteria;
import com.dd.service.RequestTaskQueryService;

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
 * REST controller for managing {@link com.dd.domain.RequestTask}.
 */
@RestController
@RequestMapping("/api")
public class RequestTaskResource {

    private final Logger log = LoggerFactory.getLogger(RequestTaskResource.class);

    private static final String ENTITY_NAME = "requestTask";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequestTaskService requestTaskService;

    private final RequestTaskQueryService requestTaskQueryService;

    public RequestTaskResource(RequestTaskService requestTaskService, RequestTaskQueryService requestTaskQueryService) {
        this.requestTaskService = requestTaskService;
        this.requestTaskQueryService = requestTaskQueryService;
    }

    /**
     * {@code POST  /request-tasks} : Create a new requestTask.
     *
     * @param requestTask the requestTask to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requestTask, or with status {@code 400 (Bad Request)} if the requestTask has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/request-tasks")
    public ResponseEntity<RequestTask> createRequestTask(@RequestBody RequestTask requestTask) throws URISyntaxException {
        log.debug("REST request to save RequestTask : {}", requestTask);
        if (requestTask.getId() != null) {
            throw new BadRequestAlertException("A new requestTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RequestTask result = requestTaskService.save(requestTask);
        return ResponseEntity.created(new URI("/api/request-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /request-tasks} : Updates an existing requestTask.
     *
     * @param requestTask the requestTask to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requestTask,
     * or with status {@code 400 (Bad Request)} if the requestTask is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requestTask couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/request-tasks")
    public ResponseEntity<RequestTask> updateRequestTask(@RequestBody RequestTask requestTask) throws URISyntaxException {
        log.debug("REST request to update RequestTask : {}", requestTask);
        if (requestTask.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RequestTask result = requestTaskService.save(requestTask);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, requestTask.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /request-tasks} : get all the requestTasks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requestTasks in body.
     */
    @GetMapping("/request-tasks")
    public ResponseEntity<List<RequestTask>> getAllRequestTasks(RequestTaskCriteria criteria, Pageable pageable) {
        log.debug("REST request to get RequestTasks by criteria: {}", criteria);
        Page<RequestTask> page = requestTaskQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /request-tasks/count} : count all the requestTasks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/request-tasks/count")
    public ResponseEntity<Long> countRequestTasks(RequestTaskCriteria criteria) {
        log.debug("REST request to count RequestTasks by criteria: {}", criteria);
        return ResponseEntity.ok().body(requestTaskQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /request-tasks/:id} : get the "id" requestTask.
     *
     * @param id the id of the requestTask to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requestTask, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/request-tasks/{id}")
    public ResponseEntity<RequestTask> getRequestTask(@PathVariable Long id) {
        log.debug("REST request to get RequestTask : {}", id);
        Optional<RequestTask> requestTask = requestTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(requestTask);
    }

    /**
     * {@code DELETE  /request-tasks/:id} : delete the "id" requestTask.
     *
     * @param id the id of the requestTask to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/request-tasks/{id}")
    public ResponseEntity<Void> deleteRequestTask(@PathVariable Long id) {
        log.debug("REST request to delete RequestTask : {}", id);
        requestTaskService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
