package org.dimas.medextest.compute.lab

enum class LabAnalyte(
    val code: String,
    val title: String,
    val unit: String,
    val low: Double,
    val high: Double,
) {
    Hemoglobin("HGB", "Гемоглобин", "г/л", 120.0, 160.0),
    Glucose("GLU", "Глюкоза", "ммоль/л", 3.9, 6.1),
    Cholesterol("CHOL", "Холестерин", "ммоль/л", 3.0, 5.2),
    Thyrotropin("TSH", "ТТГ", "мЕд/л", 0.4, 4.0),
    CReactiveProtein("CRP", "С-реактивный белок", "мг/л", 0.0, 5.0),
    Alanine("ALT", "АЛТ", "Ед/л", 7.0, 40.0),
    Ferritin("FER", "Ферритин", "нг/мл", 20.0, 250.0),
    VitaminD("VITD", "Витамин D", "нг/мл", 30.0, 100.0),
    ;

    fun isInRange(value: Double): Boolean = value in low..high

    companion object {
        private val byCode: Map<String, LabAnalyte> = entries.associateBy { it.code }

        fun of(code: String): LabAnalyte? = byCode[code]
    }
}
