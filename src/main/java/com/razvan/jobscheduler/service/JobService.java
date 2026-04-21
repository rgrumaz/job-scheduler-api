package com.razvan.jobscheduler.service;

import com.razvan.jobscheduler.model.Job;
import com.razvan.jobscheduler.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

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