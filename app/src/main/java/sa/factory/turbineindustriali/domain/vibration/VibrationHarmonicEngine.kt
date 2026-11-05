package sa.factory.turbineindustriali.domain.vibration

import kotlinx.serialization.Serializable

enum class IsoSeverityZone(val code: String, val labelArabic: String, val isCompliant: Boolean) {
    ZONE_A("Zone A", "حالة مثالية جديدة (ممتازة)", true),
    ZONE_B("Zone B", "تشغيل مستمر آمن (مقبولة)", true),
    ZONE_C("Zone C", "اهتزاز مرتفع يستوجب الصيانة (تحذير)", false),
    ZONE_D("Zone D", "اهتزاز حرج خطر يستوجب الإيقاف الفوري (خطر)", false)
}

@Serializable
data class VibrationReading(
    val axisX_mms: Double,
    val axisY_mms: Double,
    val axisZ_mms: Double,
    val peakAccelerationG: Double,
    val bearingKurtosis: Double,
    val acousticDb: Double,
    val timestamp: Long = System.currentTimeMillis()
) {
    val rmsVelocity_mms: Double
        get() {
            val sumSq = (axisX_mms * axisX_mms) + (axisY_mms * axisY_mms) + (axisZ_mms * axisZ_mms)
            val meanSq = sumSq / 3.0
            return Math.round(Math.sqrt(meanSq) * 100.0) / 100.0
        }

    val severityZone: IsoSeverityZone
        get() = when {
            rmsVelocity_mms < 1.8 -> IsoSeverityZone.ZONE_A
            rmsVelocity_mms < 4.5 -> IsoSeverityZone.ZONE_B
            rmsVelocity_mms < 11.2 -> IsoSeverityZone.ZONE_C
            else -> IsoSeverityZone.ZONE_D
        }
}

class VibrationHarmonicEngine {

    fun evaluateTelemetry(x: Double, y: Double, z: Double, peakG: Double, kurtosis: Double, db: Double): VibrationReading {
        return VibrationReading(
            axisX_mms = Math.round(x * 100.0) / 100.0,
            axisY_mms = Math.round(y * 100.0) / 100.0,
            axisZ_mms = Math.round(z * 100.0) / 100.0,
            peakAccelerationG = Math.round(peakG * 100.0) / 100.0,
            bearingKurtosis = Math.round(kurtosis * 100.0) / 100.0,
            acousticDb = Math.round(db * 10.0) / 10.0
        )
    }

    fun calculateModbusCrc16(data: ByteArray): Int {
        var crc = 0xFFFF
        for (b in data) {
            crc = crc xor (b.toInt() and 0xFF)
            for (i in 0 until 8) {
                if ((crc and 0x0001) != 0) {
                    crc = (crc ushr 1) xor 0xA001
                } else {
                    crc = crc ushr 1
                }
            }
        }
        return crc and 0xFFFF
    }
}
