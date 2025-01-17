package com.tutuka.transactionmatcher.itegration;

import com.tutuka.transactionmatcher.dto.response.ReportReferenceNumber;
import com.tutuka.transactionmatcher.dto.response.Response;
import com.tutuka.transactionmatcher.repository.ReportRepository;
import com.tutuka.transactionmatcher.utils.Constants;
import com.tutuka.transactionmatcher.utils.TestUtils;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SuccessIT extends BaseItegrationTest {

    @Autowired
    private ReportRepository reportRepository;

    @Test
    void testGenerateSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(GENERATE_URI).file(referenceFile).file(compareFile))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(Constants.JSON_STATUS, is(Constants.SUCCESS)))
                .andExpect(jsonPath(Constants.JSON_CODE, is(Constants.SUCCESS_CODE)))
                .andExpect(jsonPath(Constants.JSON_RRN, is(any(String.class))));
    }

    @Test
    void testGetMatchSuccess() throws Exception {
        Response<ReportReferenceNumber> response = generateRrn();
        String URI = GET_MATCH_URI + "?rrn=" + response.getData().getRrn();

        await().atMost(60, TimeUnit.SECONDS).until(() ->
                Objects.nonNull(reportRepository.get(response.getData().getRrn()).getUnmatchedReport()));

        mockMvc.perform(MockMvcRequestBuilders.get(URI))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(TestUtils.readJson("response/match/success_match_report_response.json")));
    }

    @Test
    void testUnmatchSuccess() throws Exception {
        Response<ReportReferenceNumber> response = generateRrn();
        String URI = GET_UNMATCH_URI + "?rrn=" + response.getData().getRrn();

        await().atMost(60, TimeUnit.SECONDS).until(() ->
                Objects.nonNull(reportRepository.get(response.getData().getRrn()).getUnmatchedReport()));

        mockMvc.perform(MockMvcRequestBuilders.get(URI))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(TestUtils.readJson("response/unmatch/success_unmatch_report_response.json")));
    }

    @Test
    void testGetMatchSuccess_multipleDuplicateEntries() throws Exception {
        getMatchReport("multiple_duplicate_file1.csv", "multiple_duplicate_file2.csv",
                "multiple_duplicate_match_report_response.json");
    }

    @Test
    void testGetUnmatchSuccess_multipleDuplicateEntries() throws Exception {
        getUnmatchReport("multiple_duplicate_file1.csv", "multiple_duplicate_file2.csv",
                "multiple_duplicate_unmatch_report_response.json");

    }

    @Test
    void testGetMatchSuccess_headerOnly() throws Exception {
        getMatchReport("success_file1.csv", "header_only_file.csv",
                "header_only_match_report_response.json");
    }

    @Test
    void testGetUnmatchSuccess_headerOnly() throws Exception {
        getUnmatchReport("success_file1.csv", "header_only_file.csv",
                "header_only_unmatch_report_response.json");
    }

    @Test
    void testGetMatchSuccess_invalidEntry() throws Exception {
        getMatchReport("success_file1.csv", "invalid_entry_file.csv",
                "invalid_entry_match_report_response.json");
    }

    @Test
    void testGetUnmatchSuccess_invalidEntry() throws Exception {
        getUnmatchReport("success_file1.csv", "invalid_entry_file.csv",
                "invalid_entry_unmatch_report_response.json");
    }

    @Test
    void testGetMatchSuccess_excessEntry() throws Exception {
        getMatchReport("success_file1.csv", "excess_entry_file.csv",
                "excess_entry_match_report_response.json");
    }

    @Test
    void testGetUnmatchSuccess_excessEntry() throws Exception {
        getUnmatchReport("success_file1.csv", "excess_entry_file.csv",
                "excess_entry_unmatch_report_response.json");
    }

    @Test
    void testGetMatchSuccess_diffHeaderCase() throws Exception {
        getMatchReport("success_file1.csv", "different_header_case_file.csv",
                "different_header_case_match_report.json");
    }

    @Test
    void testGetUnmatchSuccess_diffHeaderCase() throws Exception {
        getUnmatchReport("success_file1.csv", "different_header_case_file.csv",
                "different_header_case_unmatch_report.json");
    }

    @Test
    void testGetMatchSuccess_notInOrderHeader() throws Exception {
        getMatchReport("not_inorder_header_file.csv", "different_header_case_file.csv",
                "not_inorder_header_match_response.json");
    }

    @Test
    void testGetUnmatchSuccess_notInOrderHeader() throws Exception {
        getUnmatchReport("not_inorder_header_file.csv", "different_header_case_file.csv",
                "not_inorder_header_unmatch_response.json");
    }


    private void getUnmatchReport(String filename1, String filename2, String responseFilename) throws Exception {
        File file1 = TestUtils.getFile("files/" + filename1);
        File file2 = TestUtils.getFile("files/" + filename2);

        referenceFile = new MockMultipartFile("referenceFile", file1.getName(),
                "text/csv", FileUtils.readFileToByteArray(file1));
        compareFile = new MockMultipartFile("compareFile", file2.getName(),
                "text/csv", FileUtils.readFileToByteArray(file2));

        Response<ReportReferenceNumber> response = generateRrn();
        String URI = GET_UNMATCH_URI + "?rrn=" + response.getData().getRrn();

        await().atMost(60, TimeUnit.SECONDS).until(() ->
                Objects.nonNull(reportRepository.get(response.getData().getRrn()).getUnmatchedReport()));

        mockMvc.perform(MockMvcRequestBuilders.get(URI))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(TestUtils.readJson("response/unmatch/" + responseFilename)));
    }

    private void getMatchReport(String filename1, String filename2, String responseFilename) throws Exception {
        File file1 = TestUtils.getFile("files/" + filename1);
        File file2 = TestUtils.getFile("files/" + filename2);

        referenceFile = new MockMultipartFile("referenceFile", file1.getName(),
                "text/csv", FileUtils.readFileToByteArray(file1));
        compareFile = new MockMultipartFile("compareFile", file2.getName(),
                "text/csv", FileUtils.readFileToByteArray(file2));

        Response<ReportReferenceNumber> response = generateRrn();
        String URI = GET_MATCH_URI + "?rrn=" + response.getData().getRrn();
        
        await().atMost(60, TimeUnit.SECONDS).until(() ->
                Objects.nonNull(reportRepository.get(response.getData().getRrn()).getUnmatchedReport()));

        mockMvc.perform(MockMvcRequestBuilders.get(URI))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(TestUtils.readJson("response/match/" + responseFilename)));
    }
}
