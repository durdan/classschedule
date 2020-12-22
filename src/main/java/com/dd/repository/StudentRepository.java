package com.dd.repository;

import com.dd.domain.Student;

import com.dd.domain.UserExtra;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

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
}
