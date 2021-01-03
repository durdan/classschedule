package com.dd.web.rest;

import com.dd.ClassscheduleApp;
import com.dd.domain.Invite;
import com.dd.domain.InviteStatus;
import com.dd.repository.InviteRepository;
import com.dd.service.InviteService;
import com.dd.service.dto.InviteCriteria;
import com.dd.service.InviteQueryService;

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
 * Integration tests for the {@link InviteResource} REST controller.
 */
@SpringBootTest(classes = ClassscheduleApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class InviteResourceIT {

    private static final String DEFAULT_REQUESTED_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_REQUESTED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_INVITE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_INVITE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_INVITED_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_INVITED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private InviteRepository inviteRepository;

    @Autowired
    private InviteService inviteService;

    @Autowired
    private InviteQueryService inviteQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInviteMockMvc;

    private Invite invite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invite createEntity(EntityManager em) {
        Invite invite = new Invite()
            .requestedUserId(DEFAULT_REQUESTED_USER_ID)
            .inviteCode(DEFAULT_INVITE_CODE)
            .invitedUserId(DEFAULT_INVITED_USER_ID)
            .createdBy(DEFAULT_CREATED_BY)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED);
        return invite;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invite createUpdatedEntity(EntityManager em) {
        Invite invite = new Invite()
            .requestedUserId(UPDATED_REQUESTED_USER_ID)
            .inviteCode(UPDATED_INVITE_CODE)
            .invitedUserId(UPDATED_INVITED_USER_ID)
            .createdBy(UPDATED_CREATED_BY)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);
        return invite;
    }

    @BeforeEach
    public void initTest() {
        invite = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvite() throws Exception {
        int databaseSizeBeforeCreate = inviteRepository.findAll().size();
        // Create the Invite
        restInviteMockMvc.perform(post("/api/invites")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invite)))
            .andExpect(status().isCreated());

        // Validate the Invite in the database
        List<Invite> inviteList = inviteRepository.findAll();
        assertThat(inviteList).hasSize(databaseSizeBeforeCreate + 1);
        Invite testInvite = inviteList.get(inviteList.size() - 1);
        assertThat(testInvite.getRequestedUserId()).isEqualTo(DEFAULT_REQUESTED_USER_ID);
        assertThat(testInvite.getInviteCode()).isEqualTo(DEFAULT_INVITE_CODE);
        assertThat(testInvite.getInvitedUserId()).isEqualTo(DEFAULT_INVITED_USER_ID);
        assertThat(testInvite.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testInvite.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testInvite.getUpdated()).isEqualTo(DEFAULT_UPDATED);
    }

    @Test
    @Transactional
    public void createInviteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = inviteRepository.findAll().size();

        // Create the Invite with an existing ID
        invite.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInviteMockMvc.perform(post("/api/invites")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invite)))
            .andExpect(status().isBadRequest());

        // Validate the Invite in the database
        List<Invite> inviteList = inviteRepository.findAll();
        assertThat(inviteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllInvites() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList
        restInviteMockMvc.perform(get("/api/invites?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invite.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestedUserId").value(hasItem(DEFAULT_REQUESTED_USER_ID)))
            .andExpect(jsonPath("$.[*].inviteCode").value(hasItem(DEFAULT_INVITE_CODE)))
            .andExpect(jsonPath("$.[*].invitedUserId").value(hasItem(DEFAULT_INVITED_USER_ID)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())));
    }
    
    @Test
    @Transactional
    public void getInvite() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get the invite
        restInviteMockMvc.perform(get("/api/invites/{id}", invite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invite.getId().intValue()))
            .andExpect(jsonPath("$.requestedUserId").value(DEFAULT_REQUESTED_USER_ID))
            .andExpect(jsonPath("$.inviteCode").value(DEFAULT_INVITE_CODE))
            .andExpect(jsonPath("$.invitedUserId").value(DEFAULT_INVITED_USER_ID))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.updated").value(DEFAULT_UPDATED.toString()));
    }


    @Test
    @Transactional
    public void getInvitesByIdFiltering() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        Long id = invite.getId();

        defaultInviteShouldBeFound("id.equals=" + id);
        defaultInviteShouldNotBeFound("id.notEquals=" + id);

        defaultInviteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInviteShouldNotBeFound("id.greaterThan=" + id);

        defaultInviteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInviteShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllInvitesByRequestedUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where requestedUserId equals to DEFAULT_REQUESTED_USER_ID
        defaultInviteShouldBeFound("requestedUserId.equals=" + DEFAULT_REQUESTED_USER_ID);

        // Get all the inviteList where requestedUserId equals to UPDATED_REQUESTED_USER_ID
        defaultInviteShouldNotBeFound("requestedUserId.equals=" + UPDATED_REQUESTED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllInvitesByRequestedUserIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where requestedUserId not equals to DEFAULT_REQUESTED_USER_ID
        defaultInviteShouldNotBeFound("requestedUserId.notEquals=" + DEFAULT_REQUESTED_USER_ID);

        // Get all the inviteList where requestedUserId not equals to UPDATED_REQUESTED_USER_ID
        defaultInviteShouldBeFound("requestedUserId.notEquals=" + UPDATED_REQUESTED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllInvitesByRequestedUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where requestedUserId in DEFAULT_REQUESTED_USER_ID or UPDATED_REQUESTED_USER_ID
        defaultInviteShouldBeFound("requestedUserId.in=" + DEFAULT_REQUESTED_USER_ID + "," + UPDATED_REQUESTED_USER_ID);

        // Get all the inviteList where requestedUserId equals to UPDATED_REQUESTED_USER_ID
        defaultInviteShouldNotBeFound("requestedUserId.in=" + UPDATED_REQUESTED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllInvitesByRequestedUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where requestedUserId is not null
        defaultInviteShouldBeFound("requestedUserId.specified=true");

        // Get all the inviteList where requestedUserId is null
        defaultInviteShouldNotBeFound("requestedUserId.specified=false");
    }
                @Test
    @Transactional
    public void getAllInvitesByRequestedUserIdContainsSomething() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where requestedUserId contains DEFAULT_REQUESTED_USER_ID
        defaultInviteShouldBeFound("requestedUserId.contains=" + DEFAULT_REQUESTED_USER_ID);

        // Get all the inviteList where requestedUserId contains UPDATED_REQUESTED_USER_ID
        defaultInviteShouldNotBeFound("requestedUserId.contains=" + UPDATED_REQUESTED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllInvitesByRequestedUserIdNotContainsSomething() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where requestedUserId does not contain DEFAULT_REQUESTED_USER_ID
        defaultInviteShouldNotBeFound("requestedUserId.doesNotContain=" + DEFAULT_REQUESTED_USER_ID);

        // Get all the inviteList where requestedUserId does not contain UPDATED_REQUESTED_USER_ID
        defaultInviteShouldBeFound("requestedUserId.doesNotContain=" + UPDATED_REQUESTED_USER_ID);
    }


    @Test
    @Transactional
    public void getAllInvitesByInviteCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where inviteCode equals to DEFAULT_INVITE_CODE
        defaultInviteShouldBeFound("inviteCode.equals=" + DEFAULT_INVITE_CODE);

        // Get all the inviteList where inviteCode equals to UPDATED_INVITE_CODE
        defaultInviteShouldNotBeFound("inviteCode.equals=" + UPDATED_INVITE_CODE);
    }

    @Test
    @Transactional
    public void getAllInvitesByInviteCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where inviteCode not equals to DEFAULT_INVITE_CODE
        defaultInviteShouldNotBeFound("inviteCode.notEquals=" + DEFAULT_INVITE_CODE);

        // Get all the inviteList where inviteCode not equals to UPDATED_INVITE_CODE
        defaultInviteShouldBeFound("inviteCode.notEquals=" + UPDATED_INVITE_CODE);
    }

    @Test
    @Transactional
    public void getAllInvitesByInviteCodeIsInShouldWork() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where inviteCode in DEFAULT_INVITE_CODE or UPDATED_INVITE_CODE
        defaultInviteShouldBeFound("inviteCode.in=" + DEFAULT_INVITE_CODE + "," + UPDATED_INVITE_CODE);

        // Get all the inviteList where inviteCode equals to UPDATED_INVITE_CODE
        defaultInviteShouldNotBeFound("inviteCode.in=" + UPDATED_INVITE_CODE);
    }

    @Test
    @Transactional
    public void getAllInvitesByInviteCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where inviteCode is not null
        defaultInviteShouldBeFound("inviteCode.specified=true");

        // Get all the inviteList where inviteCode is null
        defaultInviteShouldNotBeFound("inviteCode.specified=false");
    }
                @Test
    @Transactional
    public void getAllInvitesByInviteCodeContainsSomething() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where inviteCode contains DEFAULT_INVITE_CODE
        defaultInviteShouldBeFound("inviteCode.contains=" + DEFAULT_INVITE_CODE);

        // Get all the inviteList where inviteCode contains UPDATED_INVITE_CODE
        defaultInviteShouldNotBeFound("inviteCode.contains=" + UPDATED_INVITE_CODE);
    }

    @Test
    @Transactional
    public void getAllInvitesByInviteCodeNotContainsSomething() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where inviteCode does not contain DEFAULT_INVITE_CODE
        defaultInviteShouldNotBeFound("inviteCode.doesNotContain=" + DEFAULT_INVITE_CODE);

        // Get all the inviteList where inviteCode does not contain UPDATED_INVITE_CODE
        defaultInviteShouldBeFound("inviteCode.doesNotContain=" + UPDATED_INVITE_CODE);
    }


    @Test
    @Transactional
    public void getAllInvitesByInvitedUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where invitedUserId equals to DEFAULT_INVITED_USER_ID
        defaultInviteShouldBeFound("invitedUserId.equals=" + DEFAULT_INVITED_USER_ID);

        // Get all the inviteList where invitedUserId equals to UPDATED_INVITED_USER_ID
        defaultInviteShouldNotBeFound("invitedUserId.equals=" + UPDATED_INVITED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllInvitesByInvitedUserIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where invitedUserId not equals to DEFAULT_INVITED_USER_ID
        defaultInviteShouldNotBeFound("invitedUserId.notEquals=" + DEFAULT_INVITED_USER_ID);

        // Get all the inviteList where invitedUserId not equals to UPDATED_INVITED_USER_ID
        defaultInviteShouldBeFound("invitedUserId.notEquals=" + UPDATED_INVITED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllInvitesByInvitedUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where invitedUserId in DEFAULT_INVITED_USER_ID or UPDATED_INVITED_USER_ID
        defaultInviteShouldBeFound("invitedUserId.in=" + DEFAULT_INVITED_USER_ID + "," + UPDATED_INVITED_USER_ID);

        // Get all the inviteList where invitedUserId equals to UPDATED_INVITED_USER_ID
        defaultInviteShouldNotBeFound("invitedUserId.in=" + UPDATED_INVITED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllInvitesByInvitedUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where invitedUserId is not null
        defaultInviteShouldBeFound("invitedUserId.specified=true");

        // Get all the inviteList where invitedUserId is null
        defaultInviteShouldNotBeFound("invitedUserId.specified=false");
    }
                @Test
    @Transactional
    public void getAllInvitesByInvitedUserIdContainsSomething() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where invitedUserId contains DEFAULT_INVITED_USER_ID
        defaultInviteShouldBeFound("invitedUserId.contains=" + DEFAULT_INVITED_USER_ID);

        // Get all the inviteList where invitedUserId contains UPDATED_INVITED_USER_ID
        defaultInviteShouldNotBeFound("invitedUserId.contains=" + UPDATED_INVITED_USER_ID);
    }

    @Test
    @Transactional
    public void getAllInvitesByInvitedUserIdNotContainsSomething() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where invitedUserId does not contain DEFAULT_INVITED_USER_ID
        defaultInviteShouldNotBeFound("invitedUserId.doesNotContain=" + DEFAULT_INVITED_USER_ID);

        // Get all the inviteList where invitedUserId does not contain UPDATED_INVITED_USER_ID
        defaultInviteShouldBeFound("invitedUserId.doesNotContain=" + UPDATED_INVITED_USER_ID);
    }


    @Test
    @Transactional
    public void getAllInvitesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where createdBy equals to DEFAULT_CREATED_BY
        defaultInviteShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the inviteList where createdBy equals to UPDATED_CREATED_BY
        defaultInviteShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllInvitesByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where createdBy not equals to DEFAULT_CREATED_BY
        defaultInviteShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the inviteList where createdBy not equals to UPDATED_CREATED_BY
        defaultInviteShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllInvitesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultInviteShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the inviteList where createdBy equals to UPDATED_CREATED_BY
        defaultInviteShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllInvitesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where createdBy is not null
        defaultInviteShouldBeFound("createdBy.specified=true");

        // Get all the inviteList where createdBy is null
        defaultInviteShouldNotBeFound("createdBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllInvitesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where createdBy contains DEFAULT_CREATED_BY
        defaultInviteShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the inviteList where createdBy contains UPDATED_CREATED_BY
        defaultInviteShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllInvitesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where createdBy does not contain DEFAULT_CREATED_BY
        defaultInviteShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the inviteList where createdBy does not contain UPDATED_CREATED_BY
        defaultInviteShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }


    @Test
    @Transactional
    public void getAllInvitesByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where created equals to DEFAULT_CREATED
        defaultInviteShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the inviteList where created equals to UPDATED_CREATED
        defaultInviteShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllInvitesByCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where created not equals to DEFAULT_CREATED
        defaultInviteShouldNotBeFound("created.notEquals=" + DEFAULT_CREATED);

        // Get all the inviteList where created not equals to UPDATED_CREATED
        defaultInviteShouldBeFound("created.notEquals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllInvitesByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultInviteShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the inviteList where created equals to UPDATED_CREATED
        defaultInviteShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllInvitesByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where created is not null
        defaultInviteShouldBeFound("created.specified=true");

        // Get all the inviteList where created is null
        defaultInviteShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvitesByUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where updated equals to DEFAULT_UPDATED
        defaultInviteShouldBeFound("updated.equals=" + DEFAULT_UPDATED);

        // Get all the inviteList where updated equals to UPDATED_UPDATED
        defaultInviteShouldNotBeFound("updated.equals=" + UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void getAllInvitesByUpdatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where updated not equals to DEFAULT_UPDATED
        defaultInviteShouldNotBeFound("updated.notEquals=" + DEFAULT_UPDATED);

        // Get all the inviteList where updated not equals to UPDATED_UPDATED
        defaultInviteShouldBeFound("updated.notEquals=" + UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void getAllInvitesByUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where updated in DEFAULT_UPDATED or UPDATED_UPDATED
        defaultInviteShouldBeFound("updated.in=" + DEFAULT_UPDATED + "," + UPDATED_UPDATED);

        // Get all the inviteList where updated equals to UPDATED_UPDATED
        defaultInviteShouldNotBeFound("updated.in=" + UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void getAllInvitesByUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);

        // Get all the inviteList where updated is not null
        defaultInviteShouldBeFound("updated.specified=true");

        // Get all the inviteList where updated is null
        defaultInviteShouldNotBeFound("updated.specified=false");
    }

    @Test
    @Transactional
    public void getAllInvitesByInvitestatusIsEqualToSomething() throws Exception {
        // Initialize the database
        inviteRepository.saveAndFlush(invite);
        InviteStatus invitestatus = InviteStatusResourceIT.createEntity(em);
        em.persist(invitestatus);
        em.flush();
        invite.setInvitestatus(invitestatus);
        inviteRepository.saveAndFlush(invite);
        Long invitestatusId = invitestatus.getId();

        // Get all the inviteList where invitestatus equals to invitestatusId
        defaultInviteShouldBeFound("invitestatusId.equals=" + invitestatusId);

        // Get all the inviteList where invitestatus equals to invitestatusId + 1
        defaultInviteShouldNotBeFound("invitestatusId.equals=" + (invitestatusId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInviteShouldBeFound(String filter) throws Exception {
        restInviteMockMvc.perform(get("/api/invites?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invite.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestedUserId").value(hasItem(DEFAULT_REQUESTED_USER_ID)))
            .andExpect(jsonPath("$.[*].inviteCode").value(hasItem(DEFAULT_INVITE_CODE)))
            .andExpect(jsonPath("$.[*].invitedUserId").value(hasItem(DEFAULT_INVITED_USER_ID)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())));

        // Check, that the count call also returns 1
        restInviteMockMvc.perform(get("/api/invites/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInviteShouldNotBeFound(String filter) throws Exception {
        restInviteMockMvc.perform(get("/api/invites?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInviteMockMvc.perform(get("/api/invites/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingInvite() throws Exception {
        // Get the invite
        restInviteMockMvc.perform(get("/api/invites/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvite() throws Exception {
        // Initialize the database
        inviteService.save(invite);

        int databaseSizeBeforeUpdate = inviteRepository.findAll().size();

        // Update the invite
        Invite updatedInvite = inviteRepository.findById(invite.getId()).get();
        // Disconnect from session so that the updates on updatedInvite are not directly saved in db
        em.detach(updatedInvite);
        updatedInvite
            .requestedUserId(UPDATED_REQUESTED_USER_ID)
            .inviteCode(UPDATED_INVITE_CODE)
            .invitedUserId(UPDATED_INVITED_USER_ID)
            .createdBy(UPDATED_CREATED_BY)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);

        restInviteMockMvc.perform(put("/api/invites")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedInvite)))
            .andExpect(status().isOk());

        // Validate the Invite in the database
        List<Invite> inviteList = inviteRepository.findAll();
        assertThat(inviteList).hasSize(databaseSizeBeforeUpdate);
        Invite testInvite = inviteList.get(inviteList.size() - 1);
        assertThat(testInvite.getRequestedUserId()).isEqualTo(UPDATED_REQUESTED_USER_ID);
        assertThat(testInvite.getInviteCode()).isEqualTo(UPDATED_INVITE_CODE);
        assertThat(testInvite.getInvitedUserId()).isEqualTo(UPDATED_INVITED_USER_ID);
        assertThat(testInvite.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testInvite.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testInvite.getUpdated()).isEqualTo(UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void updateNonExistingInvite() throws Exception {
        int databaseSizeBeforeUpdate = inviteRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInviteMockMvc.perform(put("/api/invites")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(invite)))
            .andExpect(status().isBadRequest());

        // Validate the Invite in the database
        List<Invite> inviteList = inviteRepository.findAll();
        assertThat(inviteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInvite() throws Exception {
        // Initialize the database
        inviteService.save(invite);

        int databaseSizeBeforeDelete = inviteRepository.findAll().size();

        // Delete the invite
        restInviteMockMvc.perform(delete("/api/invites/{id}", invite.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Invite> inviteList = inviteRepository.findAll();
        assertThat(inviteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
