package basis.models

import kotlinx.serialization.Serializable

@Suppress("PLUGIN_IS_NOT_ENABLED")
@Serializable
class Ticket(var id: Int, val sessionId: Int, val row: Int, val column: Int, var isDeleted: Boolean = false)