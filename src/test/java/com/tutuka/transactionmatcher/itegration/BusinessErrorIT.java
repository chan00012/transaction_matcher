package com.tutuka.transactionmatcher.itegration;

import com.tutuka.transactionmatcher.repository.ReportRepository;
import com.tutuka.transactionmatcher.utils.Constants;
import com.tutuka.transactionmatcher.utils.TestUtils;
import com.tutuka.transactionmatcher.utils.enums.ReportStatus;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BusinessErrorIT extends BaseItegrationTest {

    @MockBean
    private ReportRepository reportRepository;

    @Test
    void getNonExistingMatchReport() throws Exception {
        when(reportRepository.get(Mockito.anyString())).thenReturn(null);
        String URI = GET_MATCH_URI + "?rrn=" + 1122334455;

        mockMvc.perform(MockMvcRequestBuilders.get(URI))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(Constants.JSON_STATUS, is(Constants.ERROR)))
                .andExpect(jsonPath(Constants.JSON_CODE, is(Constants.NON_EXISTITING_ERRCODE)))
                .andExpect(jsonPath(Constants.JSON_ERR_REF, is(Matchers.any(String.class))))
                .andExpect(jsonPath(Constants.JSON_ERR_MSG, is(Matchers.any(String.class))));
    }

    @Test
    void getNonExistingUnmatchedReport() throws Exception {
        when(reportRepository.get(Mockito.anyString())).thenReturn(null);
        String URI = GET_UNMATCH_URI + "?rrn=" + 1122334455;

        mockMvc.perform(MockMvcRequestBuilders.get(URI))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(Constants.JSON_STATUS, is(Constants.ERROR)))
                .andExpect(jsonPath(Constants.JSON_CODE, is(Constants.NON_EXISTITING_ERRCODE)))
                .andExpect(jsonPath(Constants.JSON_ERR_REF, is(Matchers.any(String.class))))
                .andExpect(jsonPath(Constants.JSON_ERR_MSG, is(Matchers.any(String.class))));
    }

    @Test
    void getPendingMatchReport() throws Exception {
        when(reportRepository.get(Mockito.anyString())).thenReturn(TestUtils.getReportEntityResponse(ReportStatus.PENDING));
        String URI = GET_MATCH_URI + "?rrn=" + 1122334455;

        mockMvc.perform(MockMvcRequestBuilders.get(URI))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(Constants.JSON_STATUS, is(Constants.SUCCESS)))
                .andExpect(jsonPath(Constants.JSON_CODE, is(Constants.SUCCESS_CODE)))
                .andExpect(jsonPath(Constants.JSON_DATA_REF, is(Matchers.any(String.class))))
                .andExpect(jsonPath(Constants.JSON_DATA_MSG, is(Matchers.any(String.class))));
    }

    @Test
    void getPendingUnmatchedReport() throws Exception {
        when(reportRepository.get(Mockito.anyString())).thenReturn(TestUtils.getReportEntityResponse(ReportStatus.PENDING));
        String URI = GET_UNMATCH_URI + "?rrn=" + 1122334455;

        mockMvc.perform(MockMvcRequestBuilders.get(URI))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(Constants.JSON_STATUS, is(Constants.SUCCESS)))
                .andExpect(jsonPath(Constants.JSON_CODE, is(Constants.SUCCESS_CODE)))
                .andExpect(jsonPath(Constants.JSON_DATA_REF, is(Matchers.any(String.class))))
                .andExpect(jsonPath(Constants.JSON_DATA_MSG, is(Matchers.any(String.class))));
    }

    @Test
    void getFailedMatchReport() throws Exception {
        when(reportRepository.get(Mockito.anyString())).thenReturn(TestUtils.getReportEntityResponse(ReportStatus.FAILED));
        String URI = GET_MATCH_URI + "?rrn=" + 1122334455;

        mockMvc.perform(MockMvcRequestBuilders.get(URI))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(Constants.JSON_STATUS, is(Constants.ERROR)))
                .andExpect(jsonPath(Constants.JSON_CODE, is(Constants.FAILED_REPORT_ERRCODE)))
                .andExpect(jsonPath(Constants.JSON_ERR_REF, is(Matchers.any(String.class))))
                .andExpect(jsonPath(Constants.JSON_ERR_MSG, is(Matchers.any(String.class))));
    }

    @Test
    void getFailedUnmatchReport() throws Exception {
        when(reportRepository.get(Mockito.anyString())).thenReturn(TestUtils.getReportEntityResponse(ReportStatus.FAILED));
        String URI = GET_UNMATCH_URI + "?rrn=" + 1122334455;

        mockMvc.perform(MockMvcRequestBuilders.get(URI))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(Constants.JSON_STATUS, is(Constants.ERROR)))
                .andExpect(jsonPath(Constants.JSON_CODE, is(Constants.FAILED_REPORT_ERRCODE)))
                .andExpect(jsonPath(Constants.JSON_ERR_REF, is(Matchers.any(String.class))))
                .andExpect(jsonPath(Constants.JSON_ERR_MSG, is(Matchers.any(String.class))));
    }

    @Test
    void invalidFileTypes() throws Exception {
        File file1 = TestUtils.getFile("files/invalid_file.pdf");
        File file2 = TestUtils.getFile("files/tutukamarkofffile20140113.csv");

        referenceFile = new MockMultipartFile("referenceFile", file1.getName(),
                "pdf", FileUtils.readFileToByteArray(file1));
        compareFile = new MockMultipartFile("compareFile", file2.getName(),
                "text/csv", FileUtils.readFileToByteArray(file2));

        mockMvc.perform(MockMvcRequestBuilders.multipart(GENERATE_URI).file(referenceFile).file(compareFile))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(Constants.JSON_STATUS, is(Constants.ERROR)))
                .andExpect(jsonPath(Constants.JSON_CODE, is(Constants.INVALID_FILETYPE_ERRCODE)))
                .andExpect(jsonPath(Constants.JSON_ERR_REF, is(any(String.class))))
                .andExpect(jsonPath(Constants.JSON_ERR_MSG, is(any(String.class))));
    }

    @Test
    void getUnMatchReport_unrecognizedColumn() throws Exception {
        File file1 = TestUtils.getFile("files/unrecog_column_file.csv");
        File file2 = TestUtils.getFile("files/tutukamarkofffile20140113.csv");

        referenceFile = new MockMultipartFile("referenceFile", file1.getName(),
                "pdf", FileUtils.readFileToByteArray(file1));
        compareFile = new MockMultipartFile("compareFile", file2.getName(),
                "text/csv", FileUtils.readFileToByteArray(file2));

        mockMvc.perform(MockMvcRequestBuilders.multipart(GENERATE_URI).file(referenceFile).file(compareFile))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(Constants.JSON_STATUS, is(Constants.ERROR)))
                .andExpect(jsonPath(Constants.JSON_CODE, is(Constants.UNRECOG_COLUMN_ERRCODE)))
                .andExpect(jsonPath(Constants.JSON_ERR_REF, is(any(String.class))))
                .andExpect(jsonPath(Constants.JSON_ERR_MSG, is(any(String.class))));
    }

    @Test
    void getUnmatchReport_emptyFile() throws Exception {
        File file1 = TestUtils.getFile("files/empty_file.csv");
        File file2 = TestUtils.getFile("files/empty_file.csv");

        referenceFile = new MockMultipartFile("referenceFile", file1.getName(),
                "text/csv", FileUtils.readFileToByteArray(file1));
        compareFile = new MockMultipartFile("compareFile", file2.getName(),
                "text/csv", FileUtils.readFileToByteArray(file2));

        mockMvc.perform(MockMvcRequestBuilders.multipart(GENERATE_URI).file(referenceFile).file(compareFile))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(Constants.JSON_STATUS, is(Constants.ERROR)))
                .andExpect(jsonPath(Constants.JSON_CODE, is(Constants.EMPTY_HEADER_ERRCODE)))
                .andExpect(jsonPath(Constants.JSON_ERR_REF, is(any(String.class))))
                .andExpect(jsonPath(Constants.JSON_ERR_MSG, is(any(String.class))));
    }
}
