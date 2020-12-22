package com.dd.repository;

import com.dd.domain.InviteStatus;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the InviteStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InviteStatusRepository extends JpaRepository<InviteStatus, Long>, JpaSpecificationExecutor<InviteStatus> {
}
