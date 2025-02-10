package com.example.proyectodivisa.api

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    val result: String, // Resultado de la solicitud, por ejemplo: "success"
    val documentation: String, // URL de la documentación de la API
    @SerializedName("terms_of_use")
    val termsOfUse: String, // URL de los términos de uso
    @SerializedName("time_last_update_unix")
    val timeLastUpdateUnix: Long, // Última actualización en formato UNIX
    @SerializedName("time_last_update_utc")
    val timeLastUpdateUtc: String, // Última actualización en formato UTC
    @SerializedName("time_next_update_unix")
    val timeNextUpdateUnix: Long, // Próxima actualización en formato UNIX
    @SerializedName("time_next_update_utc")
    val timeNextUpdateUtc: String, // Próxima actualización en formato UTC
    @SerializedName("base_code")
    val baseCode: String, // Código de la moneda base (por ejemplo: "USD")
    @SerializedName("conversion_rates")
    val conversionRates: Map<String, Double> // Tasas de conversión como un mapa de código de moneda a su valor
)
