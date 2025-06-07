package git.matheusoliveira04.api.fintrack.file.importer.impl;

import git.matheusoliveira04.api.fintrack.dto.request.EntryRequest;
import git.matheusoliveira04.api.fintrack.file.importer.contract.FileImporter;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Component
public class EntryXlsxImporter implements FileImporter<EntryRequest> {

    @Override
    public List<EntryRequest> importFile(InputStream inputStream) throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            Iterator<Row> rowIterator = workbook.getSheetAt(0).iterator();
            if (rowIterator.hasNext()) rowIterator.next();

            return parseRowsToEntryRequestList(rowIterator);
        }
    }

    private List<EntryRequest> parseRowsToEntryRequestList(Iterator<Row> rowIterator) {
        List<EntryRequest> entryRequests = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (isNotEmpty(row)) {
                entryRequests.add(parseRowsToEntryRequest(row));
            }
        }
        return entryRequests;
    }

    private EntryRequest parseRowsToEntryRequest(Row row) {
        return EntryRequest.builder()
                .description(row.getCell(0).getStringCellValue())
                .value(BigDecimal.valueOf(row.getCell(1).getNumericCellValue()))
                .date(LocalDate.parse(row.getCell(2).getStringCellValue()))
                .paid(Boolean.parseBoolean(row.getCell(3).getStringCellValue()))
                .categoryId(row.getCell(4).getStringCellValue())
                .build();
    }

    private static boolean isNotEmpty(Row row) {
        return Objects.nonNull(row.getCell(0)) && row.getCell(0).getCellType() != CellType.BLANK;
    }
}
