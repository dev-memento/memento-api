package com.official.memento.todo.infrastructure

import com.fasterxml.jackson.annotation.JsonProperty
import com.official.memento.global.exception.ClaudeException
import com.official.memento.global.exception.ErrorCode
import com.official.memento.global.exception.MementoException
import com.official.memento.global.stereotype.Adapter
import com.official.memento.todo.domain.entity.ToDo
import com.official.memento.todo.domain.vo.ClaudeAiChatClientOutputPort
import com.official.memento.todo.domain.vo.PrioritizedToDo
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.reactive.function.client.WebClient
import reactor.util.retry.Retry
import java.time.Duration
import java.time.LocalDate

@Adapter
class ClaudeAiChatClientAdapter(
    private val webClient: WebClient,
    @Value("\${claude.ai.api-key}")
    private val claudeAiApiKey: String,
) : ClaudeAiChatClientOutputPort {
    companion object {
        private const val CLAUDE_AI_URL = "https://api.anthropic.com/v1/messages"
        private const val CLAUDE_AI_API_KEY_HEADER = "x-api-key"
        private const val CLAUDE_AI_VERSION_HEADER = "anthropic-version"
        private const val CLAUDE_AI_VERSION_VALUE = "2023-06-01"
        private const val MAX_TOKENS = 8192
        private const val MODEL_NAME = "claude-3-5-haiku-20241022"
    }

    private var prioritizationPrompt: String = """
        You are an AI assistant specialized in task analysis and prioritization. Your goal is to process a list of tasks, calculate their priority scores, and output the results in a specific JSON format.
        
        {{PERSONAL_INFO}}

        Here is the list of tasks you need to analyze:

        {{TASKS_DATA}}

        Please follow these steps to process the tasks:

        1. Parse the input data and extract the information for each task. Each task may contain:
           - Task: The title or description of the task (always present)
           - Urgency: A value between 0 and 1 (may be missing)
           - Importance: A value between 0 and 1 (may be missing)
           - Deadline: The due date for the task (may be missing)
           - Created Date: The date when the task was created (always present)

        2. For each task, analyze the available information and calculate Urgency and Importance scores:
           - If both scores are provided, use these values.
           - If one or both are missing, estimate the scores based on the available information.
           - Ensure all scores are between 0 and 1.

        3. Calculate the priority score for each task using this formula:
           Priority = (Urgency × 0.3) + (Importance × 0.7)

        4. Prepare a JSON object for each task with the following fields:
           - Task: The original task title or description
           - id : task id (from request)
           - Urgency: The calculated or provided urgency score
           - Importance: The calculated or provided importance score
           - Deadline: The original deadline or an estimated one if missing
           - Created Date: The original creation date
           - order : task order (from request)

        5. Sort the tasks by their priority scores in descending order.

        6. Format the output as a valid JSON array, with each task as an object within the array.

        Before providing your final output, show your thought process for each task inside task_analysis tags. In your analysis:
        - List out each task with its provided information.
        - For each task, explicitly state how you're estimating any missing values.
        - Show your work when calculating the priority score.
        - Explain any assumptions or decisions made during the analysis.

        Here's an example of how your output should be structured:

        <output_example>
        [
          {
            "id": 1,
            "task": "Example task 1",
            "urgency": 0.8,
            "importance": 0.9,
            "deadline": "2023-05-15",
            "createdDate": "2023-05-01",
            "order": 1
          },
          {
            "id": 2,
            "task": "Example task 2",
            "urgency": 0.8,
            "importance": 0.9,
            "deadline": "2023-05-15",
            "createdDate": "2023-05-01",
            "order": 3
            },
            {
              "id" : 3,
              "task": "Example task 3",
              "urgency": 0.8,
              "importance": 0.9,
              "deadline": "2023-05-15",
              "createdDate": "2023-05-01",
              "order": 5
            },
        ]
        </output_example>

        Remember:
        - Handle any potential errors or edge cases in the input data gracefully.
        - If you encounter any issues or inconsistencies, make reasonable assumptions and document them in your analysis.
        - Ensure that no null values are present in the output. Provide estimated or default values based on available information if needed.
        - Do not include Quadrant or comment fields in the output.

        Provide your final output as a valid JSON array enclosed in <json_output> tags.
    """.trimIndent()

    override fun prioritizeTodo(
            todoList: List<ToDo>,
            orderList: List<Int>,
            personalInfo: String
    ): List<PrioritizedToDo> {
        try {
            var taskPrompt = ""

            for (idx in todoList.indices) {
                taskPrompt += todoList[idx].toTaskDescription() + '\n' + orderList[idx] + '\n'
            }

            val replacedPrompt = prioritizationPrompt.replace("{{TASKS_DATA}}", taskPrompt)
                .replace("{{PERSONAL_INFO}}", personalInfo)

            val requestBody = mapOf(
                "model" to MODEL_NAME,
                "max_tokens" to MAX_TOKENS,
                "tools" to listOf(
                    mapOf(
                        "name" to "get_task",
                        "description" to "Get the task info given in the information",
                        "input_schema" to mapOf(
                            "type" to "object",
                            "properties" to mapOf(
                                "tasks" to mapOf(
                                    "type" to "array",
                                    "items" to mapOf(
                                        "type" to "object",
                                        "properties" to mapOf(
                                            "task" to mapOf(
                                                "type" to "string",
                                                "description" to "task description",
                                            ),
                                            "id" to mapOf(
                                                "type" to "number",
                                                "description" to "task id mapped by requested task id",
                                            ),
                                            "deadline" to mapOf(
                                                "type" to "string",
                                                "description" to "task deadline date in YYYY-MM-DD format",
                                            ),
                                            "created_date" to mapOf(
                                                "type" to "string",
                                                "description" to "task created date in YYYY-MM-DD format",
                                            ),
                                            "urgency" to mapOf(
                                                "type" to "number",
                                                "description" to "task urgency level",
                                            ),
                                            "importance" to mapOf(
                                                "type" to "number",
                                                "description" to "task importance level",
                                            ),
                                            "priority" to mapOf(
                                                "type" to "number",
                                                "description" to "task priority level",
                                            ),
                                            "order" to mapOf(
                                                "type" to "number",
                                                "description" to "task order",
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                            "required" to listOf(
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
                "messages" to listOf(
                    mapOf(
                        "role" to "user",
                        "content" to replacedPrompt,
                    ),
                ),
            )

            val response = webClient.post()
                .uri(CLAUDE_AI_URL)
                .header(CLAUDE_AI_API_KEY_HEADER, claudeAiApiKey)
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .header(CLAUDE_AI_VERSION_HEADER, CLAUDE_AI_VERSION_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(ClaudeResponse::class.java)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                .block()

            val taskResponse = response?.content?.firstOrNull {
                it.type == "tool_use"
            }?.input ?: throw MementoException(ErrorCode.INTERNAL_SERVER_ERROR)

            return taskResponse.tasks.map {
                PrioritizedToDo(
                    task = it.task,
                    id = it.id.toLong(),
                    createdDate = LocalDate.parse(it.createdDate),
                    deadline = LocalDate.parse(it.deadline),
                    priority = it.priority.toFloat(),
                    urgency = it.urgency.toFloat(),
                    importance = it.importance.toFloat(),
                    order = it.order,
                )
            }
        } catch (e: Exception) {
            throw ClaudeException(ErrorCode.INTERNAL_SERVER_ERROR)
        }

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
    val input: TaskInput?,
)

data class TaskInput(
    val tasks: List<Task>
)

data class Task(
    val task: String,
    val id: Int,
    val deadline: String,
    @JsonProperty("created_date")
    val createdDate: String,
    val urgency: Double,
    val importance: Double,
    val priority: Double,
    val order: Int
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
