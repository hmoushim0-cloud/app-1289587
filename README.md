# تطبيق مجس التوربينات — تصميم تيك باك لبيجامات — تصم (`turbine-industrial-iot-1289587`)
> منظومة industrial_iot المتكاملة مع ربط 3-Axis Vibration Accelerometer + Modbus RS485 Acoustic Probe

- **Project ID**: `#1289587`
- **Commercial Archetype**: `industrial_iot`
- **Bound Hardware Peripheral**: `3-Axis Vibration Accelerometer + Modbus RS485 Acoustic Probe`
- **Package Name**: `sa.factory.turbineindustriali`
- **Zero-Emoji Invariant**: Strictly Enforced (100% Vector Icons)

---

## [Sidekick Autonomous Update]

### 1. Completed Bug Fixes & Code Quality Enhancements
- **Resolved Kotlin Character Literal Syntax Errors**: Corrected single-quoted string fallbacks inside string interpolation templates in `HomeScreen.kt` and `TelemetryScreen.kt`.
- **Added Missing UI Imports**: Added `FontWeight` and `sp` imports in `MainActivity.kt`.
- **Relocated Unit Tests to Standard Gradle Classpath**: Moved regression tests from `app/src/main/java/test/` to `app/src/test/java/sa/factory/turbineindustriali/`.
- **Standard Gradle Wrapper Added**: Configured `gradlew`, `gradlew.bat`, and `gradle/wrapper/` for deterministic CI execution.

### 2. Newly Built Features
- **ISO 10816 3-Axis Vibration Severity Engine (`VibrationHarmonicEngine`)**:
  - Computes RMS vibration velocity in mm/s across X, Y, Z axes.
  - Classifies operational status into ISO 10816 Severity Zones (Zone A, B, C, D).
  - Implemented Modbus RS485 CRC16 cyclic redundancy checksum.
- **Predictive Maintenance & Acoustic Diagnostics (`PredictiveMaintenanceEngine`)**:
  - Calculates Remaining Useful Life (RUL) in operational hours.
  - Monitors acoustic probe dB friction thresholds and evaluates turbine health score (%).
  - Live Compose card integrated into the primary dashboard.
- **Comprehensive Regression Test Suite**:
  - Created `VibrationPredictiveRegressionTest.kt` verifying ISO 10816 zones, Modbus CRC16, and predictive health scoring.

### 3. How to Build and Run
- **Run Unit Tests**:
  ```bash
  ./gradlew testDebugUnitTest
  ```
- **Build Debug APK**:
  ```bash
  ./gradlew assembleDebug
  ```
- **Run Cloud Test Harness (Pixel 6 Emulator)**:
  ```bash
  python3 /root/capture_harness_screens.py --repo-dir "/root/sidekick-workspace/turbine-industrial-iot-1289587" --output /tmp/sidekick_shot.png
  ```

