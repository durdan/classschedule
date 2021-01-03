package com.dd.web.rest;

import com.dd.ClassscheduleApp;
import com.dd.domain.RequestStatus;
import com.dd.repository.RequestStatusRepository;
import com.dd.service.RequestStatusService;
import com.dd.service.dto.RequestStatusCriteria;
import com.dd.service.RequestStatusQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RequestStatusResource} REST controller.
 */
@SpringBootTest(classes = ClassscheduleApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RequestStatusResourceIT {

    private static final Integer DEFAULT_STATUS_CODE = 1;
    private static final Integer UPDATED_STATUS_CODE = 2;
    private static final Integer SMALLER_STATUS_CODE = 1 - 1;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private RequestStatusRepository requestStatusRepository;

    @Autowired
    private RequestStatusService requestStatusService;

    @Autowired
    private RequestStatusQueryService requestStatusQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRequestStatusMockMvc;

    private RequestStatus requestStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequestStatus createEntity(EntityManager em) {
        RequestStatus requestStatus = new RequestStatus()
            .statusCode(DEFAULT_STATUS_CODE)
            .status(DEFAULT_STATUS);
        return requestStatus;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequestStatus createUpdatedEntity(EntityManager em) {
        RequestStatus requestStatus = new RequestStatus()
            .statusCode(UPDATED_STATUS_CODE)
            .status(UPDATED_STATUS);
        return requestStatus;
    }

    @BeforeEach
    public void initTest() {
        requestStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createRequestStatus() throws Exception {
        int databaseSizeBeforeCreate = requestStatusRepository.findAll().size();
        // Create the RequestStatus
        restRequestStatusMockMvc.perform(post("/api/request-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(requestStatus)))
            .andExpect(status().isCreated());

        // Validate the RequestStatus in the database
        List<RequestStatus> requestStatusList = requestStatusRepository.findAll();
        assertThat(requestStatusList).hasSize(databaseSizeBeforeCreate + 1);
        RequestStatus testRequestStatus = requestStatusList.get(requestStatusList.size() - 1);
        assertThat(testRequestStatus.getStatusCode()).isEqualTo(DEFAULT_STATUS_CODE);
        assertThat(testRequestStatus.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createRequestStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = requestStatusRepository.findAll().size();

        // Create the RequestStatus with an existing ID
        requestStatus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequestStatusMockMvc.perform(post("/api/request-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(requestStatus)))
            .andExpect(status().isBadRequest());

        // Validate the RequestStatus in the database
        List<RequestStatus> requestStatusList = requestStatusRepository.findAll();
        assertThat(requestStatusList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRequestStatuses() throws Exception {
        // Initialize the database
        requestStatusRepository.saveAndFlush(requestStatus);

        // Get all the requestStatusList
        restRequestStatusMockMvc.perform(get("/api/request-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requestStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusCode").value(hasItem(DEFAULT_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }
    
    @Test
    @Transactional
    public void getRequestStatus() throws Exception {
        // Initialize the database
        requestStatusRepository.saveAndFlush(requestStatus);

        // Get the requestStatus
        restRequestStatusMockMvc.perform(get("/api/request-statuses/{id}", requestStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(requestStatus.getId().intValue()))
            .andExpect(jsonPath("$.statusCode").value(DEFAULT_STATUS_CODE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }


    @Test
    @Transactional
    public void getRequestStatusesByIdFiltering() throws Exception {
        // Initialize the database
        requestStatusRepository.saveAndFlush(requestStatus);

        Long id = requestStatus.getId();

        defaultRequestStatusShouldBeFound("id.equals=" + id);
        defaultRequestStatusShouldNotBeFound("id.notEquals=" + id);

        defaultRequestStatusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRequestStatusShouldNotBeFound("id.greaterThan=" + id);

        defaultRequestStatusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRequestStatusShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllRequestStatusesByStatusCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        requestStatusRepository.saveAndFlush(requestStatus);

        // Get all the requestStatusList where statusCode equals to DEFAULT_STATUS_CODE
        defaultRequestStatusShouldBeFound("statusCode.equals=" + DEFAULT_STATUS_CODE);

        // Get all the requestStatusList where statusCode equals to UPDATED_STATUS_CODE
        defaultRequestStatusShouldNotBeFound("statusCode.equals=" + UPDATED_STATUS_CODE);
    }

    @Test
    @Transactional
    public void getAllRequestStatusesByStatusCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        requestStatusRepository.saveAndFlush(requestStatus);

        // Get all the requestStatusList where statusCode not equals to DEFAULT_STATUS_CODE
        defaultRequestStatusShouldNotBeFound("statusCode.notEquals=" + DEFAULT_STATUS_CODE);

        // Get all the requestStatusList where statusCode not equals to UPDATED_STATUS_CODE
        defaultRequestStatusShouldBeFound("statusCode.notEquals=" + UPDATED_STATUS_CODE);
    }

    @Test
    @Transactional
    public void getAllRequestStatusesByStatusCodeIsInShouldWork() throws Exception {
        // Initialize the database
        requestStatusRepository.saveAndFlush(requestStatus);

        // Get all the requestStatusList where statusCode in DEFAULT_STATUS_CODE or UPDATED_STATUS_CODE
        defaultRequestStatusShouldBeFound("statusCode.in=" + DEFAULT_STATUS_CODE + "," + UPDATED_STATUS_CODE);

        // Get all the requestStatusList where statusCode equals to UPDATED_STATUS_CODE
        defaultRequestStatusShouldNotBeFound("statusCode.in=" + UPDATED_STATUS_CODE);
    }

    @Test
    @Transactional
    public void getAllRequestStatusesByStatusCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        requestStatusRepository.saveAndFlush(requestStatus);

        // Get all the requestStatusList where statusCode is not null
        defaultRequestStatusShouldBeFound("statusCode.specified=true");

        // Get all the requestStatusList where statusCode is null
        defaultRequestStatusShouldNotBeFound("statusCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllRequestStatusesByStatusCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        requestStatusRepository.saveAndFlush(requestStatus);

        // Get all the requestStatusList where statusCode is greater than or equal to DEFAULT_STATUS_CODE
        defaultRequestStatusShouldBeFound("statusCode.greaterThanOrEqual=" + DEFAULT_STATUS_CODE);

        // Get all the requestStatusList where statusCode is greater than or equal to UPDATED_STATUS_CODE
        defaultRequestStatusShouldNotBeFound("statusCode.greaterThanOrEqual=" + UPDATED_STATUS_CODE);
    }

    @Test
    @Transactional
    public void getAllRequestStatusesByStatusCodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        requestStatusRepository.saveAndFlush(requestStatus);

        // Get all the requestStatusList where statusCode is less than or equal to DEFAULT_STATUS_CODE
        defaultRequestStatusShouldBeFound("statusCode.lessThanOrEqual=" + DEFAULT_STATUS_CODE);

        // Get all the requestStatusList where statusCode is less than or equal to SMALLER_STATUS_CODE
        defaultRequestStatusShouldNotBeFound("statusCode.lessThanOrEqual=" + SMALLER_STATUS_CODE);
    }

    @Test
    @Transactional
    public void getAllRequestStatusesByStatusCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        requestStatusRepository.saveAndFlush(requestStatus);

        // Get all the requestStatusList where statusCode is less than DEFAULT_STATUS_CODE
        defaultRequestStatusShouldNotBeFound("statusCode.lessThan=" + DEFAULT_STATUS_CODE);

        // Get all the requestStatusList where statusCode is less than UPDATED_STATUS_CODE
        defaultRequestStatusShouldBeFound("statusCode.lessThan=" + UPDATED_STATUS_CODE);
    }

    @Test
    @Transactional
    public void getAllRequestStatusesByStatusCodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        requestStatusRepository.saveAndFlush(requestStatus);

        // Get all the requestStatusList where statusCode is greater than DEFAULT_STATUS_CODE
        defaultRequestStatusShouldNotBeFound("statusCode.greaterThan=" + DEFAULT_STATUS_CODE);

        // Get all the requestStatusList where statusCode is greater than SMALLER_STATUS_CODE
        defaultRequestStatusShouldBeFound("statusCode.greaterThan=" + SMALLER_STATUS_CODE);
    }


    @Test
    @Transactional
    public void getAllRequestStatusesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        requestStatusRepository.saveAndFlush(requestStatus);

        // Get all the requestStatusList where status equals to DEFAULT_STATUS
        defaultRequestStatusShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the requestStatusList where status equals to UPDATED_STATUS
        defaultRequestStatusShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllRequestStatusesByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        requestStatusRepository.saveAndFlush(requestStatus);

        // Get all the requestStatusList where status not equals to DEFAULT_STATUS
        defaultRequestStatusShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the requestStatusList where status not equals to UPDATED_STATUS
        defaultRequestStatusShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllRequestStatusesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        requestStatusRepository.saveAndFlush(requestStatus);

        // Get all the requestStatusList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultRequestStatusShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the requestStatusList where status equals to UPDATED_STATUS
        defaultRequestStatusShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllRequestStatusesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        requestStatusRepository.saveAndFlush(requestStatus);

        // Get all the requestStatusList where status is not null
        defaultRequestStatusShouldBeFound("status.specified=true");

        // Get all the requestStatusList where status is null
        defaultRequestStatusShouldNotBeFound("status.specified=false");
    }
                @Test
    @Transactional
    public void getAllRequestStatusesByStatusContainsSomething() throws Exception {
        // Initialize the database
        requestStatusRepository.saveAndFlush(requestStatus);

        // Get all the requestStatusList where status contains DEFAULT_STATUS
        defaultRequestStatusShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the requestStatusList where status contains UPDATED_STATUS
        defaultRequestStatusShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllRequestStatusesByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        requestStatusRepository.saveAndFlush(requestStatus);

        // Get all the requestStatusList where status does not contain DEFAULT_STATUS
        defaultRequestStatusShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the requestStatusList where status does not contain UPDATED_STATUS
        defaultRequestStatusShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRequestStatusShouldBeFound(String filter) throws Exception {
        restRequestStatusMockMvc.perform(get("/api/request-statuses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requestStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusCode").value(hasItem(DEFAULT_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));

        // Check, that the count call also returns 1
        restRequestStatusMockMvc.perform(get("/api/request-statuses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRequestStatusShouldNotBeFound(String filter) throws Exception {
        restRequestStatusMockMvc.perform(get("/api/request-statuses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRequestStatusMockMvc.perform(get("/api/request-statuses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingRequestStatus() throws Exception {
        // Get the requestStatus
        restRequestStatusMockMvc.perform(get("/api/request-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequestStatus() throws Exception {
        // Initialize the database
        requestStatusService.save(requestStatus);

        int databaseSizeBeforeUpdate = requestStatusRepository.findAll().size();

        // Update the requestStatus
        RequestStatus updatedRequestStatus = requestStatusRepository.findById(requestStatus.getId()).get();
        // Disconnect from session so that the updates on updatedRequestStatus are not directly saved in db
        em.detach(updatedRequestStatus);
        updatedRequestStatus
            .statusCode(UPDATED_STATUS_CODE)
            .status(UPDATED_STATUS);

        restRequestStatusMockMvc.perform(put("/api/request-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRequestStatus)))
            .andExpect(status().isOk());

        // Validate the RequestStatus in the database
        List<RequestStatus> requestStatusList = requestStatusRepository.findAll();
        assertThat(requestStatusList).hasSize(databaseSizeBeforeUpdate);
        RequestStatus testRequestStatus = requestStatusList.get(requestStatusList.size() - 1);
        assertThat(testRequestStatus.getStatusCode()).isEqualTo(UPDATED_STATUS_CODE);
        assertThat(testRequestStatus.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingRequestStatus() throws Exception {
        int databaseSizeBeforeUpdate = requestStatusRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestStatusMockMvc.perform(put("/api/request-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(requestStatus)))
            .andExpect(status().isBadRequest());

        // Validate the RequestStatus in the database
        List<RequestStatus> requestStatusList = requestStatusRepository.findAll();
        assertThat(requestStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRequestStatus() throws Exception {
        // Initialize the database
        requestStatusService.save(requestStatus);

        int databaseSizeBeforeDelete = requestStatusRepository.findAll().size();

        // Delete the requestStatus
        restRequestStatusMockMvc.perform(delete("/api/request-statuses/{id}", requestStatus.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RequestStatus> requestStatusList = requestStatusRepository.findAll();
        assertThat(requestStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
