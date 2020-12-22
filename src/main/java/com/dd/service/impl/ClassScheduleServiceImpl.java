package com.dd.service.impl;

import com.dd.service.ClassScheduleService;
import com.dd.domain.ClassSchedule;
import com.dd.repository.ClassScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ClassSchedule}.
 */
@Service
@Transactional
public class ClassScheduleServiceImpl implements ClassScheduleService {

    private final Logger log = LoggerFactory.getLogger(ClassScheduleServiceImpl.class);

    private final ClassScheduleRepository classScheduleRepository;

    public ClassScheduleServiceImpl(ClassScheduleRepository classScheduleRepository) {
        this.classScheduleRepository = classScheduleRepository;
    }

    @Override
    public ClassSchedule save(ClassSchedule classSchedule) {
        log.debug("Request to save ClassSchedule : {}", classSchedule);
        return classScheduleRepository.save(classSchedule);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClassSchedule> findAll(Pageable pageable) {
        log.debug("Request to get all ClassSchedules");
        return classScheduleRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ClassSchedule> findOne(Long id) {
        log.debug("Request to get ClassSchedule : {}", id);
        return classScheduleRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ClassSchedule : {}", id);
        classScheduleRepository.deleteById(id);
    }
}
