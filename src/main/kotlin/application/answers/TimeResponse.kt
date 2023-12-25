package application.answers

import java.time.LocalDateTime

class TimeResponse(var isValid: Boolean) {
    var dateTime: LocalDateTime? = null
}