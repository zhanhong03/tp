# Project Portfolio: Hongyu Chen

## 1. Overview
**Equipment Master** is a desktop CLI inventory management system optimized for University Lab Technicians. It replaces error-prone manual logbooks with a relational digital registry, enabling technicians to track hardware against academic modules, monitor lifecycles, and algorithmically forecast procurement needs.

---

## 2. Summary of Contributions

### Code Contributed
[RepoSense Dashboard Link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=hongyu1231&breakdown=true)

### Enhancements Implemented
* **Module Tracking & Relational Mapping:** Engineered a normalized entity structure linking `Equipment` to `Module` requirements. Implemented **Safe Dereferencing** to prevent "ghost references" and maintain data integrity during module deletions.
* **Semantic Time Calculation (Aging Report):** Designed an `AcademicSemester` normalization algorithm to perform floating-point mathematical calculations on non-standard university timelines (e.g., `AY24/25 Sem1`), enabling proactive lifespan audits.
* **Algorithmic Search Optimization (Find):** Refactored search mechanics to cross-reference both item names and module codes. Enforced the **Single Level of Abstraction Principle (SLAP)**, utilizing early returns to eliminate deeply nested iterations.
* **Architectural Refactoring:** System-wide integration of the **Context Object Pattern**, decoupling the logic component from storage and UI, which significantly improved testability via dependency injection.

### Documentation Contributions
* **User Guide (UG):** Authored the *Introduction*, *Quick Start*, *FAQ*, and core feature tutorials (*Module Tracking, Find, Aging Reports*).
  * Enhanced UX by designing the *Error Handling* section and integrating **responsive ASCII UI screenshots** to visualize command outputs.
  * Formulated the *Command Summary (Cheat Sheet)* and established strict, idiot-proof syntax guidelines (`[ ]` vs `UPPER_CASE`).
* **Developer Guide (DG):** Authored the technical deep-dives and integrated complex **PlantUML diagrams** for *Module Tracking*, *Enhanced Find*, and *Aging Reports*.
  * **Project Scope & Requirements:** Wrote the *Target User Profile*, *Prioritized User Stories*, detailed **Use Cases (MSS & Extensions)**, and *Manual Testing Instructions*.
  * **Project Effort:** Drafted the *Appendix* to explicitly highlight the team's technical challenges (Relational Mapping, Forecasting) for grading justification.

### Team-Based Tasks & Project Management
* **Release Management:** Managed the end-to-end release cycle (JAR packaging and GitHub releases) across all iterations.
* **Workflow Coordination:** Enforced weekly milestones via GitHub Issues and coordinated integration testing to maintain Storage backward compatibility.
* **Deliverable Quality:** Managed the final typesetting, Markdown standardization, and dynamic TOC generation for all PDF deliverables (UG/DG).

### Review & Mentoring Contributions
* **Code Gatekeeping:** Acted as a primary reviewer for PRs, rigorously enforcing Checkstyle compliance, Defensive Programming standards, and proper Exception handling.
* **Technical Mentorship:** Guided the team through complex refactoring phases (e.g., transitioning to the Command Factory pattern) and mentored peers on effective JUnit 5 testing strategies.