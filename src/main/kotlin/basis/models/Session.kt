package basis.models

import java.time.LocalDateTime

class Session(
    val id: Int,
    var startingHour: LocalDateTime,
    var seats: MutableList<MutableList<Seat>>
)