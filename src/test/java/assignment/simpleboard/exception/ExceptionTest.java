package assignment.simpleboard.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class })
@WebMvcTest(controllers = ExceptionController.class)
public class ExceptionTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testErrorResponses() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/error-codes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // spring rest docs
        result.andDo(MockMvcRestDocumentation.document("errorcode-response",
                customResponseFields("errorcode-response",
                        fieldDescriptors())));
    }

    private List<FieldDescriptor> fieldDescriptors() {
        List<FieldDescriptor> fieldDescriptors = new ArrayList<>();

        for (ErrorCode errorCode : ErrorCode.values()) {
            FieldDescriptor attributes = fieldWithPath(errorCode.name()).type(JsonFieldType.OBJECT)
                    .attributes(
                            Attributes.key("code").value(errorCode.name()),
                            Attributes.key("msg").value(errorCode.getMessage()),
                            Attributes.key("statusCode").value(errorCode.getHttpStatus().value()),
                            Attributes.key("statusMessage").value(errorCode.getHttpStatus().getReasonPhrase()));
            fieldDescriptors.add(attributes);
        }

        return fieldDescriptors;
    }

    public static CustomResponseFieldsSnippet customResponseFields(
            String snippetFilePrefix,
            List<FieldDescriptor> fieldDescriptors) {
        return new CustomResponseFieldsSnippet(snippetFilePrefix, fieldDescriptors, true);
    }
}