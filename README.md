# Medley-Relay-Swim: Concurrency
This project's objective was to correct an existing erroneous code and make it a correct, threads safe multithreaded Java simulation of a 4x100 medley relay swimming race. I was to use synchronization mechanisms to ensure that the simulation adheres to the specified synchronization constraints and maintains safety and liveness.


## Problem: 
- Swimmers were not swimming in order, atleast in a relay order, opposing at the game rules

## Objectives: 
- Start button initiates the simulation: Swimmers were entering the stadium before start button was pressed.
- Only one swimmer per grid block: Swimmers must not share the same grid position at the same time.
- Swimmers enter one at a time: Entrance and movement are synchronized to ensure proper race order and grid occupancy.
- Team relay mechanism: Each swimmer in the team can only start swimming after the previous teammate has completed their stroke.

## Project Structure
```bash
Medley-Relay-Swim/
│── src/medleySimulation/
│   ├── CounterDisplay.java
│   ├── FinishCounter.java
│   ├── GridBlock.java
│   ├── MedleySimulation.java  # Main class
│   ├── PeopleLocation.java
│   ├── StadiumGrid.java
│   ├── StadiumView.java
│   ├── Swimmer.java
│   ├── SwimTeam.java
│── bin/medleySimulation/ (Generated compiled files)
│── log.txt
│── makefile
│── README.md
```

## Requirements
- Java Development Kit (JDK) 8 or later
- make (for compiling and running via the Makefile)

## Installation
1. Ensure you have Java installed:
   ```bash
   java --version
   ```
2. Install make (if not installed). On windows, you can install it using Chocolatey
   ```bash
   choco install make
   ```
   On Linux/macOS:
   ```bash
   sudo apt install make  # Debian-based
   sudo yum install make  # RedHat-based
   ```

## Compilation and Execution
1. **Using Makefile**

To run the simulation, run
```bash
make run
```
