package com.dd.repository;

import com.dd.domain.Student;

import com.dd.domain.StudentReport;
import com.dd.domain.UserExtra;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Student entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    @Query(
        value = "SELECT * FROM STUDENT s WHERE s.email= ?1",
        nativeQuery = true)
    public Optional<Student> findByStudentByEmail(String email);

    @Query(value="select count(c.id) as count ,t.email as email from class_schedule c , teacher t  where c.student_id = ?1   and c.teacher_id=t.id and t.id\\:\\:TEXT like ?2 and c.schedule <= now() GROUP BY t.email", nativeQuery = true)
    List<Report> getTotalClassesGroupByTeacher(long id,String teacherId);

    @Query(value="select count(c.id) as count,t.email as email from class_schedule c , teacher t  where c.student_id = ?1 AND c.confirmed is true  and c.teacher_id=t.id and t.id\\:\\:TEXT like ?2 and c.schedule <= now() GROUP BY t.email", nativeQuery = true)
    List<Report>  getTotalConfirmedClassesGroupByTeacher(long id,String teacherId);

    @Query(value="select count(c.id) as count,t.email as email from class_schedule c , teacher t  where c.student_id = ?1 AND c.confirmed is true and payment is true  and c.teacher_id=t.id and t.id\\:\\:TEXT like ?2 and c.schedule <= now() GROUP BY t.email", nativeQuery = true)
    List<Report>  getTotalConfirmedAndPaidClassesGroupByTeacher(long id,String teacherId);

    @Query(value="select count(c.id) as count,t.email as email from class_schedule c , teacher t  where c.student_id = ?1 AND c.confirmed is true and payment is false  and c.teacher_id=t.id and t.id\\:\\:TEXT like ?2 and c.schedule <= now() GROUP BY t.email", nativeQuery = true)
    List<Report>  getTotalConfirmedAndNotPaidClassesGroupByTeacher(long id,String teacherId);

    @Query(value="select count(c.id) as count,t.email as email from class_schedule c join teacher t  where c.student_id = ?1 AND c.confirmed is false  and c.teacher_id=t.id and t.id\\:\\:TEXT like ?2 and c.schedule <= now() GROUP BY t.email", nativeQuery = true)
    List<Report>  getTotalUnConfirmedClassesGroupByTeacher(long id,String teacherId);

    public static interface Report {

        Long getCount();

        String getEmail();

    }
}
