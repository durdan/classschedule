package com.dd.web.rest;

import com.dd.ClassscheduleApp;
import com.dd.domain.Student;
import com.dd.domain.User;
import com.dd.domain.Parent;
import com.dd.domain.Teacher;
import com.dd.repository.StudentRepository;
import com.dd.service.StudentService;
import com.dd.service.dto.StudentCriteria;
import com.dd.service.StudentQueryService;

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
 * Integration tests for the {@link StudentResource} REST controller.
 */
@SpringBootTest(classes = ClassscheduleApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class StudentResourceIT {

    private static final String DEFAULT_SCHOOL_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_SCHOOL_YEAR = "BBBBBBBBBB";

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
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentQueryService studentQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentMockMvc;

    private Student student;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Student createEntity(EntityManager em) {
        Student student = new Student()
            .schoolYear(DEFAULT_SCHOOL_YEAR)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .createdBy(DEFAULT_CREATED_BY)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED);
        return student;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Student createUpdatedEntity(EntityManager em) {
        Student student = new Student()
            .schoolYear(UPDATED_SCHOOL_YEAR)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .createdBy(UPDATED_CREATED_BY)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);
        return student;
    }

    @BeforeEach
    public void initTest() {
        student = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudent() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();
        // Create the Student
        restStudentMockMvc.perform(post("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isCreated());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate + 1);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getSchoolYear()).isEqualTo(DEFAULT_SCHOOL_YEAR);
        assertThat(testStudent.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testStudent.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testStudent.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testStudent.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testStudent.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testStudent.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testStudent.getUpdated()).isEqualTo(DEFAULT_UPDATED);
    }

    @Test
    @Transactional
    public void createStudentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();

        // Create the Student with an existing ID
        student.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentMockMvc.perform(post("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllStudents() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList
        restStudentMockMvc.perform(get("/api/students?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].schoolYear").value(hasItem(DEFAULT_SCHOOL_YEAR)))
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
    public void getStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get the student
        restStudentMockMvc.perform(get("/api/students/{id}", student.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(student.getId().intValue()))
            .andExpect(jsonPath("$.schoolYear").value(DEFAULT_SCHOOL_YEAR))
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
    public void getStudentsByIdFiltering() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        Long id = student.getId();

        defaultStudentShouldBeFound("id.equals=" + id);
        defaultStudentShouldNotBeFound("id.notEquals=" + id);

        defaultStudentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStudentShouldNotBeFound("id.greaterThan=" + id);

        defaultStudentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStudentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllStudentsBySchoolYearIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where schoolYear equals to DEFAULT_SCHOOL_YEAR
        defaultStudentShouldBeFound("schoolYear.equals=" + DEFAULT_SCHOOL_YEAR);

        // Get all the studentList where schoolYear equals to UPDATED_SCHOOL_YEAR
        defaultStudentShouldNotBeFound("schoolYear.equals=" + UPDATED_SCHOOL_YEAR);
    }

    @Test
    @Transactional
    public void getAllStudentsBySchoolYearIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where schoolYear not equals to DEFAULT_SCHOOL_YEAR
        defaultStudentShouldNotBeFound("schoolYear.notEquals=" + DEFAULT_SCHOOL_YEAR);

        // Get all the studentList where schoolYear not equals to UPDATED_SCHOOL_YEAR
        defaultStudentShouldBeFound("schoolYear.notEquals=" + UPDATED_SCHOOL_YEAR);
    }

    @Test
    @Transactional
    public void getAllStudentsBySchoolYearIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where schoolYear in DEFAULT_SCHOOL_YEAR or UPDATED_SCHOOL_YEAR
        defaultStudentShouldBeFound("schoolYear.in=" + DEFAULT_SCHOOL_YEAR + "," + UPDATED_SCHOOL_YEAR);

        // Get all the studentList where schoolYear equals to UPDATED_SCHOOL_YEAR
        defaultStudentShouldNotBeFound("schoolYear.in=" + UPDATED_SCHOOL_YEAR);
    }

    @Test
    @Transactional
    public void getAllStudentsBySchoolYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where schoolYear is not null
        defaultStudentShouldBeFound("schoolYear.specified=true");

        // Get all the studentList where schoolYear is null
        defaultStudentShouldNotBeFound("schoolYear.specified=false");
    }
                @Test
    @Transactional
    public void getAllStudentsBySchoolYearContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where schoolYear contains DEFAULT_SCHOOL_YEAR
        defaultStudentShouldBeFound("schoolYear.contains=" + DEFAULT_SCHOOL_YEAR);

        // Get all the studentList where schoolYear contains UPDATED_SCHOOL_YEAR
        defaultStudentShouldNotBeFound("schoolYear.contains=" + UPDATED_SCHOOL_YEAR);
    }

    @Test
    @Transactional
    public void getAllStudentsBySchoolYearNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where schoolYear does not contain DEFAULT_SCHOOL_YEAR
        defaultStudentShouldNotBeFound("schoolYear.doesNotContain=" + DEFAULT_SCHOOL_YEAR);

        // Get all the studentList where schoolYear does not contain UPDATED_SCHOOL_YEAR
        defaultStudentShouldBeFound("schoolYear.doesNotContain=" + UPDATED_SCHOOL_YEAR);
    }


    @Test
    @Transactional
    public void getAllStudentsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName equals to DEFAULT_FIRST_NAME
        defaultStudentShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the studentList where firstName equals to UPDATED_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName not equals to DEFAULT_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the studentList where firstName not equals to UPDATED_FIRST_NAME
        defaultStudentShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultStudentShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the studentList where firstName equals to UPDATED_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName is not null
        defaultStudentShouldBeFound("firstName.specified=true");

        // Get all the studentList where firstName is null
        defaultStudentShouldNotBeFound("firstName.specified=false");
    }
                @Test
    @Transactional
    public void getAllStudentsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName contains DEFAULT_FIRST_NAME
        defaultStudentShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the studentList where firstName contains UPDATED_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName does not contain DEFAULT_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the studentList where firstName does not contain UPDATED_FIRST_NAME
        defaultStudentShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }


    @Test
    @Transactional
    public void getAllStudentsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName equals to DEFAULT_LAST_NAME
        defaultStudentShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the studentList where lastName equals to UPDATED_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName not equals to DEFAULT_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the studentList where lastName not equals to UPDATED_LAST_NAME
        defaultStudentShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultStudentShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the studentList where lastName equals to UPDATED_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName is not null
        defaultStudentShouldBeFound("lastName.specified=true");

        // Get all the studentList where lastName is null
        defaultStudentShouldNotBeFound("lastName.specified=false");
    }
                @Test
    @Transactional
    public void getAllStudentsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName contains DEFAULT_LAST_NAME
        defaultStudentShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the studentList where lastName contains UPDATED_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName does not contain DEFAULT_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the studentList where lastName does not contain UPDATED_LAST_NAME
        defaultStudentShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }


    @Test
    @Transactional
    public void getAllStudentsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where email equals to DEFAULT_EMAIL
        defaultStudentShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the studentList where email equals to UPDATED_EMAIL
        defaultStudentShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllStudentsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where email not equals to DEFAULT_EMAIL
        defaultStudentShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the studentList where email not equals to UPDATED_EMAIL
        defaultStudentShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllStudentsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultStudentShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the studentList where email equals to UPDATED_EMAIL
        defaultStudentShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllStudentsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where email is not null
        defaultStudentShouldBeFound("email.specified=true");

        // Get all the studentList where email is null
        defaultStudentShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllStudentsByEmailContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where email contains DEFAULT_EMAIL
        defaultStudentShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the studentList where email contains UPDATED_EMAIL
        defaultStudentShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllStudentsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where email does not contain DEFAULT_EMAIL
        defaultStudentShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the studentList where email does not contain UPDATED_EMAIL
        defaultStudentShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllStudentsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where phone equals to DEFAULT_PHONE
        defaultStudentShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the studentList where phone equals to UPDATED_PHONE
        defaultStudentShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllStudentsByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where phone not equals to DEFAULT_PHONE
        defaultStudentShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the studentList where phone not equals to UPDATED_PHONE
        defaultStudentShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllStudentsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultStudentShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the studentList where phone equals to UPDATED_PHONE
        defaultStudentShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllStudentsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where phone is not null
        defaultStudentShouldBeFound("phone.specified=true");

        // Get all the studentList where phone is null
        defaultStudentShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllStudentsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where phone contains DEFAULT_PHONE
        defaultStudentShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the studentList where phone contains UPDATED_PHONE
        defaultStudentShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllStudentsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where phone does not contain DEFAULT_PHONE
        defaultStudentShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the studentList where phone does not contain UPDATED_PHONE
        defaultStudentShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllStudentsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where createdBy equals to DEFAULT_CREATED_BY
        defaultStudentShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the studentList where createdBy equals to UPDATED_CREATED_BY
        defaultStudentShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllStudentsByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where createdBy not equals to DEFAULT_CREATED_BY
        defaultStudentShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the studentList where createdBy not equals to UPDATED_CREATED_BY
        defaultStudentShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllStudentsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultStudentShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the studentList where createdBy equals to UPDATED_CREATED_BY
        defaultStudentShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllStudentsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where createdBy is not null
        defaultStudentShouldBeFound("createdBy.specified=true");

        // Get all the studentList where createdBy is null
        defaultStudentShouldNotBeFound("createdBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllStudentsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where createdBy contains DEFAULT_CREATED_BY
        defaultStudentShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the studentList where createdBy contains UPDATED_CREATED_BY
        defaultStudentShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllStudentsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where createdBy does not contain DEFAULT_CREATED_BY
        defaultStudentShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the studentList where createdBy does not contain UPDATED_CREATED_BY
        defaultStudentShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }


    @Test
    @Transactional
    public void getAllStudentsByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where created equals to DEFAULT_CREATED
        defaultStudentShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the studentList where created equals to UPDATED_CREATED
        defaultStudentShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllStudentsByCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where created not equals to DEFAULT_CREATED
        defaultStudentShouldNotBeFound("created.notEquals=" + DEFAULT_CREATED);

        // Get all the studentList where created not equals to UPDATED_CREATED
        defaultStudentShouldBeFound("created.notEquals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllStudentsByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultStudentShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the studentList where created equals to UPDATED_CREATED
        defaultStudentShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllStudentsByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where created is not null
        defaultStudentShouldBeFound("created.specified=true");

        // Get all the studentList where created is null
        defaultStudentShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudentsByUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where updated equals to DEFAULT_UPDATED
        defaultStudentShouldBeFound("updated.equals=" + DEFAULT_UPDATED);

        // Get all the studentList where updated equals to UPDATED_UPDATED
        defaultStudentShouldNotBeFound("updated.equals=" + UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void getAllStudentsByUpdatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where updated not equals to DEFAULT_UPDATED
        defaultStudentShouldNotBeFound("updated.notEquals=" + DEFAULT_UPDATED);

        // Get all the studentList where updated not equals to UPDATED_UPDATED
        defaultStudentShouldBeFound("updated.notEquals=" + UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void getAllStudentsByUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where updated in DEFAULT_UPDATED or UPDATED_UPDATED
        defaultStudentShouldBeFound("updated.in=" + DEFAULT_UPDATED + "," + UPDATED_UPDATED);

        // Get all the studentList where updated equals to UPDATED_UPDATED
        defaultStudentShouldNotBeFound("updated.in=" + UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void getAllStudentsByUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where updated is not null
        defaultStudentShouldBeFound("updated.specified=true");

        // Get all the studentList where updated is null
        defaultStudentShouldNotBeFound("updated.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudentsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        student.setUser(user);
        studentRepository.saveAndFlush(student);
        Long userId = user.getId();

        // Get all the studentList where user equals to userId
        defaultStudentShouldBeFound("userId.equals=" + userId);

        // Get all the studentList where user equals to userId + 1
        defaultStudentShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllStudentsByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        Parent parent = ParentResourceIT.createEntity(em);
        em.persist(parent);
        em.flush();
        student.setParent(parent);
        studentRepository.saveAndFlush(student);
        Long parentId = parent.getId();

        // Get all the studentList where parent equals to parentId
        defaultStudentShouldBeFound("parentId.equals=" + parentId);

        // Get all the studentList where parent equals to parentId + 1
        defaultStudentShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }


    @Test
    @Transactional
    public void getAllStudentsByTeacherIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        Teacher teacher = TeacherResourceIT.createEntity(em);
        em.persist(teacher);
        em.flush();
        student.setTeacher(teacher);
        studentRepository.saveAndFlush(student);
        Long teacherId = teacher.getId();

        // Get all the studentList where teacher equals to teacherId
        defaultStudentShouldBeFound("teacherId.equals=" + teacherId);

        // Get all the studentList where teacher equals to teacherId + 1
        defaultStudentShouldNotBeFound("teacherId.equals=" + (teacherId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStudentShouldBeFound(String filter) throws Exception {
        restStudentMockMvc.perform(get("/api/students?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].schoolYear").value(hasItem(DEFAULT_SCHOOL_YEAR)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())));

        // Check, that the count call also returns 1
        restStudentMockMvc.perform(get("/api/students/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStudentShouldNotBeFound(String filter) throws Exception {
        restStudentMockMvc.perform(get("/api/students?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStudentMockMvc.perform(get("/api/students/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingStudent() throws Exception {
        // Get the student
        restStudentMockMvc.perform(get("/api/students/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudent() throws Exception {
        // Initialize the database
        studentService.save(student);

        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Update the student
        Student updatedStudent = studentRepository.findById(student.getId()).get();
        // Disconnect from session so that the updates on updatedStudent are not directly saved in db
        em.detach(updatedStudent);
        updatedStudent
            .schoolYear(UPDATED_SCHOOL_YEAR)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .createdBy(UPDATED_CREATED_BY)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED);

        restStudentMockMvc.perform(put("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedStudent)))
            .andExpect(status().isOk());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getSchoolYear()).isEqualTo(UPDATED_SCHOOL_YEAR);
        assertThat(testStudent.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testStudent.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testStudent.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testStudent.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testStudent.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testStudent.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testStudent.getUpdated()).isEqualTo(UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void updateNonExistingStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentMockMvc.perform(put("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStudent() throws Exception {
        // Initialize the database
        studentService.save(student);

        int databaseSizeBeforeDelete = studentRepository.findAll().size();

        // Delete the student
        restStudentMockMvc.perform(delete("/api/students/{id}", student.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
