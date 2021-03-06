package com.dd.web.rest;

import com.dd.ClassscheduleApp;
import com.dd.domain.UserExtra;
import com.dd.domain.User;
import com.dd.repository.UserExtraRepository;
import com.dd.service.UserExtraService;
import com.dd.service.dto.UserExtraCriteria;
import com.dd.service.UserExtraQueryService;

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
 * Integration tests for the {@link UserExtraResource} REST controller.
 */
@SpringBootTest(classes = ClassscheduleApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class UserExtraResourceIT {

    private static final String DEFAULT_USER_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_USER_TYPE = "BBBBBBBBBB";

    @Autowired
    private UserExtraRepository userExtraRepository;

    @Autowired
    private UserExtraService userExtraService;

    @Autowired
    private UserExtraQueryService userExtraQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserExtraMockMvc;

    private UserExtra userExtra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserExtra createEntity(EntityManager em) {
        UserExtra userExtra = new UserExtra()
            .userType(DEFAULT_USER_TYPE);
        return userExtra;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserExtra createUpdatedEntity(EntityManager em) {
        UserExtra userExtra = new UserExtra()
            .userType(UPDATED_USER_TYPE);
        return userExtra;
    }

    @BeforeEach
    public void initTest() {
        userExtra = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserExtra() throws Exception {
        int databaseSizeBeforeCreate = userExtraRepository.findAll().size();
        // Create the UserExtra
        restUserExtraMockMvc.perform(post("/api/user-extras")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userExtra)))
            .andExpect(status().isCreated());

        // Validate the UserExtra in the database
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeCreate + 1);
        UserExtra testUserExtra = userExtraList.get(userExtraList.size() - 1);
        assertThat(testUserExtra.getUserType()).isEqualTo(DEFAULT_USER_TYPE);
    }

    @Test
    @Transactional
    public void createUserExtraWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userExtraRepository.findAll().size();

        // Create the UserExtra with an existing ID
        userExtra.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserExtraMockMvc.perform(post("/api/user-extras")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userExtra)))
            .andExpect(status().isBadRequest());

        // Validate the UserExtra in the database
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllUserExtras() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList
        restUserExtraMockMvc.perform(get("/api/user-extras?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userExtra.getId().intValue())))
            .andExpect(jsonPath("$.[*].userType").value(hasItem(DEFAULT_USER_TYPE)));
    }
    
    @Test
    @Transactional
    public void getUserExtra() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get the userExtra
        restUserExtraMockMvc.perform(get("/api/user-extras/{id}", userExtra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userExtra.getId().intValue()))
            .andExpect(jsonPath("$.userType").value(DEFAULT_USER_TYPE));
    }


    @Test
    @Transactional
    public void getUserExtrasByIdFiltering() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        Long id = userExtra.getId();

        defaultUserExtraShouldBeFound("id.equals=" + id);
        defaultUserExtraShouldNotBeFound("id.notEquals=" + id);

        defaultUserExtraShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserExtraShouldNotBeFound("id.greaterThan=" + id);

        defaultUserExtraShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserExtraShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllUserExtrasByUserTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where userType equals to DEFAULT_USER_TYPE
        defaultUserExtraShouldBeFound("userType.equals=" + DEFAULT_USER_TYPE);

        // Get all the userExtraList where userType equals to UPDATED_USER_TYPE
        defaultUserExtraShouldNotBeFound("userType.equals=" + UPDATED_USER_TYPE);
    }

    @Test
    @Transactional
    public void getAllUserExtrasByUserTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where userType not equals to DEFAULT_USER_TYPE
        defaultUserExtraShouldNotBeFound("userType.notEquals=" + DEFAULT_USER_TYPE);

        // Get all the userExtraList where userType not equals to UPDATED_USER_TYPE
        defaultUserExtraShouldBeFound("userType.notEquals=" + UPDATED_USER_TYPE);
    }

    @Test
    @Transactional
    public void getAllUserExtrasByUserTypeIsInShouldWork() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where userType in DEFAULT_USER_TYPE or UPDATED_USER_TYPE
        defaultUserExtraShouldBeFound("userType.in=" + DEFAULT_USER_TYPE + "," + UPDATED_USER_TYPE);

        // Get all the userExtraList where userType equals to UPDATED_USER_TYPE
        defaultUserExtraShouldNotBeFound("userType.in=" + UPDATED_USER_TYPE);
    }

    @Test
    @Transactional
    public void getAllUserExtrasByUserTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where userType is not null
        defaultUserExtraShouldBeFound("userType.specified=true");

        // Get all the userExtraList where userType is null
        defaultUserExtraShouldNotBeFound("userType.specified=false");
    }
                @Test
    @Transactional
    public void getAllUserExtrasByUserTypeContainsSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where userType contains DEFAULT_USER_TYPE
        defaultUserExtraShouldBeFound("userType.contains=" + DEFAULT_USER_TYPE);

        // Get all the userExtraList where userType contains UPDATED_USER_TYPE
        defaultUserExtraShouldNotBeFound("userType.contains=" + UPDATED_USER_TYPE);
    }

    @Test
    @Transactional
    public void getAllUserExtrasByUserTypeNotContainsSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where userType does not contain DEFAULT_USER_TYPE
        defaultUserExtraShouldNotBeFound("userType.doesNotContain=" + DEFAULT_USER_TYPE);

        // Get all the userExtraList where userType does not contain UPDATED_USER_TYPE
        defaultUserExtraShouldBeFound("userType.doesNotContain=" + UPDATED_USER_TYPE);
    }


    @Test
    @Transactional
    public void getAllUserExtrasByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        userExtraRepository.saveAndFlush(userExtra);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userExtra.setUser(user);
        userExtraRepository.saveAndFlush(userExtra);
        Long userId = user.getId();

        // Get all the userExtraList where user equals to userId
        defaultUserExtraShouldBeFound("userId.equals=" + userId);

        // Get all the userExtraList where user equals to userId + 1
        defaultUserExtraShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserExtraShouldBeFound(String filter) throws Exception {
        restUserExtraMockMvc.perform(get("/api/user-extras?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userExtra.getId().intValue())))
            .andExpect(jsonPath("$.[*].userType").value(hasItem(DEFAULT_USER_TYPE)));

        // Check, that the count call also returns 1
        restUserExtraMockMvc.perform(get("/api/user-extras/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserExtraShouldNotBeFound(String filter) throws Exception {
        restUserExtraMockMvc.perform(get("/api/user-extras?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserExtraMockMvc.perform(get("/api/user-extras/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingUserExtra() throws Exception {
        // Get the userExtra
        restUserExtraMockMvc.perform(get("/api/user-extras/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserExtra() throws Exception {
        // Initialize the database
        userExtraService.save(userExtra);

        int databaseSizeBeforeUpdate = userExtraRepository.findAll().size();

        // Update the userExtra
        UserExtra updatedUserExtra = userExtraRepository.findById(userExtra.getId()).get();
        // Disconnect from session so that the updates on updatedUserExtra are not directly saved in db
        em.detach(updatedUserExtra);
        updatedUserExtra
            .userType(UPDATED_USER_TYPE);

        restUserExtraMockMvc.perform(put("/api/user-extras")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserExtra)))
            .andExpect(status().isOk());

        // Validate the UserExtra in the database
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeUpdate);
        UserExtra testUserExtra = userExtraList.get(userExtraList.size() - 1);
        assertThat(testUserExtra.getUserType()).isEqualTo(UPDATED_USER_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingUserExtra() throws Exception {
        int databaseSizeBeforeUpdate = userExtraRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserExtraMockMvc.perform(put("/api/user-extras")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(userExtra)))
            .andExpect(status().isBadRequest());

        // Validate the UserExtra in the database
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserExtra() throws Exception {
        // Initialize the database
        userExtraService.save(userExtra);

        int databaseSizeBeforeDelete = userExtraRepository.findAll().size();

        // Delete the userExtra
        restUserExtraMockMvc.perform(delete("/api/user-extras/{id}", userExtra.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserExtra> userExtraList = userExtraRepository.findAll();
        assertThat(userExtraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
