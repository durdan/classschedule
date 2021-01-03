package com.dd.service.impl;

import com.dd.service.RequestStatusService;
import com.dd.domain.RequestStatus;
import com.dd.repository.RequestStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link RequestStatus}.
 */
@Service
@Transactional
public class RequestStatusServiceImpl implements RequestStatusService {

    private final Logger log = LoggerFactory.getLogger(RequestStatusServiceImpl.class);

    private final RequestStatusRepository requestStatusRepository;

    public RequestStatusServiceImpl(RequestStatusRepository requestStatusRepository) {
        this.requestStatusRepository = requestStatusRepository;
    }

    @Override
    public RequestStatus save(RequestStatus requestStatus) {
        log.debug("Request to save RequestStatus : {}", requestStatus);
        return requestStatusRepository.save(requestStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RequestStatus> findAll(Pageable pageable) {
        log.debug("Request to get all RequestStatuses");
        return requestStatusRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<RequestStatus> findOne(Long id) {
        log.debug("Request to get RequestStatus : {}", id);
        return requestStatusRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RequestStatus : {}", id);
        requestStatusRepository.deleteById(id);
    }
}
