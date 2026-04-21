package com.razvan.jobscheduler;

import com.razvan.jobscheduler.model.Job;
import com.razvan.jobscheduler.model.JobStatus;
import com.razvan.jobscheduler.repository.JobRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final JobRepository jobRepository;

    public DataSeeder(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public void run(String... args) {
        if (jobRepository.count() == 0) {
            jobRepository.saveAll(List.of(
                createJob("Server Migration", "Komplette Migration auf neuen Server", 9, LocalDateTime.now().plusHours(18), JobStatus.PENDING),
                createJob("Security Patch", "Kritisches Sicherheitsupdate einspielen", 8, LocalDateTime.now().plusHours(20), JobStatus.PENDING),
                createJob("SSL Certificate Renewal", "SSL-Zertifikat erneuern vor Ablauf", 7, LocalDateTime.now().plusDays(1), JobStatus.PENDING),
                createJob("Database Backup", "Vollständiges Datenbank-Backup erstellen", 6, LocalDateTime.now().plusDays(2), JobStatus.PENDING),
                createJob("API Rate Limit Fix", "Rate-Limiting Bug in Production beheben", 8, LocalDateTime.now().plusDays(2), JobStatus.PENDING),
                createJob("Log Rotation Setup", "Automatische Log-Rotation konfigurieren", 4, LocalDateTime.now().plusDays(4), JobStatus.PENDING),
                createJob("Load Balancer Config", "Load Balancer für neuen Service konfigurieren", 7, LocalDateTime.now().plusDays(5), JobStatus.PENDING),
                createJob("Memory Leak Investigation", "Memory Leak im Auth-Service analysieren", 9, LocalDateTime.now().plusDays(5), JobStatus.PENDING),
                createJob("CI/CD Pipeline Update", "GitHub Actions Pipeline aktualisieren", 5, LocalDateTime.now().plusDays(7), JobStatus.PENDING),
                createJob("Docker Image Cleanup", "Alte Docker Images aus Registry löschen", 3, LocalDateTime.now().plusDays(8), JobStatus.PENDING),
                createJob("Monitoring Dashboard", "Grafana Dashboard für neue Metriken erstellen", 5, LocalDateTime.now().plusDays(10), JobStatus.PENDING),
                createJob("User Auth Refactor", "JWT Auth Logik refactoren", 6, LocalDateTime.now().plusDays(12), JobStatus.PENDING),
                createJob("API Documentation", "Swagger Dokumentation vervollständigen", 4, LocalDateTime.now().plusDays(14), JobStatus.PENDING),
                createJob("Database Index Optimization", "Langsame Queries durch Indizes optimieren", 7, LocalDateTime.now().plusDays(15), JobStatus.PENDING),
                createJob("Firewall Rules Review", "Firewall Regeln überprüfen und bereinigen", 6, LocalDateTime.now().plusDays(18), JobStatus.PENDING),
                createJob("Dependency Updates", "NPM und Maven Dependencies updaten", 3, LocalDateTime.now().plusDays(20), JobStatus.PENDING),
                createJob("Backup Restore Test", "Disaster Recovery Prozess testen", 5, LocalDateTime.now().plusDays(22), JobStatus.PENDING),
                createJob("Code Review Automation", "SonarQube in Pipeline integrieren", 4, LocalDateTime.now().plusDays(25), JobStatus.PENDING),
                createJob("Storage Cleanup", "Alte Backups und Temp-Dateien löschen", 2, LocalDateTime.now().plusDays(28), JobStatus.PENDING),
                createJob("Quarterly Security Audit", "Vierteljährliches Security-Audit durchführen", 8, LocalDateTime.now().plusDays(30), JobStatus.PENDING)
            ));
        }
    }

    private Job createJob(String title, String description, int priority, LocalDateTime deadline, JobStatus status) {
        Job job = new Job();
        job.setTitle(title);
        job.setDescription(description);
        job.setPriority(priority);
        job.setDeadline(deadline);
        job.setStatus(status);
        return job;
    }
}
