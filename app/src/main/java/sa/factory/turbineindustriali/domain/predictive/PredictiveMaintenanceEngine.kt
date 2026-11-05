package sa.factory.turbineindustriali.domain.predictive

import kotlinx.serialization.Serializable
import sa.factory.turbineindustriali.domain.vibration.IsoSeverityZone
import sa.factory.turbineindustriali.domain.vibration.VibrationReading

@Serializable
data class MachineHealthAssessment(
    val machineTag: String,
    val reading: VibrationReading,
    val estimatedRulHours: Int,
    val healthScorePct: Int,
    val recommendationArabic: String
)

class PredictiveMaintenanceEngine {

    fun assessTurbineHealth(machineTag: String, reading: VibrationReading): MachineHealthAssessment {
        val zone = reading.severityZone
        val (rulHours, healthPct, rec) = when (zone) {
            IsoSeverityZone.ZONE_A -> Triple(
                12000,
                98,
                "الحالة ممتازة: استمر في جدول التشغيل الاعتيادي مع الفحص الدوري كل 2000 ساعة."
            )
            IsoSeverityZone.ZONE_B -> Triple(
                6500,
                88,
                "تشغيل مستقر ومقبول: راقب تزييت المحامل وسجل قراءات الاهتزاز شهرياً."
            )
            IsoSeverityZone.ZONE_C -> Triple(
                800,
                55,
                "تنبيه صيانة وقائية: اهتزاز غير مرضي، يلزم إعادة موازنة المحور وفحص المحامل خلال أسبوع."
            )
            IsoSeverityZone.ZONE_D -> Triple(
                24,
                15,
                "خطر فوري: اهتزاز حرج يتجاوز معايير السلامة، يوصى بالإيقاف الطارئ لتجنب عطل كارثي."
            )
        }

        return MachineHealthAssessment(
            machineTag = machineTag,
            reading = reading,
            estimatedRulHours = rulHours,
            healthScorePct = healthPct,
            recommendationArabic = rec
        )
    }
}
