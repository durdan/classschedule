package com.dd.repository;

import com.dd.domain.Student;
import com.dd.domain.Teacher;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Teacher entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {

    @Query(
        value = "SELECT * FROM TEACHER T WHERE T.email= ?1",
        nativeQuery = true)
    public Optional<Teacher> findByTeacherByEmail(String email);

    @Query(value="select count(c.id) as count ,s.first_Name as firstName ,s.last_Name as lastName from class_schedule c , student s  where c.teacher_id = ?1  and c.student_id=s.id and s.id\\:\\:TEXT like ?2 and c.schedule <= now() GROUP BY s.first_Name,s.last_Name", nativeQuery = true)
    List<TeacherRepository.TReport> getTotalClassesGroupByStudent(long id, String studentId);

    @Query(value="select count(c.id) as count,s.first_Name as firstName ,s.last_Name as lastName from class_schedule c, student s  where c.teacher_id = ?1 AND c.confirmed is true  and c.student_id=s.id and s.id\\:\\:TEXT like ?2 and c.schedule <= now() GROUP BY s.first_Name,s.last_Name", nativeQuery = true)
    List<TeacherRepository.TReport>  getTotalConfirmedClassesGroupByStudent(long id, String studentId);

    @Query(value="select count(c.id) as count,s.first_Name as firstName ,s.last_Name as lastName from class_schedule c, student s  where c.teacher_id = ?1 AND c.confirmed is true and payment is true  and c.student_id=s.id and s.id\\:\\:TEXT like ?2 and c.schedule <= now() GROUP BY s.first_Name,s.last_Name", nativeQuery = true)
    List<TeacherRepository.TReport>  getTotalConfirmedAndPaidClassesGroupByStudent(long id, String studentId);

    @Query(value="select count(c.id) as count,s.first_Name as firstName ,s.last_Name as lastName  from class_schedule c, student s  where c.teacher_id = ?1 AND c.confirmed is true and payment is false  and c.student_id=s.id and s.id\\:\\:TEXT like ?2 and c.schedule <= now() GROUP BY s.first_Name,s.last_Name", nativeQuery = true)
    List<TeacherRepository.TReport>  getTotalConfirmedAndNotPaidClassesGroupByStudent(long id, String studentId);

    @Query(value="select count(c.id) as count,s.first_Name as firstName ,s.last_Name as lastName from class_schedule c , student s  where c.teacher_id = ?1 AND c.confirmed is false  and c.student_id=s.id and s.id\\:\\:TEXT like ?2 and c.schedule <= now() GROUP BY s.first_Name,s.last_Name", nativeQuery = true)
    List<TeacherRepository.TReport>  getTotalUnConfirmedClassesGroupByStudent(long id, String studentId);

    public static interface TReport {

        Long getCount();

        String getFirstName();
        String getLastName();

    }
}
