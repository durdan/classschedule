package com.dd.service.impl;

import com.dd.service.ParentService;
import com.dd.domain.Parent;
import com.dd.repository.ParentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Parent}.
 */
@Service
@Transactional
public class ParentServiceImpl implements ParentService {

    private final Logger log = LoggerFactory.getLogger(ParentServiceImpl.class);

    private final ParentRepository parentRepository;

    public ParentServiceImpl(ParentRepository parentRepository) {
        this.parentRepository = parentRepository;
    }

    @Override
    public Parent save(Parent parent) {
        log.debug("Request to save Parent : {}", parent);
        return parentRepository.save(parent);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Parent> findAll(Pageable pageable) {
        log.debug("Request to get all Parents");
        return parentRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Parent> findOne(Long id) {
        log.debug("Request to get Parent : {}", id);
        return parentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Parent : {}", id);
        parentRepository.deleteById(id);
    }
}
