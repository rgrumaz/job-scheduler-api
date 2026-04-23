package com.razvan.jobscheduler.service;

import com.razvan.jobscheduler.model.Job;
import com.razvan.jobscheduler.model.JobStatus;
import com.razvan.jobscheduler.repository.JobRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    public Job updateJob(Job job) {
        return jobRepository.save(job);
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.HOURS)
    public void updateDatabase(){
        List<Job> jobs = getAllJobs();
        jobs.forEach(job -> {
            if (job.isAccepted()) {
                job.setStatus(JobStatus.RUNNING);
                if (remainingDeadlineTime(job) < 0) {
                    job.setStatus(JobStatus.FAILED);
                }
            } else {
                job.setStatus(JobStatus.PENDING);
            }
        });
        jobRepository.saveAll(jobs);
    }

    public long remainingDeadlineTime(Job job) {
        LocalDateTime now = LocalDateTime.now();
        return ChronoUnit.HOURS.between(now, job.getDeadline());
    }

    public Job completeJob(Long id) {
        Job job = jobRepository.findById(id).orElseThrow();
        if (remainingDeadlineTime(job) < 0) {
            job.setStatus(JobStatus.LATE);
        } else {
            job.setStatus(JobStatus.DONE);
        }
        return jobRepository.save(job);
    }

    public Job postponeJob(Long id) {
        Job job = jobRepository.findById(id).orElseThrow();
        job.setAccepted(false);
        job.setStatus(JobStatus.POSTPONED);
        return jobRepository.save(job);
    }

    public Job acceptJob(Long id) {
        Job job = jobRepository.findById(id).orElseThrow();
        job.setAccepted(true);
        job.setStatus(JobStatus.RUNNING);
        return jobRepository.save(job);
    }

    public List<Job> getPrioritizedJobs(){
        List<Job> jobsList = jobRepository.findAll();
        TreeMap<Double, List<Job>> jobsPrioritized = new TreeMap<>(Collections.reverseOrder());

        jobsList.forEach(job -> {
            LocalDateTime now = LocalDateTime.now();
            long hoursUntilDeadLine = ChronoUnit.HOURS.between(now, job.getDeadline()) + 1;
            long daysUntilDeadLine = ChronoUnit.DAYS.between(now, job.getDeadline());
            double scoreDeadline = 0;

            if (daysUntilDeadLine > 5) {
                scoreDeadline = (1.0 / daysUntilDeadLine) * 2;
            } else if (hoursUntilDeadLine > 24) {
                scoreDeadline = (1.0 / daysUntilDeadLine) * 3;
            } else {
                scoreDeadline = (1.0 / hoursUntilDeadLine) * 6;
            }

            double score = job.getPriority() * 0.1 + scoreDeadline;
            if (jobsPrioritized.containsKey(score)) {
                jobsPrioritized.get(score).add(job);
            } else {
                List<Job> tempList = new ArrayList<>();
                tempList.add(job);
                jobsPrioritized.put(score, tempList);
            }

        });
        List<Job> result = new ArrayList<>();
        jobsPrioritized.values().forEach(result::addAll);
        return result;
    }
}