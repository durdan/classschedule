package com.dd.service.impl;

import com.dd.service.UserExtraService;
import com.dd.domain.UserExtra;
import com.dd.repository.UserExtraRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link UserExtra}.
 */
@Service
@Transactional
public class UserExtraServiceImpl implements UserExtraService {

    private final Logger log = LoggerFactory.getLogger(UserExtraServiceImpl.class);

    private final UserExtraRepository userExtraRepository;

    public UserExtraServiceImpl(UserExtraRepository userExtraRepository) {
        this.userExtraRepository = userExtraRepository;
    }

    @Override
    public UserExtra save(UserExtra userExtra) {
        log.debug("Request to save UserExtra : {}", userExtra);
        return userExtraRepository.save(userExtra);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserExtra> findAll(Pageable pageable) {
        log.debug("Request to get all UserExtras");
        return userExtraRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<UserExtra> findOne(Long id) {
        log.debug("Request to get UserExtra : {}", id);
        return userExtraRepository.findById(id);
    }

    @Override
    public Optional<UserExtra> findOneWithUserId(Long id) {
        log.debug("Request to get UserExtra by userId: {}", id);
        return userExtraRepository.findByUserId(id);

    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserExtra : {}", id);
        userExtraRepository.deleteById(id);
    }
}
