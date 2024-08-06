package assignment.simpleboard;

import assignment.simpleboard.schedule.controller.ScheduleController;
import assignment.simpleboard.schedule.dto.CreateScheduleDto;
import assignment.simpleboard.schedule.dto.ScheduleDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PingController.class)
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class PingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPing() throws Exception {
        // when && then
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));

        // docs
        result.andDo(document("health-check",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
    }
}
