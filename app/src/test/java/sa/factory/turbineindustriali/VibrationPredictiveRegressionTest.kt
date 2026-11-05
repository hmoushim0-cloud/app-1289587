package sa.factory.turbineindustriali

import sa.factory.turbineindustriali.domain.vibration.IsoSeverityZone
import sa.factory.turbineindustriali.domain.vibration.VibrationHarmonicEngine
import sa.factory.turbineindustriali.domain.predictive.PredictiveMaintenanceEngine
import org.junit.Assert.*
import org.junit.Test

class VibrationPredictiveRegressionTest {

    @Test
    fun testIso10816VibrationSeverityCalculations() {
        val engine = VibrationHarmonicEngine()

        // Test Zone A (Excellent < 1.8 mm/s)
        val readingA = engine.evaluateTelemetry(1.0, 1.2, 0.8, 0.35, 2.9, 64.2)
        assertEquals(1.01, readingA.rmsVelocity_mms, 0.05)
        assertEquals(IsoSeverityZone.ZONE_A, readingA.severityZone)
        assertTrue(readingA.severityZone.isCompliant)

        // Test Zone B (Acceptable 1.8 - 4.5 mm/s)
        val readingB = engine.evaluateTelemetry(2.8, 3.1, 2.5, 0.85, 3.4, 71.5)
        assertTrue(readingB.rmsVelocity_mms in 1.8..4.5)
        assertEquals(IsoSeverityZone.ZONE_B, readingB.severityZone)
        assertTrue(readingB.severityZone.isCompliant)

        // Test Zone D (Critical > 11.2 mm/s)
        val readingD = engine.evaluateTelemetry(12.5, 14.0, 11.8, 4.2, 7.8, 92.4)
        assertTrue(readingD.rmsVelocity_mms > 11.2)
        assertEquals(IsoSeverityZone.ZONE_D, readingD.severityZone)
        assertFalse(readingD.severityZone.isCompliant)
    }

    @Test
    fun testModbusCrc16AndPredictiveAssessment() {
        val engine = VibrationHarmonicEngine()
        val predictiveEngine = PredictiveMaintenanceEngine()

        // Verify Modbus CRC16
        val testPacket = byteArrayOf(0x01, 0x03, 0x00, 0x00, 0x00, 0x02)
        val crc = engine.calculateModbusCrc16(testPacket)
        assertTrue("CRC must be valid non-zero 16-bit integer", crc in 1..0xFFFF)

        // Verify Predictive Health Assessment
        val reading = engine.evaluateTelemetry(1.2, 1.4, 1.1, 0.42, 3.0, 66.8)
        val assessment = predictiveEngine.assessTurbineHealth("TURB-GEN-04", reading)

        assertEquals("TURB-GEN-04", assessment.machineTag)
        assertTrue(assessment.healthScorePct >= 95)
        assertTrue(assessment.estimatedRulHours >= 10000)
    }
}
