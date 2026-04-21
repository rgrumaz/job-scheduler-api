package com.razvan.jobscheduler.repository;

import com.razvan.jobscheduler.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
    Long id(Long id);
}