package assignment.simpleboard.schedule.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CreateScheduleDto {
    @NotNull(message = "The name field must exist.")
    @Size(max = 64, message = "The name field must be 64 characters or less in length.")
    private String name;

    @Builder.Default
    private LocalDateTime startTime = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime endTime = LocalDateTime.now();

    @Builder.Default
    private Boolean finished = false;

    @Builder.Default
    private String memo = "";

    @Builder.Default
    private String location = "";

    @Size(max = 16, message = "The category field must be 16 characters or less in length.")
    @Builder.Default
    private String category = "";
}
