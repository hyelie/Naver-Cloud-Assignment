package assignment.simpleboard.schedule.repository;

import assignment.simpleboard.schedule.entity.Schedule;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ScheduleRepositoryTest {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Test
    void testFindByStartTimeBetweenOrEndTimeBetween() {
        // given
        Schedule schedule1 = Schedule.builder()
                .name("test-schedule-1")
                .startTime(LocalDateTime.of(2024, 8, 1, 10, 0))
                .endTime(LocalDateTime.of(2024, 8, 1, 12, 0))
                .build();
        scheduleRepository.save(schedule1);

        Schedule schedule2 = Schedule.builder()
                .name("test-schedule-1")
                .startTime(LocalDateTime.of(2024, 8, 2, 14, 0))
                .endTime(LocalDateTime.of(2024, 8, 2, 16, 0))
                .build();
        scheduleRepository.save(schedule2);

        Schedule schedule3 = Schedule.builder()
                .name("test-schedule-1")
                .startTime(LocalDateTime.of(2024, 9, 2, 14, 0))
                .endTime(LocalDateTime.of(2024, 9, 2, 16, 0))
                .build();
        scheduleRepository.save(schedule3);

        // when
        LocalDateTime startDateStart = LocalDateTime.of(2024, 8, 1, 0, 0);
        LocalDateTime startDateEnd = LocalDateTime.of(2024, 8, 1, 23, 59);
        LocalDateTime endDateStart = LocalDateTime.of(2024, 8, 2, 0, 0);
        LocalDateTime endDateEnd = LocalDateTime.of(2024, 8, 2, 23, 59);

        List<Schedule> schedules = scheduleRepository.findByStartTimeBetweenOrEndTimeBetween(startDateStart, startDateEnd, endDateStart, endDateEnd);

        // then
        assertThat(schedules).hasSize(2);
        assertThat(schedules).contains(schedule1, schedule2);
        assertThat(schedules).doesNotContain(schedule3);
        assertThat(scheduleRepository.findAll()).hasSize(3);
    }
}