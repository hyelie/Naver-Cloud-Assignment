package assignment.simpleboard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {
    @GetMapping("/")
    public ResponseEntity<Object> ping() {
        return ResponseEntity.ok("OK");
    }
}
