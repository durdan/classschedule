package com.dd.service.impl;

import com.dd.service.RequestTaskService;
import com.dd.domain.RequestTask;
import com.dd.repository.RequestTaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link RequestTask}.
 */
@Service
@Transactional
public class RequestTaskServiceImpl implements RequestTaskService {

    private final Logger log = LoggerFactory.getLogger(RequestTaskServiceImpl.class);

    private final RequestTaskRepository requestTaskRepository;

    public RequestTaskServiceImpl(RequestTaskRepository requestTaskRepository) {
        this.requestTaskRepository = requestTaskRepository;
    }

    @Override
    public RequestTask save(RequestTask requestTask) {
        log.debug("Request to save RequestTask : {}", requestTask);
        return requestTaskRepository.save(requestTask);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RequestTask> findAll(Pageable pageable) {
        log.debug("Request to get all RequestTasks");
        return requestTaskRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<RequestTask> findOne(Long id) {
        log.debug("Request to get RequestTask : {}", id);
        return requestTaskRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RequestTask : {}", id);
        requestTaskRepository.deleteById(id);
    }
}
