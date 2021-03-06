package com.dd.web.rest;

import com.dd.domain.Parent;
import com.dd.service.ParentService;
import com.dd.web.rest.errors.BadRequestAlertException;
import com.dd.service.dto.ParentCriteria;
import com.dd.service.ParentQueryService;

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
 * REST controller for managing {@link com.dd.domain.Parent}.
 */
@RestController
@RequestMapping("/api")
public class ParentResource {

    private final Logger log = LoggerFactory.getLogger(ParentResource.class);

    private static final String ENTITY_NAME = "parent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParentService parentService;

    private final ParentQueryService parentQueryService;

    public ParentResource(ParentService parentService, ParentQueryService parentQueryService) {
        this.parentService = parentService;
        this.parentQueryService = parentQueryService;
    }

    /**
     * {@code POST  /parents} : Create a new parent.
     *
     * @param parent the parent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parent, or with status {@code 400 (Bad Request)} if the parent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parents")
    public ResponseEntity<Parent> createParent(@RequestBody Parent parent) throws URISyntaxException {
        log.debug("REST request to save Parent : {}", parent);
        if (parent.getId() != null) {
            throw new BadRequestAlertException("A new parent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Parent result = parentService.save(parent);
        return ResponseEntity.created(new URI("/api/parents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parents} : Updates an existing parent.
     *
     * @param parent the parent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parent,
     * or with status {@code 400 (Bad Request)} if the parent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parents")
    public ResponseEntity<Parent> updateParent(@RequestBody Parent parent) throws URISyntaxException {
        log.debug("REST request to update Parent : {}", parent);
        if (parent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Parent result = parentService.save(parent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, parent.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /parents} : get all the parents.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parents in body.
     */
    @GetMapping("/parents")
    public ResponseEntity<List<Parent>> getAllParents(ParentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Parents by criteria: {}", criteria);
        Page<Parent> page = parentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /parents/count} : count all the parents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/parents/count")
    public ResponseEntity<Long> countParents(ParentCriteria criteria) {
        log.debug("REST request to count Parents by criteria: {}", criteria);
        return ResponseEntity.ok().body(parentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /parents/:id} : get the "id" parent.
     *
     * @param id the id of the parent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parents/{id}")
    public ResponseEntity<Parent> getParent(@PathVariable Long id) {
        log.debug("REST request to get Parent : {}", id);
        Optional<Parent> parent = parentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(parent);
    }

    /**
     * {@code DELETE  /parents/:id} : delete the "id" parent.
     *
     * @param id the id of the parent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parents/{id}")
    public ResponseEntity<Void> deleteParent(@PathVariable Long id) {
        log.debug("REST request to delete Parent : {}", id);
        parentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
