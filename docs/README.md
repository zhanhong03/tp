# Equipment Master

**Equipment Master** is a high-performance **Command Line Interface (CLI)** application specifically designed for **Laboratory Technicians** and **Teaching Assistants** at the National University of Singapore (NUS). It streamlines the management of engineering assets, tracks module-specific equipment requirements, and automates procurement workflows.

---

## Key Features

* **Centralized Inventory**: Track quantity, loan status, and availability of lab assets (e.g., STM32 boards, Oscilloscopes, Resistors).
* **Module-Based Tagging**: Link equipment to specific NUS modules (e.g., CG2111A, EE2026) to calculate required quantities based on student enrollment.
* **Smart Procurement Reports**:
    * **Low Stock Alert**: Identify items falling below safety thresholds.
    * **Aging Report**: Track equipment lifespan and identify units nearing retirement.
* **Safety Buffer Management**: Set custom buffers for critical items to handle unexpected demand during peak lab seasons.
* **Data Persistence**: Automatically saves your laboratory state to local storage, ensuring no data is lost between sessions.

---

## Preview

```text
Welcome to Equipment Master! How can I help you today?
    ______                             __  ___           __
   / ____/___  __  ______  ____  ___  /  |/  /___ ______/ /____  _____
  / __/ / __ `/ / / / __ \/ __ \/ _ \/ /|_/ / __ `/ ___/ __/ _ \/ ___/
 / /___/ /_/ / /_/ / /_/ / /_/ /  __/ /  / / /_/ (__  ) /_/  __/ /
/_____/\__, /\__,_/ .___/ .___/\___/\__/  /_/  /_/\__,_/____/\__/
      /____/     /_/   /_/
```

---

## Quick Start

1.  **Prerequisites**: Ensure you have **Java 17** or above installed on your system.
2.  **Download**: Get the latest version of `EquipmentMaster.jar` from our [Releases](https://github.com/AY2526S2-CS2113-F09-3/tp/releases) page.
3.  **Run**: Open your terminal/command prompt, navigate to the folder containing the file, and run:
    ```bash
    java -jar EquipmentMaster.jar
    ```
4.  **Explore**: Type `help` in the application to see a full list of available commands.

---

## Documentation

* **[User Guide](UserGuide.md)**: Detailed instructions on how to use every command.
* **[Developer Guide](DeveloperGuide.md)**: Deep dive into the architecture, design patterns, and implementation details.
* **[About Us](AboutUs.md)**: Meet the development team behind Equipment Master.

---

## Contributing

Equipment Master is an open-source project developed as part of the CS2113 Software Engineering module at the National University of Singapore. We welcome feedback and bug reports via GitHub Issues.
