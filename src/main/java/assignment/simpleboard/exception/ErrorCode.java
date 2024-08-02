package assignment.simpleboard.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 400
    PARAMETER_NOT_VALID(HttpStatus.BAD_REQUEST, "The input information is invalid."),

    // 404
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "There is no corresponding schedule."),

    // 502
    DATABASE_CONNECTION_ERROR(HttpStatus.BAD_GATEWAY, "Failed to connect to database server."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
