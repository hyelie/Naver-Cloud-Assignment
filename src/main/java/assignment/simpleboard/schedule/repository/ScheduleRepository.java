package assignment.simpleboard.schedule.repository;

import assignment.simpleboard.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<Schedule, String> {
    List<Schedule> findByStartTimeBetweenOrEndTimeBetween(LocalDateTime startDateStart, LocalDateTime startDateEnd, LocalDateTime endDateStart, LocalDateTime endDateEnd);
}