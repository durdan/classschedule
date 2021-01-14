package com.dd.web.rest;

import com.dd.domain.StudentReport;
import com.dd.domain.UserExtra;
import com.dd.repository.StudentRepository;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link UserExtra}.
 */
@RestController
@RequestMapping("/api")
public class StudentReportResource {

    private final Logger log = LoggerFactory.getLogger(StudentReportResource.class);

    private static final String ENTITY_NAME = "studentReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentRepository studentReportRepository;



    public StudentReportResource(StudentRepository studentReportRepository) {


        this.studentReportRepository = studentReportRepository;
    }




    /**
     * {@code GET  /student-report/total-classes/:id} : get the "id" studentReport.
     *
     * @param id the id of the StudentReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the StudentReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/student-report/total-classes/{id}/{teacherId}")
    public ResponseEntity<List<StudentReport>> getTotalClassesGroupByTeacher(@PathVariable("id") Long id,@PathVariable("teacherId") Optional<String> teacherId) {
        log.debug("REST request to get Total Classes : {}", id);

         String   teacher="%"+teacherId.get().trim()+"%";

        List<StudentRepository.Report> reportList = studentReportRepository.getTotalClassesGroupByTeacher(id,teacher);
        List<StudentReport> studentReportList = new ArrayList<StudentReport>();
        if(!reportList.isEmpty())
        {
            for (StudentRepository.Report report:reportList
            ) {
                StudentReport studentReport = new StudentReport();
                studentReport.setCount(report.getCount());
                studentReport.setTeacher(report.getEmail());
                studentReportList.add(studentReport);
            }

        }
        return ResponseEntity.ok().body(studentReportList);
    }

    /**
     * {@code GET  /student-report/total-Confirmed/:id} : get the "id" studentReport.
     *
     * @param id the id of the StudentReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the StudentReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/student-report/total-Confirmed/{id}/{teacherId}")
    public ResponseEntity<List<StudentReport>> getTotalConfirmedClassesGroupByTeacher(@PathVariable("id") Long id,@PathVariable("teacherId") Optional<String> teacherId) {
        log.debug("REST request to get Total Classes : {}", id);
        String   teacher="%"+teacherId.get().trim()+"%";
        List<StudentRepository.Report> reportList = studentReportRepository.getTotalConfirmedClassesGroupByTeacher(id,teacher);
        List<StudentReport> studentReportList = new ArrayList<StudentReport>();
        if(!reportList.isEmpty())
        {
            for (StudentRepository.Report report:reportList
                 ) {
                StudentReport studentReport = new StudentReport();
                studentReport.setCount(report.getCount());
                studentReport.setTeacher(report.getEmail());
                studentReportList.add(studentReport);
            }

        }
        return ResponseEntity.ok().body(studentReportList);
    }

    /**
     * {@code GET  /student-report/total-Confirmed-Paid/:id} : get the "id" studentReport.
     *
     * @param id the id of the StudentReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the StudentReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/student-report/total-Confirmed-Paid/{id}/{teacherId}")
    public ResponseEntity<List<StudentReport>> getTotalConfirmedAndPaidClassesGroupByTeacher(@PathVariable("id") Long id,@PathVariable("teacherId") Optional<String> teacherId) {
        log.debug("REST request to get Total Classes : {}", id);
        String   teacher="%"+teacherId.get().trim()+"%";
        List<StudentRepository.Report> reportList =   studentReportRepository.getTotalConfirmedAndPaidClassesGroupByTeacher(id,teacher);
        List<StudentReport> studentReportList = new ArrayList<StudentReport>();
        if(!reportList.isEmpty())
        {
            for (StudentRepository.Report report:reportList
            ) {
                StudentReport studentReport = new StudentReport();
                studentReport.setCount(report.getCount());
                studentReport.setTeacher(report.getEmail());
                studentReportList.add(studentReport);
            }

        }
        return ResponseEntity.ok().body(studentReportList);
    }

    /**
     * {@code GET  /student-report/total-Confirmed-paid/:id} : get the "id" studentReport.
     *
     * @param id the id of the StudentReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the StudentReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/student-report/total-Confirmed-NotPaid/{id}/{teacherId}")
    public ResponseEntity<List<StudentReport>> getTotalConfirmedAndNotPaidClassesGroupByTeacher(@PathVariable("id") Long id,@PathVariable("teacherId") Optional<String> teacherId) {
        log.debug("REST request to get Total Classes : {}", id);
        String   teacher="%"+teacherId.get().trim()+"%";
        List<StudentRepository.Report> reportList =   studentReportRepository.getTotalConfirmedAndNotPaidClassesGroupByTeacher(id,teacher);
        List<StudentReport> studentReportList = new ArrayList<StudentReport>();
        if(!reportList.isEmpty())
        {
            for (StudentRepository.Report report:reportList
            ) {
                StudentReport studentReport = new StudentReport();
                studentReport.setCount(report.getCount());
                studentReport.setTeacher(report.getEmail());
                studentReportList.add(studentReport);
            }

        }
        return ResponseEntity.ok().body(studentReportList);
    }

    /**
     * {@code GET  /student-report/total-UnConfirmed/:id} : get the "id" studentReport.
     *
     * @param id the id of the StudentReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the StudentReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/student-report/total-UnConfirmed/{id}/{teacherId}")
    public ResponseEntity<List<StudentReport>> getTotalUnConfirmedClassesGroupByTeacher(@PathVariable("id") Long id, @PathVariable("teacherId") Optional<String> teacherId) {
        log.debug("REST request to get Total Classes : {}", id);
        String   teacher="%"+teacherId.get().trim()+"%";
        List<StudentRepository.Report> reportList =   studentReportRepository.getTotalUnConfirmedClassesGroupByTeacher(id,teacher);
        List<StudentReport> studentReportList = new ArrayList<StudentReport>();
        if(!reportList.isEmpty())
        {
            for (StudentRepository.Report report:reportList
            ) {
                StudentReport studentReport = new StudentReport();
                studentReport.setCount(report.getCount());
                studentReport.setTeacher(report.getEmail());
                studentReportList.add(studentReport);
            }

        }
        return ResponseEntity.ok().body(studentReportList);
    }
}
