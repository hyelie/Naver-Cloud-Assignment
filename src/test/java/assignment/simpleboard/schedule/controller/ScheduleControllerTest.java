package assignment.simpleboard.schedule.controller;

import assignment.simpleboard.exception.CustomException;
import assignment.simpleboard.exception.ErrorCode;
import assignment.simpleboard.schedule.dto.*;
import assignment.simpleboard.schedule.entity.Schedule;
import assignment.simpleboard.schedule.repository.ScheduleRepository;
import assignment.simpleboard.schedule.service.ScheduleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScheduleController.class)
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class ScheduleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleRepository scheduleRepository;

    @MockBean
    private ScheduleService scheduleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateSchedule() throws Exception {
        // given
        CreateScheduleDto createDto = CreateScheduleDto.builder()
                .name("test-schedule")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .finished(false)
                .memo("test-memo")
                .location("test-location")
                .category("test-category")
                .build();

        ScheduleDto createdDto = ScheduleDto.builder()
                .id(UUID.randomUUID().toString())
                .name("test-schedule")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .lastUpdateTime(LocalDateTime.now())
                .finished(false)
                .memo("test-memo")
                .location("test-location")
                .category("test-category")
                .build();

        when(scheduleService.create(any(CreateScheduleDto.class))).thenReturn(createdDto);

        // when && then
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                        .andExpect((status().isCreated()));

        // docs
        result.andDo(document("create-schedule",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath("name").description("일정 이름").attributes(
                                Attributes.key("constraints").value("길이 1 이상 64 이하")
                        ),
                        fieldWithPath("startTime").description("시작 시간").optional(),
                        fieldWithPath("endTime").description("종료 시간").optional(),
                        fieldWithPath("finished").description("완료 여부").optional(),
                        fieldWithPath("memo").description("메모").optional(),
                        fieldWithPath("location").description("위치").optional(),
                        fieldWithPath("category").description("카테고리").optional().attributes(
                                Attributes.key("constraints").value("길이 16 이하")
                        )
                )));
    }

    @Test
    public void testUpdateSchedule() throws Exception {
        // given && when
        String name = "updated-schedule";
        String id = UUID.randomUUID().toString();
        Schedule schedule = Schedule.builder()
                                        .id(id)
                                        .name(name)
                                        .startTime(LocalDateTime.now())
                                        .endTime(LocalDateTime.now().plusHours(2))
                                        .lastUpdateTime(LocalDateTime.now())
                                        .finished(true)
                                        .memo("memo")
                                        .location("location")
                                        .category("category")
                                        .build();

        UpdateScheduleDto updateDto = UpdateScheduleDto.builder()
                .id(id)
                .name(name)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .finished(true)
                .memo("updated-memo")
                .location("updated-location")
                .category("updated-category")
                .build();

        ScheduleDto updatedDto = ScheduleDto.builder()
                                        .id(id)
                                        .name(name)
                                        .startTime(LocalDateTime.now())
                                        .endTime(LocalDateTime.now().plusHours(2))
                                        .finished(true)
                                        .memo("updated-memo")
                                        .location("updated-location")
                                        .category("updated-category")
                                        .build();

        when(scheduleRepository.findById(id)).thenReturn(Optional.of(schedule));
        when(scheduleService.update(any(UpdateScheduleDto.class))).thenReturn(updatedDto);

        // when && then
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.put("/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                        .andExpect((status().isOk()))
                        .andExpect(jsonPath("$.name").value(name));

        // docs
        result.andDo(document("update-schedule",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath("id").description("일정 ID"),
                        fieldWithPath("name").description("일정 이름").attributes(
                                Attributes.key("constraints").value("길이 1 이상 64 이하")
                        ),
                        fieldWithPath("startTime").description("시작 시간").optional(),
                        fieldWithPath("endTime").description("종료 시간").optional(),
                        fieldWithPath("finished").description("완료 여부").optional(),
                        fieldWithPath("memo").description("메모").optional(),
                        fieldWithPath("location").description("위치").optional(),
                        fieldWithPath("category").description("카테고리").optional().attributes(
                                Attributes.key("constraints").value("길이 16 이하")
                        )
                ),
                responseFields(
                        fieldWithPath("id").description("일정 ID"),
                        fieldWithPath("name").description("수정 후 일정 이름"),
                        fieldWithPath("startTime").description("수정 후 시작 시간"),
                        fieldWithPath("endTime").description("수정 후 종료 시간"),
                        fieldWithPath("lastUpdateTime").description("수정 시간"),
                        fieldWithPath("finished").description("수정 후 완료 여부"),
                        fieldWithPath("memo").description("수정 후 메모"),
                        fieldWithPath("location").description("수정 후 위치"),
                        fieldWithPath("category").description("수정 후 카테고리")
                )));
    }

    @Test
    public void testUpdateInvalidSchedule() throws Exception {
        // given && when
        UpdateScheduleDto updateDto = UpdateScheduleDto.builder()
                                                        .id(UUID.randomUUID().toString())
                                                        .name("update-name")
                                                        .build();

        when(scheduleService.update(any(UpdateScheduleDto.class))).thenThrow(new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        // when && then
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.put("/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                        .andExpect((status().isNotFound()));

        // docs
        result.andDo(document("update-schedule/not-found",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    @Test
    public void testDeleteSchedule() throws Exception {
        // given
        String id = UUID.randomUUID().toString();
        DeleteScheduleDto deleteDto = DeleteScheduleDto.builder()
                .id(id)
                .build();

        // when && then
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.delete("/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteDto)))
                        .andExpect(status().isNoContent());

        // docs
        result.andDo(document("delete-schedule",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                        fieldWithPath("id").description("삭제할 일정 ID")
                )));
    }

    @Test
    public void testDeleteInvalidSchedule() throws Exception {
        // given
        DeleteScheduleDto deleteDto = DeleteScheduleDto.builder()
                                                        .id(UUID.randomUUID().toString())
                                                        .build();

        doThrow(new CustomException(ErrorCode.SCHEDULE_NOT_FOUND)).when(scheduleService).delete(any(DeleteScheduleDto.class));

        // when && then
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.delete("/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteDto)))
                        .andExpect(status().isNotFound());

        // docs
        result.andDo(document("delete-schedule/not-found",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    @Test
    public void testGetDetailedSchedule() throws Exception {
        // given
        String id = UUID.randomUUID().toString();
        ScheduleDto scheduleDto = ScheduleDto.builder()
                .id(id)
                .name("test-schedule")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .lastUpdateTime(LocalDateTime.now())
                .finished(false)
                .memo("test-memo")
                .location("test-location")
                .category("test-category")
                .build();

        when(scheduleService.getDetailedSchedule(id)).thenReturn(scheduleDto);

        // when && then
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/schedule/{id}", id))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name").value("test-schedule"));

        // docs
        result.andDo(document("get-detailed-schedule",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("id").description("조회할 일정 ID")
                ),
                responseFields(
                        fieldWithPath("id").description("일정 ID"),
                        fieldWithPath("name").description("일정 이름"),
                        fieldWithPath("startTime").description("시작 시간"),
                        fieldWithPath("endTime").description("종료 시간"),
                        fieldWithPath("lastUpdateTime").description("마지막 업데이트 시간"),
                        fieldWithPath("finished").description("완료 여부"),
                        fieldWithPath("memo").description("메모"),
                        fieldWithPath("location").description("위치"),
                        fieldWithPath("category").description("카테고리")
                )));
    }

    @Test
    public void testGetInvalidDetailedSchedule() throws Exception {
        // given
        String id = UUID.randomUUID().toString();

        when(scheduleService.getDetailedSchedule(id)).thenThrow(new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        // when && then
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/schedule/{id}", id))
                        .andExpect(status().isNotFound());

        // docs
        result.andDo(document("get-detailed-schedule/not-found",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }

    @Test
    public void testGetMonthlySchedules() throws Exception {
        // given
        int year = 2024;
        int month = 8;
        SimpleScheduleDto scheduleDto1 = SimpleScheduleDto.builder()
                .id(UUID.randomUUID().toString())
                .name("test-schedule-1")
                .startTime(LocalDateTime.of(2024, 8, 1, 10, 0))
                .endTime(LocalDateTime.of(2024, 8, 1, 12, 0))
                .finished(false)
                .category("test-category-1")
                .build();
        SimpleScheduleDto scheduleDto2 = SimpleScheduleDto.builder()
                .id(UUID.randomUUID().toString())
                .name("test-schedule-2")
                .startTime(LocalDateTime.of(2024, 8, 15, 14, 0))
                .endTime(LocalDateTime.of(2024, 8, 15, 16, 0))
                .finished(true)
                .category("test-category-2")
                .build();

        when(scheduleService.getMonthlySchedules(year, month)).thenReturn(Arrays.asList(scheduleDto1, scheduleDto2));

        // when && then
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/schedule")
                        .param("year", String.valueOf(year))
                        .param("month", String.valueOf(month)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.schedules").isArray())
                        .andExpect(jsonPath("$.schedules[0].name").value("test-schedule-1"))
                        .andExpect(jsonPath("$.schedules[1].name").value("test-schedule-2"));

        // docs
        result.andDo(document("get-monthly-schedules",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                        parameterWithName("year").description("조회할 연도"),
                        parameterWithName("month").description("조회할 월")
                ),
                responseFields(
                        fieldWithPath("schedules").description("월별 일정 목록"),
                        fieldWithPath("schedules[].id").description("일정 ID"),
                        fieldWithPath("schedules[].name").description("일정 이름"),
                        fieldWithPath("schedules[].startTime").description("시작 시간"),
                        fieldWithPath("schedules[].endTime").description("종료 시간"),
                        fieldWithPath("schedules[].finished").description("완료 여부"),
                        fieldWithPath("schedules[].category").description("카테고리")
                )));
    }
}