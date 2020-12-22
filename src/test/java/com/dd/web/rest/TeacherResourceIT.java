package com.dd.web.rest;

import com.dd.ClassscheduleApp;
import com.dd.domain.Teacher;
import com.dd.domain.User;
import com.dd.domain.Parent;
import com.dd.domain.Student;
import com.dd.repository.TeacherRepository;
import com.dd.service.TeacherService;
import com.dd.service.dto.TeacherCriteria;
import com.dd.service.TeacherQueryService;

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
 * Integration tests for the {@link TeacherResource} REST controller.
 */
@SpringBootTest(classes = ClassscheduleApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TeacherResourceIT {

    private static final String DEFAULT_PROFILE_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_CONTENT = "BBBBBBBBBB";

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
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherQueryService teacherQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTeacherMockMvc;

    private Teacher teacher;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Teacher createEntity(EntityManager em) {
        Teacher teacher = new Teacher()
            .profileContent(DEFAULT_PROFILE_CONTENT)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .createdBy(DEFAULT_CREATED_BY)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED);
        return teacher;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Teacher createUpdatedEntity(EntityManager em) {
        Teacher teacher = new Teacher()
            .profileContent(UPDATED_PROFILE_CONTENT)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .createdBy(UPDATED_CREATED_BY)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);
        return teacher;
    }

    @BeforeEach
    public void initTest() {
        teacher = createEntity(em);
    }

    @Test
    @Transactional
    public void createTeacher() throws Exception {
        int databaseSizeBeforeCreate = teacherRepository.findAll().size();
        // Create the Teacher
        restTeacherMockMvc.perform(post("/api/teachers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(teacher)))
            .andExpect(status().isCreated());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeCreate + 1);
        Teacher testTeacher = teacherList.get(teacherList.size() - 1);
        assertThat(testTeacher.getProfileContent()).isEqualTo(DEFAULT_PROFILE_CONTENT);
        assertThat(testTeacher.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testTeacher.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testTeacher.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTeacher.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testTeacher.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTeacher.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testTeacher.getUpdated()).isEqualTo(DEFAULT_UPDATED);
    }

    @Test
    @Transactional
    public void createTeacherWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = teacherRepository.findAll().size();

        // Create the Teacher with an existing ID
        teacher.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeacherMockMvc.perform(post("/api/teachers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(teacher)))
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTeachers() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList
        restTeacherMockMvc.perform(get("/api/teachers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teacher.getId().intValue())))
            .andExpect(jsonPath("$.[*].profileContent").value(hasItem(DEFAULT_PROFILE_CONTENT)))
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
    public void getTeacher() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get the teacher
        restTeacherMockMvc.perform(get("/api/teachers/{id}", teacher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(teacher.getId().intValue()))
            .andExpect(jsonPath("$.profileContent").value(DEFAULT_PROFILE_CONTENT))
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
    public void getTeachersByIdFiltering() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        Long id = teacher.getId();

        defaultTeacherShouldBeFound("id.equals=" + id);
        defaultTeacherShouldNotBeFound("id.notEquals=" + id);

        defaultTeacherShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTeacherShouldNotBeFound("id.greaterThan=" + id);

        defaultTeacherShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTeacherShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTeachersByProfileContentIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where profileContent equals to DEFAULT_PROFILE_CONTENT
        defaultTeacherShouldBeFound("profileContent.equals=" + DEFAULT_PROFILE_CONTENT);

        // Get all the teacherList where profileContent equals to UPDATED_PROFILE_CONTENT
        defaultTeacherShouldNotBeFound("profileContent.equals=" + UPDATED_PROFILE_CONTENT);
    }

    @Test
    @Transactional
    public void getAllTeachersByProfileContentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where profileContent not equals to DEFAULT_PROFILE_CONTENT
        defaultTeacherShouldNotBeFound("profileContent.notEquals=" + DEFAULT_PROFILE_CONTENT);

        // Get all the teacherList where profileContent not equals to UPDATED_PROFILE_CONTENT
        defaultTeacherShouldBeFound("profileContent.notEquals=" + UPDATED_PROFILE_CONTENT);
    }

    @Test
    @Transactional
    public void getAllTeachersByProfileContentIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where profileContent in DEFAULT_PROFILE_CONTENT or UPDATED_PROFILE_CONTENT
        defaultTeacherShouldBeFound("profileContent.in=" + DEFAULT_PROFILE_CONTENT + "," + UPDATED_PROFILE_CONTENT);

        // Get all the teacherList where profileContent equals to UPDATED_PROFILE_CONTENT
        defaultTeacherShouldNotBeFound("profileContent.in=" + UPDATED_PROFILE_CONTENT);
    }

    @Test
    @Transactional
    public void getAllTeachersByProfileContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where profileContent is not null
        defaultTeacherShouldBeFound("profileContent.specified=true");

        // Get all the teacherList where profileContent is null
        defaultTeacherShouldNotBeFound("profileContent.specified=false");
    }
                @Test
    @Transactional
    public void getAllTeachersByProfileContentContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where profileContent contains DEFAULT_PROFILE_CONTENT
        defaultTeacherShouldBeFound("profileContent.contains=" + DEFAULT_PROFILE_CONTENT);

        // Get all the teacherList where profileContent contains UPDATED_PROFILE_CONTENT
        defaultTeacherShouldNotBeFound("profileContent.contains=" + UPDATED_PROFILE_CONTENT);
    }

    @Test
    @Transactional
    public void getAllTeachersByProfileContentNotContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where profileContent does not contain DEFAULT_PROFILE_CONTENT
        defaultTeacherShouldNotBeFound("profileContent.doesNotContain=" + DEFAULT_PROFILE_CONTENT);

        // Get all the teacherList where profileContent does not contain UPDATED_PROFILE_CONTENT
        defaultTeacherShouldBeFound("profileContent.doesNotContain=" + UPDATED_PROFILE_CONTENT);
    }


    @Test
    @Transactional
    public void getAllTeachersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where firstName equals to DEFAULT_FIRST_NAME
        defaultTeacherShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the teacherList where firstName equals to UPDATED_FIRST_NAME
        defaultTeacherShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllTeachersByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where firstName not equals to DEFAULT_FIRST_NAME
        defaultTeacherShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the teacherList where firstName not equals to UPDATED_FIRST_NAME
        defaultTeacherShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllTeachersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultTeacherShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the teacherList where firstName equals to UPDATED_FIRST_NAME
        defaultTeacherShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllTeachersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where firstName is not null
        defaultTeacherShouldBeFound("firstName.specified=true");

        // Get all the teacherList where firstName is null
        defaultTeacherShouldNotBeFound("firstName.specified=false");
    }
                @Test
    @Transactional
    public void getAllTeachersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where firstName contains DEFAULT_FIRST_NAME
        defaultTeacherShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the teacherList where firstName contains UPDATED_FIRST_NAME
        defaultTeacherShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllTeachersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where firstName does not contain DEFAULT_FIRST_NAME
        defaultTeacherShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the teacherList where firstName does not contain UPDATED_FIRST_NAME
        defaultTeacherShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }


    @Test
    @Transactional
    public void getAllTeachersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where lastName equals to DEFAULT_LAST_NAME
        defaultTeacherShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the teacherList where lastName equals to UPDATED_LAST_NAME
        defaultTeacherShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllTeachersByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where lastName not equals to DEFAULT_LAST_NAME
        defaultTeacherShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the teacherList where lastName not equals to UPDATED_LAST_NAME
        defaultTeacherShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllTeachersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultTeacherShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the teacherList where lastName equals to UPDATED_LAST_NAME
        defaultTeacherShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllTeachersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where lastName is not null
        defaultTeacherShouldBeFound("lastName.specified=true");

        // Get all the teacherList where lastName is null
        defaultTeacherShouldNotBeFound("lastName.specified=false");
    }
                @Test
    @Transactional
    public void getAllTeachersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where lastName contains DEFAULT_LAST_NAME
        defaultTeacherShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the teacherList where lastName contains UPDATED_LAST_NAME
        defaultTeacherShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllTeachersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where lastName does not contain DEFAULT_LAST_NAME
        defaultTeacherShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the teacherList where lastName does not contain UPDATED_LAST_NAME
        defaultTeacherShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }


    @Test
    @Transactional
    public void getAllTeachersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where email equals to DEFAULT_EMAIL
        defaultTeacherShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the teacherList where email equals to UPDATED_EMAIL
        defaultTeacherShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllTeachersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where email not equals to DEFAULT_EMAIL
        defaultTeacherShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the teacherList where email not equals to UPDATED_EMAIL
        defaultTeacherShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllTeachersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultTeacherShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the teacherList where email equals to UPDATED_EMAIL
        defaultTeacherShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllTeachersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where email is not null
        defaultTeacherShouldBeFound("email.specified=true");

        // Get all the teacherList where email is null
        defaultTeacherShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllTeachersByEmailContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where email contains DEFAULT_EMAIL
        defaultTeacherShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the teacherList where email contains UPDATED_EMAIL
        defaultTeacherShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllTeachersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where email does not contain DEFAULT_EMAIL
        defaultTeacherShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the teacherList where email does not contain UPDATED_EMAIL
        defaultTeacherShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllTeachersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where phone equals to DEFAULT_PHONE
        defaultTeacherShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the teacherList where phone equals to UPDATED_PHONE
        defaultTeacherShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllTeachersByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where phone not equals to DEFAULT_PHONE
        defaultTeacherShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the teacherList where phone not equals to UPDATED_PHONE
        defaultTeacherShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllTeachersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultTeacherShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the teacherList where phone equals to UPDATED_PHONE
        defaultTeacherShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllTeachersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where phone is not null
        defaultTeacherShouldBeFound("phone.specified=true");

        // Get all the teacherList where phone is null
        defaultTeacherShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllTeachersByPhoneContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where phone contains DEFAULT_PHONE
        defaultTeacherShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the teacherList where phone contains UPDATED_PHONE
        defaultTeacherShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllTeachersByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where phone does not contain DEFAULT_PHONE
        defaultTeacherShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the teacherList where phone does not contain UPDATED_PHONE
        defaultTeacherShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllTeachersByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where createdBy equals to DEFAULT_CREATED_BY
        defaultTeacherShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the teacherList where createdBy equals to UPDATED_CREATED_BY
        defaultTeacherShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllTeachersByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where createdBy not equals to DEFAULT_CREATED_BY
        defaultTeacherShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the teacherList where createdBy not equals to UPDATED_CREATED_BY
        defaultTeacherShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllTeachersByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultTeacherShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the teacherList where createdBy equals to UPDATED_CREATED_BY
        defaultTeacherShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllTeachersByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where createdBy is not null
        defaultTeacherShouldBeFound("createdBy.specified=true");

        // Get all the teacherList where createdBy is null
        defaultTeacherShouldNotBeFound("createdBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllTeachersByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where createdBy contains DEFAULT_CREATED_BY
        defaultTeacherShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the teacherList where createdBy contains UPDATED_CREATED_BY
        defaultTeacherShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllTeachersByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where createdBy does not contain DEFAULT_CREATED_BY
        defaultTeacherShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the teacherList where createdBy does not contain UPDATED_CREATED_BY
        defaultTeacherShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }


    @Test
    @Transactional
    public void getAllTeachersByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where created equals to DEFAULT_CREATED
        defaultTeacherShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the teacherList where created equals to UPDATED_CREATED
        defaultTeacherShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllTeachersByCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where created not equals to DEFAULT_CREATED
        defaultTeacherShouldNotBeFound("created.notEquals=" + DEFAULT_CREATED);

        // Get all the teacherList where created not equals to UPDATED_CREATED
        defaultTeacherShouldBeFound("created.notEquals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllTeachersByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultTeacherShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the teacherList where created equals to UPDATED_CREATED
        defaultTeacherShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllTeachersByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where created is not null
        defaultTeacherShouldBeFound("created.specified=true");

        // Get all the teacherList where created is null
        defaultTeacherShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeachersByUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where updated equals to DEFAULT_UPDATED
        defaultTeacherShouldBeFound("updated.equals=" + DEFAULT_UPDATED);

        // Get all the teacherList where updated equals to UPDATED_UPDATED
        defaultTeacherShouldNotBeFound("updated.equals=" + UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void getAllTeachersByUpdatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where updated not equals to DEFAULT_UPDATED
        defaultTeacherShouldNotBeFound("updated.notEquals=" + DEFAULT_UPDATED);

        // Get all the teacherList where updated not equals to UPDATED_UPDATED
        defaultTeacherShouldBeFound("updated.notEquals=" + UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void getAllTeachersByUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where updated in DEFAULT_UPDATED or UPDATED_UPDATED
        defaultTeacherShouldBeFound("updated.in=" + DEFAULT_UPDATED + "," + UPDATED_UPDATED);

        // Get all the teacherList where updated equals to UPDATED_UPDATED
        defaultTeacherShouldNotBeFound("updated.in=" + UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void getAllTeachersByUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList where updated is not null
        defaultTeacherShouldBeFound("updated.specified=true");

        // Get all the teacherList where updated is null
        defaultTeacherShouldNotBeFound("updated.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeachersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        teacher.setUser(user);
        teacherRepository.saveAndFlush(teacher);
        Long userId = user.getId();

        // Get all the teacherList where user equals to userId
        defaultTeacherShouldBeFound("userId.equals=" + userId);

        // Get all the teacherList where user equals to userId + 1
        defaultTeacherShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllTeachersByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);
        Parent parent = ParentResourceIT.createEntity(em);
        em.persist(parent);
        em.flush();
        teacher.setParent(parent);
        teacherRepository.saveAndFlush(teacher);
        Long parentId = parent.getId();

        // Get all the teacherList where parent equals to parentId
        defaultTeacherShouldBeFound("parentId.equals=" + parentId);

        // Get all the teacherList where parent equals to parentId + 1
        defaultTeacherShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }


    @Test
    @Transactional
    public void getAllTeachersByStudentIsEqualToSomething() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);
        Student student = StudentResourceIT.createEntity(em);
        em.persist(student);
        em.flush();
        teacher.setStudent(student);
        teacherRepository.saveAndFlush(teacher);
        Long studentId = student.getId();

        // Get all the teacherList where student equals to studentId
        defaultTeacherShouldBeFound("studentId.equals=" + studentId);

        // Get all the teacherList where student equals to studentId + 1
        defaultTeacherShouldNotBeFound("studentId.equals=" + (studentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTeacherShouldBeFound(String filter) throws Exception {
        restTeacherMockMvc.perform(get("/api/teachers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teacher.getId().intValue())))
            .andExpect(jsonPath("$.[*].profileContent").value(hasItem(DEFAULT_PROFILE_CONTENT)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())));

        // Check, that the count call also returns 1
        restTeacherMockMvc.perform(get("/api/teachers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTeacherShouldNotBeFound(String filter) throws Exception {
        restTeacherMockMvc.perform(get("/api/teachers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTeacherMockMvc.perform(get("/api/teachers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingTeacher() throws Exception {
        // Get the teacher
        restTeacherMockMvc.perform(get("/api/teachers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeacher() throws Exception {
        // Initialize the database
        teacherService.save(teacher);

        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();

        // Update the teacher
        Teacher updatedTeacher = teacherRepository.findById(teacher.getId()).get();
        // Disconnect from session so that the updates on updatedTeacher are not directly saved in db
        em.detach(updatedTeacher);
        updatedTeacher
            .profileContent(UPDATED_PROFILE_CONTENT)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .createdBy(UPDATED_CREATED_BY)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);

        restTeacherMockMvc.perform(put("/api/teachers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTeacher)))
            .andExpect(status().isOk());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
        Teacher testTeacher = teacherList.get(teacherList.size() - 1);
        assertThat(testTeacher.getProfileContent()).isEqualTo(UPDATED_PROFILE_CONTENT);
        assertThat(testTeacher.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testTeacher.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testTeacher.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTeacher.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testTeacher.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTeacher.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testTeacher.getUpdated()).isEqualTo(UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void updateNonExistingTeacher() throws Exception {
        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeacherMockMvc.perform(put("/api/teachers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(teacher)))
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTeacher() throws Exception {
        // Initialize the database
        teacherService.save(teacher);

        int databaseSizeBeforeDelete = teacherRepository.findAll().size();

        // Delete the teacher
        restTeacherMockMvc.perform(delete("/api/teachers/{id}", teacher.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Teacher> teacherList = teacherRepository.findAll();
        assertThat(teacherList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
