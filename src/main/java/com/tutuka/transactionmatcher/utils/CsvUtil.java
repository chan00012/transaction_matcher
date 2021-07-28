package com.tutuka.transactionmatcher.utils;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.tutuka.transactionmatcher.entity.Transaction;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CsvUtil {

    private CsvUtil() {}

    private static final CsvMapper mapper = new CsvMapper();

    public static List<Transaction> read(InputStream stream) throws IOException {
        mapper.enable(CsvParser.Feature.TRIM_SPACES);
        mapper.enable(CsvParser.Feature.SKIP_EMPTY_LINES);
        mapper.enable(CsvParser.Feature.IGNORE_TRAILING_UNMAPPABLE);
        mapper.disable(CsvParser.Feature.ALLOW_TRAILING_COMMA);
        AtomicInteger rowCounter = new AtomicInteger(2);
        CsvSchema schema = mapper.schemaFor(Transaction.class).withHeader().withColumnReordering(true);
        ObjectReader reader = mapper.readerFor(Transaction.class).with(schema);

        return reader.<Transaction>readValues(stream).readAll()
                .stream()
                .peek(t -> t.setRowNum(rowCounter.getAndIncrement()))
                .collect(Collectors.toList());
    }
}