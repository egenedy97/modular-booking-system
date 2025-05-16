# ✅ CI Workflow Checklist for Flight Search System

This checklist outlines the Continuous Integration (CI) tasks required for the project.

## 📦 Build & Test Pipeline

- [x] Checkout repository code
- [x] Set up Java 17 using `actions/setup-java`
- [x] Compile the project using Maven (`mvn clean compile`)
- [x] Run unit tests (`mvn test`)
- [x] Run code quality/lint checks (e.g., Checkstyle, PMD)
- [x] Package the application (`mvn package`)
- [x] Upload build artifacts to GitHub Actions

## 🔁 Trigger Conditions

- [x] On push to:
    - [x] `main`
    - [x] `develop`
    - [x]  feature branches
