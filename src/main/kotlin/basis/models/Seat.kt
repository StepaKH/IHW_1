package basis.models

import kotlinx.serialization.Serializable

@Suppress("PLUGIN_IS_NOT_ENABLED")
@Serializable
enum class Seat{
    FREE,
    SOLD,
    HERE
}