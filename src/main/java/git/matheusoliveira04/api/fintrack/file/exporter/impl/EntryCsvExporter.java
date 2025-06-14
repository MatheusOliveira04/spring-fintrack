package git.matheusoliveira04.api.fintrack.file.exporter.impl;

import git.matheusoliveira04.api.fintrack.dto.response.EntryResponse;
import git.matheusoliveira04.api.fintrack.file.exporter.contract.FileExporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class EntryCsvExporter implements FileExporter<EntryResponse> {

    private static final String[] HEADERS = {"description", "value", "date", "paid", "category_id"};

    @Override
    public Resource exportFile(List<EntryResponse> list) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter write = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        CSVFormat csvFormat = createCsvFormat();

        writeCsv(list, write, csvFormat);
        return new ByteArrayResource(outputStream.toByteArray());
    }

    private static CSVFormat createCsvFormat() {
        return CSVFormat.Builder.create()
                .setHeader(HEADERS)
                .setSkipHeaderRecord(false)
                .get();
    }

    private static void writeCsv(List<EntryResponse> list, OutputStreamWriter write, CSVFormat csvFormat) throws IOException {
        try(CSVPrinter csvPrinter = new CSVPrinter(write, csvFormat)) {
            for (EntryResponse entry: list) {
                csvPrinter.printRecord(
                        entry.getDescription(),
                        entry.getValue(),
                        entry.getDate(),
                        entry.getPaid(),
                        entry.getCategory().getId()
                );
            }
        }
    }
}
