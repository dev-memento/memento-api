package com.official.memento.global.exception;

public enum ErrorCode {

    /*빈 데이터*/
    NULL_DATA_ERROR("필수 입력 데이터가 누락되었습니다."),

    /* 서버 내부  에러 */
    INTERNAL_SERVER_ERROR("서버 내부에서 에러가 발생했습니다."),

    /* 존재하지 않는 데이터 */
    NOT_FOUND_ENTITY("찾을 수 없는 데이터입니다."),

    /* 유효하지 않은 요청 */
    INVALID_REQUEST_BODY("유효하지 않은 요청입니다."),
    INVALID_ARGUMENT_TYPE("잘못된 매개변수 타입입니다."),
    INVALID_JSON_FORMAT("요청 JSON 형식이 올바르지 않습니다."),
    DECODING_FAILURE("디코딩 실패로 인해 요청을 처리할 수 없습니다."),

    /* 소셜 로그인 관련 에러 */
    INVALID_ID_TOKEN("유효하지 않은 ID 토큰입니다."),
    UNSUPPORTED_PROVIDER("지원하지 않는 소셜 로그인 제공자입니다."),
    UNAUTHORIZED_USER("인증되지 않은 유저입니다."),
    INVALID_ACCESS_TOKEN("유효하지 않은 액세스 토큰입니다."),
    INVALID_REFRESH_TOKEN("유효하지 않은 리프레시 토큰입니다."),
    EXPIRED_TOKEN("토큰이 만료되었습니다"),
    EXPECTED_TOKEN_ERROR("토큰 에러입니다"),
    UNEXPECTED_TOKEN_ERROR("예상치 못한 토큰 에러입니다"),

    INVALID_AI_PRIORITIZATION_REQUEST("AI 우선순위 요청이 유효하지 않습니다.");
    ;

    private final String message;

    ErrorCode(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
