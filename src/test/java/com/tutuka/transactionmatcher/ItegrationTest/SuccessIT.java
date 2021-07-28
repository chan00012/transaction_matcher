package com.tutuka.transactionmatcher.ItegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutuka.transactionmatcher.dto.response.ReportReferenceNumber;
import com.tutuka.transactionmatcher.dto.response.Response;
import com.tutuka.transactionmatcher.repository.ReportRepository;
import com.tutuka.transactionmatcher.utils.Constants;
import com.tutuka.transactionmatcher.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SuccessIT extends BaseItegrationTest {

    @Autowired
    private ReportRepository reportRepository;

    @Test
    void generateSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(GENERATE_URI).file(referenceFile).file(compareFile))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(Constants.JSON_STATUS, is(Constants.SUCCESS)))
                .andExpect(jsonPath(Constants.JSON_CODE, is(Constants.SUCCESS_CODE)))
                .andExpect(jsonPath(Constants.JSON_DATA, is(notNullValue())))
                .andExpect(jsonPath(Constants.JSON_RRN, is(any(String.class))));
    }

    @Test
    void getMatchSuccess() throws Exception {
        Response<ReportReferenceNumber> response = generateRrn();
        String URI = GET_MATCH_URI + "?rrn=" + response.getData().getRrn();

        await().until(() ->
                Objects.nonNull(reportRepository.get(response.getData().getRrn()).getMatchReport()));

        mockMvc.perform(MockMvcRequestBuilders.get(URI))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(TestUtils.readJson("response/success_match_report_response.json")));
    }

    @Test
    void getUnmatchSuccess() throws Exception {
        Response<ReportReferenceNumber> response = generateRrn();
        String URI = GET_UNMATCH_URI + "?rrn=" + response.getData().getRrn();

        await().until(() ->
                Objects.nonNull(reportRepository.get(response.getData().getRrn()).getUnmatchedReport()));

        mockMvc.perform(MockMvcRequestBuilders.get(URI))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(TestUtils.readJson("response/success_unmatch_report_response.json")));
    }

}
