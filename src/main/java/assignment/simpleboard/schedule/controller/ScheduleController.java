package assignment.simpleboard.schedule.controller;

import assignment.simpleboard.schedule.dto.*;
import assignment.simpleboard.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// controller layer : 입력 검증 & 출력 형식 맞춰서 리턴 & service layer 호출

@RestController
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/schedule")
    public ResponseEntity<Object> createSchedule(@RequestBody @Valid CreateScheduleDto createScheduleDto) {
        ScheduleDto createdScheduleDto = scheduleService.create(createScheduleDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/schedule")
    public ResponseEntity<ScheduleDto> updateSchedule(@RequestBody @Valid UpdateScheduleDto updateScheduleDto) {
        ScheduleDto updatedScheduleDto = scheduleService.update(updateScheduleDto);
        return new ResponseEntity<>(updatedScheduleDto, HttpStatus.OK);
    }

    @DeleteMapping("/schedule")
    public ResponseEntity<Object> deleteSchedule(@RequestBody @Valid DeleteScheduleDto deleteScheduleDto) {
        scheduleService.delete(deleteScheduleDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/schedule/{id}")
    public ResponseEntity<ScheduleDto> getDetailedSchedule(@PathVariable("id") @Valid String id){
        ScheduleDto scheduleDto = scheduleService.getDetailedSchedule(id);
        return new ResponseEntity<>(scheduleDto, HttpStatus.OK);
    }

    @GetMapping("/schedule")
    public ResponseEntity<Object> getMonthlySchedules(@RequestParam("year") Integer year, @RequestParam("month") Integer month){
        Map<String, Object> result = new HashMap<>();
        List<SimpleScheduleDto> schedules = scheduleService.getMonthlySchedules(year, month);
        result.put("schedules", schedules);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}