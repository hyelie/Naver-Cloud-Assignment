package assignment.simpleboard.schedule.service;

import assignment.simpleboard.exception.CustomException;
import assignment.simpleboard.exception.ErrorCode;
import assignment.simpleboard.schedule.dto.*;
import assignment.simpleboard.schedule.entity.Schedule;
import assignment.simpleboard.schedule.repository.ScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ScheduleServiceTest {
    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    @Test
    void testCreateSchedule() {
        // given
        CreateScheduleDto createScheduleDto = CreateScheduleDto.builder()
                                                                .name("test-schedule")
                                                                .startTime(LocalDateTime.now())
                                                                .endTime(LocalDateTime.now().plusHours(1))
                                                                .finished(false)
                                                                .memo("test-memo")
                                                                .location("test-location")
                                                                .category("test-category")
                                                                .build();

        Schedule schedule = Schedule.builder()
                .name(createScheduleDto.getName())
                .startTime(createScheduleDto.getStartTime())
                .endTime(createScheduleDto.getEndTime())
                .lastUpdateTime(LocalDateTime.now())
                .finished(createScheduleDto.getFinished())
                .memo(createScheduleDto.getMemo())
                .location(createScheduleDto.getLocation())
                .category(createScheduleDto.getCategory())
                .build();

        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        // when
        ScheduleDto result = scheduleService.create(createScheduleDto);

        // then
        assertNotNull(result);
        assertEquals(createScheduleDto.getName(), result.getName());
        verify(scheduleRepository, times(1)).save(any(Schedule.class));
    }

    @Test
    void testDeleteSchedule() {
        // given
        String id = "test-id";
        DeleteScheduleDto deleteScheduleDto = DeleteScheduleDto.builder().id(id).build();
        when(scheduleRepository.existsById(id)).thenReturn(true);

        // when
        scheduleService.delete(deleteScheduleDto);

        // then
        verify(scheduleRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteSchedule_NotFound() {
        // given
        String id = "test-id";
        DeleteScheduleDto deleteScheduleDto = DeleteScheduleDto.builder().id(id).build();
        when(scheduleRepository.findById(id)).thenReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class, () -> scheduleService.delete(deleteScheduleDto));

        // then
        assertEquals(ErrorCode.SCHEDULE_NOT_FOUND, exception.getErrorCode());
        verify(scheduleRepository, never()).deleteById(id);
    }

    @Test
    void testUpdateSchedule() {
        // given
        UpdateScheduleDto updateScheduleDto = UpdateScheduleDto.builder()
                                                                .id("test-id")
                                                                .name(("updated-schedule"))
                                                                .startTime(LocalDateTime.now())
                                                                .endTime(LocalDateTime.now().plusHours(1))
                                                                .finished(false)
                                                                .memo(("updated-memo"))
                                                                .location(("updated-location"))
                                                                .category(("updated-category"))
                                                                .build();

        Schedule schedule = new Schedule();
        when(scheduleRepository.findById(updateScheduleDto.getId())).thenReturn(Optional.of(schedule));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        // when
        ScheduleDto result = scheduleService.update(updateScheduleDto);

        // then
        assertNotNull(result);
        assertEquals(updateScheduleDto.getName(), result.getName());
        verify(scheduleRepository, times(1)).findById(updateScheduleDto.getId());
        verify(scheduleRepository, times(1)).save(any(Schedule.class));
    }

    @Test
    void updateSchedule_NotFound() {
        // given
        UpdateScheduleDto updateScheduleDto = UpdateScheduleDto.builder().id("test-id").build();

        when(scheduleRepository.findById(updateScheduleDto.getId())).thenReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class, () -> scheduleService.update(updateScheduleDto));

        // then
        assertEquals(ErrorCode.SCHEDULE_NOT_FOUND, exception.getErrorCode());
        verify(scheduleRepository, times(1)).findById(updateScheduleDto.getId());
        verify(scheduleRepository, never()).save(any(Schedule.class));
    }

    @Test
    void getDetailedSchedule() {
        // given
        String id = "test-id";
        Schedule schedule = new Schedule();
        when(scheduleRepository.findById(id)).thenReturn(Optional.of(schedule));

        // when
        ScheduleDto result = scheduleService.getDetailedSchedule(id);

        // then
        assertNotNull(result);
        verify(scheduleRepository, times(1)).findById(id);
    }

    @Test
    void getDetailedSchedule_NotFound() {
        // given
        String id = "test-id";
        when(scheduleRepository.findById(id)).thenReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class, () -> scheduleService.getDetailedSchedule(id));

        // then
        assertEquals(ErrorCode.SCHEDULE_NOT_FOUND, exception.getErrorCode());
        verify(scheduleRepository, times(1)).findById(id);
    }

    @Test
    void getMonthlySchedules() {
        // given
        int year = 2024;
        int month = 8;
        List<Schedule> schedules = List.of(new Schedule());

        when(scheduleRepository.findByStartTimeBetweenOrEndTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(schedules);

        // when
        List<SimpleScheduleDto> result = scheduleService.getMonthlySchedules(year, month);

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(scheduleRepository, times(1)).findByStartTimeBetweenOrEndTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class), any(LocalDateTime.class), any(LocalDateTime.class));
    }
}

