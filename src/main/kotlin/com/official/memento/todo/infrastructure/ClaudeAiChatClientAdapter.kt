package com.official.memento.todo.infrastructure

import com.fasterxml.jackson.annotation.JsonProperty
import com.official.memento.global.exception.ErrorCode
import com.official.memento.global.exception.MementoException
import com.official.memento.global.stereotype.Adapter
import com.official.memento.todo.domain.ToDo
import com.official.memento.todo.domain.vo.ClaudeAiChatClientOutputPort
import com.official.memento.todo.domain.vo.PrioritizedToDo
import com.official.memento.todo.infrastructure.persistence.ToDoJpaRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.reactive.function.client.WebClient
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate

@Adapter
class ClaudeAiChatClientAdapter(
    private val webClient: WebClient,
    @Value("\${claude.ai.api-key}")
    private val claudeAiApiKey: String,
    private val toDoJpaRepository: ToDoJpaRepository,
) : ClaudeAiChatClientOutputPort {
    companion object {
        private const val CLAUDE_AI_URL = "https://api.anthropic.com/v1/messages"
        private const val CLAUDE_AI_API_KEY_HEADER = "x-api-key"
        private const val CLAUDE_AI_VERSION_HEADER = "anthropic-version"
        private const val CLAUDE_AI_VERSION_VALUE = "2023-06-01"
        private const val MAX_TOKENS = 4096
        private const val MODEL_NAME = "claude-3-5-haiku-20241022"
        private const val PRIORITIZE_PROMPT_FILE_PATH = "src/main/resources/prioritize-prompt.txt"
    }

    private lateinit var prioritizationPrompt: String

    override fun prioritizeTodo(
        todoList: List<ToDo>,
        orderList: List<Int>,
    ): List<PrioritizedToDo> {
        prioritizationPrompt = readPromptFromFile()
        var taskPrompt = ""

        for (idx in todoList.indices) {
            taskPrompt += todoList[idx].toTaskDescription() + '\n' + orderList[idx] + '\n'
        }

        todoList.map { taskPrompt += it.toTaskDescription() + '\n' }
        val replacedPrompt = prioritizationPrompt.replace("{{TASKS_DATA}}", taskPrompt)

        val requestBody = mapOf(
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
                                                    "tasks" to
                                                            mapOf(
                                                                "type" to "array",
                                                                "items" to
                                                                        mapOf(
                                                                            "type" to "object",
                                                                            "properties" to
                                                                                    mapOf(
                                                                                        "task" to
                                                                                                mapOf(
                                                                                                    "type" to "string",
                                                                                                    "description" to "task description",
                                                                                                ),
                                                                                        "id" to
                                                                                                mapOf(
                                                                                                    "type" to "number",
                                                                                                    "description" to "task id mapped by requested task id",
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
                                                                                                    "description" to "task urgency level",
                                                                                                ),
                                                                                        "importance" to
                                                                                                mapOf(
                                                                                                    "type" to "number",
                                                                                                    "description" to "task importance level",
                                                                                                ),
                                                                                        "priority" to
                                                                                                mapOf(
                                                                                                    "type" to "number",
                                                                                                    "description" to "task priority level",
                                                                                                ),
                                                                                        "order" to
                                                                                                mapOf(
                                                                                                    "type" to "number",
                                                                                                    "description" to "task order",
                                                                                                ),
                                                                                    ),
                                                                        ),
                                                            ),
                                                ),
                                        "required" to
                                                listOf(
                                                    "task",
                                                    "id",
                                                    "deadline",
                                                    "created_date",
                                                    "urgency",
                                                    "importance",
                                                    "priority",
                                                    "order",
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
        )
        val response =
            webClient.post()
                .uri(CLAUDE_AI_URL)
                .header(CLAUDE_AI_API_KEY_HEADER, claudeAiApiKey)
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(CLAUDE_AI_VERSION_HEADER, CLAUDE_AI_VERSION_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(ClaudeResponse::class.java)
                .block()
        val taskListResponse =
            response?.content?.filter {
                it.type == "tool_use"
            }?.firstOrNull()?.input ?: throw MementoException(ErrorCode.INTERNAL_SERVER_ERROR)
        return taskListResponse.map {
            PrioritizedToDo(
                task = it.task,
                id = it.id,
                createdDate = LocalDate.parse(it.createdDate),
                deadline = LocalDate.parse(it.deadline),
                priority = it.priority,
                urgency = it.urgency,
                importance = it.importance,
                order = it.order,
            )
        }.toList()
    }

    private fun readPromptFromFile(): String {
        val path = Paths.get(PRIORITIZE_PROMPT_FILE_PATH)
        return Files.readString(path)
    }
}

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
    val input: List<TaskInput>,
)

data class TaskInput(
    val task: String,
    val id: Long,
    val deadline: String,
    @JsonProperty("created_date")
    val createdDate: String,
    val urgency: Float,
    val importance: Float,
    val priority: Float,
    val order: Int,
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
