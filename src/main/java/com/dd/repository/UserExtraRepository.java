package com.dd.repository;

import com.dd.domain.UserExtra;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the UserExtra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserExtraRepository extends JpaRepository<UserExtra, Long>, JpaSpecificationExecutor<UserExtra> {

   // @Query("SELECT ue FROM UserExtra ue WHERE ue.userId = :userId")
    @Query(
        value = "SELECT * FROM USER_EXTRA u WHERE u.user_id= ?1",
        nativeQuery = true)
    public Optional<UserExtra> findByUserId(Long userId);
}
