package com.company.didi.model

import java.text.SimpleDateFormat
import java.util.*

open class DiDiDriver(
    var city: String,
    var phone: String,
    var name: String = ""
) {
    var time:String = SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(Date())
    var date:String = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(Date())
    var requestNumber:Int = 0
    set(value) {
        field = value + 1
    }

    init {
        phone = phone.replace("+7", "8")
            .replace(" ", "")
            .replace("-", "")
    }

    open fun toTelegramString(): String {
        return "№ $requestNumber %0A " +
                "Водитель: %0A  " +
                "Телефон: $phone %0A  " +
                "Город: $city %0A  " +
                "Имя: $name %0A "
    }
}