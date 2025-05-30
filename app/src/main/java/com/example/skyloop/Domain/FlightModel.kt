package com.example.skyloop.Domain

import java.io.Serializable

data class FlightModel(
    var airlineLogo: String = "",
    var airlineName: String = "",
    var arriveTime: String = "",
    var classSeat: String = "",
    var date: String = "",
    var from: String = "",
    var fromShort: String = "",
    var numberSeat: Int = 0,
    var price: Double = 0.0,
    var reservedSeats: String = "",
    var time: String = "",
    var to: String = "",
    var toShort: String = ""
): Serializable
