package assignment.simpleboard.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now().withNano(0);
    private final int status;
    private final String error;
    private final String code;
    private final String msg;

    public static ResponseEntity<Object> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .status(errorCode.getHttpStatus().value())
                        .error(errorCode.getHttpStatus().name())
                        .code(errorCode.name())
                        .msg(errorCode.getMessage())
                        .build());
    }

    public static Map<String, Object> toResponseObject(ErrorCode errorCode) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().withNano(0));
        response.put("status", errorCode.getHttpStatus().value());
        response.put("error", errorCode.getHttpStatus().name());
        response.put("code", errorCode.name());
        response.put("msg", errorCode.getMessage());

        return response;
    }

    public static Map<String, Object> toResponseObject(ErrorCode errorCode, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now().withNano(0));
        response.put("status", errorCode.getHttpStatus().value());
        response.put("error", errorCode.getHttpStatus().name());
        response.put("code", errorCode.name());
        response.put("msg", message);

        return response;
    }
}