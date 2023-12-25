package application.models

import java.time.LocalDateTime
import basis.models.Seat

class SessionWithFilm(
    val id: Int,
    var startingHour: LocalDateTime,
    var seats: MutableList<MutableList<Seat>>,
    var name: String,
    var description: String,
    var rating: Int,
    var durationMinutes: Int
)