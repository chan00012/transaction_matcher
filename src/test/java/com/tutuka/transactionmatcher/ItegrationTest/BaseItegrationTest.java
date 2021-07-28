package com.tutuka.transactionmatcher.ItegrationTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutuka.transactionmatcher.dto.response.ReportReferenceNumber;
import com.tutuka.transactionmatcher.dto.response.Response;
import com.tutuka.transactionmatcher.utils.TestUtils;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseItegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;
    protected static final String GENERATE_URI = "/v1/reports/generate";
    protected static final String GET_MATCH_URI = "/v1/reports/match";
    protected static final String GET_UNMATCH_URI = "/v1/reports/unmatch";

    protected MockMultipartFile referenceFile;
    protected MockMultipartFile compareFile;

    @Bean
    private ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @BeforeAll
    public final void setup() throws IOException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        File file1 = TestUtils.getFile("clientmarkofffile20140113.csv");
        File file2 = TestUtils.getFile("tutukamarkofffile20140113.csv");

        referenceFile = new MockMultipartFile("referenceFile", file1.getName(),
                "text/csv", FileUtils.readFileToByteArray(file1));
        compareFile = new MockMultipartFile("compareFile", file2.getName(),
                "text/csv", FileUtils.readFileToByteArray(file2));
    }

    public Response<ReportReferenceNumber> generateRrn() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart(GENERATE_URI).file(referenceFile).file(compareFile))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        return objectMapper().readValue(result.getResponse().getContentAsString(), new TypeReference<Response<ReportReferenceNumber>>() {
        });
    }
}
