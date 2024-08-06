package assignment.simpleboard.schedule.dto;

import assignment.simpleboard.schedule.entity.Schedule;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class SimpleScheduleDto {
    String name;
    LocalDateTime startTime;
    LocalDateTime endTime;
    Boolean finished;
    String category;

    public SimpleScheduleDto(Schedule schedule){
        this.name = schedule.getName();
        this.startTime = schedule.getStartTime();
        this.endTime = schedule.getEndTime();
        this.finished = schedule.getFinished();
        this.category = schedule.getCategory();
    }
}
