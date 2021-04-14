package com.company.didi.model

class DiDiDriverWithPhoto(
    city: String,
    phone: String,
    name: String = "",
    var additional: String = ""
): DiDiDriver(
    city,
    phone,
    name
) {
    override fun toString(): String {
        return """
            |Водитель: 
            |   Телефон: $phone
            |   Город: $city
            |   Имя: $name
            |   Уже оставляли заявку: $additional
            """.trimMargin("|")
    }

    override fun toTelegramString(): String {
        return "№ $requestNumber %0A " +
                "Водитель: %0A  " +
                "Телефон: $phone %0A  " +
                "Город: $city %0A  " +
                "Имя: $name %0A " +
                "Уже оставляли заявку: $additional"
    }
}