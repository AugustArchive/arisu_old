package land.arisu.breb.core.ast

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val line: Int,
    val character: Int
)
