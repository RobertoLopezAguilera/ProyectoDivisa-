package com.example.proyectodivisa.provider

import android.content.*
import android.database.Cursor
import android.database.SQLException
import android.net.Uri
import android.util.Log
import com.example.proyectodivisa.AppDatabase
import com.example.proyectodivisa.model.ExchangeRateDao

class ExchangeRateProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.example.proyectodivisa.provider"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/exchange_rates")

        private const val CODE_EXCHANGE_RATE_RANGE = 1
        private const val CODE_EXCHANGE_RATE_LAST_10 = 2  // Nueva constante

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "exchange_rates/*/#/#", CODE_EXCHANGE_RATE_RANGE)
            addURI(AUTHORITY, "exchange_rates/*", CODE_EXCHANGE_RATE_LAST_10)  // Nueva URI
        }
    }



    private lateinit var exchangeRateDao: ExchangeRateDao

    override fun onCreate(): Boolean {
        exchangeRateDao = AppDatabase.getDatabase(context!!).exchangeRateDao()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            CODE_EXCHANGE_RATE_RANGE -> {
                val pathSegments = uri.pathSegments
                val currency = pathSegments[1] // Moneda
                val startDate = pathSegments[2].toLongOrNull() ?: return null
                val endDate = pathSegments[3].toLongOrNull() ?: return null
                Log.d("ExchangeRateProvider", "Query con moneda=$currency, startDate=$startDate, endDate=$endDate")
                exchangeRateDao.getExchangeRatesInRangeCursor(currency, startDate, endDate)
            }
            CODE_EXCHANGE_RATE_LAST_10 -> {  // Nueva opción
                val currency = uri.lastPathSegment ?: return null
                Log.d("ExchangeRateProvider", "Query últimos 10 registros de la moneda $currency")
                exchangeRateDao.getLast10ExchangeRatesByCurrency(currency)
            }
            else -> {
                Log.e("ExchangeRateProvider", "URI no soportado: $uri")
                null
            }
        }
    }



    override fun getType(uri: Uri): String? = "vnd.android.cursor.dir/vnd.$AUTHORITY.exchange_rates"

    override fun insert(uri: Uri, values: ContentValues?): Uri? = throw SQLException("Insert not supported")
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0
}