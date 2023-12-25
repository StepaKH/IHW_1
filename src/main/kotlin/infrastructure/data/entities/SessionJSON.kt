package infrastructure.data.entities

import basis.models.Seat
import kotlinx.serialization.Serializable

@Suppress("PLUGIN_IS_NOT_ENABLED")
@Serializable
class SessionJSON(
    val id: Int,
    var startingHour: String,
    var seats: MutableList<MutableList<Seat>>
)