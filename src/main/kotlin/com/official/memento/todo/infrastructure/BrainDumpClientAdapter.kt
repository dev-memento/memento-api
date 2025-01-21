package com.official.memento.todo.infrastructure

import com.fasterxml.jackson.annotation.JsonProperty
import com.official.memento.global.exception.ErrorCode
import com.official.memento.global.exception.MementoException
import com.official.memento.global.stereotype.Adapter
import com.official.memento.todo.domain.BrainDumpClientOutputPort
import com.official.memento.todo.domain.vo.BrainDump
import com.official.memento.todo.domain.vo.ToDoBrainDump
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.reactive.function.client.WebClient
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate

@Adapter
class BrainDumpClientAdapter(
    private val webClient: WebClient,
    @Value("\${claude.ai.api-key}")
    private val claudeApiKey: String,
) : BrainDumpClientOutputPort {
    companion object {
        private val CLAUDE_API_URL = "https://api.anthropic.com/v1/messages"
        private val CLAUDE_API_KEY_HEADER = "x-api-key"
        private val ANTHROPIC_VERSION_HEADER = "anthropic-version"
        private val CLAUDE_ANTHROPIC_VERSION = "2023-06-01"
        private val MAX_TOKENS = 4096
        private val MODEL_NAME = "claude-3-5-haiku-20241022"
        private val BRAINDUMP_PROMPT_FILE_PATH = "src/main/resources/braindump-prompt.txt"
    }

    private lateinit var brainDumpPrompt: String

    private var userInput: String? = null

    override fun createByBrainDump(brainDump: BrainDump): ToDoBrainDump {
        userInput = brainDump.content
        brainDumpPrompt = readPromptFromFile()
        val replacedPrompt = brainDumpPrompt.replace("{{USER_INPUT}}", userInput!!)

        val response =
            webClient.post()
                .uri(CLAUDE_API_URL)
                .header(CLAUDE_API_KEY_HEADER, claudeApiKey)
                .header(ANTHROPIC_VERSION_HEADER, CLAUDE_ANTHROPIC_VERSION)
                .header("content-type", "application/json")
                .bodyValue(
                    mapOf(
                        "model" to MODEL_NAME,
                        "max_tokens" to MAX_TOKENS,
                        "tools" to
                            listOf(
                                mapOf(
                                    "name" to "get_task",
                                    "description" to "Get the task info given in the information",
                                    "input_schema" to
                                        mapOf(
                                            "type" to "object",
                                            "properties" to
                                                mapOf(
                                                    "task" to
                                                        mapOf(
                                                            "type" to "string",
                                                            "description" to "task description",
                                                        ),
                                                    "deadline" to
                                                        mapOf(
                                                            "type" to "string",
                                                            "description" to "task deadline date in YYYY-MM-DD format",
                                                        ),
                                                    "created_date" to
                                                        mapOf(
                                                            "type" to "string",
                                                            "description" to "task created date in YYYY-MM-DD format",
                                                        ),
                                                    "urgency" to
                                                        mapOf(
                                                            "type" to "number",
                                                            "description" to "Urgency score from 0 to 1, two decimal places",
                                                        ),
                                                    "importance" to
                                                        mapOf(
                                                            "type" to "number",
                                                            "description" to "Importance score from 0 to 1, two decimal places",
                                                        ),
                                                    "priority" to
                                                        mapOf(
                                                            "type" to "number",
                                                            "description" to "Calculated priority score from 0 to 1, two decimal places",
                                                        ),
                                                ),
                                            "required" to
                                                listOf(
                                                    "task",
                                                    "deadline",
                                                    "created_date",
                                                    "urgency",
                                                    "importance",
                                                    "priority",
                                                ),
                                        ),
                                ),
                            ),
                        "messages" to
                            listOf(
                                mapOf(
                                    "role" to "user",
                                    "content" to replacedPrompt,
                                ),
                            ),
                    ),
                )
                .retrieve()
                .bodyToMono(ClaudeResponse::class.java)
                .block()
        val taskJsonResponse =
            response?.content?.filter {
                it.type == "tool_use"
            }?.firstOrNull()?.input ?: throw MementoException(ErrorCode.INTERNAL_SERVER_ERROR)

        return ToDoBrainDump(
            task = taskJsonResponse.task,
            deadline = LocalDate.parse(taskJsonResponse.deadline),
            createdDate = LocalDate.parse(taskJsonResponse.createdDate),
            urgency = taskJsonResponse.urgency,
            importance = taskJsonResponse.importance,
            priority = taskJsonResponse.priority,
        )
    }

    private fun readPromptFromFile(): String {
        val path = Paths.get(BRAINDUMP_PROMPT_FILE_PATH)
        return Files.readString(path)
    }

    data class ClaudeMessage(
        val role: String,
        val content: String,
    )

    data class ClaudeResponse(
        val id: String,
        val type: String,
        val role: String,
        val model: String,
        val content: List<Content>,
        @JsonProperty("stop_reason")
        val stopReason: String?,
        @JsonProperty("stop_sequence")
        val stopSequence: String?,
        val usage: Usage,
    )

    data class Content(
        val type: String,
        val text: String?,
        val id: String?,
        val name: String?,
        val input: TaskInput?,
    )

    data class TaskInput(
        val task: String,
        val deadline: String,
        @JsonProperty("created_date")
        val createdDate: String,
        val urgency: Float,
        val importance: Float,
        val priority: Float,
    )

    data class Usage(
        @JsonProperty("input_tokens")
        val inputTokens: Int,
        @JsonProperty("cache_creation_input_tokens")
        val cacheCreationInputTokens: Int,
        @JsonProperty("cache_read_input_tokens")
        val cacheReadInputTokens: Int,
        @JsonProperty("output_tokens")
        val outputTokens: Int,
    )
}
