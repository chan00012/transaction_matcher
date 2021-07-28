package com.tutuka.transactionmatcher.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchReport {

    private FileResult file1;
    private FileResult file2;
}
