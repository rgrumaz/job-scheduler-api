package com.razvan.jobscheduler.controller;

import com.razvan.jobscheduler.model.Job;
import com.razvan.jobscheduler.repository.JobRepository;
import com.razvan.jobscheduler.service.JobService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService, JobRepository jobRepository) {
        this.jobService = jobService;
    }

    @GetMapping
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    @PostMapping
    public Job createJob(@RequestBody Job job) {
        return jobService.createJob(job);
    }

    @PutMapping("/{id}")
    public Job updateJob(@PathVariable Long id, @RequestBody Job job) {
        return  jobService.updateJob(job);
    }

    @PatchMapping("/{id}/completed")
    public Job completeJob(@PathVariable Long id) {
        return jobService.completeJob(id);
    }

    @DeleteMapping("/{id}")
    public void deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
    }

    @GetMapping("/prioritized")
    public List<Job> getPrioritizedJobs() {
        return jobService.getPrioritizedJobs();
    }
}