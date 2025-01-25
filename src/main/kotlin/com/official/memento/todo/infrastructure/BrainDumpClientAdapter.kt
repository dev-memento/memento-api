package com.official.memento.todo.infrastructure

import com.fasterxml.jackson.annotation.JsonProperty
import com.official.memento.global.exception.ClaudeException
import com.official.memento.global.exception.ErrorCode
import com.official.memento.global.exception.MementoException
import com.official.memento.global.stereotype.Adapter
import com.official.memento.todo.domain.BrainDumpClientOutputPort
import com.official.memento.todo.domain.vo.BrainDump
import com.official.memento.todo.domain.vo.ToDoBrainDump
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.reactive.function.client.WebClient
import reactor.util.retry.Retry
import java.time.Duration
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
    }

    private var brainDumpPrompt: String = """
        You are an AI assistant designed to analyze user input and extract task information to create a structured task entry. Your goal is to process natural language input and organize it into a standardized format for task management.\n
        Here is the user input you need to analyze: {{USER_INPUT}}\n
        Please follow these steps to create a structured task entry:\n
        1. Analyze the user input and extract the following information:\n
           - Task description\n
           - Deadline (if mentioned)\n
           - Created date (if mentioned)\n
           - Indicators of urgency and importance\n
        2. Wrap your analysis inside <task_analysis> tags. Break down your thought process for each component as follows:\n
           - Quote relevant parts of the user input for each component.\n
           - For urgency and importance, explicitly consider and note down factors affecting the scores.\n
           - When estimating deadlines not explicitly mentioned, show your reasoning. This analysis will not be included in the final output but will help ensure accurate information extraction.\n
        3. After your analysis, create a structured task entry with the following components:\n
           - Task: The main action or objective (concise description)\n
           - Deadline: Due date for the task (format: YYYY-MM-DD)\n
           - Created Date: Date the task was created or mentioned (format: YYYY-MM-DD)\n
           - Urgency: Score from 0 to 1 (two decimal places)\n
           - Importance: Score from 0 to 1 (two decimal places)\n
           - Priority: Calculated score using the formula: (Urgency * 0.3) + (Importance * 0.7)\n
        4. Guidelines for each component:\n
           - Task: Extract the main action or objective from the input text.\n
           - Deadline:
             - If explicitly mentioned, use the date provided.\n
             - If not mentioned, estimate a reasonable deadline based on the task's nature and context.\n
           - Created Date:
             - If mentioned in the text, use that date.\n
             - If not mentioned, use today's date.\n
           - Urgency and Importance:\n
             - Analyze the text for keywords, phrases, and context indicating urgency and importance.\n
             - Assign scores from 0 to 1 (two decimal places) for both.\n
             - Consider factors such as deadline proximity, urgent language, and task significance.\n
             - If unclear, default to 0.50 for either score.\n
        5. Ensure all fields are filled with valid values. Do not leave any field empty or null.\n
        6. Provide your output in the following format:\n
           {\n
             "task": "[Task description]",\n
             "deadline": "[Deadline date in YYYY-MM-DD format]",\n
             "created_date": "[Created date in YYYY-MM-DD format]",\n
             "urgency": "[Urgency score from 0 to 1, two decimal places]",\n
             "importance": "[Importance score from 0 to 1, two decimal places]",\n
             "priority": "[Calculated priority score from 0 to 1, two decimal places]"\n
           }\n
        Remember, your analysis should be separate from the final task entry output. The user is only interested in the structured task entry.
    """.trimIndent()

    private var userInput: String? = null

    override fun createByBrainDump(brainDump: BrainDump): ToDoBrainDump {
        try {
            userInput = brainDump.content
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
                    .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
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
        } catch (e: Exception) {
            throw ClaudeException(ErrorCode.INTERNAL_SERVER_ERROR)
        }
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
