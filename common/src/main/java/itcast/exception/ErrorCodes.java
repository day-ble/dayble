package itcast.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCodes {

    BLOG_NOT_FOUND("블로그가 유효하지 않습니다.", 1001L, HttpStatus.NOT_FOUND),
    NEWS_NOT_FOUND("뉴스가 유효하지 않습니다.", 2001L, HttpStatus.NOT_FOUND),

    BAD_REQUEST("BAD_REQUEST", 9404L, HttpStatus.BAD_REQUEST),
    BAD_REQUEST_JSON_PARSE_ERROR("[BAD_REQUEST] JSON_PARSE_ERROR - 올바른 JSON 형식이 아님", 9405L, HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", 9999L, HttpStatus.INTERNAL_SERVER_ERROR);

    public final String message;
    public final Long code;
    public final HttpStatus status;

    ErrorCodes(final String message, final Long code, final HttpStatus status) {
        this.message = message;
        this.code = code;
        this.status = status;
    }
}
