package com.dd.repository;

import com.dd.domain.Invite;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Invite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InviteRepository extends JpaRepository<Invite, Long>, JpaSpecificationExecutor<Invite> {
}
