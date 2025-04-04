package id.rasyiid.accord_android_test.data

import android.util.Log
import id.rasyiid.accord_android_test.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyHttpClient @Inject constructor() {

    fun getHttpClient() = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json( Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        engine {
            config {
                preconfigured = null
                pipelining = true
                webSocketFactory = null
                connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                callTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                followRedirects(false)
                followSslRedirects(false)
            }
        }

        install(Logging) {
            if(BuildConfig.DEBUG) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.i(TAG_KTOR_LOGGER, message)
                    }
                }
            }
        }

        install(ResponseObserver) {
            if(BuildConfig.DEBUG) {
                onResponse {
                    Log.i(TAG_HTTP_STATUS_LOGGER, "${it.status.value}")
                    Log.i(TAG_HTTP_BODY, it.bodyAsText(Charsets.UTF_8))
                }
            }
        }

        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }


    companion object {
        private const val TIME_OUT = 60
        private const val TAG_KTOR_LOGGER = "ktor_logger"
        private const val TAG_HTTP_STATUS_LOGGER = "http_status"
        private const val TAG_HTTP_BODY = "http_body"
    }
}