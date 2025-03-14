package com.example.proyectodivisa.provider

import android.content.*
import android.database.Cursor
import android.database.SQLException
import android.net.Uri
import android.util.Log
import com.example.proyectodivisa.AppDatabase
import com.example.proyectodivisa.model.ExchangeRateDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

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
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return runBlocking(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context!!) // BD inicializada

            val cursor: Cursor? = when (uriMatcher.match(uri)) {
                CODE_EXCHANGE_RATE_RANGE -> {
                    val currency = uri.pathSegments[1]
                    val startDate = uri.pathSegments[2].toLong()
                    val endDate = uri.pathSegments[3].toLong()

                    Log.d("ExchangeRateProvider", "Consulta recibida: currency=$currency, startDate=$startDate, endDate=$endDate")

                    val resultCursor = db.exchangeRateDao().getExchangeRatesInRangeCursor(currency, startDate, endDate)

                    if (resultCursor != null && resultCursor.count > 0) {
                        Log.d("ExchangeRateProvider", "Cursor tiene ${resultCursor.count} registros")
                    } else {
                        Log.e("ExchangeRateProvider", "Cursor vacío")
                    }

                    resultCursor
                }
                else -> {
                    Log.e("ExchangeRateProvider", "URI no reconocida: $uri")
                    null
                }
            }
            cursor
        }
    }


    override fun getType(uri: Uri): String? = "vnd.android.cursor.dir/vnd.$AUTHORITY.exchange_rates"

    override fun insert(uri: Uri, values: ContentValues?): Uri? = throw SQLException("Insert not supported")
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0
}