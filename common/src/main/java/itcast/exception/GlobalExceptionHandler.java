package itcast.exception;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleJsonParseExceptionHandler(HttpMessageNotReadableException ex) {
        final ApiErrorResponse body = ApiErrorResponse.from(ErrorCodes.BAD_REQUEST_JSON_PARSE_ERROR);

        final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(contentType)
                .body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidExceptionHandler(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fieldError -> camelToSnake(fieldError.getField()),
                        FieldError::getDefaultMessage
                ));

        final ApiErrorResponse body = ApiErrorResponse.from(ErrorCodes.BAD_REQUEST, errors);
        final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(contentType)
                .body(body);
    }

    @ExceptionHandler(value = {ItCastApplicationException.class})
    public ResponseEntity<ApiErrorResponse> itApplicationException(ItCastApplicationException ex) {
        final ApiErrorResponse body = ApiErrorResponse.from(ex.getErrorCodes());
        final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);

        return ResponseEntity.status(ex.getHttpStatus())
                .contentType(contentType)
                .body(body);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<ApiErrorResponse> RuntimeExceptionHandler(RuntimeException ex) {
        final ApiErrorResponse body = ApiErrorResponse.from(ErrorCodes.INTERNAL_SERVER_ERROR);
        final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(status)
                .contentType(contentType)
                .body(body);
    }

    private String camelToSnake(String str) {
        return str.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }
}
