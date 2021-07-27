package com.tutuka.transactionmatcher.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class MatchReport {

    private FileResult file1;
    private FileResult file2;
}
