package com.dd.web.rest;

import com.dd.ClassscheduleApp;
import com.dd.domain.RequestTask;
import com.dd.domain.RequestStatus;
import com.dd.domain.User;
import com.dd.repository.RequestTaskRepository;
import com.dd.service.RequestTaskService;
import com.dd.service.dto.RequestTaskCriteria;
import com.dd.service.RequestTaskQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RequestTaskResource} REST controller.
 */
@SpringBootTest(classes = ClassscheduleApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RequestTaskResourceIT {

    private static final String DEFAULT_REQUESTED_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_REQUESTED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_REQUEST_CODE = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_REQUEST_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_REQUIRED_ACTION_FROM_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_REQUIRED_ACTION_FROM_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private RequestTaskRepository requestTaskRepository;

    @Autowired
    private RequestTaskService requestTaskService;

    @Autowired
    private RequestTaskQueryService requestTaskQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRequestTaskMockMvc;

    private RequestTask requestTask;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequestTask createEntity(EntityManager em) {
        RequestTask requestTask = new RequestTask()
            .requestedUserId(DEFAULT_REQUESTED_USER_ID)
            .requestCode(DEFAULT_REQUEST_CODE)
            .requestType(DEFAULT_REQUEST_TYPE)
            .requiredActionFromUserId(DEFAULT_REQUIRED_ACTION_FROM_USER_ID)
            .createdBy(DEFAULT_CREATED_BY)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED);
        return requestTask;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequestTask createUpdatedEntity(EntityManager em) {
        RequestTask requestTask = new RequestTask()
            .requestedUserId(UPDATED_REQUESTED_USER_ID)
            .requestCode(UPDATED_REQUEST_CODE)
            .requestType(UPDATED_REQUEST_TYPE)
            .requiredActionFromUserId(UPDATED_REQUIRED_ACTION_FROM_USER_ID)
            .createdBy(UPDATED_CREATED_BY)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);
        return requestTask;
    }

    @BeforeEach
    public void initTest() {
        requestTask = createEntity(em);
    }

    @Test
    @Transactional
    public void createRequestTask() throws Exception {
        int databaseSizeBeforeCreate = requestTaskRepository.findAll().size();
        // Create the RequestTask
        restRequestTaskMockMvc.perform(post("/api/request-tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(requestTask)))
            .andExpect(status().isCreated());

        // Validate the RequestTask in the database
        List<RequestTask> requestTaskList = requestTaskRepository.findAll();
        assertThat(requestTaskList).hasSize(databaseSizeBeforeCreate + 1);
        RequestTask testRequestTask = requestTaskList.get(requestTaskList.size() - 1);
        assertThat(testRequestTask.getRequestedUserId()).isEqualTo(DEFAULT_REQUESTED_USER_ID);
        assertThat(testRequestTask.getRequestCode()).isEqualTo(DEFAULT_REQUEST_CODE);
        assertThat(testRequestTask.getRequestType()).isEqualTo(DEFAULT_REQUEST_TYPE);
        assertThat(testRequestTask.getRequiredActionFromUserId()).isEqualTo(DEFAULT_REQUIRED_ACTION_FROM_USER_ID);
        assertThat(testRequestTask.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testRequestTask.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testRequestTask.getUpdated()).isEqualTo(DEFAULT_UPDATED);
    }

    @Test
    @Transactional
    public void createRequestTaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = requestTaskRepository.findAll().size();

        // Create the RequestTask with an existing ID
        requestTask.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequestTaskMockMvc.perform(post("/api/request-tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(requestTask)))
            .andExpect(status().isBadRequest());

        // Validate the RequestTask in the database
        List<RequestTask> requestTaskList = requestTaskRepository.findAll();
        assertThat(requestTaskList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRequestTasks() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList
        restRequestTaskMockMvc.perform(get("/api/request-tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requestTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestedUserId").value(hasItem(DEFAULT_REQUESTED_USER_ID)))
            .andExpect(jsonPath("$.[*].requestCode").value(hasItem(DEFAULT_REQUEST_CODE)))
            .andExpect(jsonPath("$.[*].requestType").value(hasItem(DEFAULT_REQUEST_TYPE)))
            .andExpect(jsonPath("$.[*].requiredActionFromUserId").value(hasItem(DEFAULT_REQUIRED_ACTION_FROM_USER_ID)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())));
    }
    
    @Test
    @Transactional
    public void getRequestTask() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get the requestTask
        restRequestTaskMockMvc.perform(get("/api/request-tasks/{id}", requestTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(requestTask.getId().intValue()))
            .andExpect(jsonPath("$.requestedUserId").value(DEFAULT_REQUESTED_USER_ID))
            .andExpect(jsonPath("$.requestCode").value(DEFAULT_REQUEST_CODE))
            .andExpect(jsonPath("$.requestType").value(DEFAULT_REQUEST_TYPE))
            .andExpect(jsonPath("$.requiredActionFromUserId").value(DEFAULT_REQUIRED_ACTION_FROM_USER_ID))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.updated").value(DEFAULT_UPDATED.toString()));
    }


    @Test
    @Transactional
    public void getRequestTasksByIdFiltering() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        Long id = requestTask.getId();

        defaultRequestTaskShouldBeFound("id.equals=" + id);
        defaultRequestTaskShouldNotBeFound("id.notEquals=" + id);

        defaultRequestTaskShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRequestTaskShouldNotBeFound("id.greaterThan=" + id);

        defaultRequestTaskShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRequestTaskShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllRequestTasksByRequestedUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where requestedUserId equals to DEFAULT_REQUESTED_USER_ID
        defaultRequestTaskShouldBeFound("requestedUserId.equals=" + DEFAULT_REQUESTED_USER_ID);

        // Get all the requestTaskList where requestedUserId equals to UPDATED_REQUESTED_USER_ID
        defaultRequestTaskShouldNotBeFound("requestedUserId.equals=" + UPDATED_REQUESTED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByRequestedUserIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where requestedUserId not equals to DEFAULT_REQUESTED_USER_ID
        defaultRequestTaskShouldNotBeFound("requestedUserId.notEquals=" + DEFAULT_REQUESTED_USER_ID);

        // Get all the requestTaskList where requestedUserId not equals to UPDATED_REQUESTED_USER_ID
        defaultRequestTaskShouldBeFound("requestedUserId.notEquals=" + UPDATED_REQUESTED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByRequestedUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where requestedUserId in DEFAULT_REQUESTED_USER_ID or UPDATED_REQUESTED_USER_ID
        defaultRequestTaskShouldBeFound("requestedUserId.in=" + DEFAULT_REQUESTED_USER_ID + "," + UPDATED_REQUESTED_USER_ID);

        // Get all the requestTaskList where requestedUserId equals to UPDATED_REQUESTED_USER_ID
        defaultRequestTaskShouldNotBeFound("requestedUserId.in=" + UPDATED_REQUESTED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByRequestedUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where requestedUserId is not null
        defaultRequestTaskShouldBeFound("requestedUserId.specified=true");

        // Get all the requestTaskList where requestedUserId is null
        defaultRequestTaskShouldNotBeFound("requestedUserId.specified=false");
    }
                @Test
    @Transactional
    public void getAllRequestTasksByRequestedUserIdContainsSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where requestedUserId contains DEFAULT_REQUESTED_USER_ID
        defaultRequestTaskShouldBeFound("requestedUserId.contains=" + DEFAULT_REQUESTED_USER_ID);

        // Get all the requestTaskList where requestedUserId contains UPDATED_REQUESTED_USER_ID
        defaultRequestTaskShouldNotBeFound("requestedUserId.contains=" + UPDATED_REQUESTED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByRequestedUserIdNotContainsSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where requestedUserId does not contain DEFAULT_REQUESTED_USER_ID
        defaultRequestTaskShouldNotBeFound("requestedUserId.doesNotContain=" + DEFAULT_REQUESTED_USER_ID);

        // Get all the requestTaskList where requestedUserId does not contain UPDATED_REQUESTED_USER_ID
        defaultRequestTaskShouldBeFound("requestedUserId.doesNotContain=" + UPDATED_REQUESTED_USER_ID);
    }


    @Test
    @Transactional
    public void getAllRequestTasksByRequestCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where requestCode equals to DEFAULT_REQUEST_CODE
        defaultRequestTaskShouldBeFound("requestCode.equals=" + DEFAULT_REQUEST_CODE);

        // Get all the requestTaskList where requestCode equals to UPDATED_REQUEST_CODE
        defaultRequestTaskShouldNotBeFound("requestCode.equals=" + UPDATED_REQUEST_CODE);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByRequestCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where requestCode not equals to DEFAULT_REQUEST_CODE
        defaultRequestTaskShouldNotBeFound("requestCode.notEquals=" + DEFAULT_REQUEST_CODE);

        // Get all the requestTaskList where requestCode not equals to UPDATED_REQUEST_CODE
        defaultRequestTaskShouldBeFound("requestCode.notEquals=" + UPDATED_REQUEST_CODE);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByRequestCodeIsInShouldWork() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where requestCode in DEFAULT_REQUEST_CODE or UPDATED_REQUEST_CODE
        defaultRequestTaskShouldBeFound("requestCode.in=" + DEFAULT_REQUEST_CODE + "," + UPDATED_REQUEST_CODE);

        // Get all the requestTaskList where requestCode equals to UPDATED_REQUEST_CODE
        defaultRequestTaskShouldNotBeFound("requestCode.in=" + UPDATED_REQUEST_CODE);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByRequestCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where requestCode is not null
        defaultRequestTaskShouldBeFound("requestCode.specified=true");

        // Get all the requestTaskList where requestCode is null
        defaultRequestTaskShouldNotBeFound("requestCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllRequestTasksByRequestCodeContainsSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where requestCode contains DEFAULT_REQUEST_CODE
        defaultRequestTaskShouldBeFound("requestCode.contains=" + DEFAULT_REQUEST_CODE);

        // Get all the requestTaskList where requestCode contains UPDATED_REQUEST_CODE
        defaultRequestTaskShouldNotBeFound("requestCode.contains=" + UPDATED_REQUEST_CODE);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByRequestCodeNotContainsSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where requestCode does not contain DEFAULT_REQUEST_CODE
        defaultRequestTaskShouldNotBeFound("requestCode.doesNotContain=" + DEFAULT_REQUEST_CODE);

        // Get all the requestTaskList where requestCode does not contain UPDATED_REQUEST_CODE
        defaultRequestTaskShouldBeFound("requestCode.doesNotContain=" + UPDATED_REQUEST_CODE);
    }


    @Test
    @Transactional
    public void getAllRequestTasksByRequestTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where requestType equals to DEFAULT_REQUEST_TYPE
        defaultRequestTaskShouldBeFound("requestType.equals=" + DEFAULT_REQUEST_TYPE);

        // Get all the requestTaskList where requestType equals to UPDATED_REQUEST_TYPE
        defaultRequestTaskShouldNotBeFound("requestType.equals=" + UPDATED_REQUEST_TYPE);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByRequestTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where requestType not equals to DEFAULT_REQUEST_TYPE
        defaultRequestTaskShouldNotBeFound("requestType.notEquals=" + DEFAULT_REQUEST_TYPE);

        // Get all the requestTaskList where requestType not equals to UPDATED_REQUEST_TYPE
        defaultRequestTaskShouldBeFound("requestType.notEquals=" + UPDATED_REQUEST_TYPE);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByRequestTypeIsInShouldWork() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where requestType in DEFAULT_REQUEST_TYPE or UPDATED_REQUEST_TYPE
        defaultRequestTaskShouldBeFound("requestType.in=" + DEFAULT_REQUEST_TYPE + "," + UPDATED_REQUEST_TYPE);

        // Get all the requestTaskList where requestType equals to UPDATED_REQUEST_TYPE
        defaultRequestTaskShouldNotBeFound("requestType.in=" + UPDATED_REQUEST_TYPE);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByRequestTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where requestType is not null
        defaultRequestTaskShouldBeFound("requestType.specified=true");

        // Get all the requestTaskList where requestType is null
        defaultRequestTaskShouldNotBeFound("requestType.specified=false");
    }
                @Test
    @Transactional
    public void getAllRequestTasksByRequestTypeContainsSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where requestType contains DEFAULT_REQUEST_TYPE
        defaultRequestTaskShouldBeFound("requestType.contains=" + DEFAULT_REQUEST_TYPE);

        // Get all the requestTaskList where requestType contains UPDATED_REQUEST_TYPE
        defaultRequestTaskShouldNotBeFound("requestType.contains=" + UPDATED_REQUEST_TYPE);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByRequestTypeNotContainsSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where requestType does not contain DEFAULT_REQUEST_TYPE
        defaultRequestTaskShouldNotBeFound("requestType.doesNotContain=" + DEFAULT_REQUEST_TYPE);

        // Get all the requestTaskList where requestType does not contain UPDATED_REQUEST_TYPE
        defaultRequestTaskShouldBeFound("requestType.doesNotContain=" + UPDATED_REQUEST_TYPE);
    }


    @Test
    @Transactional
    public void getAllRequestTasksByRequiredActionFromUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where requiredActionFromUserId equals to DEFAULT_REQUIRED_ACTION_FROM_USER_ID
        defaultRequestTaskShouldBeFound("requiredActionFromUserId.equals=" + DEFAULT_REQUIRED_ACTION_FROM_USER_ID);

        // Get all the requestTaskList where requiredActionFromUserId equals to UPDATED_REQUIRED_ACTION_FROM_USER_ID
        defaultRequestTaskShouldNotBeFound("requiredActionFromUserId.equals=" + UPDATED_REQUIRED_ACTION_FROM_USER_ID);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByRequiredActionFromUserIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where requiredActionFromUserId not equals to DEFAULT_REQUIRED_ACTION_FROM_USER_ID
        defaultRequestTaskShouldNotBeFound("requiredActionFromUserId.notEquals=" + DEFAULT_REQUIRED_ACTION_FROM_USER_ID);

        // Get all the requestTaskList where requiredActionFromUserId not equals to UPDATED_REQUIRED_ACTION_FROM_USER_ID
        defaultRequestTaskShouldBeFound("requiredActionFromUserId.notEquals=" + UPDATED_REQUIRED_ACTION_FROM_USER_ID);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByRequiredActionFromUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where requiredActionFromUserId in DEFAULT_REQUIRED_ACTION_FROM_USER_ID or UPDATED_REQUIRED_ACTION_FROM_USER_ID
        defaultRequestTaskShouldBeFound("requiredActionFromUserId.in=" + DEFAULT_REQUIRED_ACTION_FROM_USER_ID + "," + UPDATED_REQUIRED_ACTION_FROM_USER_ID);

        // Get all the requestTaskList where requiredActionFromUserId equals to UPDATED_REQUIRED_ACTION_FROM_USER_ID
        defaultRequestTaskShouldNotBeFound("requiredActionFromUserId.in=" + UPDATED_REQUIRED_ACTION_FROM_USER_ID);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByRequiredActionFromUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where requiredActionFromUserId is not null
        defaultRequestTaskShouldBeFound("requiredActionFromUserId.specified=true");

        // Get all the requestTaskList where requiredActionFromUserId is null
        defaultRequestTaskShouldNotBeFound("requiredActionFromUserId.specified=false");
    }
                @Test
    @Transactional
    public void getAllRequestTasksByRequiredActionFromUserIdContainsSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where requiredActionFromUserId contains DEFAULT_REQUIRED_ACTION_FROM_USER_ID
        defaultRequestTaskShouldBeFound("requiredActionFromUserId.contains=" + DEFAULT_REQUIRED_ACTION_FROM_USER_ID);

        // Get all the requestTaskList where requiredActionFromUserId contains UPDATED_REQUIRED_ACTION_FROM_USER_ID
        defaultRequestTaskShouldNotBeFound("requiredActionFromUserId.contains=" + UPDATED_REQUIRED_ACTION_FROM_USER_ID);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByRequiredActionFromUserIdNotContainsSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where requiredActionFromUserId does not contain DEFAULT_REQUIRED_ACTION_FROM_USER_ID
        defaultRequestTaskShouldNotBeFound("requiredActionFromUserId.doesNotContain=" + DEFAULT_REQUIRED_ACTION_FROM_USER_ID);

        // Get all the requestTaskList where requiredActionFromUserId does not contain UPDATED_REQUIRED_ACTION_FROM_USER_ID
        defaultRequestTaskShouldBeFound("requiredActionFromUserId.doesNotContain=" + UPDATED_REQUIRED_ACTION_FROM_USER_ID);
    }


    @Test
    @Transactional
    public void getAllRequestTasksByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where createdBy equals to DEFAULT_CREATED_BY
        defaultRequestTaskShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the requestTaskList where createdBy equals to UPDATED_CREATED_BY
        defaultRequestTaskShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where createdBy not equals to DEFAULT_CREATED_BY
        defaultRequestTaskShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the requestTaskList where createdBy not equals to UPDATED_CREATED_BY
        defaultRequestTaskShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultRequestTaskShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the requestTaskList where createdBy equals to UPDATED_CREATED_BY
        defaultRequestTaskShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where createdBy is not null
        defaultRequestTaskShouldBeFound("createdBy.specified=true");

        // Get all the requestTaskList where createdBy is null
        defaultRequestTaskShouldNotBeFound("createdBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllRequestTasksByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where createdBy contains DEFAULT_CREATED_BY
        defaultRequestTaskShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the requestTaskList where createdBy contains UPDATED_CREATED_BY
        defaultRequestTaskShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where createdBy does not contain DEFAULT_CREATED_BY
        defaultRequestTaskShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the requestTaskList where createdBy does not contain UPDATED_CREATED_BY
        defaultRequestTaskShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }


    @Test
    @Transactional
    public void getAllRequestTasksByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where created equals to DEFAULT_CREATED
        defaultRequestTaskShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the requestTaskList where created equals to UPDATED_CREATED
        defaultRequestTaskShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where created not equals to DEFAULT_CREATED
        defaultRequestTaskShouldNotBeFound("created.notEquals=" + DEFAULT_CREATED);

        // Get all the requestTaskList where created not equals to UPDATED_CREATED
        defaultRequestTaskShouldBeFound("created.notEquals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultRequestTaskShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the requestTaskList where created equals to UPDATED_CREATED
        defaultRequestTaskShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where created is not null
        defaultRequestTaskShouldBeFound("created.specified=true");

        // Get all the requestTaskList where created is null
        defaultRequestTaskShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    public void getAllRequestTasksByUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where updated equals to DEFAULT_UPDATED
        defaultRequestTaskShouldBeFound("updated.equals=" + DEFAULT_UPDATED);

        // Get all the requestTaskList where updated equals to UPDATED_UPDATED
        defaultRequestTaskShouldNotBeFound("updated.equals=" + UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByUpdatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where updated not equals to DEFAULT_UPDATED
        defaultRequestTaskShouldNotBeFound("updated.notEquals=" + DEFAULT_UPDATED);

        // Get all the requestTaskList where updated not equals to UPDATED_UPDATED
        defaultRequestTaskShouldBeFound("updated.notEquals=" + UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where updated in DEFAULT_UPDATED or UPDATED_UPDATED
        defaultRequestTaskShouldBeFound("updated.in=" + DEFAULT_UPDATED + "," + UPDATED_UPDATED);

        // Get all the requestTaskList where updated equals to UPDATED_UPDATED
        defaultRequestTaskShouldNotBeFound("updated.in=" + UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void getAllRequestTasksByUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);

        // Get all the requestTaskList where updated is not null
        defaultRequestTaskShouldBeFound("updated.specified=true");

        // Get all the requestTaskList where updated is null
        defaultRequestTaskShouldNotBeFound("updated.specified=false");
    }

    @Test
    @Transactional
    public void getAllRequestTasksByRequestStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);
        RequestStatus requestStatus = RequestStatusResourceIT.createEntity(em);
        em.persist(requestStatus);
        em.flush();
        requestTask.setRequestStatus(requestStatus);
        requestTaskRepository.saveAndFlush(requestTask);
        Long requestStatusId = requestStatus.getId();

        // Get all the requestTaskList where requestStatus equals to requestStatusId
        defaultRequestTaskShouldBeFound("requestStatusId.equals=" + requestStatusId);

        // Get all the requestTaskList where requestStatus equals to requestStatusId + 1
        defaultRequestTaskShouldNotBeFound("requestStatusId.equals=" + (requestStatusId + 1));
    }


    @Test
    @Transactional
    public void getAllRequestTasksByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        requestTaskRepository.saveAndFlush(requestTask);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        requestTask.setUser(user);
        requestTaskRepository.saveAndFlush(requestTask);
        Long userId = user.getId();

        // Get all the requestTaskList where user equals to userId
        defaultRequestTaskShouldBeFound("userId.equals=" + userId);

        // Get all the requestTaskList where user equals to userId + 1
        defaultRequestTaskShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRequestTaskShouldBeFound(String filter) throws Exception {
        restRequestTaskMockMvc.perform(get("/api/request-tasks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requestTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestedUserId").value(hasItem(DEFAULT_REQUESTED_USER_ID)))
            .andExpect(jsonPath("$.[*].requestCode").value(hasItem(DEFAULT_REQUEST_CODE)))
            .andExpect(jsonPath("$.[*].requestType").value(hasItem(DEFAULT_REQUEST_TYPE)))
            .andExpect(jsonPath("$.[*].requiredActionFromUserId").value(hasItem(DEFAULT_REQUIRED_ACTION_FROM_USER_ID)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())));

        // Check, that the count call also returns 1
        restRequestTaskMockMvc.perform(get("/api/request-tasks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRequestTaskShouldNotBeFound(String filter) throws Exception {
        restRequestTaskMockMvc.perform(get("/api/request-tasks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRequestTaskMockMvc.perform(get("/api/request-tasks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingRequestTask() throws Exception {
        // Get the requestTask
        restRequestTaskMockMvc.perform(get("/api/request-tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequestTask() throws Exception {
        // Initialize the database
        requestTaskService.save(requestTask);

        int databaseSizeBeforeUpdate = requestTaskRepository.findAll().size();

        // Update the requestTask
        RequestTask updatedRequestTask = requestTaskRepository.findById(requestTask.getId()).get();
        // Disconnect from session so that the updates on updatedRequestTask are not directly saved in db
        em.detach(updatedRequestTask);
        updatedRequestTask
            .requestedUserId(UPDATED_REQUESTED_USER_ID)
            .requestCode(UPDATED_REQUEST_CODE)
            .requestType(UPDATED_REQUEST_TYPE)
            .requiredActionFromUserId(UPDATED_REQUIRED_ACTION_FROM_USER_ID)
            .createdBy(UPDATED_CREATED_BY)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);

        restRequestTaskMockMvc.perform(put("/api/request-tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRequestTask)))
            .andExpect(status().isOk());

        // Validate the RequestTask in the database
        List<RequestTask> requestTaskList = requestTaskRepository.findAll();
        assertThat(requestTaskList).hasSize(databaseSizeBeforeUpdate);
        RequestTask testRequestTask = requestTaskList.get(requestTaskList.size() - 1);
        assertThat(testRequestTask.getRequestedUserId()).isEqualTo(UPDATED_REQUESTED_USER_ID);
        assertThat(testRequestTask.getRequestCode()).isEqualTo(UPDATED_REQUEST_CODE);
        assertThat(testRequestTask.getRequestType()).isEqualTo(UPDATED_REQUEST_TYPE);
        assertThat(testRequestTask.getRequiredActionFromUserId()).isEqualTo(UPDATED_REQUIRED_ACTION_FROM_USER_ID);
        assertThat(testRequestTask.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRequestTask.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testRequestTask.getUpdated()).isEqualTo(UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void updateNonExistingRequestTask() throws Exception {
        int databaseSizeBeforeUpdate = requestTaskRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestTaskMockMvc.perform(put("/api/request-tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(requestTask)))
            .andExpect(status().isBadRequest());

        // Validate the RequestTask in the database
        List<RequestTask> requestTaskList = requestTaskRepository.findAll();
        assertThat(requestTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRequestTask() throws Exception {
        // Initialize the database
        requestTaskService.save(requestTask);

        int databaseSizeBeforeDelete = requestTaskRepository.findAll().size();

        // Delete the requestTask
        restRequestTaskMockMvc.perform(delete("/api/request-tasks/{id}", requestTask.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RequestTask> requestTaskList = requestTaskRepository.findAll();
        assertThat(requestTaskList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
