package com.tutuka.transactionmatcher.itegration;

import com.tutuka.transactionmatcher.controller.ReportController;
import com.tutuka.transactionmatcher.utils.Constants;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ServerErrorIT extends BaseItegrationTest {

    @MockBean
    private ReportController reportController;

    @Test
    void testGenerateInternalServerError() throws Exception {
        when(reportController.generateReport(any(MockMultipartFile.class), any(MockMultipartFile.class)))
                .thenThrow(new RuntimeException("Internal Server Error"));

        mockMvc.perform(MockMvcRequestBuilders.multipart(GENERATE_URI).file(referenceFile).file(compareFile))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(Constants.JSON_STATUS, is(Constants.ERROR)))
                .andExpect(jsonPath(Constants.JSON_CODE, is(Constants.INTERNAL_SERVER_ERRCODE)))
                .andExpect(jsonPath(Constants.JSON_ERR_MSG, is(Matchers.any(String.class))))
                .andExpect(jsonPath(Constants.JSON_ERR_REF, is(Matchers.any(String.class))));
    }

    @Test
    void testGetMatchReportInternalServerError() throws Exception {
        when(reportController.getMatchReport(any(String.class)))
                .thenThrow(new RuntimeException("Internal Server Error"));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_MATCH_URI + "?rrn=1122334455"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(Constants.JSON_STATUS, is(Constants.ERROR)))
                .andExpect(jsonPath(Constants.JSON_CODE, is(Constants.INTERNAL_SERVER_ERRCODE)))
                .andExpect(jsonPath(Constants.JSON_ERR_MSG, is(Matchers.any(String.class))))
                .andExpect(jsonPath(Constants.JSON_ERR_REF, is(Matchers.any(String.class))));
    }

    @Test
    void testGetUnmatchReportInternalServerError() throws Exception {
        when(reportController.getUnMatchReport(any(String.class)))
                .thenThrow(new RuntimeException("Internal Server Error"));

        mockMvc.perform(MockMvcRequestBuilders.get(GET_UNMATCH_URI + "?rrn=1122334455"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(Constants.JSON_STATUS, is(Constants.ERROR)))
                .andExpect(jsonPath(Constants.JSON_CODE, is(Constants.INTERNAL_SERVER_ERRCODE)))
                .andExpect(jsonPath(Constants.JSON_ERR_MSG, is(Matchers.any(String.class))))
                .andExpect(jsonPath(Constants.JSON_ERR_REF, is(Matchers.any(String.class))));
    }
}
