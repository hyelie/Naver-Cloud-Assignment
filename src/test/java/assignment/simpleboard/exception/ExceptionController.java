package assignment.simpleboard.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionController {

    @GetMapping(value = "/error-codes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, ErrorCodeResponse> findEnums() {
        Map<String, ErrorCodeResponse> map = new HashMap<>();

        for (ErrorCode errorCode : ErrorCode.values()) {
            map.put(errorCode.name(), new ErrorCodeResponse(errorCode));
        }

        return map;
    }

    protected static class ErrorCodeResponse {
        private final String name;
        private final String message;
        private final int statusCode;

        public String getName() {
            return name;
        }

        public String getMessage() {
            return message;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public ErrorCodeResponse(ErrorCode errorCode) {
            this.name = errorCode.name();
            this.message = errorCode.getMessage();
            this.statusCode = errorCode.getHttpStatus().value();
        }
    }
}
