package com.dd.web.rest;

import com.dd.ClassscheduleApp;
import com.dd.domain.ClassSchedule;
import com.dd.domain.Student;
import com.dd.domain.Teacher;
import com.dd.domain.Parent;
import com.dd.domain.Course;
import com.dd.repository.ClassScheduleRepository;
import com.dd.service.ClassScheduleService;
import com.dd.service.dto.ClassScheduleCriteria;
import com.dd.service.ClassScheduleQueryService;

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
 * Integration tests for the {@link ClassScheduleResource} REST controller.
 */
@SpringBootTest(classes = ClassscheduleApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ClassScheduleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIRMED_BY_STUDENT = "AAAAAAAAAA";
    private static final String UPDATED_CONFIRMED_BY_STUDENT = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIRMED_BY_TEACHER = "AAAAAAAAAA";
    private static final String UPDATED_CONFIRMED_BY_TEACHER = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CONNECTED = false;
    private static final Boolean UPDATED_CONNECTED = true;

    @Autowired
    private ClassScheduleRepository classScheduleRepository;

    @Autowired
    private ClassScheduleService classScheduleService;

    @Autowired
    private ClassScheduleQueryService classScheduleQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassScheduleMockMvc;

    private ClassSchedule classSchedule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassSchedule createEntity(EntityManager em) {
        ClassSchedule classSchedule = new ClassSchedule()
            .name(DEFAULT_NAME)
            .created(DEFAULT_CREATED)
            .updated(DEFAULT_UPDATED)
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY)
            .confirmedByStudent(DEFAULT_CONFIRMED_BY_STUDENT)
            .confirmedByTeacher(DEFAULT_CONFIRMED_BY_TEACHER)
            .comment(DEFAULT_COMMENT)
            .connected(DEFAULT_CONNECTED);
        return classSchedule;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassSchedule createUpdatedEntity(EntityManager em) {
        ClassSchedule classSchedule = new ClassSchedule()
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY)
            .confirmedByStudent(UPDATED_CONFIRMED_BY_STUDENT)
            .confirmedByTeacher(UPDATED_CONFIRMED_BY_TEACHER)
            .comment(UPDATED_COMMENT)
            .connected(UPDATED_CONNECTED);
        return classSchedule;
    }

    @BeforeEach
    public void initTest() {
        classSchedule = createEntity(em);
    }

    @Test
    @Transactional
    public void createClassSchedule() throws Exception {
        int databaseSizeBeforeCreate = classScheduleRepository.findAll().size();
        // Create the ClassSchedule
        restClassScheduleMockMvc.perform(post("/api/class-schedules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classSchedule)))
            .andExpect(status().isCreated());

        // Validate the ClassSchedule in the database
        List<ClassSchedule> classScheduleList = classScheduleRepository.findAll();
        assertThat(classScheduleList).hasSize(databaseSizeBeforeCreate + 1);
        ClassSchedule testClassSchedule = classScheduleList.get(classScheduleList.size() - 1);
        assertThat(testClassSchedule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClassSchedule.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testClassSchedule.getUpdated()).isEqualTo(DEFAULT_UPDATED);
        assertThat(testClassSchedule.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testClassSchedule.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testClassSchedule.getConfirmedByStudent()).isEqualTo(DEFAULT_CONFIRMED_BY_STUDENT);
        assertThat(testClassSchedule.getConfirmedByTeacher()).isEqualTo(DEFAULT_CONFIRMED_BY_TEACHER);
        assertThat(testClassSchedule.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testClassSchedule.isConnected()).isEqualTo(DEFAULT_CONNECTED);
    }

    @Test
    @Transactional
    public void createClassScheduleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = classScheduleRepository.findAll().size();

        // Create the ClassSchedule with an existing ID
        classSchedule.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassScheduleMockMvc.perform(post("/api/class-schedules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classSchedule)))
            .andExpect(status().isBadRequest());

        // Validate the ClassSchedule in the database
        List<ClassSchedule> classScheduleList = classScheduleRepository.findAll();
        assertThat(classScheduleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllClassSchedules() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList
        restClassScheduleMockMvc.perform(get("/api/class-schedules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classSchedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].confirmedByStudent").value(hasItem(DEFAULT_CONFIRMED_BY_STUDENT)))
            .andExpect(jsonPath("$.[*].confirmedByTeacher").value(hasItem(DEFAULT_CONFIRMED_BY_TEACHER)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].connected").value(hasItem(DEFAULT_CONNECTED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getClassSchedule() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get the classSchedule
        restClassScheduleMockMvc.perform(get("/api/class-schedules/{id}", classSchedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classSchedule.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.updated").value(DEFAULT_UPDATED.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.confirmedByStudent").value(DEFAULT_CONFIRMED_BY_STUDENT))
            .andExpect(jsonPath("$.confirmedByTeacher").value(DEFAULT_CONFIRMED_BY_TEACHER))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.connected").value(DEFAULT_CONNECTED.booleanValue()));
    }


    @Test
    @Transactional
    public void getClassSchedulesByIdFiltering() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        Long id = classSchedule.getId();

        defaultClassScheduleShouldBeFound("id.equals=" + id);
        defaultClassScheduleShouldNotBeFound("id.notEquals=" + id);

        defaultClassScheduleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClassScheduleShouldNotBeFound("id.greaterThan=" + id);

        defaultClassScheduleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClassScheduleShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllClassSchedulesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where name equals to DEFAULT_NAME
        defaultClassScheduleShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the classScheduleList where name equals to UPDATED_NAME
        defaultClassScheduleShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where name not equals to DEFAULT_NAME
        defaultClassScheduleShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the classScheduleList where name not equals to UPDATED_NAME
        defaultClassScheduleShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where name in DEFAULT_NAME or UPDATED_NAME
        defaultClassScheduleShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the classScheduleList where name equals to UPDATED_NAME
        defaultClassScheduleShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where name is not null
        defaultClassScheduleShouldBeFound("name.specified=true");

        // Get all the classScheduleList where name is null
        defaultClassScheduleShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllClassSchedulesByNameContainsSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where name contains DEFAULT_NAME
        defaultClassScheduleShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the classScheduleList where name contains UPDATED_NAME
        defaultClassScheduleShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where name does not contain DEFAULT_NAME
        defaultClassScheduleShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the classScheduleList where name does not contain UPDATED_NAME
        defaultClassScheduleShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllClassSchedulesByCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where created equals to DEFAULT_CREATED
        defaultClassScheduleShouldBeFound("created.equals=" + DEFAULT_CREATED);

        // Get all the classScheduleList where created equals to UPDATED_CREATED
        defaultClassScheduleShouldNotBeFound("created.equals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByCreatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where created not equals to DEFAULT_CREATED
        defaultClassScheduleShouldNotBeFound("created.notEquals=" + DEFAULT_CREATED);

        // Get all the classScheduleList where created not equals to UPDATED_CREATED
        defaultClassScheduleShouldBeFound("created.notEquals=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where created in DEFAULT_CREATED or UPDATED_CREATED
        defaultClassScheduleShouldBeFound("created.in=" + DEFAULT_CREATED + "," + UPDATED_CREATED);

        // Get all the classScheduleList where created equals to UPDATED_CREATED
        defaultClassScheduleShouldNotBeFound("created.in=" + UPDATED_CREATED);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where created is not null
        defaultClassScheduleShouldBeFound("created.specified=true");

        // Get all the classScheduleList where created is null
        defaultClassScheduleShouldNotBeFound("created.specified=false");
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where updated equals to DEFAULT_UPDATED
        defaultClassScheduleShouldBeFound("updated.equals=" + DEFAULT_UPDATED);

        // Get all the classScheduleList where updated equals to UPDATED_UPDATED
        defaultClassScheduleShouldNotBeFound("updated.equals=" + UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByUpdatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where updated not equals to DEFAULT_UPDATED
        defaultClassScheduleShouldNotBeFound("updated.notEquals=" + DEFAULT_UPDATED);

        // Get all the classScheduleList where updated not equals to UPDATED_UPDATED
        defaultClassScheduleShouldBeFound("updated.notEquals=" + UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where updated in DEFAULT_UPDATED or UPDATED_UPDATED
        defaultClassScheduleShouldBeFound("updated.in=" + DEFAULT_UPDATED + "," + UPDATED_UPDATED);

        // Get all the classScheduleList where updated equals to UPDATED_UPDATED
        defaultClassScheduleShouldNotBeFound("updated.in=" + UPDATED_UPDATED);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where updated is not null
        defaultClassScheduleShouldBeFound("updated.specified=true");

        // Get all the classScheduleList where updated is null
        defaultClassScheduleShouldNotBeFound("updated.specified=false");
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where createdBy equals to DEFAULT_CREATED_BY
        defaultClassScheduleShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the classScheduleList where createdBy equals to UPDATED_CREATED_BY
        defaultClassScheduleShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where createdBy not equals to DEFAULT_CREATED_BY
        defaultClassScheduleShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the classScheduleList where createdBy not equals to UPDATED_CREATED_BY
        defaultClassScheduleShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultClassScheduleShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the classScheduleList where createdBy equals to UPDATED_CREATED_BY
        defaultClassScheduleShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where createdBy is not null
        defaultClassScheduleShouldBeFound("createdBy.specified=true");

        // Get all the classScheduleList where createdBy is null
        defaultClassScheduleShouldNotBeFound("createdBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllClassSchedulesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where createdBy contains DEFAULT_CREATED_BY
        defaultClassScheduleShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the classScheduleList where createdBy contains UPDATED_CREATED_BY
        defaultClassScheduleShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where createdBy does not contain DEFAULT_CREATED_BY
        defaultClassScheduleShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the classScheduleList where createdBy does not contain UPDATED_CREATED_BY
        defaultClassScheduleShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }


    @Test
    @Transactional
    public void getAllClassSchedulesByUpdatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where updatedBy equals to DEFAULT_UPDATED_BY
        defaultClassScheduleShouldBeFound("updatedBy.equals=" + DEFAULT_UPDATED_BY);

        // Get all the classScheduleList where updatedBy equals to UPDATED_UPDATED_BY
        defaultClassScheduleShouldNotBeFound("updatedBy.equals=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByUpdatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where updatedBy not equals to DEFAULT_UPDATED_BY
        defaultClassScheduleShouldNotBeFound("updatedBy.notEquals=" + DEFAULT_UPDATED_BY);

        // Get all the classScheduleList where updatedBy not equals to UPDATED_UPDATED_BY
        defaultClassScheduleShouldBeFound("updatedBy.notEquals=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByUpdatedByIsInShouldWork() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where updatedBy in DEFAULT_UPDATED_BY or UPDATED_UPDATED_BY
        defaultClassScheduleShouldBeFound("updatedBy.in=" + DEFAULT_UPDATED_BY + "," + UPDATED_UPDATED_BY);

        // Get all the classScheduleList where updatedBy equals to UPDATED_UPDATED_BY
        defaultClassScheduleShouldNotBeFound("updatedBy.in=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByUpdatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where updatedBy is not null
        defaultClassScheduleShouldBeFound("updatedBy.specified=true");

        // Get all the classScheduleList where updatedBy is null
        defaultClassScheduleShouldNotBeFound("updatedBy.specified=false");
    }
                @Test
    @Transactional
    public void getAllClassSchedulesByUpdatedByContainsSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where updatedBy contains DEFAULT_UPDATED_BY
        defaultClassScheduleShouldBeFound("updatedBy.contains=" + DEFAULT_UPDATED_BY);

        // Get all the classScheduleList where updatedBy contains UPDATED_UPDATED_BY
        defaultClassScheduleShouldNotBeFound("updatedBy.contains=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByUpdatedByNotContainsSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where updatedBy does not contain DEFAULT_UPDATED_BY
        defaultClassScheduleShouldNotBeFound("updatedBy.doesNotContain=" + DEFAULT_UPDATED_BY);

        // Get all the classScheduleList where updatedBy does not contain UPDATED_UPDATED_BY
        defaultClassScheduleShouldBeFound("updatedBy.doesNotContain=" + UPDATED_UPDATED_BY);
    }


    @Test
    @Transactional
    public void getAllClassSchedulesByConfirmedByStudentIsEqualToSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where confirmedByStudent equals to DEFAULT_CONFIRMED_BY_STUDENT
        defaultClassScheduleShouldBeFound("confirmedByStudent.equals=" + DEFAULT_CONFIRMED_BY_STUDENT);

        // Get all the classScheduleList where confirmedByStudent equals to UPDATED_CONFIRMED_BY_STUDENT
        defaultClassScheduleShouldNotBeFound("confirmedByStudent.equals=" + UPDATED_CONFIRMED_BY_STUDENT);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByConfirmedByStudentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where confirmedByStudent not equals to DEFAULT_CONFIRMED_BY_STUDENT
        defaultClassScheduleShouldNotBeFound("confirmedByStudent.notEquals=" + DEFAULT_CONFIRMED_BY_STUDENT);

        // Get all the classScheduleList where confirmedByStudent not equals to UPDATED_CONFIRMED_BY_STUDENT
        defaultClassScheduleShouldBeFound("confirmedByStudent.notEquals=" + UPDATED_CONFIRMED_BY_STUDENT);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByConfirmedByStudentIsInShouldWork() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where confirmedByStudent in DEFAULT_CONFIRMED_BY_STUDENT or UPDATED_CONFIRMED_BY_STUDENT
        defaultClassScheduleShouldBeFound("confirmedByStudent.in=" + DEFAULT_CONFIRMED_BY_STUDENT + "," + UPDATED_CONFIRMED_BY_STUDENT);

        // Get all the classScheduleList where confirmedByStudent equals to UPDATED_CONFIRMED_BY_STUDENT
        defaultClassScheduleShouldNotBeFound("confirmedByStudent.in=" + UPDATED_CONFIRMED_BY_STUDENT);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByConfirmedByStudentIsNullOrNotNull() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where confirmedByStudent is not null
        defaultClassScheduleShouldBeFound("confirmedByStudent.specified=true");

        // Get all the classScheduleList where confirmedByStudent is null
        defaultClassScheduleShouldNotBeFound("confirmedByStudent.specified=false");
    }
                @Test
    @Transactional
    public void getAllClassSchedulesByConfirmedByStudentContainsSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where confirmedByStudent contains DEFAULT_CONFIRMED_BY_STUDENT
        defaultClassScheduleShouldBeFound("confirmedByStudent.contains=" + DEFAULT_CONFIRMED_BY_STUDENT);

        // Get all the classScheduleList where confirmedByStudent contains UPDATED_CONFIRMED_BY_STUDENT
        defaultClassScheduleShouldNotBeFound("confirmedByStudent.contains=" + UPDATED_CONFIRMED_BY_STUDENT);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByConfirmedByStudentNotContainsSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where confirmedByStudent does not contain DEFAULT_CONFIRMED_BY_STUDENT
        defaultClassScheduleShouldNotBeFound("confirmedByStudent.doesNotContain=" + DEFAULT_CONFIRMED_BY_STUDENT);

        // Get all the classScheduleList where confirmedByStudent does not contain UPDATED_CONFIRMED_BY_STUDENT
        defaultClassScheduleShouldBeFound("confirmedByStudent.doesNotContain=" + UPDATED_CONFIRMED_BY_STUDENT);
    }


    @Test
    @Transactional
    public void getAllClassSchedulesByConfirmedByTeacherIsEqualToSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where confirmedByTeacher equals to DEFAULT_CONFIRMED_BY_TEACHER
        defaultClassScheduleShouldBeFound("confirmedByTeacher.equals=" + DEFAULT_CONFIRMED_BY_TEACHER);

        // Get all the classScheduleList where confirmedByTeacher equals to UPDATED_CONFIRMED_BY_TEACHER
        defaultClassScheduleShouldNotBeFound("confirmedByTeacher.equals=" + UPDATED_CONFIRMED_BY_TEACHER);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByConfirmedByTeacherIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where confirmedByTeacher not equals to DEFAULT_CONFIRMED_BY_TEACHER
        defaultClassScheduleShouldNotBeFound("confirmedByTeacher.notEquals=" + DEFAULT_CONFIRMED_BY_TEACHER);

        // Get all the classScheduleList where confirmedByTeacher not equals to UPDATED_CONFIRMED_BY_TEACHER
        defaultClassScheduleShouldBeFound("confirmedByTeacher.notEquals=" + UPDATED_CONFIRMED_BY_TEACHER);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByConfirmedByTeacherIsInShouldWork() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where confirmedByTeacher in DEFAULT_CONFIRMED_BY_TEACHER or UPDATED_CONFIRMED_BY_TEACHER
        defaultClassScheduleShouldBeFound("confirmedByTeacher.in=" + DEFAULT_CONFIRMED_BY_TEACHER + "," + UPDATED_CONFIRMED_BY_TEACHER);

        // Get all the classScheduleList where confirmedByTeacher equals to UPDATED_CONFIRMED_BY_TEACHER
        defaultClassScheduleShouldNotBeFound("confirmedByTeacher.in=" + UPDATED_CONFIRMED_BY_TEACHER);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByConfirmedByTeacherIsNullOrNotNull() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where confirmedByTeacher is not null
        defaultClassScheduleShouldBeFound("confirmedByTeacher.specified=true");

        // Get all the classScheduleList where confirmedByTeacher is null
        defaultClassScheduleShouldNotBeFound("confirmedByTeacher.specified=false");
    }
                @Test
    @Transactional
    public void getAllClassSchedulesByConfirmedByTeacherContainsSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where confirmedByTeacher contains DEFAULT_CONFIRMED_BY_TEACHER
        defaultClassScheduleShouldBeFound("confirmedByTeacher.contains=" + DEFAULT_CONFIRMED_BY_TEACHER);

        // Get all the classScheduleList where confirmedByTeacher contains UPDATED_CONFIRMED_BY_TEACHER
        defaultClassScheduleShouldNotBeFound("confirmedByTeacher.contains=" + UPDATED_CONFIRMED_BY_TEACHER);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByConfirmedByTeacherNotContainsSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where confirmedByTeacher does not contain DEFAULT_CONFIRMED_BY_TEACHER
        defaultClassScheduleShouldNotBeFound("confirmedByTeacher.doesNotContain=" + DEFAULT_CONFIRMED_BY_TEACHER);

        // Get all the classScheduleList where confirmedByTeacher does not contain UPDATED_CONFIRMED_BY_TEACHER
        defaultClassScheduleShouldBeFound("confirmedByTeacher.doesNotContain=" + UPDATED_CONFIRMED_BY_TEACHER);
    }


    @Test
    @Transactional
    public void getAllClassSchedulesByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where comment equals to DEFAULT_COMMENT
        defaultClassScheduleShouldBeFound("comment.equals=" + DEFAULT_COMMENT);

        // Get all the classScheduleList where comment equals to UPDATED_COMMENT
        defaultClassScheduleShouldNotBeFound("comment.equals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByCommentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where comment not equals to DEFAULT_COMMENT
        defaultClassScheduleShouldNotBeFound("comment.notEquals=" + DEFAULT_COMMENT);

        // Get all the classScheduleList where comment not equals to UPDATED_COMMENT
        defaultClassScheduleShouldBeFound("comment.notEquals=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByCommentIsInShouldWork() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where comment in DEFAULT_COMMENT or UPDATED_COMMENT
        defaultClassScheduleShouldBeFound("comment.in=" + DEFAULT_COMMENT + "," + UPDATED_COMMENT);

        // Get all the classScheduleList where comment equals to UPDATED_COMMENT
        defaultClassScheduleShouldNotBeFound("comment.in=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where comment is not null
        defaultClassScheduleShouldBeFound("comment.specified=true");

        // Get all the classScheduleList where comment is null
        defaultClassScheduleShouldNotBeFound("comment.specified=false");
    }
                @Test
    @Transactional
    public void getAllClassSchedulesByCommentContainsSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where comment contains DEFAULT_COMMENT
        defaultClassScheduleShouldBeFound("comment.contains=" + DEFAULT_COMMENT);

        // Get all the classScheduleList where comment contains UPDATED_COMMENT
        defaultClassScheduleShouldNotBeFound("comment.contains=" + UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByCommentNotContainsSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where comment does not contain DEFAULT_COMMENT
        defaultClassScheduleShouldNotBeFound("comment.doesNotContain=" + DEFAULT_COMMENT);

        // Get all the classScheduleList where comment does not contain UPDATED_COMMENT
        defaultClassScheduleShouldBeFound("comment.doesNotContain=" + UPDATED_COMMENT);
    }


    @Test
    @Transactional
    public void getAllClassSchedulesByConnectedIsEqualToSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where connected equals to DEFAULT_CONNECTED
        defaultClassScheduleShouldBeFound("connected.equals=" + DEFAULT_CONNECTED);

        // Get all the classScheduleList where connected equals to UPDATED_CONNECTED
        defaultClassScheduleShouldNotBeFound("connected.equals=" + UPDATED_CONNECTED);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByConnectedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where connected not equals to DEFAULT_CONNECTED
        defaultClassScheduleShouldNotBeFound("connected.notEquals=" + DEFAULT_CONNECTED);

        // Get all the classScheduleList where connected not equals to UPDATED_CONNECTED
        defaultClassScheduleShouldBeFound("connected.notEquals=" + UPDATED_CONNECTED);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByConnectedIsInShouldWork() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where connected in DEFAULT_CONNECTED or UPDATED_CONNECTED
        defaultClassScheduleShouldBeFound("connected.in=" + DEFAULT_CONNECTED + "," + UPDATED_CONNECTED);

        // Get all the classScheduleList where connected equals to UPDATED_CONNECTED
        defaultClassScheduleShouldNotBeFound("connected.in=" + UPDATED_CONNECTED);
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByConnectedIsNullOrNotNull() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);

        // Get all the classScheduleList where connected is not null
        defaultClassScheduleShouldBeFound("connected.specified=true");

        // Get all the classScheduleList where connected is null
        defaultClassScheduleShouldNotBeFound("connected.specified=false");
    }

    @Test
    @Transactional
    public void getAllClassSchedulesByStudentIsEqualToSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);
        Student student = StudentResourceIT.createEntity(em);
        em.persist(student);
        em.flush();
        classSchedule.setStudent(student);
        classScheduleRepository.saveAndFlush(classSchedule);
        Long studentId = student.getId();

        // Get all the classScheduleList where student equals to studentId
        defaultClassScheduleShouldBeFound("studentId.equals=" + studentId);

        // Get all the classScheduleList where student equals to studentId + 1
        defaultClassScheduleShouldNotBeFound("studentId.equals=" + (studentId + 1));
    }


    @Test
    @Transactional
    public void getAllClassSchedulesByTeacherIsEqualToSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);
        Teacher teacher = TeacherResourceIT.createEntity(em);
        em.persist(teacher);
        em.flush();
        classSchedule.setTeacher(teacher);
        classScheduleRepository.saveAndFlush(classSchedule);
        Long teacherId = teacher.getId();

        // Get all the classScheduleList where teacher equals to teacherId
        defaultClassScheduleShouldBeFound("teacherId.equals=" + teacherId);

        // Get all the classScheduleList where teacher equals to teacherId + 1
        defaultClassScheduleShouldNotBeFound("teacherId.equals=" + (teacherId + 1));
    }


    @Test
    @Transactional
    public void getAllClassSchedulesByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);
        Parent parent = ParentResourceIT.createEntity(em);
        em.persist(parent);
        em.flush();
        classSchedule.setParent(parent);
        classScheduleRepository.saveAndFlush(classSchedule);
        Long parentId = parent.getId();

        // Get all the classScheduleList where parent equals to parentId
        defaultClassScheduleShouldBeFound("parentId.equals=" + parentId);

        // Get all the classScheduleList where parent equals to parentId + 1
        defaultClassScheduleShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }


    @Test
    @Transactional
    public void getAllClassSchedulesByCourseIsEqualToSomething() throws Exception {
        // Initialize the database
        classScheduleRepository.saveAndFlush(classSchedule);
        Course course = CourseResourceIT.createEntity(em);
        em.persist(course);
        em.flush();
        classSchedule.setCourse(course);
        classScheduleRepository.saveAndFlush(classSchedule);
        Long courseId = course.getId();

        // Get all the classScheduleList where course equals to courseId
        defaultClassScheduleShouldBeFound("courseId.equals=" + courseId);

        // Get all the classScheduleList where course equals to courseId + 1
        defaultClassScheduleShouldNotBeFound("courseId.equals=" + (courseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClassScheduleShouldBeFound(String filter) throws Exception {
        restClassScheduleMockMvc.perform(get("/api/class-schedules?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classSchedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].updated").value(hasItem(DEFAULT_UPDATED.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].confirmedByStudent").value(hasItem(DEFAULT_CONFIRMED_BY_STUDENT)))
            .andExpect(jsonPath("$.[*].confirmedByTeacher").value(hasItem(DEFAULT_CONFIRMED_BY_TEACHER)))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].connected").value(hasItem(DEFAULT_CONNECTED.booleanValue())));

        // Check, that the count call also returns 1
        restClassScheduleMockMvc.perform(get("/api/class-schedules/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClassScheduleShouldNotBeFound(String filter) throws Exception {
        restClassScheduleMockMvc.perform(get("/api/class-schedules?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClassScheduleMockMvc.perform(get("/api/class-schedules/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingClassSchedule() throws Exception {
        // Get the classSchedule
        restClassScheduleMockMvc.perform(get("/api/class-schedules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassSchedule() throws Exception {
        // Initialize the database
        classScheduleService.save(classSchedule);

        int databaseSizeBeforeUpdate = classScheduleRepository.findAll().size();

        // Update the classSchedule
        ClassSchedule updatedClassSchedule = classScheduleRepository.findById(classSchedule.getId()).get();
        // Disconnect from session so that the updates on updatedClassSchedule are not directly saved in db
        em.detach(updatedClassSchedule);
        updatedClassSchedule
            .name(UPDATED_NAME)
            .created(UPDATED_CREATED)
            .updated(UPDATED_UPDATED)
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY)
            .confirmedByStudent(UPDATED_CONFIRMED_BY_STUDENT)
            .confirmedByTeacher(UPDATED_CONFIRMED_BY_TEACHER)
            .comment(UPDATED_COMMENT)
            .connected(UPDATED_CONNECTED);

        restClassScheduleMockMvc.perform(put("/api/class-schedules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedClassSchedule)))
            .andExpect(status().isOk());

        // Validate the ClassSchedule in the database
        List<ClassSchedule> classScheduleList = classScheduleRepository.findAll();
        assertThat(classScheduleList).hasSize(databaseSizeBeforeUpdate);
        ClassSchedule testClassSchedule = classScheduleList.get(classScheduleList.size() - 1);
        assertThat(testClassSchedule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClassSchedule.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testClassSchedule.getUpdated()).isEqualTo(UPDATED_UPDATED);
        assertThat(testClassSchedule.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testClassSchedule.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testClassSchedule.getConfirmedByStudent()).isEqualTo(UPDATED_CONFIRMED_BY_STUDENT);
        assertThat(testClassSchedule.getConfirmedByTeacher()).isEqualTo(UPDATED_CONFIRMED_BY_TEACHER);
        assertThat(testClassSchedule.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testClassSchedule.isConnected()).isEqualTo(UPDATED_CONNECTED);
    }

    @Test
    @Transactional
    public void updateNonExistingClassSchedule() throws Exception {
        int databaseSizeBeforeUpdate = classScheduleRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassScheduleMockMvc.perform(put("/api/class-schedules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classSchedule)))
            .andExpect(status().isBadRequest());

        // Validate the ClassSchedule in the database
        List<ClassSchedule> classScheduleList = classScheduleRepository.findAll();
        assertThat(classScheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClassSchedule() throws Exception {
        // Initialize the database
        classScheduleService.save(classSchedule);

        int databaseSizeBeforeDelete = classScheduleRepository.findAll().size();

        // Delete the classSchedule
        restClassScheduleMockMvc.perform(delete("/api/class-schedules/{id}", classSchedule.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClassSchedule> classScheduleList = classScheduleRepository.findAll();
        assertThat(classScheduleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
