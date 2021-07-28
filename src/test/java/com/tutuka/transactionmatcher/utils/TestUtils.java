package com.tutuka.transactionmatcher.utils;

import com.tutuka.transactionmatcher.entity.ReportEntity;
import com.tutuka.transactionmatcher.utils.enums.ReportStatus;
import org.apache.commons.io.IOUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TestUtils {

    public static File getFile(String filename) throws FileNotFoundException {
        return ResourceUtils.getFile("classpath:" + filename);
    }

    public static String readJson(String filename) throws IOException {
        File file = getFile(filename);
        InputStream is = new FileInputStream(file);
        return IOUtils.toString(is, StandardCharsets.UTF_8);
    }

    public static ReportEntity getReportEntityResponse(ReportStatus status) {
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setStatus(status);
        return reportEntity;
    }
}
