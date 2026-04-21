# Job Scheduler API

A REST API for managing and automatically prioritizing jobs based on business importance and deadline urgency. Built with Spring Boot, JPA, and an in-memory H2 database.

## What it does

The core feature is a custom scheduling algorithm that calculates a priority score for each job by combining two factors:

- **Business priority** (manually set, 1-10)
- **Deadline urgency** (dynamically weighted, the closer the deadline the higher the score)

The weighting shifts depending on how much time is left:
- More than 5 days out: coarse scoring by days
- Under 5 days: finer scoring with increased weight
- Under 24 hours: highest multiplier kicks in

Jobs are returned sorted by score, highest first.

A background scheduler runs every hour and automatically updates job statuses based on whether they have been accepted and whether their deadline has passed.

## Tech stack

- Java 21
- Spring Boot 4.0.5
- Spring Data JPA + Hibernate
- H2 in-memory database
- Maven

## Getting started

Clone the repo and run it:

```bash
git clone https://github.com/rgrumaz/job-scheduler-api.git
cd job-scheduler-api
./mvnw spring-boot:run
```

The app starts on `http://localhost:8080`. On first run, 20 sample jobs are seeded automatically.

## Endpoints

### Get all jobs
```
GET /jobs
```

### Get jobs sorted by priority score
```
GET /jobs/prioritized
```

### Create a job
```
POST /jobs
Content-Type: application/json

{
  "title": "Server Migration",
  "description": "Migrate to new production server",
  "priority": 9,
  "deadline": "2026-04-22T10:00:00",
  "status": "PENDING",
  "accepted": false
}
```

### Update a job
```
PUT /jobs/{id}
Content-Type: application/json

{
  "id": 1,
  "title": "Server Migration",
  "description": "Migrate to new production server",
  "priority": 10,
  "deadline": "2026-04-22T10:00:00",
  "status": "RUNNING",
  "accepted": true
}
```

### Mark a job as complete
```
PATCH /jobs/{id}/completed
```

Returns `DONE` if submitted before the deadline, `LATE` if submitted after.

### Delete a job
```
DELETE /jobs/{id}
```

## Job lifecycle

Jobs move through the following statuses:

- `PENDING` - job has not been accepted yet
- `RUNNING` - job has been accepted and is in progress
- `DONE` - job was completed before the deadline
- `LATE` - job was submitted after the deadline
- `FAILED` - deadline passed without submission

The background scheduler checks all jobs every hour and updates statuses automatically. Jobs that are accepted flip to `RUNNING`. Jobs that are accepted but past their deadline flip to `FAILED`.

## Project structure

```
src/main/java/com/razvan/jobscheduler/
├── controller/    # HTTP endpoints
├── service/       # Business logic, scheduling algorithm and background scheduler
├── repository/    # Database access via JPA
├── model/         # Job entity and JobStatus enum
└── DataSeeder.java
```

## Notes

- H2 is in-memory, data resets on every restart (sample jobs are re-seeded automatically)
- The priority score is recalculated on every request to `/jobs/prioritized`, so it always reflects current deadlines
