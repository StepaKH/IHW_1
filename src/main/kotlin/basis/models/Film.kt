package basis.models

import kotlinx.serialization.Serializable

@Suppress("PLUGIN_IS_NOT_ENABLED")
@Serializable
class Film(
    var id: Int,
    var sessionId: Int,
    var name: String,
    var description: String,
    var rating: Int,
    var durationMinutes: Int)