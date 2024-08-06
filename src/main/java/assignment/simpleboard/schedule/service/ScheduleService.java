package assignment.simpleboard.schedule.service;

import assignment.simpleboard.exception.CustomException;
import assignment.simpleboard.exception.ErrorCode;
import assignment.simpleboard.schedule.dto.*;
import assignment.simpleboard.schedule.entity.Schedule;
import assignment.simpleboard.schedule.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

// service layer : repository layer 호출 & data logic (DTO <-> Entity 변환)

@Service
@Transactional
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public ScheduleDto create(CreateScheduleDto scheduleDto) {
        Schedule schedule = Schedule.builder()
                .name(scheduleDto.getName())
                .startTime(scheduleDto.getStartTime())
                .endTime(scheduleDto.getEndTime())
                .lastUpdateTime(LocalDateTime.now())
                .finished(scheduleDto.getFinished())
                .memo(scheduleDto.getMemo())
                .location(scheduleDto.getLocation())
                .category(scheduleDto.getCategory())
                .build();

        scheduleRepository.save(schedule);
        return new ScheduleDto(schedule);
    }

    public ScheduleDto update(UpdateScheduleDto scheduleDto) {
        Schedule schedule = scheduleRepository.findById(scheduleDto.getId()).orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        schedule.updateField(scheduleDto.getName(),
                scheduleDto.getStartTime(),
                scheduleDto.getEndTime(),
                LocalDateTime.now(),
                schedule.getFinished(),
                scheduleDto.getMemo(),
                scheduleDto.getLocation(),
                scheduleDto.getCategory());

        scheduleRepository.save(schedule);
        return new ScheduleDto(schedule);
    }

    public void delete(DeleteScheduleDto scheduleDto) {
        String id = scheduleDto.getId();
        if(!scheduleRepository.existsById(id)){
            throw new CustomException(ErrorCode.SCHEDULE_NOT_FOUND);
        }

        scheduleRepository.deleteById(id);
    }

    public ScheduleDto getDetailedSchedule(String id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));
        return new ScheduleDto(schedule);
    }

    public List<SimpleScheduleDto> getMonthlySchedules(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDate endDate = LocalDate.of(year, month, startDate.lengthOfMonth());
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        return scheduleRepository.findByStartTimeBetweenOrEndTimeBetween(startDateTime, endDateTime, startDateTime, endDateTime)
                .stream()
                .map(SimpleScheduleDto::new)
                .collect(Collectors.toList());
    }
}