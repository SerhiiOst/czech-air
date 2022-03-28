package com.serhii.czechair.data.models

data class MapStatsResponse(
    val states: List<List<Any>>,
    val time: Int
) {
    fun toMapStats() =
        MapStats(
            time = time,
            flightStates = this.states.map { FlightState(it) }
        )
}

data class MapStats(
    val time: Int,
    val flightStates: List<FlightState>
)

data class FlightState(
    //half of these variables I don't need but I'll include them anyway just in case
    val states: List<Any>,
    val icao24: String? = states[0] as? String,
    val callsign: String? = states[1] as? String,
    val origin_country: String? = states[2] as? String,
    val time_position: Int? = states[3] as? Int,
    val last_contact: Int? = states[4] as? Int,
    val longitude: Double? = states[5] as? Double,
    val latitude: Double? = states[6] as? Double,
    val baro_altitude: Double? = states[7] as? Double,
    val on_ground: Boolean? = states[8] as? Boolean,
    val velocity: Double? = states[9] as? Double,
    val true_track: Double? = states[10] as? Double,
    val vertical_rate: Double? = states[11] as? Double,
//    val sensors: String = states[12]?.toString(),
    val geo_altitude: Double? = states[13] as? Double,
    val squawk: String? = states[14] as? String,
    val spi: Boolean? = states[15] as? Boolean,
    val position_source: Double? = states[16] as? Double,
) {
    fun isValid() = latitude != null && longitude != null

    //remove whitespaces from call sign
    override fun toString(): String = "${callsign?.replace("\\s".toRegex(), "")} from $origin_country"
}