# Project Portfolio: Hongyu Chen

## 1. Overview
**Equipment Master** is a desktop CLI inventory management system for University Lab Technicians. It replaces manual logbooks with a digital registry capable of tracking equipment against academic modules and forecasting procurement needs.

---

## 2. Summary of Contributions

### Code Contributed
[Repo Dashboard Link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=hongyu1231&breakdown=true)

### Enhancements Implemented
* **Module Tracking System:** Engineered a normalized entity structure to track course enrollment (`pax`). Implemented **Safe Dereferencing** in `delmod` to maintain data integrity when modules are removed.
* **Semantic Aging Report:** Designed a custom `AcademicSemester` class to perform time-difference calculations on university timelines (e.g., `AY24/25 Sem1`), enabling proactive equipment replacement audits.
* **Enhanced Find Feature:** Refactored search logic to cross-reference equipment names and module codes. Applied **SLAP** and eliminated deeply nested loops via early returns and helper methods.
* **Architecture Implementation:** System-wide implementation of the **Context Object Pattern** to facilitate clean dependency injection across all commands.

### Documentation Contributions
* **User Guide (UG):** Authored the *Introduction*, *Quick Start*, and functional instructions for *Module Tracking*, *Find*, and *Aging Reports*. Standardized command formats and provided practical usage examples.
* **Developer Guide (DG):** 
  * **Technical Architecture:** Documented logic for **Module Tracking**, **Enhanced Find**, and **Aging Reports** via PlantUML Class, Sequence (Update/Report), and Activity (Find algorithm) diagrams.
  * **Project Scope:** Drafted the *Target User Profile*, *User Stories*, and *Non-Functional Requirements*.
  * **Quality Assurance:** Authored the *Glossary* and *Manual Testing Instructions* to ensure system reliability and edge-case verification.

### Team-Based Tasks
* **Release Management:** Orchestrated GitHub Issue tracking and enforced **WEEKLY milestones**. Managed the end-to-end Release process (JAR packaging and tagging) for all iterations.
* **Quality Control:** Led overarching debugging efforts and maintained storage backward compatibility. Managed the final compilation and formatting of PDF deliverables (UG/DG).

### Review / Mentoring Contributions
* **Gatekeeping:** Conducted rigorous code reviews focusing on **SLAP**, **Checkstyle**, and preventing regression bugs in storage logic.
* **Technical Mentorship:** Guided the team through major refactoring phases (dependency injection) and mentored peers on effective JUnit testing and IDE debugging.