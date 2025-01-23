package com.official.memento.alarm.infrastructure.client

import com.official.memento.alarm.domain.port.AlarmOutputPort
import com.official.memento.global.stereotype.Adapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodilessEntity

@Adapter
class AlarmSendClientAdapter(
    private val webClient: WebClient,
    @Value("\${discord.error-alert-url}")
    private val discordErrorAlarmUri: String
) : AlarmOutputPort {
    override fun sendAlarm(
        uri: String,
        content: String,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            webClient.post()
                .uri(uri)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(AlarmRequestBody(content))
                .retrieve()
                .awaitBodilessEntity()
        }
    }

    override fun sendExceptionAlarm(e: Exception) {
        CoroutineScope(Dispatchers.IO).launch {
            webClient.post()
                .uri(discordErrorAlarmUri)
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(AlarmRequestBody(e.message ?: "알 수 없는 에러 발생"))
                .retrieve()
                .awaitBodilessEntity()
        }
    }

    data class AlarmRequestBody(
        val content: String,
    )
}
