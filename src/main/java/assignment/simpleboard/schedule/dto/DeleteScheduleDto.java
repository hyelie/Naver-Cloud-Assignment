package assignment.simpleboard.schedule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteScheduleDto {
    @NotNull(message = "The id field must exist.")
    String id;
}
