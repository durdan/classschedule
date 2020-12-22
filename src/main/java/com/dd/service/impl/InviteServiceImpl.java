package com.dd.service.impl;

import com.dd.service.InviteService;
import com.dd.domain.Invite;
import com.dd.repository.InviteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Invite}.
 */
@Service
@Transactional
public class InviteServiceImpl implements InviteService {

    private final Logger log = LoggerFactory.getLogger(InviteServiceImpl.class);

    private final InviteRepository inviteRepository;

    public InviteServiceImpl(InviteRepository inviteRepository) {
        this.inviteRepository = inviteRepository;
    }

    @Override
    public Invite save(Invite invite) {
        log.debug("Request to save Invite : {}", invite);
        return inviteRepository.save(invite);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Invite> findAll(Pageable pageable) {
        log.debug("Request to get all Invites");
        return inviteRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Invite> findOne(Long id) {
        log.debug("Request to get Invite : {}", id);
        return inviteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Invite : {}", id);
        inviteRepository.deleteById(id);
    }
}
