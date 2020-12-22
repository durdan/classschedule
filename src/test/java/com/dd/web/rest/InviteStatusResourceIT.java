package com.dd.web.rest;

import com.dd.ClassscheduleApp;
import com.dd.domain.InviteStatus;
import com.dd.repository.InviteStatusRepository;
import com.dd.service.InviteStatusService;
import com.dd.service.dto.InviteStatusCriteria;
import com.dd.service.InviteStatusQueryService;

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
 * Integration tests for the {@link InviteStatusResource} REST controller.
 */
@SpringBootTest(classes = ClassscheduleApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class InviteStatusResourceIT {

    private static final Integer DEFAULT_STATUS_CODE = 1;
    private static final Integer UPDATED_STATUS_CODE = 2;
    private static final Integer SMALLER_STATUS_CODE = 1 - 1;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private InviteStatusRepository inviteStatusRepository;

    @Autowired
    private InviteStatusService inviteStatusService;

    @Autowired
    private InviteStatusQueryService inviteStatusQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInviteStatusMockMvc;

    private InviteStatus inviteStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InviteStatus createEntity(EntityManager em) {
        InviteStatus inviteStatus = new InviteStatus()
            .statusCode(DEFAULT_STATUS_CODE)
            .status(DEFAULT_STATUS);
        return inviteStatus;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InviteStatus createUpdatedEntity(EntityManager em) {
        InviteStatus inviteStatus = new InviteStatus()
            .statusCode(UPDATED_STATUS_CODE)
            .status(UPDATED_STATUS);
        return inviteStatus;
    }

    @BeforeEach
    public void initTest() {
        inviteStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createInviteStatus() throws Exception {
        int databaseSizeBeforeCreate = inviteStatusRepository.findAll().size();
        // Create the InviteStatus
        restInviteStatusMockMvc.perform(post("/api/invite-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inviteStatus)))
            .andExpect(status().isCreated());

        // Validate the InviteStatus in the database
        List<InviteStatus> inviteStatusList = inviteStatusRepository.findAll();
        assertThat(inviteStatusList).hasSize(databaseSizeBeforeCreate + 1);
        InviteStatus testInviteStatus = inviteStatusList.get(inviteStatusList.size() - 1);
        assertThat(testInviteStatus.getStatusCode()).isEqualTo(DEFAULT_STATUS_CODE);
        assertThat(testInviteStatus.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createInviteStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = inviteStatusRepository.findAll().size();

        // Create the InviteStatus with an existing ID
        inviteStatus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInviteStatusMockMvc.perform(post("/api/invite-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inviteStatus)))
            .andExpect(status().isBadRequest());

        // Validate the InviteStatus in the database
        List<InviteStatus> inviteStatusList = inviteStatusRepository.findAll();
        assertThat(inviteStatusList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllInviteStatuses() throws Exception {
        // Initialize the database
        inviteStatusRepository.saveAndFlush(inviteStatus);

        // Get all the inviteStatusList
        restInviteStatusMockMvc.perform(get("/api/invite-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inviteStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusCode").value(hasItem(DEFAULT_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }
    
    @Test
    @Transactional
    public void getInviteStatus() throws Exception {
        // Initialize the database
        inviteStatusRepository.saveAndFlush(inviteStatus);

        // Get the inviteStatus
        restInviteStatusMockMvc.perform(get("/api/invite-statuses/{id}", inviteStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inviteStatus.getId().intValue()))
            .andExpect(jsonPath("$.statusCode").value(DEFAULT_STATUS_CODE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }


    @Test
    @Transactional
    public void getInviteStatusesByIdFiltering() throws Exception {
        // Initialize the database
        inviteStatusRepository.saveAndFlush(inviteStatus);

        Long id = inviteStatus.getId();

        defaultInviteStatusShouldBeFound("id.equals=" + id);
        defaultInviteStatusShouldNotBeFound("id.notEquals=" + id);

        defaultInviteStatusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInviteStatusShouldNotBeFound("id.greaterThan=" + id);

        defaultInviteStatusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInviteStatusShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllInviteStatusesByStatusCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        inviteStatusRepository.saveAndFlush(inviteStatus);

        // Get all the inviteStatusList where statusCode equals to DEFAULT_STATUS_CODE
        defaultInviteStatusShouldBeFound("statusCode.equals=" + DEFAULT_STATUS_CODE);

        // Get all the inviteStatusList where statusCode equals to UPDATED_STATUS_CODE
        defaultInviteStatusShouldNotBeFound("statusCode.equals=" + UPDATED_STATUS_CODE);
    }

    @Test
    @Transactional
    public void getAllInviteStatusesByStatusCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        inviteStatusRepository.saveAndFlush(inviteStatus);

        // Get all the inviteStatusList where statusCode not equals to DEFAULT_STATUS_CODE
        defaultInviteStatusShouldNotBeFound("statusCode.notEquals=" + DEFAULT_STATUS_CODE);

        // Get all the inviteStatusList where statusCode not equals to UPDATED_STATUS_CODE
        defaultInviteStatusShouldBeFound("statusCode.notEquals=" + UPDATED_STATUS_CODE);
    }

    @Test
    @Transactional
    public void getAllInviteStatusesByStatusCodeIsInShouldWork() throws Exception {
        // Initialize the database
        inviteStatusRepository.saveAndFlush(inviteStatus);

        // Get all the inviteStatusList where statusCode in DEFAULT_STATUS_CODE or UPDATED_STATUS_CODE
        defaultInviteStatusShouldBeFound("statusCode.in=" + DEFAULT_STATUS_CODE + "," + UPDATED_STATUS_CODE);

        // Get all the inviteStatusList where statusCode equals to UPDATED_STATUS_CODE
        defaultInviteStatusShouldNotBeFound("statusCode.in=" + UPDATED_STATUS_CODE);
    }

    @Test
    @Transactional
    public void getAllInviteStatusesByStatusCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        inviteStatusRepository.saveAndFlush(inviteStatus);

        // Get all the inviteStatusList where statusCode is not null
        defaultInviteStatusShouldBeFound("statusCode.specified=true");

        // Get all the inviteStatusList where statusCode is null
        defaultInviteStatusShouldNotBeFound("statusCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllInviteStatusesByStatusCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        inviteStatusRepository.saveAndFlush(inviteStatus);

        // Get all the inviteStatusList where statusCode is greater than or equal to DEFAULT_STATUS_CODE
        defaultInviteStatusShouldBeFound("statusCode.greaterThanOrEqual=" + DEFAULT_STATUS_CODE);

        // Get all the inviteStatusList where statusCode is greater than or equal to UPDATED_STATUS_CODE
        defaultInviteStatusShouldNotBeFound("statusCode.greaterThanOrEqual=" + UPDATED_STATUS_CODE);
    }

    @Test
    @Transactional
    public void getAllInviteStatusesByStatusCodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        inviteStatusRepository.saveAndFlush(inviteStatus);

        // Get all the inviteStatusList where statusCode is less than or equal to DEFAULT_STATUS_CODE
        defaultInviteStatusShouldBeFound("statusCode.lessThanOrEqual=" + DEFAULT_STATUS_CODE);

        // Get all the inviteStatusList where statusCode is less than or equal to SMALLER_STATUS_CODE
        defaultInviteStatusShouldNotBeFound("statusCode.lessThanOrEqual=" + SMALLER_STATUS_CODE);
    }

    @Test
    @Transactional
    public void getAllInviteStatusesByStatusCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        inviteStatusRepository.saveAndFlush(inviteStatus);

        // Get all the inviteStatusList where statusCode is less than DEFAULT_STATUS_CODE
        defaultInviteStatusShouldNotBeFound("statusCode.lessThan=" + DEFAULT_STATUS_CODE);

        // Get all the inviteStatusList where statusCode is less than UPDATED_STATUS_CODE
        defaultInviteStatusShouldBeFound("statusCode.lessThan=" + UPDATED_STATUS_CODE);
    }

    @Test
    @Transactional
    public void getAllInviteStatusesByStatusCodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        inviteStatusRepository.saveAndFlush(inviteStatus);

        // Get all the inviteStatusList where statusCode is greater than DEFAULT_STATUS_CODE
        defaultInviteStatusShouldNotBeFound("statusCode.greaterThan=" + DEFAULT_STATUS_CODE);

        // Get all the inviteStatusList where statusCode is greater than SMALLER_STATUS_CODE
        defaultInviteStatusShouldBeFound("statusCode.greaterThan=" + SMALLER_STATUS_CODE);
    }


    @Test
    @Transactional
    public void getAllInviteStatusesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        inviteStatusRepository.saveAndFlush(inviteStatus);

        // Get all the inviteStatusList where status equals to DEFAULT_STATUS
        defaultInviteStatusShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the inviteStatusList where status equals to UPDATED_STATUS
        defaultInviteStatusShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllInviteStatusesByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        inviteStatusRepository.saveAndFlush(inviteStatus);

        // Get all the inviteStatusList where status not equals to DEFAULT_STATUS
        defaultInviteStatusShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the inviteStatusList where status not equals to UPDATED_STATUS
        defaultInviteStatusShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllInviteStatusesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        inviteStatusRepository.saveAndFlush(inviteStatus);

        // Get all the inviteStatusList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultInviteStatusShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the inviteStatusList where status equals to UPDATED_STATUS
        defaultInviteStatusShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllInviteStatusesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        inviteStatusRepository.saveAndFlush(inviteStatus);

        // Get all the inviteStatusList where status is not null
        defaultInviteStatusShouldBeFound("status.specified=true");

        // Get all the inviteStatusList where status is null
        defaultInviteStatusShouldNotBeFound("status.specified=false");
    }
                @Test
    @Transactional
    public void getAllInviteStatusesByStatusContainsSomething() throws Exception {
        // Initialize the database
        inviteStatusRepository.saveAndFlush(inviteStatus);

        // Get all the inviteStatusList where status contains DEFAULT_STATUS
        defaultInviteStatusShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the inviteStatusList where status contains UPDATED_STATUS
        defaultInviteStatusShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllInviteStatusesByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        inviteStatusRepository.saveAndFlush(inviteStatus);

        // Get all the inviteStatusList where status does not contain DEFAULT_STATUS
        defaultInviteStatusShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the inviteStatusList where status does not contain UPDATED_STATUS
        defaultInviteStatusShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInviteStatusShouldBeFound(String filter) throws Exception {
        restInviteStatusMockMvc.perform(get("/api/invite-statuses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inviteStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].statusCode").value(hasItem(DEFAULT_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));

        // Check, that the count call also returns 1
        restInviteStatusMockMvc.perform(get("/api/invite-statuses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInviteStatusShouldNotBeFound(String filter) throws Exception {
        restInviteStatusMockMvc.perform(get("/api/invite-statuses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInviteStatusMockMvc.perform(get("/api/invite-statuses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingInviteStatus() throws Exception {
        // Get the inviteStatus
        restInviteStatusMockMvc.perform(get("/api/invite-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInviteStatus() throws Exception {
        // Initialize the database
        inviteStatusService.save(inviteStatus);

        int databaseSizeBeforeUpdate = inviteStatusRepository.findAll().size();

        // Update the inviteStatus
        InviteStatus updatedInviteStatus = inviteStatusRepository.findById(inviteStatus.getId()).get();
        // Disconnect from session so that the updates on updatedInviteStatus are not directly saved in db
        em.detach(updatedInviteStatus);
        updatedInviteStatus
            .statusCode(UPDATED_STATUS_CODE)
            .status(UPDATED_STATUS);

        restInviteStatusMockMvc.perform(put("/api/invite-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedInviteStatus)))
            .andExpect(status().isOk());

        // Validate the InviteStatus in the database
        List<InviteStatus> inviteStatusList = inviteStatusRepository.findAll();
        assertThat(inviteStatusList).hasSize(databaseSizeBeforeUpdate);
        InviteStatus testInviteStatus = inviteStatusList.get(inviteStatusList.size() - 1);
        assertThat(testInviteStatus.getStatusCode()).isEqualTo(UPDATED_STATUS_CODE);
        assertThat(testInviteStatus.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingInviteStatus() throws Exception {
        int databaseSizeBeforeUpdate = inviteStatusRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInviteStatusMockMvc.perform(put("/api/invite-statuses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inviteStatus)))
            .andExpect(status().isBadRequest());

        // Validate the InviteStatus in the database
        List<InviteStatus> inviteStatusList = inviteStatusRepository.findAll();
        assertThat(inviteStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInviteStatus() throws Exception {
        // Initialize the database
        inviteStatusService.save(inviteStatus);

        int databaseSizeBeforeDelete = inviteStatusRepository.findAll().size();

        // Delete the inviteStatus
        restInviteStatusMockMvc.perform(delete("/api/invite-statuses/{id}", inviteStatus.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InviteStatus> inviteStatusList = inviteStatusRepository.findAll();
        assertThat(inviteStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
