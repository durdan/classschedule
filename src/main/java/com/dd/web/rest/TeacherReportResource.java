package com.dd.web.rest;

import com.dd.domain.StudentReport;
import com.dd.domain.TeacherReport;
import com.dd.domain.UserExtra;
import com.dd.repository.StudentRepository;
import com.dd.repository.TeacherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link UserExtra}.
 */
@RestController
@RequestMapping("/api")
public class TeacherReportResource {

    private final Logger log = LoggerFactory.getLogger(TeacherReportResource.class);

    private static final String ENTITY_NAME = "teacherReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TeacherRepository teacherRepository;



    public TeacherReportResource(TeacherRepository teacherRepository) {


        this.teacherRepository = teacherRepository;
    }




    /**
     * {@code GET  /teacher-report/total-classes/:id} : get the "id" studentReport.
     *
     * @param id the id of the TeacherReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the TeacherReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/teacher-report/total-classes/{id}/{studentId}")
    public ResponseEntity<List<TeacherReport>> getTotalClassesGroupByStudent(@PathVariable("id") Long id, @PathVariable("studentId") Optional<String> studentId) {
        log.debug("REST request to get Total Classes for Teacher Report: {}", id);

         String   student="%"+studentId.get().trim()+"%";

        List<TeacherRepository.TReport> reportList =  teacherRepository.getTotalClassesGroupByStudent(id,student);
        List<TeacherReport> teacherReportList = new ArrayList<TeacherReport>();
        if(!reportList.isEmpty())
        {
            for (TeacherRepository.TReport report:reportList
            ) {
                TeacherReport teacherReport=new TeacherReport();
                teacherReport.setCount(report.getCount());
                teacherReport.setStudent(report.getFirstName()+" "+report.getLastName());
                teacherReportList.add(teacherReport);
            }

        }
        return ResponseEntity.ok().body(teacherReportList);
    }

    /**
     * {@code GET  /teacher-report/total-Confirmed/:id} : get the "id" studentReport.
     *
     * @param id the id of the TeacherReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the TeacherReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/teacher-report/total-Confirmed/{id}/{studentId}")
    public ResponseEntity<List<TeacherReport>> getTotalConfirmedClassesGroupByStudent(@PathVariable("id") Long id,@PathVariable("studentId") Optional<String> studentId) {
        log.debug("REST request to get Teacher Report Total Classes  : {}", id);
        String   student ="%"+studentId.get().trim()+"%";
        List<TeacherRepository.TReport> reportList = teacherRepository.getTotalConfirmedClassesGroupByStudent(id,student);
        List<TeacherReport> teacherReportList = new ArrayList<TeacherReport>();
        if(!reportList.isEmpty())
        {
            for (TeacherRepository.TReport report:reportList
            ) {
                TeacherReport teacherReport=new TeacherReport();
                teacherReport.setCount(report.getCount());
                teacherReport.setStudent(report.getFirstName()+" "+report.getLastName());
                teacherReportList.add(teacherReport);
            }

        }
        return ResponseEntity.ok().body(teacherReportList);
    }

    /**
     * {@code GET  /teacher-report/total-Confirmed-Paid/:id} : get the "id" studentReport.
     *
     * @param id the id of the TeacherReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the TeacherReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/teacher-report/total-Confirmed-Paid/{id}/{studentId}")
    public ResponseEntity<List<TeacherReport>> getTotalConfirmedAndPaidClassesGroupByStudent(@PathVariable("id") Long id,@PathVariable("studentId") Optional<String> studentId) {
        log.debug("REST request to get Total Classes : {}", id);
        String   student="%"+studentId.get().trim()+"%";
        List<TeacherRepository.TReport> reportList = teacherRepository.getTotalConfirmedAndPaidClassesGroupByStudent(id,student);
        List<TeacherReport> teacherReportList = new ArrayList<TeacherReport>();
        if(!reportList.isEmpty())
        {
            for (TeacherRepository.TReport report:reportList
            ) {
                TeacherReport teacherReport=new TeacherReport();
                teacherReport.setCount(report.getCount());
                teacherReport.setStudent(report.getFirstName()+" "+report.getLastName());
                teacherReportList.add(teacherReport);
            }

        }
        return ResponseEntity.ok().body(teacherReportList);
    }

    /**
     * {@code GET  /teacher-report/total-Confirmed-paid/:id} : get the "id" studentReport.
     *
     * @param id the id of the TeacherReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the TeacherReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/teacher-report/total-Confirmed-NotPaid/{id}/{studentId}")
    public ResponseEntity<List<TeacherReport>> getTotalConfirmedAndNotPaidClassesGroupByStudent(@PathVariable("id") Long id,@PathVariable("studentId") Optional<String> studentId) {
        log.debug("REST request to get Total Classes : {}", id);
        String   student="%"+studentId.get().trim()+"%";
        List<TeacherRepository.TReport> reportList = teacherRepository.getTotalConfirmedAndNotPaidClassesGroupByStudent(id,student);
        List<TeacherReport> teacherReportList = new ArrayList<TeacherReport>();
        if(!reportList.isEmpty())
        {
            for (TeacherRepository.TReport report:reportList
            ) {
                TeacherReport teacherReport=new TeacherReport();
                teacherReport.setCount(report.getCount());
                teacherReport.setStudent(report.getFirstName()+" "+report.getLastName());
                teacherReportList.add(teacherReport);
            }

        }
        return ResponseEntity.ok().body(teacherReportList);
    }

    /**
     * {@code GET  /teacher-report/total-UnConfirmed/:id} : get the "id" studentReport.
     *
     * @param id the id of the TeacherReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the TeacherReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/teacher-report/total-UnConfirmed/{id}/{studentId}")
    public ResponseEntity<List<TeacherReport>> getTotalUnConfirmedClassesGroupByStudent(@PathVariable("id") Long id, @PathVariable("studentId") Optional<String> studentId) {
        log.debug("REST request to get Total Classes : {}", id);
        String   student="%"+studentId.get().trim()+"%";
        List<TeacherRepository.TReport> reportList =   teacherRepository.getTotalUnConfirmedClassesGroupByStudent(id,student);
        List<TeacherReport> teacherReportList = new ArrayList<TeacherReport>();
        if(!reportList.isEmpty())
        {
            for (TeacherRepository.TReport report:reportList
            ) {
                TeacherReport teacherReport=new TeacherReport();
                teacherReport.setCount(report.getCount());
                teacherReport.setStudent(report.getFirstName()+" "+report.getLastName());
                teacherReportList.add(teacherReport);
            }

        }
        return ResponseEntity.ok().body(teacherReportList);
    }
}
