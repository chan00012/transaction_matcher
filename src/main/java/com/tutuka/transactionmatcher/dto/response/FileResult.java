package com.tutuka.transactionmatcher.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileResult {
    private String fileName;
    private int totalRecords;
    private int matchingRecords;
    private int unmatchedRecords;
}