package com.dd.service.impl;

import com.dd.service.InviteStatusService;
import com.dd.domain.InviteStatus;
import com.dd.repository.InviteStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link InviteStatus}.
 */
@Service
@Transactional
public class InviteStatusServiceImpl implements InviteStatusService {

    private final Logger log = LoggerFactory.getLogger(InviteStatusServiceImpl.class);

    private final InviteStatusRepository inviteStatusRepository;

    public InviteStatusServiceImpl(InviteStatusRepository inviteStatusRepository) {
        this.inviteStatusRepository = inviteStatusRepository;
    }

    @Override
    public InviteStatus save(InviteStatus inviteStatus) {
        log.debug("Request to save InviteStatus : {}", inviteStatus);
        return inviteStatusRepository.save(inviteStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InviteStatus> findAll(Pageable pageable) {
        log.debug("Request to get all InviteStatuses");
        return inviteStatusRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<InviteStatus> findOne(Long id) {
        log.debug("Request to get InviteStatus : {}", id);
        return inviteStatusRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InviteStatus : {}", id);
        inviteStatusRepository.deleteById(id);
    }
}
