package com.dd.web.rest;

import com.dd.ClassscheduleApp;
import com.dd.domain.Parent;
import com.dd.domain.User;
import com.dd.repository.ParentRepository;
import com.dd.service.ParentService;
import com.dd.service.dto.ParentCriteria;
import com.dd.service.ParentQueryService;

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
 * Integration tests for the {@link ParentResource} REST controller.
 */
@SpringBootTest(classes = ClassscheduleApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ParentResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private ParentService parentService;

    @Autowired
    private ParentQueryService parentQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParentMockMvc;

    private Parent parent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parent createEntity(EntityManager em) {
        Parent parent = new Parent()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .createdBy(DEFAULT_CREATED_BY)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED);
        return parent;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parent createUpdatedEntity(EntityManager em) {
        Parent parent = new Parent()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .createdBy(UPDATED_CREATED_BY)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);
        return parent;
    }

    @BeforeEach
    public void initTest() {
        parent = createEntity(em);
    }

    @Test
    @Transactional
    public void createParent() throws Exception {
        int databaseSizeBeforeCreate = parentRepository.findAll().size();
        // Create the Parent
        restParentMockMvc.perform(post("/api/parents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(parent)))
            .andExpect(status().isCreated());

        // Validate the Parent in the database
        List<Parent> parentList = parentRepository.findAll();
        assertThat(parentList).hasSize(databaseSizeBeforeCreate + 1);
        Parent testParent = parentList.get(parentList.size() - 1);
        assertThat(testParent.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testParent.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testParent.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testParent.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testParent.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testParent.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testParent.getUpdated()).isEqualTo(DEFAULT_UPDATED);
    }

    @Test
    @Transactional
    public void createParentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = parentRepository.findAll().size();

        // Create the Parent with an existing ID
        parent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParentMockMvc.perform(post("/api/parents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(parent)))
            .andExpect(status().isBadRequest());

        // Validate the Parent in the database
        List<Parent> parentList = parentRepository.findAll();
        assertThat(parentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllParents() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList
        restParentMockMvc.perform(get("/api/parents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parent.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())));
    }
    
    @Test
    @Transactional
    public void getParent() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get the parent
        restParentMockMvc.perform(get("/api/parents/{id}", parent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parent.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.updated").value(DEFAULT_UPDATED.toString()));
    }


    @Test
    @Transactional
    public void getParentsByIdFiltering() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        Long id = parent.getId();

        defaultParentShouldBeFound("id.equals=" + id);
        defaultParentShouldNotBeFound("id.notEquals=" + id);

        defaultParentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultParentShouldNotBeFound("id.greaterThan=" + id);

        defaultParentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultParentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllParentsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where firstName equals to DEFAULT_FIRST_NAME
        defaultParentShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the parentList where firstName equals to UPDATED_FIRST_NAME
        defaultParentShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllParentsByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where firstName not equals to DEFAULT_FIRST_NAME
        defaultParentShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the parentList where firstName not equals to UPDATED_FIRST_NAME
        defaultParentShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllParentsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultParentShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the parentList where firstName equals to UPDATED_FIRST_NAME
        defaultParentShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllParentsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where firstName is not null
        defaultParentShouldBeFound("firstName.specified=true");

        // Get all the parentList where firstName is null
        defaultParentShouldNotBeFound("firstName.specified=false");
    }
                @Test
    @Transactional
    public void getAllParentsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where firstName contains DEFAULT_FIRST_NAME
        defaultParentShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the parentList where firstName contains UPDATED_FIRST_NAME
        defaultParentShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllParentsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where firstName does not contain DEFAULT_FIRST_NAME
        defaultParentShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the parentList where firstName does not contain UPDATED_FIRST_NAME
        defaultParentShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }


    @Test
    @Transactional
    public void getAllParentsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where lastName equals to DEFAULT_LAST_NAME
        defaultParentShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the parentList where lastName equals to UPDATED_LAST_NAME
        defaultParentShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllParentsByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where lastName not equals to DEFAULT_LAST_NAME
        defaultParentShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the parentList where lastName not equals to UPDATED_LAST_NAME
        defaultParentShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllParentsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultParentShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the parentList where lastName equals to UPDATED_LAST_NAME
        defaultParentShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllParentsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where lastName is not null
        defaultParentShouldBeFound("lastName.specified=true");

        // Get all the parentList where lastName is null
        defaultParentShouldNotBeFound("lastName.specified=false");
    }
                @Test
    @Transactional
    public void getAllParentsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where lastName contains DEFAULT_LAST_NAME
        defaultParentShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the parentList where lastName contains UPDATED_LAST_NAME
        defaultParentShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllParentsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where lastName does not contain DEFAULT_LAST_NAME
        defaultParentShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the parentList where lastName does not contain UPDATED_LAST_NAME
        defaultParentShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }


    @Test
    @Transactional
    public void getAllParentsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where email equals to DEFAULT_EMAIL
        defaultParentShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the parentList where email equals to UPDATED_EMAIL
        defaultParentShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllParentsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where email not equals to DEFAULT_EMAIL
        defaultParentShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the parentList where email not equals to UPDATED_EMAIL
        defaultParentShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllParentsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultParentShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the parentList where email equals to UPDATED_EMAIL
        defaultParentShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllParentsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where email is not null
        defaultParentShouldBeFound("email.specified=true");

        // Get all the parentList where email is null
        defaultParentShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllParentsByEmailContainsSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where email contains DEFAULT_EMAIL
        defaultParentShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the parentList where email contains UPDATED_EMAIL
        defaultParentShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllParentsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where email does not contain DEFAULT_EMAIL
        defaultParentShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the parentList where email does not contain UPDATED_EMAIL
        defaultParentShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllParentsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where phone equals to DEFAULT_PHONE
        defaultParentShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the parentList where phone equals to UPDATED_PHONE
        defaultParentShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllParentsByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where phone not equals to DEFAULT_PHONE
        defaultParentShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the parentList where phone not equals to UPDATED_PHONE
        defaultParentShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllParentsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultParentShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the parentList where phone equals to UPDATED_PHONE
        defaultParentShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllParentsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where phone is not null
        defaultParentShouldBeFound("phone.specified=true");

        // Get all the parentList where phone is null
        defaultParentShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllParentsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where phone contains DEFAULT_PHONE
        defaultParentShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the parentList where phone contains UPDATED_PHONE
        defaultParentShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllParentsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where phone does not contain DEFAULT_PHONE
        defaultParentShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the parentList where phone does not contain UPDATED_PHONE
        defaultParentShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllParentsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where createdBy equals to DEFAULT_CREATED_BY
        defaultParentShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the parentList where createdBy equals to UPDATED_CREATED_BY
        defaultParentShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllParentsByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where createdBy not equals to DEFAULT_CREATED_BY
        defaultParentShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the parentList where createdBy not equals to UPDATED_CREATED_BY
        defaultParentShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllParentsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultParentShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the parentList where createdBy equals to UPDATED_CREATED_BY
        defaultParentShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllParentsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where createdBy is not null
        defaultParentShouldBeFound("createdBy.specified=true");

        // Get all the parentList where createdBy is null
        defaultParentShouldNotBeFound("createdBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllParentsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where createdBy contains DEFAULT_CREATED_BY
        defaultParentShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the parentList where createdBy contains UPDATED_CREATED_BY
        defaultParentShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllParentsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where createdBy does not contain DEFAULT_CREATED_BY
        defaultParentShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the parentList where createdBy does not contain UPDATED_CREATED_BY
        defaultParentShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }


    @Test
    @Transactional
    public void getAllParentsByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where created equals to DEFAULT_CREATED
        defaultParentShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the parentList where created equals to UPDATED_CREATED
        defaultParentShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllParentsByCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where created not equals to DEFAULT_CREATED
        defaultParentShouldNotBeFound("created.notEquals=" + DEFAULT_CREATED);

        // Get all the parentList where created not equals to UPDATED_CREATED
        defaultParentShouldBeFound("created.notEquals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllParentsByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultParentShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the parentList where created equals to UPDATED_CREATED
        defaultParentShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllParentsByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where created is not null
        defaultParentShouldBeFound("created.specified=true");

        // Get all the parentList where created is null
        defaultParentShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    public void getAllParentsByUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where updated equals to DEFAULT_UPDATED
        defaultParentShouldBeFound("updated.equals=" + DEFAULT_UPDATED);

        // Get all the parentList where updated equals to UPDATED_UPDATED
        defaultParentShouldNotBeFound("updated.equals=" + UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void getAllParentsByUpdatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where updated not equals to DEFAULT_UPDATED
        defaultParentShouldNotBeFound("updated.notEquals=" + DEFAULT_UPDATED);

        // Get all the parentList where updated not equals to UPDATED_UPDATED
        defaultParentShouldBeFound("updated.notEquals=" + UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void getAllParentsByUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where updated in DEFAULT_UPDATED or UPDATED_UPDATED
        defaultParentShouldBeFound("updated.in=" + DEFAULT_UPDATED + "," + UPDATED_UPDATED);

        // Get all the parentList where updated equals to UPDATED_UPDATED
        defaultParentShouldNotBeFound("updated.in=" + UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void getAllParentsByUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);

        // Get all the parentList where updated is not null
        defaultParentShouldBeFound("updated.specified=true");

        // Get all the parentList where updated is null
        defaultParentShouldNotBeFound("updated.specified=false");
    }

    @Test
    @Transactional
    public void getAllParentsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        parentRepository.saveAndFlush(parent);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        parent.setUser(user);
        parentRepository.saveAndFlush(parent);
        Long userId = user.getId();

        // Get all the parentList where user equals to userId
        defaultParentShouldBeFound("userId.equals=" + userId);

        // Get all the parentList where user equals to userId + 1
        defaultParentShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultParentShouldBeFound(String filter) throws Exception {
        restParentMockMvc.perform(get("/api/parents?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parent.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())));

        // Check, that the count call also returns 1
        restParentMockMvc.perform(get("/api/parents/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultParentShouldNotBeFound(String filter) throws Exception {
        restParentMockMvc.perform(get("/api/parents?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restParentMockMvc.perform(get("/api/parents/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingParent() throws Exception {
        // Get the parent
        restParentMockMvc.perform(get("/api/parents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParent() throws Exception {
        // Initialize the database
        parentService.save(parent);

        int databaseSizeBeforeUpdate = parentRepository.findAll().size();

        // Update the parent
        Parent updatedParent = parentRepository.findById(parent.getId()).get();
        // Disconnect from session so that the updates on updatedParent are not directly saved in db
        em.detach(updatedParent);
        updatedParent
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .createdBy(UPDATED_CREATED_BY)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);

        restParentMockMvc.perform(put("/api/parents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedParent)))
            .andExpect(status().isOk());

        // Validate the Parent in the database
        List<Parent> parentList = parentRepository.findAll();
        assertThat(parentList).hasSize(databaseSizeBeforeUpdate);
        Parent testParent = parentList.get(parentList.size() - 1);
        assertThat(testParent.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testParent.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testParent.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testParent.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testParent.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testParent.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testParent.getUpdated()).isEqualTo(UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void updateNonExistingParent() throws Exception {
        int databaseSizeBeforeUpdate = parentRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParentMockMvc.perform(put("/api/parents")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(parent)))
            .andExpect(status().isBadRequest());

        // Validate the Parent in the database
        List<Parent> parentList = parentRepository.findAll();
        assertThat(parentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteParent() throws Exception {
        // Initialize the database
        parentService.save(parent);

        int databaseSizeBeforeDelete = parentRepository.findAll().size();

        // Delete the parent
        restParentMockMvc.perform(delete("/api/parents/{id}", parent.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Parent> parentList = parentRepository.findAll();
        assertThat(parentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
