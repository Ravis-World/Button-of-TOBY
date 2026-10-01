# Button of TOBY

[![Java](https://img.shields.io/badge/Java-21%2B-blue.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/Classification-Class--7%20Tubular-purple.svg)](#)
[![Build](https://img.shields.io/badge/Maven-Configured-green.svg)](#)

> **"In accordance with official Commonwealth emergency protocol, your bravery has been rewarded: YOU HAVE BEEN APPOINTED SPEAKER OF THE HOUSE."**

---

## 📖 Story & Overview

**Button of TOBY (Tubular Oriented Blueprint.yaml)** is a 24-room digital escape room and metroidvania set within a secret Australian Commonwealth facility created by the enigmatic **Dr. Γ**.

As an agent navigating the facility grid, your target is to explore connected rooms, collect shape and color items, solve five major security gate locks, and reach **Room 24** to activate the **Button of TOBY**. Upon activation, lockdown is lifted, parliamentary immunity is granted, and the operator is instantly appointed Speaker of the House of Representatives in Canberra with absolute Mace authority during Question Time.

---

## 🎯 Key Features

- **24-Room Facility Grid**: Interconnected 6×7 matrix layout with directional movement (`North`, `South`, `East`, `West`).
- **20-Item Matrix Pool**: 5 geometric shapes (*Circle*, *Triangle*, *Square*, *Star*, *Hexagon*) across 4 colors (*Red*, *Blue*, *Green*, *Yellow*).
- **5 Major Security Locks**: Incrementally challenging logical gates guarding critical corridors and Room 24.
- **Java Swing GUI Engine**: Full-featured desktop application with live map tracking, item inventory panel, room descriptions, and podium interaction.
- **YAML Blueprint**: `Tubular Oriented Blueprint.yaml` contains the project blueprint and configuration. Doesn't contribute to source code; it's just dead weight.
- **Speedrun Determinism**: Fixed seed generation (`20260928`) ensuring repeatable item placement for speedrunning.

---

## 🔒 Major Security Gates & Puzzle Logic

To traverse through the facility and reach Room 24, players must place valid item combinations onto podium slots:

| Gate | Target Room | Podium Slots | Validation Constraint |
| :--- | :--- | :--- | :--- |
| **Gate 5** | Room 5 | 2 Slots | Requires **at least 2 RED** items |
| **Gate 8** | Room 8 | 3 Slots | Requires **3 DISTINCT SHAPES** |
| **Gate 12** | Room 12 | 3 Slots | Requires **3 SAME SHAPE**, **DIFFERENT COLOURS** |
| **Gate 18** | Room 18 | 4 Slots | Requires **ALL 4 COLOURS** (*Red, Blue, Green, Yellow*) |
| **Gate 24** | Room 24 | 4 Slots | **Button of TOBY Gate**: **4 DISTINCT SHAPES & 4 DISTINCT COLOURS** |

---

## 🗺️ Facility Grid Layout

The facility layout matrix spans 6 rows and 7 columns. Empty coordinates are represented by `-1`:

```text
Row 6 (Top)    :  [ -1, -1, 24, -1, -1, -1, -1 ]
Row 5          :  [ -1, -1, 6, 12, 23, -1, -1 ]
Row 4          :  [ -1, -1, -1,  3, 21,  9, -1 ]
Row 3          :  [ -1, 20, 15, 13, 22, 14, -1 ]
Row 2          :  [ -1, 8, 18,  2, 19,  1, -1 ]
Row 1 (Spawn)  :  [ 10, 17,  7, 11, 16,  4,  5 ]
```

---

## 🚀 How to Build & Run

### 1. Java Desktop Application (Swing)

#### Prerequisites
- **JDK 21** or higher
- **Maven 3.8+**

#### Build and Run

From the repository root on Windows, run the included script. It runs the Maven tests, compiles the application, creates `button-of-toby.jar`, and launches it:

```bash
build_and_run.bat
```

To run the Maven tests separately:

```bash
mvn -f button-of-toby/pom.xml clean test
```

---

## ⚡ Speedrunning

Optimal play requires allocating items backwards from Room 24 down to Room 5 to prevent soft-locking key color and shape constraints required for higher-tier gates.

---

## 📜 License & Credits

- **Author:** Ravi's World
- **Favicon:** [maxluczynski on Flaticon](https://www.flaticon.com/free-icons/exclamation)
- **Licence:** [GNU AGPLv3](https://github.com/Ravis-World/Button-of-TOBY/blob/main/LICENCE)