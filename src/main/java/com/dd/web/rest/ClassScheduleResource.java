package com.dd.web.rest;

import com.dd.domain.ClassSchedule;
import com.dd.service.ClassScheduleService;
import com.dd.web.rest.errors.BadRequestAlertException;
import com.dd.service.dto.ClassScheduleCriteria;
import com.dd.service.ClassScheduleQueryService;

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
import java.time.*;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.dd.domain.ClassSchedule}.
 */
@RestController
@RequestMapping("/api")
public class ClassScheduleResource {

    private final Logger log = LoggerFactory.getLogger(ClassScheduleResource.class);

    private static final String ENTITY_NAME = "classSchedule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassScheduleService classScheduleService;

    private final ClassScheduleQueryService classScheduleQueryService;

    public ClassScheduleResource(ClassScheduleService classScheduleService, ClassScheduleQueryService classScheduleQueryService) {
        this.classScheduleService = classScheduleService;
        this.classScheduleQueryService = classScheduleQueryService;
    }

    /**
     * {@code POST  /class-schedules} : Create a new classSchedule.
     *
     * @param classSchedule the classSchedule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classSchedule, or with status {@code 400 (Bad Request)} if the classSchedule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/class-schedules")
    public ResponseEntity<ClassSchedule> createClassSchedule(@RequestBody ClassSchedule classSchedule) throws URISyntaxException {
        log.debug("REST request to save ClassSchedule : {}", classSchedule);
        if (classSchedule.getId() != null) {
            throw new BadRequestAlertException("A new classSchedule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClassSchedule result=null;
        if (classSchedule.getReoccurringType()!=null){
            ZonedDateTime d = ZonedDateTime.parse(classSchedule.getSchedule().toString());
           List<LocalDateTime> localDates= getWeeklyMeetingDates(d.toLocalDateTime(),26,classSchedule.getReoccurringType());
            for (LocalDateTime localDateTime : localDates){
                ClassSchedule classSchedule1 = new ClassSchedule();
                classSchedule1 = classSchedule;
//                classSchedule1.setComment(classSchedule.getComment());
//                classSchedule1.setConfirmedByStudent(classSchedule.getConfirmedByStudent());
//                classSchedule1.setConfirmedByTeacher(classSchedule.getConfirmedByTeacher());
//                classSchedule1.setConnected(classSchedule.isConnected());
//                classSchedule1.setCourse(classSchedule.getCourse());
//                classSchedule1.setParent(classSchedule.getParent());
//                classSchedule1.setStudent(classSchedule.getStudent());
//                classSchedule1.setTeacher(classSchedule.getTeacher());
//                classSchedule1.setCreated(classSchedule.getCreated());
//                classSchedule1.setCreatedBy(classSchedule.getCreatedBy());
//                classSchedule1.setPayment(classSchedule.isPayment());
//                classSchedule1.setReoccurring(classSchedule.isReoccurring());
//                clas
                classSchedule1.setSchedule(localDateTime.toInstant(ZoneOffset.UTC));
                classSchedule1.setId(null);
                result = classScheduleService.save(classSchedule1);
            }
        }else{
              result = classScheduleService.save(classSchedule);
        }



        return ResponseEntity.created(new URI("/api/class-schedules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /class-schedules} : Updates an existing classSchedule.
     *
     * @param classSchedule the classSchedule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classSchedule,
     * or with status {@code 400 (Bad Request)} if the classSchedule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classSchedule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/class-schedules")
    public ResponseEntity<ClassSchedule> updateClassSchedule(@RequestBody ClassSchedule classSchedule) throws URISyntaxException {
        log.debug("REST request to update ClassSchedule : {}", classSchedule);
        if (classSchedule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClassSchedule result = classScheduleService.save(classSchedule);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classSchedule.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /class-schedules} : get all the classSchedules.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classSchedules in body.
     */
    @GetMapping("/class-schedules")
    public ResponseEntity<List<ClassSchedule>> getAllClassSchedules(ClassScheduleCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ClassSchedules by criteria: {}", criteria);
        Page<ClassSchedule> page = classScheduleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /class-schedules/count} : count all the classSchedules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/class-schedules/count")
    public ResponseEntity<Long> countClassSchedules(ClassScheduleCriteria criteria) {
        log.debug("REST request to count ClassSchedules by criteria: {}", criteria);
        return ResponseEntity.ok().body(classScheduleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /class-schedules/:id} : get the "id" classSchedule.
     *
     * @param id the id of the classSchedule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classSchedule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/class-schedules/{id}")
    public ResponseEntity<ClassSchedule> getClassSchedule(@PathVariable Long id) {
        log.debug("REST request to get ClassSchedule : {}", id);
        Optional<ClassSchedule> classSchedule = classScheduleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(classSchedule);
    }

    /**
     * {@code DELETE  /class-schedules/:id} : delete the "id" classSchedule.
     *
     * @param id the id of the classSchedule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/class-schedules/{id}")
    public ResponseEntity<Void> deleteClassSchedule(@PathVariable Long id) {
        log.debug("REST request to delete ClassSchedule : {}", id);
        classScheduleService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    private  List<LocalDateTime>
    getWeeklyMeetingDates(LocalDateTime localDate, int count, String reoccuringtype)
    {
        //Custom temporal adjuster with lambda
        TemporalAdjuster temporalAdjuster = t -> t.plus(Period.ofDays(7));
        List<LocalDateTime> dates = new ArrayList<>();
        for(int i = 0; i < count; i++)
        {
            if(reoccuringtype.equals("1")){
                localDate = localDate
                    .with(TemporalAdjusters.next(localDate.getDayOfWeek()));

                dates.add(localDate);
                log.info("Weekly {}",localDate);

            }
            if(reoccuringtype.equals("2")){
                localDate = localDate
                    .with(TemporalAdjusters.next(localDate.getDayOfWeek()));
                localDate= localDate.plusWeeks( 1 );

                dates.add(localDate);
                log.info("BI-WEEKLY{}",localDate);

            }




            //log.info("MONTHLY {}",localDate.with(TemporalAdjusters.firstInMonth(0,localDate.getDayOfWeek())));
        }
        return dates;
    }
}
