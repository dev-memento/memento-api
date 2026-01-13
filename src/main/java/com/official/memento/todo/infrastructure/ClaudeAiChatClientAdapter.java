package com.official.memento.todo.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.official.memento.global.exception.ClaudeException;
import com.official.memento.global.exception.ErrorCode;
import com.official.memento.global.exception.MementoException;
import com.official.memento.global.stereotype.Adapter;
import com.official.memento.todo.domain.entity.ToDo;
import com.official.memento.todo.domain.vo.ClaudeAiChatClientOutputPort;
import com.official.memento.todo.domain.vo.PrioritizedToDo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Adapter
public class ClaudeAiChatClientAdapter implements ClaudeAiChatClientOutputPort {

    private static final String CLAUDE_AI_URL = "https://api.anthropic.com/v1/messages";
    private static final String CLAUDE_AI_API_KEY_HEADER = "x-api-key";
    private static final String CLAUDE_AI_VERSION_HEADER = "anthropic-version";
    private static final String CLAUDE_AI_VERSION_VALUE = "2023-06-01";
    private static final int MAX_TOKENS = 8192;
    private static final String MODEL_NAME = "claude-3-5-haiku-20241022";

    private final WebClient webClient;
    private final String claudeAiApiKey;

    private final String prioritizationPrompt = """
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
            """;

    public ClaudeAiChatClientAdapter(
            WebClient webClient,
            @Value("${claude.ai.api-key}") String claudeAiApiKey
    ) {
        this.webClient = webClient;
        this.claudeAiApiKey = claudeAiApiKey;
    }

    @Override
    public List<PrioritizedToDo> prioritizeTodo(
            List<ToDo> todoList,
            List<Double> orderList,
            String personalInfo
    ) {
        try {
            StringBuilder taskPrompt = new StringBuilder();

            for (int i = 0; i < todoList.size(); i++) {
                taskPrompt.append(todoList.get(i).toTaskDescription())
                        .append('\n')
                        .append(orderList.get(i))
                        .append('\n');
            }

            String replacedPrompt = prioritizationPrompt
                    .replace("{{TASKS_DATA}}", taskPrompt.toString())
                    .replace("{{PERSONAL_INFO}}", personalInfo);

            Map<String, Object> requestBody = Map.of(
                    "model", MODEL_NAME,
                    "max_tokens", MAX_TOKENS,
                    "tools", List.of(
                            Map.of(
                                    "name", "get_task",
                                    "description", "Get the task info given in the information",
                                    "input_schema", Map.of(
                                            "type", "object",
                                            "properties", Map.of(
                                                    "tasks", Map.of(
                                                            "type", "array",
                                                            "items", Map.of(
                                                                    "type", "object",
                                                                    "properties", Map.of(
                                                                            "task", Map.of(
                                                                                    "type", "string",
                                                                                    "description", "task description"
                                                                            ),
                                                                            "id", Map.of(
                                                                                    "type", "number",
                                                                                    "description", "task id mapped by requested task id"
                                                                            ),
                                                                            "deadline", Map.of(
                                                                                    "type", "string",
                                                                                    "description", "task deadline date in YYYY-MM-DD format"
                                                                            ),
                                                                            "created_date", Map.of(
                                                                                    "type", "string",
                                                                                    "description", "task created date in YYYY-MM-DD format"
                                                                            ),
                                                                            "urgency", Map.of(
                                                                                    "type", "number",
                                                                                    "description", "task urgency level"
                                                                            ),
                                                                            "importance", Map.of(
                                                                                    "type", "number",
                                                                                    "description", "task importance level"
                                                                            ),
                                                                            "priority", Map.of(
                                                                                    "type", "number",
                                                                                    "description", "task priority level"
                                                                            ),
                                                                            "order", Map.of(
                                                                                    "type", "number",
                                                                                    "description", "task order"
                                                                            )
                                                                    )
                                                            )
                                                    )
                                            ),
                                            "required", List.of(
                                                    "task",
                                                    "id",
                                                    "deadline",
                                                    "created_date",
                                                    "urgency",
                                                    "importance",
                                                    "priority",
                                                    "order"
                                            )
                                    )
                            )
                    ),
                    "messages", List.of(
                            Map.of(
                                    "role", "user",
                                    "content", replacedPrompt
                            )
                    )
            );

            ClaudeResponse response = webClient.post()
                    .uri(CLAUDE_AI_URL)
                    .header(CLAUDE_AI_API_KEY_HEADER, claudeAiApiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(CLAUDE_AI_VERSION_HEADER, CLAUDE_AI_VERSION_VALUE)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(ClaudeResponse.class)
                    .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                    .block();

            TaskInput taskResponse = response.content().stream()
                    .filter(content -> "tool_use".equals(content.type()))
                    .map(Content::input)
                    .findFirst()
                    .orElseThrow(() -> new MementoException(ErrorCode.INTERNAL_SERVER_ERROR));

            return taskResponse.tasks().stream()
                    .map(task -> new PrioritizedToDo(
                            task.task(),
                            (long) task.id(),
                            LocalDate.parse(task.createdDate()),
                            LocalDate.parse(task.deadline()),
                            (float) task.priority(),
                            (float) task.urgency(),
                            (float) task.importance(),
                            task.order()
                    ))
                    .toList();
        } catch (Exception e) {
            throw new ClaudeException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}

record ClaudeResponse(
        String id,
        String type,
        String role,
        String model,
        List<Content> content,
        @JsonProperty("stop_reason") String stopReason,
        @JsonProperty("stop_sequence") String stopSequence,
        Usage usage
) {}

record Content(
        String type,
        String text,
        String id,
        String name,
        TaskInput input
) {}

record TaskInput(
        List<Task> tasks
) {}

record Task(
        String task,
        int id,
        String deadline,
        @JsonProperty("created_date") String createdDate,
        double urgency,
        double importance,
        double priority,
        double order
) {}

record Usage(
        @JsonProperty("input_tokens") int inputTokens,
        @JsonProperty("cache_creation_input_tokens") int cacheCreationInputTokens,
        @JsonProperty("cache_read_input_tokens") int cacheReadInputTokens,
        @JsonProperty("output_tokens") int outputTokens
) {}
