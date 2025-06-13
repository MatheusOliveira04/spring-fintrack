package git.matheusoliveira04.api.fintrack.file.importer.impl;

import git.matheusoliveira04.api.fintrack.dto.request.EntryRequest;
import git.matheusoliveira04.api.fintrack.file.importer.contract.FileImporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class EntryCsvImporter implements FileImporter<EntryRequest> {

    @Override
    public List<EntryRequest> importFile(InputStream inputStream) throws Exception {
        CSVFormat format = CSVFormat.Builder.create()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreEmptyLines(true)
                .setTrim(true)
                .build();

        return parseCsvParserToEntryRequests(format.parse(new InputStreamReader(inputStream)));
    }

    private List<EntryRequest> parseCsvParserToEntryRequests(CSVParser csvParser) {
        List<EntryRequest> requests = new ArrayList<>();

        csvParser.getRecords().forEach(record -> {
            requests.add(
                    EntryRequest.builder()
                        .description(record.get("description"))
                        .value(new BigDecimal(record.get("value")))
                        .date(LocalDate.parse(record.get("date")))
                        .paid(Boolean.parseBoolean(record.get("paid")))
                        .categoryId(record.get("category_id"))
                        .build()
            );
        });
        return requests;
    }
}
